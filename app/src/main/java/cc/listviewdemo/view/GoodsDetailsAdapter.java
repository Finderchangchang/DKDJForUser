package cc.listviewdemo.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.MainActivity;
import cc.listviewdemo.fragment.GoodListFragment;
import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodType;
import cc.listviewdemo.model.Good;
import cc.listviewdemo.model.Goods;

/**
 * Description:商品详细信息的适配器
 * author: henry
 * Date: 16-4-20 上午9:58.
 */
public class GoodsDetailsAdapter extends BaseAdapter {
    private GoodListFragment context;
    private List<FoodDetail> showList = new ArrayList<>();
    private List<Goods> goodsList = new ArrayList<>();
    private List<FoodType> foodTypeList = new ArrayList<>();
    private int count;
    private boolean isOpen;

    public GoodsDetailsAdapter(GoodListFragment context, List<FoodDetail> FoodDetailList, List<FoodType> foodTypes, boolean isOpen) {
        this.context = context;
        this.showList = FoodDetailList;
        foodTypeList = foodTypes;
        this.isOpen = isOpen;
    }

    @Override
    public int getCount() {
        return showList.size();
    }

    @Override
    public Object getItem(int position) {
        return showList.get(position);
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
        return showList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FoodDetail FoodDetail = showList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MainActivity.mInstance, R.layout.item_food_detail, null);
            holder = new ViewHolder();
            holder.mGoodsName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mGoodsPrice = (TextView) convertView.findViewById(R.id.price_tv);
            holder.mGoodsNum = (TextView) convertView.findViewById(R.id.num);
            holder.mMainIv = (ImageView) convertView.findViewById(R.id.main_iv);
            holder.mGoodsAdd = (ImageView) convertView.findViewById(R.id.add_iv);
            holder.mGoodsReduce = (ImageView) convertView.findViewById(R.id.reduce);
            holder.mSmallIv = (ImageView) convertView.findViewById(R.id.notice_iv);
            holder.mSellNum = (TextView) convertView.findViewById(R.id.sell_num_tv);
            holder.mTopTitle = (TextView) convertView.findViewById(R.id.top_title_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!isOpen) {
            holder.mGoodsAdd.setVisibility(View.GONE);
        } else {
            holder.mGoodsAdd.setVisibility(View.VISIBLE);
        }
        for (FoodType type : foodTypeList) {
            if ((position + "").equals(type.getFirstPosition())) {
                holder.mTopTitle.setVisibility(View.VISIBLE);
                holder.mTopTitle.setText(type.getSortName());
                break;
            } else {
                holder.mTopTitle.setVisibility(View.GONE);
            }
        }
        holder.mGoodsName.setText(FoodDetail.getName());
        Glide.with(context).load(FoodDetail.getIcon()).asBitmap().centerCrop()
                .placeholder(R.mipmap.no_img).transform(new GlideRoundTransform(MainActivity.mInstance, 10)).
                into(new MyBitmapImageViewTarget(holder.mMainIv));
        int num = FoodDetail.getCount();
        if (num == 0) {
            holder.mGoodsNum.setVisibility(View.GONE);
            holder.mGoodsReduce.setVisibility(View.GONE);
        } else {
            holder.mGoodsNum.setVisibility(View.VISIBLE);
            holder.mGoodsReduce.setVisibility(View.VISIBLE);
        }
        holder.mSellNum.setText(FoodDetail.getSale());
        holder.mGoodsPrice.setText("￥" + FoodDetail.getFoodstylelist().get(0).getPrice());
        double totalPrice = num * Double.parseDouble(showList.get(position).getFoodstylelist().get(0).getPrice());
        FoodDetail.setTotalMoney("￥" + totalPrice);
        holder.mGoodsNum.setText(num + "");
        holder.mGoodsReduce.setVisibility(View.GONE);//减显示
        holder.mGoodsNum.setVisibility(View.GONE);
        //当所点上商品数量为0时，隐藏“减”的图标
        for (int i = 0; i < goodsList.size(); i++) {
            Goods goods = goodsList.get(i);
            if (FoodDetail.getFoodID().equals(goods.getPId())) {//如果商品信息与列表的商品ID一致，显示当前选择的商品数量
                holder.mGoodsReduce.setVisibility(View.VISIBLE);//减显示
                holder.mGoodsNum.setVisibility(View.VISIBLE);
                holder.mGoodsNum.setText(goods.getPNum());
            }
        }
        final double price = Double.parseDouble(FoodDetail.getFoodstylelist().get(0).getPrice());
        //设置加减监听事件
        holder.mGoodsReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = showList.get(position).getCount();
                count--;
                showList.get(position).setCount(count);
                context.calculateTotalPrice(-1, -price, -Double.parseDouble(FoodDetail.getPackageFree()), new Goods(FoodDetail.getName(), "0", "0", "0", FoodDetail.getFoodID(), "", "0.00", FoodDetail.getFoodID(), FoodDetail.getFoodID(), "0.00", price + "", price + "", FoodDetail.getPackageFree()));
                GoodsDetailsAdapter.this.notifyDataSetChanged();

            }
        });
        holder.mGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = showList.get(position).getCount();
                count++;
                showList.get(position).setCount(count);
                context.calculateTotalPrice(1, price, Double.parseDouble(FoodDetail.getPackageFree()), new Goods(FoodDetail.getName(), "0", "0", "0", FoodDetail.getFoodID(), "", "0.00", FoodDetail.getFoodID(), FoodDetail.getFoodID(), "0.00", price + "", price + "", FoodDetail.getPackageFree()));
                GoodsDetailsAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void refresh(List<Goods> goodses) {
        if (goodses == null) {
            this.goodsList = new ArrayList<>();
        } else {
            goodsList = goodses;
        }
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
        private TextView mSellNum;//销售数量
        private TextView mTopTitle;//顶部文字
    }
}
