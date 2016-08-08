package cc.listviewdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.AddressListActivity;
import cc.listviewdemo.activity.UserInfoActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.RegUserActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 我的页面
 * 作者：lwj on 2016/7/3 15:39
 * 邮箱：1031066280@qq.com
 */
public class UserFragment extends BaseFragment {
    @CodeNote(id = R.id.logined_container, click = "onClick")
    LinearLayout logined_container;
    @CodeNote(id = R.id.username_tv)
    TextView username_tv;
    @CodeNote(id = R.id.iv_my_icon)
    ImageView iv_my_icon;
    @CodeNote(id = R.id.tv_myaddress, click = "onClick")
    TextView tv_myaddress;
    String userId;//当前登录的用户ID
    Map<String, String> map;
    @CodeNote(id=R.id.kefu_tel_tv,click = "onClick")
    TextView kefu_tel_tv;
    private UserModel model;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_user);
        map = new HashMap<>();
    }

    @Override
    public void initEvents() {
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        map.put("userid", userId);
        if (!userId.equals("")) {//当前用户为登录状态
            //根据用户ID加载当前用户信息
            HttpUtils.loadJson("GetUserInfo", map, new HttpUtils.LoadJsonListener() {
                @Override
                public void load(JSONObject obj) {
                    if (obj != null) {
                        model = new Gson().fromJson(obj.toString(), UserModel.class);
                        if (model != null) {
                            if (!model.getPic().equals("")) {//设置头像
                                Glide.with(MainActivity.mInstance)
                                        .load(model.getPic()).error(R.mipmap.user_icon)
                                        .into(iv_my_icon);
                            }
                            username_tv.setText(model.getUsername());
                            Utils.IntentPost(UserInfoActivity.class, new Utils.putListener() {
                                @Override
                                public void put(Intent intent) {
                                    intent.putExtra("user",model);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logined_container:
                if (username_tv.getText().toString().equals("登录/注册")) {
                    Utils.IntentPost(RegUserActivity.class, new Utils.putListener() {
                        @Override
                        public void put(Intent intent) {
                            intent.putExtra("position", 2);
                        }
                    });
                } else {
                    MainActivity.mInstance.ToastShort("敬请期待");
                }
                break;
            case R.id.tv_myaddress:
                if (!userId.equals("")) {
                    Utils.IntentPost(AddressListActivity.class);
                } else {
                    MainActivity.mInstance.ToastShort("请先登录");
                }
                break;
            case R.id.kefu_tel_tv:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4001663779"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
