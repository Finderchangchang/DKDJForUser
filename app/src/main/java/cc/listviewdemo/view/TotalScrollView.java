package cc.listviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/6/26.
 */
public class TotalScrollView extends ScrollView {
    View mView;
    View mView1;
    View mView2;

    public TotalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.smoothScrollTo(0, 20);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            int mipampHeght = mView.getHeight() * 3;
            if (t < mipampHeght) {
                int alp = (int) ((t / (float) mipampHeght) * 255);
                mView.getBackground().setAlpha(alp);
                mView1.getBackground().setAlpha(255 - alp);
                mView2.getBackground().setAlpha(255 - alp);
            } else {
                mView.getBackground().setAlpha(255);
                mView1.getBackground().setAlpha(0);
                mView2.getBackground().setAlpha(0);
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
     *
     * @param view
     */
    public void setTitleView(View view, View view1, View view2) {
        mView = view;
        mView1 = view1;
        mView2 = view2;
        mView.getBackground().setAlpha(0);
        mView1.getBackground().setAlpha(255);
        mView2.getBackground().setAlpha(255);
    }
}
