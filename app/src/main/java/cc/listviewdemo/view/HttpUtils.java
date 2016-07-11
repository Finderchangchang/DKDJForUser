package cc.listviewdemo.view;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
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
        String url = Config.URL + method + ".aspx?1=1";
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            url = url + "&" + i.next().toString();
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
                listener.load(null);
            }
        });
        if (BaseApplication.mQueue != null) {
            BaseApplication.mQueue = Volley.newRequestQueue(BaseApplication.getContext());
        }
        BaseApplication.mQueue.add(json);
    }

    public interface LoadJsonListener {
        void load(JSONObject obj);
    }
}
