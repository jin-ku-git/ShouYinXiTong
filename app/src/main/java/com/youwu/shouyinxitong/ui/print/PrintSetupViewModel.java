package com.youwu.shouyinxitong.ui.print;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 打印设置
 * 2021/11/03
 *
 */

public class PrintSetupViewModel extends BaseViewModel {
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();

    public PrintSetupViewModel(@NonNull Application application) {
        super(application);
    }


    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
        }
    });

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
}
