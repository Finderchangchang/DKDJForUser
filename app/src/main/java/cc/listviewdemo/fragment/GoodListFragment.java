package cc.listviewdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.TotalListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.GoodDetailActivity;
import cc.listviewdemo.activity.LoginActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.activity.ConfirmOrderActivity;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.control.GoodListListener;
import cc.listviewdemo.control.IFGoodListView;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodType;
import cc.listviewdemo.model.Goods;
import cc.listviewdemo.model.OrderModel;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopListModel;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.GoodsClassifyAdapter;
import cc.listviewdemo.view.GoodsDetailsAdapter;
import cc.listviewdemo.view.ListSortUtil;
import cc.listviewdemo.view.Utils;

/**
 * 商品列表
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class GoodListFragment extends BaseFragment implements IFGoodListView {
    @CodeNote(id = R.id.parentlist)
    ListView left_lv;
    @CodeNote(id = R.id.childlist)
    ListView right_lv;
    @CodeNote(id = R.id.billing, click = "onClick")
    TextView billing;
    List<FoodType> mLeft;
    List<FoodDetail> mRight1;
    GoodsClassifyAdapter mLefts;//商品分类
    @CodeNote(id = R.id.price_main)
    TextView price_main;
    @CodeNote(id = R.id.total_num_tv)
    TextView total_num_tv;
    @CodeNote(id = R.id.pei_song_tv)
    TextView pei_song_tv;
    GoodsDetailsAdapter detailsAdapter;//商品列表
    GoodListListener mListener;//调用访问后台数据接口
    CommonAdapter<Goods> mAdapter;
    Shop shop = SHDetailsActivity.mInstance.shop;
    String shopId;//店铺ID
    String userId;
    TotalListView gouwuche_lv;
    @CodeNote(id = R.id.gwc_rl, click = "onClick")
    RelativeLayout gwc_rl;
    private PopupWindow pop_win;
    Button pop_billing;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_goodlist);
        shopId = shop.getDataID();
        mLeft = new ArrayList<>();
        mRight1 = new ArrayList<>();
        shops = new ArrayList<>();
        goods = new ArrayList<>();
        mTotalList = new ArrayList<>();
        mTag = new HashMap<>();
    }

    RelativeLayout clear_gwc_rl;
    LinearLayout top_total_ll;

    /**
     * 显示popwindow的顶部显示消息的内容
     *
     * @author 柳伟杰
     * created at 2016/1/30 14:20
     */
    private void showPopUp(View v) {
        Rect frame = new Rect();
        SHDetailsActivity.mInstance.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        LayoutInflater layoutInflater = (LayoutInflater) SHDetailsActivity.mInstance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_gwc, null);
        gouwuche_lv = (TotalListView) view.findViewById(R.id.gouwuche_lv);
        clear_gwc_rl = (RelativeLayout) view.findViewById(R.id.clear_gwc_rl);
        top_total_ll = (LinearLayout) view.findViewById(R.id.top_total_ll);
        top_total_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_win.dismiss();//关闭当前页面
            }
        });
        pop_billing = (Button) view.findViewById(R.id.billing);
        pop_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToOrder();
                pop_win.dismiss();
            }
        });
        pop_win = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        gouwuche_lv.setAdapter(mAdapter);
        clear_gwc_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_win.dismiss();//关闭popwindow
                calculateTotalPrice(0, 0, 0, null);
                detailsAdapter.refresh(goods);//清空列表显示
            }
        });
        pop_win.setFocusable(true);
        pop_win.setOutsideTouchable(true);
        pop_win.setBackgroundDrawable(new BitmapDrawable());
        pop_win.showAtLocation(v, Gravity.TOP, 0, frame.top);
    }

    @Override
    public void initEvents() {
        mListener = new GoodListListener(this, SHDetailsActivity.mInstance.shop.getDataID());
        mListener.loadType();//加载分类
        mListener.loadFoods();//加载出所有的Goods
        mLefts = new GoodsClassifyAdapter(SHDetailsActivity.mInstance, mLeft);
        left_lv.setAdapter(mLefts);
        left_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = mTag.get(mLeft.get(position).getSortID());
                if (tag != null) {
                    final int seleId = Integer.parseInt(tag);
                    right_lv.setSelection(seleId);
                    right_lv.post(new Runnable() {
                        public void run() {
                            right_lv.setSelection(seleId);
                        }
                    });
                }
                mLefts.clearSelection(position);
            }
        });
        billing.setBackgroundColor(getResources().getColor(R.color.smallLab));
        billing.setText("￥" + shop.getMinmoney() + "起送");
        pei_song_tv.setText("配送费￥" + shop.getSendmoney());
        mAdapter = new CommonAdapter<Goods>(SHDetailsActivity.mInstance, goods, R.layout.item_gwc_good) {
            @Override
            public void convert(CommonViewHolder holder, final Goods goods, int position) {
                holder.setText(R.id.name_tv, goods.getPName());
                holder.setText(R.id.price_tv, Integer.parseInt(goods.getPNum()) * Double.parseDouble(goods.getCurrentprice()));
                holder.setOnClickListener(R.id.jian_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calculateTotalPrice(-1, -Double.parseDouble(goods.getCurrentprice()), -Double.parseDouble(goods.getPackageFree()), goods);
                    }
                });
                holder.setOnClickListener(R.id.add_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calculateTotalPrice(1, Double.parseDouble(goods.getCurrentprice()), Double.parseDouble(goods.getPackageFree()), goods);
                    }
                });
                holder.setText(R.id.num_tv, goods.getPNum());
            }
        };
    }

    List<ShopListModel> shops;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gwc_rl:
                if (goods.size() > 0) {
                    showPopUp(gwc_rl);
                }
                break;
            case R.id.billing://结算按钮
                GoToOrder();
                break;
        }
    }

    private void GoToOrder() {
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        if (!userId.equals("")) {
            if (count > 0) {
                shop.setGoodses(goods);
                shops.add(new ShopListModel(shop.getLng(), shop.getLat(), shop.getDataID(), shop.getSendmoney(), shop.getTogoName(), goods));//添加商品到商户，再将商户信息添加到shop集合中。
                Utils.IntentPost(ConfirmOrderActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        OrderModel model = new OrderModel(userId,
                                Utils.getNormalTime(), "", shop.getDataID(), "2", shops);
                        intent.putExtra("order", model);
                    }
                });
            } else {
                SHDetailsActivity.mInstance.ToastShort("购物车不能为空");
            }
        } else {
            Utils.IntentPost(LoginActivity.class, new Utils.putListener() {
                @Override
                public void put(Intent intent) {
                    intent.putExtra("isGWC", true);
                }
            });
        }
    }

    List<Goods> goods;
    double price = 0;
    int count = 0;

    /**
     * 计算总价与选中的个数,并显示在界面上
     *
     * @param num     购物车商品数量
     * @param p       总钱数
     * @param peisong 配送费
     * @param good    商品信息
     */
    public void calculateTotalPrice(int num, double p, double peisong, Goods good) {
        count = count + num;
        price = price + p;
        if (num == 1) {
            addGood(good);
        } else if (num == -1) {
            removeGood(good);
        } else {
            goods = new ArrayList<>();
            price = 0;
            count = 0;
        }
        mAdapter.refresh(goods);
        detailsAdapter.refresh(goods);
        price_main.setText("￥" + Math.round(price * 100) / 100.0);
        if (price >= Double.parseDouble(shop.getMinmoney())) {
            billing.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            billing.setText("去结算");
        } else {
            billing.setBackgroundColor(getResources().getColor(R.color.smallLab));
            billing.setText("￥" + shop.getMinmoney() + "起送");
        }
        if (count > 0) {
            total_num_tv.setText(count + "");
            total_num_tv.setVisibility(View.VISIBLE);
        } else {
            total_num_tv.setVisibility(View.GONE);
        }
    }

    /**
     * 在集合中添加Goods
     *
     * @param good
     */
    public void addGood(Goods good) {
        if (goods == null) {
            goods = new ArrayList<>();
        }
        if (check(good)) {
            good.setPNum((Integer.parseInt(goods.get(position).getPNum()) + 1) + "");
            goods.remove(position);
            goods.add(position, good);
        } else {
            good.setPNum("1");
            goods.add(good);
        }
    }

    int position = -1;

    /**
     * 判断集合中是否存在该Good
     *
     * @param good
     * @return true
     */
    private boolean check(Goods good) {
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getSid() == good.getSid()) {
                position = i;
                return true;
            }
        }
        return false;
    }

    public void removeGood(Goods good) {
        if (check(good)) {
            int nu = Integer.parseInt(goods.get(position).getPNum()) - 1;
            if (nu == 0) {
                goods.remove(position);
            } else {
                good.setPNum(nu + "");
                if (Integer.parseInt(goods.get(position).getPNum()) >= 0) {
                    goods.remove(position);
                    goods.add(position, good);
                }
            }
            if (goods.size() == 0) {
                if (pop_win != null && pop_win.isShowing()) {
                    pop_win.dismiss();
                }
            }

        }
    }

    /**
     * 加载商品分类列表
     *
     * @param mList
     */
    @Override
    public void loadGoodType(List<FoodType> mList) {
        new ListSortUtil<FoodType>().sort(mList, "SortID", "asc");
        mLefts.refresh(mList);
        mLeft = mList;
    }

    List<FoodDetail> mTotalList;
    Map<String, String> mTag;

    /**
     * 加载出所有的商品列表
     *
     * @param mList
     */
    @Override
    public void loadGoodDetails(final List<FoodDetail> mList) {
        new ListSortUtil<FoodDetail>().sort(mList, "FoodType", "asc");
        mTag = getSortID(mList);
        detailsAdapter = new GoodsDetailsAdapter(this, mList);
        right_lv.setAdapter(detailsAdapter);
        right_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(GoodDetailActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("detail", mList.get(position));
                    }
                });
            }
        });
    }

    /**
     * 获得List中的model分类
     */
    private Map getSortID(List<FoodDetail> mList) {
        Map<String, String> map = new HashMap<>();
        String result = "";
        for (int i = 0; i < mList.size(); i++) {
            if (result.equals("")) {
                map.put(mList.get(i).getFoodType(), i + "");
            } else {
                if (!result.equals(mList.get(i).getFoodType())) {
                    map.put(mList.get(i).getFoodType(), i + "");
                }
            }
        }
        return map;
    }
}
