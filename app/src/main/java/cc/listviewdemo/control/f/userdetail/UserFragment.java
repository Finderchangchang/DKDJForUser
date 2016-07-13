package cc.listviewdemo.control.f.userdetail;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.control.a.main.MainActivitys;
import cc.listviewdemo.control.a.zhuce.RegUserActivity;
import cc.listviewdemo.view.Utils;

/**
 * 我的页面
 * 作者：lwj on 2016/7/3 15:39
 * 邮箱：1031066280@qq.com
 */
public class UserFragment extends BaseFragment {
    @CodeNote(id=R.id.logined_container,click = "onClick")
    LinearLayout logined_container;
    @CodeNote(id=R.id.username_tv)
    TextView username_tv;
    String userId;//当前登录的用户ID
    @Override
    public void initViews() {
        setContentView(R.layout.frag_user);
    }

    @Override
    public void initEvents() {

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.logined_container:
                if(username_tv.getText().toString().equals("登录/注册")){
                    Utils.IntentPost(RegUserActivity.class);
                }else{
                    MainActivitys.mInstance.ToastShort("敬请期待");
                }
                break;
        }
    }
}
