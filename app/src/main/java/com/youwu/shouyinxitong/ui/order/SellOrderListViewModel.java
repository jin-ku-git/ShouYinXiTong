package com.youwu.shouyinxitong.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.data.DemoRepository;

import java.util.ArrayList;
import java.util.List;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/10/28
 * 金库
 */

public class SellOrderListViewModel extends BaseViewModel<DemoRepository> {
    public ObservableField<OrderBean> entity = new ObservableField<>();
    //使用LiveData
    public SingleLiveEvent<String> EditTextEvent = new SingleLiveEvent<>();
    public ObservableField<String> editText = new ObservableField<>("");

    public SingleLiveEvent<OrderBean> orderBean2Event = new SingleLiveEvent<>();
    public SingleLiveEvent<OrderBean> orderBeanEvent = new SingleLiveEvent<>();

    public SellOrderListViewModel(@NonNull Application application) {
        super(application);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<SellOrderListItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<SellOrderListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_order_item);

    //给RecyclerView添加ObservableList
    public ObservableList<RestingOrderListItemViewModel> goodsList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<RestingOrderListItemViewModel> itemBindingOrdering = ItemBinding.of(BR.viewModel, R.layout.adapter_order_list_item);

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    //订单
    public void getOrderList(int pageNo) {
        for (int i=0;i<10;i++) {
            OrderBean orderBean=new OrderBean();
            orderBean.setOrder_price("90"+i);
            orderBean.setOrder_start_time("2021-10-29 11:18:0"+i);
            orderBean.setOrder_id("12324325235234"+i);
            orderBean.setOrder_discount_price("30"+i);
            orderBean.setOrder_type("微信");
            orderBean.setUser_name("金库"+i);
            orderBean.setOrder_numbers("1"+i);
            orderBean.setOrder_commodity_price("99");
            orderBean.setOrder_receivable_price("100"+i);
            orderBean.setOrder_remarks("味道特别好，而且也不贵");
            if (i==0&&pageNo==1){
                orderBean.setSelect(1);
                orderBean.setOrder_status(3);
                orderBean2Event.setValue(orderBean);
                entity.set(orderBean);
            }else if (i==2){
                orderBean.setPay_type(6);
                orderBean.setSelect(0);
            }else {
                orderBean.setSelect(0);
            }
            List<OrderBean.OrderDetailsBean> list=new ArrayList<>();
            for (int j=0;j<5;j++){
                OrderBean.OrderDetailsBean orderDetailsBean=new OrderBean.OrderDetailsBean();
                orderDetailsBean.setCommodity_name("测试");
                orderDetailsBean.setCommodity_number(i+""+j);
                orderDetailsBean.setCommodity_primary_price(i+""+j);
                orderDetailsBean.setCommodity_discount_price(i+""+j);
                orderDetailsBean.setCommodity_total_price(i+""+j);
                list.add(orderDetailsBean);
                if (i==0&&pageNo==1){
                    RestingOrderListItemViewModel itemViewModel = new RestingOrderListItemViewModel(SellOrderListViewModel.this, orderDetailsBean);
                    //双向绑定动态添加Item
                    goodsList.add(itemViewModel);
                }

            }
            orderBean.setOrder_details(list);


            SellOrderListItemViewModel itemViewModel = new SellOrderListItemViewModel(SellOrderListViewModel.this, orderBean);
            //双向绑定动态添加Item
            observableList.add(itemViewModel);
        }

    }


    /**
     * 获取条目下标
     *
     * @param sellOrderListItemViewModel
     * @return
     */
    public int getItemPosition(SellOrderListItemViewModel sellOrderListItemViewModel) {
        return observableList.indexOf(sellOrderListItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param restingOrderListItemViewModel
     * @return
     */
    public int getItemPosition(RestingOrderListItemViewModel restingOrderListItemViewModel) {
        return observableList.indexOf(restingOrderListItemViewModel);
    }

}
