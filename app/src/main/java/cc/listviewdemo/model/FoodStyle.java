package cc.listviewdemo.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5.
 */

public class FoodStyle implements Serializable {

    /**
     * name : 不辣
     * price : 20
     * FoodtId : 6729
     * DataId : 1
     * Title : 味道
     */

    private String name;
    private String price;
    private int FoodtId;
    private int DataId;
    private String Title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getFoodtId() {
        return FoodtId;
    }

    public void setFoodtId(int FoodtId) {
        this.FoodtId = FoodtId;
    }

    public int getDataId() {
        return DataId;
    }

    public void setDataId(int DataId) {
        this.DataId = DataId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
}
