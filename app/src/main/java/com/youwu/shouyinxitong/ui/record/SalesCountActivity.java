package com.youwu.shouyinxitong.ui.record;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.databinding.ActivitySalesCountBinding;
import com.youwu.shouyinxitong.presenter.bean.SaleCountBean;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

import static com.youwu.shouyinxitong.ui.main.MainActivity.ISCONNECT;
import static com.youwu.shouyinxitong.ui.main.MainActivity.printerPresenter;

/**
 * 销售统计
 * 2021/11/01
 * 金库
 */

public class SalesCountActivity extends BaseActivity<ActivitySalesCountBinding, SalesCountViewModel> {

    private SaleCountBean salesCountBean;//数据
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public SalesCountViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SalesCountViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sales_count;
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
                            showDatePickerDialog(binding.tvStartTime);
                            break;
                        case 2://结束时间
                            showDatePickerDialog(binding.tvEndTime);
                            break;
                        case 3://打印小票
                            //打印代码
                            if (salesCountBean != null) {
                                salesCountBean.setPrint_time(getTime()+"");
                                int printType = AppApplication.spUtils.getInt("printType", 0);
                                if (printType == 1 && ISCONNECT ) {
                                    printerPresenter.saleCountPrint(salesCountBean);
                                }  else {
                                    ToastUtil.showShortToast("请在打印设置中使用芯烨usb打印机！");
                                }

                            } else {
                                ToastUtil.showShortToast("请先获取数据！");
                            }
                            break;
                    }
            }
        });

        viewModel.dataEvent.observe(this, new Observer<SaleCountBean>() {
            @Override
            public void onChanged(SaleCountBean salesCountBeans) {
                salesCountBean=salesCountBeans;
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
        getData();

    }

    public void getData() {
        viewModel.getData(getTimestamp(viewModel.start_time.get()) + "", getTimestamp(viewModel.end_time.get()) + "");
    }

    /**
     * 日期选择
     */
    public void showDatePickerDialog(TextView tv) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showTimePickerDialog(tv, year + "." + (monthOfYear + 1) + "." + dayOfMonth);
            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    /**
     * 时间选择
     *
     * @param tv
     * @param date
     */
    public void showTimePickerDialog(TextView tv, String date) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, 0,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText(date + " " + hourOfDay + ":" + minute);
                    }
                }
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true).show();
    }

    public Long getTimestamp(String time) {
        Long timestamp = null;
        try {
            timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp / 1000;
    }

    private String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }
}
