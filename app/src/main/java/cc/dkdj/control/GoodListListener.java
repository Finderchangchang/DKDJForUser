package cc.dkdj.control;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dkdj.model.FoodDetail;
import cc.dkdj.model.FoodType;
import cc.dkdj.view.HttpUtils;

/**
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class GoodListListener {
    IFGoodListView mView;
    Map<String, String> foodtype;
    String shopid;

    public GoodListListener(IFGoodListView mView, String shopId) {
        this.mView = mView;
        this.shopid = shopId;
    }

    /**
     * 根据shopId获得数据
     */
    public void loadType() {
        foodtype = new HashMap<>();
        foodtype.put("shopid", shopid);
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

    }

    public void loadFoods(String shopsortid) {
        foodtype = new HashMap<>();
        foodtype.put("shopid", shopid);
        if (!shopsortid.equals("")) {
            foodtype.put("shopsortid", shopsortid);
        }
        foodtype.put("pagesize","100");
        HttpUtils.loadJson("GetFoodListByShopId", foodtype, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    if (obj != null) {
                        List<FoodDetail> list = new ArrayList<>();
                        String page = obj.getString("page");
                        String total = obj.getString("total");
                        JSONArray array = obj.getJSONArray("foodlist");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(new Gson().fromJson(array.getString(i), FoodDetail.class));
                        }
                        mView.loadGoodDetails(list);
                    } else {
                        mView.loadGoodDetails(null);
                    }
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage(), e);
                }
            }
        });
    }
}
