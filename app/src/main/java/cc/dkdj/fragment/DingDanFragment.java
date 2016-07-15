package cc.dkdj.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dkdj.R;
import cc.dkdj.activity.LoginActivity;
import cc.dkdj.activity.MainActivity;
import cc.dkdj.base.BaseFragment;
import cc.dkdj.config.SaveKey;
import cc.dkdj.model.OrderList;
import cc.dkdj.view.CommonAdapter;
import cc.dkdj.view.CommonViewHolder;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

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

    @Override
    public void initViews() {
        setContentView(R.layout.frag_dingdan);
        map = new HashMap<>();
        no_beans = new ArrayList<>();
        have_beans = new ArrayList<>();
    }

    OrderList orderList;

    @Override
    public void initEvents() {
        mAdapter = new CommonAdapter<OrderList.OrderlistBean>(MainActivity.mInstance, no_beans, R.layout.item_order) {
            @Override
            public void convert(CommonViewHolder holder, OrderList.OrderlistBean orderList, int position) {
                holder.setGlideImage(R.id.shop_img_iv, orderList.getTogoPic());
                holder.setText(R.id.shop_name_tv, orderList.getTogoName());
                holder.setText(R.id.price_tv, "￥" + orderList.getTotalPrice());
                holder.setText(R.id.state_tv, "审核通过");
            }
        };
        main_lv.setAdapter(mAdapter);
        userId = Utils.ReadString(SaveKey.KEY_UserId);//获得当前用户ID
        if (userId.equals("")) {
            Utils.IntentPost(LoginActivity.class, new Utils.putListener() {
                @Override
                public void put(Intent intent) {
                    intent.putExtra("position", 1);
                }
            });
        } else {
            map.put("userid", userId);
            HttpUtils.loadJson("GetOrderListByUserId", map, new HttpUtils.LoadJsonListener() {
                @Override
                public void load(JSONObject obj) {
                    orderList = new Gson().fromJson(obj.toString(), OrderList.class);
                    for (int i = 0; i < orderList.getOrderlist().size(); i++) {
                        OrderList.OrderlistBean bean = orderList.getOrderlist().get(i);
                        if (bean.getSendstate().equals("3")) {
                            have_beans.add(bean);
                        } else {
                            no_beans.add(bean);
                        }
                    }
                    setClick(1);
                }
            });
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_success_btn:
                setClick(1);
                break;
            case R.id.have_success_btn:
                setClick(2);
                break;
        }
    }

    int position = 0;

    private void setClick(int btn) {
        no_order_tv.setVisibility(View.GONE);
        switch (btn) {
            case 1:
                if (position == 2 || position == 0) {
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
                    position = 1;
                }
                break;
            case 2:
                if (position == 1) {
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
                    position = 2;
                }
                break;
        }
    }
}
