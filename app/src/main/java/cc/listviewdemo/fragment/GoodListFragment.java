package cc.listviewdemo.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

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
    Shop shop = SHDetailsActivity.mInstance.shop;
    String shopId;//店铺ID
    String userId;
    @CodeNote(id = R.id.gouwuche_lv)
    ListView gouwuche_lv;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_goodlist);
        shopId = shop.getDataID();
        mLeft = new ArrayList<>();
        mRight1 = new ArrayList<>();
        shops = new ArrayList<>();
        goods = new ArrayList<>();
        mTotalList=new ArrayList<>();
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
                final int seleId = Integer.parseInt(mTag.get(mLeft.get(position).getSortID()));
                String s = "";
                right_lv.setSelection(seleId);
                right_lv.post(new Runnable() {
                    public void run() {
                        right_lv.setSelection(seleId);
                    }
                });
                mLefts.clearSelection(position);
            }
        });
        billing.setBackgroundColor(getResources().getColor(R.color.smallLab));
        billing.setText("￥" + shop.getMinmoney() + "起送");
        pei_song_tv.setText("配送费￥" + shop.getSendmoney());
    }

    List<ShopListModel> shops;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.billing://结算按钮
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
                break;
        }
    }

    List<Goods> goods;
    double price = 0;
    int count = 0;

    /**
     * 计算总价与选中的个数,并显示在界面上
     *
     * @param num 购物车商品数量
     * @param p   总钱数
     */
    public void calculateTotalPrice(int num, double p, Goods good) {
        count = count + num;
        price = price + p;
        price_main.setText("￥" + Math.round(price * 100) / 100.0);
        if (num == 1) {
            addGood(good);
        } else {
            removeGood(good);
        }
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
     * 存在该id
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
        }
    }

    /**
     * 加载商品分类列表
     * @param mList
     */
    @Override
    public void loadGoodType(List<FoodType> mList) {
        mLeft = mList;
        mLefts.refresh(mList);
    }
    List<FoodDetail> mTotalList;//
    Map<String,String> mTag;
    /**
     * 加载出所有的商品列表
     * @param mList
     */
    @Override
    public void loadGoodDetails(final List<FoodDetail> mList) {
        new ListSortUtil<FoodDetail>().sort(mList, "FoodType", "asc");
        mTag=getSortID(mList);
        detailsAdapter = new GoodsDetailsAdapter(this, mList);
        right_lv.setAdapter(detailsAdapter);
        right_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(GoodDetailActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("detail",mList.get(position));
                    }
                });
            }
        });
    }

    /**
     * 获得List中的model分类
     */
    private Map getSortID(List<FoodDetail> mList){
        Map<String,String> map=new HashMap<>();
        String result="";
        for(int i=0;i<mList.size();i++){
            if(result.equals("")){
                map.put(mList.get(i).getFoodType(),i+"");
            }else{
                if(!result.equals(mList.get(i).getFoodType())){
                    map.put(mList.get(i).getFoodType(),i+"");
                }
            }
        }
        return map;
    }
}
