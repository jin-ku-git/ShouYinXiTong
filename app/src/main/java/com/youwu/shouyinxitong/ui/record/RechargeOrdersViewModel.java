package com.youwu.shouyinxitong.ui.record;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.data.DemoRepository;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/11/01
 * 金库
 */

public class RechargeOrdersViewModel extends BaseViewModel<DemoRepository> {
    public ObservableField<RechargeRecordBean.ListBean> entity = new ObservableField<>();

    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;
    public int visibility;
    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();

    public ObservableField<String> start_time = new ObservableField<>();
    public ObservableField<String> end_time = new ObservableField<>();


    public RechargeOrdersViewModel(@NonNull Application application, DemoRepository model) {
        super(application,model);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<RechargeOrdersListItemViewModel> rechargeOrdersList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<RechargeOrdersListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_recharge_order);

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
    //开始时间点击事件
    public BindingCommand StartTimeonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(1);
        }
    });
    //结束时间点击事件
    public BindingCommand EndTimeonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(2);
        }
    });

    /**
     * 获取充值订单列表
     */
    public void getRechargeOrdersList(int pageNo,String startTime,String endTime){
        for(int i=0;i<10;i++){

            RechargeRecordBean.ListBean listBean=new RechargeRecordBean.ListBean();
            listBean.setUser_name("张三");

            RechargeOrdersListItemViewModel itemViewModel = new RechargeOrdersListItemViewModel(RechargeOrdersViewModel.this, listBean);
            //双向绑定动态添加Item
            rechargeOrdersList.add(itemViewModel);
        }


    }



    /**
     * 获取条目下标
     *
     * @param rechargeOrdersListItemViewModel
     * @return
     */
    public int getItemPosition(RechargeOrdersListItemViewModel rechargeOrdersListItemViewModel) {
        return rechargeOrdersList.indexOf(rechargeOrdersListItemViewModel);
    }

}
