package cc.dkdj.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dkdj.R;
import cc.dkdj.base.BaseApplication;
import cc.dkdj.base.BaseFragment;
import cc.dkdj.activity.MainActivity;
import cc.dkdj.activity.SHDetailsActivity;
import cc.dkdj.config.SaveKey;
import cc.dkdj.control.FMainListener;
import cc.dkdj.control.IFMainView;
import cc.dkdj.model.Shop;
import cc.dkdj.model.ShopType;
import cc.dkdj.service.LocationService;
import cc.dkdj.view.CommonAdapter;
import cc.dkdj.view.CommonViewHolder;
import cc.dkdj.view.MeasureListView;
import cc.dkdj.view.NetworkImageHolderView;
import cc.dkdj.view.TotalScrollView;
import cc.dkdj.view.Utils;

/**
 * 首页Fragment
 * Created by Administrator on 2016/7/3.
 */
public class MainFragment extends BaseFragment implements IFMainView {

    @CodeNote(id = R.id.main)
    TotalScrollView main;
    @CodeNote(id = R.id.iv)
    RelativeLayout iv;
    @CodeNote(id = R.id.dizhi_tv)
    TextView dizhi_tv;
    @CodeNote(id = R.id.shop_img_iv)
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
        BaseApplication.locationService.registerListener(dbListener);
        BaseApplication.locationService.start();// 定位SDK
        map = new HashMap<>();
    }

    /***
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener dbListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            map.put("pageindex", "1");//pageindex=1&pagesize=20&lat=38.893189&lng=115.508560
            map.put("pagesize", "100");
            String lat = "0";
            String lon = "0";
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                lat=location.getLatitude()+"";
                lon=location.getLongitude()+"";
//                lat = "38.894431";
//                lon = "115.504949";
            } else {
                dizhi_tv.setText("无法定位>");
            }
            Utils.WriteString(SaveKey.KEY_LAT, lat);
            Utils.WriteString(SaveKey.KEY_LON, lon);
            map.put("lat", lat);
            map.put("lng", lon);
            mListener.initShops(map);
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                dizhi_tv.setText(location.getPoiList().get(0).getName() + ">");
            }
        }

    };
    Map<String, String> map;

    @Override
    public void initEvents() {
        mListener.initGG();//加载广告
        mListener.initShopType();
        main.setTitleView(iv);//设置需要显示隐藏的view
        //附近商家
        mAdapter = new CommonAdapter<Shop>(MainActivity.mInstance, mShopDetailsList, R.layout.item_main_shop) {
            @Override
            public void convert(CommonViewHolder holder, Shop detail, int position) {
                holder.setText(R.id.title_tv, detail.getTogoName());
                holder.setGlideImage(R.id.iv, detail.getIcon());
                holder.setText(R.id.qisong_num_tv, "￥" + detail.getMinmoney());//起送价
                holder.setText(R.id.peisong_num_tv, "￥" + detail.getSendmoney());//配送费
                holder.setTags(R.id.tag_gv, detail.getTaglist());
                if (!detail.getStatus().equals("1")) {
                    holder.setVisible(R.id.shop_no_open_tv, true);
                }else{
                    holder.setVisible(R.id.shop_no_open_tv, false);
                }
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
        shoptype_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.mInstance.ToastShort("敬请期待...");
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
                if (list.get(position).getStatus().equals("1")) {
                    Utils.IntentPost(SHDetailsActivity.class, new Utils.putListener() {
                        @Override
                        public void put(Intent intent) {
                            intent.putExtra("Shop", list.get(position));
                        }
                    });
                } else {
                    MainActivity.mInstance.ToastShort("商家休息中...");
                }
            }
        });
    }
}
