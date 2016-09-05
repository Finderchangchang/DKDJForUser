package cc.listviewdemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 手机号快捷登录和账号密码登录
 * 作者：lwj on 2016/8/9 15:43
 * 邮箱：1031066280@qq.com
 */
public class TelAndLoginActivity extends BaseActivity {
    public static TelAndLoginActivity mInstance;
    @CodeNote(id = R.id.tel_login_tv, click = "onClick")
    Button tel_login_tv;
    @CodeNote(id = R.id.login_tb)
    TitleBar login_tb;
    @CodeNote(id = R.id.num_pwd_login_tv, click = "onClick")
    Button num_pwd_login_tv;
    @CodeNote(id = R.id.tel_login_ll)
    LinearLayout tel_login_ll;
    @CodeNote(id = R.id.num_pwd_login_ll)
    LinearLayout num_pwd_login_ll;
    @CodeNote(id = R.id.find_pwd_tv)
    TextView find_pwd_tv;
    @CodeNote(id = R.id.tel_et)
    EditText tel_et;
    @CodeNote(id = R.id.get_code_btn, click = "onClick")
    Button get_code_btn;
    @CodeNote(id = R.id.code_et)
    EditText code_et;
    @CodeNote(id = R.id.num_et)
    EditText num_et;//登录账号
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    private int recLen = 60;
    Timer timer = new Timer();
    Map<String, String> map;
    String code = "";
    String tel = "";
    private int position = 1;//当前按钮点击位置

    @Override
    public void initViews() {
        setContentView(R.layout.activity_tel_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 11:
                setResult(1);
                mInstance.finish();
                break;
        }
    }

    @Override
    public void initEvents() {
        mInstance = this;
        login_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
        login_tb.setRightClick(new TitleBar.OnRightClick() {
            @Override
            public void onClick() {
                startActivityForResult(new Intent(mInstance, RegUserActivity.class), 22);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tel_login_tv:
                position = 1;
                tel_login_tv.setBackgroundColor(getResources().getColor(R.color.white));
                num_pwd_login_tv.setBackgroundColor(getResources().getColor(R.color.choice_btn_bg));
                tel_login_ll.setVisibility(View.VISIBLE);
                num_pwd_login_ll.setVisibility(View.GONE);
                find_pwd_tv.setVisibility(View.GONE);
                break;
            case R.id.num_pwd_login_tv:
                position = 2;
                tel_login_tv.setBackgroundColor(getResources().getColor(R.color.choice_btn_bg));
                num_pwd_login_tv.setBackgroundColor(getResources().getColor(R.color.white));
                tel_login_ll.setVisibility(View.GONE);
                num_pwd_login_ll.setVisibility(View.VISIBLE);
//                find_pwd_tv.setVisibility(View.VISIBLE);
                break;
            case R.id.login_btn://验证手机号验证码是否为空，在判断合法性
                if (position == 1) {
                    String tels = tel_et.getText().toString().trim();
                    String codes = code_et.getText().toString().trim();
                    if (tels.equals("")) {
                        ToastShort("手机号不能为空");
                    } else if (codes.equals("")) {
                        ToastShort("验证码不能为空");
                    } else if (!tels.equals(tel) || !codes.equals(code)) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setMessage("手机动态码已过期，请重新获取");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    } else {
                        map = new HashMap<>();
                        String keys = Utils.ReadString(SaveKey.KEY_LAT_LON);
                        map.put("type", "3");//快捷登录
                        map.put("tel", tel);//手机号码
                        map.put("lat", keys.split(":")[0]);
                        map.put("lng", keys.split(":")[1]);
                        map.put("province", keys.split(":")[2].split(",")[0]);
                        map.put("city", keys.split(":")[2].split(",")[1]);
                        map.put("area", keys.split(":")[2].split(",")[2]);
                        HttpUtils.loadJson("sendcode", map, new HttpUtils.LoadJsonListener() {
                            @Override
                            public void load(JSONObject obj) {
                                try {
                                    if (obj.getString("msg").equals("用户信息获取成功")) {//跳转到首页
                                        Utils.WriteString(SaveKey.KEY_UserId, obj.getString("userid"));
                                        setResult(1);
                                        mInstance.finish();//直接关闭当前页面
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        });
                    }
                } else if (position == 2) {
                    if (num_et.getText().toString().equals("") || pwd_et.getText().toString().equals("")) {
                        ToastShort("账号或密码不能为空，请重新输入");
                    } else {
                        map = new HashMap<>();
                        map.put("username", num_et.getText().toString().trim());//用户名
                        map.put("password", pwd_et.getText().toString().trim());//密码
                        HttpUtils.loadJson("Login", map, new HttpUtils.LoadJsonListener() {
                            @Override
                            public void load(JSONObject obj) {
                                try {
                                    if (obj != null) {
                                        if (obj.getString("state").equals("1")) {
                                            ToastShort("登录成功");
                                            if (RegUserActivity.mInstance != null) {
                                                RegUserActivity.mInstance.finish();
                                            }
                                            Utils.WriteString(SaveKey.KEY_UserId, obj.getString("userid"));
                                            setResult(1);
                                            mInstance.finish();
                                        } else {
                                            ToastShort("登录失败");
                                        }
                                    }
                                } catch (JSONException e) {
                                    ToastShort("登录失败");
                                }

                            }
                        });
                    }
                }
                break;
            case R.id.get_code_btn://获得验证码按钮
                String tels = tel_et.getText().toString().trim();
                if (tels.equals("")) {
                    ToastShort("手机号码不能为空");
                } else if (!Utils.isMobileNo(tels)) {//检测输入的手机号码是否合法
                    ToastShort("请输入正确的手机号码");
                } else {
                    get_code_btn.setText(recLen + "s重新获取");
                    get_code_btn.setEnabled(false);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recLen--;
                                    get_code_btn.setText(recLen + "s重新获取");
                                    if (recLen == 0) {
                                        timer.cancel();
                                        get_code_btn.setEnabled(true);
                                        get_code_btn.setText("重新获取");
                                        recLen = 60;
                                    }
                                }
                            });
                        }
                    }, 1000, 1000);
                    recLen = 60;
                    tel = tel_et.getText().toString().trim();
                    map = new HashMap<>();
                    map.put("type", "2");//仅发送验证码
                    map.put("tel", tel);//密码
                    HttpUtils.loadJson("sendcode", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                if (obj != null) {
                                    if (obj.getString("msg").equals("ok")) {
                                        code = obj.getString("pwd");//获得验证码
                                        ToastShort("发送成功");
                                    } else {
                                        ToastShort("发送失败");
                                        timer.cancel();
                                    }
                                } else {
                                    ToastShort("发送失败！");
                                    timer.cancel();
                                }
                            } catch (JSONException e) {

                            }
                        }
                    });
                }
                break;
        }
    }
}
