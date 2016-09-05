package cc.listviewdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.model.CommentModel;
import cc.listviewdemo.model.OrderList;
import cc.listviewdemo.model.Taglist;
import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;

/**
 * Created by liuliu on 2015/11/16   16:29
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class CommonViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;

    private CommonViewHolder(Context context, ViewGroup parent, int layoutId,
                             int position) {
        this.mPosition = position;
        this.mContext = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConvertView.setTag(this);
    }


    public CommonViewHolder setTag(int viewId, final List<Taglist> list, final int tag_height) {
//        LinearLayoutForListView lv = getView(viewId);
        ListView lv = getView(viewId);
        final RelativeLayout rv = getView(R.id.tag_rl);
        final TextView isOpen_tv = getView(R.id.is_open_tv);
        TagAdapter adapter = new TagAdapter(mContext, list);
        lv.setAdapter(adapter);
        if (list.size() > 3) {//标签数大于2
            final Button btn = getView(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setText(R.id.activity_num_tv, list.size());
                    String isOpen = isOpen_tv.getText().toString();
                    if (isOpen.equals("2")) {//默认未打开2.打开了
                        rv.setLayoutParams(new LinearLayout.LayoutParams(-1, list.size() * tag_height));
                        isOpen_tv.setText("4");
                        setImageResource(R.id.is_open_iv, R.mipmap.jiantou_up);
                    } else {
                        isOpen_tv.setText("2");
                        rv.setLayoutParams(new LinearLayout.LayoutParams(-1, tag_height * 3));
                        setImageResource(R.id.is_open_iv, R.mipmap.jiantou_down);
                    }
                }
            });
            setVisible(R.id.activity_num_ll, true);
            rv.setLayoutParams(new LinearLayout.LayoutParams(-1, tag_height * 3));
        } else {//标签数小于等于2
            rv.setLayoutParams(new LinearLayout.LayoutParams(-1, tag_height * list.size()));
            setVisible(R.id.activity_num_ll, false);
        }
        return this;
    }

    public CommonViewHolder setHeight(int viewId, int height) {
        LinearLayout view = getView(viewId);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
        return this;
    }

    public CommonViewHolder setMargin(int viewId, int left, int right, int top, int bottom) {
        LinearLayout ll = getView(viewId);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
        layoutParams.setMargins(left, right, top, bottom);//4个参数按顺序分别是左上右下
        ll.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        }
        return (CommonViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, Object text) {
        TextView view = getView(viewId);
        view.setText(text + "");
        return this;
    }

    public CommonViewHolder setCubeImage(int viewId, ImageLoader loader, String url) {
        CubeImageView view = getView(viewId);
        view.loadImage(loader, url);
        return this;
    }

    public CommonViewHolder setGlideImage(int viewId, final String url) {
        final ImageView view = getView(viewId);
        Glide.with(mContext).load(url).asBitmap().centerCrop().placeholder(R.mipmap.no_img).into(new MyBitmapImageViewTarget(view));
        return this;
    }

    public CommonViewHolder setCycleGlideImage(int viewId, String url) {
        ImageView view = getView(viewId);
        if (("").equals(url)) {
            Glide.with(mContext).load(R.mipmap.no_img_no_bk).asBitmap().centerCrop().transform(new GlideCircleTransform(MainActivity.mInstance)).into(new MyBitmapImageViewTarget(view));
        } else {
            Glide.with(mContext).load(url).asBitmap().centerCrop().placeholder(R.mipmap.no_img).transform(new GlideCircleTransform(MainActivity.mInstance)).into(new MyBitmapImageViewTarget(view));
        }
        return this;
    }

    public CommonViewHolder setStar(int viewId, String num) {
        RatingBar rb = getView(viewId);
        if (num != null || ("").equals(num)) {
            rb.setRating(Integer.parseInt(num));
        } else {
            rb.setRating(0);
        }
        return this;
    }

    public CommonViewHolder setListView(int viewId, List<OrderList.OrderlistBean.FoodlistBean> list) {
        ListView view = getView(viewId);
        view.setAdapter(new CommonAdapter<OrderList.OrderlistBean.FoodlistBean>(mContext, list, R.layout.item_good) {
            @Override
            public void convert(CommonViewHolder holder, OrderList.OrderlistBean.FoodlistBean foodlistBean, int position) {
                holder.setText(R.id.name_num_tv, foodlistBean.getFoodname());
                holder.setText(R.id.total_price_tv, "X" + foodlistBean.getNum());
            }
        });
        return this;
    }


    //获取知道textview的值
    public String getText(int viewId) {
        TextView view = getView(viewId);
        return view.getText().toString().trim();
    }

    /**
     * 设置监听时间
     *
     * @param viewId
     * @param listener
     * @return
     */
    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    ;

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public CommonViewHolder setTextColor(int viewId, String color) {
        TextView view = getView(viewId);
        view.setTextColor(Color.parseColor(color));
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public CommonViewHolder setImageDrawable(int viewId, Drawable bm) {
        ImageView view = getView(viewId);
        view.setImageDrawable(bm);
        return this;
    }

    private ImageView setImageTag(int viewId, String url) {
        ImageView view = getView(viewId);
        view.setTag(url);
        return view;
    }

    /**
     * 设置控件显示隐藏
     *
     * @param viewId（控件id）
     * @param result(控件显示隐藏)
     */
    public CommonViewHolder setVisible(int viewId, boolean result) {
        View view = getView(viewId);
        if (result) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        return this;
    }
    public int getPosition() {
        return mPosition;
    }
}
