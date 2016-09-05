package cc.listviewdemo.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 查询页面
 * Created by Administrator on 2016/8/17.
 */
public class SearchShopActivity extends BaseActivity {
    public static SearchShopActivity mInstance;
    @CodeNote(id = R.id.search_btn, click = "onClick")
    Button search_btn;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView bar;
    @CodeNote(id = R.id.search_key_word_et)
    EditText search_key_word_et;
    Map<String, String> map;
    List<ShopType> tags;
    @CodeNote(id = R.id.hot_search_gv)
    GridView hot_search_gv;
    CommonAdapter mTag;
    CommonAdapter mShop;
    List<Shop> shopList;
    String key_word = "";
    @CodeNote(id = R.id.search_tb)
    TitleBar search_tb;
    @CodeNote(id = R.id.search_key_word_tv)
    TextView search_key_word_tv;
    @CodeNote(id = R.id.no_internet_ll)
    LinearLayout no_internet_ll;
    @CodeNote(id = R.id.refresh_btn, click = "onClick")
    Button refresh_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_search_shop);
        Utils.WriteString(SaveKey.KEY_Load_Index, "0");
        mInstance = this;
        String keys = getIntent().getStringExtra("keyword");
        if (keys != null) {
            if (keys.contains(":")) {
                key_word = keys.split(":")[0];
                search_tb.setCenterText(keys.split(":")[1]);
            }
        }
        tags = new ArrayList<>();
        shopList = new ArrayList<>();
        mTag = new CommonAdapter<ShopType>(mInstance, tags, R.layout.item_only_tag) {
            @Override
            public void convert(CommonViewHolder holder, ShopType o, int position) {
                holder.setText(R.id.tag_val_tv, o.getSortName());
            }
        };
        mShop = new CommonAdapter<Shop>(mInstance, shopList, R.layout.item_search_shop) {
            @Override
            public void convert(CommonViewHolder holder, Shop shop, int position) {
                holder.setText(R.id.shop_name_tv, shop.getTogoName());
                holder.setCycleGlideImage(R.id.shop_img_iv, shop.getIcon());
                holder.setText(R.id.send_time_tv, shop.getSenttime() + "分钟送达");
            }
        };
        shop_list_lv.setAdapter(mShop);
        hot_search_gv.setAdapter(mTag);
        shop_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(SHDetailsActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("Shop", shopList.get(position));
                    }
                });
            }
        });
        hot_search_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map = new HashMap<>();
                map.put("shoptype", tags.get(position).getSortID());//搜索页tag点击
                initShops();
                search_key_word_tv.setText(tags.get(position).getSortName());//改为搜索的内容
            }
        });
        if (!("").equals(key_word)) {//接受的值不为空，直接加载shop数据，换Title
            search_tb.setVisibility(View.VISIBLE);
            main_title_ll.setVisibility(View.GONE);
            search_key_word_tv.setVisibility(View.GONE);
            map = new HashMap<>();
            map.put("shoptype", key_word);//首页跳转过来的
            initShops();
        } else {
            search_tb.setVisibility(View.GONE);
            main_title_ll.setVisibility(View.VISIBLE);
            loadTag();
        }

    }

    @CodeNote(id = R.id.main_title_ll)
    LinearLayout main_title_ll;
    @CodeNote(id = R.id.shop_list_lv)
    ListView shop_list_lv;
    @CodeNote(id = R.id.no_data_tv)
    TextView no_data_tv;

    private void initShops() {
        if (Utils.isNetworkConnected()) {//联网的时候可以进行网络访问。
            no_internet_ll.setVisibility(View.GONE);
            shop_list_lv.setVisibility(View.VISIBLE);
            hot_search_gv.setVisibility(View.GONE);
            no_data_tv.setVisibility(View.GONE);
            map.put("pageindex", "1");//pageindex=1&pagesize=20&lat=38.893189&lng=115.508560
            map.put("pagesize", "10");
            map.put("lat", Utils.ReadString(SaveKey.KEY_LAT_LON).split(":")[0]);
            map.put("lng", Utils.ReadString(SaveKey.KEY_LAT_LON).split(":")[1]);
            map.put("languageType", "2");

            HttpUtils.loadJson("GetShopListByLocation", map, new HttpUtils.LoadJsonListener() {
                @Override
                public void load(JSONObject obj) {
                    try {
                        shopList = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            shopList.add(new Gson().fromJson(array.getString(i), Shop.class));
                        }
                        if (shopList.size() > 0) {
                            mShop.refresh(shopList);
                        } else {
                            no_data_tv.setVisibility(View.VISIBLE);
                            shop_list_lv.setVisibility(View.GONE);
                            hot_search_gv.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {

                    }
                }
            });
        } else {
            no_internet_ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvents() {
        search_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
        search_key_word_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!("").equals(search_key_word_et.getText().toString().trim())) {//输入内容以后启用查询按钮
                    search_btn.setEnabled(true);
                } else {//禁用查询按钮
                    search_btn.setEnabled(false);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                map = new HashMap<>();
                map.put("shopname", search_key_word_et.getText().toString().trim());
                initShops();
                break;
            case R.id.back_iv:
                mInstance.finish();
                break;
            case R.id.refresh_btn:
                break;
        }
    }

    /**
     * 第一次进来以后加载Tag
     */
    private void loadTag() {
        shop_list_lv.setVisibility(View.GONE);
        hot_search_gv.setVisibility(View.VISIBLE);
        no_data_tv.setVisibility(View.GONE);
        map = new HashMap<>();
        map.put("pid", "0");
        map.put("indexpage", "1");
        map.put("pagesize", "100");
        map.put("languageType", "2");
        HttpUtils.loadJson("GetShopTypeList", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    tags = new ArrayList<ShopType>();
                    JSONArray array = obj.getJSONArray("datalist");
                    for (int i = 0; i < array.length(); i++) {
                        tags.add(new Gson().fromJson(array.getString(i), ShopType.class));
                    }
                    if (tags.size() > 0) {
                        mTag.refresh(tags);
                    } else {
                        no_data_tv.setVisibility(View.VISIBLE);
                        shop_list_lv.setVisibility(View.GONE);
                        hot_search_gv.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                }
            }
        });
    }
}
