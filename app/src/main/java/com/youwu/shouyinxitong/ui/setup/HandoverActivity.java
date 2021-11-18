package com.youwu.shouyinxitong.ui.setup;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.databinding.ActivityHandoverBinding;
import com.youwu.shouyinxitong.databinding.ActivitySearchVipBinding;
import com.youwu.shouyinxitong.ui.search.SearchVipViewModel;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.view.MyCustKeybords;
import com.youwu.shouyinxitong.widget.loaddialog.LoadingDialog;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 交接班
 * 2021/10/28
 * 金库
 */

public class HandoverActivity extends BaseActivity<ActivityHandoverBinding, HandoverViewModel> {

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_handover;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.EditTextEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();

    }

}
