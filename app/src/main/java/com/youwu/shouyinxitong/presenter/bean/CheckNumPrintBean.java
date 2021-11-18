package com.youwu.shouyinxitong.presenter.bean;

import java.io.Serializable;
import java.util.List;

public class CheckNumPrintBean implements Serializable {

    private String checkNumTime;
    private String admin_name;
    private String store_name;
    private List checkNumData;

    public String getCheckNumTime() {
        return checkNumTime;
    }

    public void setCheckNumTime(String checkNumTime) {
        this.checkNumTime = checkNumTime;
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

    public List getCheckNumData() {
        return checkNumData;
    }

    public void setCheckNumData(List checkNumData) {
        this.checkNumData = checkNumData;
    }
}
