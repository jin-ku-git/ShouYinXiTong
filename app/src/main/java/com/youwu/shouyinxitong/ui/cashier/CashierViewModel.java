package com.youwu.shouyinxitong.ui.cashier;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 收银Model
 * 2021/11/08
 * 金库
 */

public class CashierViewModel extends BaseViewModel {
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();

    public ObservableField<Integer> entity = new ObservableField<>();


    public CashierViewModel(@NonNull Application application) {
        super(application);
    }



    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
    //组合支付
    public BindingCommand payBlendonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.set(1);
        }
    });
    //现金支付
    public BindingCommand payCashonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.set(2);
        }
    });
    //余额支付
    public BindingCommand payBalanceonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.set(3);
        }
    });
    //微信支付
    public BindingCommand payWeChatonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.set(4);
        }
    });
    //支付宝支付
    public BindingCommand payAlipayonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.set(5);
        }
    });
}
