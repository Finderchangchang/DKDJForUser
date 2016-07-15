package cc.dkdj.activity;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.ChangeItem;
import net.tsz.afinal.model.ItemModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dkdj.R;
import cc.dkdj.base.BaseActivity;
import cc.dkdj.config.SaveKey;
import cc.dkdj.fragment.DingDanFragment;
import cc.dkdj.fragment.MainFragment;
import cc.dkdj.fragment.UserFragment;
import cc.dkdj.model.UserModel;
import cc.dkdj.view.HttpUtils;
import cc.dkdj.view.Utils;

/**
 * 主页
 * 作者：lwj on 2016/7/3 15:17
 * 邮箱：1031066280@qq.com
 */
public class MainActivity extends BaseActivity {
    public static MainActivity mInstance;
    @CodeNote(id = R.id.main_ll, click = "onClick")
    LinearLayout main_ll;
    @CodeNote(id = R.id.search_ll, click = "onClick")
    LinearLayout search_ll;
    @CodeNote(id = R.id.setting_ll, click = "onClick")
    LinearLayout setting_ll;
    @CodeNote(id = R.id.main_iv)
    ImageView main_iv;
    @CodeNote(id = R.id.search_iv)
    ImageView search_iv;
    @CodeNote(id = R.id.setting_iv)
    ImageView setting_iv;
    @CodeNote(id = R.id.main_tv)
    TextView main_tv;
    @CodeNote(id = R.id.search_tv)
    TextView search_tv;
    @CodeNote(id = R.id.setting_tv)
    TextView setting_tv;
    //底部菜单相关联
    List<ItemModel> mItems;
    int mClick = 0;
    List<ChangeItem> listbtn;
    MainFragment main_frag;
    DingDanFragment search_frag;
    UserFragment user_frag;
    String userId;
    Map<String,String> map;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mItems = new ArrayList<>();
        listbtn = new ArrayList<>();
        mInstance = this;
        map=new HashMap<>();
    }

    @Override
    public void initEvents() {
        mClick = getIntent().getIntExtra("position", 0);
        mItems.add(new ItemModel("首页", R.mipmap.bottom_menu_home, R.mipmap.bottom_menu_home_sel));
        mItems.add(new ItemModel("订单", R.mipmap.bottom_menu_search, R.mipmap.bottom_menu_search_sel));
        mItems.add(new ItemModel("我的", R.mipmap.bottom_menu_more, R.mipmap.bottom_menu_more_sel));
        listbtn.add(new ChangeItem(main_tv, main_iv));
        listbtn.add(new ChangeItem(search_tv, search_iv));
        listbtn.add(new ChangeItem(setting_tv, setting_iv));
        setItem(mClick);//显示首页
        userId=Utils.ReadString(SaveKey.KEY_UserId);
        if (!userId.equals("")) {
            if (finalDb.findAll(UserModel.class).size() == 0) {
                map.put("userid", userId);
                if (!userId.equals("")) {//当前用户为登录状态
                    //根据用户ID加载当前用户信息
                    HttpUtils.loadJson("GetUserInfo", map, new HttpUtils.LoadJsonListener() {
                        @Override
                        public void load(JSONObject obj) {
                            if (obj != null) {
                                UserModel model = new Gson().fromJson(obj.toString(), UserModel.class);
                                finalDb.save(model);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * 点击事件处理
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_ll:
                setItem(0);
                break;
            case R.id.search_ll:
                setItem(1);
                break;
            case R.id.setting_ll:
                setItem(2);
                break;
        }
    }

    private void setItem(int position) {
        //恢复成未点击状态
        listbtn.get(mClick).getTv().setTextColor(getResources().getColor(R.color.bottom_normal_lab));//正常字体颜色
        listbtn.get(mClick).getIv().setImageResource(mItems.get(mClick).getNormal_img());
        //设置为点击状态
        listbtn.get(position).getTv().setTextColor(getResources().getColor(R.color.bottom_pressed_lab));//点击以后字体颜色
        listbtn.get(position).getIv().setImageResource(mItems.get(position).getPressed_img());
        mClick = position;
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (main_frag != null) {
            transaction.hide(main_frag);
        }
        if (search_frag != null) {
            transaction.hide(search_frag);
        }
        if (user_frag != null) {
            transaction.hide(user_frag);
        }
        switch (position) {
            case 0:
                if (main_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    main_frag = new MainFragment();
                    transaction.add(R.id.main_frag, main_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(main_frag);
                }
                break;
            case 1:
                if (search_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    search_frag = new DingDanFragment();
                    transaction.add(R.id.main_frag, search_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(search_frag);
                }
                break;
            case 2:
                if (user_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    user_frag = new UserFragment();
                    transaction.add(R.id.main_frag, user_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(user_frag);
                }
                break;
        }
        transaction.commit();
    }
}
