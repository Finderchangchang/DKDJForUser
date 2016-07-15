package cc.dkdj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MeasureListView extends ListView {
    public MeasureListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public MeasureListView(Context context) {
        this(context,null);
    }

    public MeasureListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
