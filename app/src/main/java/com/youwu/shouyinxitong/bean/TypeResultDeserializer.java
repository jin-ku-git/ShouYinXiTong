package com.youwu.shouyinxitong.bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean_used.TypeResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeResultDeserializer implements JsonDeserializer<TypeResult> {

    @Override
    public TypeResult deserialize(JsonElement arg0, Type arg1,
                                  JsonDeserializationContext arg2) throws JsonParseException {

        JsonArray asJsonArray = arg0.getAsJsonArray();
        TypeResult result = new TypeResult();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject jsonOb = jsonElement.getAsJsonObject();


            if (jsonOb.has("meal_goods_name")) {//套餐  如果有这个字段的   就是套餐，这个是我根据json  自己定的

                MealsItemBean itemsBean = new MealsItemBean();
                itemsBean.setBuynum(jsonOb.get("buynum").getAsInt());
                itemsBean.setGoodsJson(jsonOb.get("goodsJson").getAsString());
                itemsBean.setGoods_type_id(jsonOb.get("goods_type_id").getAsInt());
                itemsBean.setImg(jsonOb.get("img").getAsString());
                itemsBean.setMeal_goods_id(jsonOb.get("meal_goods_id").getAsInt());
                itemsBean.setMeal_goods_name(jsonOb.get("meal_goods_name").getAsString());
                itemsBean.setMeal_goods_price(jsonOb.get("meal_goods_price").getAsString());
                itemsBean.setMeal_goods_sku(jsonOb.get("meal_goods_sku").getAsString());
                itemsBean.setSales_status(jsonOb.get("sales_status").getAsString());
                Gson gson = new Gson();
                Type type = new TypeToken<List<MealGoodsBean>>() {}.getType();
                List<MealGoodsBean> list = gson.fromJson(itemsBean.getGoodsJson(), type);
                itemsBean.setMeal_store_goods_detail(list);
                result.data.add(itemsBean);
            } else {//单个商品
                YWGoodBean ywGoodBean = new YWGoodBean();
                Log.d("取单123",jsonOb.toString());
//                if (!jsonOb.get("product_name").getAsString().equals("无码商品")){
//                    if (jsonOb.getAsString()!=null){
//                        ywGoodBean.setImage(jsonOb.get("image").getAsString());
//                    }
//                }

//                ywGoodBean.setCategory_id(jsonOb.get("category_id").getAsString());
//                ywGoodBean.setId(jsonOb.get("id").getAsString());
                ywGoodBean.setProduct_name(jsonOb.get("product_name").getAsString());
//                ywGoodBean.setLetters(jsonOb.get("letters").getAsString());
                ywGoodBean.setCanAddShop(jsonOb.get("canAddShop").getAsBoolean());
                ywGoodBean.setPrice(jsonOb.get("price").getAsString());

                ywGoodBean.setSku(jsonOb.get("sku").getAsString());
                ywGoodBean.setSelect(jsonOb.get("select").getAsInt());
                ywGoodBean.setBuynum(jsonOb.get("buynum").getAsInt());
                ywGoodBean.setDiscount(jsonOb.get("discount").getAsString());
                ywGoodBean.setSales_status(jsonOb.get("sales_status").getAsString());
                ywGoodBean.setGoods_type(jsonOb.get("goods_type").getAsInt());
                ywGoodBean.setWeightnum(jsonOb.get("weightnum").getAsDouble());

//                ywGoodBean.setSpecsBean(specs);

                List<YWGoodBean.SpecsBean> specsBeans = new ArrayList<>();


//                ywGoodBean.setSpecs(specsBeans);
                result.data.add(ywGoodBean);
            }
        }
        return result;
    }
}