package com.youwu.shouyinxitong.bean_used;

import java.io.Serializable;

public class CouponBean implements Serializable {
    /**
     * image :
     * subtravt : 1.00
     * coupon_id : 1
     * name : 测试优惠券1
     * startTime : 2019-07-13 14:54:33
     * endTime : 2019-08-18 10:48:40
     * full_price : 1.00
     * type : 1
     * token : bfd35ee4-7e70-4177-9f99-e820cb100dcc
     */

    private String image;
    private String deduction_price;
    private String discount_price;
    private String highest_price;
    private String goods_id;
    private String type;
    private String goods_sku;
    private String token;
    private String subtravt;
    private String species_uperposition;
    private String coupon_id;
    private String id;
    private String name;
    private String similar_superposition;
    private String details;
    private String startTime;
    private String endTime;
    private String full_price;
    private String payment_code_num;
    private boolean select = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayment_code_num() {
        return payment_code_num;
    }

    public void setPayment_code_num(String payment_code_num) {
        this.payment_code_num = payment_code_num;
    }

    public String getGoods_sku() {
        return goods_sku;
    }

    public void setGoods_sku(String goods_sku) {
        this.goods_sku = goods_sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeduction_price() {
        return deduction_price;
    }

    public void setDeduction_price(String deduction_price) {
        this.deduction_price = deduction_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getHighest_price() {
        return highest_price;
    }

    public void setHighest_price(String highest_price) {
        this.highest_price = highest_price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubtravt() {
        return subtravt;
    }

    public void setSubtravt(String subtravt) {
        this.subtravt = subtravt;
    }

    public String getSpecies_uperposition() {
        return species_uperposition;
    }

    public void setSpecies_uperposition(String species_uperposition) {
        this.species_uperposition = species_uperposition;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimilar_superposition() {
        return similar_superposition;
    }

    public void setSimilar_superposition(String similar_superposition) {
        this.similar_superposition = similar_superposition;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFull_price() {
        return full_price;
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
