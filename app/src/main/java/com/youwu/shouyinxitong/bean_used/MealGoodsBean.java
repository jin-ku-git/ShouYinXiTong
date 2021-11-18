package com.youwu.shouyinxitong.bean_used;

import java.io.Serializable;

public class MealGoodsBean implements Serializable {

    /**
     * id : 306
     * store_goods_id : 4
     * goods_id : 3
     * goods_sku : 2011144403080
     * goods_name : 韭菜盒子
     * goods_number : 15
     * sell_price : 1.50
     */

    private int id;
    private int store_goods_id;
    private int goods_id;
    private String goods_sku;
    private String goods_name;
    private int goods_number;
    private String sell_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_goods_id() {
        return store_goods_id;
    }

    public void setStore_goods_id(int store_goods_id) {
        this.store_goods_id = store_goods_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_sku() {
        return goods_sku;
    }

    public void setGoods_sku(String goods_sku) {
        this.goods_sku = goods_sku;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }
}
