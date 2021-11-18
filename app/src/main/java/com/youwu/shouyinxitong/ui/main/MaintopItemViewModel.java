package com.youwu.shouyinxitong.ui.main;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/11/15
 * 金库
 */

public class MaintopItemViewModel extends MultiItemViewModel<MainViewModel> {
    public ObservableField<GoodsTypesBean> goodsTypes = new ObservableField<>();

    public MaintopItemViewModel(@NonNull MainViewModel viewModel) {
        super(viewModel);

    }

    //给RecyclerView添加ObservableList
    public ObservableList<MainItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_goods_type);

    //给RecyclerView添加ObservableList
    public ObservableList<MainGoodItemViewModel> goodsList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainGoodItemViewModel> itemBindingShopping = ItemBinding.of(BR.viewModel, R.layout.adapter_good);

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //拿到position
            int position = viewModel.observableList.indexOf(MaintopItemViewModel.this);
            ToastUtils.showShort("position：" + position);
        }
    });
}
