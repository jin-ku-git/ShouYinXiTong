package com.youwu.shouyinxitong.bean_new;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

/**
 * 用户表
 */
public class UserBean extends BaseObservable implements Serializable {
    private String user_id;//用户id
    private String user_name;//用户名称
    private String user_img;//用户头像
    private String user_phone;//用户手机号
    private String user_money;//用户余额
    private String user_create_time;//用户创建时间
    private String balance_pay_type;//1 用余额 2 不用余额
    private String remarks;//备注
    private int user_type;//1 选择 2取消选择

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getUser_create_time() {
        return user_create_time;
    }

    public void setUser_create_time(String user_create_time) {
        this.user_create_time = user_create_time;
    }

    public String getBalance_pay_type() {
        return balance_pay_type;
    }

    public void setBalance_pay_type(String balance_pay_type) {
        this.balance_pay_type = balance_pay_type;
    }
}
