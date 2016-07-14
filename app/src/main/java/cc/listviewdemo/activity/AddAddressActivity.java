package cc.listviewdemo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.AddressModel;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 添加地址
 * 作者：lwj on 2016/7/14 19:44
 * 邮箱：1031066280@qq.com
 */
public class AddAddressActivity extends BaseActivity {
    public static AddAddressActivity mInstance;
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;
    @CodeNote(id=R.id.back_iv,click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.right_tv)
    TextView right_tv;
    @CodeNote(id = R.id.del_address_btn, click = "onClick")
    Button del_address_btn;
    @CodeNote(id=R.id.save_address_btn,click = "onClick")
    Button save_address_btn;
    @CodeNote(id=R.id.name_et)
    EditText name_et;//联系人
    @CodeNote(id=R.id.tel_et)
    EditText tel_et;
    @CodeNote(id=R.id.address_et)
    EditText address_et;
    @CodeNote(id=R.id.louhao_et)
    EditText louhao_et;
    @CodeNote(id=R.id.xiansheng_cb,click = "onClick")
    CheckBox xiansheng_cb;
    @CodeNote(id=R.id.nvshi_cb,click = "onClick")
    CheckBox nvshi_cb;
    AddressModel model;
    Map<String,String> map;
    String address;
    boolean isAdd=false;//false.编辑.true,新增
    //新增1，删除-1，编辑0
    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_address);
        mInstance=this;
    }

    @Override
    public void initEvents() {
        title_name_tv.setText("编辑地址");
        model = (AddressModel) getIntent().getSerializableExtra("address");
        if (model != null) {
            address=model.getAddress();
            del_address_btn.setVisibility(View.VISIBLE);
            if(!address.contains("|")&&!address.equals("")){
                address_et.setText(model.getAddress());
                address_et.setSelection(model.getAddress().length());
            }else if(address.contains("|")){
                if(address.split("|").length==3){
                    String[] s=address.split("|");
                    address_et.setText(s[0]);
                    address_et.setSelection(s[0].length());
                    louhao_et.setText(s[1]);
                }
            }
            String name=model.getReceiver();
            if(name.contains(" ")&&name.split("").length==2){
                name_et.setText(name.split(" ")[0]);//姓名
                if(name.split(" ")[0].equals("先生")){
                    setSexIsMan(true);
                }else{
                    setSexIsMan(false);
                }
            }
            tel_et.setText(model.getMobilephone());
        }else{
            isAdd=true;
        }
    }

    /**
     * 设置性别选项
     * @param result true，先生。false，女士
     */
    public void setSexIsMan(boolean result){
        xiansheng_cb.setChecked(result);
        nvshi_cb.setChecked(!result);
    }
    public void load(){
        HttpUtils.loadJson("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {

            }
        });
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_address_btn://删除按钮
                map=new HashMap<>();
                map.put("userid", Utils.ReadString(SaveKey.KEY_UserId));
                map.put("op","-1");
                map.put("dataid",model.getDataid());
                HttpUtils.loadJson("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
                    @Override
                    public void load(JSONObject obj) {
                        try {
                            String s=obj.getString("state");
                            if(s.equals("1")){
                                mInstance.finish();
                                ToastShort("删除成功");
                            }else{
                                ToastShort("删除失败");
                            }
                        } catch (JSONException e) {

                        }
                    }
                });
                break;
            case R.id.xiansheng_cb:
                xiansheng_cb.setChecked(true);
                nvshi_cb.setChecked(false);
                break;
            case R.id.nvshi_cb:
                xiansheng_cb.setChecked(false);
                nvshi_cb.setChecked(true);
                break;
            case R.id.save_address_btn://保存按钮
                map=new HashMap<>();
                map.put("userid", Utils.ReadString(SaveKey.KEY_UserId));
                if(isAdd){
                    map.put("op","1");
                }else{
                    map.put("op","0");
                }
                map.put("receiver",model.getDataid());
                map.put("address",model.getDataid());
                map.put("mobilephone",tel_et.getText().toString().trim());
                map.put("BuildingName",model.getDataid());
                map.put("buildingid","0");
                map.put("lat","");
                map.put("lng","");
                HttpUtils.loadJson("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
                    @Override
                    public void load(JSONObject obj) {
                        try {
                            String s=obj.getString("state");
                            if(s.equals("1")){
                                mInstance.finish();
                                ToastShort("添加成功");
                            }else{
                                ToastShort("添加失败");
                            }
                        } catch (JSONException e) {

                        }
                    }
                });
                break;
            case R.id.back_iv:
                mInstance.finish();
                break;
        }
    }
}
