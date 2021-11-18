package com.youwu.shouyinxitong.ui.commodity;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 新增商品
 * 2021/10/28
 * 金库
 */

public class AddCommodityViewModel extends BaseViewModel {
    public CommodityDetailBean entity;
    public String Textnull="";
    //使用LiveData
    public SingleLiveEvent<Integer> IntegerNum = new SingleLiveEvent<>();
    public ObservableField<String> commodityImage = new ObservableField<>();

    public SingleLiveEvent<CommodityDetailBean> CommodityDetailLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Test = new SingleLiveEvent<>();

    public int type;
    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;

    public boolean OFF=false;
    public boolean ON=true;


    public AddCommodityViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCommodityDetailBean(CommodityDetailBean entity) {
        if (this.entity == null) {
            this.entity = entity;
        }
    }

    public void setType(int type) {

            this.type = type;

    }

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
    //提交点击事件
    public BindingCommand SubmitonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            CommodityDetailLiveData.setValue(entity);

//            String submitJson = new Gson().toJson(entity);
//            Test.setValue(submitJson);
        }
    });

    //选择图片点击事件
    public BindingCommand imageOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (commodityImage.get()!=null){
                Log.e("图片地址",commodityImage.get());
            }

            IntegerNum.setValue(1);
        }
    });


}
