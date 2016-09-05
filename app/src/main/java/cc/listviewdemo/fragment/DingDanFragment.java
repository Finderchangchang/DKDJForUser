package cc.listviewdemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.OrderDetailActivity;
import cc.listviewdemo.activity.TelAndLoginActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.OrderList;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 我的订单
 * 作者：lwj on 2016/7/3 15:38
 * 邮箱：1031066280@qq.com
 */
public class DingDanFragment extends BaseFragment {
    String userId;
    @CodeNote(id = R.id.no_success_btn, click = "onClick")
    Button no_success_btn;
    @CodeNote(id = R.id.have_success_btn, click = "onClick")
    Button have_success_btn;
    @CodeNote(id = R.id.main_lv)
    ListView main_lv;
    CommonAdapter<OrderList.OrderlistBean> mAdapter;
    Map<String, String> map;
    List<OrderList.OrderlistBean> no_beans;
    List<OrderList.OrderlistBean> have_beans;
    @CodeNote(id = R.id.no_order_tv)
    TextView no_order_tv;
    @CodeNote(id = R.id.order_list_ll)
    LinearLayout order_list_ll;
    @CodeNote(id = R.id.no_login_ll)
    LinearLayout no_login_ll;
    @CodeNote(id = R.id.order_login_btn, click = "onClick")
    Button order_login_btn;
    boolean isComplement = false;
    ProgressDialog progressDialog = null;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_dingdan);
        no_beans = new ArrayList<>();
        have_beans = new ArrayList<>();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//显示
            initDingDan();
        }
    }

    OrderList orderList;

    @Override
    public void initEvents() {
        mAdapter = new CommonAdapter<OrderList.OrderlistBean>(MainActivity.mInstance, no_beans, R.layout.item_order_small_list) {
            @Override
            public void convert(CommonViewHolder holder, OrderList.OrderlistBean orderlistBean, int position) {
                holder.setListView(R.id.foodlist_listView, orderlistBean.getFoodlist());
                holder.setText(R.id.shop_name_tv, orderlistBean.getTogoName());
                holder.setCycleGlideImage(R.id.shop_img_iv, orderlistBean.getTogoPic());
                holder.setText(R.id.goods_num_tv, orderlistBean.getFoodlist().size());
                double money = Double.parseDouble(orderlistBean.getTotalPrice()) - Double.parseDouble(orderlistBean.getYouHui());
                holder.setText(R.id.total_money_tv, money + "元");
                String state = "未支付";
                if (orderlistBean.getPaystate().equals("0")) {
                    state = "未支付";
                } else {
                    switch (orderlistBean.getSendstate()) {
                        case "0":
                            if (orderlistBean.getState().equals("2")) {
                                if (orderlistBean.getIsShopSet().equals("0")) {
                                    state = "订单已提交";
                                } else {
                                    state = "商家已接单";
                                }
                            } else if (orderlistBean.getState().equals("4")) {
                                state = "订单已取消";//商家拒接此单
                            } else if (orderlistBean.getState().equals("7")) {
                                state = "正在匹配骑手";
                            } else {
                                state = "订单已取消";
                            }
                            break;
                        case "1":
                            state = "骑手去商家取货";
                            break;
                        case "2":
                            state = "已取货，配送中";
                            break;
                        case "3":
                            state = "已送达";
                            break;
                    }
                }
                holder.setText(R.id.shop_state_tv, state);
            }
        };
        main_lv.setAdapter(mAdapter);
        main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.mInstance, OrderDetailActivity.class);
                if (isComplement) {
                    intent.putExtra("orderId", have_beans.get(position).getOrderID());
                    intent.putExtra("normalPrice", have_beans.get(position).getYouHui());
                } else {
                    intent.putExtra("orderId", no_beans.get(position).getOrderID());
                    intent.putExtra("normalPrice", no_beans.get(position).getYouHui());
                }
                startActivityForResult(intent, 1);
            }
        });
        initDingDan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initDingDan();
        }
    }

    private void initDingDan() {
        if (!Utils.ReadString(SaveKey.KEY_UserId).equals("")) {
            userId = Utils.ReadString(SaveKey.KEY_UserId);//获得当前用户ID
            map = new HashMap<>();
            map.put("userid", userId);
            map.put("pageindex", "1");
            map.put("pagesize", "20");
            no_beans = new ArrayList<>();
            have_beans = new ArrayList<>();
            progressDialog = ProgressDialog.show(MainActivity.mInstance, "",
                    "加载中...");
            HttpUtils.loadJson("GetOrderListByUserId", map, new HttpUtils.LoadJsonListener() {
                @Override
                public void load(JSONObject obj) {
                    if (obj != null) {
                        orderList = new Gson().fromJson(obj.toString(), OrderList.class);
                        for (int i = 0; i < orderList.getOrderlist().size(); i++) {
                            OrderList.OrderlistBean bean = orderList.getOrderlist().get(i);
                            if (bean.getState().equals("3")) {
                                have_beans.add(bean);
                            } else {
                                no_beans.add(bean);
                            }
                        }
                        setClick(1);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                    }
                }
            });
            order_list_ll.setVisibility(View.VISIBLE);
            no_login_ll.setVisibility(View.GONE);
        } else {
            order_list_ll.setVisibility(View.GONE);
            no_login_ll.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_success_btn:
                setClick(1);
                isComplement = false;
                break;
            case R.id.have_success_btn:
                setClick(2);
                isComplement = true;
                break;
            case R.id.order_login_btn://跳转到登录页面
                Intent intent = new Intent(MainActivity.mInstance, TelAndLoginActivity.class);
                startActivityForResult(intent, 21);
                break;
        }
    }


    private void setClick(int btn) {
        no_order_tv.setVisibility(View.GONE);
        switch (btn) {
            case 1:
                if (no_beans.size() > 0) {
                    mAdapter.refresh(no_beans);
                    no_order_tv.setVisibility(View.GONE);
                    main_lv.setVisibility(View.VISIBLE);
                } else {
                    no_order_tv.setVisibility(View.VISIBLE);
                    main_lv.setVisibility(View.GONE);
                }
                no_success_btn.setTextColor(getResources().getColor(R.color.white));
                no_success_btn.setBackgroundResource(R.drawable.order_btn_pressed);
                have_success_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                have_success_btn.setBackgroundResource(R.drawable.order_btn_normal);
                mAdapter.refresh(no_beans);
                break;
            case 2:
                if (have_beans.size() > 0) {
                    mAdapter.refresh(have_beans);
                    no_order_tv.setVisibility(View.GONE);
                    main_lv.setVisibility(View.VISIBLE);
                } else {
                    no_order_tv.setVisibility(View.VISIBLE);
                    main_lv.setVisibility(View.GONE);
                }
                have_success_btn.setTextColor(getResources().getColor(R.color.white));
                have_success_btn.setBackgroundResource(R.drawable.order_btn_pressed);
                no_success_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                no_success_btn.setBackgroundResource(R.drawable.order_btn_normal);
                mAdapter.refresh(have_beans);
                break;
        }
    }
}

