package com.youwu.shouyinxitong.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;


import com.youwu.shouyinxitong.bean_used.StoreBean;
import com.youwu.shouyinxitong.data.source.HttpDataSource;
import com.youwu.shouyinxitong.data.source.LocalDataSource;
import com.youwu.shouyinxitong.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2021/10/29.
 */
public class DemoRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static DemoRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private DemoRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static DemoRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<Object> login() {
        return mHttpDataSource.login();
    }
    /**
     *登录
     * @return
     */
    @Override
    public Observable<BaseBean<StoreBean>> CALOGIN(String jsonData) {
        return mHttpDataSource.CALOGIN(jsonData);
    }

    /**
     * 获取副屏banner
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> GETBANNER(String jsonData) {
        return mHttpDataSource.GETBANNER(jsonData);
    }
    /**
     * 获取商品分类
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CATEGORY_LIST(String jsonData) {
        return mHttpDataSource.CATEGORY_LIST(jsonData);
    }
    /**
     * 获取套餐分类
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> MEALGOODSTYPE(String jsonData) {
        return mHttpDataSource.MEALGOODSTYPE(jsonData);
    }
    /**
     * 获取商品列表
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAGE(String jsonData) {
        return mHttpDataSource.PAGE(jsonData);
    }
    /**
     * 获取套餐商品
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> MEALGOODSLIST(String jsonData) {
        return mHttpDataSource.MEALGOODSLIST(jsonData);
    }
    /**
     * 优惠活动
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> LIST(String jsonData) {
        return mHttpDataSource.LIST(jsonData);
    }
    /**
     *  根据会员卡/IC卡号获取会员
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAYMENT_CARD_TWO(String jsonData) {
        return mHttpDataSource.PAYMENT_CARD_TWO(jsonData);
    }
    /**
     *  根据会员码获取会员
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> PAYMENT_CODE(String jsonData) {
        return mHttpDataSource.PAYMENT_CODE(jsonData);
    }
    /**
     * 获取优惠券
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> CONVERSION(String jsonData) {
        return mHttpDataSource.CONVERSION(jsonData);
    }
    /**
     * 获取销售统计
     * @return
     */
    @Override
    public Observable<BaseBean<Object>> STATISTICS(String jsonData) {
        return mHttpDataSource.STATISTICS(jsonData);
    }


    @Override
    public Observable<DemoEntity> loadMore() {
        return mHttpDataSource.loadMore();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return mHttpDataSource.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return mHttpDataSource.demoPost(catalog);
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }
}
