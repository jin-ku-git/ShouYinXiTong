package com.youwu.shouyinxitong.ui.main;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.bean_used.MealGoodsBean;

import me.goldze.mvvmhabit.base.ItemViewModel;

/**
 * 2021/10/21.
 */

public class MainGoodItemTCViewModel extends ItemViewModel<MainViewModel> {
    public ObservableField<MealGoodsBean> entity = new ObservableField<>();
    public ObservableField<String> entity_null = new ObservableField<>();
    public int visibility;
    public int textColor=Color.WHITE;
    public String string_null="";

    public MainGoodItemTCViewModel(@NonNull MainViewModel viewModel, MealGoodsBean MealsItemBean) {
        super(viewModel);
        this.entity.set(MealsItemBean);
    }

//    /**
//     * 可以在xml中使用binding:currentView="@{viewModel.titleTextView}" 拿到这个控件的引用, 但是强烈不推荐这样做，避免内存泄漏
//     **/
//    private TextView tv;
//    //将标题TextView控件回调到ViewModel中
//    public BindingCommand<TextView> titleTextView = new BindingCommand(new BindingConsumer<TextView>() {
//        @Override
//        public void call(TextView tv) {
//            NetWorkItemViewModel.this.tv = tv;
//        }
//    });
}
