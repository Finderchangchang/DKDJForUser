package cc.dkdj.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class FoodDetail {
    private int id;
    /**
     * FoodID : 6747
     * Name : 薯格
     * togoid : 914
     * publicgood : 0
     * intro :
     * note :
     * PackageFree : 1
     * icon : http://122.114.94.150//upload/201606/201606172043273165.jpg
     * sale : 0
     * Stylelist : [{"Pic":null,"Name":null,"DataId":6652,"FoodtId":6747,"Title":"","Price":15,"MarkeyPrice":0,"InUser":0,"SaleSum":0,"MaxPerDay":0,"Intro":"","Inve1":0,"Inve2":""}]
     * attrlist : []
     */
    private int Count;
    private String FoodID;
    private String Name;
    private String togoid;
    private String publicgood;
    private String intro;
    private String note;
    private String PackageFree;
    private String icon;
    private String sale;
    private double TotalMoney;

    public double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        TotalMoney = totalMoney;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    /**
     * Pic : null
     * Name : null
     * DataId : 6652
     * FoodtId : 6747
     * Title :
     * Price : 15.0
     * MarkeyPrice : 0.0
     * InUser : 0
     * SaleSum : 0
     * MaxPerDay : 0
     * Intro :
     * Inve1 : 0
     * Inve2 :
     */

    private List<StylelistBean> Stylelist;
    private List<?> attrlist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTogoid() {
        return togoid;
    }

    public void setTogoid(String togoid) {
        this.togoid = togoid;
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

    public String getPackageFree() {
        return PackageFree;
    }

    public void setPackageFree(String PackageFree) {
        this.PackageFree = PackageFree;
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

    public List<StylelistBean> getStylelist() {
        return Stylelist;
    }

    public void setStylelist(List<StylelistBean> Stylelist) {
        this.Stylelist = Stylelist;
    }

    public List<?> getAttrlist() {
        return attrlist;
    }

    public void setAttrlist(List<?> attrlist) {
        this.attrlist = attrlist;
    }

    public static class StylelistBean {
        private Object Pic;
        private Object Name;
        private int DataId;
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

        public Object getPic() {
            return Pic;
        }

        public void setPic(Object Pic) {
            this.Pic = Pic;
        }

        public Object getName() {
            return Name;
        }

        public void setName(Object Name) {
            this.Name = Name;
        }

        public int getDataId() {
            return DataId;
        }

        public void setDataId(int DataId) {
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
