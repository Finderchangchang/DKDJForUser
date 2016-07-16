package cc.dkdj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.model.Version;
import cc.dkdj.view.FileDownloadThread;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

/**
 * 作者：lwj on 2016/7/16 09:07
 * 邮箱：1031066280@qq.com
 */
public class StartActivity extends BaseActivity {
    public static StartActivity mInstance;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_start);
        mInstance=this;
    }

    @Override
    public void initEvents() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Utils.IntentPost(MainActivity.class);//延迟并跳页
                mInstance.finish();
            }
        }, 1500);
    }
}
