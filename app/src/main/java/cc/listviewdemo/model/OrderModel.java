package cc.listviewdemo.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lwj on 2016/7/12 10:36
 * 邮箱：1031066280@qq.com
 */
public class OrderModel implements Serializable {

    /**
     * Phone : 18920219901
     * Mobilephone : 18920219901
     * UserID : 26
     * Receiver : 先生
     * CustomerName : 先生
     * Address : 鞋和女装
     * PayPassword :余额支付的时候传支付密码（model=3）
     * GainTime : 2016-07-12%2010:26
     * ulng : 115.508560
     * ulat : 38.893189
     * state : 6
     * bid :
     * TogoId : 914 店铺ID
     * Remark :
     * PayMode : 1支付宝，3余额 5微信
     * Ordersource : 3 订单来源 0：web ;1:wap;2:android;3:ios;
     * ShopList : [{"Lng":"115.509015","Lat":"38.894451","TogoId":"914","ItemList":[{"PName":"尝试1","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6757","sname":"","addprice":"0.00","PId":"6757","PNum":"1","TogoId":"914","owername":"0.00","PPrice":"0.10","Currentprice":"0.10"},{"PName":"薯格","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6747","sname":"","addprice":"0.00","PId":"6747","PNum":"1","TogoId":"914","owername":"1.00","PPrice":"15.00","Currentprice":"15.00"}]}]
     */
    @Expose
    int position;
    private String Phone;
    private String Mobilephone;
    private String UserID;
    private String Receiver;
    private String CustomerName;
    private String Address;
    private String PayPassword;
    private String GainTime;
    private String ulng;//收件人经纬度
    private String ulat;
    private String bid;
    private String TogoId;
    private String Remark;
    private String PayMode;
    private String Ordersource;

    public OrderModel() {

    }

    public OrderModel(String phone, String mobilephone, String userID, String receiver, String customerName, String address, String payPassword, String gainTime, String ulng, String ulat, String bid, String togoId, String ordersource, List<ShopListModel> shop) {
        Phone = phone;
        Mobilephone = mobilephone;
        UserID = userID;
        Receiver = receiver;
        CustomerName = customerName;
        Address = address;
        PayPassword = payPassword;
        GainTime = gainTime;
        this.ulng = ulng;
        this.ulat = ulat;
        this.bid = bid;
        TogoId = togoId;
        Ordersource = ordersource;
        ShopList = shop;
    }
    /**
     * Lng : 115.509015
     * Lat : 38.894451
     * TogoId : 914
     * ItemList : [{"PName":"尝试1","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6757","sname":"","addprice":"0.00","PId":"6757","PNum":"1","TogoId":"914","owername":"0.00","PPrice":"0.10","Currentprice":"0.10"},{"PName":"薯格","ReveInt1":"0","ReveInt2":"0","Material":"0","sid":"6747","sname":"","addprice":"0.00","PId":"6747","PNum":"1","TogoId":"914","owername":"1.00","PPrice":"15.00","Currentprice":"15.00"}]
     */

    private List<ShopListModel> ShopList;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getMobilephone() {
        return Mobilephone;
    }

    public void setMobilephone(String Mobilephone) {
        this.Mobilephone = Mobilephone;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPayPassword() {
        return PayPassword;
    }

    public void setPayPassword(String PayPassword) {
        this.PayPassword = PayPassword;
    }

    public String getGainTime() {
        return GainTime;
    }

    public void setGainTime(String GainTime) {
        this.GainTime = GainTime;
    }

    public String getUlng() {
        return ulng;
    }

    public void setUlng(String ulng) {
        this.ulng = ulng;
    }

    public String getUlat() {
        return ulat;
    }

    public void setUlat(String ulat) {
        this.ulat = ulat;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTogoId() {
        return TogoId;
    }

    public void setTogoId(String TogoId) {
        this.TogoId = TogoId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getPayMode() {
        return PayMode;
    }

    public void setPayMode(String PayMode) {
        this.PayMode = PayMode;
    }

    public String getOrdersource() {
        return Ordersource;
    }

    public void setOrdersource(String Ordersource) {
        this.Ordersource = Ordersource;
    }

    public List<ShopListModel> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListModel> shopList) {
        ShopList = shopList;
    }
}
