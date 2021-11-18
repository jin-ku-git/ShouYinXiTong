package com.youwu.shouyinxitong.bean_used;

import com.youwu.shouyinxitong.bean.YWGoodBean;

import java.util.List;

public class GoodsListBean {
    /**
     * totalNumber : 2
     * pageNumber : 1
     * totalPage : 1
     * pageSize : 100
     * items : [{"image":"http://images.youwuu.com/22","specs":[{"price":"0.00","spec_name":"dd","repertory":"0.000","sku":"33"},{"price":"0.00","spec_name":"ss","repertory":"0.000","sku":"44"}],"category_id":1,"id":2,"product_name":"测试产品2","letters":""},{"image":"http://images.youwuu.com/1","specs":[{"price":"0.00","spec_name":"小杯","repertory":"0.000","sku":"22"},{"price":"0.00","spec_name":"大杯","repertory":"0.000","sku":"11"}],"category_id":1,"id":1,"product_name":"测试产品","letters":"cs"}]
     * token : e54a6a1e-c171-43f0-9f92-b01ea97cc98d
     */

    public String totalNumber;
    public String pageNumber;
    public String totalPage;
    public String pageSize;
    public String token;
    public List<YWGoodBean> items;
    public List<MealsItemBean> itemsMeals;

    public List<MealsItemBean> getItemsMeals() {
        return itemsMeals;
    }

    public void setItemsMeals(List<MealsItemBean> itemsMeals) {
        this.itemsMeals = itemsMeals;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<YWGoodBean> getItems() {
        return items;
    }

    public void setItems(List<YWGoodBean> items) {
        this.items = items;
    }

}
