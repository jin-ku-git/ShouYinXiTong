package com.youwu.shouyinxitong.ui.record;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinxitong.app.UriUtils;
import com.youwu.shouyinxitong.bean_new.SalesCountBean;
import com.youwu.shouyinxitong.bean_new.TimeBean;
import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.presenter.bean.SaleCountBean;
import com.youwu.shouyinxitong.utils_tool.RxToast;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 2021/11/01
 * 金库
 */

public class SalesCountViewModel extends BaseViewModel<DemoRepository> {
    public ObservableField<SalesCountBean> entity = new ObservableField<>();
    public ObservableField<String> start_time = new ObservableField<>();
    public ObservableField<String> end_time = new ObservableField<>();
    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();
    //传递接口返回值
    public SingleLiveEvent<SaleCountBean> dataEvent = new SingleLiveEvent<>();

    public SalesCountViewModel(@NonNull Application application, DemoRepository model) {
        super(application,model);
    }


    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //开始时间点击事件
    public BindingCommand StartTimeonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(1);
        }
    });
    //结束时间点击事件
    public BindingCommand EndTimeonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(2);
        }
    });
    //打印小票点击事件
    public BindingCommand PrintonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(3);
        }
    });

    public void getData(String startTime,String endTime){

        TimeBean timeBean=new TimeBean();
        timeBean.setStartTime(startTime);
        timeBean.setEndTime(endTime);

        //获取销售统计
        model.STATISTICS(UriUtils.getRequestPostUrl(timeBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("获取销售统计返回结果-----已解析",toPrettyFormat(submitJson));
                        dismissDialog();

                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            SalesCountBean systemBannerBean = JSON.parseObject(toPrettyFormat(submitJsonData), SalesCountBean.class);
                            SaleCountBean saleCountBean = JSON.parseObject(toPrettyFormat(submitJsonData), SaleCountBean.class);
                            entity.set(systemBannerBean);
                            dataEvent.setValue(saleCountBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            String submitJson = new Gson().toJson(throwable);
                            Log.e("返回结果-----已解析",toPrettyFormat(submitJson));
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }

}
