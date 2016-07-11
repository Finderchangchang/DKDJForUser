package cc.listviewdemo.control.f.main;

import java.util.List;
import cc.listviewdemo.model.Shop;
import cc.listviewdemo.model.ShopType;

/**
 * Created by Administrator on 2016/7/3.
 */
public interface IFMainView {
    /**
     * 加载头部广告
     * @param list    商品类型
     */
    void loadGG(List<String> list);

    /**
     * 加载中间八个Item
     * @param list    商店类型
     */
    void load8Item(List<ShopType> list);

    /**
     * 加载附近商户信息
     * @param record   总数量
     * @param page 当前页数
     * @param total 所有页数
     * @param list 店铺信息合集
     */
    void loadNearSH(int record,int page,int total,List<Shop> list);
}
