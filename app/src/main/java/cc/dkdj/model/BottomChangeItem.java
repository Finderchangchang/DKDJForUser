package cc.dkdj.model;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 作者：lwj on 2016/7/3 15:35
 * 邮箱：1031066280@qq.com
 */
public class BottomChangeItem {
    private TextView tv;
    private ImageView iv;
    private RelativeLayout rl;
    private LinearLayout ll;

    public BottomChangeItem(TextView mtv, ImageView miv) {
        tv = mtv;
        iv = miv;
    }

    public TextView getTv() {
        return tv;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public RelativeLayout getRl() {
        return rl;
    }

    public void setRl(RelativeLayout rl) {
        this.rl = rl;
    }

    public LinearLayout getLl() {
        return ll;
    }

    public void setLl(LinearLayout ll) {
        this.ll = ll;
    }
}
