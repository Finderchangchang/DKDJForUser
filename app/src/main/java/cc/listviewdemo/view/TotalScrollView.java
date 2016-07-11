package cc.listviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/6/26.
 */
public class TotalScrollView extends ScrollView {
    View mView;

    public TotalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.smoothScrollTo(0,20);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            int mipampHeght = mView.getHeight() * 3;
            if (t < mipampHeght) {
                int alp = (int) ((t / (float) mipampHeght) * 255);
                mView.getBackground().setAlpha(alp);
            } else {
                mView.getBackground().setAlpha(255);
            }
        }
    }

    public TotalScrollView(Context context) {
        this(context, null);
    }

    public TotalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 设置顶部TitleBar显示隐藏
     * @param view
     */
    public void setTitleView(View view) {
        mView = view;
        mView.getBackground().setAlpha(0);
    }
}
