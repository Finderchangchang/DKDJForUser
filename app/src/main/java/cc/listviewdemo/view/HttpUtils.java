package cc.listviewdemo.view;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cc.listviewdemo.base.BaseApplication;
import cc.listviewdemo.config.Config;

/**
 * 作者：lwj on 2016/7/9 10:30
 * 邮箱：1031066280@qq.com
 */
public class HttpUtils {

    public static void loadJson(String method, Map<String, String> map, final LoadJsonListener listener) {
        String url;
        if (method.equals("version")) {
            url = Config.DOWN_PATH + method + ".aspx?1=1";
        } else {
            url = Config.PATH + method + ".aspx?1=1";
        }
        if (map != null) {
            Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                url = url + "&" + i.next().toString();
            }
            url = url.replace(" ", "%20");
        }
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
                Toast.makeText(BaseApplication.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
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
}
