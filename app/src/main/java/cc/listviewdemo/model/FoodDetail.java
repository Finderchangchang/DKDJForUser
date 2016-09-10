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
    private List<FoodTotalStyle> attrlist;//口味
    private List<FoodstylelistBean> Stylelist;//规格

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

        /**
         * Pic : null
         * Name : null
         * DataId : 13724
         * FoodtId : 6729
         * Title : 大份
         * Price : 1
         * MarkeyPrice : 0
         * InUser : 0
         * SaleSum : 0
         * MaxPerDay : 10
         * Intro :
         * Inve1 : 0
         * Inve2 :
         */

        private String Pic;
        private String Name;
        private String DataId;
        private int FoodtId;
        private String Title;
        private double Price;
        private double MarkeyPrice;
        private int InUser;
        private int SaleSum;
        private int MaxPerDay;
        private String Intro;
        private int Inve1;
        private String Inve2;

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDataId() {
            return DataId;
        }

        public void setDataId(String DataId) {
            this.DataId = DataId;
        }

        public int getFoodtId() {
            return FoodtId;
        }

        public void setFoodtId(int FoodtId) {
            this.FoodtId = FoodtId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public double getMarkeyPrice() {
            return MarkeyPrice;
        }

        public void setMarkeyPrice(double MarkeyPrice) {
            this.MarkeyPrice = MarkeyPrice;
        }

        public int getInUser() {
            return InUser;
        }

        public void setInUser(int InUser) {
            this.InUser = InUser;
        }

        public int getSaleSum() {
            return SaleSum;
        }

        public void setSaleSum(int SaleSum) {
            this.SaleSum = SaleSum;
        }

        public int getMaxPerDay() {
            return MaxPerDay;
        }

        public void setMaxPerDay(int MaxPerDay) {
            this.MaxPerDay = MaxPerDay;
        }

        public String getIntro() {
            return Intro;
        }

        public void setIntro(String Intro) {
            this.Intro = Intro;
        }

        public int getInve1() {
            return Inve1;
        }

        public void setInve1(int Inve1) {
            this.Inve1 = Inve1;
        }

        public String getInve2() {
            return Inve2;
        }

        public void setInve2(String Inve2) {
            this.Inve2 = Inve2;
        }
    }
}
