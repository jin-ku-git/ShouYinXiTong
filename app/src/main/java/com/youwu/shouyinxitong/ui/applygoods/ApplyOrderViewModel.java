package com.youwu.shouyinxitong.ui.applygoods;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.BleBean;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.ui.order.ConfirmGoodsActivity;
import com.youwu.shouyinxitong.ui.order.OrderNewlyBuildActivity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/11/01
 * 金库
 */

public class ApplyOrderViewModel extends BaseViewModel<DemoRepository> {

    public ObservableField<OrderBean> entity = new ObservableField<>();
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<OrderBean> orderLiveData = new SingleLiveEvent<>();

    public ObservableField<Integer> currentEvent = new ObservableField<>();

    public ApplyOrderViewModel(@NonNull Application application,DemoRepository model) {
        super(application,model);
    }

    public SingleLiveEvent<OrderBean> orderBeanEvent = new SingleLiveEvent<>();

    //给RecyclerView添加ObservableList
    public ObservableList<ApplyOrderItemViewModel> ApplyOrdersList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<ApplyOrderItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_apply_order);
    //给RecyclerView添加ObservableList
    public ObservableList<CommodityItemViewModel> commodityList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CommodityItemViewModel> CommodityitemBinding = ItemBinding.of(BR.viewModel, R.layout.item_apply_order_commodity);



    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //新建订单点击事件
    public BindingCommand orderonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(OrderNewlyBuildActivity.class);
        }
    });
    //复用订单点击事件
    public BindingCommand CopyConfirmOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            Bundle bundle=new Bundle();

            bundle.putSerializable("entity", ApplyOrdersList.get(currentEvent.get()).entity.get());
            startActivity(OrderNewlyBuildActivity.class,bundle);
        }
    });
    //复用订单点击事件
    public BindingCommand CopyOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            Bundle bundle=new Bundle();

            bundle.putSerializable("CopyOrder", ApplyOrdersList.get(currentEvent.get()).entity.get());
            startActivity(ConfirmGoodsActivity.class,bundle);
        }
    });

    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
        }
    });

    public void ApplyOrderList(int pageNo){
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
            orderBean.setOrder_status_name("已取消");
            if (i==0&&pageNo==1){//第一次选择第一个

                    orderBean.setSelect(1);
                    orderBean.setOrder_status(3);
                    orderBeanEvent.setValue(orderBean);
                    entity.set(orderBean);

            }else if (i==2){
                orderBean.setPay_type(6);
                orderBean.setSelect(0);
            }else {
                orderBean.setSelect(0);
            }
            List<OrderBean.OrderDetailsBean> list=new ArrayList<>();

            for (int j=0;j<i+1;j++){
                OrderBean.OrderDetailsBean orderDetailsBean=new OrderBean.OrderDetailsBean();
                orderDetailsBean.setCommodity_name("测试");
                orderDetailsBean.setCommodity_number(i+""+j);
                orderDetailsBean.setCommodity_primary_price(i+""+j);
                orderDetailsBean.setCommodity_discount_price(i+""+j);
                orderDetailsBean.setCommodity_total_price((Double.parseDouble(orderDetailsBean.getCommodity_number())*(Double.parseDouble(orderDetailsBean.getCommodity_primary_price())))+"");
                list.add(orderDetailsBean);
                if (i==0&&pageNo==1){
                    CommodityItemViewModel itemViewModel = new CommodityItemViewModel(ApplyOrderViewModel.this, orderDetailsBean);
                    //双向绑定动态添加Item
                    commodityList.add(itemViewModel);
                }

            }
            orderBean.setOrder_details(list);


            ApplyOrderItemViewModel itemViewModel = new ApplyOrderItemViewModel(ApplyOrderViewModel.this, orderBean);
            //双向绑定动态添加Item
            ApplyOrdersList.add(itemViewModel);
        }
    }

    /**
     * 获取条目下标
     *
     * @param applyOrderItemViewModel
     * @return
     */
    public int getItemPosition(ApplyOrderItemViewModel applyOrderItemViewModel) {
        return ApplyOrdersList.indexOf(applyOrderItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param commodityItemViewModel
     * @return
     */
    public int getItemPosition(CommodityItemViewModel commodityItemViewModel) {
        return commodityList.indexOf(commodityItemViewModel);
    }

}
