package cc.listviewdemo.activity;

import android.view.View;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.TitleBar;

/**
 * 用户详细信息
 * 作者：lwj on 2016/8/6 17:55
 * 邮箱：1031066280@qq.com
 */
public class UserInfoActivity extends BaseActivity {
    public static UserInfoActivity mInstance;
    @CodeNote(id = R.id.top_title_tb)
    TitleBar top_title_tb;
    UserModel model;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_user_info);
        mInstance=this;
        model= (UserModel) getIntent().getSerializableExtra("user");
    }

    @Override
    public void initEvents() {
        top_title_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
    }
}
