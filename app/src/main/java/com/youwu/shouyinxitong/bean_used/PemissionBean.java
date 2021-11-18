package com.youwu.shouyinxitong.bean_used;

import java.io.Serializable;
import java.util.List;

public class PemissionBean implements Serializable {

    /**
     * id : 100
     * label : 系统设置
     * children : [{"id":110,"label":"副屏设置","children":[]},{"id":120,"label":"账号设置","children":[]},{"id":130,"label":"关于我们","children":[]}]
     */

    private int id;
    private String label;
    private List<ChildrenBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean {
        /**
         * id : 110
         * label : 副屏设置
         * children : []
         */

        private int id;
        private String label;
        private List<?> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
