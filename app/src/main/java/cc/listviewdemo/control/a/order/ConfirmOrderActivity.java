package cc.listviewdemo.control.a.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.Config;
import cc.listviewdemo.control.a.shanghu.SHDetailsActivity;
import cc.listviewdemo.model.Goods;
import cc.listviewdemo.model.OrderModel;
import cc.listviewdemo.model.PayResult;
import cc.listviewdemo.model.PayState;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.money.Util;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.SignUtils;
import cc.listviewdemo.view.Utils;

/**
 * 确认订单页面
 * Created by Administrator on 2016/7/8.
 */
public class ConfirmOrderActivity extends BaseActivity {
    public static ConfirmOrderActivity mInstance;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;
    @CodeNote(id = R.id.address_rl, click = "onClick")
    RelativeLayout address_rl;
    @CodeNote(id = R.id.address_tv)
    TextView address_tv;//地址名称
    @CodeNote(id = R.id.name_tv)
    TextView name_tv;//姓
    @CodeNote(id = R.id.tel_tv)
    TextView tel_tv;//手机号
    @CodeNote(id = R.id.choice_send_time_ll)
    LinearLayout choice_send_time_ll;//选择送达时间
    @CodeNote(id = R.id.zfb_cb)
    CheckBox zfb_cb;//支付宝选项
    @CodeNote(id = R.id.wx_cb)
    CheckBox wx_cb;//微信选项
    @CodeNote(id = R.id.zfb_ll, click = "onClick")
    LinearLayout zfb_ll;
    @CodeNote(id = R.id.wx_ll, click = "onClick")
    LinearLayout wx_ll;
    @CodeNote(id = R.id.good_list_lv)
    ListView good_list_lv;
    CommonAdapter<Goods> mAdapter;
    List<Goods> goods;
    @CodeNote(id = R.id.shop_name_tv)
    TextView shop_name_tv;//店铺名称
    @CodeNote(id = R.id.pack_price_tv)
    TextView pack_price_tv;//打包费
    @CodeNote(id = R.id.send_price_tv)
    TextView send_price_tv;//配送费
    @CodeNote(id = R.id.total_price_tv)
    TextView total_price_tv;
    @CodeNote(id = R.id.sure_lock_btn, click = "onClick")
    Button sure_lock_btn;
    double pack_price = 0;
    double total_price = 0;
    private static final int SDK_PAY_FLAG = 1;
    Map<String, String> map = new HashMap<>();
    OrderModel model;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_confirm_order);
    }

    @Override
    public void initEvents() {
        mInstance = this;
        goods = new ArrayList<>();
        title_name_tv.setText("我的购物车");
        model = (OrderModel) getIntent().getSerializableExtra("order");
        goods = model.getShopList().get(0).getItemList();//获得所有商品信息
        mAdapter = new CommonAdapter<Goods>(mInstance, goods, R.layout.item_good) {
            @Override
            public void convert(CommonViewHolder holder, Goods good, int position) {
                holder.setText(R.id.name_num_tv, good.getPName() + "*" + good.getPNum());
                holder.setText(R.id.total_price_tv, good.getCurrentprice() + "");
            }
        };
        good_list_lv.setAdapter(mAdapter);
        shop_name_tv.setText(model.getShopList().get(0).getShopName());
        for (Goods good : goods) {
            double packs = Double.parseDouble(good.getPNum()) * Double.parseDouble(good.getOwername());
            total_price += (Double.parseDouble(good.getCurrentprice()) * Integer.parseInt(good.getPNum())) + packs;//商品价格+打包费
            pack_price += packs;
        }
        pack_price_tv.setText("￥" + (pack_price) + "");//打包费
        send_price_tv.setText("￥" + model.getShopList().get(0).getSendMoney());//配送费
        total_price = total_price + Double.parseDouble(model.getShopList().get(0).getSendMoney());//配送费
        total_price_tv.setText("￥" + total_price);
    }

    String orderid = "";//订单编号

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv://关闭当前页面
                mInstance.finish();
                break;
            case R.id.address_rl://点击进入选择地址页面
                ToastShort("敬请期待...");
                break;
            case R.id.zfb_ll://点击选择支付宝
                zfb_cb.setChecked(true);
                wx_cb.setChecked(false);
                break;
            case R.id.wx_ll://点击微信
                zfb_cb.setChecked(false);
                wx_cb.setChecked(true);
                break;
            case R.id.sure_lock_btn://调用支付接口
                if (TextUtils.isEmpty(Config.PARTNER) || TextUtils.isEmpty(Config.RSA_PRIVATE) || TextUtils.isEmpty(Config.SELLER)) {
                    new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    finish();
                                }
                            }).show();
                    return;
                }
                List<OrderModel> orderModels = new ArrayList<>();
                orderModels.add(model);
                String json = new Gson().toJson(orderModels);
                map.put("ordermodel", json);

                HttpUtils.loadJson("SubmitOrder", map, new HttpUtils.LoadJsonListener() {
                    @Override
                    public void load(JSONObject obj) {
                        if (obj != null) {
                            state = new Gson().fromJson(obj.toString(), PayState.class);
                            if (state.getOrderstate().equals("1")) {
                                String orderInfo = getOrderInfo("大可到家Android支付", "body", total_price + "");
                                String sign = sign(orderInfo);
                                try {
                                    sign = URLEncoder.encode(sign, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * 完整的符合支付宝参数规范的订单信息
                                 */
                                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(ConfirmOrderActivity.this);
                                        // 调用支付接口，获取支付结果
                                        String result = alipay.pay(payInfo, true);
                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            } else {
                                ToastShort("预订单生成失败...");
                            }
                        }
                    }
                });
                break;
        }
    }

    PayState state;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        mInstance.finish();//关闭当前页面
                        SHDetailsActivity.mInstance.finish();//关闭商户页面
                        /*后期需要实现跳转到订单相信页面，是支付失败还是再来一单*/
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ConfirmOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            mInstance.finish();//关闭当前页面
                            SHDetailsActivity.mInstance.finish();//关闭商户页面
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Config.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Config.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Config.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
