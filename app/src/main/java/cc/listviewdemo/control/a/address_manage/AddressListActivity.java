package cc.listviewdemo.control.a.address_manage;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.view.CommonAdapter;

/**
 * 地址列表
 * 作者：lwj on 2016/7/9 11:03
 * 邮箱：1031066280@qq.com
 */
public class AddressListActivity extends BaseActivity {
    public static AddressListActivity mInstance;
    @CodeNote(id = R.id.back_iv, click = "onClick")
    ImageView back_iv;
    @CodeNote(id=R.id.right_tv)
    TextView right_tv;//右侧文字
    @CodeNote(id=R.id.title_name_tv)
    TextView title_name_tv;//中间文字
    @CodeNote(id=R.id.main_lv)
    ListView main_lv;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_address_list);
    }

    @Override
    public void initEvents() {
        mInstance = this;
        right_tv.setText("新增");
        title_name_tv.setText("我的地址");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv://关闭当前页面
                mInstance.finish();
                break;
        }
    }
}
