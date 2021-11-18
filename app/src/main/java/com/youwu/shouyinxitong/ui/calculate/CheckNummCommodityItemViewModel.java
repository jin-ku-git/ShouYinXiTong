package com.youwu.shouyinxitong.ui.calculate;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.ui.main.MainGoodItemTCViewModel;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 沽清商品
 * 2021/11/03
 */

public class CheckNummCommodityItemViewModel extends ItemViewModel<CheckNumViewModel> {
    public ObservableField<CommodityDetailBean> entity = new ObservableField<>();

    public Drawable drawableImg;

    public int textColor=Color.WHITE;
    public int GONE=View.GONE;
    public int VISIBLE=View.VISIBLE;



    public CheckNummCommodityItemViewModel(@NonNull CheckNumViewModel viewModel, CommodityDetailBean entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.color.colorPrimary);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<MainGoodItemTCViewModel> goodsList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MainGoodItemTCViewModel> itemBindingShopping = ItemBinding.of(BR.viewModel, R.layout.item_goods_tc);

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //条目的点击事件
    public BindingCommand imgSynconsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            //拿到position
            int position = viewModel.observableList.indexOf(CheckNummCommodityItemViewModel.this);

        }
    });

    //添加到购物车
    public View.OnClickListener addShoppingCart=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            viewModel.commodityDetailBeanSingleLiveEvent.setValue(entity.get());
        }
    };


    //条目的点击事件
    public BindingCommand layoutonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });


}
