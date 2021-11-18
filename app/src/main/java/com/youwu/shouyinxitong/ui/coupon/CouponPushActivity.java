package com.youwu.shouyinxitong.ui.coupon;


import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.databinding.ActivityCouponPushBinding;
import com.youwu.shouyinxitong.databinding.ActivityCouponPushPackageBinding;

import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 多选推送优惠券
 * 2021/11/10
 * 金库
 */

public class CouponPushActivity extends BaseActivity<ActivityCouponPushBinding, CouponPushViewModel> {



    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_coupon_push;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //点击优惠券推送监听
        viewModel.LiveEvent.observe(this, new Observer<RechargeRecordBean.ListBean>() {
            @Override
            public void onChanged(RechargeRecordBean.ListBean Bean) {
                if (Bean.getCurrentSelect()==1){
                    Bean.setCurrentSelect(0);
                }else {
                    Bean.setCurrentSelect(1);
                }
                CouponPushItemViewModel itemViewModel = new CouponPushItemViewModel(viewModel, Bean);
                viewModel.CouponPushList.set(Bean.getPosition(), itemViewModel);

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        //获取优惠券包
        viewModel.getList();
    }
}
