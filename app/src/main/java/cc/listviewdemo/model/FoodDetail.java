package cc.listviewdemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class FoodDetail implements Serializable {
    private int count;
    private String FoodID;
    private String Name;
    private String MaxPerDay;
    private String isDelete;
    private String publicgood;
    private String intro;
    private String note;
    private String FullPrice;
    private String icon;
    private String sale;
    private String istuan;
    private String Weekday;
    private String SortNum;
    private String Remains;
    private String FoodType;
    private String TotalMoney;
    private String PackageFree;//打包费
    private List<FoodTotalStyle> attrlist;

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getPackageFree() {
        return PackageFree;
    }

    public void setPackageFree(String packageFree) {
        PackageFree = packageFree;
    }

    public List<FoodTotalStyle> getAttrlist() {
        return attrlist;
    }

    public void setAttrlist(List<FoodTotalStyle> attrlist) {
        this.attrlist = attrlist;
    }

    /**
     * DataId : 0
     * nprice : 0
     * Price : 2
     * Foodcurrentprice : 0
     */


    private List<FoodstylelistBean> Stylelist;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String FoodID) {
        this.FoodID = FoodID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMaxPerDay() {
        return MaxPerDay;
    }

    public void setMaxPerDay(String MaxPerDay) {
        this.MaxPerDay = MaxPerDay;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getPublicgood() {
        return publicgood;
    }

    public void setPublicgood(String publicgood) {
        this.publicgood = publicgood;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFullPrice() {
        return FullPrice;
    }

    public void setFullPrice(String FullPrice) {
        this.FullPrice = FullPrice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getIstuan() {
        return istuan;
    }

    public void setIstuan(String istuan) {
        this.istuan = istuan;
    }

    public String getWeekday() {
        return Weekday;
    }

    public void setWeekday(String Weekday) {
        this.Weekday = Weekday;
    }

    public String getSortNum() {
        return SortNum;
    }

    public void setSortNum(String SortNum) {
        this.SortNum = SortNum;
    }

    public String getRemains() {
        return Remains;
    }

    public void setRemains(String Remains) {
        this.Remains = Remains;
    }

    public String getFoodType() {
        return FoodType;
    }

    public void setFoodType(String FoodType) {
        this.FoodType = FoodType;
    }

    public List<FoodstylelistBean> getFoodstylelist() {
        return Stylelist;
    }

    public void setFoodstylelist(List<FoodstylelistBean> foodstylelist) {
        this.Stylelist = foodstylelist;
    }

    public static class FoodstylelistBean implements Serializable {
        private String DataId;
        private String nprice;
        private String Price;
        private String Foodcurrentprice;

        public String getDataId() {
            return DataId;
        }

        public void setDataId(String DataId) {
            this.DataId = DataId;
        }

        public String getNprice() {
            return nprice;
        }

        public void setNprice(String nprice) {
            this.nprice = nprice;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getFoodcurrentprice() {
            return Foodcurrentprice;
        }

        public void setFoodcurrentprice(String Foodcurrentprice) {
            this.Foodcurrentprice = Foodcurrentprice;
        }
    }
}
