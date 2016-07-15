package cc.dkdj.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cc.dkdj.R;
import cc.dkdj.activity.MainActivity;
import cc.dkdj.fragment.GoodListFragment;
import cc.dkdj.model.FoodDetail;
import cc.dkdj.model.Goods;

/**
 * Description:商品详细信息的适配器
 * author: henry
 * Date: 16-4-20 上午9:58.
 */
public class GoodsDetailsAdapter extends BaseAdapter {
    private GoodListFragment context;
    private List<FoodDetail> FoodDetailList = new ArrayList<>();
    private int count;
    private GoodsClassifyAdapter classifyAdapter;

    public GoodsDetailsAdapter(GoodListFragment context, List<FoodDetail> FoodDetailList,
                               GoodsClassifyAdapter classifyAdapter) {
        this.context = context;
        this.FoodDetailList = FoodDetailList;
        this.classifyAdapter = classifyAdapter;
    }

    @Override
    public int getCount() {
        return FoodDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return FoodDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回修改会的列表
     *
     * @return
     */
    public List<FoodDetail> getList() {
        return FoodDetailList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FoodDetail FoodDetail = FoodDetailList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MainActivity.mInstance, R.layout.item_food_detail, null);
            holder = new ViewHolder();
            holder.mGoodsName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mGoodsPrice = (TextView) convertView.findViewById(R.id.price_tv);
            holder.mGoodsNum = (TextView) convertView.findViewById(R.id.num);
            holder.mMainIv= (ImageView) convertView.findViewById(R.id.main_iv);
            holder.mGoodsAdd = (ImageView) convertView.findViewById(R.id.add_iv);
            holder.mGoodsReduce = (ImageView) convertView.findViewById(R.id.reduce);
            convertView.setTag(holder);
            Glide.with(MainActivity.mInstance)
                    .load(FoodDetail.getIcon()).error(R.mipmap.no_img)
                    .into(holder.mMainIv);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mGoodsName.setText(FoodDetail.getName());
        int num = FoodDetail.getCount();
        //当所点上商品数量为0时，隐藏“减”的图标
        if (num == 0) {
            holder.mGoodsNum.setVisibility(View.GONE);
            holder.mGoodsReduce.setVisibility(View.GONE);
        } else {
            holder.mGoodsNum.setVisibility(View.VISIBLE);
            holder.mGoodsReduce.setVisibility(View.VISIBLE);
        }
        holder.mGoodsPrice.setText(FoodDetail.getStylelist().get(0).getPrice() + "");
        double totalPrice = num * FoodDetailList.get(position).getStylelist().get(0).getPrice();
        FoodDetail.setTotalMoney(totalPrice);
        holder.mGoodsNum.setText(num + "");
        final double price=FoodDetail.getStylelist().get(0).getPrice();
        //设置加减监听事件
        holder.mGoodsReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = FoodDetailList.get(position).getCount();
                count--;
                FoodDetailList.get(position).setCount(count);
                context.calculateTotalPrice(-1, -price, new Goods(FoodDetail.getName(), "0", "0","0", FoodDetail.getFoodID(),"","0.00", FoodDetail.getFoodID(),FoodDetail.getTogoid(),"0.00",price+"",price+""));
                GoodsDetailsAdapter.this.notifyDataSetChanged();
//                BaseApplication.mCar.removeGood(new Good(FoodDetail.getFoodID(),FoodDetail.getName(),price,Double.parseDouble(FoodDetail.getPackageFree())));//从购物车删除商品
//                classifyAdapter.notifyDataSetChanged();

            }
        });
        holder.mGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = FoodDetailList.get(position).getCount();
                count++;
                FoodDetailList.get(position).setCount(count);
                context.calculateTotalPrice(1, price, new Goods(FoodDetail.getName(), "0", "0","0", FoodDetail.getFoodID(),"","0.00", FoodDetail.getFoodID(),FoodDetail.getTogoid(),"0.00",price+"",price+""));
                GoodsDetailsAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void refresh(List<FoodDetail> items) {
        this.FoodDetailList = items;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView mGoodsName;    //商品名称
        private ImageView mMainIv;//商品图片
        private ImageView mSmallIv;//“推荐”小图
        private TextView mGoodsNum;     //商品数量
        private TextView mGoodsPrice;//商品价格
        private ImageView mGoodsAdd;    //增加
        private ImageView mGoodsReduce; //减少
    }
}
