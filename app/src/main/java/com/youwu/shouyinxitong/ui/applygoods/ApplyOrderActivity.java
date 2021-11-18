package com.youwu.shouyinxitong.ui.applygoods;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.databinding.ActivityApplyOrderBinding;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 补货申请
 * 2021/11/01
 * 金库
 */

public class ApplyOrderActivity extends BaseActivity<ActivityApplyOrderBinding, ApplyOrderViewModel> {


    //默认选中第一个
    private int currentType = 0;
    public OrderBean orderBean;//


    int pageNo=1;//页数
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public ApplyOrderViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(ApplyOrderViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_apply_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //获取数据的监听
        viewModel.orderBeanEvent.observe(this, new Observer<OrderBean>() {
            @Override
            public void onChanged(OrderBean orderBeans) {
                orderBean=orderBeans;
            }
        });
        //订单点击监听
        viewModel.orderLiveData.observe(this, new Observer<OrderBean>() {
            @Override
            public void onChanged(OrderBean orderBeans) {
                Log.e("走了","11111111");
                orderBean.setSelect(0);
                ApplyOrderItemViewModel itemViewModels = new ApplyOrderItemViewModel(viewModel, orderBean);
                viewModel.ApplyOrdersList.set(currentType,itemViewModels);
                currentType=orderBeans.getPosition();
                viewModel.currentEvent.set(currentType);
                orderBeans.setSelect(1);
                ApplyOrderItemViewModel itemViewModel = new ApplyOrderItemViewModel(viewModel, orderBeans);
                viewModel.ApplyOrdersList.set(orderBeans.getPosition(),itemViewModel);
                orderBean=orderBeans;

                List<CommodityItemViewModel> iss=new ArrayList<>();
                for (int i=0;i<orderBeans.getOrder_details().size();i++){
                    OrderBean.OrderDetailsBean orderDetailsBean=new OrderBean.OrderDetailsBean();
                    orderDetailsBean=orderBeans.getOrder_details().get(i);

                    CommodityItemViewModel itemViewModelss = new CommodityItemViewModel(viewModel, orderDetailsBean);
                    //双向绑定动态添加Item
                    iss.add(itemViewModelss);
                }
                viewModel.commodityList.clear();
                viewModel.commodityList.addAll(iss);
                viewModel.entity.set(orderBeans);
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        viewModel.currentEvent.set(currentType);
          //下拉刷新
        binding.smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.ApplyOrdersList.clear();
                pageNo=1;
                //获取数据
                viewModel.ApplyOrderList(pageNo);
                refreshLayout.finishRefresh(true);
            }
        });
        //上拉加载
        binding.smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                //获取充值记录
                //获取数据
                viewModel.ApplyOrderList(pageNo);

                refreshLayout.finishLoadMore(true);
            }
        });
        //获取数据
        viewModel.ApplyOrderList(pageNo);
    }
}
