package cc.listviewdemo.activity;

import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;

/**
 * 订单详细信息
 * 作者：lwj on 2016/7/10 20:15
 * 邮箱：1031066280@qq.com
 */
public class OrderDetailActivity extends BaseActivity {
    @CodeNote(id = R.id.title_name_tv)
    TextView title_name_tv;
    int key;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_order_detail);
        title_name_tv.setText("订单详情");
        key=getIntent().getIntExtra("orderId",0);
    }

    @Override
    public void initEvents() {

    }
}
