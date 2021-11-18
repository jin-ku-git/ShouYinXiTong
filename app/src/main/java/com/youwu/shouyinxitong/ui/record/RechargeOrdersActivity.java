package com.youwu.shouyinxitong.ui.record;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinxitong.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.databinding.ActivityRechargeOrdersBinding;
import com.youwu.shouyinxitong.presenter.bean.SaleCountBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 充值订单
 * 2021/11/01
 * 金库
 */

public class RechargeOrdersActivity extends BaseActivity<ActivityRechargeOrdersBinding, RechargeOrdersViewModel> {

    int pageNo=1;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public RechargeOrdersViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(RechargeOrdersViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_recharge_orders;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //注册监听时间的请求
        viewModel.loadUrlEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                    switch (s){
                        case 1://开始时间
                            showDatePickerDialog(binding.tvHandoverStartTime);
                            break;
                        case 2://结束时间
                            showDatePickerDialog(binding.tvHandoverEndTime);
                            break;

                    }
            }
        });


    }




    @Override
    public void initData() {
        super.initData();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = calendar.getTime();//昨天的日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        viewModel.start_time.set(format.format(startTime));
        viewModel.end_time.set(format.format(new Date()));

        //下拉刷新
      binding.smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
          @Override
          public void onRefresh(@NonNull RefreshLayout refreshLayout) {
              viewModel.rechargeOrdersList.clear();
              pageNo=1;
              //获取充值记录
              getData();
              refreshLayout.finishRefresh(true);
          }
      });
      //上拉加载
        binding.smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                //获取充值记录
                getData();
                refreshLayout.finishLoadMore(true);
            }
        });
        getData();
    }


    public void getData() {
        //获取充值记录
        viewModel.getRechargeOrdersList(pageNo,getTimestamp(viewModel.start_time.get()) + "", getTimestamp(viewModel.end_time.get()) + "");

    }


}
