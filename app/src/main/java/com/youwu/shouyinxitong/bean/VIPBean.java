package com.youwu.shouyinxitong.bean;


import com.youwu.shouyinxitong.bean_used.CouponBean;

import java.io.Serializable;
import java.util.List;

public class VIPBean implements Serializable {


    /**
     * award : 0
     * balance : 0.00
     * create_time : 2019-07-13 14:54:33
     * phone : 15376072877
     * user_id : 1
     * couponList : [{"image":"","subtravt":"1.00","coupon_id":"1","name":"测试优惠券1","startTime":"2019-07-13 14:54:33","endTime":"2020-07-13 14:54:33","full_price":"1.00","type":"1"}]
     * name : 2
     * headPortrait :
     * token : e54a6a1e-c171-43f0-9f92-b01ea97cc98d
     */

    private String balance;
    private String create_time;//开卡时间
    private String phone;//手机号
    private String user_id;//id
    private String integral;
    private List<CouponBean> couponList;
    private String name;//名字
    private String headPortrait;
    private String token;
    private String money;//余额
    private String remarks;//备注

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public List<CouponBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponBean> couponList) {
        this.couponList = couponList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public VIPBean() {
    }
}
