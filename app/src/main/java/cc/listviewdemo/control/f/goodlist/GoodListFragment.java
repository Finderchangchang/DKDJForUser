package cc.listviewdemo.control.f.goodlist;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.control.a.order.ConfirmOrderActivity;
import cc.listviewdemo.control.a.main.MainActivitys;
import cc.listviewdemo.control.a.shanghu.SHDetailsActivity;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodType;
import cc.listviewdemo.model.GWCar;
import cc.listviewdemo.model.Good;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.view.GoodsClassifyAdapter;
import cc.listviewdemo.view.GoodsDetailsAdapter;
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
    GoodsClassifyAdapter mLefts;
    @CodeNote(id = R.id.price_main)
    TextView price_main;
    @CodeNote(id = R.id.total_num_tv)
    TextView total_num_tv;
    GoodsDetailsAdapter detailsAdapter;
    GoodListListener mListener;//调用访问后台数据接口
    Shop shop=SHDetailsActivity.mInstance.shop;
    String shopId;//店铺ID
    @Override
    public void initViews() {
        setContentView(R.layout.frag_goodlist);
        shopId=shop.getDataID();
        mLeft = new ArrayList<>();
        mRight1 = new ArrayList<>();
        mListener = new GoodListListener(MainActivitys.mInstance, this);
    }

    @Override
    public void initEvents() {
        mListener.load(SHDetailsActivity.mInstance.shop.getDataID());
        mLefts = new GoodsClassifyAdapter(SHDetailsActivity.mInstance, mLeft);
        left_lv.setAdapter(mLefts);
        left_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLefts.clearSelection(position);
            }
        });
        detailsAdapter = new GoodsDetailsAdapter(this, mRight1, mLefts);
        right_lv.setAdapter(detailsAdapter);
        mCar=new GWCar();
        mCar.setShopName(shop.getTogoName());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.billing:
                if(count>0){
                    mCar.setPsMoney(Double.parseDouble(shop.getSendmoney()));
                    Utils.IntentPost(ConfirmOrderActivity.class, new Utils.putListener() {
                        @Override
                        public void put(Intent intent) {
                            intent.putExtra("goods",mCar);
                        }
                    });
                }else{
                    SHDetailsActivity.mInstance.ToastShort("购物车不能为空");
                }
                break;
        }
    }

    double price = 0;
    int count = 0;
    GWCar mCar;
    /**
     * 计算总价与选中的个数,并显示在界面上
     *
     * @param num 购物车商品数量
     * @param p   总钱数
     */
    public void calculateTotalPrice(int num, double p,Good good) {
        count = count + num;
        price = price + p;
        price_main.setText("￥" + Math.round(price * 100) / 100.0);
        if(num==1){
            mCar.addGood(good);
        }else{
            mCar.removeGood(good);
        }
        if (count > 0) {
            total_num_tv.setText(count + "");
            total_num_tv.setVisibility(View.VISIBLE);
        } else {
            total_num_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadGoodType(List<FoodType> mList) {
        mLefts.refresh(mList);
    }

    @Override
    public void loadGoodDetails(List<FoodDetail> mList) {
        detailsAdapter.refresh(mList);
    }
}
