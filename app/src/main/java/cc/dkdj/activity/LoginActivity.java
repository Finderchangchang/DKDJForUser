package cc.dkdj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.config.SaveKey;
import cc.dkdj.model.UserModel;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

/**
 * 用户登录
 * 作者：lwj on 2016/7/9 14:57
 * 邮箱：1031066280@qq.com
 */
public class LoginActivity extends BaseActivity {
    public static LoginActivity mInstance;
    @CodeNote(id = R.id.login_account_et)
    EditText login_account_et;//账号
    @CodeNote(id = R.id.login_password_et)
    EditText login_password_et;//密码
    @CodeNote(id = R.id.tv_findpassword, click = "onClick")
    TextView tv_findpassword;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    Map<String, String> map;
    boolean isGWC;
    int position;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        map = new HashMap<>();
        mInstance=this;
    }

    @Override
    public void initEvents() {
        isGWC=getIntent().getBooleanExtra("isGWC",false);
        position=getIntent().getIntExtra("position",-1);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_findpassword:
                Utils.IntentPost(RegUserActivity.class);//跳转到注册页面
                break;
            case R.id.login_btn:
                if (login_account_et.getText().toString().equals("") || login_password_et.getText().toString().equals("")) {
                    ToastShort("账号或密码不能为空，请重新输入");
                } else {
                    map.put("username", login_account_et.getText().toString().trim());//用户名
                    map.put("password", login_password_et.getText().toString().trim());//密码
                    HttpUtils.loadJson("Login", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            try {
                                if (obj != null) {
                                    UserModel model=new Gson().fromJson(obj.toString(),UserModel.class);
                                    if (obj.getString("state").equals("1")) {
                                        ToastShort("登录成功");
                                        if(!isGWC){
                                            if(MainActivity.mInstance!=null){
                                                MainActivity.mInstance.finish();
                                            }
                                            if(position>=0){
                                                Utils.IntentPost(MainActivity.class, new Utils.putListener() {
                                                    @Override
                                                    public void put(Intent intent) {
                                                        intent.putExtra("position",position);
                                                    }
                                                });
                                            }

                                        }
                                        if(RegUserActivity.mInstance!=null){
                                            RegUserActivity.mInstance.finish();
                                        }
                                        Utils.WriteString(SaveKey.KEY_UserId,obj.getString("userid"));
                                        mInstance.finish();//关闭当前页面
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
                break;
        }
    }
}
