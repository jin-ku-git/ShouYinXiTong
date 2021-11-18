package com.youwu.shouyinxitong.ui.order;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.ui.calculate.CheckNummShoppingCartItemViewModel;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 确认订单分类
 * 2021/11/05
 */

public class ConfirmGoodsItemViewModel extends ItemViewModel<ConfirmGoodsViewModel> {
    public ObservableField<CommodityDetailBean> entity = new ObservableField<>();
    public Drawable drawableImg;



    public ConfirmGoodsItemViewModel(@NonNull ConfirmGoodsViewModel viewModel, CommodityDetailBean entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.color.colorPrimary);
    }

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            //拿到position
            int position = viewModel.observableList.indexOf(ConfirmGoodsItemViewModel.this);


        }
    });
    //删除的点击事件
    public BindingCommand deleteOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            //拿到position
            int position = viewModel.observableList.indexOf(ConfirmGoodsItemViewModel.this);

            KLog.d("position-->"+position);
            viewModel.observableList.remove(position);
//            viewModel.deleteEvent.setValue(position);
        }
    });


}
