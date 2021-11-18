package com.youwu.shouyinxitong.ui.cashier;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.databinding.ActivityCashierBinding;
import com.youwu.shouyinxitong.databinding.ActivityDemoBinding;
import com.youwu.shouyinxitong.ui.main.DemoViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 收银页
 * 2021/11/08
 * 金库
 */

public class CashierActivity extends BaseActivity<ActivityCashierBinding, CashierViewModel> {


    private boolean isCombination = false;//是否点击了组合支付
    private List<Integer> payList = new ArrayList<>();

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_cashier;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //注册监听相机权限的请求
        viewModel.requestCameraPermissions.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

            }
        });

    }

    @Override
    public void initData() {
        super.initData();

    }

}
