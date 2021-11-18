package com.youwu.shouyinxitong.ui.setup;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2021/10/26
 * 金库
 */

public class SystemSettingViewModel extends BaseViewModel {
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<Integer> TypeEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public ObservableField<Integer> type = new ObservableField<>();

    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;

//    public int type;//1 副屏设置 2 账号设置 3 关于我们 4 蓝牙设置s

    public SystemSettingViewModel(@NonNull Application application) {
        super(application);
    }


    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
                finish();
        }
    });
    //副屏设置点击事件
    public BindingCommand setViceScreenOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            TypeEvent.setValue(1);

        }
    });
    //账号设置点击事件
    public BindingCommand setAccountOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            TypeEvent.setValue(2);
        }
    });
    //关于我们点击事件
    public BindingCommand setAboutUsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            TypeEvent.setValue(3);
        }
    });
    //蓝牙设置点击事件
    public BindingCommand setAboutBleOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            TypeEvent.setValue(4);
        }
    });
    //切换语言点击事件
    public BindingCommand switchOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
}
