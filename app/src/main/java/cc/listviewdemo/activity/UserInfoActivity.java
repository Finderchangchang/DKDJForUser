package cc.listviewdemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 用户详细信息
 * 作者：lwj on 2016/8/6 17:55
 * 邮箱：1031066280@qq.com
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static UserInfoActivity mInstance;
    @CodeNote(id = R.id.top_title_tb)
    TitleBar top_title_tb;
    UserModel model;
    @CodeNote(id = R.id.exit_user_btn, click = "click")
    Button exit_user_btn;
    @CodeNote(id = R.id.touxaing_ll, click = "click")
    LinearLayout touxaing_ll;
    @CodeNote(id = R.id.user_name_ll, click = "click")
    LinearLayout user_name_ll;
    @CodeNote(id = R.id.update_pwd_ll, click = "click")
    LinearLayout update_pwd_ll;
    @CodeNote(id = R.id.bd_tel_ll, click = "click")
    LinearLayout bd_tel_ll;
    PopupWindow pop;
    RelativeLayout menu_view;
    Button photo_manager_btn;
    Button camera_btn;
    Button cancel_btn;
    View view;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_user_info);
        mInstance = this;
        model = (UserModel) getIntent().getSerializableExtra("user");
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件 - 即弹窗的界面
        view = inflater.inflate(R.layout.item_sheet_layout, null);
        menu_view = (RelativeLayout) view.findViewById(R.id.menu_view);
        photo_manager_btn = (Button) view.findViewById(R.id.photo_manager_btn);
        camera_btn = (Button) view.findViewById(R.id.camera_btn);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
    }

    @Override
    public void initEvents() {
        menu_view.setOnClickListener(this);
        photo_manager_btn.setOnClickListener(this);
        camera_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        top_title_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();
            }
        });
        pop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setAnimationStyle(R.style.PopAnimationFade);
        pop.setBackgroundDrawable(new BitmapDrawable());
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.exit_user_btn://退出当前用户
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("确定要退出当前账号？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(10,new Intent());
                        Utils.WriteString(SaveKey.KEY_UserId, "");
                        UserInfoActivity.mInstance.finish();
                    }
                });
                builder.show();
                break;
            case R.id.touxaing_ll://头像点击
//                changePopupWindowState();
                break;
            case R.id.user_name_ll://用户姓名
                break;
            case R.id.update_pwd_ll://修改密码
                break;
            case R.id.bd_tel_ll://绑定手机号
                break;
        }
    }

    @Override
    public void onClick(View v) {
        changePopupWindowState();
        switch (view.getId()) {
            case R.id.photo_manager_btn://调用系统图册
                break;
            case R.id.camera_btn://调用相机
                break;
        }
    }

    /**
     * 改变 PopupWindow 的显示和隐藏
     */
    private void changePopupWindowState() {
        if (pop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
            pop.dismiss();
        } else {
            // 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
            pop.showAtLocation(top_title_tb, Gravity.BOTTOM, 0, 0);
        }
    }
}
