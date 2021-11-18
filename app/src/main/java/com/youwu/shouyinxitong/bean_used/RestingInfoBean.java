package com.youwu.shouyinxitong.bean_used;

import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean_used.OrderNumberBean;

import java.io.Serializable;
import java.util.List;

public class RestingInfoBean implements  Serializable {
    private int select = 0;

    @Override
    public String toString() {
        return "RestingInfoBean{" +
                "select=" + select +
                ", orderNumberBean=" + orderNumberBean +
                ", bean=" + bean +
                ", shopCarYWGoodBeans=" + shopCarYWGoodBeans +
                '}';
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    private OrderNumberBean orderNumberBean;
    private VIPBean bean;
    private List shopCarYWGoodBeans;


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

    public List getShopCarYWGoodBeans() {
        return shopCarYWGoodBeans;
    }

    public void setShopCarYWGoodBeans(List shopCarYWGoodBeans) {
        this.shopCarYWGoodBeans = shopCarYWGoodBeans;
    }


}
