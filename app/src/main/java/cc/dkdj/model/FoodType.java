package cc.dkdj.model;

/**
 * 食物分类
 * 作者：lwj on 2016/7/3 16:27
 * 邮箱：1031066280@qq.com
 */
public class FoodType {
    private int id;
    private String SortID;
    private String SortName;
    private String JOrder;
    private String icon;

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

    public String getJOrder() {
        return JOrder;
    }

    public void setJOrder(String JOrder) {
        this.JOrder = JOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
