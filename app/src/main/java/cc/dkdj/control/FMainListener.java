package cc.dkdj.control;


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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dkdj.activity.SHDetailsActivity;
import cc.dkdj.model.Shop;
import cc.dkdj.model.ShopType;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

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
     * 加载顶部广告
     */
    public void initGG() {
        HttpUtils.loadJson("specialad", null, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    List<String> list = new ArrayList<>();
                    JSONArray array = obj.getJSONArray("foodtypelist");
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
        });
    }

    /**
     * 加载shop分类
     */
    public void initShopType() {
        Map<String,String> map=new HashMap<>();
        map.put("pid","0");
        map.put("indexpage","1");
        map.put("pagesize","100");
        map.put("languageType","2");
        HttpUtils.loadJson("GetShopTypeList", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    List<ShopType> list = new ArrayList<>();
                    JSONArray array = obj.getJSONArray("datalist");
                    for (int i = 0; i < array.length(); i++) {
                        list.add(new Gson().fromJson(array.getString(i), ShopType.class));
                    }
                    mView.load8Item(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initShops(Map maps) {
        maps.put("languageType","2");
        HttpUtils.loadJson("GetShopListByLocation", maps, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    List<Shop> list = new ArrayList<>();
                    JSONArray array = obj.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        list.add(new Gson().fromJson(array.getString(i), Shop.class));
                    }
                    mView.loadNearSH(obj.getInt("record"), obj.getInt("page"), obj.getInt("total"), list);
                } catch (JSONException e) {

                }
            }
        });
    }
}
