package com.youwu.shouyinxitong.bean_new;

import java.io.Serializable;
import java.util.List;

/**
 * 商品类
 */
public class GoodList implements Serializable {

    /**
     * _id : 6180ce4b693f0000e4007e32
     * goods_no : 11576276407
     * goods_name : Iphone 13 Pros
     * unit : 台
     * pic : https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg
     * pic_data : ["https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg"]
     * parent_category_id : 1
     * category_id : 2
     * type : 1
     * use_type : 1
     * is_standard : 1
     * sell_price : 900000
     * market_price : 900000
     * content : Iphone 新品上市
     * status : 1
     * attr_data : [{"attr_id":2,"attr_name":"内存","attr_item_data":["64G","128G","256G"]},{"attr_id":1,"attr_name":"颜色","attr_item_data":["磨砂黑","宝藏蓝","炫彩白"]}]
     * sku_data : [{"original_price":900000,"goods_no":"11576276407","goods_sku_no":"25503120149","sell_price":900000,"is_enabled":1,"stock":200,"attribute_data":{"attr_combo_id":"2:1","attr_combo_value":"256G:炫彩白"},"pic_urls":["https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg","https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg"],"store_id":0},{"original_price":900000,"sell_price":900000,"goods_no":"11576276407","goods_sku_no":"25742200616","is_enabled":1,"stock":200,"attribute_data":{"attr_combo_id":"2:1","attr_combo_value":"64G:磨砂黑"},"pic_urls":["https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg","https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg"],"store_id":0}]
     * store_id : 0
     * admin_id : 0
     * admin_name :
     * relation_goods_data_info : []
     * updated_at : 1635831371
     * created_at : 1635831371
     */

    private String _id;//商品id
    private String goods_no;//商品编号
    private String goods_name;//商品名称
    private String unit;//商品单位
    private String pic;//商品图片
    private int parent_category_id;//所属分类id
    private int category_id;
    private int type;
    private int use_type;
    private int is_standard;
    private int sell_price;
    private int market_price;
    private String content;//商品内容
    private int status;
    private int store_id;
    private int admin_id;
    private String admin_name;
    private int updated_at;
    private int created_at;
    private List<String> pic_data;//商品图片列表
    private List<AttrDataBean> attr_data;
    private List<SkuDataBean> sku_data;
    private List<?> relation_goods_data_info;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoods_no() {
        return goods_no;
    }

    public void setGoods_no(String goods_no) {
        this.goods_no = goods_no;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(int parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUse_type() {
        return use_type;
    }

    public void setUse_type(int use_type) {
        this.use_type = use_type;
    }

    public int getIs_standard() {
        return is_standard;
    }

    public void setIs_standard(int is_standard) {
        this.is_standard = is_standard;
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public int getMarket_price() {
        return market_price;
    }

    public void setMarket_price(int market_price) {
        this.market_price = market_price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public List<String> getPic_data() {
        return pic_data;
    }

    public void setPic_data(List<String> pic_data) {
        this.pic_data = pic_data;
    }

    public List<AttrDataBean> getAttr_data() {
        return attr_data;
    }

    public void setAttr_data(List<AttrDataBean> attr_data) {
        this.attr_data = attr_data;
    }

    public List<SkuDataBean> getSku_data() {
        return sku_data;
    }

    public void setSku_data(List<SkuDataBean> sku_data) {
        this.sku_data = sku_data;
    }

    public List<?> getRelation_goods_data_info() {
        return relation_goods_data_info;
    }

    public void setRelation_goods_data_info(List<?> relation_goods_data_info) {
        this.relation_goods_data_info = relation_goods_data_info;
    }

    public static class AttrDataBean {
        /**
         * attr_id : 2
         * attr_name : 内存
         * attr_item_data : ["64G","128G","256G"]
         */

        private int attr_id;
        private String attr_name;
        private List<String> attr_item_data;

        public int getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(int attr_id) {
            this.attr_id = attr_id;
        }

        public String getAttr_name() {
            return attr_name;
        }

        public void setAttr_name(String attr_name) {
            this.attr_name = attr_name;
        }

        public List<String> getAttr_item_data() {
            return attr_item_data;
        }

        public void setAttr_item_data(List<String> attr_item_data) {
            this.attr_item_data = attr_item_data;
        }
    }

    public static class SkuDataBean {
        /**
         * original_price : 900000
         * goods_no : 11576276407
         * goods_sku_no : 25503120149
         * sell_price : 900000
         * is_enabled : 1
         * stock : 200
         * attribute_data : {"attr_combo_id":"2:1","attr_combo_value":"256G:炫彩白"}
         * pic_urls : ["https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg","https://images2.youwuu.com/2021/09/29/24c529d27756f2c2bf1593c7e718211f.jpg"]
         * store_id : 0
         */

        private int original_price;
        private String goods_no;
        private String goods_sku_no;
        private int sell_price;
        private int is_enabled;
        private int stock;
        private AttributeDataBean attribute_data;
        private int store_id;
        private List<String> pic_urls;

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public String getGoods_no() {
            return goods_no;
        }

        public void setGoods_no(String goods_no) {
            this.goods_no = goods_no;
        }

        public String getGoods_sku_no() {
            return goods_sku_no;
        }

        public void setGoods_sku_no(String goods_sku_no) {
            this.goods_sku_no = goods_sku_no;
        }

        public int getSell_price() {
            return sell_price;
        }

        public void setSell_price(int sell_price) {
            this.sell_price = sell_price;
        }

        public int getIs_enabled() {
            return is_enabled;
        }

        public void setIs_enabled(int is_enabled) {
            this.is_enabled = is_enabled;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public AttributeDataBean getAttribute_data() {
            return attribute_data;
        }

        public void setAttribute_data(AttributeDataBean attribute_data) {
            this.attribute_data = attribute_data;
        }

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }

        public List<String> getPic_urls() {
            return pic_urls;
        }

        public void setPic_urls(List<String> pic_urls) {
            this.pic_urls = pic_urls;
        }

        public static class AttributeDataBean {
            /**
             * attr_combo_id : 2:1
             * attr_combo_value : 256G:炫彩白
             */

            private String attr_combo_id;
            private String attr_combo_value;

            public String getAttr_combo_id() {
                return attr_combo_id;
            }

            public void setAttr_combo_id(String attr_combo_id) {
                this.attr_combo_id = attr_combo_id;
            }

            public String getAttr_combo_value() {
                return attr_combo_value;
            }

            public void setAttr_combo_value(String attr_combo_value) {
                this.attr_combo_value = attr_combo_value;
            }
        }
    }
}
