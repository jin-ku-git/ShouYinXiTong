package com.youwu.shouyinxitong.ui.calculate;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 沽清分类
 * 2021/11/03
 */

public class CheckNumItemViewModel extends ItemViewModel<CheckNumViewModel> {
    public ObservableField<InventoryTypeBean.GoodsTypeBean> entity = new ObservableField<>();
    public Drawable drawableImg;
    public int textColor=Color.WHITE;
    public int ColorYellow=R.color.colorPrimary;
    public int ColorPrimaryQing=R.color.colorPrimaryQing;

    public CheckNumItemViewModel(@NonNull CheckNumViewModel viewModel, InventoryTypeBean.GoodsTypeBean entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.color.colorPrimary);
    }

    public ObservableField<InventoryTypeBean.GoodsTypeBean> getEntity() {
        return entity;
    }

    public void setEntity(ObservableField<InventoryTypeBean.GoodsTypeBean> entity) {
        this.entity = entity;
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
            int position = viewModel.observableList.indexOf(CheckNumItemViewModel.this);
            entity.get().setPosition(position);
            viewModel.goodsTypeBeanSingleLiveEvent.setValue(entity.get());

        }
    });
    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //以前是使用Messenger发送事件，在InventoryViewModel中完成删除逻辑
//            Messenger.getDefault().send(NetWorkItemViewModel.this, InventoryViewModel.TOKEN_InventoryViewModel_DELTE_ITEM);
            //现在ItemViewModel中存在ViewModel引用，可以直接拿到LiveData去做删除
            ToastUtils.showShort(entity.get().getName());
        }
    });

}
