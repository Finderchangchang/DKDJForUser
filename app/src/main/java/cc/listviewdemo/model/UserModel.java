package cc.listviewdemo.model;

import java.io.Serializable;

/**
 * 用户表
 * 作者：lwj on 2016/7/3 16:31
 * 邮箱：1031066280@qq.com
 */
public class UserModel implements Serializable{
    private int id;
    /**
     * count : 1
     * userid : 26
     * username : 17093215800
     * truename :
     * email :
     * qq :
     * phone : 17093215800
     * HaveMoney : 0.00
     * Point : 50
     * historypoint : 0
     * publicgood : 0
     * gradename :
     * pic :
     */

    private String count;
    private String userid;
    private String username;
    private String truename;
    private String email;
    private String qq;
    private String phone;
    private String HaveMoney;
    private String Point;
    private String historypoint;
    private String publicgood;
    private String gradename;
    private String pic;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHaveMoney() {
        return HaveMoney;
    }

    public void setHaveMoney(String HaveMoney) {
        this.HaveMoney = HaveMoney;
    }

    public String getPoint() {
        return Point;
    }

    public void setPoint(String Point) {
        this.Point = Point;
    }

    public String getHistorypoint() {
        return historypoint;
    }

    public void setHistorypoint(String historypoint) {
        this.historypoint = historypoint;
    }

    public String getPublicgood() {
        return publicgood;
    }

    public void setPublicgood(String publicgood) {
        this.publicgood = publicgood;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
