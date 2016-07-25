package cc.listviewdemo.model;

import java.io.Serializable;

/**
 * 作者：lwj on 2016/7/12 11:16
 * 邮箱：1031066280@qq.com
 */
public class Goods implements Serializable{
    private int position;//Good选择的位置
    private String PName;
    private String ReveInt1;
    private String ReveInt2;
    private String Material;
    private String sid;
    private String sname;
    private String addprice;
    private String PId;
    private String PNum;
    private String TogoId;
    private String owername;//配送费
    private String PPrice;
    private String Currentprice;
    private String PackageFree;//打包费

    /**
     * PName : 尝试1
     *
     * ReveInt1 : 0
     * ReveInt2 : 0
     * Material : 0
     * sid : 6757 商品ID
     * sname :
     * addprice : 0.00
     * PId : 6757 商品ID
     * PNum : 1 数量
     * TogoId : 914 店铺ID
     * owername : 0.00 配送费
     * PPrice : 0.10 平常价格
     * Currentprice : 0.10 现在价格
     */
    public Goods(String PName, String reveInt1, String reveInt2, String material, String sid, String sname, String addprice, String PId, String togoId, String owername, String PPrice, String currentprice,String PackageFree) {
        this.PName = PName;
        ReveInt1 = reveInt1;
        ReveInt2 = reveInt2;
        Material = material;
        this.sid = sid;
        this.sname = sname;
        this.addprice = addprice;
        this.PId = PId;
        TogoId = togoId;
        this.owername = owername;
        this.PPrice = PPrice;
        Currentprice = currentprice;
        this.PackageFree=PackageFree;
    }

    public String getPackageFree() {
        return PackageFree;
    }

    public void setPackageFree(String packageFree) {
        PackageFree = packageFree;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getReveInt1() {
        return ReveInt1;
    }

    public void setReveInt1(String reveInt1) {
        ReveInt1 = reveInt1;
    }

    public String getReveInt2() {
        return ReveInt2;
    }

    public void setReveInt2(String reveInt2) {
        ReveInt2 = reveInt2;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getAddprice() {
        return addprice;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getPId() {
        return PId;
    }

    public void setPId(String PId) {
        this.PId = PId;
    }

    public String getPNum() {
        return PNum;
    }

    public void setPNum(String PNum) {
        this.PNum = PNum;
    }

    public String getTogoId() {
        return TogoId;
    }

    public void setTogoId(String togoId) {
        TogoId = togoId;
    }

    public String getOwername() {
        return owername;
    }

    public void setOwername(String owername) {
        this.owername = owername;
    }

    public String getPPrice() {
        return PPrice;
    }

    public void setPPrice(String PPrice) {
        this.PPrice = PPrice;
    }

    public String getCurrentprice() {
        return Currentprice;
    }

    public void setCurrentprice(String currentprice) {
        Currentprice = currentprice;
    }
}
