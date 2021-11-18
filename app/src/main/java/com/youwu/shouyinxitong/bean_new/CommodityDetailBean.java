package com.youwu.shouyinxitong.bean_new;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

public class CommodityDetailBean extends BaseObservable implements Serializable {
    private String commodity_id;
    private String commodity_ranking;//商品排名
    private String commodity_name;//商品名称
    private String commodity_image;//商品图片
    private String commodity_sku;//商品规格
    private String commodity_price;//商品售价
    private String commodity_purchase_price;//商品进价
    private String commodity_purchase_price_subtotal;//商品进价小计
    private String commodity_classification;//商品分类
    private String commodity_type;//商品类型
    private int commodity_stock_num;//商品原库存
    private String commodity_num;//商品数量
    private int commodity_change_num;//商品库存变更
    private String commodity_code;//商品条码
    private String commodity_code_pinyin;//商品拼音码
    private String commodity_company;//商品单位  杯、桶、kg、g...
    private String commodity_custom;//商品自定义属性
    private String commodity_group;//商品群组
    private int commodity_state;//商品状态  上架、下架
    private int goods_type;//商品类型 1标品、2非标品
    private String start_time;//商品上架时间
    private String end_time;//商品下架时间
    private int judge_type;//1 添加商品 2编辑商品
    private int package_type;//1 套餐 2非套餐


    private int ratio_base;//配比
    private String commodity_unit;//单位

    private List<GroupListBean> group_list;

    private String category_name;
    private int is_sale; //1 成品 2辅料
    public CommodityDetailBean() {
    }

    public String getCommodity_unit() {
        return commodity_unit;
    }

    public void setCommodity_unit(String commodity_unit) {
        this.commodity_unit = commodity_unit;
    }

    public String getCommodity_num() {
        return commodity_num;
    }

    public void setCommodity_num(String commodity_num) {
        this.commodity_num = commodity_num;
    }

    public int getRatio_base() {
        return ratio_base;
    }

    public void setRatio_base(int ratio_base) {
        this.ratio_base = ratio_base;
    }

    public String getCommodity_purchase_price_subtotal() {
        return commodity_purchase_price_subtotal;
    }

    public void setCommodity_purchase_price_subtotal(String commodity_purchase_price_subtotal) {
        this.commodity_purchase_price_subtotal = commodity_purchase_price_subtotal;
    }

    public int getCommodity_change_num() {
        return commodity_change_num;
    }

    public void setCommodity_change_num(int commodity_change_num) {
        this.commodity_change_num = commodity_change_num;
    }

    public String getCommodity_ranking() {
        return commodity_ranking;
    }

    public void setCommodity_ranking(String commodity_ranking) {
        this.commodity_ranking = commodity_ranking;
    }

    public int getPackage_type() {
        return package_type;
    }

    public void setPackage_type(int package_type) {
        this.package_type = package_type;
    }

    public int getJudge_type() {
        return judge_type;
    }

    public void setJudge_type(int judge_type) {
        this.judge_type = judge_type;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getCommodity_image() {
        return commodity_image;
    }

    public void setCommodity_image(String commodity_image) {
        this.commodity_image = commodity_image;
    }

    public String getCommodity_sku() {
        return commodity_sku;
    }

    public void setCommodity_sku(String commodity_sku) {
        this.commodity_sku = commodity_sku;
    }

    public String getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(String commodity_price) {
        this.commodity_price = commodity_price;
    }

    public String getCommodity_purchase_price() {
        return commodity_purchase_price;
    }

    public void setCommodity_purchase_price(String commodity_purchase_price) {
        this.commodity_purchase_price = commodity_purchase_price;
    }

    public String getCommodity_classification() {
        return commodity_classification;
    }

    public void setCommodity_classification(String commodity_classification) {
        this.commodity_classification = commodity_classification;
    }

    public String getCommodity_type() {
        return commodity_type;
    }

    public void setCommodity_type(String commodity_type) {
        this.commodity_type = commodity_type;
    }

    public int getCommodity_stock_num() {
        return commodity_stock_num;
    }

    public void setCommodity_stock_num(int commodity_stock_num) {
        this.commodity_stock_num = commodity_stock_num;
    }

    public String getCommodity_code() {
        return commodity_code;
    }

    public void setCommodity_code(String commodity_code) {
        this.commodity_code = commodity_code;
    }

    public String getCommodity_code_pinyin() {
        return commodity_code_pinyin;
    }

    public void setCommodity_code_pinyin(String commodity_code_pinyin) {
        this.commodity_code_pinyin = commodity_code_pinyin;
    }

    public String getCommodity_company() {
        return commodity_company;
    }

    public void setCommodity_company(String commodity_company) {
        this.commodity_company = commodity_company;
    }

    public String getCommodity_custom() {
        return commodity_custom;
    }

    public void setCommodity_custom(String commodity_custom) {
        this.commodity_custom = commodity_custom;
    }

    public String getCommodity_group() {
        return commodity_group;
    }

    public void setCommodity_group(String commodity_group) {
        this.commodity_group = commodity_group;
    }

    public int getCommodity_state() {
        return commodity_state;
    }

    public void setCommodity_state(int commodity_state) {
        this.commodity_state = commodity_state;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
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

    public List<GroupListBean> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<GroupListBean> group_list) {
        this.group_list = group_list;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getIs_sale() {
        return is_sale;
    }

    public void setIs_sale(int is_sale) {
        this.is_sale = is_sale;
    }

    public static class GroupListBean {
        /**
         * id : 1
         */

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
