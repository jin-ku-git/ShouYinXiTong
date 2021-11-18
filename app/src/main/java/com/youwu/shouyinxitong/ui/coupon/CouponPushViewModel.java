package com.youwu.shouyinxitong.ui.coupon;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 多选推送优惠券
 * 2021/11/10
 */

public class CouponPushViewModel extends BaseViewModel {

    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();

    List listData=new ArrayList();//优惠券包数据

    //点击选择观察者
    public SingleLiveEvent<RechargeRecordBean.ListBean> LiveEvent = new SingleLiveEvent<>();
    public CouponPushViewModel(@NonNull Application application) {
        super(application);
    }


    //给RecyclerView添加ObservableList
    public ObservableList<CouponPushItemViewModel> CouponPushList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CouponPushItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_coupon_push_list);
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

            CouponPushItemViewModel itemViewModel = new CouponPushItemViewModel(CouponPushViewModel.this, listBean);
            //双向绑定动态添加Item
            CouponPushList.add(itemViewModel);
            listData.add(listBean);
        }
        ArrayList<RechargeRecordBean.ListBean> categoryBeans = (ArrayList<RechargeRecordBean.ListBean>) listData;
        categoryBeans.get(0).currentSelect=1;

    }

    /**
     * 获取条目下标
     *
     * @param couponPushItemViewModel
     * @return
     */
    public int getItemPosition(CouponPushItemViewModel couponPushItemViewModel) {
        return CouponPushList.indexOf(couponPushItemViewModel);
    }
}
