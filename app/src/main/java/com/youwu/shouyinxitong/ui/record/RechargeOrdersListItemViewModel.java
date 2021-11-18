package com.youwu.shouyinxitong.ui.record;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;

import org.greenrobot.eventbus.EventBus;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 2021/10/29
 */

public class RechargeOrdersListItemViewModel extends ItemViewModel<RechargeOrdersViewModel> {
    public ObservableField<RechargeRecordBean.ListBean> entity = new ObservableField<>();
    public Drawable drawableImg;
    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;

    public RechargeOrdersListItemViewModel(@NonNull RechargeOrdersViewModel viewModel, RechargeRecordBean.ListBean entity) {
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
            int position = viewModel.rechargeOrdersList.indexOf(RechargeOrdersListItemViewModel.this);
            entity.get().setPosition(position);
            EventBus.getDefault().post(entity.get());
        }
    });


}
