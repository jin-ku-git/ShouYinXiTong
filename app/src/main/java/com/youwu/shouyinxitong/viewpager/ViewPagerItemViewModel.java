package com.youwu.shouyinxitong.viewpager;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.ui.main.MainGoodItemViewModel;
import com.youwu.shouyinxitong.ui.main.MainItemViewModel;
import com.youwu.shouyinxitong.ui.main.MainViewModel;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.KLog;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 所有例子仅做参考,千万不要把它当成一种标准,毕竟主打的不是例子,业务场景繁多,理解如何使用才最重要。
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel<MainViewModel> {

    public ObservableField<String> EditBean = new ObservableField<>();

    public SingleLiveEvent<String> stringEvent = new SingleLiveEvent<>();

    public ViewPagerItemViewModel(@NonNull MainViewModel viewModel, ObservableList<MainItemViewModel> observableList,ObservableList<MainGoodItemViewModel> goodsList,ObservableList<MainGoodItemViewModel> goodsLists,int i) {
        super(viewModel);
        if (i==1){
            this.observableList = observableList;
            this.goodsList = goodsList;
        }else {
            this.goodsLists = goodsLists;
        }


    }

    /**
     * 分类
     */
    //给RecyclerView添加ObservableList
    public ObservableList<MainItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_goods_type);
    /**
     * 商品
     */
    //给RecyclerView添加ObservableList
    public ObservableList<MainGoodItemViewModel> goodsList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainGoodItemViewModel> itemBindingShopping = ItemBinding.of(BR.viewModel, R.layout.adapter_good);

    /**
     * 搜索商品
     */
    //给RecyclerView添加ObservableList
    public ObservableList<MainGoodItemViewModel> goodsLists = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainGoodItemViewModel> itemBindingShoppings = ItemBinding.of(BR.viewModel, R.layout.adapter_good);

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
    /**
     * 搜索
     */
    public BindingCommand SearchonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            KLog.d(EditBean.get());
                viewModel.searchLiveEvent.setValue(EditBean.get());
        }
    });

}
