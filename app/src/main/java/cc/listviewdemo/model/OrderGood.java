package cc.listviewdemo.model;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：lwj on 2016/7/18 00:05
 * 邮箱：1031066280@qq.com
 */
public class OrderGood {

    /**
     * Num : 1
     * FoodID : 6757
     * FoodPrice : 0.1
     * shopname :
     * foodname : 尝试1
     * activetype : 0
     * activeid : 0
     * package : 0
     * isreview : 0
     */

    private int Num;
    private String FoodID;
    private double FoodPrice;
    private String shopname;
    private String foodname;
    private String activetype;
    private String activeid;
    @SerializedName("package")
    private String packageX;
    private String isreview;

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

    public double getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(double FoodPrice) {
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
}
