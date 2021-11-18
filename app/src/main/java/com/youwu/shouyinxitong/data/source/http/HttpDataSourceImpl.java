package com.youwu.shouyinxitong.data.source.http;


import com.youwu.shouyinxitong.bean_used.StoreBean;
import com.youwu.shouyinxitong.data.source.HttpDataSource;
import com.youwu.shouyinxitong.data.source.http.service.DemoApiService;
import com.youwu.shouyinxitong.entity.DemoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private DemoApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(DemoApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(DemoApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<DemoEntity> loadMore() {
        return Observable.create(new ObservableOnSubscribe<DemoEntity>() {
            @Override
            public void subscribe(ObservableEmitter<DemoEntity> observableEmitter) throws Exception {
                DemoEntity entity = new DemoEntity();
                List<DemoEntity.ItemsEntity> itemsEntities = new ArrayList<>();
                //模拟一部分假数据
                for (int i = 0; i < 10; i++) {
                    DemoEntity.ItemsEntity item = new DemoEntity.ItemsEntity();
                    item.setId(-1);
                    item.setName("模拟条目");
                    itemsEntities.add(item);
                }
                entity.setItems(itemsEntities);
                observableEmitter.onNext(entity);
            }
        }).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }

    /**
     * 登录
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<StoreBean>> CALOGIN(String jsonData) {
        return apiService.CALOGIN(jsonData);
    }

    /**
     * 获取副屏banner
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GETBANNER(String jsonData) {
        return apiService.GETBANNER(jsonData);
    }

    /**
     * 获取商品分类
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CATEGORY_LIST(String jsonData) {
        return apiService.CATEGORY_LIST(jsonData);
    }
    /**
     * 获取套餐分类
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> MEALGOODSTYPE(String jsonData) {
        return apiService.MEALGOODSTYPE(jsonData);
    }
    /**
     * 获取商品列表
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAGE(String jsonData) {
        return apiService.PAGE(jsonData);
    }
    /**
     * 获取套餐商品列表
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> MEALGOODSLIST(String jsonData) {
        return apiService.MEALGOODSLIST(jsonData);
    }
    /**
     * 优惠活动
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> LIST(String jsonData) {
        return apiService.LIST(jsonData);
    }
    /**
     * 根据会员卡/IC卡号获取会员
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAYMENT_CARD_TWO(String jsonData) {
        return apiService.PAYMENT_CARD_TWO(jsonData);
    }
    /**
     * 根据会员码获取会员
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAYMENT_CODE(String jsonData) {
        return apiService.PAYMENT_CODE(jsonData);
    }
    /**
     * 获取优惠券
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CONVERSION(String jsonData) {
        return apiService.CONVERSION(jsonData);
    }
    /**
     * 获取销售统计
     * @param jsonData
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> STATISTICS(String jsonData) {
        return apiService.STATISTICS(jsonData);
    }
}
