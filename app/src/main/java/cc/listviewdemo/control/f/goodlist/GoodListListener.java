package cc.listviewdemo.control.f.goodlist;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.config.Config;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodType;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;
import cc.listviewdemo.view.HttpUtils;

/**
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class GoodListListener {
    private Context mContext;
    IFGoodListView mView;

    public GoodListListener(Context mContext, IFGoodListView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    /**
     * 根据shopId获得数据
     *
     * @param shopId
     */
    public void load(String shopId) {
        Map<String, String> foodtype = new HashMap<>();
        foodtype.put("shopid", shopId);
        HttpUtils.loadJson("GetFoodTypeListByShopId", foodtype, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    if (obj != null) {
                        List<FoodType> list = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("foodtypelist");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getString(i), FoodType.class));
                        }
                        mView.loadGoodType(list);
                    } else {
                        mView.loadGoodType(null);//未返回数据
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        HttpUtils.loadJson("GetFoodListByShopId", foodtype, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    if(obj!=null) {
                        List<FoodDetail> list = new ArrayList<>();
                        String page = obj.getString("page");
                        String total = obj.getString("total");
                        JSONArray array = obj.getJSONArray("foodlist");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getString(i), FoodDetail.class));
                        }
                        mView.loadGoodDetails(list);
                    }else{
                        mView.loadGoodDetails(null);
                    }
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage(), e);
                }
            }
        });

    }
}
