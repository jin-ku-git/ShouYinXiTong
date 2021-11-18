package com.youwu.shouyinxitong.ui.vip;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.Gson;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargePageBean;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.coupon.CouponPushItemViewModel;
import com.youwu.shouyinxitong.ui.coupon.CouponPushPackageItemViewModel;
import com.youwu.shouyinxitong.ui.coupon.CouponPushPackageViewModel;
import com.youwu.shouyinxitong.ui.form.FormFragment;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 会员充值Model
 * 2021/11/11
 * 金库
 */

public class VipRechargeViewModel extends BaseViewModel {

    public ObservableField<Integer> custom_type = new ObservableField<>();
    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();
    List listData=new ArrayList();//优惠券包数据
    //充值金额观察者
    public SingleLiveEvent<ArrayList<RechargePageBean>> MoneyList = new SingleLiveEvent<>();
    //点击选择观察者
    public SingleLiveEvent<RechargePageBean> LiveEvent = new SingleLiveEvent<>();

    public VipRechargeViewModel(@NonNull Application application) {
        super(application);
    }


    //给RecyclerView添加ObservableList
    public ObservableList<VipRechargeItemViewModel> VipRechargeList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<VipRechargeItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.recharge_page_list_item);

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
    //现金充值点击事件
    public BindingCommand CashonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(1);
        }
    });

    //自定义点击事件
    public BindingCommand CustomOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if (custom_type.get()==1){
                custom_type.set(2);
            }else {
                custom_type.set(1);
            }
        }
    });


    public void getList(){
        for(int i=0;i<10;i++){

            RechargePageBean listBean=new RechargePageBean();
            listBean.setTotal_money(100.0+i);
            listBean.setStart_time("2021-05-29 00:00:00");
            listBean.setEnd_time("2021-11-29 00:00:00");

            VipRechargeItemViewModel itemViewModel = new VipRechargeItemViewModel(VipRechargeViewModel.this, listBean);
            //双向绑定动态添加Item
            VipRechargeList.add(itemViewModel);
            listData.add(listBean);
        }
        ArrayList<RechargePageBean> categoryBeans = (ArrayList<RechargePageBean>) listData;
        categoryBeans.get(0).setCurrentSelect(1);
        categoryBeans.get(0).setCheck(true);
        MoneyList.setValue(categoryBeans);
    }


    /**
     * 获取条目下标
     *
     * @param vipRechargeItemViewModel
     * @return
     */
    public int getItemPosition(VipRechargeItemViewModel vipRechargeItemViewModel) {
        return VipRechargeList.indexOf(vipRechargeItemViewModel);
    }

}
