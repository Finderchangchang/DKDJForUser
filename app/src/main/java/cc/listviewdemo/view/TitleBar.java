package cc.listviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.listviewdemo.R;

/**
 * 作者：lwj on 2016/8/6 17:57
 * 邮箱：1031066280@qq.com
 */
public class TitleBar extends LinearLayout {
    Context mContext;
    TextView title_name_tv;
    ImageView back_iv;
    TextView right_tv;

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.total_title, this);
        title_name_tv = (TextView) findViewById(R.id.title_name_tv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        right_tv = (TextView) findViewById(R.id.right_tv);
        back_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftClick != null) {
                    onLeftClick.onClick();
                }
            }
        });
        right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightClick != null) {
                    onRightClick.onClick();
                }
            }
        });
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TitleBar_centerIsBlod:
                    title_name_tv.getPaint().setFakeBoldText(true);
                    break;
                case R.styleable.TitleBar_centerText:
                    title_name_tv.setText(a.getString(attr));
                    break;
                case R.styleable.TitleBar_leftImg:
                    back_iv.setImageResource(a.getResourceId(attr, R.mipmap.icon_back));
                    break;
                case R.styleable.TitleBar_centerTextColor:
                    title_name_tv.setTextColor(a.getColor(attr, getResources().getColor(R.color.white)));
                    break;
                case R.styleable.TitleBar_centerTextSize:
                    title_name_tv.setTextSize(a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())));
                    break;
                case R.styleable.TitleBar_rightText:
                    right_tv.setText(a.getString(attr));
                    break;
                case R.styleable.TitleBar_rightTextColor:
                    right_tv.setTextColor(a.getColor(attr, getResources().getColor(R.color.white)));
                    break;
                case R.styleable.TitleBar_rightTextSize:
                    right_tv.setTextSize(a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics())));
                    break;
                case R.styleable.TitleBar_no_left_img:
                    if(!a.getBoolean(attr,false)){//默认存在（false）
                        back_iv.setVisibility(VISIBLE);
                    }else{
                        back_iv.setVisibility(GONE);
                    }
                    break;
            }
        }
        a.recycle();
    }

    private OnLeftClick onLeftClick;
    private OnRightClick onRightClick;

    public void setLeftClick(OnLeftClick onLeftClick) {
        this.onLeftClick = onLeftClick;
    }

    public void setRightClick(OnRightClick onRightClick) {
        this.onRightClick = onRightClick;
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public interface OnLeftClick {
        void onClick();
    }

    public interface OnRightClick {
        void onClick();
    }
}
