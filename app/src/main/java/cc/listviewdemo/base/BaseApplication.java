package cc.listviewdemo.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cc.listviewdemo.model.GWCar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BaseApplication extends Application {
    private static BaseApplication sInstance;
    private static Context context;
    public static RequestQueue mQueue;
    public static GWCar mCar;//购物车
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        if(mQueue==null){
            mQueue = Volley.newRequestQueue(context);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }

    //系统处于资源匮乏的状态
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}