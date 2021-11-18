package com.youwu.shouyinxitong.bean_used;

import java.util.List;

public class SystemBannerBean {


    /**
     * items : [{"image":"jpg/b78db00459202010311033497516.jpg","name":"1"},{"image":"jpg/aa952b3933202010311034025957.jpg","name":"2"},{"image":"jpg/1ca773e657202010311458209208.jpg","name":"轮播"}]
     * video :
     * token : b5fFfpEVRhpitGveu9jFx8DbVnLhUoIZALZi50nLl31UHzMzZSTByUUFYdC8y5G7lxzN7xpC1KLcSPWRxzQH76BBclr+nQjhklqbrSECt74GjOy7oUU75c8W99ErCenEgGBmQJ2zEun7JNiqByA0eOwFNDfjVxvJyPQswskEa5vXjhtesTEfqq/4OaT+YfZU
     */

    private String video;
    private String token;
    private List<ItemsBean> items;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

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

    public static class ItemsBean {
        /**
         * image : jpg/b78db00459202010311033497516.jpg
         * name : 1
         */

        private String image;
        private String name;

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
    }
}
