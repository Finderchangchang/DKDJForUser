package cc.listviewdemo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.AddressModel;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 地址列表
 * 作者：lwj on 2016/7/9 11:03
 * 邮箱：1031066280@qq.com
 */
public class AddressListActivity extends BaseActivity {
    public static AddressListActivity mInstance;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.right_tv, click = "onClick")
    TextView right_tv;//右侧文字
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;//中间文字
    @CodeNote(id = R.id.main_lv)
    ListView main_lv;
    Map<String, String> map;
    String userId;
    List<AddressModel> address;
    CommonAdapter<AddressModel> mAdapter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_address_list);
        map = new HashMap<>();
        address=new ArrayList<>();
    }

    @Override
    public void initEvents() {
        mInstance = this;
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        right_tv.setText("新增");
        title_name_tv.setText("我的地址");
        map.put("userid", userId);
        mAdapter = new CommonAdapter<AddressModel>(MainActivity.mInstance, address, R.layout.item_address) {
            @Override
            public void convert(CommonViewHolder holder, AddressModel addressModel, int position) {
                holder.setText(R.id.address_tv,addressModel.getAddress());
                holder.setText(R.id.first_name_tv,addressModel.getReceiver());
                holder.setText(R.id.tel_tv,addressModel.getMobilephone());
            }
        };
        main_lv.setAdapter(mAdapter);
        main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Utils.IntentPost(AddAddressActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("address", address.get(position));
                    }
                });
            }
        });
        HttpUtils.loadJson("GetUserAddressList", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    if (obj != null) {
                        JSONArray array = obj.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            address.add(new Gson().fromJson(array.getString(i), AddressModel.class));
                        }
                        mAdapter.refresh(address);
                    }
                } catch (JSONException e) {

                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv://关闭当前页面
                mInstance.finish();
                break;
            case R.id.right_tv://跳转到新增页面
                Utils.IntentPost(AddAddressActivity.class);
                break;
        }
    }
}
