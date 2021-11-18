package com.youwu.shouyinxitong.ui.vip;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean_new.CommodityListBean;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.databinding.ActivityAddVipBinding;
import com.youwu.shouyinxitong.databinding.ActivityHandoverBinding;
import com.youwu.shouyinxitong.ui.setup.HandoverViewModel;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

/**
 * 会员新增
 * 2021/10/28
 * 金库
 */

public class AddVIPActivity extends BaseActivity<ActivityAddVipBinding, AddVIPViewModel> {

    private int type;// 0 新增 1选择会员 2取消选择
    UserBean userBean=new UserBean();
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        Intent intent=getIntent();

        type = intent.getIntExtra("type",0);
        userBean= (UserBean) intent.getSerializableExtra("user");

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_add_vip;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.addVIP.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String submitJson) {
                RxToast.normal("提交的json实体数据：\r\n" + submitJson);
                if (type==1){
                    finish();
                    userBean.setUser_type(type);
                    EventBus.getDefault().post(userBean);
                }else if (type==2){
                    finish();
                    userBean.setUser_type(type);
                    EventBus.getDefault().post(userBean);
                }

            }
        });
    }

    @Override
        public void initData() {
        super.initData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        viewModel.setType(type);
        if (userBean!=null){
            viewModel.userBean.set(userBean);
        }else {
            UserBean userBean=new UserBean();
            userBean.setUser_create_time(simpleDateFormat.format(date));
            viewModel.userBean.set(userBean);
        }


    }

}
