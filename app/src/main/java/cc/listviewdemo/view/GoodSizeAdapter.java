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
import cc.listviewdemo.model.FoodType;

/**
 * Created by Administrator on 2016/9/8.
 */

public class GoodSizeAdapter extends BaseAdapter {
    private Context context;
    private List<FoodDetail.FoodstylelistBean> foodstyle = new ArrayList<FoodDetail.FoodstylelistBean>();
    private int selectedPostion = 0;    //选择的位置

    public GoodSizeAdapter(Context context, List<FoodDetail.FoodstylelistBean> foodstyle) {
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

    public void refresh(List<FoodDetail.FoodstylelistBean> items) {
        this.foodstyle = items;
        notifyDataSetChanged();
    }

    /**
     * 获得当前Tag显示的内容
     *
     * @return
     */
    public String getTagVal() {
        return foodstyle.get(selectedPostion).getTitle();
    }
    /**
     * 获得当前Tag显示的内容
     *
     * @return
     */
    public double getTagPrice() {
        return foodstyle.get(selectedPostion).getPrice();
    }

    /**
     * 获得当前Tag的Key
     *
     * @return
     */
    public String getTagKey() {
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
        holder.mName.setText(foodstyle.get(position).getTitle());

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
