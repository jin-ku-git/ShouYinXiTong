package com.youwu.shouyinxitong.bean_used;

import com.youwu.shouyinxitong.bean.TypeSuper;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;

import java.io.Serializable;
import java.util.List;

/**
 * 套餐类
 */
public class MealsItemBean extends TypeSuper implements Serializable {


    /**
     * id : 3
     * create_time : 2021-04-19 22:35:43
     * update_time : 2021-04-20 08:53:32
     * store_id : 1
     * store_name : 1店 谢小龙在用 不要乱动 ok?不ok
     * meal_goods_id : 4
     * meal_goods_name : 早餐套餐4
     * meal_goods_sku : 1000021234522
     * meal_initial : aok
     * meal_goods_price : 15.00
     * goods_type_id : 1
     * goods_type_name : 早餐
     * status : 2
     * status_name : 下架
     * sales_status : 1
     * sales_status_name : 正常销售
     * del : 0
     * del_name : 正常
     * sort : 1
     * img :
     * time : 00:00:00-10:00:00
     * meal_store_goods_detail : []
     */

    private int id;
    private String create_time;
    private String update_time;
    private int store_id;
    private String store_name;
    private int meal_goods_id;
    private String meal_goods_name;
    private String meal_goods_sku;
    private String meal_initial;
    private String meal_goods_price;
    private int goods_type_id;
    private String goods_type_name;
    private String status;
    private String status_name;
    private String sales_status;
    private String sales_status_name;
    private String remarks;
    private int del;
    private String del_name;
    private int sort;
    public int buynum;
    private String img;
    private String time;
    private String goodsJson;
    private String goods_type;
    private double weightnum;
    private List<MealGoodsBean> meal_store_goods_detail;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int isCar;

    public int getIsCar() {
        return isCar;
    }

    public void setIsCar(int isCar) {
        this.isCar = isCar;
    }

    public double getWeightnum() {
        return weightnum;
    }

    public void setWeightnum(double weightnum) {
        this.weightnum = weightnum;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getGoodsJson() {
        return goodsJson;
    }

    public void setGoodsJson(String goodsJson) {
        this.goodsJson = goodsJson;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getMeal_goods_id() {
        return meal_goods_id;
    }

    public void setMeal_goods_id(int meal_goods_id) {
        this.meal_goods_id = meal_goods_id;
    }

    public String getMeal_goods_name() {
        return meal_goods_name;
    }

    public void setMeal_goods_name(String meal_goods_name) {
        this.meal_goods_name = meal_goods_name;
    }

    public String getMeal_goods_sku() {
        return meal_goods_sku;
    }

    public void setMeal_goods_sku(String meal_goods_sku) {
        this.meal_goods_sku = meal_goods_sku;
    }

    public String getMeal_initial() {
        return meal_initial;
    }

    public void setMeal_initial(String meal_initial) {
        this.meal_initial = meal_initial;
    }

    public String getMeal_goods_price() {
        return meal_goods_price;
    }

    public void setMeal_goods_price(String meal_goods_price) {
        this.meal_goods_price = meal_goods_price;
    }

    public int getGoods_type_id() {
        return goods_type_id;
    }

    public void setGoods_type_id(int goods_type_id) {
        this.goods_type_id = goods_type_id;
    }

    public String getGoods_type_name() {
        return goods_type_name;
    }

    public void setGoods_type_name(String goods_type_name) {
        this.goods_type_name = goods_type_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getSales_status() {
        return sales_status;
    }

    public void setSales_status(String sales_status) {
        this.sales_status = sales_status;
    }

    public String getSales_status_name() {
        return sales_status_name;
    }

    public void setSales_status_name(String sales_status_name) {
        this.sales_status_name = sales_status_name;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public String getDel_name() {
        return del_name;
    }

    public void setDel_name(String del_name) {
        this.del_name = del_name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<MealGoodsBean> getMeal_store_goods_detail() {
        return meal_store_goods_detail;
    }

    public void setMeal_store_goods_detail(List<MealGoodsBean> meal_store_goods_detail) {
        this.meal_store_goods_detail = meal_store_goods_detail;
    }
}
