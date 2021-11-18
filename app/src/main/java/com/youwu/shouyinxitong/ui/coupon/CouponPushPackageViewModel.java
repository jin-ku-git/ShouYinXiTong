package com.youwu.shouyinxitong.ui.coupon;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersListItemViewModel;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersViewModel;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 推送优惠券包
 * 2021/11/10
 */

public class CouponPushPackageViewModel extends BaseViewModel {

    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();

    List listData=new ArrayList();//优惠券包数据
    //优惠券包观察者
    public SingleLiveEvent<ArrayList<RechargeRecordBean.ListBean>> goodsList = new SingleLiveEvent<>();
    //点击选择观察者
    public SingleLiveEvent<RechargeRecordBean.ListBean> LiveEvent = new SingleLiveEvent<>();
    public CouponPushPackageViewModel(@NonNull Application application) {
        super(application);
    }


    //给RecyclerView添加ObservableList
    public ObservableList<CouponPushPackageItemViewModel> CouponPushList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CouponPushPackageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_coupon_page_list);
    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public void getList(){
        for(int i=0;i<10;i++){

            RechargeRecordBean.ListBean listBean=new RechargeRecordBean.ListBean();
            listBean.setUser_name("张三");

            CouponPushPackageItemViewModel itemViewModel = new CouponPushPackageItemViewModel(CouponPushPackageViewModel.this, listBean);
            //双向绑定动态添加Item
            CouponPushList.add(itemViewModel);
            listData.add(listBean);
        }
        ArrayList<RechargeRecordBean.ListBean> categoryBeans = (ArrayList<RechargeRecordBean.ListBean>) listData;
        categoryBeans.get(0).currentSelect=1;
        goodsList.setValue(categoryBeans);
    }

    /**
     * 获取条目下标
     *
     * @param couponPushPackageItemViewModel
     * @return
     */
    public int getItemPosition(CouponPushPackageItemViewModel couponPushPackageItemViewModel) {
        return CouponPushList.indexOf(couponPushPackageItemViewModel);
    }
}
