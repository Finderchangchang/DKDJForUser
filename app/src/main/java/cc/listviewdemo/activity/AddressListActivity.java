package cc.listviewdemo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import cc.listviewdemo.base.BaseApplication;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.AddressModel;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 地址列表
 * 作者：lwj on 2016/7/9 11:03
 * 邮箱：1031066280@qq.com
 */
public class AddressListActivity extends BaseActivity {
    public static AddressListActivity mInstance;
    @CodeNote(id = R.id.no_internet_ll)
    LinearLayout no_internet_ll;
    @CodeNote(id = R.id.main_lv)
    ListView main_lv;
    Map<String, String> map;
    String userId;
    List<AddressModel> address;
    CommonAdapter<AddressModel> mAdapter;
    @CodeNote(id = R.id.main_tb)
    TitleBar main_tb;
    @CodeNote(id = R.id.refresh_btn, click = "onClick")
    Button refresh_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_address_list);
        map = new HashMap<>();
        address = new ArrayList<>();
    }

    boolean onlySelect;//true：仅仅用来选择地址。false，点击进去进行编辑

    @Override
    public void initEvents() {
        mInstance = this;
        onlySelect = getIntent().getBooleanExtra("onlySelect", false);
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        main_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
        main_tb.setRightClick(new TitleBar.OnRightClick() {
            @Override
            public void onClick() {
                startActivityForResult(new Intent(mInstance, AddAddressActivity.class), 0);
            }
        });
        map.put("userid", userId);
        mAdapter = new CommonAdapter<AddressModel>(MainActivity.mInstance, address, R.layout.item_address) {
            @Override
            public void convert(CommonViewHolder holder, AddressModel addressModel, int position) {
                String address = addressModel.getAddress();
                if (address.contains("|")) {
                    if (address.split("\\|").length == 3) {
                        address = address.split("\\|")[1] + " " + address.split("\\|")[2];
                    } else {
                        address = address.replace("\\|", " ");
                    }
                }
                if (address.length() > 13) {
                    address = address.substring(0, 13) + "...";
                }
                holder.setText(R.id.address_tv, address);
                holder.setText(R.id.first_name_tv, addressModel.getReceiver());
                holder.setText(R.id.tel_tv, addressModel.getMobilephone());
            }
        };
        main_lv.setAdapter(mAdapter);
        main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (onlySelect) {//仅仅是为了选择地址
                    Intent intent = new Intent();
                    intent.putExtra("address", address.get(position));
                    setResult(2, intent);
                    Utils.WriteString(SaveKey.KEY_Load_Index, "1");
                    finish();
                } else {//跳转到地址详细信息，可以进行编辑操作
                    Intent intent = new Intent(mInstance, AddAddressActivity.class);
                    intent.putExtra("address", address.get(position));
                    startActivityForResult(intent, 0);
                    Utils.WriteString(SaveKey.KEY_Load_Index, "2");
                }
            }
        });
        load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                load();
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_btn:
                load();
                break;
        }
    }

    private void load() {
        address = new ArrayList<>();
        if (Utils.isNetworkConnected()) {
            no_internet_ll.setVisibility(View.GONE);
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
        } else {
            no_internet_ll.setVisibility(View.VISIBLE);
        }
    }
}
