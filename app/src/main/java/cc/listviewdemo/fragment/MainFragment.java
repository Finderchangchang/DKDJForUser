package cc.listviewdemo.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.control.FMainListener;
import cc.listviewdemo.control.IFMainView;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.MeasureListView;
import cc.listviewdemo.view.NetworkImageHolderView;
import cc.listviewdemo.view.TotalScrollView;
import cc.listviewdemo.view.Utils;

/**
 * 首页Fragment
 * Created by Administrator on 2016/7/3.
 */
public class MainFragment extends BaseFragment implements IFMainView {
    @CodeNote(id = R.id.main)
    TotalScrollView main;
    @CodeNote(id = R.id.iv)
    RelativeLayout iv;
    @CodeNote(id=R.id.shop_img_iv)
    ImageView shop_img_iv;
    @CodeNote(id = R.id.main_list)
    MeasureListView main_list;
    CommonAdapter<Shop> mAdapter;
    CommonAdapter<ShopType> mShopTypeAdapter;
    List<String> mList;
    List<ShopType> mShopTypeList;
    List<Shop> mShopDetailsList;
    @CodeNote(id = R.id.guanggao_cb)
    ConvenientBanner convenientBanner;
    @CodeNote(id = R.id.shoptype_gv)
    GridView shoptype_gv;
    FMainListener mListener;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mList = new ArrayList<>();
        mShopTypeList = new ArrayList<>();
        mShopDetailsList = new ArrayList<>();
        mListener = new FMainListener(this, MainActivity.mInstance);
    }

    @Override
    public void initEvents() {
        mListener.loading();//加载数据
        main.setTitleView(iv);//设置需要显示隐藏的view
        //附近商家
        mAdapter = new CommonAdapter<Shop>(MainActivity.mInstance, mShopDetailsList, R.layout.item_main_shop) {
            @Override
            public void convert(CommonViewHolder holder, Shop detail, int position) {
                holder.setText(R.id.title_tv, detail.getTogoName());
                holder.setGlideImage(R.id.iv,detail.getIcon());
                holder.setStar(R.id.star_rb,detail.getGrade());
                holder.setText(R.id.sell_num_tv,"月售"+detail.getSales()+"单");
                holder.setText(R.id.qisong_num_tv,"￥"+detail.getMinmoney());//起送价
                holder.setText(R.id.peisong_num_tv,"￥"+detail.getSendmoney());//配送费
                holder.setTags(R.id.tag_gv,detail.getTaglist());
            }
        };
        main_list.setAdapter(mAdapter);
        //商家分类
        mShopTypeAdapter = new CommonAdapter<ShopType>(MainActivity.mInstance, mShopTypeList, R.layout.item_main_shoptype) {
            @Override
            public void convert(CommonViewHolder holder, ShopType type, int position) {
                holder.setText(R.id.tv, type.getSortName());
                holder.setGlideImage(R.id.iv, type.getSortPic());
            }
        };
        shoptype_gv.setAdapter(mShopTypeAdapter);
        shoptype_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * @param array 商品类型
     */
    @Override
    public void loadGG(List<String> array) {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, array);
    }

    /**
     * @param list 商店类型
     */
    @Override
    public void load8Item(List<ShopType> list) {
        if (list.size() > 0) {
            mShopTypeAdapter.refresh(list);
        }
    }

    @Override
    public void loadNearSH(int record, int page, int total, final List<Shop> list) {
        mAdapter.refresh(list);
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(SHDetailsActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("Shop", list.get(position));
                    }
                });
            }
        });
    }
}
