package com.youwu.shouyinxitong.ui.vip;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargePageBean;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.databinding.ActivityDemoBinding;
import com.youwu.shouyinxitong.databinding.ActivityVipRechargeBinding;
import com.youwu.shouyinxitong.ui.coupon.CouponPushPackageItemViewModel;
import com.youwu.shouyinxitong.ui.main.DemoViewModel;
import com.youwu.shouyinxitong.utils_tool.MoneyInputFilter;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.view.MyCustKeybords;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 会员充值
 * 2021/11/11
 * 金库
 */

public class VipRechargeActivity extends BaseActivity<ActivityVipRechargeBinding, VipRechargeViewModel> {

    private RechargePageBean listBean;//
    //默认选中第一个
    private int currentType = 0;

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity__vip_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //充值金额监听
        viewModel.MoneyList.observe(this, new Observer<ArrayList<RechargePageBean>>() {
            @Override
            public void onChanged(ArrayList<RechargePageBean> rechargePageBeans) {
                listBean=rechargePageBeans.get(0);
            }
        });

        //点击选择监听
        viewModel.LiveEvent.observe(this, new Observer<RechargePageBean>() {
            @Override
            public void onChanged(RechargePageBean Bean) {
                listBean.setCurrentSelect(0);
                listBean.setCheck(false);
                VipRechargeItemViewModel itemViewModels = new VipRechargeItemViewModel(viewModel, listBean);
                viewModel.VipRechargeList.set(currentType, itemViewModels);
                currentType = Bean.getPosition();
                Bean.setCurrentSelect(1);
                Bean.setCheck(true);
                VipRechargeItemViewModel itemViewModel = new VipRechargeItemViewModel(viewModel, Bean);
                viewModel.VipRechargeList.set(Bean.getPosition(), itemViewModel);

                listBean = Bean;
            }
        });
        viewModel.loadUrlEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1:
                        if (viewModel.custom_type.get()==1){
                            String submitJson = new Gson().toJson(listBean);
                            RxToast.normal("充值提交的json实体数据：\r\n" + submitJson);
                        }else {
                            RxToast.normal("充值金额：\r\n" + binding.customRechargeMoney.getText().toString());
                        }

                        break;
                }
            }
        });

    }
    private String customRechargeMoney = "-1";

    @Override
    public void initData() {
        super.initData();
        viewModel.custom_type.set(1);
        viewModel.getList();



        binding.customKeyboard.bindEditText(binding.customRechargeMoney);
        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if (TextUtils.isEmpty(binding.customRechargeMoney.getText().toString())) {

                    RxToast.normal("请先输入然点击确认！");
                    return;
                }
                customRechargeMoney = binding.customRechargeMoney.getText().toString() ;
                RxToast.normal("已确认自定义充值金额是："+customRechargeMoney+" 元，请现金充值或微信/支付宝支付！");

            }
        });

        MoneyInputFilter filter = new MoneyInputFilter();
        filter.setDecimalLength(2);//保留小数点后2位
        filter.setMaxValue(10000);// 最多可输入1万元
        InputFilter[] filters = {filter};
        binding.customRechargeMoney.setFilters(filters);

    }
}
