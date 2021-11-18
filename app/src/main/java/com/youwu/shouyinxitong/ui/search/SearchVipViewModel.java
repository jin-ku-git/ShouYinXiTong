package com.youwu.shouyinxitong.ui.search;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.commodity.EditCommodityItemViewModel;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersListItemViewModel;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersViewModel;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/10/28
 * 金库
 */

public class SearchVipViewModel extends BaseViewModel<DemoRepository> {

    //使用LiveData
    public SingleLiveEvent<String> EditTextEvent = new SingleLiveEvent<>();
    public ObservableField<String> editText = new ObservableField<>("");


    public SearchVipViewModel(@NonNull Application application,DemoRepository model) {
        super(application,model);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<SearchVipItemViewModel> searchVipleList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<SearchVipItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_search_vip);

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
    //清空点击事件
    public BindingCommand emptyonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            editText.set("");
        }
    });



    /**
     * 获取用户列表
     */
    public void getUserList(String phone){
        for(int i=0;i<10;i++){

            UserBean userBean=new UserBean();
            userBean.setUser_name("张三"+i);
            userBean.setUser_phone("1346821414"+i);
            userBean.setUser_money("99"+i);
            userBean.setRemarks("ceshi1"+i);
            userBean.setUser_create_time("2021-10-04 10:22:0"+i);

            SearchVipItemViewModel itemViewModel = new SearchVipItemViewModel(SearchVipViewModel.this, userBean);
            //双向绑定动态添加Item
            searchVipleList.add(itemViewModel);
        }


    }


    /**
     * 获取条目下标
     *
     * @param searchVipItemViewModel
     * @return
     */
    public int getItemPosition(SearchVipItemViewModel searchVipItemViewModel) {
        return searchVipleList.indexOf(searchVipItemViewModel);
    }

}
