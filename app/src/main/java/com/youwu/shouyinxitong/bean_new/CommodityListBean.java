package com.youwu.shouyinxitong.bean_new;

import androidx.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

public class CommodityListBean extends BaseObservable implements Serializable {

    private List<CommodityDetailBean> item;

    public List<CommodityDetailBean> getItem() {
        return item;
    }

    public void setItem(List<CommodityDetailBean> item) {
        this.item = item;
    }
}
