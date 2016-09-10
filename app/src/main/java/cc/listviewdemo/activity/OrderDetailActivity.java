package cc.listviewdemo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.Config;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.ActivityModel;
import cc.listviewdemo.model.FoodDescModel;
import cc.listviewdemo.model.Goods;
import cc.listviewdemo.model.OrderDetailModel;
import cc.listviewdemo.model.OrderGood;
import cc.listviewdemo.model.PayResult;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.LinearLayoutForListView;
import cc.listviewdemo.view.SignUtils;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;
import cc.listviewdemo.view.WxUtil;

/**
 * 订单详细信息
 * 作者：lwj on 2016/7/10 20:15
 * 邮箱：1031066280@qq.com
 */
public class OrderDetailActivity extends BaseActivity {
    public static OrderDetailActivity mInstance;
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
    @CodeNote(id = R.id.mBtnDoorder, click = "onClick")
    TextView mBtnDoorder;
    @CodeNote(id = R.id.cancel_tv, click = "onClick")
    TextView cancel_tv;
    Map<String, String> map;
    String key = "";
    OrderDetailModel model;
    CommonAdapter<String> mAdapter;
    @CodeNote(id = R.id.main_tb)
    TitleBar main_tb;
    @CodeNote(id = R.id.activity_tag_lv)
    LinearLayoutForListView activity_tag_lv;
    CommonAdapter<ActivityModel> mActivityAdapter;
    List<ActivityModel> mList;
    @CodeNote(id = R.id.good_list_lv)
    LinearLayoutForListView good_list_lv;
    String youhui;
    @CodeNote(id = R.id.activity_line_view)
    View activity_line_view;
    Dialog dialog = null;//弹出框
    ProgressDialog progressDialog;//加载进度条

    @Override
    public void initViews() {
        Utils.WriteString(SaveKey.KEY_Load_Index, "1");
        setContentView(R.layout.activity_order_detail);
        map = new HashMap<>();
        mList = new ArrayList<>();
        key = getIntent().getStringExtra("orderId");
        youhui = getIntent().getStringExtra("normalPrice");
        goods = new ArrayList<>();
        mInstance = this;
        main_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
        progressDialog = ProgressDialog.show(MainActivity.mInstance, "",
                "加载中...");
    }

    class MyThread extends Thread {
        String Order_Id = "";
        int Order_Price;

        public MyThread(String orderId, String price) {
            Order_Id = orderId;
            Order_Price = (int) (Double.parseDouble(price) * 100);
        }

        public void run() {
            WxUtil.load(mInstance, "大可到家", "大可到家Android支付", Order_Id, Order_Price);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                if (model.getPaystate().equals("0")) {
                    OrderDetailActivity.mInstance.ToastShort("未支付订单不能取消");
                } else {
                    switch (model.getIsShopSet()) {
                        case "0":
                            AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                                    .setTitle("提示").setMessage("确定要取消当前订单？");

                            dialog = localBuilder1.setPositiveButton("返回", null)
                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            map = new HashMap<>();
                                            map.put("orderid", model.getOrderID());
                                            map.put("state", "2");
                                            map.put("shopid", model.getTogoId());
                                            HttpUtils.loadJson("/App/shop/saveorderstate", map, new HttpUtils.LoadJsonListener() {
                                                @Override
                                                public void load(JSONObject obj) {
                                                    try {
                                                        if (!obj.getString("state").equals("0")) {
                                                            OrderDetailActivity.mInstance.ToastShort("取消成功");
                                                        } else {
                                                            OrderDetailActivity.mInstance.ToastShort("取消失败");
                                                        }
                                                    } catch (JSONException e) {

                                                    }
                                                }
                                            });
                                        }
                                    }).create();
                            dialog.show();
                            break;
                        case "1":
                            OrderDetailActivity.mInstance.ToastShort("配送小哥正在路上，请耐心等待");
                            break;
                        case "2":
                            OrderDetailActivity.mInstance.ToastShort("订单已经取消，请耐心等待回款");
                            break;
                    }
                }
                break;
            case R.id.mBtnDoorder:
                if (mBtnDoorder.getText().equals("去支付")) {
                    if (model.getIsfirst().equals("False")) {
                        mInstance.ToastShort("订单已过期，请重新下单");
                    } else {
                        map = new HashMap<String, String>();
                        map.put("orderid", key);
                        map.put("price", normal_price + "");
                        HttpUtils.loadJson("buildpaynum", map, new HttpUtils.LoadJsonListener() {
                            @Override
                            public void load(JSONObject obj) {
                                try {
                                    String path = obj.getString("batch");//获得OrderID
                                    if (model.getPayMode().equals("1")) {
                                        String orderInfo = getOrderInfo("大可到家Android支付", "body", model.getTotalPrice() + "", path);
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
                                        Thread payThread = new Thread(payRunnable);
                                        payThread.start();
                                    } else {
                                        new MyThread(path, model.getTotalPrice()).start();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        });
                    }
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
                        setResult(1);
                        mInstance.finish();//关闭当前页面
                        if (SHDetailsActivity.mInstance != null) {
                            SHDetailsActivity.mInstance.finish();//关闭商户页面
                        }
                        /*后期需要实现跳转到订单相信页面，是支付失败还是再来一单*/
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            mInstance.finish();//关闭当前页面
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
     * 关闭当前页面
     */
    public static void closeThis() {
        mInstance.setResult(1);
        mInstance.finish();//关闭当前页面
    }

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
    double normal_price;

    @Override
    public void initEvents() {
        map.put("orderid", key);
        progressDialog.show();
        HttpUtils.loadJson("GetOrderDetailByOrderId", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                if (obj != null) {
                    model = new Gson().fromJson(obj.toString(), OrderDetailModel.class);
                    shopname.setText(model.getTogoName());
                    tv_packfee.setText(model.getPackagefree());
                    tv_sentmoney.setText(model.getSendmoney());
                    normal_price = (Double.parseDouble(model.getTotalPrice()) - Double.parseDouble(youhui));
                    mEtSum.setText(normal_price + "");
                    etOrderNum.setText(model.getOrderID());
                    etUserName.setText(model.getUserName());
                    etphone.setText(model.getTel());
                    String[] address = model.getAddress().split("|");
                    if (address.length == 3) {
                        etAddress.setText(address[1] + address[2]);
                    } else {
                        etAddress.setText(model.getAddress().replace("|", ""));
                    }
                    if (model.getPayMode().equals("1")) {
                        etOrderType.setText("支付宝");
                    } else {
                        etOrderType.setText("微信");
                    }
                    if (model.getFoodlist().size() > 0) {
                        for (FoodDescModel foods : model.getFoodlist()) {
                            list.add(foods.getFoodname() + "*" + foods.getNum() + ":" + foods.getFoodPrice());
                        }
                    }
                    etRemark.setText(model.getRemark());
                    mAdapter = new CommonAdapter<String>(OrderDetailActivity.this, list, R.layout.item_good_list) {
                        @Override
                        public void convert(CommonViewHolder holder, String str, int position) {
                            holder.setText(R.id.name_num_tv, str.split(":")[0]);
                            holder.setText(R.id.total_price_tv, str.split(":")[1]);
                        }
                    };
                    good_list_lv.setAdapter(mAdapter);
                    if (!model.getPaymoney().equals("0.00")) {
                        mBtnDoorder.setText("催单");
                    } else {
                        mBtnDoorder.setText("去支付");
                    }
                    if (model.getHuoDong().size() > 0) {
                        List<ActivityModel> huodong = (List<ActivityModel>) model.getHuoDong();
                        mActivityAdapter = new CommonAdapter<ActivityModel>(mInstance, huodong, R.layout.item_activity_tag) {
                            @Override
                            public void convert(CommonViewHolder holder, ActivityModel activityModel, int position) {
                                holder.setGlideImage(R.id.activity_tag_iv, activityModel.get活动图片());
                                holder.setText(R.id.activity_desc_tv, activityModel.get活动名称());
                                if (!("0").equals(activityModel.get优惠())) {
                                    holder.setText(R.id.youhui_price_tv, "-￥" + activityModel.get优惠());
                                } else {
                                    holder.setVisible(R.id.youhui_price_tv, false);
                                }
                            }
                        };
                    } else {
                        activity_line_view.setVisibility(View.GONE);
                    }
                    activity_tag_lv.setAdapter(mActivityAdapter);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }
}
