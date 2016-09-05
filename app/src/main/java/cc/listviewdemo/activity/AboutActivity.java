package cc.listviewdemo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.control.UpdateManager;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.TitleBar;
import cc.listviewdemo.view.Utils;

/**
 * 关于App的相关内容
 * Created by Administrator on 2016/8/26.
 */

public class AboutActivity extends BaseActivity {
    public static AboutActivity mInstance;
    @CodeNote(id = R.id.main_tb)
    TitleBar main_tb;
    @CodeNote(id = R.id.app_version_tv)
    TextView app_version_tv;//app版本
    @CodeNote(id = R.id.check_update_tv, click = "onClick")
    TextView check_update_tv;//检查更新
    @CodeNote(id = R.id.share_app_tv, click = "onClick")
    TextView share_app_tv;//分享

    @Override
    public void initViews() {
        setContentView(R.layout.activity_about_app);
        mInstance = this;
        hander = new UIHandler();
    }

    @Override
    public void initEvents() {
        main_tb.setLeftClick(new TitleBar.OnLeftClick() {
            @Override
            public void onClick() {
                mInstance.finish();//关闭当前页面
            }
        });
        app_version_tv.setText("大可到家V"+Utils.getVersion());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_update_tv://检查更新
                checkForUpdate();
                break;
        }
    }

    private UIHandler hander;
    String dialogStr;
    private String url;
    UpdateManager mUpdateManager;

    private void DownLoad() {
        mInstance.removeDialog(323);
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo(url);
    }

    /**
     * 检查当前版本
     */
    private void checkForUpdate() {
        dialogStr = "正在检查更新请稍后";
        showDialog(322);
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        localHashMap.put("c", "1");
        HttpUtils.loadJson("version", localHashMap, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                removeDialog(322);
                try {
                    JSONObject json = new JSONObject(obj.toString());
                    String version = json.getString("version");
                    Message message = new Message();
                    int oldVersion = Integer.parseInt(Utils.getVersion().replace(".", ""));//当前app版本
                    int newVersion = Integer.parseInt(version.replace(".", ""));//系统最新版本
                    if (newVersion > oldVersion) {//需要更新
                        message.what = 323;
                        url = json.getString("download");
                    } else {
                        message.what = 333;
                    }
                    hander.sendMessage(message); // 发送消息给Handler
                } catch (JSONException e) {

                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int paramInt) {
        Dialog dialog = null;

        if (paramInt == 323) {
            AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                    .setTitle("提示").setMessage("有新版本更新，点击确定下载");
            DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    DownLoad();
                }
            };
            dialog = localBuilder1.setPositiveButton("返回", null)
                    .setNegativeButton("确定", local3).create();
            return dialog;
        }

        if (paramInt == 333) {
            dialog = null;
            AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
                    .setTitle("提示").setMessage("当前版本已经是最新版本");

            DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    removeDialog(333);
                }
            };
            dialog = localBuilder1.setPositiveButton("确定", local3).create();
        }
        return dialog;
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 333: {
                    showDialog(333);
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
}
