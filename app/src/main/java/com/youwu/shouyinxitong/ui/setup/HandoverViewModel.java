package com.youwu.shouyinxitong.ui.setup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.ui.form.FormFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2021/10/28
 * 金库
 */

public class HandoverViewModel extends BaseViewModel {

    //使用LiveData
    public SingleLiveEvent<String> EditTextEvent = new SingleLiveEvent<>();
    public ObservableField<String> editText = new ObservableField<>("");


    public HandoverViewModel(@NonNull Application application) {
        super(application);
    }

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


}
