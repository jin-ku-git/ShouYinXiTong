package com.youwu.shouyinxitong.bean_used;

import java.io.Serializable;
import java.util.List;

public class LanguageBean implements Serializable {

        private String name;//语言
        private String id;
        private int position;
        private boolean select = false;

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

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

}
