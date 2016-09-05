package cc.listviewdemo.fragment;

import android.widget.ListView;

import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.CodeNote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.base.BaseFragment;
import cc.listviewdemo.model.CommentModel;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.HttpUtils;

/**
 * 评论页面
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class PingLunFragment extends BaseFragment {
    @Override
    public void initViews() {
        setContentView(R.layout.frag_pinglun);
    }

    int page_index = 1;
    @CodeNote(id = R.id.pinglun_lv)
    ListView pinglun_lv;
    CommonAdapter<CommentModel> mAdapter;
    List<CommentModel> models;
    Map map;

    @Override
    public void initEvents() {
        models = new ArrayList<>();
        mAdapter = new CommonAdapter<CommentModel>(MainActivity.mInstance, models, R.layout.item_pinglun) {
            @Override
            public void convert(CommonViewHolder holder, CommentModel commentModel, int position) {
                holder.setCycleGlideImage(R.id.user_iv, commentModel.getUserpic());
                if (("").equals(commentModel.getUserName())) {
                    holder.setText(R.id.user_tv, "匿名");
                } else {
                    holder.setText(R.id.user_tv, commentModel.getUserName());
                }
                holder.setText(R.id.desc_tv, commentModel.getComment());
            }
        };
        pinglun_lv.setAdapter(mAdapter);
        map = new HashMap();
        map.put("pagesize", "10");
        map.put("pageindex", page_index);
        map.put("shopid", SHDetailsActivity.mInstance.shop.getDataID());
        HttpUtils.loadJson("reviewlist", map, new HttpUtils.LoadJsonListener() {
            @Override
            public void load(JSONObject obj) {
                try {
                    JSONArray array = obj.getJSONArray("datalist");
                    for (int i = 0; i < array.length(); i++) {
                        models.add(new Gson().fromJson(array.getJSONObject(i).toString(), CommentModel.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }
}
