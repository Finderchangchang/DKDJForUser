package cc.listviewdemo.activity;

import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.cloud.CloudListener;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;

import com.baidu.mapapi.cloud.BoundSearchInfo;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;

/**
 * 作者：lwj on 2016/8/10 17:00
 * 邮箱：1031066280@qq.com
 */
public class GetNearlyShopActivity extends BaseActivity implements CloudListener {
    private static final String LTAG = GetNearlyShopActivity.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_nearly_shop);
    }

    @Override
    public void initEvents() {
/**
 * CloudManager:LBS云检索管理类
 * getInstance():获取唯一可用实例
 * init(CloudListener listener):初始化
 * 需要实现CloudListener接口的onGetDetailSearchResult和onGetSearchResult方法
 * */
        CloudManager.getInstance().init(GetNearlyShopActivity.this);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //本地搜索
        findViewById(R.id.regionSearch).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * LocalSearchInfo:设置云检索中本地检索的参数，继承自 BaseCloudSearchInfo
                         * */
                        LocalSearchInfo info = new LocalSearchInfo();

                        //access_key（必须），最大长度50
                        info.ak = "B266f735e43ab207ec152deff44fec8b";

                        //geo table 表主键（必须）
                        info.geoTableId = 31869;

                        //标签，可选，空格分隔的多字符串，最长45个字符，样例：美食 小吃
                        info.tags = "";

                        //检索关键字，可选。最长45个字符。
                        info.q = "天安门";

                        //检索区域名称，必选。市或区的名字，如北京市，海淀区。最长25个字符。
                        info.region = "北京市";

                        /**
                         * localSearch(LocalSearchInfo info)
                         * 区域检索，如果所有参数都合法，返回true，否则返回 fasle，
                         * 检索的结果在 CloudListener 中的 onGetSearchResult() 函数中。
                         * */
                        CloudManager.getInstance().localSearch(info);
                    }
                });
        //周边搜索
        findViewById(R.id.nearbySearch).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        /**
                         * NearbySearchInfo:周边检索设置参数类，继承自 BaseCloudSearchInfo
                         * */
                        NearbySearchInfo info = new NearbySearchInfo();

                        //access_key（必须），最大长度50
                        info.ak = "D9ace96891048231e8777291cda45ca0";

                        ////geo table 表主键（必须）
                        info.geoTableId = 32038;

                        //检索半径，可选；单位为米，默认为1000米。样例：500
                        info.radius = 30000;

                        //检索中心点坐标（经纬度），必选；样例：116.4321,38.76623
                        info.location = "115.504949,38.894431";

                        /**
                         * nearbySearch(NearbySearchInfo info)
                         * 周边检索，如果所有参数都合法，返回true，否则返回 fasle，
                         * 检索的结果在 CloudListener 中的 onGetSearchResult() 函数中。
                         * */
                        CloudManager.getInstance().nearbySearch(info);
                    }
                });
        //矩形搜索
        findViewById(R.id.boundsSearch).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        /**
                         *BoundSearchInfo:设置矩形检索条件类，继承自 BaseCloudSearchInfo
                         * */
                        BoundSearchInfo info = new BoundSearchInfo();
                        info.ak = "B266f735e43ab207ec152deff44fec8b";
                        info.geoTableId = 31869;
                        info.q = "天安门";

                        //矩形区域，必须。左下角和右上角的经纬度坐标点。2个坐标点用;号分隔
                        //样例：116.30,36.20;117.30,37.20
                        info.bound = "116.401663,39.913961;116.406529,39.917396";

                        /**
                         * boundSearch(BoundSearchInfo info)
                         * 矩形检索，如果所有参数都合法，返回true，否则返回 fasle，
                         * 检索的结果在 CloudListener 中的 onGetSearchResult() 函数中。
                         * */
                        CloudManager.getInstance().boundSearch(info);
                    }
                });
        //详情搜索
        findViewById(R.id.detailsSearch).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        /**
                         *DetailSearchInfo:详细信息检索条件类，继承自 BaseSearchInfo
                         * */
                        DetailSearchInfo info = new DetailSearchInfo();
                        info.ak = "B266f735e43ab207ec152deff44fec8b";
                        info.geoTableId = 31869;

                        //poi数据id
                        info.uid = 18622266;

                        /**
                         * detailSearch(DetailSearchInfo info)
                         * 详细信息检索，如果所有参数都合法，返回true，否则返回 fasle。
                         * */
                        CloudManager.getInstance().detailSearch(info);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        CloudManager.getInstance().destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * void onGetSearchResult(CloudSearchResult result,int error)
     * 当详情检索完成时回调此函数
     * @param
     * result -搜索的结果，如果发生网络错误为null
     * error - 错误号，0表示正确返回，-1表示网络错误
     * */
    /**
     * DetailSearchResult:详细信息检索结果类
     * 字段：  poiInfo 详细信息结果数据
     */
    public void onGetDetailSearchResult(DetailSearchResult result, int error) {
        if (result != null) {
            if (result.poiInfo != null) {
                ToastShort(result.poiInfo.title);
            } else {
                ToastShort("status:" + result.status);
            }
        }
    }

    /**
     * void onGetDetailSearchResult(DetailSearchResult result,int error)
     * 当检索完成时回调此函数
     * @param
     * result - 详情搜索结果，如果发生网络错误为null
     * error - 错误号，0表示正确返回，-1表示网络错误
     * */

    /**
     * CloudSearchResult:
     * java.util.List<CloudPoiInfo>
     * poiList
     * poi结果列表
     */
    public void onGetSearchResult(CloudSearchResult result, int error) {
        if (result != null && result.poiList != null && result.poiList.size() > 0) {
            ToastShort(result.poiList.toString());
            //清空地图所有的 Overlay 覆盖物以及 InfoWindow
            mBaiduMap.clear();

            /**
             * public static BitmapDescriptor fromResource(int resourceId)
             * 根据资源 Id 创建 bitmap 描述信息
             * */
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_delete);

            LatLng ll;
            LatLngBounds.Builder builder = new Builder();

            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                /**
                 * OverlayOptions:地图覆盖物选型基类
                 *
                 * public MarkerOptions icon(BitmapDescriptor icon):
                 * 设置 Marker 覆盖物的图标，相同图案的 icon 的 marker
                 * 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
                 * @param icon - Marker 覆盖物的图标
                 * @return 该 Marker 选项对象
                 *
                 * public MarkerOptions position(LatLng position):
                 * 设置 marker 覆盖物的位置坐标
                 * @param position - marker 覆盖物的位置坐标
                 * @param 该 Marker 选项对象
                 * */
                OverlayOptions oo = new MarkerOptions()
                        .icon(bd)
                        .position(ll);
                /**
                 * addOverlay(OverlayOptions options):
                 * 向地图添加一个 Overlay
                 * */
                mBaiduMap.addOverlay(oo);

                /**
                 * public LatLngBounds.Builder include(LatLng point)
                 * 让该地理范围包含一个地理位置坐标
                 * @param point - 地理位置坐标
                 * @return 该构造器对象
                 * */
                builder.include(ll);
            }
            /**
             * public LatLngBounds build()
             * 创建地理范围对象
             * @return 创建出的地理范围对象
             * */
            LatLngBounds bounds = builder.build();

            /**
             * MapStatusUpdateFactory:生成地图状态将要发生的变化
             *
             * public static MapStatusUpdate newLatLngBounds(LatLngBounds bounds)
             * 设置显示在屏幕中的地图地理范围
             * @param bounds - 地图显示地理范围，不能为 null
             * @return 返回构造的 MapStatusUpdate， 如果 bounds 为 null 则返回空。
             * */
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);
        }
    }
}
