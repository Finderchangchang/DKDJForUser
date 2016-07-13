package cc.listviewdemo.view;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cc.listviewdemo.base.BaseApplication;
import cc.listviewdemo.config.Config;
import cc.listviewdemo.money.Util;

/**
 * 作者：lwj on 2016/7/9 10:30
 * 邮箱：1031066280@qq.com
 */
public class HttpUtils {
    public static void loadJson(String method, Map<String, String> map, final LoadJsonListener listener) {
        String url = Config.URL + method + ".aspx?1=1";
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            url = url + "&" + i.next().toString();
        }
        url=url.replace(" ","%20");
        Log.i("TAG","url:"+url);
        JsonObjectRequest json = new JsonObjectRequest(
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.load(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.load(null);
            }
        });
        json.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (BaseApplication.mQueue != null) {
            BaseApplication.mQueue = Volley.newRequestQueue(BaseApplication.getContext());
        }
        BaseApplication.mQueue.add(json);
    }

    public interface LoadJsonListener {
        void load(JSONObject obj);
    }
    public interface LoadStringListener{
        void load(String obj);
    }
    public static void loadString(String method, Map<String,String> map){
        String url = Config.URL + method + ".aspx?1=1";
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            url = url + "&" + i.next().toString();
        }
        Log.i("TAG","url:"+url);
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url).build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String s="";
            }

            @Override
            public void onResponse(final com.squareup.okhttp.Response response) throws IOException {
                String htmlStr =  response.body().string();
            }
        });
    }
}
