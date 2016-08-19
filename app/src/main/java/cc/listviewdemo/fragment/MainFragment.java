package cc.listviewdemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
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
    @CodeNote(id = R.id.xuanze_zuobiao_ll, click = "onClick")
    LinearLayout xuanze_zuobiao_ll;
    @CodeNote(id = R.id.title_search_ll, click = "onClick")
    LinearLayout title_search_ll;
    Map<String, String> map;
    @CodeNote(id = R.id.main_refresh_srl)
    SwipeRefreshLayout main_refresh_srl;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);

        mList = new ArrayList<>();
        mShopTypeList = new ArrayList<>();
        mShopDetailsList = new ArrayList<>();
        mListener = new FMainListener(this, MainActivity.mInstance);
        progressDialog = ProgressDialog.show(MainActivity.mInstance, "",
                "加载中...");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开GPRS
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedLocationPoiList(true);
        mLocClient=new LocationClient(MainActivity.mInstance);
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
            case R.id.xuanze_zuobiao_ll:
                startActivityForResult(new Intent(MainActivity.mInstance, GetLocationActivity.class), 0);
                break;
            case R.id.title_search_ll:
                Utils.IntentPost(SearchShopActivity.class);//跳转到查询页面
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {//重新获取坐标，刷新当前页面
            case 99:
                String address = data.getStringExtra("selectAddress");
                String[] vals = address.split(":");
                dizhi_tv.setText(vals[0]);
                map = new HashMap<>();
                Utils.WriteString(SaveKey.KEY_LAT_LON, vals[1] + ":" + vals[2] + ":" + vals[3]);
                map.put("lat", vals[1]);
                map.put("lng", vals[2]);
                page_size=1;
                map.put("pageindex", page_size+"");
                mListener.initShops(map);
                break;
        }
    }

    /***
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener dbListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {//首次定位，加载第一页
            map = new HashMap<>();
            String lat;
            String lon;
            lat = "38.893125";
            lon = "115.508290";
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                lat = location.getLatitude() + "";
                lon = location.getLongitude() + "";
            } else {
                dizhi_tv.setText("无法定位>");
            }
            Utils.WriteString(SaveKey.KEY_LAT_LON, lat + ":" + lon + ":" +
                    location.getProvince() + "," + location.getCity() + "," + location.getDistrict());
            map.put("lat", lat);
            map.put("lng", lon);
            page_size=1;
            map.put("pageindex", page_size+"");
            mListener.initShops(map);
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                Poi poi = (Poi) location.getPoiList().get(0);
                dizhi_tv.setText(poi.getName());
            }
        }

    };

    @Override
    public void initEvents() {
        mListener.initGG();//加载广告
        mListener.initShopType();
        main.setTitleView(iv, xuanze_zuobiao_ll, title_search_ll);//设置需要显示隐藏的view
        main.setOnScrollToBottomLintener(new TotalScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                Log.i("TAGS",isBottom+":"+isLoadMore+":"+load_more_tv.getVisibility());
                if(isBottom&&!isLoadMore&&load_more_tv.getVisibility()==View.VISIBLE){
                    String loca = Utils.ReadString(SaveKey.KEY_LAT_LON);
                    map.put("lat", loca.split(":")[0]);
                    map.put("lng", loca.split(":")[1]);
                    map.put("pageindex", (page_size++)+"");
                    mListener.initShops(map);
                    isLoadMore=true;
                }
            }
        });
        //附近商家
        mAdapter = new CommonAdapter<Shop>(MainActivity.mInstance, mShopDetailsList, R.layout.item_main_shop) {
            @Override
            public void convert(final CommonViewHolder holder, final Shop detail, int position) {
                holder.setText(R.id.title_tv, detail.getTogoName());
                holder.setGlideImage(R.id.iv, detail.getIcon());
                holder.setText(R.id.qisong_num_tv, "￥" + detail.getMinmoney());//起送价
                if (("0.0").equals(detail.getSendmoney()) || ("0").equals(detail.getSendmoney())) {
                    holder.setText(R.id.peisong_num_tv, "免费配送");//配送费
                } else {
                    holder.setText(R.id.peisong_num_tv, "￥" + detail.getSendmoney() + "  配送费");//配送费
                }
                holder.setText(R.id.sell_num_tv, "月售" + detail.getSales() + "单");
                holder.setTags(R.id.tag_gv, detail.getTaglist());
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
                holder.setStar(R.id.start_num_rb, detail.getStar());
                if (detail.getTaglist().size() > 0) {//标签数大于0
                    final TextView isOpen_tv = holder.getView(R.id.is_open_tv);
                    final TextView activity_num_tv = holder.getView(R.id.activity_num_tv);
                    final RelativeLayout rv = holder.getView(R.id.tag_rl);
                    holder.setText(R.id.activity_num_tv, detail.getTaglist().size());
                    if (Integer.parseInt(activity_num_tv.getText().toString().trim()) > 2) {
                        holder.setOnClickListener(R.id.tag_rl, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String isOpen = isOpen_tv.getText().toString();
                                String activity_num = activity_num_tv.getText().toString();
                                if (isOpen.equals("2")) {
                                    rv.setLayoutParams(new LinearLayout.LayoutParams(-1, detail.getTaglist().size() * 79));
                                    isOpen_tv.setText(activity_num);
                                    holder.setImageResource(R.id.is_open_iv, R.mipmap.jiantou_up);
                                } else {
                                    rv.setLayoutParams(new LinearLayout.LayoutParams(-1, 150));
                                    isOpen_tv.setText("2");
                                    holder.setImageResource(R.id.is_open_iv, R.mipmap.jiantou_down);
                                }
                            }
                        });
                    } else {
                        holder.setVisible(R.id.activity_num_ll, false);
                    }
                } else {//隐藏标签
                    holder.setVisible(R.id.tag_rl, false);
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
        main_refresh_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String loca = Utils.ReadString(SaveKey.KEY_LAT_LON);
                page_size=1;
                map.put("lat", loca.split(":")[0]);
                map.put("lng", loca.split(":")[1]);
                map.put("pageindex", page_size+"");
                mListener.initShops(map);
            }
        });
    }
    int page_size=1;
    boolean isLoadMore=false;
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

    @Override
    public void loadNearSH(int record, int page, int total, final List<Shop> list) {
        if(page_size>1) {
            mShopDetailsList.addAll(list);
        }else {
            mShopDetailsList=list;
        }
        mAdapter.refresh(mShopDetailsList);
        progressDialog.dismiss();
        isLoadMore=false;
        if (main_refresh_srl.isRefreshing()) {
            main_refresh_srl.setRefreshing(false);
        }
        if (page < total) {
            load_more_tv.setVisibility(View.VISIBLE);
        } else {
            load_more_tv.setVisibility(View.GONE);
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
}
