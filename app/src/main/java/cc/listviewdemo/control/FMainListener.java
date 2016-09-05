package cc.listviewdemo.control;


import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;
import cc.listviewdemo.view.HttpUtils;

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
                if (obj != null) {
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

                    }
                }
            }
        });
    }

    /**
     * 加载shop分类
     */
    public void initShopType() {
        Map<String, String> map = new HashMap<>();
        map.put("pid", "0");
        map.put("indexpage", "1");
        map.put("pagesize", "100");
        map.put("languageType", "2");
        HttpUtils.loadJson("GetShopTypeList", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                if (obj != null) {
                    try {
                        List<ShopType> list = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("datalist");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getString(i), ShopType.class));
                        }
                        mView.load8Item(list);
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }

    public void initShops(Map maps) {
        maps.put("pagesize", "10");
        maps.put("languageType", "2");
        maps.put("sortname", "SortNum");
        HttpUtils.loadJson("GetShopListByLocation", maps, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                if (obj != null) {
                    try {
                        List<Shop> list = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getString(i), Shop.class));
                        }
                        mView.loadNearSH(obj.getInt("record"), obj.getInt("page"), obj.getInt("total"), list);
                    } catch (JSONException e) {

                    }
                }else{
                    mView.loadNearSH(0,0,0,null);
                }
            }
        });
    }
}
