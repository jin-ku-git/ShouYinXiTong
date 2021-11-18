package com.youwu.shouyinxitong.bean_new;

import java.io.Serializable;

/**
 * 销售记录
 * 2021/11/01
 * 金库
 */
public class SalesCountBean implements Serializable {


    /**
     * all_count_total_price : 183.67
     * all_count_total_num : 25
     * all_count_avg_price : 7.35
     * take_count_total_price : 21.46
     * take_count_total_num : 2
     * take_count_avg_price : 10.73
     * store_count_total_price : 41.01
     * store_count_total_num : 3
     * store_count_avg_price : 13.67
     * all_refund_total_price : 62.47
     * all_refund_total_num : 5
     * take_refund_total_price : 0
     * take_refund_total_num : 0
     * store_refund_total_price : 0
     * store_refund_total_num : 0
     * coupon_total_price : 15.00
     * coupon_total_num : 5
     * all_discount_total_price : 88.42
     * all_discount_total_num : 16
     * take_discount_total_price : 0
     * take_discount_total_num : 0
     * store_discount_total_price : 88.42
     * store_discount_total_num : 16
     * recharge_total_price : 400.00
     * recharge_total_num : 2
     */

    private String all_sales_total_paice;//销售实收总额
    private String all_sales_total_order_num;//销售实收订单量
    private String all_sales_single_price;//销售实收客单价

    private String take_out_total_price;//外卖总额
    private String take_out_order_num;//外卖订单量
    private String take_out_single_price;//外卖客单价

    private String canteen_total_price;//食堂总额
    private String canteen_order_num;//食堂订单量
    private String canteen_single_price;//食堂客单价

    private String all_refund_total_price;//反结帐总额
    private String all_refund_total_order_num;//反结帐总订单量

    private String take_out_refund_total_price;//外卖反结帐总额
    private String take_out_refund_order_num;//外卖反结帐订单量

    private String canteen_refund_total_price;//食堂反结帐总额
    private String canteen_refund_order_num;//食堂反结帐订单量

    private String all_recharge_total_price;//充值总额
    private String all_recharge_total_order_num;//充值总订单量

    private String all_coupon_total_price;//优惠券总额
    private String all_coupon_total_order_num;//优惠券总订单量

    private String all_discount_total_price;//折扣总额
    private String all_discount_total_order_num;//折扣总订单量

    private String take_out_discount_total_price;//外卖折扣总额
    private String take_out_discount_order_num;//外卖折扣总订单量

    private String canteen_discount_total_price;//食堂折扣总额
    private String canteen_discount_order_num;//食堂折扣订单量

    private String print_time;//打印时间

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
    }

    public String getAll_sales_total_paice() {
        return all_sales_total_paice;
    }

    public void setAll_sales_total_paice(String all_sales_total_paice) {
        this.all_sales_total_paice = all_sales_total_paice;
    }

    public String getAll_sales_total_order_num() {
        return all_sales_total_order_num;
    }

    public void setAll_sales_total_order_num(String all_sales_total_order_num) {
        this.all_sales_total_order_num = all_sales_total_order_num;
    }

    public String getAll_sales_single_price() {
        return all_sales_single_price;
    }

    public void setAll_sales_single_price(String all_sales_single_price) {
        this.all_sales_single_price = all_sales_single_price;
    }

    public String getTake_out_total_price() {
        return take_out_total_price;
    }

    public void setTake_out_total_price(String take_out_total_price) {
        this.take_out_total_price = take_out_total_price;
    }

    public String getTake_out_order_num() {
        return take_out_order_num;
    }

    public void setTake_out_order_num(String take_out_order_num) {
        this.take_out_order_num = take_out_order_num;
    }

    public String getTake_out_single_price() {
        return take_out_single_price;
    }

    public void setTake_out_single_price(String take_out_single_price) {
        this.take_out_single_price = take_out_single_price;
    }

    public String getCanteen_total_price() {
        return canteen_total_price;
    }

    public void setCanteen_total_price(String canteen_total_price) {
        this.canteen_total_price = canteen_total_price;
    }

    public String getCanteen_order_num() {
        return canteen_order_num;
    }

    public void setCanteen_order_num(String canteen_order_num) {
        this.canteen_order_num = canteen_order_num;
    }

    public String getCanteen_single_price() {
        return canteen_single_price;
    }

    public void setCanteen_single_price(String canteen_single_price) {
        this.canteen_single_price = canteen_single_price;
    }

    public String getAll_refund_total_price() {
        return all_refund_total_price;
    }

    public void setAll_refund_total_price(String all_refund_total_price) {
        this.all_refund_total_price = all_refund_total_price;
    }

    public String getAll_refund_total_order_num() {
        return all_refund_total_order_num;
    }

    public void setAll_refund_total_order_num(String all_refund_total_order_num) {
        this.all_refund_total_order_num = all_refund_total_order_num;
    }

    public String getTake_out_refund_total_price() {
        return take_out_refund_total_price;
    }

    public void setTake_out_refund_total_price(String take_out_refund_total_price) {
        this.take_out_refund_total_price = take_out_refund_total_price;
    }

    public String getTake_out_refund_order_num() {
        return take_out_refund_order_num;
    }

    public void setTake_out_refund_order_num(String take_out_refund_order_num) {
        this.take_out_refund_order_num = take_out_refund_order_num;
    }

    public String getCanteen_refund_total_price() {
        return canteen_refund_total_price;
    }

    public void setCanteen_refund_total_price(String canteen_refund_total_price) {
        this.canteen_refund_total_price = canteen_refund_total_price;
    }

    public String getCanteen_refund_order_num() {
        return canteen_refund_order_num;
    }

    public void setCanteen_refund_order_num(String canteen_refund_order_num) {
        this.canteen_refund_order_num = canteen_refund_order_num;
    }

    public String getAll_recharge_total_price() {
        return all_recharge_total_price;
    }

    public void setAll_recharge_total_price(String all_recharge_total_price) {
        this.all_recharge_total_price = all_recharge_total_price;
    }

    public String getAll_recharge_total_order_num() {
        return all_recharge_total_order_num;
    }

    public void setAll_recharge_total_order_num(String all_recharge_total_order_num) {
        this.all_recharge_total_order_num = all_recharge_total_order_num;
    }

    public String getAll_coupon_total_price() {
        return all_coupon_total_price;
    }

    public void setAll_coupon_total_price(String all_coupon_total_price) {
        this.all_coupon_total_price = all_coupon_total_price;
    }

    public String getAll_coupon_total_order_num() {
        return all_coupon_total_order_num;
    }

    public void setAll_coupon_total_order_num(String all_coupon_total_order_num) {
        this.all_coupon_total_order_num = all_coupon_total_order_num;
    }

    public String getAll_discount_total_price() {
        return all_discount_total_price;
    }

    public void setAll_discount_total_price(String all_discount_total_price) {
        this.all_discount_total_price = all_discount_total_price;
    }

    public String getAll_discount_total_order_num() {
        return all_discount_total_order_num;
    }

    public void setAll_discount_total_order_num(String all_discount_total_order_num) {
        this.all_discount_total_order_num = all_discount_total_order_num;
    }

    public String getTake_out_discount_total_price() {
        return take_out_discount_total_price;
    }

    public void setTake_out_discount_total_price(String take_out_discount_total_price) {
        this.take_out_discount_total_price = take_out_discount_total_price;
    }

    public String getTake_out_discount_order_num() {
        return take_out_discount_order_num;
    }

    public void setTake_out_discount_order_num(String take_out_discount_order_num) {
        this.take_out_discount_order_num = take_out_discount_order_num;
    }

    public String getCanteen_discount_total_price() {
        return canteen_discount_total_price;
    }

    public void setCanteen_discount_total_price(String canteen_discount_total_price) {
        this.canteen_discount_total_price = canteen_discount_total_price;
    }

    public String getCanteen_discount_order_num() {
        return canteen_discount_order_num;
    }

    public void setCanteen_discount_order_num(String canteen_discount_order_num) {
        this.canteen_discount_order_num = canteen_discount_order_num;
    }
}
