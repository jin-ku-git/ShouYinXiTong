package com.youwu.shouyinxitong.presenter.bean;

import java.io.Serializable;
import java.util.List;

public class InventoryPrintBean implements Serializable {

    private String inventoryTime;
    private String admin_name;
    private String store_name;
    private List inventoryData;

    public String getInventoryTime() {
        return inventoryTime;
    }

    public void setInventoryTime(String inventoryTime) {
        this.inventoryTime = inventoryTime;
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

    public List getInventoryData() {
        return inventoryData;
    }

    public void setInventoryData(List inventoryData) {
        this.inventoryData = inventoryData;
    }
}
