package cc.dkdj.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lwj on 2016/7/3 16:31 123
 * 邮箱：1031066280@qq.com
 */
public class OrderList {

    /**
     * page : 1
     * total : 9
     * orderlist : [{"OrderID":"16071411355458000","TogoPic":"","TogoName":"四号商户","orderTime":"2016-07-14 11:35:14","TotalPrice":"8.0","State":"2","sendmoney":"5.00","sendstate":"0","IsShopSet":"0","PayMode":"1","paystate":"0","cardpay":"0.0","SendTime":"2016/7/12 12:10:26","eattype":"","Packagefree":"","foodlist":[{"Num":"1","FoodID":"6697","FoodPrice":"3.0","shopname":"","foodname":"素食火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"}]},{"OrderID":"16071409283458000","TogoPic":"","TogoName":"四号商户","orderTime":"2016-07-14 09:28:14","TotalPrice":"15.0","State":"2","sendmoney":"5.00","sendstate":"0","IsShopSet":"0","PayMode":"1","paystate":"0","cardpay":"0.0","SendTime":"2016/7/12 12:10:26","eattype":"","Packagefree":"","foodlist":[{"Num":"2","FoodID":"6697","FoodPrice":"3.0","shopname":"","foodname":"素食火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"},{"Num":"1","FoodID":"6696","FoodPrice":"4.0","shopname":"","foodname":"海鲜火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"}]},{"OrderID":"16071319200558000","TogoPic":"","TogoName":"四号商户","orderTime":"2016-07-13 19:20:13","TotalPrice":"16.0","State":"2","sendmoney":"5.00","sendstate":"0","IsShopSet":"0","PayMode":"1","paystate":"0","cardpay":"0.0","SendTime":"2016/7/12 12:10:26","eattype":"","Packagefree":"","foodlist":[{"Num":"1","FoodID":"6697","FoodPrice":"3.0","shopname":"","foodname":"素食火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"},{"Num":"2","FoodID":"6696","FoodPrice":"4.0","shopname":"","foodname":"海鲜火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"}]},{"OrderID":"16071317500058000","TogoPic":"","TogoName":"四号商户","orderTime":"2016-07-13 17:50:13","TotalPrice":"16.0","State":"2","sendmoney":"5.00","sendstate":"0","IsShopSet":"0","PayMode":"1","paystate":"0","cardpay":"0.0","SendTime":"2016/7/12 12:10:26","eattype":"","Packagefree":"","foodlist":[{"Num":"1","FoodID":"6697","FoodPrice":"3.0","shopname":"","foodname":"素食火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"},{"Num":"2","FoodID":"6696","FoodPrice":"4.0","shopname":"","foodname":"海鲜火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"}]}]
     */

    private String page;
    private String total;
    /**
     * OrderID : 16071411355458000
     * TogoPic :
     * TogoName : 四号商户
     * orderTime : 2016-07-14 11:35:14
     * TotalPrice : 8.0
     * State : 2
     * sendmoney : 5.00
     * sendstate : 0
     * IsShopSet : 0
     * PayMode : 1
     * paystate : 0
     * cardpay : 0.0
     * SendTime : 2016/7/12 12:10:26
     * eattype :
     * Packagefree :
     * foodlist : [{"Num":"1","FoodID":"6697","FoodPrice":"3.0","shopname":"","foodname":"素食火锅","activetype":"0","activeid":"0","package":"0","isreview":"0"}]
     */

    private List<OrderlistBean> orderlist;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OrderlistBean> getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(List<OrderlistBean> orderlist) {
        this.orderlist = orderlist;
    }

    public static class OrderlistBean implements Serializable{
        private String OrderID;
        private String TogoPic;
        private String TogoName;
        private String orderTime;
        private String TotalPrice;
        private String State;
        private String sendmoney;
        private String sendstate;
        private String IsShopSet;
        private String PayMode;
        private String paystate;
        private String cardpay;
        private String SendTime;
        private String eattype;
        private String Packagefree;
        /**
         * Num : 1
         * FoodID : 6697
         * FoodPrice : 3.0
         * shopname :
         * foodname : 素食火锅
         * activetype : 0
         * activeid : 0
         * package : 0
         * isreview : 0
         */

        private List<FoodlistBean> foodlist;

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String OrderID) {
            this.OrderID = OrderID;
        }

        public String getTogoPic() {
            return TogoPic;
        }

        public void setTogoPic(String TogoPic) {
            this.TogoPic = TogoPic;
        }

        public String getTogoName() {
            return TogoName;
        }

        public void setTogoName(String TogoName) {
            this.TogoName = TogoName;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getTotalPrice() {
            return TotalPrice;
        }

        public void setTotalPrice(String TotalPrice) {
            this.TotalPrice = TotalPrice;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getSendmoney() {
            return sendmoney;
        }

        public void setSendmoney(String sendmoney) {
            this.sendmoney = sendmoney;
        }

        public String getSendstate() {
            return sendstate;
        }

        public void setSendstate(String sendstate) {
            this.sendstate = sendstate;
        }

        public String getIsShopSet() {
            return IsShopSet;
        }

        public void setIsShopSet(String IsShopSet) {
            this.IsShopSet = IsShopSet;
        }

        public String getPayMode() {
            return PayMode;
        }

        public void setPayMode(String PayMode) {
            this.PayMode = PayMode;
        }

        public String getPaystate() {
            return paystate;
        }

        public void setPaystate(String paystate) {
            this.paystate = paystate;
        }

        public String getCardpay() {
            return cardpay;
        }

        public void setCardpay(String cardpay) {
            this.cardpay = cardpay;
        }

        public String getSendTime() {
            return SendTime;
        }

        public void setSendTime(String SendTime) {
            this.SendTime = SendTime;
        }

        public String getEattype() {
            return eattype;
        }

        public void setEattype(String eattype) {
            this.eattype = eattype;
        }

        public String getPackagefree() {
            return Packagefree;
        }

        public void setPackagefree(String Packagefree) {
            this.Packagefree = Packagefree;
        }

        public List<FoodlistBean> getFoodlist() {
            return foodlist;
        }

        public void setFoodlist(List<FoodlistBean> foodlist) {
            this.foodlist = foodlist;
        }

        public static class FoodlistBean implements Serializable{
            private String Num;
            private String FoodID;
            private String FoodPrice;
            private String shopname;
            private String foodname;
            private String activetype;
            private String activeid;
            @SerializedName("package")
            private String packageX;
            private String isreview;

            public String getNum() {
                return Num;
            }

            public void setNum(String Num) {
                this.Num = Num;
            }

            public String getFoodID() {
                return FoodID;
            }

            public void setFoodID(String FoodID) {
                this.FoodID = FoodID;
            }

            public String getFoodPrice() {
                return FoodPrice;
            }

            public void setFoodPrice(String FoodPrice) {
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
    }
}
