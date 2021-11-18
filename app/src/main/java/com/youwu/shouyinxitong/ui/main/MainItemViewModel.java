package com.youwu.shouyinxitong.ui.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;


import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 2021/10/25
 */

public class MainItemViewModel extends ItemViewModel<MainViewModel> {
    public ObservableField<GoodsTypesBean.GoodsTypeBean> entity = new ObservableField<>();
    public Drawable drawableImg;
    public int textColor=Color.WHITE;


    public MainItemViewModel(@NonNull MainViewModel viewModel, GoodsTypesBean.GoodsTypeBean entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.color.colorPrimary);
    }

    public ObservableField<GoodsTypesBean.GoodsTypeBean> getEntity() {
        return entity;
    }

    public void setEntity(ObservableField<GoodsTypesBean.GoodsTypeBean> entity) {
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
            int position = viewModel.observableList.indexOf(MainItemViewModel.this);
            entity.get().setPosition(position);
            viewModel.goodsTypeBeanSingleLiveEvent.setValue(entity.get());
        }
    });
    //条目的添加分类
    public BindingCommand additemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            RxToast.normal("添加分类");
        }
    });
    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //以前是使用Messenger发送事件，在MainViewModel中完成删除逻辑
//            Messenger.getDefault().send(NetWorkItemViewModel.this, MainViewModel.TOKEN_MainViewModel_DELTE_ITEM);
            //现在ItemViewModel中存在ViewModel引用，可以直接拿到LiveData去做删除
            ToastUtils.showShort(entity.get().getName());
        }
    });

}
