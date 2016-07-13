package cc.listviewdemo.control.f.userdetail;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.view.Utils;

/**
 * 我的页面
 * 作者：lwj on 2016/7/3 15:39
 * 邮箱：1031066280@qq.com
 */
public class UserFragment extends BaseFragment {
    @CodeNote(id=R.id.logined_container,click = "onClick")
    RelativeLayout logined_container;
    @CodeNote(id=R.id.username_tv)
    TextView username_tv;
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
                    //跳转到登录页面
                }else{
                    //查看用户详细信息
                }
                break;
        }
    }
}
