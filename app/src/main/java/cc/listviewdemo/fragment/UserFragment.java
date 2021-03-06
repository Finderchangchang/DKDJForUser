package cc.listviewdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.AboutActivity;
import cc.listviewdemo.activity.AddressListActivity;
import cc.listviewdemo.activity.TelAndLoginActivity;
import cc.listviewdemo.activity.UserInfoActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.RegUserActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.view.GlideCircleTransform;
import cc.listviewdemo.view.GlideRoundTransform;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.MyBitmapImageViewTarget;
import cc.listviewdemo.view.Utils;

/**
 * 我的页面
 * 作者：lwj on 2016/7/3 15:39
 * 邮箱：1031066280@qq.com
 */
public class UserFragment extends BaseFragment {
    @CodeNote(id = R.id.username_tv)
    TextView username_tv;
    @CodeNote(id = R.id.iv_my_icon, click = "onClick")
    ImageView iv_my_icon;
    @CodeNote(id = R.id.tv_myaddress, click = "onClick")
    TextView tv_myaddress;
    String userId;//当前登录的用户ID
    Map<String, String> map;
    @CodeNote(id = R.id.kefu_tel_tv, click = "onClick")
    TextView kefu_tel_tv;
    @CodeNote(id = R.id.yue_tv)
    TextView yue_tv;
    @CodeNote(id = R.id.daijin_tv)
    TextView daijin_tv;
    @CodeNote(id = R.id.shoucang_tv)
    TextView shoucang_tv;
    @CodeNote(id = R.id.about_app_tv, click = "onClick")
    TextView about_app_tv;

    private UserModel model;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_user);
        map = new HashMap<>();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refreshUI();
    }

    @Override
    public void initEvents() {
        refreshUI();
    }

    /**
     * 刷新当前UI
     */
    private void refreshUI() {
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        if (!Utils.ReadString(SaveKey.KEY_UserId).equals("")) {
            if (!userId.equals("")) {//当前用户为登录状态
                map.put("userid", userId);
                //根据用户ID加载当前用户信息
                if (Utils.isNetworkConnected()) {
                    HttpUtils.loadJson("GetUserInfo", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            if (obj != null) {
                                try {
                                    if (!obj.getString("userid").equals("-1")) {//账户存在
                                        model = new Gson().fromJson(obj.toString(), UserModel.class);
                                        if (model != null) {
                                            if (!model.getPic().equals("")) {//设置头像
                                                Glide.with(MainActivity.mInstance).load(R.mipmap.no_img).asBitmap().centerCrop()
                                                        .placeholder(R.mipmap.no_img).transform(new GlideCircleTransform(MainActivity.mInstance)).
                                                        into(new MyBitmapImageViewTarget(iv_my_icon));
                                            }
                                            username_tv.setText(model.getUsername());
                                        }
                                    } else {//账户不存在
                                        Utils.WriteString(SaveKey.KEY_UserId, "");
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        }
                    });
                }
            }
        } else {
            iv_my_icon.setImageResource(R.mipmap.user_icon);
            username_tv.setText("登录/注册");
            yue_tv.setText("0");
            daijin_tv.setText("0");
            shoucang_tv.setText("0");
        }
    }

    public void onClick(View view) {
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        switch (view.getId()) {
            case R.id.iv_my_icon:
                if (username_tv.getText().toString().equals("登录/注册")) {
                    Intent intent = new Intent(MainActivity.mInstance, TelAndLoginActivity.class);
                    startActivityForResult(intent, 21);
                } else {
                    Intent intent = new Intent(MainActivity.mInstance, UserInfoActivity.class);
                    intent.putExtra("user", model);
                    startActivityForResult(intent, 1);
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
                Utils.WriteString(SaveKey.KEY_Load_Index, "2");
                break;
            case R.id.about_app_tv://关于App相关信息
                Utils.IntentPost(AboutActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10 || resultCode == 1) {//resultCode:10,从注册页面返回。1.从登录页面返回
            refreshUI();
        }
    }
}
