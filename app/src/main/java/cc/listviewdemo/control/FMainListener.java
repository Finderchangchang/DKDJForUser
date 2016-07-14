package cc.listviewdemo.control;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;

/**
 * 首页Fragment逻辑处理页面
 */
public class FMainListener {
    IFMainView mView;
    RequestQueue mQueue;

    public FMainListener(IFMainView mView, Context context) {
        this.mView = mView;
        mQueue = Volley.newRequestQueue(context);
    }

    /**
     * 加载主页数据
     */
    public void loading() {
        initGG();
        initShopType();
        initShops();
    }

    /**
     * 加载顶部广告
     */
    private void initGG() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://122.114.94.150/App/Android/specialad.aspx", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<String> list = new ArrayList<>();
                            JSONArray array = response.getJSONArray("foodtypelist");
                            for (int i = 0; i < array.length(); i++) {
                                Gson gson = new Gson();
                                ShopType type = gson.fromJson(array.getString(i), ShopType.class);
                                list.add(type.getSortName());
                            }
                            mView.loadGG(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                3,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonObjectRequest);
    }

    private void initShopType() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                "http://122.114.94.150/App/Android/GetShopTypeList.aspx?pid=0&indexpage=1&pagesize=100&languageType=2", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ShopType> list = new ArrayList<>();
                            JSONArray array = response.getJSONArray("datalist");
                            for (int i = 0; i < array.length(); i++) {
                                list.add(new Gson().fromJson(array.getString(i), ShopType.class));
                            }
                            mView.load8Item(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                3,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonObjectRequest);
    }

    private void initShops() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                "http://122.114.94.150/App/Android/GetShopListByLocation.aspx?languageType=2&pageindex=1&sortname=SortNum&pagesize=20&shoptype=15&lat=38.893189&sortflag=1&lng=115.508560",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<Shop> list = new ArrayList<>();
                    JSONArray array = response.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        list.add(new Gson().fromJson(array.getString(i), Shop.class));
                    }
                    mView.loadNearSH(response.getInt("record"), response.getInt("page"), response.getInt("total"), list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                3,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonObjectRequest);
    }

}
