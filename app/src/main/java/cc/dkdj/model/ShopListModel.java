package cc.dkdj.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lwj on 2016/7/12 11:40
 * 邮箱：1031066280@qq.com
 */
public class ShopListModel implements Serializable {
    /**
     * http://122.114.94.150/App/Android/SubmitOrder.aspx?ordermodel=[{"Phone":"18920219901","Mobilephone":"18920219901","UserID":"26","Receiver":"先生","CustomerName":"先生","Address":"鞋和女装","PayPassword":"","GainTime":"2016-07-12%2012:10:26","ulng":"115.508560","ulat":"38.893189","state":"6","bid":"","TogoId":"914","Remark":"","PayMode":"1","Ordersource":"3","ShopList":[{"Lng":"115.509015","Lat":"38.894451","TogoId":"914","ItemList":[{"PName":"尝试1","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6757","sname":"","addprice":"0.00","PId":"6757","PNum":"1","TogoId":"914","owername":"0.00","PPrice":"0.10","Currentprice":"0.10"},{"PName":"薯格","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6747","sname":"","addprice":"0.00","PId":"6747","PNum":"1","TogoId":"914","owername":"1.00","PPrice":"15.00","Currentprice":"15.00"}]}]}]
     * Lat : 38.894553
     * TogoId : 913
     * ItemList : [{"PName":"大虾仁","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6732","sname":"","addprice":"0.00","PId":"6732","PNum":"2","TogoId":"913","owername":"0.00","PPrice":"1.00","Currentprice":"1.00"},{"PName":"白蚬子","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6730","sname":"","addprice":"0.00","PId":"6730","PNum":"1","TogoId":"913","owername":"0.00","PPrice":"2.00","Currentprice":"2.00"},{"PName":"黄蚬子","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6729","sname":"","addprice":"0.00","PId":"6729","PNum":"3","TogoId":"913","owername":"0.00","PPrice":"3.00","Currentprice":"3.00"}]
     */
    private String Lng;
    private String Lat;
    private String TogoId;
    @Expose
    private String SendMoney;//配送费
    @Expose
    private String ShopName;//店铺名称

    public ShopListModel(String lng, String lat, String togoId, String sendMoney, String shopName, List<Goods> itemList) {
        Lng = lng;
        Lat = lat;
        TogoId = togoId;
        SendMoney = sendMoney;
        ItemList = itemList;
        this.ShopName = shopName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getSendMoney() {
        return SendMoney;
    }

    public void setSendMoney(String sendMoney) {
        SendMoney = sendMoney;
    }

    /**
     * PName : 大虾仁
     * ReveInt1 : 0
     * ReveInt2 : 0
     * Material : 0
     * sid : 6732
     * sname :
     * addprice : 0.00
     * PId : 6732
     * PNum : 2
     * TogoId : 913
     * owername : 0.00
     * PPrice : 1.00
     * Currentprice : 1.00
     */

    private List<Goods> ItemList;//商品列表

    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getTogoId() {
        return TogoId;
    }

    public void setTogoId(String TogoId) {
        this.TogoId = TogoId;
    }

    public List<Goods> getItemList() {
        return ItemList;
    }

    public void setItemList(List<Goods> ItemList) {
        this.ItemList = ItemList;
    }
}
