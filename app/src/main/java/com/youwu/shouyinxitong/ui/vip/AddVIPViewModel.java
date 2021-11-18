package com.youwu.shouyinxitong.ui.vip;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.coupon.CouponPushActivity;
import com.youwu.shouyinxitong.ui.coupon.CouponPushPackageActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2021/10/28
 * 金库
 */

public class AddVIPViewModel extends BaseViewModel {

    //使用LiveData
    public SingleLiveEvent<String> addVIP = new SingleLiveEvent<>();
    public ObservableField<UserBean> userBean = new ObservableField<>();

    public int type;
    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;

    public AddVIPViewModel(@NonNull Application application) {
        super(application);
    }

    public void setType(int type) {

            this.type = type;

    }

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //提交按钮点击事件
    public BindingCommand onCmtClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            String submitJson = new Gson().toJson(userBean);
            addVIP.setValue(submitJson);
        }
    });

    //跳转到单选优惠券包推送
    public BindingCommand CouPonPushPackageonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
         startActivity(CouponPushPackageActivity.class);
        }
    });
    //跳转到多选优惠券推送
    public BindingCommand CouPonPushonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
         startActivity(CouponPushActivity.class);
        }
    });
    //跳转到充值页面
    public BindingCommand RechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(VipRechargeActivity.class);
        }
    });


}
