package com.youwu.shouyinxitong.bean_used;

import com.youwu.shouyinxitong.bean.YWGoodBean;

import java.io.Serializable;
import java.util.List;

public class ActivityListBean implements Serializable {
    /**
     * items : [{"image":"","discountDetails":[],"name":"梯度折扣111111111******","secondDiscount":[],"startTime":"2019-07-17 11:16:21","id":"54","sort":"4","endTime":"2020-12-30 00:00:00","type":"3","gradientDetails":[{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291108538","goodsName":"","isCoupon":"2"},{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291035440","goodsName":"","isCoupon":"2"},{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291033377","goodsName":"","isCoupon":"2"}]},{"image":"","discountDetails":[],"name":"第二件打折","secondDiscount":[{"discount":"90","sku":"1907291031588","goodsName":"1907291031588","isCoupon":"2"},{"discount":"85","sku":"1907291114204","goodsName":"1907291114204","isCoupon":"2"}],"startTime":"2019-07-17 11:16:21","id":"55","sort":"3","endTime":"2020-12-30 00:00:00","type":"4","gradientDetails":[]},{"image":"","discountDetails":[{"discount":"50","sku":"1907291035440","goodsName":"","isCoupon":"1"},{"discount":"50","sku":"1907291031588","goodsName":"","isCoupon":"1"}],"name":"打折促销","secondDiscount":[],"startTime":"2019-07-17 11:16:21","id":"53","sort":"1","endTime":"2020-12-30 00:00:00","type":"2","gradientDetails":[]},{"image":"","discountDetails":[{"discount":"80","sku":"1907291108538","goodsName":"冬菇滑鸡粥(大碗)","isCoupon":"1"}],"name":"打折促销1","secondDiscount":[],"startTime":"2019-07-17 11:16:21","id":"52","sort":"1","endTime":"2020-12-30 00:00:00","type":"2","gradientDetails":[]},{"image":"","discountDetails":[{"discount":"90","sku":"1907291035440","goodsName":"","isCoupon":"1"}],"name":"打折促销","secondDiscount":[],"startTime":"2019-07-17 11:16:21","id":"43","sort":"0","endTime":"2020-12-30 00:00:00","type":"2","gradientDetails":[]},{"image":"jpg/eee6d901e5201908051428014944.jpg","discountDetails":[],"name":"1二建打折","secondDiscount":[{"discount":"10","sku":"22","goodsName":"22","isCoupon":"1"},{"discount":"10","sku":"1908011039564","goodsName":"1908011039564","isCoupon":"1"},{"discount":"10","sku":"1907291133027","goodsName":"1907291133027","isCoupon":"1"},{"discount":"10","sku":"1907291127369","goodsName":"1907291127369","isCoupon":"1"}],"startTime":"2019-08-01 00:00:00","id":"71","sort":"0","endTime":"2019-08-31 23:59:59","type":"4","gradientDetails":[]},{"image":"","discountDetails":[{"discount":"90","sku":"1907291033377","goodsName":"","isCoupon":"1"},{"discount":"90","sku":"1907291031588","goodsName":"","isCoupon":"1"}],"name":"打折促销1","secondDiscount":[],"startTime":"2019-07-17 11:16:21","id":"49","sort":"0","endTime":"2020-12-30 00:00:00","type":"2","gradientDetails":[]},{"image":"","discountDetails":[{"discount":"90","sku":"1907291033377","goodsName":"","isCoupon":"1"},{"discount":"90","sku":"1907291031588","goodsName":"","isCoupon":"1"}],"name":"打折促销2019年8月3日17:08:39","secondDiscount":[],"startTime":"2019-08-01 00:00:00","id":"56","sort":"0","endTime":"2019-08-31 23:59:59","type":"2","gradientDetails":[]}]
     * token : 6b1fe85a-b255-438a-a4d0-ae85da143e12
     */

    private String token;
    private List<ItemsBean> items;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Comparable<ItemsBean>,Serializable{
        /**
         * image :
         * discountDetails : []
         * name : 梯度折扣111111111******
         * secondDiscount : []
         * startTime : 2019-07-17 11:16:21
         * id : 54
         * sort : 4
         * endTime : 2020-12-30 00:00:00
         * type : 3
         * gradientDetails : [{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291108538","goodsName":"","isCoupon":"2"},{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291035440","goodsName":"","isCoupon":"2"},{"isActivity":"2","priceValue":[{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}],"sku":"1907291033377","goodsName":"","isCoupon":"2"}]
         */

        private String image;
        private String name;
        private String startTime;
        private String id;
        private String sort = "99999";
        private String endTime;
        private String type;
        private List<GradientDetailsBean.PriceValueBean> priceValue;
        private List<GradientDetailsBean.Rebate> rebate;


        private String explain;
        private String isgoods;
        private String discount;
        private List<YWGoodBean> activityGoods;
        private String halfPrice;
        private String isCoupon;

        public List<GradientDetailsBean.PriceValueBean> getPriceValue() {
            return priceValue;
        }

        public void setPriceValue(List<GradientDetailsBean.PriceValueBean> priceValue) {
            this.priceValue = priceValue;
        }

        public List<GradientDetailsBean.Rebate> getRebate() {
            return rebate;
        }

        public void setRebate(List<GradientDetailsBean.Rebate> rebate) {
            this.rebate = rebate;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getIsgoods() {
            return isgoods;
        }

        public void setIsgoods(String isgoods) {
            this.isgoods = isgoods;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public List<YWGoodBean> getActivityGoods() {
            return activityGoods;
        }

        public void setActivityGoods(List<YWGoodBean> activityGoods) {
            this.activityGoods = activityGoods;
        }

        public String getHalfPrice() {
            return halfPrice;
        }

        public void setHalfPrice(String halfPrice) {
            this.halfPrice = halfPrice;
        }

        public String getIsCoupon() {
            return isCoupon;
        }

        public void setIsCoupon(String isCoupon) {
            this.isCoupon = isCoupon;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSort() {
            return Integer.parseInt(sort);
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getType() {
            return Integer.parseInt(type);
        }

        public void setType(String type) {
            this.type = type;
        }




        @Override
        public int compareTo(ItemsBean itemsBean) {
            return itemsBean.getSort();
        }

        public static class GradientDetailsBean implements Serializable{
            /**
             * isActivity : 2
             * priceValue : [{"reduce_price":"10","full_number":"2"},{"reduce_price":"30","full_number":"5"},{"reduce_price":"60","full_number":"5"}]
             * sku : 1907291108538
             * goodsName :
             * isCoupon : 2
             */

            private String isActivity;
            private String sku;
            private String goodsName;
            private String isCoupon;
            private List<PriceValueBean> priceValue;

            public int getIsActivity() {
                return Integer.parseInt(isActivity);
            }

            public void setIsActivity(String isActivity) {
                this.isActivity = isActivity;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getIsCoupon() {
                return isCoupon;
            }

            public void setIsCoupon(String isCoupon) {
                this.isCoupon = isCoupon;
            }

            public List<PriceValueBean> getPriceValue() {
                return priceValue;
            }

            public void setPriceValue(List<PriceValueBean> priceValue) {
                this.priceValue = priceValue;
            }

            public static class PriceValueBean implements Serializable{
                /**
                 * reduce_price : 10
                 * full_number : 2
                 */

                private String reduce_price;
                private String full_number;

                public String getReduce_price() {
                    return reduce_price;
                }

                public void setReduce_price(String reduce_price) {
                    this.reduce_price = reduce_price;
                }

                public String getFull_number() {
                    return full_number;
                }

                public void setFull_number(String full_number) {
                    this.full_number = full_number;
                }
            }

            public static class Rebate implements Serializable{
                /**
                 * reduce_price : 10
                 * full_number : 2
                 */

                private String rebate_price;
                private String full_number;

                public String getRebate_price() {
                    return rebate_price;
                }

                public void setRebate_price(String rebate_price) {
                    this.rebate_price = rebate_price;
                }

                public String getFull_number() {
                    return full_number;
                }

                public void setFull_number(String full_number) {
                    this.full_number = full_number;
                }
            }
        }

        public class SecondDiscount implements Serializable{


            /**
             * discount : 90
             * sku : 1907291031588
             * goodsName : 1907291031588
             * isCoupon : 2
             */

            private String discount;
            private String sku;
            private String goodsName;
            private String isCoupon;

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getIsCoupon() {
                return isCoupon;
            }

            public void setIsCoupon(String isCoupon) {
                this.isCoupon = isCoupon;
            }
        }

        public class DiscountDeyails implements Serializable{
            /**
             * discount : 80
             * sku : 1907291108538
             * goodsName : 冬菇滑鸡粥(大碗)
             * isCoupon : 1
             */

            private String discount;
            private String sku;
            private String goodsName;
            private String isCoupon;

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getIsCoupon() {
                return isCoupon;
            }

            public void setIsCoupon(String isCoupon) {
                this.isCoupon = isCoupon;
            }
        }
    }
}
