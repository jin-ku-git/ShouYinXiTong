package com.youwu.shouyinxitong.data.source.http.service;


import com.youwu.shouyinxitong.bean_used.StoreBean;
import com.youwu.shouyinxitong.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 2021/10/20.
 * 金库
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<DemoEntity>> demoPost(@Field("catalog") String catalog);

    /**
     * 登录
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("machine/stafflogin")
    Observable<BaseBean<StoreBean>> CALOGIN(@Field("jsonData") String jsonData);

    /**
     * 获取副屏banner
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("machine/getbanner")
    Observable<BaseBean<Object>> GETBANNER(@Field("jsonData") String jsonData);

    /**
     * 获取商品分类
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("product/categoryList")
    Observable<BaseBean<Object>> CATEGORY_LIST(@Field("jsonData") String jsonData);
    /**
     * 获取套餐分类
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("meal_set_goods/mealGoodsType")
    Observable<BaseBean<Object>> MEALGOODSTYPE(@Field("jsonData") String jsonData);
    /**
     * 获取商品列表
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("product/page")
    Observable<BaseBean<Object>> PAGE(@Field("jsonData") String jsonData);

    /**
     * 获取套餐商品列表
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("meal_set_goods/storeMealSetGoodsList")
    Observable<BaseBean<Object>> MEALGOODSLIST(@Field("jsonData") String jsonData);
    /**
     * 优惠活动
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("activity/list")
    Observable<BaseBean<Object>> LIST(@Field("jsonData") String jsonData);
    /**
     * 根据会员卡/IC卡号获取会员
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("member/getIcCardInfo")
    Observable<BaseBean<Object>> PAYMENT_CARD_TWO(@Field("jsonData") String jsonData);
    /**
     * 根据会员码获取会员
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("member/paymentCode")
    Observable<BaseBean<Object>> PAYMENT_CODE(@Field("jsonData") String jsonData);
    /**
     * 获取优惠券
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("member/conversion")
    Observable<BaseBean<Object>> CONVERSION(@Field("jsonData") String jsonData);
    /**
     * 获取销售统计
     * @param jsonData
     * @return
     */
    @FormUrlEncoded
    @POST("statistics/index")
    Observable<BaseBean<Object>> STATISTICS(@Field("jsonData") String jsonData);

}
