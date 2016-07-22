package cc.listviewdemo.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.model.FoodDetail;

/**
 * 商品详细信息
 * 作者：lwj on 2016/7/21 20:39
 * 邮箱：1031066280@qq.com
 */
public class GoodDetailActivity extends BaseActivity {
    public static GoodDetailActivity mInstance;
    @CodeNote(id = R.id.iv)
    ImageView iv;
    @CodeNote(id=R.id.back_iv,click = "onClick")
    ImageView back_iv;
    @CodeNote(id = R.id.name_tv)
    TextView name_tv;
    @CodeNote(id = R.id.sell_num_tv)
    TextView sell_num_tv;
    @CodeNote(id = R.id.price_tv)
    TextView price_tv;
    @CodeNote(id = R.id.count_tv)
    TextView count_tv;
    FoodDetail detail;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_good_detail);
        mInstance=this;
        detail= (FoodDetail) getIntent().getSerializableExtra("detail");
    }

    @Override
    public void initEvents() {
        Glide.with(mInstance)
                .load(detail.getIcon())
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .crossFade()
                .into(iv);
        name_tv.setText(detail.getName());
        sell_num_tv.setText(detail.getSale());
        price_tv.setText("￥" + detail.getFoodstylelist().get(0).getPrice());
        count_tv.setText(detail.getIntro());
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_iv://关闭当前页面
                mInstance.finish();
                break;
        }
    }
}
