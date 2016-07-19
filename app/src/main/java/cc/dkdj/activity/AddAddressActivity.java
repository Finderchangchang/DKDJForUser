package cc.dkdj.activity;

import android.content.Intent;
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

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.config.SaveKey;
import cc.dkdj.model.AddressModel;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

/**
 * 添加地址
 * 作者：lwj on 2016/7/14 19:44
 * 邮箱：1031066280@qq.com
 */
public class AddAddressActivity extends BaseActivity {
    public static AddAddressActivity mInstance;
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.right_tv)
    TextView right_tv;
    @CodeNote(id = R.id.del_address_btn, click = "onClick")
    Button del_address_btn;
    @CodeNote(id = R.id.save_address_btn, click = "onClick")
    Button save_address_btn;
    @CodeNote(id = R.id.name_et)
    EditText name_et;//联系人
    @CodeNote(id = R.id.tel_et)
    EditText tel_et;
    @CodeNote(id = R.id.address_et)
    EditText address_et;
    @CodeNote(id = R.id.louhao_et)
    EditText louhao_et;
    @CodeNote(id = R.id.xiansheng_cb, click = "onClick")
    CheckBox xiansheng_cb;
    @CodeNote(id = R.id.nvshi_cb, click = "onClick")
    CheckBox nvshi_cb;
    AddressModel model;
    Map<String, String> map;
    String address;
    boolean isAdd = false;//false.编辑.true,新增
    //新增1，删除-1，编辑0
    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_address);
        mInstance = this;
    }

    @Override
    public void initEvents() {
        title_name_tv.setText("编辑地址");
        model = (AddressModel) getIntent().getSerializableExtra("address");
        if (model != null) {
            address = model.getAddress();
            del_address_btn.setVisibility(View.VISIBLE);
            if (!address.contains("|")) {
                address_et.setText(model.getAddress());
                address_et.setSelection(model.getAddress().length());
            } else if (address.contains("|")) {
                if (address.split("\\|").length == 3) {
                    String[] s = address.split("\\|");
                    address_et.setText(s[1]);
                    address_et.setSelection(s[1].length());
                    louhao_et.setText(s[2]);
                }
            }
            String name = model.getReceiver();
            if (name.contains(" ") && name.split(" ").length == 2) {
                name_et.setText(name.split(" ")[0]);//姓名
                if (name.split(" ")[0].equals("先生")) {
                    setSexIsMan(true);
                } else {
                    setSexIsMan(false);
                }
            } else {
                name_et.setText(name);
            }
            tel_et.setText(model.getMobilephone());
        } else {
            isAdd = true;
        }
    }

    /**
     * 设置性别选项
     *
     * @param result true，先生。false，女士
     */
    public void setSexIsMan(boolean result) {
        xiansheng_cb.setChecked(result);
        nvshi_cb.setChecked(!result);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_address_btn://删除按钮
                map = new HashMap<>();
                map.put("userid", Utils.ReadString(SaveKey.KEY_UserId));
                map.put("op", "-1");
                map.put("dataid", model.getDataid());
                HttpUtils.loadJson("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
                    @Override
                    public void load(JSONObject obj) {
                        try {
                            String s = obj.getString("state");
                            if (s.equals("1")) {
                                mInstance.setResult(1, new Intent());
                                mInstance.finish();
                                ToastShort("删除成功");
                            } else {
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
                //修改dataid=0&userid=26&op=0&receiver=胡海珍&address=河北保定|秀兰康欣园|2050室&mobilephone=17093215800
                    //添加userid=26&op=1&receiver=胡海珍&address=河北保定|秀兰康欣园|2050室&mobilephone=17093215800
                if (checkET()) {
                    map = new HashMap<>();
                    map.put("userid", Utils.ReadString(SaveKey.KEY_UserId));
                    if (isAdd) {
                        map.put("op", "1");//新增
                    } else {
                        map.put("op", "0");//编辑
                        map.put("dataid", model.getDataid());//地址编码
                    }
                    String sex="女士";
                    if(xiansheng_cb.isChecked()){
                        sex="先生";
                    }
                    map.put("receiver", name_et.getText().toString().trim()+" "+sex);
                    map.put("address", "|" + address_et.getText().toString().trim() + "|" + louhao_et.getText().toString().trim());
                    map.put("mobilephone", tel_et.getText().toString().trim());
//                  map.put("lat", "");(目前没用到)
//                  map.put("lng", "");
                    HttpUtils.loadJson("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                String s = obj.getString("state");
                                if (Integer.parseInt(s)>0) {
                                    mInstance.setResult(1, new Intent());
                                    mInstance.finish();
                                    if(isAdd){
                                        ToastShort("添加成功");
                                    }else{
                                        ToastShort("修改成功");
                                    }
                                } else {
                                    ToastShort("添加失败");
                                }
                            } catch (JSONException e) {

                            }
                        }
                    });
                }
                break;
            case R.id.back_iv:
                mInstance.finish();
                break;
        }
    }

    private boolean checkET() {
        if (address_et.getText().toString().trim().equals("")) {
            ToastShort("送货地址不能为空");
            return false;
        } else if (louhao_et.getText().toString().trim().equals("")) {
            ToastShort("楼号不能为空");
            return false;
        } else if (name_et.getText().toString().trim().equals("")) {
            ToastShort("收货姓名不能为空");
            return false;
        } else if (tel_et.getText().toString().trim().equals("")) {
            ToastShort("电话不能为空");
            return false;
        }
        return true;
    }
}
