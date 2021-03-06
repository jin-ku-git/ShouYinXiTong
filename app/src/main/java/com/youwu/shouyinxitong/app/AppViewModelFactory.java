package com.youwu.shouyinxitong.app;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.ui.applygoods.ApplyOrderViewModel;
import com.youwu.shouyinxitong.ui.calculate.CheckNumItemViewModel;
import com.youwu.shouyinxitong.ui.calculate.CheckNumViewModel;
import com.youwu.shouyinxitong.ui.commodity.EditCommodityDetailViewModel;
import com.youwu.shouyinxitong.ui.login.LoginViewModel;
import com.youwu.shouyinxitong.ui.main.MainViewModel;
import com.youwu.shouyinxitong.ui.order.OrderNewlyBuildViewModel;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersViewModel;
import com.youwu.shouyinxitong.ui.record.SalesCountViewModel;
import com.youwu.shouyinxitong.ui.search.SearchVipViewModel;


/**
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
         if (modelClass.isAssignableFrom(LoginViewModel.class)) {//??????
            return (T) new LoginViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(MainViewModel.class)) {//??????
            return (T) new MainViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SalesCountViewModel.class)) {//????????????
             return (T) new SalesCountViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(RechargeOrdersViewModel.class)) {//????????????
             return (T) new RechargeOrdersViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(ApplyOrderViewModel.class)) {//????????????
             return (T) new ApplyOrderViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(EditCommodityDetailViewModel.class)) {//????????????
             return (T) new EditCommodityDetailViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(SearchVipViewModel.class)) {//????????????
             return (T) new SearchVipViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(CheckNumViewModel.class)) {//??????
             return (T) new CheckNumViewModel(mApplication, mRepository);
         }else if (modelClass.isAssignableFrom(OrderNewlyBuildViewModel.class)) {//????????????
             return (T) new OrderNewlyBuildViewModel(mApplication, mRepository);
         }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
