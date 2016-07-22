package cc.listviewdemo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 注册用户界面
 * 作者：lwj on 2016/7/13 19:27
 * 邮箱：1031066280@qq.com
 */
public class RegUserActivity extends BaseActivity {
    public static RegUserActivity mInstance;
    @CodeNote(id = R.id.right_tv, click = "onClick")
    TextView right_tv;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.login_id_et)
    EditText login_id_et;//账号
    @CodeNote(id = R.id.get_code_btn, click = "onClick")
    Button get_code_btn;
    @CodeNote(id = R.id.sms_code_et)
    EditText sms_code_et;
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;//密码
    @CodeNote(id = R.id.re_pwd_et)
    EditText re_pwd_et;//确认密码
    @CodeNote(id = R.id.reg_btn, click = "onClick")
    Button reg_btn;
    int position;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_user);
        mInstance = this;
        map = new HashMap<>();
    }

    String SMS_Code = "";

    @Override
    public void initEvents() {
        position = getIntent().getIntExtra("position", -1);
    }

    private boolean checkEt() {
        if (login_id_et.getText().toString().trim() == "") {
            ToastShort("手机号码不能为空");
            return false;
        } else if (!sms_code_et.getText().toString().trim().equals(SMS_Code)||sms_code_et.getText().toString().trim().equals("")) {
            ToastShort("短信验证码不正确");
            return false;
        } else if (pwd_et.getText().toString().trim() == "") {
            ToastShort("密码不能为空");
            return false;
        } else if (!pwd_et.getText().toString().trim().equals(re_pwd_et.getText().toString().trim())) {
            ToastShort("前后密码不一致，请重新输入");
            return false;
        }
        return true;
    }
    private int recLen = 60;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    get_code_btn.setText(recLen + "s");
                    if (recLen == 0) {
                        timer.cancel();
                        get_code_btn.setClickable(true);
                        get_code_btn.setText("获取验证码");
                    }
                }
            });
        }
    };
    Map<String,String> map;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                Utils.IntentPost(LoginActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        if (position >= 0) {
                            intent.putExtra("position", position);
                        }
                    }
                });
                if (LoginActivity.mInstance != null) {
                    LoginActivity.mInstance.finish();
                }
                mInstance.finish();
                break;
            case R.id.reg_btn://注册按钮
                if (checkEt()) {//验证输入是否正确
                    map=new HashMap<>();
                    map.put("tel",login_id_et.getText().toString().trim());
                    map.put("password",pwd_et.getText().toString().trim());
                    HttpUtils.loadJson("NewRegedit", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                String state=obj.getString("state");
                                if(state.equals("1")){//注册成功
                                    if(LoginActivity.mInstance==null){
                                        Utils.IntentPost(LoginActivity.class, new Utils.putListener() {
                                            @Override
                                            public void put(Intent intent) {
                                                if (position >= 0) {
                                                    intent.putExtra("position", position);
                                                }
                                            }
                                        });
                                    }
                                    mInstance.finish();

                                }
                                if(state.equals("-2")){
                                    ToastShort(obj.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                break;
            case R.id.back_iv:
                mInstance.finish();
                break;
            case R.id.get_code_btn://获得验证码
                if (!login_id_et.getText().toString().trim().equals("")) {
                    get_code_btn.setClickable(false);
                    timer= new Timer();
                    timer.schedule(task, 1000, 1000);
                    recLen=60;
                    if (get_code_btn.getText().equals("获取验证码")) {
                        map.put("type", "0");//用户名
                        map.put("tel", login_id_et.getText().toString().trim());//密码
                        HttpUtils.loadJson("sendcode", map, new HttpUtils.LoadJsonListener() {
                            @Override
                            public void load(JSONObject obj) {
                                try {
                                    if (obj != null) {
                                        if (obj.getString("state").equals("1")) {
                                            SMS_Code = obj.getString("pwd");//获得验证码
                                        } else if(obj.getString("state").equals("-1")){
                                            ToastShort("该手机号已存在，可直接登陆");
                                        }else {
                                            ToastShort("发送失败");
                                            timer.cancel();
                                            get_code_btn.setClickable(true);
                                            get_code_btn.setText("获取验证码");
                                        }
                                    } else {
                                        ToastShort("发送失败！");
                                        timer.cancel();
                                        get_code_btn.setClickable(true);
                                        get_code_btn.setText("获取验证码");
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        });
                    }

                } else {
                    ToastShort("手机号不能为空或者格式不正确！");
                }
                break;
        }
    }
}
