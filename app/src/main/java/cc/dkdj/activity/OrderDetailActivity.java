package cc.dkdj.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.config.Config;
import cc.dkdj.config.SaveKey;
import cc.dkdj.model.Good;
import cc.dkdj.model.OrderDetailModel;
import cc.dkdj.model.OrderGood;
import cc.dkdj.model.OrderList;
import cc.dkdj.model.OrderModel;
import cc.dkdj.model.PayResult;
import cc.dkdj.model.PayState;
import cc.dkdj.view.CommonAdapter;
import cc.dkdj.view.CommonViewHolder;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.SignUtils;
import cc.dkdj.view.Utils;

/**
 * 订单详细信息
 * 作者：lwj on 2016/7/10 20:15
 * 邮箱：1031066280@qq.com
 */
public class OrderDetailActivity extends BaseActivity {
    public static OrderDetailActivity mInstance;
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;
    @CodeNote(id = R.id.tv_shopName)
    TextView shopname;
    @CodeNote(id = R.id.tv_packfee)
    TextView tv_packfee;
    @CodeNote(id = R.id.tv_sentmoney)
    TextView tv_sentmoney;
    @CodeNote(id = R.id.mEtSum)
    TextView mEtSum;
    @CodeNote(id = R.id.etOrderNum)
    TextView etOrderNum;
    @CodeNote(id = R.id.etUserName)
    TextView etUserName;


    @CodeNote(id = R.id.etphone)
    TextView etphone;
    @CodeNote(id = R.id.etAddress)
    TextView etAddress;
    @CodeNote(id = R.id.etOrderType)
    TextView etOrderType;
    @CodeNote(id = R.id.etRemark)
    TextView etRemark;
    @CodeNote(id = R.id.order_detail_lv)
    ListView lv;
    @CodeNote(id = R.id.mBtnDoorder, click = "onClick")
    TextView mBtnDoorder;
    Map<String, String> map;
    String key = "";
    OrderDetailModel model;
    CommonAdapter<String> mAdapter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_order_detail);
        map = new HashMap<>();
        title_name_tv.setText("订单详情");
        key = getIntent().getStringExtra("orderId");
        goods = new ArrayList<>();
        mInstance = this;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnDoorder:
                if (mBtnDoorder.getText().equals("去支付")) {
                    map = new HashMap<String, String>();
                    map.put("orderid", key);
                    map.put("price", model.getTotalPrice());
                    HttpUtils.loadJson("buildpaynum", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                String orderInfo = getOrderInfo("大可到家Android支付", "body", model.getTotalPrice() + "", obj.getString("batch"));
                                String sign = sign(orderInfo);
                                try {
                                    sign = URLEncoder.encode(sign, "UTF-8");
                                } catch (UnsupportedEncodingException e) {

                                }
                                /**
                                 * 完整的符合支付宝参数规范的订单信息
                                 */
                                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(OrderDetailActivity.this);
                                        // 调用支付接口，获取支付结果
                                        String result = alipay.pay(payInfo, true);
                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };

//             必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            } catch (JSONException e) {

                            }
                        }
                    });
                } else {
                    ToastShort("敬请期待...");
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        mInstance.finish();//关闭当前页面
                        SHDetailsActivity.mInstance.finish();//关闭商户页面
                        if(MainActivity.mInstance!=null){
                            MainActivity.mInstance.finish();
                            Utils.IntentPost(MainActivity.class, new Utils.putListener() {
                                @Override
                                public void put(Intent intent) {
                                    intent.putExtra("position",1);
                                }
                            });
                        }
                        /*后期需要实现跳转到订单相信页面，是支付失败还是再来一单*/
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
    private String getOrderInfo(String subject, String body, String price, String orderid) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Config.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Config.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderid + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.dakedaojia.com/Alipay/iosnotify.aspx" + "\"";

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

    List<OrderGood> goods;
    List<String> list = new ArrayList<String>();

    @Override
    public void initEvents() {
        map.put("orderid", key);
        HttpUtils.loadJson("GetOrderDetailByOrderId", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                model = new Gson().fromJson(obj.toString(), OrderDetailModel.class);
                shopname.setText(model.getTogoName());
                tv_packfee.setText(model.getPackagefree());
                tv_sentmoney.setText(model.getSendmoney());
                mEtSum.setText(model.getTotalPrice());
                etOrderNum.setText(model.getOrderID());
                etUserName.setText(model.getUserName());
                etphone.setText(model.getTel());
                etAddress.setText(model.getAddress());
                if (model.getPaystate().equals("1")) {
                    etOrderType.setText("支付宝");
                } else {
                    etOrderType.setText("微信");
                }
                String foods = model.getFoodlist().get(0);

                for (int i = 0; i < foods.split("]").length; i++) {
                    String name = foods.split("]")[i].split(":")[5].split(",")[0].replace("\"", "");
                    String num = foods.split("]")[i].split(":")[1].split(",")[0];
                    String price = foods.split("]")[i].split(":")[3].split(",")[0];
                    list.add(name + "*" + num + ":" + price);
                }
                etRemark.setText(model.getRemark());
                mAdapter = new CommonAdapter<String>(OrderDetailActivity.this, list, R.layout.item_good) {
                    @Override
                    public void convert(CommonViewHolder holder, String str, int position) {
                        holder.setText(R.id.name_num_tv, str.split(":")[0]);
                        holder.setText(R.id.total_price_tv, str.split(":")[1]);
                    }
                };
                lv.setAdapter(mAdapter);
                if (!model.getPaymoney().equals("0.00")) {
                    mBtnDoorder.setText("催单");
                } else {
                    mBtnDoorder.setText("去支付");
                }
            }
        });
    }
}
