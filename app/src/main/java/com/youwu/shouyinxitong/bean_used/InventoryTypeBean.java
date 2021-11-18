package com.youwu.shouyinxitong.bean_used;

import androidx.databinding.BaseObservable;

import java.util.List;

/**
 * 分类
 */
public class InventoryTypeBean extends BaseObservable {
    /**
     * items : [{"parent_id":"0","name":"测试分类","id":"1","sort":"0"},{"parent_id":"0","name":"测试分类","id":"1","sort":"0"}]
     * token : e54a6a1e-c171-43f0-9f92-b01ea97cc98d
     */

    public String token;
    public List<GoodsTypeBean> items;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<GoodsTypeBean> getGoodsTypeBeans() {
        return items;
    }

    public void setGoodsTypeBeans(List<GoodsTypeBean> items) {
        this.items = items;
    }

    public List<GoodsTypeBean> getItems() {
        return items;
    }

    public void setItems(List<GoodsTypeBean> items) {
        this.items = items;
    }


    public static class GoodsTypeBean {
        /**
         * parent_id : 0
         * name : 测试分类
         * id : 1
         * sort : 0
         */

        public String parent_id;
        public String name;
        public String id;             //分类id
        public String sort;
        public int currentSelect;
        public String fangIXang = "1";
        public int type = 1;//1.普通2加号3空白
        public boolean isMeals = false; //是否是套餐

        public String add_time;
        public String update_time;
        public String img;
        public String status;
        public String start_time;
        public String end_time;


        public int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isMeals() {
            return isMeals;
        }

        public void setMeals(boolean meals) {
            isMeals = meals;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFangIXang() {
            return fangIXang;
        }

        public void setFangIXang(String fangIXang) {
            this.fangIXang = fangIXang;
        }

        public int getCurrentSelect() {
            return currentSelect;
        }

        public void setCurrentSelect(int currentSelect) {
            this.currentSelect = currentSelect;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }


}
