package com.youwu.shouyinxitong.data.source;


import com.youwu.shouyinxitong.bean_used.StoreBean;
import com.youwu.shouyinxitong.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

    //模拟上拉加载
    Observable<DemoEntity> loadMore();

    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String jsonData);
    //登录
    Observable<BaseBean<StoreBean>> CALOGIN(String jsonData);
    //获取副屏banner
    Observable<BaseBean<Object>> GETBANNER(String jsonData);
    //获取商品分类
    Observable<BaseBean<Object>> CATEGORY_LIST(String jsonData);
    //获取套餐分类
    Observable<BaseBean<Object>> MEALGOODSTYPE(String jsonData);
    //获取商品列表
    Observable<BaseBean<Object>> PAGE(String jsonData);
    //获取套餐商品
    Observable<BaseBean<Object>> MEALGOODSLIST(String jsonData);
    //优惠活动
    Observable<BaseBean<Object>> LIST(String jsonData);
    //根据会员卡/IC卡号获取会员
    Observable<BaseBean<Object>> PAYMENT_CARD_TWO(String jsonData);
    //根据会员码获取会员
    Observable<BaseBean<Object>> PAYMENT_CODE(String jsonData);
    //获取优惠券
    Observable<BaseBean<Object>> CONVERSION(String jsonData);
    //获取销售统计
    Observable<BaseBean<Object>> STATISTICS(String jsonData);


}
