package cc.listviewdemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.GetLocationActivity;
import cc.listviewdemo.activity.SearchShopActivity;
import cc.listviewdemo.base.BaseApplication;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.config.SaveKey;
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
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 首页Fragment
 * Created by Administrator on 2016/7/3.
 */
public class MainFragment extends BaseFragment implements IFMainView {
    ProgressDialog progressDialog = null;
    @CodeNote(id = R.id.main)
    TotalScrollView main;
    @CodeNote(id = R.id.iv)
    RelativeLayout iv;
    @CodeNote(id = R.id.dizhi_tv)
    TextView dizhi_tv;
    @CodeNote(id = R.id.shop_img_iv)
    ImageView shop_img_iv;
    @CodeNote(id = R.id.main_list)
    ListView main_list;
    CommonAdapter<Shop> mAdapter;
    CommonAdapter<ShopType> mShopTypeAdapter;
    List<ShopType> mShopTypeList;
    List<Shop> mShopDetailsList;
    @CodeNote(id = R.id.guanggao_cb)
    ConvenientBanner convenientBanner;
    @CodeNote(id = R.id.shoptype_gv)
    GridView shoptype_gv;
    FMainListener mListener;
    @CodeNote(id = R.id.xuanze_zuobiao_ll)
    LinearLayout xuanze_zuobiao_ll;
    @CodeNote(id = R.id.title_search_ll, click = "onClick")
    LinearLayout title_search_ll;
    Map<String, String> map;
    @CodeNote(id = R.id.ptr)
    PtrFrameLayout ptr;
    ImageLoader imageLoader;
    @CodeNote(id = R.id.xuanze_zuobiao_rl, click = "onClick")
    RelativeLayout xuanze_zuobiao_rl;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mShopTypeList = new ArrayList<>();
        mShopDetailsList = new ArrayList<>();
        mListener = new FMainListener(this, MainActivity.mInstance);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        mLocClient = new LocationClient(MainActivity.mInstance);
        mLocClient.setLocOption(option);
        mLocClient.registerLocationListener(dbListener);
        mLocClient.start();
    }

    /**
     * 定位SDK的核心类
     */
    private LocationClient mLocClient;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xuanze_zuobiao_rl:
                isIntent = true;
                startActivityForResult(new Intent(MainActivity.mInstance, GetLocationActivity.class), 0);
                break;
            case R.id.title_search_ll:
                Utils.IntentPost(SearchShopActivity.class);//跳转到查询页面
                break;
        }
    }

    boolean isIntent = false;


    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        isIntent = false;
        switch (resultCode) {//重新获取坐标，刷新当前页面
            case 99:
                String address = data.getStringExtra("selectAddress");
                String[] vals = address.split(":");
                dizhi_tv.setText(vals[0].split("\\(")[0]);
                Utils.WriteString(SaveKey.KEY_LAT_LON, vals[1] + ":" + vals[2] + ":" + vals[3]);
                page_size = 1;
                loadShop(vals[1], vals[2]);
                break;
        }
    }

    private GeoCoder geoCoder;
    /***
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener dbListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {//首次定位，加载第一页
            // 创建GeoCoder实例对象
            geoCoder = GeoCoder.newInstance();
            // 发起反地理编码请求(经纬度->地址信息)
            ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
            // 设置反地理编码位置坐标
            reverseGeoCodeOption.location(new LatLng(location.getLatitude(), location.getLongitude()));
            geoCoder.reverseGeoCode(reverseGeoCodeOption);
            if (!isIntent) {
                // 设置查询结果监听者
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
                        String name = poiInfos.get(0).name.toString();
                        LatLng lnn = poiInfos.get(0).location;
                        String lat = (lnn.latitude + "").substring(0, 9);
                        String lon = (lnn.longitude + "").substring(0, 10);
                        page_size = 1;
                        loadShop(lat, lon);
                        dizhi_tv.setText(name);
                        ReverseGeoCodeResult.AddressComponent address = reverseGeoCodeResult.getAddressDetail();
                        Utils.WriteString(SaveKey.KEY_LAT_LON, name + "(" + poiInfos.get(0).address + "):" + lat + ":" + lon + ":" +
                                address.province + "," + address.city + "," + address.district + "," +
                                address.street + "," + address.streetNumber);
                    }
                });
            }
        }
    };
    private int index = 0;

    @Override
    public void initEvents() {
        imageLoader = ImageLoaderFactory.create(MainActivity.mInstance);
        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(MainActivity.mInstance);//1.默认经典头布局
        ptr.setHeaderView(defaultHeader);//给Ptr添加头布局
        ptr.addPtrUIHandler(defaultHeader);//使头布局的状态和刷新状态同步
        if (Utils.isNetworkConnected()) {
            progressDialog = ProgressDialog.show(MainActivity.mInstance, "",
                    "加载中...");
            mListener.initGG();//加载广告
            mListener.initShopType();
        }
        main.setTitleView(iv, xuanze_zuobiao_ll, title_search_ll);//设置需要显示隐藏的view
        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        index++;
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    if (view.getMeasuredHeight() - 100 <= v.getScrollY() + v.getHeight()) {
                        if (!isBottom) {
                            String loca = Utils.ReadString(SaveKey.KEY_LAT_LON);
                            if (!loca.equals("::,,")) {
                                if (Utils.isNetworkConnected()) {
                                    page_size = page_size + 1;
                                    loadShop(loca.split(":")[1], loca.split(":")[2]);
                                } else {//无网加载底部

                                }
                            }
                        }
                    }
                }
                return false;
            }
        });
        //附近商家
        mAdapter = new CommonAdapter<Shop>(MainActivity.mInstance, mShopDetailsList, R.layout.item_main_shop) {
            @Override
            public void convert(final CommonViewHolder holder, final Shop detail, int position) {
                if (detail.getTogoName().length() > 10) {
                    holder.setText(R.id.title_tv, detail.getTogoName().substring(0, 10) + "...");
                } else {
                    holder.setText(R.id.title_tv, detail.getTogoName());
                }

                holder.setGlideImage(R.id.iv, detail.getIcon());
                holder.setText(R.id.qisong_num_tv, "￥" + detail.getMinmoney());//起送价
                if (("0.0").equals(detail.getSendmoney()) || ("0").equals(detail.getSendmoney())) {
                    holder.setText(R.id.peisong_num_tv, "免费配送");//配送费
                } else {
                    holder.setText(R.id.peisong_num_tv, "￥" + detail.getSendmoney() + "  配送费");//配送费
                }
                holder.setText(R.id.sell_num_tv, "月售" + detail.getSales() + "单");

                holder.setTag(R.id.tag_gv, detail.getTaglist(), tag_height);

                if (!detail.getStatus().equals("1")) {
                    holder.setVisible(R.id.shop_no_open_iv, true);
                } else {
                    holder.setVisible(R.id.shop_no_open_iv, false);
                }
                holder.setText(R.id.send_time_tv, detail.getSenttime() + "分钟");
                if (("0").equals(detail.getSentorg())) {//显示大可专送
                    holder.setVisible(R.id.spall_icon_tv, true);
                } else {
                    holder.setVisible(R.id.spall_icon_tv, false);
                }
                holder.setStar(R.id.start_num_rb, "5");//detail.getStar()
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
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                String loca = Utils.ReadString(SaveKey.KEY_LAT_LON);
                if (Utils.isNetworkConnected()) {
                    if (!loca.equals("::,,")) {
                        page_size = 1;
                        loadShop(loca.split(":")[1], loca.split(":")[2]);
                        mListener.initGG();//加载广告
                        mListener.initShopType();
                        isBottom = false;
                    } else {
                        iv.setVisibility(View.VISIBLE);
                        ptr.refreshComplete();
                    }
                } else {
                    ptr.refreshComplete();
                }
            }
        });
//        load_more_tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        load_more_height = load_more_tv.getMeasuredHeight();
        View view = View.inflate(MainActivity.mInstance, R.layout.item_main_shop, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        item_heigh = view.getMeasuredHeight();
        View total_ll = View.inflate(MainActivity.mInstance, R.layout.item_main_tag, null);
        total_ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tag_height = total_ll.getMeasuredHeight();
    }

    /**
     * 加载
     */
    private void loadShop(String lat, String lng) {
        map = new HashMap<>();
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("pageindex", page_size + "");
        mListener.initShops(map);
    }

    int item_heigh = 0;//shop的item的高度
    int tag_height = 0;//标签的高度
    int load_more_height = 0;//加载更多的高度
    int page_size = 1;
    boolean isLoadMore = false;

    private void initListviewHeight() {
        int total_height = item_heigh * mShopDetailsList.size() + 5;
        for (Shop shop : mShopDetailsList) {
            if (shop.getTaglist().size() > 3) {
                total_height = total_height + 3 * tag_height;
            } else {
                total_height = total_height + shop.getTaglist().size() * tag_height;
            }
        }
        main_list.setLayoutParams(new LinearLayout.LayoutParams(-1, total_height));
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
        convenientBanner.startTurning(10 * 1000);
    }

    /**
     * @param list 商店类型
     */
    @Override
    public void load8Item(final List<ShopType> list) {
        if (list.size() > 0) {
            mShopTypeAdapter.refresh(list);
            shoptype_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Utils.IntentPost(SearchShopActivity.class, new Utils.putListener() {
                        @Override
                        public void put(Intent intent) {
                            intent.putExtra("keyword", list.get(position).getSortID() + ":" + list.get(position).getSortName());
                        }
                    });
                }
            });
        }
    }

    @CodeNote(id = R.id.load_more_tv)
    TextView load_more_tv;
    boolean isBottom = false;

    /**
     * @param record 总数量
     * @param page   当前页数
     * @param total  所有页数
     * @param list   店铺信息合集
     */
    @Override
    public void loadNearSH(int record, final int page, final int total, final List<Shop> list) {
        if (page == total) {
            isBottom = true;
        }
        if (page_size > 1) {
            mShopDetailsList.addAll(list);
        } else {
            mShopDetailsList = list;
        }

        mAdapter.refresh(mShopDetailsList);
        getTotalHeightofListView(mAdapter);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        isLoadMore = false;
        ptr.refreshComplete();
        if (page < total) {
            main_list.addFooterView(load_more_tv);
        } else {
            main_list.removeFooterView(load_more_tv);
        }
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(SHDetailsActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("Shop", mShopDetailsList.get(position));
                    }
                });
            }
        });
    }

    /**
     * 根据CommonAdapter计算Listview的高度
     *
     * @param mAdapter
     */
    public void getTotalHeightofListView(CommonAdapter mAdapter) {
        if (mAdapter == null) {
            return;
        }
        if (page_size == 1) {
            nowHeight = 0;
        }
        int totalHeight = 0;
        for (int i = 10 * (page_size - 1); i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, main_list);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        nowHeight = totalHeight + nowHeight;
        ViewGroup.LayoutParams params = main_list.getLayoutParams();
        params.height = nowHeight + (main_list.getDividerHeight() * (mAdapter.getCount() - 1));
        main_list.setLayoutParams(params);
        main_list.requestLayout();
    }

    int nowHeight = 0;//当前ListView高度
}
