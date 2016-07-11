package cc.listviewdemo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Model
 * Created by Administrator on 2016/7/8.
 */
public class GWCar implements Serializable {
    private List<Good> foods;//商品集合
    private String shopName;//店铺名称
    private double psMoney;//配送费
    int position;

    public GWCar() {
        position=-1;
    }

    public List<Good> getFoods() {
        return foods;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setFoods(List<Good> foods) {
        this.foods = foods;
    }

    public double getPsMoney() {
        return psMoney;
    }

    public void setPsMoney(double psMoney) {
        this.psMoney = psMoney;
    }

    public void addGood(Good good){
        if(foods==null){
            foods=new ArrayList<>();
        }
        if(check(good)){
            good.setNum(foods.get(position).getNum()+1);
            foods.remove(position);
            foods.add(position,good);
        }else{
            good.setNum(1);
            foods.add(good);
        }
    }

    /**
     * 存在该id
     * @param good
     * @return true
     */
    private boolean check(Good good){
        for(int i=0;i<foods.size();i++){
            if(foods.get(i).getGoodId()==good.getGoodId()){
                position=i;
                return true;
            }
        }
        return false;
    }
    public void removeGood(Good good){
        if(check(good)){
            int nu=foods.get(position).getNum()-1;
            if(nu==0){
                foods.remove(position);
            }else{
                good.setNum(nu);
                if(foods.get(position).getNum()>=0){
                    foods.remove(position);
                    foods.add(position,good);
                }
            }
        }
    }
}
