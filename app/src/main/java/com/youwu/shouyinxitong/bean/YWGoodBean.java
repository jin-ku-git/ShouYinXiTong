package com.youwu.shouyinxitong.bean;

import android.text.TextUtils;

import com.youwu.shouyinxitong.bean_used.GroupItemsBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class YWGoodBean extends TypeSuper implements Serializable {


    //    自己创建的数据

    private String totalmoney = "0";
    private String realmoney = "0";
    private String countStr;
    private String discount;
    private String discount_price;
    private String remarks;
    private String num;
    private String purchasePrice;//进货时用到的价格

    private String total_money;//应付价格
    private boolean canAddShop = true;

    private int isCar;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsCar() {
        return isCar;
    }

    public void setIsCar(int isCar) {
        this.isCar = isCar;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public SpecsBean getBean() {
        return bean;
    }

    public void setBean(SpecsBean bean) {
        this.bean = bean;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_sku() {
        return goods_sku;
    }

    public void setGoods_sku(String goods_sku) {
        this.goods_sku = goods_sku;
    }

    public String getRatio_num() {
        return ratio_num;
    }

    public void setRatio_num(String ratio_num) {
        this.ratio_num = ratio_num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isCanAddShop() {
        return canAddShop;
    }

    public void setCanAddShop(boolean canAddShop) {
        this.canAddShop = canAddShop;
    }

    //后台返回的原始数据
    SpecsBean bean = new SpecsBean();
    public List<SpecsBean> specs = new ArrayList<>();

    public String sales_status;
    public String image;
    public String original_price;
    public String category_name;
    public String category_id;               //分类id
    public String price;
    public String id;
    public String goods_id;
    public String sku;
    public String goods_sku;
    public String product_name;
    public String letters;
    public String attribute;
    public String unit;
    public int inventory;
    public int goods_type;//商品类型 1标品、2非标品
    public int buynum;
    public double weightnum;//重量
    public int weight_type;//=16为克 =32为千克
    private int select = 0;
    public Boolean sale = true;
    private String bar_code;
    private String cost_price;//进货价
    private int ratio_base;
    private int ratio_goods_id;
    private int store_goods_id;
    private int goods_number;//套餐中商品的数量
    private String ratio_unit;
    private String ratio_num;
    private String group_value;
    private String order_cost_price;
    private List<GroupItemsBean> group_list;

    public int getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(int weight_type) {
        this.weight_type = weight_type;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public double getWeightnum() {
        return weightnum;
    }

    public void setWeightnum(double weightnum) {
        this.weightnum = weightnum;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getOrder_cost_price() {
        return order_cost_price;
    }

    public void setOrder_cost_price(String order_cost_price) {
        this.order_cost_price = order_cost_price;
    }

    public String getGroup_value() {
        return group_value;
    }

    public void setGroup_value(String group_value) {
        this.group_value = group_value;
    }

    public List<GroupItemsBean> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<GroupItemsBean> group_list) {
        this.group_list = group_list;
    }

    public int getStore_goods_id() {
        return store_goods_id;
    }

    public void setStore_goods_id(int store_goods_id) {
        this.store_goods_id = store_goods_id;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public int getRatio_base() {
        return ratio_base;
    }

    public void setRatio_base(int ratio_base) {
        this.ratio_base = ratio_base;
    }

    public int getRatio_goods_id() {
        return ratio_goods_id;
    }

    public void setRatio_goods_id(int ratio_goods_id) {
        this.ratio_goods_id = ratio_goods_id;
    }

    public String getRatio_unit() {
        return ratio_unit;
    }

    public void setRatio_unit(String ratio_unit) {
        this.ratio_unit = ratio_unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public List<YWGoodBean2.XYPoint> xyPoints = new ArrayList<>();

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public Boolean getSale() {
        return sale;
    }

    public List<YWGoodBean2.XYPoint> getXyPoints() {
        return xyPoints;
    }

    public void setXyPoints(List<YWGoodBean2.XYPoint> xyPoints) {
        this.xyPoints = xyPoints;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSales_status() {
        return sales_status;
    }

    public void setSales_status(String sales_status) {
        this.sales_status = sales_status;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        if (TextUtils.isEmpty(sku)) {
            return goods_sku;
        }
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPurchasePrice() {
        if (purchasePrice == null) {
            return cost_price;
        }
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setOptNum(int optNum) {
        this.optNum = optNum;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getRealmoney() {


        return (Double.parseDouble(realmoney) == 0 ? totalmoney : realmoney);
    }

    public void setRealmoney(String realmoney) {
        this.realmoney = realmoney;
    }

    public String getCountStr() {
        return countStr;
    }

    public void setCountStr(String countStr) {
        this.countStr = countStr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public List<SpecsBean> getSpecs() {
        specs.add(bean);
        return specs;
    }

    public void setSpecs(List<SpecsBean> specs) {
        this.specs = specs;
    }


    public static class SpecsBean implements Serializable {

        //是否可以加入购物车，弹窗显示规格列表的时候，根据购物车里面的商品进行设置   根据当时购物车及库存情况每次都重新设置，所以不用在意之前的是啥
        private boolean canAddShop = true;

        public boolean isCanAddShop() {
            return canAddShop;
        }

        public void setCanAddShop(boolean canAddShop) {
            this.canAddShop = canAddShop;
        }

        @Override
        public String toString() {
            return "SpecsBean{" +
                    "canAddShop=" + canAddShop +
                    ", price='" + price + '\'' +
                    ", spec_name='" + spec_name + '\'' +
                    ", repertory='" + repertory + '\'' +
                    ", sku='" + sku + '\'' +
                    ", select=" + select +
                    ", buynum=" + buynum +
                    '}';
        }

        /**
         * price : 0.00
         * spec_name : dd
         * repertory : 0.000
         * sku : 33
         */

        private String price = "0";
        private String spec_name;
        private String repertory = "0";
        private String sku;
        private int select = 0;

        private int buynum = 0;

        public int getBuynum() {
            return buynum;
        }

        public void setBuynum(int buynum) {
            this.buynum = buynum;
        }

        public int getSelect() {
            return select;
        }

        public void setSelect(int select) {
            this.select = select;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public String getRepertory() {
            return repertory;
        }

        public void setRepertory(String repertory) {
            this.repertory = repertory;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public SpecsBean() {
        }


    }

    public YWGoodBean() {
    }

    public YWGoodBean(SpecsBean specsBean, String totalmoney, String realmoney, String countStr, String image, String category_id, String id, String product_name, String letters, List<SpecsBean> specs, int optNum) {

        this.totalmoney = totalmoney;
        this.realmoney = realmoney;
        this.countStr = countStr;
        this.image = image;
        this.category_id = category_id;
        this.id = id;
        this.product_name = product_name;
        this.letters = letters;
        this.specs = specs;
        this.optNum = optNum;
    }


    //标品数量  非标品的扫描个数
    private int optNum;

    public int getOptNum() {
        return optNum;
    }


}
