package cc.listviewdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.view.LinearLayoutForListView;
import cc.listviewdemo.view.TagAdapter;

/**
 * 商家详细信息
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class ShopDetailFragment extends BaseFragment {
    @CodeNote(id = R.id.shop_tel_tv)
    TextView shop_tel_tv;
    @CodeNote(id = R.id.shop_tel_ll, click = "onClick")
    LinearLayout shop_tel_ll;
    @CodeNote(id = R.id.shop_address_tv)
    TextView shop_address_tv;
    @CodeNote(id = R.id.shop_address_ll, click = "onClick")
    LinearLayout shop_address_ll;
    @CodeNote(id = R.id.shop_send_time_tv)
    TextView shop_send_time_tv;
    @CodeNote(id = R.id.shop_send_time_ll, click = "onClick")
    LinearLayout shop_send_time_ll;
    @CodeNote(id = R.id.tag_gv)
    LinearLayoutForListView tag_gv;
    @CodeNote(id = R.id.bottom_line_ll)
    LinearLayout bottom_line_ll;
    Shop shop;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_shopdetail);
        shop = SHDetailsActivity.mInstance.shop;
    }

    @Override
    public void initEvents() {
        if (shop.getComm() == null) {
            shop_tel_tv.setText("商家电话：无");
        } else {
            shop_tel_tv.setText("商家电话：" + shop.getComm());
        }
        shop_address_tv.setText("商家地址：" + shop.getAddress());
        String sendTime = "";
        if (shop.getTime1Start() != null) {
            sendTime = shop.getTime1Start() + "-" + shop.getTime1End();
        } else if (shop.getTime2Start() != null) {
            sendTime = sendTime + " " + shop.getTime2Start() + "-" + shop.getTime2End();
        }
        shop_send_time_tv.setText("配送时间：" + sendTime);
        TagAdapter adapter = new TagAdapter(SHDetailsActivity.mInstance, shop.getTaglist());
        tag_gv.setAdapter(adapter);


    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.shop_tel_ll:
                if (shop.getComm() == null) {
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shop.getComm()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                break;
        }
    }
}
