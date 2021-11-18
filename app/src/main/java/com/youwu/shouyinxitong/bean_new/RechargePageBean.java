package com.youwu.shouyinxitong.bean_new;

import java.io.Serializable;
import java.util.List;

/**
 * 充值金额列表
 */
public class RechargePageBean implements Serializable {



    /**
     * id : 111
     * start_time : 2020-11-10
     * end_time : 2020-11-10
     * total_num : 10
     * total_money : 0.01
     */

    private int id;
    private String start_time;//活动开始时间
    private String end_time;//活动结束时间
    private int total_num;//
    private double total_money;//充值金额
    private int position;
    private int currentSelect;
    private boolean isCheck;

    public int getCurrentSelect() {
        return currentSelect;
    }

    public void setCurrentSelect(int currentSelect) {
        this.currentSelect = currentSelect;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }
}
