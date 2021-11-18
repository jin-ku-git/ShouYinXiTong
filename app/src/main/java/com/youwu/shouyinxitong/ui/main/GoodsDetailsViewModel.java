package com.youwu.shouyinxitong.ui.main;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by goldze on 2017/7/17.
 */

public class GoodsDetailsViewModel extends BaseViewModel {

    public YWGoodBean entity;
    public MealsItemBean mealsItemBean;
    public int  type;//0 YWGoodBean / 1 MealsItemBean
    public String xiugai="修改";
    public String jiaru="加入";

    private String yuanjia;


    //使用LiveData
    public SingleLiveEvent<Integer> loadUrlEvent = new SingleLiveEvent<>();

    public GoodsDetailsViewModel(@NonNull Application application) {
        super(application);
    }



    public void setYWGoodBean(YWGoodBean entity) {
        if (this.entity == null) {
            Double yuanjia = BigDecimalUtils.multiply(entity.getPrice(), entity.getDiscount());
            this.yuanjia=BigDecimalUtils.formatRoundUp(BigDecimalUtils.divide(yuanjia, 100), 2) + "";
            entity.setTotal_money(this.yuanjia);
            this.entity = entity;
        }
    }
    public void setMealsItemBean(MealsItemBean mealsItemBean) {
        if (this.mealsItemBean == null) {
            this.mealsItemBean = mealsItemBean;
        }
    }

    public void setYType(int type) {
            this.type = type;
    }

    //返回点击事件
    public BindingCommand finishClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(1);//返回

        }
    });
    //单品折扣点击事件
    public BindingCommand discountClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadUrlEvent.setValue(2);//

        }
    });

    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
        }
    });

    //表单修改点击事件
    public BindingCommand formModifyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //模拟一个修改的实体数据
            FormEntity entity = new FormEntity();
            entity.setId("12345678");
            entity.setName("goldze");
            entity.setSex("1");
            entity.setBir("xxxx年xx月xx日");
            entity.setMarry(true);
            //传入实体数据
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", entity);
            startContainerActivity(FormFragment.class.getCanonicalName(), mBundle);
        }
    });

}
