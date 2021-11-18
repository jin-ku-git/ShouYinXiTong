package com.youwu.shouyinxitong.bean;

import android.graphics.RectF;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class YWGoodBean2 {
    @Override
    public String toString() {
        return "YWGoodBean{" +
                "bar_code='" + bar_code + '\'' +
                ", price='" + price + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", sku='" + sku + '\'' +

                ", token='" + token + '\'' +
                ", vip_price='" + vip_price + '\'' +
                ", weight='" + weight + '\'' +
                ", repertory='" + repertory + '\'' +
                ", status='" + status + '\'' +

                ", image='" + image + '\'' +
                ", good_id='" + good_id + '\'' +
                ", show='" + show + '\'' +
                ", reset_price_time='" + reset_price_time + '\'' +
                ", reset_price='" + reset_price + '\'' +

                ", optNum=" + optNum +
                ", atypidNum=" + atypidNum +
                ", vipPrice='" + vipPrice + '\'' +
                ", is_standard='" + is_standard + '\'' +
                ", pinyin_name='" + pinyin_name + '\'' +

                ", letters='" + letters + '\'' +
                ", isVipAction=" + isVipAction +
                ", discount=" + discount +
                ", actionSaleId='" + actionSaleId + '\'' +
                ", select='" + select + '\'' +
                ", edtAtypidNum=" + edtAtypidNum +
                '}';
    }

    public static final int ATYPID_GOOD = 0;//非标品
    public static final int STANDARD_GOOD = 1;//标品

    /**
     * bar_code : 测试内容k40a
     * price : 测试内容6614
     * product_id : 测试内容1s2i
     * product_name : 测试内容lho3
     * sku : 测试内容d8w2
     * token : 测试内容8871
     * vip_price : 测试内容63xg
     * weight : 测试内容i9u1
     */

    //非扫描的商品  0
    private String isNotSelect = "0";
    private String bar_code;
    private String price;
    private String product_id;
    private String product_name;
    private String sku;
    private String token;
    private String vip_price;
    private String weight;
    private String repertory;
    private String status;
    private String image;
    private String good_id;
    private String show;
    private Boolean isTuiJian = false;
    private String reset_price_time = "";
    private String reset_price = "0";
    //标品数量  非标品的扫描个数
    private int optNum = 1;
    //非标品  数量
    private float atypidNum;
    private String vipPrice;
    private String is_standard;
    private String pinyin_name;
    private String letters;

    private boolean isVipAction;
    private float discount;
    private String actionSaleId;

    private float edtAtypidNum;
    private int select = 0;
    private RectF rectF;

    //商品重量
    private String goods_weight;

    public String getGoods_weight() {
        return goods_weight;
    }

    public void setGoods_weight(String goods_weight) {
        this.goods_weight = goods_weight;
    }

    public String isNotSelect() {
        return isNotSelect;
    }

    public void setNotSelect(String notSelect) {
        isNotSelect = notSelect;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public Boolean getTuiJian() {
        return isTuiJian;
    }

    public void setTuiJian(Boolean tuiJian) {
        isTuiJian = tuiJian;
    }

    public static class XYPoint implements Serializable {
        public XYPoint(List<Integer> posArray) {
            this.posArray = posArray;
        }

        private List<Integer> posArray = new ArrayList<>();

        public List<Integer> getPosArray() {
            return posArray;
        }

        public void setPosArray(List<Integer> posArray) {
            this.posArray = posArray;
        }
    }

    public List<XYPoint> getXyPoints() {
        return xyPoints == null ? new ArrayList<XYPoint>() : xyPoints;
    }

    public void setXyPoints(List<XYPoint> xyPoints) {
        this.xyPoints = xyPoints;
    }

    List<XYPoint> xyPoints = new ArrayList<>();

    public void setActionSaleId(String actionSaleId) {
        this.actionSaleId = actionSaleId;
    }

    public String getActionSaleId() {
        return actionSaleId;
    }

    public void setVipAction(boolean vipAction) {
        isVipAction = vipAction;
    }

    public boolean isVipAction() {
        return isVipAction;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getDiscount() {
        return discount / 100.0f;
    }

    public void setEdtAtypidNum(float edtAtypidNum) {
        this.edtAtypidNum = edtAtypidNum;
    }

    public float getEdtAtypidNum() {
        return edtAtypidNum;
    }


    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getLetters() {
        if (!TextUtils.isEmpty(letters)) {
            return letters.toLowerCase();
        }
        return "#";
    }


    public void setPinyin_name(String pinyin_name) {
        this.pinyin_name = pinyin_name;
    }

    public String getPinyin_name() {
        // 汉字转换成拼音
        String pinyin = Pinyin.toPinyin("" + product_name, "");
        if (null == pinyin || "".equals(pinyin)) {
            return "#";
        } else {
            return pinyin.toLowerCase();
        }
    }

    public void setIs_standard(String is_standard) {
        this.is_standard = is_standard;
    }

    public String getIs_standard() {
        return is_standard;
    }

    public String getReset_price_time() {
        return reset_price_time;
    }

    public void setReset_price_time(String reset_price_time) {
        this.reset_price_time = reset_price_time;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getReset_price() {
        return reset_price;
    }

    public void setReset_price(String reset_price) {
        this.reset_price = reset_price;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRepertory() {
        return repertory;
    }

    public void setRepertory(String repertory) {
        this.repertory = repertory;
    }

    public int getOptNum() {
        return optNum;
    }

    public void setOptNum(int optNum) {
        this.optNum = optNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public float getAtypidNum() {
//        return atypidNum / 1000.0f;
        return atypidNum;
    }

    public void setAtypidNum(float atypidNum) {
        this.atypidNum = atypidNum;
    }


    public YWGoodBean2() {
    }


}
