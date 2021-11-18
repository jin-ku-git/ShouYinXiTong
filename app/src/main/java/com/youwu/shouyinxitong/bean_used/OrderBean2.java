package com.youwu.shouyinxitong.bean_used;

import java.util.List;

public class OrderBean2 {

    /**
     * id : 38546
     * order_sn : 1601034446497000408499
     * member_id : 0
     * member_name :
     * member_tel :
     * pay_price : 15.00
     * total_price : 15.00
     * round_price : 0.00
     * activity_discount_price : 0.00
     * coupon_price : 0.00
     * order_type : 2
     * order_status : 2
     * pay_type : 4
     * refund_amount : 0.00
     * refund_time :
     * order_details : [{"id":118114,"goods_id":368,"goods_sku":"2009251722103","goods_name":"产品测试1","goods_number":1,"goods_price":"15.00","discount_price":"0.00"}]
     */

    private int select;
    private int id;
    private String order_sn;
    private int member_id;
    private String member_name;
    private String member_tel;
    private String pay_price;
    private String total_price;
    private String round_price;
    private String activity_discount_price;
    private String coupon_price;
    private String order_type;
    private int order_status;//3 已反结账
    private int pay_type;
    private String refund_amount;
    private String refund_time;
    private String create_time;
    private String staff_id;
    private String staff_name;
    private String remarks;
    private String order_pay_info;

    private List<OrderDetailsBean> order_details;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public String getOrder_pay_info() {
        return order_pay_info;
    }

    public void setOrder_pay_info(String order_pay_info) {
        this.order_pay_info = order_pay_info;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_tel() {
        return member_tel;
    }

    public void setMember_tel(String member_tel) {
        this.member_tel = member_tel;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getRound_price() {
        return round_price;
    }

    public void setRound_price(String round_price) {
        this.round_price = round_price;
    }

    public String getActivity_discount_price() {
        return activity_discount_price;
    }

    public void setActivity_discount_price(String activity_discount_price) {
        this.activity_discount_price = activity_discount_price;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
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

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
    }

    public List<OrderDetailsBean> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<OrderDetailsBean> order_details) {
        this.order_details = order_details;
    }

    public static class OrderDetailsBean {
        /**
         * id : 118114
         * goods_id : 368
         * goods_sku : 2009251722103
         * goods_name : 产品测试1
         * goods_number : 1
         * goods_price : 15.00
         * discount_price : 0.00
         */

        private int id;
        private int goods_id;
        private String goods_sku;
        private String goods_name;
        private int goods_number;
        private String goods_price;
        private String discount_price;
        private List detail;

        public List getDetail() {
            return detail;
        }

        public void setDetail(List detail) {
            this.detail = detail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getDiscount_price() {
            return discount_price;
        }

        public void setDiscount_price(String discount_price) {
            this.discount_price = discount_price;
        }
    }
}
