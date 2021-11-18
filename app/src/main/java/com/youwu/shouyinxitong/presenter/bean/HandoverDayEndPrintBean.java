package com.youwu.shouyinxitong.presenter.bean;

import java.io.Serializable;

public class HandoverDayEndPrintBean implements Serializable {

    private String admin_name;
    private String store_name;
    private String handoverStartTime;
    private String handoverEndTime;
    private String handoverDayendSum;
    private String handoverDayendReal;
    private String wx;
    private String zfb;
    private String cashi;
    private String yue;
    private String mt;

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getHandoverStartTime() {
        return handoverStartTime;
    }

    public void setHandoverStartTime(String handoverStartTime) {
        this.handoverStartTime = handoverStartTime;
    }

    public String getHandoverEndTime() {
        return handoverEndTime;
    }

    public void setHandoverEndTime(String handoverEndTime) {
        this.handoverEndTime = handoverEndTime;
    }

    public String getHandoverDayendSum() {
        return handoverDayendSum;
    }

    public void setHandoverDayendSum(String handoverDayendSum) {
        this.handoverDayendSum = handoverDayendSum;
    }

    public String getHandoverDayendReal() {
        return handoverDayendReal;
    }

    public void setHandoverDayendReal(String handoverDayendReal) {
        this.handoverDayendReal = handoverDayendReal;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getZfb() {
        return zfb;
    }

    public void setZfb(String zfb) {
        this.zfb = zfb;
    }

    public String getCashi() {
        return cashi;
    }

    public void setCashi(String cashi) {
        this.cashi = cashi;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }
}
