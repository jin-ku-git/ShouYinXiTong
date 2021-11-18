package com.youwu.shouyinxitong.bean_new;

import java.io.Serializable;
import java.util.List;

/**
 * 充值记录表
 */
public class RechargeRecordBean implements Serializable {

    private String rchg_num;//充值次数
    private String rchg_total_price;//充值总数
    private List<ListBean> list;//充值数据


    public String getRchg_num() {
        return rchg_num;
    }

    public void setRchg_num(String rchg_num) {
        this.rchg_num = rchg_num;
    }

    public String getRchg_total_price() {
        return rchg_total_price;
    }

    public void setRchg_total_price(String rchg_total_price) {
        this.rchg_total_price = rchg_total_price;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String rchg_id;//id
        private String user_name;//充值人名称
        private String rchg_price;//充值金额
        private String rchg_coupons;//充值优惠券
        private String coupons_num;//获得优惠券的次数
        private String rchg_mode;//充值方式
        private String rchg_cashier;//收银员
        private String rchg_time;//充值时间
        public int currentSelect;

        private int position;

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

        public String getRchg_id() {
            return rchg_id;
        }

        public void setRchg_id(String rchg_id) {
            this.rchg_id = rchg_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getRchg_price() {
            return rchg_price;
        }

        public void setRchg_price(String rchg_price) {
            this.rchg_price = rchg_price;
        }

        public String getRchg_coupons() {
            return rchg_coupons;
        }

        public void setRchg_coupons(String rchg_coupons) {
            this.rchg_coupons = rchg_coupons;
        }

        public String getCoupons_num() {
            return coupons_num;
        }

        public void setCoupons_num(String coupons_num) {
            this.coupons_num = coupons_num;
        }

        public String getRchg_mode() {
            return rchg_mode;
        }

        public void setRchg_mode(String rchg_mode) {
            this.rchg_mode = rchg_mode;
        }

        public String getRchg_cashier() {
            return rchg_cashier;
        }

        public void setRchg_cashier(String rchg_cashier) {
            this.rchg_cashier = rchg_cashier;
        }

        public String getRchg_time() {
            return rchg_time;
        }

        public void setRchg_time(String rchg_time) {
            this.rchg_time = rchg_time;
        }
    }



}
