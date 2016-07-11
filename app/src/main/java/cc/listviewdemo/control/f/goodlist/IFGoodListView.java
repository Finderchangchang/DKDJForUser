package cc.listviewdemo.control.f.goodlist;

import java.util.List;

import cc.listviewdemo.model.FoodDetail;
import cc.listviewdemo.model.FoodType;

/**
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public interface IFGoodListView {
    /**
     * 获得商品分类
     * @param mList
     */
    void loadGoodType(List<FoodType> mList);

    /**
     * 商品详细信息列表
     * @param mList
     */
    void loadGoodDetails(List<FoodDetail> mList);
}
