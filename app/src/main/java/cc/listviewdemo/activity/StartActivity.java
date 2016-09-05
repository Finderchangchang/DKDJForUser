package cc.listviewdemo.activity;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.model.ActivityModel;
import cc.listviewdemo.view.HttpUtils;
import cc.listviewdemo.view.Utils;

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
        Utils.WriteString(SaveKey.KEY_Load_Index, "0");
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
