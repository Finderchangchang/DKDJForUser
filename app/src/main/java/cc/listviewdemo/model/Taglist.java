package cc.listviewdemo.model;

import java.io.Serializable;

/**
 * 店铺标签
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class Taglist implements Serializable {
    private Object togoname;
    private int IID;
    private int ShopId;
    private Object Url;
    private String Picture;
    private String Title;
    private int Inve1;
    private Object Inve2;
    private int cityid;

    public Object getTogoname() {
        return togoname;
    }

    public void setTogoname(Object togoname) {
        this.togoname = togoname;
    }

    public int getIID() {
        return IID;
    }

    public void setIID(int IID) {
        this.IID = IID;
    }

    public int getShopId() {
        return ShopId;
    }

    public void setShopId(int ShopId) {
        this.ShopId = ShopId;
    }

    public Object getUrl() {
        return Url;
    }

    public void setUrl(Object Url) {
        this.Url = Url;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public int getInve1() {
        return Inve1;
    }

    public void setInve1(int Inve1) {
        this.Inve1 = Inve1;
    }

    public Object getInve2() {
        return Inve2;
    }

    public void setInve2(Object Inve2) {
        this.Inve2 = Inve2;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }
}
