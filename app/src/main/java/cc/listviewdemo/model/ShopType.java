package cc.listviewdemo.model;

/**
 * 店铺分类
 * 作者：lwj on 2016/7/3 16:15
 * 邮箱：1031066280@qq.com
 */
public class ShopType {
    private int id;
    private String SortID;
    private String SortName;
    private String SortPic;
    private String JOrder;

    public ShopType() {
    }

    public ShopType(String sortID, String sortName, String sortPic, String JOrder) {
        SortID = sortID;
        SortName = sortName;
        SortPic = sortPic;
        this.JOrder = JOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSortID() {
        return SortID;
    }

    public void setSortID(String sortID) {
        SortID = sortID;
    }

    public String getSortName() {
        return SortName;
    }

    public void setSortName(String sortName) {
        SortName = sortName;
    }

    public String getSortPic() {
        return SortPic;
    }

    public void setSortPic(String sortPic) {
        SortPic = sortPic;
    }

    public String getJOrder() {
        return JOrder;
    }

    public void setJOrder(String JOrder) {
        this.JOrder = JOrder;
    }
}
