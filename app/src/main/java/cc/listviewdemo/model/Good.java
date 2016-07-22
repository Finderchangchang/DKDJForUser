package cc.listviewdemo.model;

import java.io.Serializable;

/**
 * 作者：lwj on 2016/7/9 17:05
 * 邮箱：1031066280@qq.com
 */
public class Good implements Serializable {
    private String goodId;//商品Id
    private String name;//商品名称
    private int num;//商品数量
    private double packageMoney;//打包费
    private double price;//单价
    private String dnum;
    private String dprice;

    public Good(String name, String dnum, String dprice) {
        this.name = name;
        this.dnum = dnum;
        this.dprice = dprice;
    }

    public String getDnum() {
        return dnum;
    }

    public void setDnum(String dnum) {
        this.dnum = dnum;
    }

    public String getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
        this.dprice = dprice;
    }

    public Good(String goodId, String name, double price,double packageMoney) {
        this.goodId = goodId;
        this.name = name;
        this.price = price;
        this.packageMoney=packageMoney;
    }

    public double getPackageMoney() {
        return packageMoney;
    }

    public void setPackageMoney(double packageMoney) {
        this.packageMoney = packageMoney;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
