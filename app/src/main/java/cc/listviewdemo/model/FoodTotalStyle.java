package cc.listviewdemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 菜品大分类
 * Created by Administrator on 2016/9/5.
 */

public class FoodTotalStyle implements Serializable {

    /**
     * Name : null
     * DataId : 1
     * FoodtId : 6729
     * Title : 味道
     * SelectType : 0
     * Attributes : 不辣?20#中辣?20
     * Inve1 : 1
     * Inve2 :
     * attritems : [{"name":"不辣","price":"20","FoodtId":6729,"DataId":1,"Title":"味道"},{"name":"中辣","price":"20","FoodtId":6729,"DataId":1,"Title":"味道"}]
     */

    private String Name;
    private int DataId;
    private int FoodtId;
    private String Title;
    private int SelectType;
    private String Attributes;
    private int Inve1;
    private String Inve2;
    /**
     * name : 不辣
     * price : 20
     * FoodtId : 6729
     * DataId : 1
     * Title : 味道
     */

    private List<FoodStyle> attritems;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
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

    public int getSelectType() {
        return SelectType;
    }

    public void setSelectType(int SelectType) {
        this.SelectType = SelectType;
    }

    public String getAttributes() {
        return Attributes;
    }

    public void setAttributes(String Attributes) {
        this.Attributes = Attributes;
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

    public List<FoodStyle> getAttritems() {
        return attritems;
    }

    public void setAttritems(List<FoodStyle> attritems) {
        this.attritems = attritems;
    }
}
