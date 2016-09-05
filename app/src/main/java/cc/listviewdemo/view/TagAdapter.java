package cc.listviewdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.model.Taglist;
import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;

/**
 * Created by Administrator on 2016/8/23.
 */
public class TagAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Taglist> mList;
    public TagAdapter(Context context,List<Taglist> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList=list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_main_tag, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(MainActivity.mInstance).load(mList.get(position).getPicture()).asBitmap().centerCrop().into(new MyBitmapImageViewTarget(holder.iv));
        String title=mList.get(position).getTitle();
        if(title.contains("，")){
            holder.tv.setText(title.split("，")[0]);
        }else{
            holder.tv.setText(title);
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}

class ViewHolder {
    public ImageView iv;
    public TextView tv;
}
