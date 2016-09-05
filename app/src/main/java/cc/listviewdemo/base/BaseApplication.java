package cc.listviewdemo.base;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.tencent.bugly.crashreport.CrashReport;

import cc.listviewdemo.service.LocationService;
import cc.listviewdemo.view.Utils;
import in.srain.cube.Cube;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BaseApplication extends Application {
    private static Context context;
    public static RequestQueue mQueue;
    public static LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        Cube.onCreate(this);
        GrowingIO.startWithConfiguration(this, new Configuration()
                .useID()
                .trackAllFragments()
                .setChannel("应用宝"));
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(Utils.getVersion());
        CrashReport.initCrashReport(getApplicationContext(), "1105548720", false, strategy);//Bugly异常检测
    }

    public static Context getContext() {
        return context;
    }

    //系统处于资源匮乏的状态
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}