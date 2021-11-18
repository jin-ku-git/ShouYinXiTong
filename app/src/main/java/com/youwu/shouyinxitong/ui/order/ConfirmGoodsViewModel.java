package com.youwu.shouyinxitong.ui.order;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.entity.FormEntity;
import com.youwu.shouyinxitong.ui.form.FormFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 确认收货
 * 2021/11/05
 * 金库
 */

public class ConfirmGoodsViewModel extends BaseViewModel {
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();

    public ObservableField<Integer> allNum = new ObservableField<>();
    public ObservableField<Double> total_money = new ObservableField<>();

    public ConfirmGoodsViewModel(@NonNull Application application) {
        super(application);
    }


    //给RecyclerView添加ObservableList
    public ObservableList<ConfirmGoodsItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<ConfirmGoodsItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_confirm_goods);


    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    /**
     * 获取条目下标
     *
     * @param confirmGoodsItemViewModel
     * @return
     */
    public int getItemPosition(ConfirmGoodsItemViewModel confirmGoodsItemViewModel) {
        return observableList.indexOf(confirmGoodsItemViewModel);
    }
}
