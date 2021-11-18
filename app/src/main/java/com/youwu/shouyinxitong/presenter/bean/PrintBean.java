package com.youwu.shouyinxitong.presenter.bean;

import com.youwu.shouyinxitong.bean_used.CouponBean;
import com.youwu.shouyinxitong.bean_used.OrderNumberBean;
import com.youwu.shouyinxitong.bean.VIPBean;

import java.io.Serializable;
import java.util.List;

public class PrintBean implements Serializable {


    private OrderNumberBean orderNumberBean;
    private VIPBean bean;
    private CouponBean couponBean;
    private List shopCarYWGoodBeans;
    private String order_sn;
    private String paymoney;
    private String paytype;
    private String cashier;
    private String foodCode;
    private String creatTime;
    private String attribute;
    private String couponPrice;
    public Boolean isOnlion = false;
    private String couponName;
    private String orderType;
    private String deliveryType;   //订单类型 1店内就餐，2打包带走，3外卖
    private String orderAddress;   //外卖地址
    private String freight;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public Boolean getOnlion() {
        return isOnlion;
    }

    public void setOnlion(Boolean onlion) {
        isOnlion = onlion;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    private String money;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public OrderNumberBean getOrderNumberBean() {
        return orderNumberBean;
    }

    public void setOrderNumberBean(OrderNumberBean orderNumberBean) {
        this.orderNumberBean = orderNumberBean;
    }

    public VIPBean getBean() {
        return bean;
    }

    public void setBean(VIPBean bean) {
        this.bean = bean;
    }

    public CouponBean getCouponBean() {
        return couponBean;
    }

    public void setCouponBean(CouponBean couponBean) {
        this.couponBean = couponBean;
    }

    public List getShopCarYWGoodBeans() {
        return shopCarYWGoodBeans;
    }

    public void setShopCarYWGoodBeans(List shopCarYWGoodBeans) {
        this.shopCarYWGoodBeans = shopCarYWGoodBeans;
    }
}
