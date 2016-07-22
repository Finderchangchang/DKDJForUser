package cc.listviewdemo.activity;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import com.shizhefei.DisplayUtil;

import com.bumptech.glide.Glide;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.fragment.GoodListFragment;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.view.TitleFragmentPagerAdapter;
import cc.listviewdemo.fragment.PingLunFragment;
import cc.listviewdemo.fragment.ShopDetailFragment;

/**
 * 商户信息页面
 * Created by Administrator on 2016/7/3.
 */
public class SHDetailsActivity extends BaseActivity {
    public static SHDetailsActivity mInstance;
    @CodeNote(id = R.id.tab)
    TabLayout tab;
    @CodeNote(id = R.id.moretab_viewPager)
    ViewPager moretab_viewPager;
    @CodeNote(id=R.id.back_iv,click = "onClick")
    ImageView back_iv;//点击返回
    @CodeNote(id=R.id.shop_name_tv)
    TextView shop_name_tv;//顶部名称。可更改
    @CodeNote(id=R.id.shop_img_iv)
    ImageView shop_img_iv;//头像
    @CodeNote(id=R.id.start_price_tv)
    TextView start_price_tv;//起送价
    @CodeNote(id=R.id.pei_song_tv)
    TextView pei_song_tv;//配送费
    @CodeNote(id=R.id.tag_tv)
    TextView tag_tv;//公告
    @CodeNote(id=R.id.collection_btn,click = "onClick")
    Button collection_btn;//收藏按钮
    List<Fragment> fragments = new ArrayList<>();
    public Shop shop;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_detail);
    }

    @Override
    public void initEvents() {
        mInstance = this;
        shop=(Shop)getIntent().getSerializableExtra("Shop");
        Glide.with(MainActivity.mInstance)
                .load(shop.getIcon()).error(R.mipmap.no_img)
                .into(shop_img_iv);
        start_price_tv.setText(shop.getMinmoney());
        shop_name_tv.setText(shop.getTogoName());
        pei_song_tv.setText(shop.getSendmoney());//配送费
        fragments.add(new GoodListFragment());//商品列表
        fragments.add(new PingLunFragment());//评论页面
        fragments.add(new ShopDetailFragment());//商家详细信息页面
        final TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getFragmentManager(), fragments, new String[]{"购买", "评论", "商家信息"});
        moretab_viewPager.setAdapter(adapter);
        tab.setupWithViewPager(moretab_viewPager);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                moretab_viewPager.setCurrentItem(position);
                switch (position) {//item选项不同title显示文本不同。
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHDetailsActivity.mInstance.finish();//关闭页面
            }
        });
    }
}
