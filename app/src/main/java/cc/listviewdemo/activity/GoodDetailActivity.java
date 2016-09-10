package cc.listviewdemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.annotation.view.CodeNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodStyle;
import cc.listviewdemo.model.FoodTotalStyle;
import cc.listviewdemo.model.Good;
import cc.listviewdemo.model.Goods;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.GoodSizeAdapter;
import cc.listviewdemo.view.GoodTasteAdapter;

/**
 * 商品详细信息
 * 作者：lwj on 2016/7/21 20:39
 * 邮箱：1031066280@qq.com
 */
public class GoodDetailActivity extends BaseActivity {
    public static GoodDetailActivity mInstance;
    @CodeNote(id = R.id.iv)
    ImageView iv;
    @CodeNote(id = R.id.back_iv, click = "onClick")
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
    @CodeNote(id = R.id.size_style_gv)
    GridView size_style_gv;

    @CodeNote(id = R.id.kouwei_style_gv)
    GridView kouwei_style_gv;
    GoodTasteAdapter mTasteAdapter;
    List<FoodTotalStyle> mSize;
    List<FoodDetail.FoodstylelistBean> mKouwei;
    @CodeNote(id = R.id.kouwei_ll)
    LinearLayout kouwei_ll;
    @CodeNote(id = R.id.size_ll)
    LinearLayout size_ll;
    @CodeNote(id = R.id.add_gwc_btn, click = "onClick")
    Button add_gwc_btn;
    GoodSizeAdapter mSizeAdapter;
    List<Goods> mGoods;
    List kouweis;//口味集合
    String shopId;
    int click_size = 0;
    int click_kouwei = 0;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_good_detail);
        mInstance = this;
        kouweis = new ArrayList();
        detail = (FoodDetail) getIntent().getSerializableExtra("detail");
        mGoods = (List<Goods>) getIntent().getSerializableExtra("model");
        shopId = getIntent().getStringExtra("shopId");
    }

    @Override
    public void initEvents() {

        if (detail.getFoodstylelist().size() > 1) {
            kouwei_ll.setVisibility(View.VISIBLE);
            size_ll.setVisibility(View.VISIBLE);
        }
        if (detail.getAttrlist().size() > 0) {//口味
            kouweis = detail.getAttrlist().get(0).getAttritems();
            //设置口味的显示
            if (detail.getAttrlist().get(0).getAttritems().size() > 0) {
                mTasteAdapter = new GoodTasteAdapter(mInstance, kouweis);
                kouwei_style_gv.setAdapter(mTasteAdapter);
                kouwei_style_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mTasteAdapter.setSelection(position);
                        click_kouwei = position;
                        num_tv.setText("0");
                        checks(0);
                    }
                });
                mTasteAdapter.setSelection(0);
            }
        }
        //规格
        if (detail.getFoodstylelist().size() == 1) {
            price_tv.setText("￥" + detail.getFoodstylelist().get(0).getPrice());
        } else if (detail.getFoodstylelist().size() > 1) {
            mSizeAdapter = new GoodSizeAdapter(mInstance, detail.getFoodstylelist());
            //规格点击触发事件
            size_style_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    price_tv.setText("￥" + detail.getFoodstylelist().get(position).getPrice());
                    mSizeAdapter.setSelection(position);
                    click_size = position;
                    add_gwc_btn.setVisibility(View.VISIBLE);
                    add_goods_ll.setVisibility(View.GONE);
                    num_tv.setText("0");
                    checks(0);
                }
            });
            size_style_gv.setAdapter(mSizeAdapter);
            mSizeAdapter.setSelection(0);
            price_tv.setText("￥" + detail.getFoodstylelist().get(0).getPrice());
        }

        Glide.with(mInstance)
                .load(detail.getIcon())
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .crossFade()
                .into(iv);
        name_tv.setText(detail.getName());
        sell_num_tv.setText(detail.getSale());
        count_tv.setText(detail.getIntro());
        checks(0);
    }

    private void checks(int num) {
        pei_song_tv.setText("配送费￥" + shopId.split(":")[1]);
        if (mGoods.size() > 0) {
            add_gwc_btn.setVisibility(View.VISIBLE);
            add_goods_ll.setVisibility(View.GONE);
            loadBottom();
            for (int i = 0; i < mGoods.size(); i++) {
                Goods goods = mGoods.get(i);
                if (goods.getSid().equals(mSizeAdapter.getTagKey()) &&
                        goods.getMaterial().equals(mTasteAdapter.getTagVal())) {//规格，口味
                    add_gwc_btn.setVisibility(View.GONE);
                    add_goods_ll.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(goods.getPNum()) > 0) {
                        add_goods_ll.setVisibility(View.VISIBLE);
                    } else {
                        add_goods_ll.setVisibility(View.GONE);
                    }
                    goods.setPNum((Integer.parseInt(goods.getPNum()) + num) + "");
                    num_tv.setText(goods.getPNum());
                    mGoods.remove(i);
                    if (!num_tv.getText().toString().trim().equals("0")) {
                        mGoods.add(i, goods);
                        add_gwc_btn.setVisibility(View.GONE);
                        add_goods_ll.setVisibility(View.VISIBLE);
                    } else {
                        add_gwc_btn.setVisibility(View.VISIBLE);
                        add_goods_ll.setVisibility(View.GONE);
                    }
                    loadBottom();
                }
            }
        }
    }

    private void loadBottom() {
        double money = 0;
        int total_num = 0;
        for (Goods good : mGoods) {
            money += Double.parseDouble(good.getPPrice()) * Integer.parseInt(good.getPNum());
            total_num += Integer.parseInt(good.getPNum());
        }
        price_main.setText("￥" + Math.round(money * 100) / 100.0);

        if (money > Double.parseDouble(shopId.split(":")[2])) {
            billing.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            billing.setText("去结算");
            billing.setEnabled(true);
        } else {
            billing.setBackgroundColor(getResources().getColor(R.color.smallLab));
            billing.setText("￥" + shopId.split(":")[2] + "起送");
            billing.setEnabled(false);
        }
        if (total_num > 0) {
            total_num_tv.setText(total_num + "");
            gwc_rl.setBackgroundResource(R.mipmap.have_goods_gwc);
            total_num_rl.setVisibility(View.VISIBLE);
        } else {
            gwc_rl.setBackgroundResource(R.mipmap.no_good_gwc);
            total_num_rl.setVisibility(View.GONE);
        }
    }

    @CodeNote(id = R.id.billing, click = "onClick")
    Button billing;
    @CodeNote(id = R.id.total_num_rl)
    RelativeLayout total_num_rl;
    @CodeNote(id = R.id.gwc_rl)
    RelativeLayout gwc_rl;
    @CodeNote(id = R.id.add_goods_ll)
    LinearLayout add_goods_ll;
    @CodeNote(id = R.id.price_main)
    TextView price_main;
    @CodeNote(id = R.id.total_num_tv)
    TextView total_num_tv;
    @CodeNote(id = R.id.jian_iv, click = "onClick")
    ImageView jian_iv;
    @CodeNote(id = R.id.num_tv)
    TextView num_tv;
    @CodeNote(id = R.id.add_iv, click = "onClick")
    ImageView add_iv;
    String select_size = "";//选择的型号
    String select_taste = "";//选择的口味
    @CodeNote(id = R.id.pei_song_tv)
    TextView pei_song_tv;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv://关闭当前页面
                Intent innt = new Intent();
                innt.putExtra("model", (Serializable) mGoods);
                setResult(11, innt);
                mInstance.finish();
                break;
            case R.id.add_gwc_btn:
                //点击添加Button肯定执行的添加方法，而不是改变里面的数量)
                add_gwc_btn.setVisibility(View.GONE);
                add_goods_ll.setVisibility(View.VISIBLE);
                mGoods.add(new Goods(Uri.encode(detail.getName()), "0", "0", mTasteAdapter.getTagVal(), mSizeAdapter.getTagKey(),
                        mSizeAdapter.getTagVal(), "0.00", detail.getFoodID(), num_tv.getText().toString().trim(),
                        shopId.split(":")[0], detail.getPackageFree(), mSizeAdapter.getTagPrice() + "", num_tv.getText().toString().trim(), detail.getName()));
                checks(1);
                break;
            case R.id.jian_iv://减
                checks(-1);
                break;
            case R.id.add_iv://(改变里面的数量)
                int now = Integer.parseInt(num_tv.getText().toString().trim());
                num_tv.setText((now + 1) + "");
                checks(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent innt = new Intent();
        innt.putExtra("model", (Serializable) mGoods);
        setResult(11, innt);
        mInstance.finish();
    }
}
