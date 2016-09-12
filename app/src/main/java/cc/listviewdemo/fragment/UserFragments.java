package cc.listviewdemo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.view.Utils;

/**
 * 个人中心
 * Created by Administrator on 2016/9/12.
 */

public class UserFragments extends BaseFragment {
    @CodeNote(id = R.id.setting_iv, click = "onClick")
    ImageView setting_iv;//设置按钮
    @CodeNote(id = R.id.message_iv, click = "onClick")
    ImageView message_iv;//通知按钮
    @CodeNote(id = R.id.user_info_iv, click = "onClick")
    ImageView user_info_iv;//头像
    @CodeNote(id = R.id.user_name_tv, click = "onClick")
    TextView user_name_tv;
    @CodeNote(id = R.id.daijinquan_tv)
    TextView daijinquan_tv;//代金券
    @CodeNote(id = R.id.yue_tv)
    TextView yue_tv;/*余额*/
    @CodeNote(id = R.id.my_order_ll, click = "onClick")
    LinearLayout my_order_ll;//我的订单
    @CodeNote(id = R.id.daifukuan_ll, click = "onClick")
    LinearLayout daifukuan_ll;//待付款
    @CodeNote(id = R.id.jinxing_ll, click = "onClick")
    LinearLayout jinxing_ll;//进行中
    @CodeNote(id = R.id.daipingjia_ll, click = "onClick")
    LinearLayout daipingjia_ll;//待评价
    @CodeNote(id = R.id.tuikuan_ll, click = "onClick")
    LinearLayout tuikuan_ll;//退款
    @CodeNote(id = R.id.shoucang_ll, click = "onClick")
    LinearLayout shoucang_ll;//收藏
    @CodeNote(id = R.id.dizhi_ll, click = "onClick")
    LinearLayout dizhi_ll;//地址
    @CodeNote(id = R.id.share_ll, click = "onClick")
    LinearLayout share_ll;//分享
    @CodeNote(id = R.id.wenti_ll, click = "onClick")
    LinearLayout wenti_ll;//问题
    @CodeNote(id = R.id.tel_ll, click = "onClick")
    LinearLayout tel_ll;//电话
    Dialog dialog;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_users);
        Dialog dialog = Utils.setDialog("内容", new Utils.setSure() {
            @Override
            public void click() {

            }
        }, new Utils.setCancle() {
            @Override
            public void click() {

            }
        });
    }

    @Override
    public void initEvents() {

    }
}
