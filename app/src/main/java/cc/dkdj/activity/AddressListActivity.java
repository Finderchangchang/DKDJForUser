package cc.dkdj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.config.SaveKey;
import cc.dkdj.model.AddressModel;
import cc.dkdj.view.CommonAdapter;
import cc.dkdj.view.CommonViewHolder;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

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
        address = new ArrayList<>();
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
                String address = addressModel.getAddress();
                if(address.contains("|")){
                    if(address.split("\\|").length==3){
//                        holder.setText(R.id.address_tv, address.split("\\|")[1] + " " + address.split("\\|")[2]);
                        address=address.split("\\|")[1]+" "+address.split("\\|")[2];
                    }else{
                        address=address.replace("\\|"," ");
//                        holder.setText(R.id.address_tv, address.replace("\\|"," "));
                    }
                }
                if(address.length()>13){
                    address=address.substring(0,13)+"...";
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
                Intent intent = new Intent(mInstance, AddAddressActivity.class);
                intent.putExtra("address", address.get(position));
                startActivityForResult(intent, 0);
            }
        });
        load();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv://关闭当前页面
                mInstance.finish();
                break;
            case R.id.right_tv://跳转到新增页面
                startActivityForResult(new Intent(mInstance, AddAddressActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                load();
                break;
        }
    }
    private void load(){
        address = new ArrayList<>();
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
}
