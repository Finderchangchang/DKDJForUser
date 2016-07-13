package cc.listviewdemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺合集
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class Shop implements Serializable {
    private String DataID;
    private String TogoName;
    private String Grade;
    private String sortname;
    private String address;
    private String icon;
    private String sales;
    private String reason;
    private String desc;
    private String minmoney;
    private String senttime;
    private String shopdiscount;
    private String ptimes;
    private String Time1Start;
    private String Time1End;
    private String Time2Start;
    private String Time2End;
    private String distance;
    private String Star;
    private String lng;
    private String lat;
    private String sendmoney;
    private String status;
    private int seekType;
    private String keyWord;
    private List<Goods> ItemList;

    public Shop(List<Goods> goods, String lat, String lng) {
        this.ItemList = goods;
        this.lat = lat;
        this.lng = lng;
    }

    public List<Goods> getGoodses() {
        return ItemList;
    }

    public void setGoodses(List<Goods> goodses) {
        this.ItemList = goodses;
    }

    /**
     * PName : 尝试1
     * ReveInt1 : 0
     * ReveInt2 : 0
     * Material : 0
     * sid : 6757 商品ID
     * sname :
     * addprice : 0.00
     * PId : 6757 商品ID
     * PNum : 1 数量
     * TogoId : 914 店铺ID
     * owername : 0.00 不用管
     * PPrice : 0.10 价格
     * Currentprice : 0.10 价格
     */

    /**
     * togoname : null
     * IID : 0
     * ShopId : 0
     * Url : null
     * Picture : http://122.114.94.150/images/waimai.png
     * Title : 大可专送
     * Inve1 : 0
     * Inve2 : null
     * cityid : 0
     */

    private List<Taglist> taglist;//标签
    private List<?> promotions;//促销内容

    public String getDataID() {
        return DataID;
    }

    public void setDataID(String DataID) {
        this.DataID = DataID;
    }

    public String getTogoName() {
        return TogoName;
    }

    public void setTogoName(String TogoName) {
        this.TogoName = TogoName;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(String minmoney) {
        this.minmoney = minmoney;
    }

    public String getSenttime() {
        return senttime;
    }

    public void setSenttime(String senttime) {
        this.senttime = senttime;
    }

    public String getShopdiscount() {
        return shopdiscount;
    }

    public void setShopdiscount(String shopdiscount) {
        this.shopdiscount = shopdiscount;
    }

    public String getPtimes() {
        return ptimes;
    }

    public void setPtimes(String ptimes) {
        this.ptimes = ptimes;
    }

    public String getTime1Start() {
        return Time1Start;
    }

    public void setTime1Start(String Time1Start) {
        this.Time1Start = Time1Start;
    }

    public String getTime1End() {
        return Time1End;
    }

    public void setTime1End(String Time1End) {
        this.Time1End = Time1End;
    }

    public String getTime2Start() {
        return Time2Start;
    }

    public void setTime2Start(String Time2Start) {
        this.Time2Start = Time2Start;
    }

    public String getTime2End() {
        return Time2End;
    }

    public void setTime2End(String Time2End) {
        this.Time2End = Time2End;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String Star) {
        this.Star = Star;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSendmoney() {
        return sendmoney;
    }

    public void setSendmoney(String sendmoney) {
        this.sendmoney = sendmoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeekType() {
        return seekType;
    }

    public void setSeekType(int seekType) {
        this.seekType = seekType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<Taglist> getTaglist() {
        return taglist;
    }

    public void setTaglist(List<Taglist> taglist) {
        this.taglist = taglist;
    }

    public List<?> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<?> promotions) {
        this.promotions = promotions;
    }
}
