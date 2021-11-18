package com.youwu.shouyinxitong.bean_used;

public class BackupCashRecordBean {
    /**
     * fund_id : 1
     * price : 12.22
     * type : 1
     * remark : test
     */

    private int fund_id;
    private double price;
    private int type;
    private String remark;

    public int getFund_id() {
        return fund_id;
    }

    public void setFund_id(int fund_id) {
        this.fund_id = fund_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
