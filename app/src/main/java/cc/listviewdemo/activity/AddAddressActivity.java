package cc.listviewdemo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 添加地址
 * 作者：lwj on 2016/7/14 19:44
 * 邮箱：1031066280@qq.com
 */
public class AddAddressActivity extends BaseActivity {
    public static AddAddressActivity mInstance;
    @CodeNote(id = R.id.main_tb)
    TitleBar main_tb;
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
    TextView address_et;
    @CodeNote(id = R.id.louhao_et)
    EditText louhao_et;
    @CodeNote(id = R.id.xiansheng_cb, click = "onClick")
    CheckBox xiansheng_cb;
    @CodeNote(id = R.id.nvshi_cb, click = "onClick")
    CheckBox nvshi_cb;
    @CodeNote(id = R.id.address_ll, click = "onClick")
    LinearLayout address_ll;
    AddressModel model;
    Map<String, String> map;
    String address;
    boolean isAdd = false;//false.编辑.true,新增
    String lat_lon;

    //新增1，删除-1，编辑0
    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_address);
        mInstance = this;
        lat_lon = Utils.ReadString(SaveKey.KEY_LAT_LON);
        if (lat_lon != null) {
            vals = lat_lon.split(":");
        }
    }

    private String full_address;

    @Override
    public void initEvents() {
        main_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();//关闭当前页面
            }
        });
        model = (AddressModel) getIntent().getSerializableExtra("address");
        if (model != null) {
            address = model.getAddress();
            del_address_btn.setVisibility(View.VISIBLE);
            if (!address.contains("|")) {
                if (model.getAddress() != null) {
                    full_address = model.getAddress();
                    address_et.setText(model.getAddress().substring(0, 19) + "...");
                }

            } else if (address.contains("|")) {
                if (address.split("\\|").length == 3) {
                    String[] s = address.split("\\|");
                    if (s.length >= 3) {
                        full_address = s[1];
                        if (s[1].length() > 19) {
                            address_et.setText(s[1].substring(0, 19) + "...");
                        } else {
                            address_et.setText(s[1]);
                        }
                        louhao_et.setText(s[2]);
                        louhao_et.setSelection(s[2].length());
                    }

                }
            }
            String name = model.getReceiver();
            if (name.contains(" ") && name.split(" ").length == 2) {
                name_et.setText(name.split(" ")[0]);//姓名
                if (name.split(" ")[1].equals("先生")) {
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

    Dialog dialog = null;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_address_btn://删除按钮
                AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                        .setTitle("提示").setMessage("确定要删除当前地址？");
                dialog = localBuilder1.setPositiveButton("取消", null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                map = new HashMap<>();
                                map.put("userid", Utils.ReadString(SaveKey.KEY_UserId));
                                map.put("op", "-1");
                                map.put("dataid", model.getDataid());
                                HttpUtils.loadSave("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
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
                            }
                        }).create();
                dialog.show();
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
                    String sex = "女士";
                    if (xiansheng_cb.isChecked()) {
                        sex = "先生";
                    }
                    map.put("receiver", name_et.getText().toString().trim() + " " + sex);
                    map.put("address", vals[3] + "|" + full_address + "|" + louhao_et.getText().toString().trim());
                    map.put("mobilephone", tel_et.getText().toString().trim());
                    map.put("lat", vals[1]);
                    map.put("lng", vals[2]);
                    HttpUtils.loadSave("SaveUserAddress", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                String s = obj.getString("state");
                                if (Integer.parseInt(s) > 0) {
                                    mInstance.setResult(1, new Intent());
                                    mInstance.finish();
                                    if (isAdd) {
                                        ToastShort("添加成功");
                                    } else {
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
            case R.id.address_ll:
                startActivityForResult(new Intent(AddAddressActivity.mInstance, GetLocationActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {//重新获取坐标，刷新当前页面
            case 99:
                String address = data.getStringExtra("selectAddress");
                vals = address.split(":");
                Utils.WriteString(SaveKey.KEY_LAT_LON, address);
                full_address = vals[0];
                if (vals[0].length() > 19) {
                    address_et.setText(full_address.substring(0, 19) + "...");
                } else {
                    address_et.setText(full_address);
                }
                break;
        }
    }

    private String[] vals;

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
        } else if (!Utils.isMobileNo(tel_et.getText().toString().trim())) {
            ToastShort("电话号码格式不正确");
            return false;
        }
        return true;
    }
}
