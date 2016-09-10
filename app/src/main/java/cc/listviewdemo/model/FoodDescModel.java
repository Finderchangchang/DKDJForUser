package cc.listviewdemo.model;

import com.google.gson.annotations.SerializedName;

/**
 * 我的订单页面解析出来的商品model
 * Created by Administrator on 2016/9/6.
 */

public class FoodDescModel {

    /**
     * Num : 1
     * FoodID : 9049
     * FoodPrice : 1
     * shopname :
     * foodname : 0.01
     * activetype : 0
     * activeid : 0
     * package : 0
     * isreview : 0
     * sid :
     * material : 0
     */

    private int Num;
    private String FoodID;
    private int FoodPrice;
    private String shopname;
    private String foodname;
    private String activetype;
    private String activeid;
    @SerializedName("package")
    private String packageX;
    private String isreview;
    private String sid;
    private String material;

    public int getNum() {
        return Num;
    }

    public void setNum(int Num) {
        this.Num = Num;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String FoodID) {
        this.FoodID = FoodID;
    }

    public int getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(int FoodPrice) {
        this.FoodPrice = FoodPrice;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getActivetype() {
        return activetype;
    }

    public void setActivetype(String activetype) {
        this.activetype = activetype;
    }

    public String getActiveid() {
        return activeid;
    }

    public void setActiveid(String activeid) {
        this.activeid = activeid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getIsreview() {
        return isreview;
    }

    public void setIsreview(String isreview) {
        this.isreview = isreview;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
