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
import cc.listviewdemo.model.FoodType;

/**
 * Description:商品分类的适配器
 * author: henry
 * Date: 16-4-20 下午1:47.
 */
public class GoodsClassifyAdapter extends BaseAdapter {
    private Context context;
    private List<FoodType> classifies = new ArrayList<FoodType>();
    private int selectedPostion = 0;    //选择的位置

    public GoodsClassifyAdapter(Context context, List<FoodType> classifies){
        this.context = context;
        this.classifies = classifies;
    }
    @Override
    public int getCount() {
        return classifies.size();
    }

    @Override
    public Object getItem(int position) {
        return classifies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 自定义方法，改变position
     * @param position
     */
    public void clearSelection(int position) {
        selectedPostion = position;
        this.notifyDataSetChanged();
    }
    public void refresh(List<FoodType> items) {
        this.classifies = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.tab_good_type,null);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(classifies.get(position).getSortName());

        //设置选中位置的字体和背景颜色
        if (selectedPostion == position){
            convertView.setBackgroundColor(Color.WHITE);
            holder.mName.setTextColor(Color.GREEN);
        }else{
            convertView.setBackgroundColor(Color.LTGRAY);
            holder.mName.setTextColor(Color.GRAY);
        }
        return convertView;
    }


    class ViewHolder{
        private TextView mName;
    }
}
