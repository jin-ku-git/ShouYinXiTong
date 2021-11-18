package com.youwu.shouyinxitong.ui.order;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.databinding.ActivitySellOrderListBinding;
import com.youwu.shouyinxitong.utils_tool.UrlUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 销售单据
 * 2021/10/29
 * 金库
 */

public class SellOrderListActivity extends BaseActivity<ActivitySellOrderListBinding, SellOrderListViewModel> {

    private String startTime;//开始时间
    private String endTime;//结束时间

    private String str2;
    private String str1;
    private int pos1 = 0;
    private int pos2 = 0;
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
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sell_order_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //订单数据监听
        viewModel.orderBean2Event.observe(this, new Observer<OrderBean>() {
            @Override
            public void onChanged(OrderBean orderBeans) {
                orderBean=orderBeans;
            }
        });
        //订单点击事件监听
        viewModel.orderBeanEvent.observe(this, new Observer<OrderBean>() {
            @Override
            public void onChanged(OrderBean orderBeans) {
                showDialog("加载中");
                orderBean.setSelect(0);
                SellOrderListItemViewModel itemViewModels = new SellOrderListItemViewModel(viewModel, orderBean);
                viewModel.observableList.set(currentType,itemViewModels);
                currentType=orderBeans.getPosition();
                orderBeans.setSelect(1);
                SellOrderListItemViewModel itemViewModel = new SellOrderListItemViewModel(viewModel, orderBeans);
                viewModel.observableList.set(orderBeans.getPosition(),itemViewModel);
                orderBean=orderBeans;

                List<RestingOrderListItemViewModel> data = new ArrayList<>();
                for (int i=0;i<orderBeans.getOrder_details().size();i++){
                    OrderBean.OrderDetailsBean orderDetailsBean=new OrderBean.OrderDetailsBean();
                    orderDetailsBean=orderBeans.getOrder_details().get(i);
                    RestingOrderListItemViewModel itemViewModelss = new RestingOrderListItemViewModel(viewModel, orderDetailsBean);
                    data.add(itemViewModelss);
                    //双向绑定动态添加Item
//            viewModel.goodsList.add(itemViewModelss);

                }
                viewModel.goodsList.clear();
                viewModel.goodsList.addAll(data);
                viewModel.entity.set(orderBeans);

                dismissDialog();

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initTime();

        //下拉刷新
        binding.smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.observableList.clear();
                pageNo=1;
                //获取订单列表
                viewModel.getOrderList(pageNo);
                refreshLayout.finishRefresh(true);
            }
        });
        //上拉加载
        binding.smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                //获取订单列表
                viewModel.getOrderList(pageNo);

                refreshLayout.finishLoadMore(true);
            }
        });
        //获取订单列表
        viewModel.getOrderList(pageNo);

    }


    private void initTime() {
        //获取系统的日期
        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        startTime = year + "-" + month + "-" + (day - 1) + " 00:00:00";
        endTime = year + "-" + month + "-" + day + " 23:59:59";
        try {
            startTime = dateToStamp(startTime);
            endTime = dateToStamp(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = System.currentTimeMillis() / 1000;
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list1.add(UrlUtils.timeStampDate(time - (i * 86400) + ""));
        }
        final String[] spinner1 = new String[list1.size()];
        list1.toArray(spinner1);
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this,
                R.layout.item_select, spinner1);
        binding.sp1.setAdapter(spinnerAdapter1);
        binding.sp1.setSelection(1);
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list2.add(UrlUtils.timeStampDate(time - (i * 86400) + ""));
        }
        final String[] spinner2 = new String[list2.size()];
        list2.toArray(spinner2);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this,
                R.layout.item_select, spinner2);
        binding.sp2.setAdapter(spinnerAdapter2);
        binding.sp2.setSelection(0);


        binding.sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str1 = UrlUtils.dateToStamp(spinner1[position]);
                pos1 = position;
                if (str2 != null && str1 != null) {
//                    if (Long.parseLong(str1) > Long.parseLong(str2)) {
//                        sp1.setSelection(pos2 + 1);
//                    }
                    startTime = spinner1[pos1] + " 00:00:00";
                    endTime = spinner2[pos2] + " 23:59:59";
                    try {
                        startTime = dateToStamp(spinner1[pos1] + " 00:00:00");
                        endTime = dateToStamp(spinner2[pos2] + " 23:59:59");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    presenter.getOrderList(SellOrderListActivity.this, order_sn, page + "", size + "", startTime, endTime);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str2 = UrlUtils.dateToStamp(spinner1[position]);
                pos2 = position;
                if (str2 != null && str1 != null) {
//                    if (Long.parseLong(str1) > Long.parseLong(str2)) {
//                        sp2.setSelection(pos2 + 1);
                    startTime = spinner1[pos1] + " 00:00:00";
                    endTime = spinner2[pos2] + " 23:59:59";
                    try {
                        startTime = dateToStamp(spinner1[pos1] + " 00:00:00");
                        endTime = dateToStamp(spinner2[pos2] + " 23:59:59");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    presenter.getOrderList(SellOrderListActivity.this, order_sn, page + "", size + "", startTime, endTime);
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }






}
