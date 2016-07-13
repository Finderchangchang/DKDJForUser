package cc.listviewdemo.control.a.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import cc.listviewdemo.model.FoodType;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 用户登录
 * 作者：lwj on 2016/7/9 14:57
 * 邮箱：1031066280@qq.com
 */
public class LoginActivity extends BaseActivity {
    @CodeNote(id = R.id.login_account_et)
    EditText login_account_et;//账号
    @CodeNote(id = R.id.login_password_et)
    EditText login_password_et;//密码
    @CodeNote(id = R.id.tv_findpassword, click = "onClick")
    TextView tv_findpassword;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;

    Map<String, String> map;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        map = new HashMap<>();
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_findpassword:
                Utils.IntentPost(null);//跳转到注册页面
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
                                    if (obj.getString("state").equals("1")) {
                                        ToastShort("登录成功");
                                    } else {
                                        ToastShort("登录失败");
                                    }
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
