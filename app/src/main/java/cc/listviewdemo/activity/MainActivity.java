package cc.listviewdemo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.ChangeItem;
import net.tsz.afinal.model.ItemModel;
import net.tsz.afinal.view.NormalDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.base.BaseApplication;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.control.UpdateManager;
import cc.listviewdemo.fragment.DingDanFragment;
import cc.listviewdemo.fragment.MainFragment;
import cc.listviewdemo.fragment.UserFragment;
import cc.listviewdemo.model.ActivityModel;
import cc.listviewdemo.model.UserModel;
import cc.listviewdemo.model.Version;
import cc.listviewdemo.view.FileDownloadThread;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

/**
 * 主页
 * 作者：lwj on 2016/7/3 15:17
 * 邮箱：1031066280@qq.com
 */
public class MainActivity extends BaseActivity {
    public static MainActivity mInstance;
    @CodeNote(id = R.id.main_ll, click = "onClick")
    LinearLayout main_ll;
    @CodeNote(id = R.id.search_ll, click = "onClick")
    LinearLayout search_ll;
    @CodeNote(id = R.id.setting_ll, click = "onClick")
    LinearLayout setting_ll;
    @CodeNote(id = R.id.main_iv)
    ImageView main_iv;
    @CodeNote(id = R.id.search_iv)
    ImageView search_iv;
    @CodeNote(id = R.id.setting_iv)
    ImageView setting_iv;
    @CodeNote(id = R.id.main_tv)
    TextView main_tv;
    @CodeNote(id = R.id.search_tv)
    TextView search_tv;
    @CodeNote(id = R.id.setting_tv)
    TextView setting_tv;
    //底部菜单相关联
    List<ItemModel> mItems;
    int mClick = 0;
    List<ChangeItem> listbtn;
    MainFragment main_frag;
    DingDanFragment search_frag;
    UserFragment user_frag;
    String userId;
    Map<String, String> map;
    NormalDialog dialog;
    int clickItem = 0;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mItems = new ArrayList<>();
        listbtn = new ArrayList<>();
        mInstance = this;
        map = new HashMap<>();
        hander = new UIHandler();
    }

    @Override
    public void initEvents() {
        mClick = getIntent().getIntExtra("position", 0);
        mItems.add(new ItemModel("首页", R.mipmap.bottom_menu_home, R.mipmap.bottom_menu_home_sel));
        mItems.add(new ItemModel("订单", R.mipmap.bottom_menu_search, R.mipmap.bottom_menu_search_sel));
        mItems.add(new ItemModel("我的", R.mipmap.bottom_menu_more, R.mipmap.bottom_menu_more_sel));
        listbtn.add(new ChangeItem(main_tv, main_iv));
        listbtn.add(new ChangeItem(search_tv, search_iv));
        listbtn.add(new ChangeItem(setting_tv, setting_iv));
        setItem(mClick);//显示首页
        userId = Utils.ReadString(SaveKey.KEY_UserId);
        if (!userId.equals("")) {
            if (finalDb.findAll(UserModel.class).size() == 0) {
                map.put("userid", userId);
                if (!userId.equals("")) {//当前用户为登录状态
                    //根据用户ID加载当前用户信息
                    HttpUtils.loadJson("GetUserInfo", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            if (obj != null) {
                                UserModel model = new Gson().fromJson(obj.toString(), UserModel.class);
                                finalDb.save(model);
                            }
                        }
                    });
                }
            }
        }
        checkForUpdates();//验证是否需要更新
    }

    @Override
    protected void onStart() {
        super.onStart();
        String ind = Utils.ReadString(SaveKey.KEY_Load_Index);
        if (ind == "") {
            Utils.WriteString(SaveKey.KEY_Load_Index, "0");
        } else {
            setItem(Integer.parseInt(ind));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 点击事件处理
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_ll:
                setItem(0);
                Utils.WriteString(SaveKey.KEY_Load_Index, "0");
                break;
            case R.id.search_ll:
                Utils.WriteString(SaveKey.KEY_Load_Index, "1");
                setItem(1);
                IsRefresh2 = true;
                break;
            case R.id.setting_ll:
                setItem(2);
                Utils.WriteString(SaveKey.KEY_Load_Index, "2");
                break;
        }
    }

    boolean IsRefresh2 = false;
    String dialogStr;
    private UIHandler hander;

    private void checkForUpdates() {
        dialogStr = "正在检查更新请稍后";
        showDialog(322);
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        localHashMap.put("c", "1");
        HttpUtils.loadJson("version", localHashMap, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                removeDialog(322);
                if (obj != null) {
                    try {
                        JSONObject json = new JSONObject(obj.toString());
                        String version = json.getString("version");
                        Message message = new Message();
                        int oldVersion = Integer.parseInt(Utils.getVersion().replace(".", ""));//当前app版本
                        int newVersion = Integer.parseInt(version.replace(".", ""));//系统最新版本
                        if (newVersion > oldVersion) {//需要更新
                            message.what = 323;
                            url = json.getString("download");
                            content = json.getString("content");
                        } else {
                            message.what = 333;
                        }
                        hander.sendMessage(message); // 发送消息给Handler
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }

    private String url;
    private String content;

    @Override
    protected Dialog onCreateDialog(int paramInt) {
        Dialog dialog = null;

        if (paramInt == 323) {
            AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                    .setTitle("提示").setMessage(content);
            DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    // 开始下载数据
                    DownLoad();
                }
            };

            dialog = localBuilder1.setPositiveButton("返回", null)
                    .setNegativeButton("下载", local3).create();

            return dialog;
        }

        if (paramInt == 333) {
            AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                    .setTitle("提示").setMessage("当前版本已经是最新版本");

            DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    removeDialog(333);
                }
            };
            dialog = localBuilder1.setPositiveButton("返回", local3).create();
        }

        return dialog;
    }

    UpdateManager mUpdateManager;

    private void DownLoad() {
        MainActivity.this.removeDialog(323);
        //dialogStr = "正在下载最新版系统...";
        //showDialog(322);

        //这里来检测版本是否需要更新
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo(url);
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 333: {
//                    showDialog(333);
                    return;
                }
                case 323: {
                    showDialog(323);
                    return;
                }
            }

            return;
        }
    }


    /**
     * 选择指定Fragment
     *
     * @param position
     */
    private void setItem(int position) {
        //恢复成未点击状态
        listbtn.get(mClick).getTv().setTextColor(getResources().getColor(R.color.bottom_normal_lab));//正常字体颜色
        listbtn.get(mClick).getIv().setImageResource(mItems.get(mClick).getNormal_img());
        //设置为点击状态
        listbtn.get(position).getTv().setTextColor(getResources().getColor(R.color.bottom_pressed_lab));//点击以后字体颜色
        listbtn.get(position).getIv().setImageResource(mItems.get(position).getPressed_img());
        mClick = position;
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (main_frag != null) {
            transaction.hide(main_frag);
        }
        if (search_frag != null) {
            transaction.hide(search_frag);
        }
        if (user_frag != null) {
            transaction.hide(user_frag);
        }
        switch (position) {
            case 0:
                if (main_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    main_frag = new MainFragment();
                    transaction.add(R.id.main_frag, main_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(main_frag);
                }
                break;
            case 1:
                if (search_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    search_frag = new DingDanFragment();
                    transaction.add(R.id.main_frag, search_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(search_frag);
                }
                break;
            case 2:
                if (user_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    user_frag = new UserFragment();
                    transaction.add(R.id.main_frag, user_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(user_frag);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 点击两次关闭APP
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastShort("再按一次退出程序");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BaseApplication.locationService.stop();
        Utils.WriteString(SaveKey.KEY_LAT, "");
        Utils.WriteString(SaveKey.KEY_LON, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //Log.v("LH", "onSaveInstanceState"+outState);
        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
    }
}
