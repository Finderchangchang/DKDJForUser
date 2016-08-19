package cc.listviewdemo.activity;

import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.TitleBar;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.List;

/**
 * 修改定位
 * Created by Administrator on 2016/8/15.
 */
public class GetLocationActivity extends BaseActivity implements
        OnMapStatusChangeListener, BDLocationListener, OnGetGeoCoderResultListener {
    public static GetLocationActivity mInstance;
    protected static final String TAG = "GetLocationActivity";
    private ListView lv_near_address;
    private BaiduMap mBaiduMap = null;
    @CodeNote(id = R.id.main_tb)
    TitleBar main_tb;
    private MapView map;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private GeoCoder geoCoder;
    private boolean isFirstLoc = true;


    private void initData() {
        map = (MapView) findViewById(R.id.map);
        mBaiduMap = map.getMap();
        MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mLocClient = new LocationClient(this);
        // 注册定位监听
        mLocClient.registerLocationListener(this);
        // 定位选项
        LocationClientOption option = new LocationClientOption();
        /**
         * coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系
         * ：bd09ll
         */
        option.setCoorType("bd09ll");
        // 设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
        // 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        // 设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式 Battery_Saving 低功耗模式 Device_Sensors 仅设备(Gps)模式 Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置是否打开gps进行定位
        option.setOpenGps(true);
        mLocClient.setLocOption(option);
        // 开始定位
        mLocClient.start();
        lv_near_address = (ListView) findViewById(R.id.lv_near_address);
    }

	/*
     * 接受周边地理位置结果
	 *
	 * @param poiResult
	 */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        map.onDestroy();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
        map = null;
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng cenpt = mapStatus.target;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null || mBaiduMap == null) {
            return;
        }

        // 定位数据
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getDirection())
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(data);

        // 是否是第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
            mBaiduMap.animateMapStatus(msu);
        }

        // 创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        // 发起反地理编码请求(经纬度->地址信息)
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置反地理编码位置坐标
        reverseGeoCodeOption.location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
        geoCoder.reverseGeoCode(reverseGeoCodeOption);

        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
        String s = "123";
    }

    // 拿到变化地点后的附近地址
    @Override
    public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
        final List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        if (poiInfos != null && !"".equals(poiInfos)) {
            lv_near_address.setAdapter(new CommonAdapter<PoiInfo>(GetLocationActivity.this, poiInfos, R.layout.item_location_pois) {
                @Override
                public void convert(CommonViewHolder holder, PoiInfo poiInfo, int position) {
                    holder.setText(R.id.locationpois_address, poiInfo.address);
                    if (position == 0) {
                        holder.setText(R.id.locationpois_name, "[当前]" + poiInfo.name);
                        holder.setImageResource(R.id.iv_gps, R.mipmap.small_location_selected);
                        holder.setTextColor(R.id.locationpois_name, "#FF9D06");
                        holder.setTextColor(R.id.locationpois_address, "#FF9D06");
                    } else {
                        holder.setText(R.id.locationpois_name, poiInfo.name);
                        holder.setImageResource(R.id.iv_gps, R.mipmap.small_location_no_selected);
                        holder.setTextColor(R.id.locationpois_name, "#4A4A4A");
                        holder.setTextColor(R.id.locationpois_address, "#ffa9a9a9");
                    }
                }
            });
            lv_near_address.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = poiInfos.get(position).name.toString();
                    LatLng lnn = poiInfos.get(position).location;
                    Intent intent = new Intent();
                    String lat = poiInfos.get(position).location.latitude + "";
                    String lon = poiInfos.get(position).location.longitude + "";
                    ReverseGeoCodeResult.AddressComponent address = reverseGeoCodeResult.getAddressDetail();
                    intent.putExtra("selectAddress", name + ":" + lat.substring(0, 9) + ":" + lon.substring(0, 10) + ":" +
                            address.province + "," + address.city + "," + address.district);
                    setResult(99, intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void initViews() {
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_get_location);
        initData();
    }

    @Override
    public void initEvents() {
        main_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {

            }
        });
    }
}
