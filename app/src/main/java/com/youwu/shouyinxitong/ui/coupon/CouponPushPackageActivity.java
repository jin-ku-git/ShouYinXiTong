package com.youwu.shouyinxitong.ui.coupon;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.databinding.ActivityCouponPushPackageBinding;
import java.util.ArrayList;
import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 推送优惠券包
 * 2021/11/10
 * 金库
 */

public class CouponPushPackageActivity extends BaseActivity<ActivityCouponPushPackageBinding, CouponPushPackageViewModel> {


    private RechargeRecordBean.ListBean listBean;//
    //默认选中第一个
    private int currentType = 0;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_coupon_push_package;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //优惠券包监听
        viewModel.goodsList.observe(this, new Observer<ArrayList<RechargeRecordBean.ListBean>>() {
            @Override
            public void onChanged(ArrayList<RechargeRecordBean.ListBean> TypeBean) {
                listBean = TypeBean.get(0);

            }
        });

        //点击优惠券包监听
        viewModel.LiveEvent.observe(this, new Observer<RechargeRecordBean.ListBean>() {
            @Override
            public void onChanged(RechargeRecordBean.ListBean Bean) {
                listBean.setCurrentSelect(0);
                CouponPushPackageItemViewModel itemViewModels = new CouponPushPackageItemViewModel(viewModel, listBean);
                viewModel.CouponPushList.set(currentType, itemViewModels);
                currentType = Bean.getPosition();
                Bean.setCurrentSelect(1);
                CouponPushPackageItemViewModel itemViewModel = new CouponPushPackageItemViewModel(viewModel, Bean);
                viewModel.CouponPushList.set(Bean.getPosition(), itemViewModel);

                listBean = Bean;
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
