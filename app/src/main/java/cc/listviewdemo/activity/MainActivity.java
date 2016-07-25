package cc.listviewdemo.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
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

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.ChangeItem;
import net.tsz.afinal.model.ItemModel;
import net.tsz.afinal.view.NormalDialog;

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
import cc.listviewdemo.fragment.DingDanFragment;
import cc.listviewdemo.fragment.MainFragment;
import cc.listviewdemo.fragment.UserFragment;
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

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mItems = new ArrayList<>();
        listbtn = new ArrayList<>();
        mInstance = this;
        map = new HashMap<>();
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
        checkForUpdate();//验证是否需要更新
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
                break;
            case R.id.search_ll:
                setItem(1);
                break;
            case R.id.setting_ll:
                setItem(2);
                break;
        }
    }

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

    Version model;

    /**
     * 检查更新
     */
    private void checkForUpdate() {
        map = new HashMap<>();
        map.put("c", "1");
        HttpUtils.loadJson("version", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                model = new Gson().fromJson(obj.toString(), Version.class);
                String old_version = Utils.getVersion();
                String new_version = model.getVersion();
                boolean result=false;
                if(Integer.parseInt(new_version.substring(0,1))>Integer.parseInt(old_version.substring(0,1))){
                    result=true;
                }else{
                    if(Double.parseDouble(new_version.substring(2))>Double.parseDouble(old_version.substring(2))){
                        result=true;
                    }
                }
                if (result) {//最新版本大于当前版本
                    dialog = new NormalDialog(MainActivity.mInstance);
                    dialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doDownload();
                            dialog.dismiss();
                        }
                    });
                    dialog.setMiddleMessage("有新版本需要更新");
                    dialog.show();
                }
            }
        });
    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/dkdj/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = "dkdj.apk";
        int threadNum = 5;
        String filepath = path + fileName;
        downloadTask task = new downloadTask(model.getDownload(), threadNum, filepath);
        task.start();
    }

    /**
     * 多线程文件下载
     *
     * @author yangxiaolong
     * @2014-8-7
     */
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }
                if (isfinished) {
                    installApk(file);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

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

    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //Log.v("LH", "onSaveInstanceState"+outState);
        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
    }
}
