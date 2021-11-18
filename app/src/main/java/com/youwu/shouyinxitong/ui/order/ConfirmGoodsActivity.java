package com.youwu.shouyinxitong.ui.order;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_new.CommodityListBean;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.databinding.ActivityConfirmGoodsBinding;
import com.youwu.shouyinxitong.databinding.ActivityDemoBinding;
import com.youwu.shouyinxitong.ui.main.DemoViewModel;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 确认订货
 * 2021/11/05
 * 金库
 */

public class ConfirmGoodsActivity extends BaseActivity<ActivityConfirmGoodsBinding, ConfirmGoodsViewModel> {
    //申请订货传值
    CommodityListBean commodityListBean=new CommodityListBean();
    //再次提交
    OrderBean orderBean=new OrderBean();

    int allNum=0;//订货总数量
    double total_money=0.0;//订货总金额
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent =getIntent();
        commodityListBean= (CommodityListBean) intent.getSerializableExtra("commodityListBean");
        orderBean= (OrderBean) intent.getSerializableExtra("CopyOrder");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_confirm_goods;
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

        //如果是申请订货订单
        if (commodityListBean!=null){
            for (int i=0;i<commodityListBean.getItem().size();i++) {

                CommodityDetailBean commodityDetailBean =new CommodityDetailBean();

                commodityDetailBean=commodityListBean.getItem().get(i);

                ConfirmGoodsItemViewModel itemViewModel = new ConfirmGoodsItemViewModel(viewModel, commodityDetailBean);
                //双向绑定动态添加Item
                viewModel.observableList.add(itemViewModel);

            }
        }
        //如果是再次订单
        if (orderBean!=null){
            for (int i=0;i<orderBean.getOrder_details().size();i++) {

                CommodityDetailBean commodityDetailBean =new CommodityDetailBean();

                commodityDetailBean.setCommodity_name(orderBean.getOrder_details().get(i).getCommodity_name());//名称
                commodityDetailBean.setCommodity_num(orderBean.getOrder_details().get(i).getCommodity_number());//数量
                commodityDetailBean.setCommodity_purchase_price_subtotal(orderBean.getOrder_details().get(i).getCommodity_total_price());//
                commodityDetailBean.setCommodity_purchase_price(orderBean.getOrder_details().get(i).getCommodity_primary_price());
                ConfirmGoodsItemViewModel itemViewModel = new ConfirmGoodsItemViewModel(viewModel, commodityDetailBean);
                //双向绑定动态添加Item
                viewModel.observableList.add(itemViewModel);

            }
        }

        for (int i=0;i<viewModel.observableList.size();i++){
            allNum+=Integer.parseInt(viewModel.observableList.get(i).entity.get().getCommodity_num());
            total_money+=Double.parseDouble(viewModel.observableList.get(i).entity.get().getCommodity_purchase_price_subtotal());
        }
        viewModel.allNum.set(allNum);
        viewModel.total_money.set(total_money);
    }
}
