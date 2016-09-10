package cc.listviewdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodStyle;

/**
 * Created by Administrator on 2016/9/8.
 */

public class GoodTasteAdapter extends BaseAdapter {
    private Context context;
    private List<FoodStyle> foodstyle = new ArrayList<FoodStyle>();
    private int selectedPostion = 0;    //选择的位置

    public GoodTasteAdapter(Context context, List<FoodStyle> foodstyle) {
        this.context = context;
        this.foodstyle = foodstyle;
    }

    @Override
    public int getCount() {
        return foodstyle.size();
    }

    @Override
    public Object getItem(int position) {
        return foodstyle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 自定义方法，改变position
     *
     * @param position
     */
    public void setSelection(int position) {
        selectedPostion = position;
        this.notifyDataSetChanged();
    }

    public void refresh(List<FoodStyle> items) {
        this.foodstyle = items;
        notifyDataSetChanged();
    }

    /**
     * 获得当前Tag显示的内容
     *
     * @return
     */
    public String getTagVal() {
        return foodstyle.get(selectedPostion).getName();
    }

    /**
     * 获得当前Tag的Key
     *
     * @return
     */
    public int getTagKey() {
        return foodstyle.get(selectedPostion).getDataId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_size_tag, null);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.tag_val_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(foodstyle.get(position).getName());

        //设置选中位置的字体和背景颜色
        if (selectedPostion == position) {
            holder.mName.setBackgroundResource(R.mipmap.selected_tag);
        } else {
            holder.mName.setBackgroundResource(R.mipmap.unselect_tag);
        }
        return convertView;
    }


    class ViewHolder {
        private TextView mName;
    }
}
