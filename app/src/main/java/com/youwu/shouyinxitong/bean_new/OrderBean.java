package com.youwu.shouyinxitong.bean_new;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {

    private String id;
    private String user_name;//用户昵称
    private String user_image;//用户头像
    private String user_phone;//用户手机号
    private String order_id;//订单编号
    private String order_name;//订单名称
    private String order_start_time;//订单下单时间
    private String order_purchase_time;//订单购买时间
    private String order_cancel_time;//订单取消时间
    private String order_return_time;//订单退货时间
    private String order_receiving_time;//订单确认收货时间
    private String order_remarks;//订单备注
    private String order_cashier;//订单收银员
    private String order_numbers;//订单商品数量
    private String order_receivable_price;//订单应收价格
    private String order_price;//订单实收价格
    private String order_type;//订单类型
    private String order_commodity_price;//商品价格
    private String order_discount_price;//订单优惠总金额
    private String order_price_integral;//订单优惠----积分
    private String order_price_coupons;//订单优惠----券
    private String order_discount_num;//订单优惠----几折
    private String order_freight_price;//运费
    private String order_packing_price;//包装费
    private String order_status_name;//订单状态

    private int order_status;//3 已反结账
    private int pay_type;//支付类型 //1 现金 2 余额 3 微信 4 支付宝
    private int select;

    private List<OrderDetailsBean> order_details;//商品列表

    private int position;

    public String getOrder_commodity_price() {
        return order_commodity_price;
    }

    public void setOrder_commodity_price(String order_commodity_price) {
        this.order_commodity_price = order_commodity_price;
    }

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_start_time() {
        return order_start_time;
    }

    public void setOrder_start_time(String order_start_time) {
        this.order_start_time = order_start_time;
    }

    public String getOrder_purchase_time() {
        return order_purchase_time;
    }

    public void setOrder_purchase_time(String order_purchase_time) {
        this.order_purchase_time = order_purchase_time;
    }

    public String getOrder_cancel_time() {
        return order_cancel_time;
    }

    public void setOrder_cancel_time(String order_cancel_time) {
        this.order_cancel_time = order_cancel_time;
    }

    public String getOrder_return_time() {
        return order_return_time;
    }

    public void setOrder_return_time(String order_return_time) {
        this.order_return_time = order_return_time;
    }

    public String getOrder_receiving_time() {
        return order_receiving_time;
    }

    public void setOrder_receiving_time(String order_receiving_time) {
        this.order_receiving_time = order_receiving_time;
    }

    public String getOrder_remarks() {
        return order_remarks;
    }

    public void setOrder_remarks(String order_remarks) {
        this.order_remarks = order_remarks;
    }

    public String getOrder_cashier() {
        return order_cashier;
    }

    public void setOrder_cashier(String order_cashier) {
        this.order_cashier = order_cashier;
    }

    public String getOrder_numbers() {
        return order_numbers;
    }

    public void setOrder_numbers(String order_numbers) {
        this.order_numbers = order_numbers;
    }

    public String getOrder_receivable_price() {
        return order_receivable_price;
    }

    public void setOrder_receivable_price(String order_receivable_price) {
        this.order_receivable_price = order_receivable_price;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_discount_price() {
        return order_discount_price;
    }

    public void setOrder_discount_price(String order_discount_price) {
        this.order_discount_price = order_discount_price;
    }

    public String getOrder_price_integral() {
        return order_price_integral;
    }

    public void setOrder_price_integral(String order_price_integral) {
        this.order_price_integral = order_price_integral;
    }

    public String getOrder_price_coupons() {
        return order_price_coupons;
    }

    public void setOrder_price_coupons(String order_price_coupons) {
        this.order_price_coupons = order_price_coupons;
    }

    public String getOrder_discount_num() {
        return order_discount_num;
    }

    public void setOrder_discount_num(String order_discount_num) {
        this.order_discount_num = order_discount_num;
    }

    public String getOrder_freight_price() {
        return order_freight_price;
    }

    public void setOrder_freight_price(String order_freight_price) {
        this.order_freight_price = order_freight_price;
    }

    public String getOrder_packing_price() {
        return order_packing_price;
    }

    public void setOrder_packing_price(String order_packing_price) {
        this.order_packing_price = order_packing_price;
    }

    public List<OrderDetailsBean> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<OrderDetailsBean> order_details) {
        this.order_details = order_details;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class OrderDetailsBean implements Serializable{


        private String id;//
        private String commodity_id;//商品id
        private String commodity_sku;//商品参数
        private String commodity_name;//商品名称
        private String commodity_number;//商品数量
        private String commodity_primary_price;//商品原价格
        private String commodity_present_price;//商品现价格
        private String commodity_discount_price;//商品优惠金额
        private String commodity_total_price;//商品总价


        public String getCommodity_total_price() {
            return commodity_total_price;
        }

        public void setCommodity_total_price(String commodity_total_price) {
            this.commodity_total_price = commodity_total_price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommodity_id() {
            return commodity_id;
        }

        public void setCommodity_id(String commodity_id) {
            this.commodity_id = commodity_id;
        }

        public String getCommodity_sku() {
            return commodity_sku;
        }

        public void setCommodity_sku(String commodity_sku) {
            this.commodity_sku = commodity_sku;
        }

        public String getCommodity_name() {
            return commodity_name;
        }

        public void setCommodity_name(String commodity_name) {
            this.commodity_name = commodity_name;
        }

        public String getCommodity_number() {
            return commodity_number;
        }

        public void setCommodity_number(String commodity_number) {
            this.commodity_number = commodity_number;
        }

        public String getCommodity_primary_price() {
            return commodity_primary_price;
        }

        public void setCommodity_primary_price(String commodity_primary_price) {
            this.commodity_primary_price = commodity_primary_price;
        }

        public String getCommodity_present_price() {
            return commodity_present_price;
        }

        public void setCommodity_present_price(String commodity_present_price) {
            this.commodity_present_price = commodity_present_price;
        }

        public String getCommodity_discount_price() {
            return commodity_discount_price;
        }

        public void setCommodity_discount_price(String commodity_discount_price) {
            this.commodity_discount_price = commodity_discount_price;
        }
    }
}
