package com.youwu.shouyinxitong.presenter.bean;

import java.io.Serializable;
import java.util.List;

public class ApplyOrderPrintBean implements Serializable {

    /**
     * id : 173
     * order_sn : 1602747586793378
     * admin_name : 111
     * store_id : 1
     * store_name :
     * total_money : 32.50
     * total_cost_money : 32.50
     * total_num : 15
     * status_name : 已确认
     * status : 3
     * mark :
     * add_time : 2020-10-15 15:39:46
     * update_time : 2020-10-15 15:43:42
     */

    private String order_sn;
    private String admin_name;
    private String store_name;
    private String total_money;
    private String total_cost_money;
    private String total_num;
    private String status_name;
    private String mark;
    private String add_time;
    private String update_time;
    private String allSum;
    private List carYWGoodBeans;

    public List getCarYWGoodBeans() {
        return carYWGoodBeans;
    }

    public void setCarYWGoodBeans(List carYWGoodBeans) {
        this.carYWGoodBeans = carYWGoodBeans;
    }

    public String getAllSum() {
        return allSum;
    }

    public void setAllSum(String allSum) {
        this.allSum = allSum;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getTotal_cost_money() {
        return total_cost_money;
    }

    public void setTotal_cost_money(String total_cost_money) {
        this.total_cost_money = total_cost_money;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}