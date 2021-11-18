package com.youwu.shouyinxitong.ui.coupon;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 多选推送优惠券
 * 2021/11/10
 */

public class CouponPushItemViewModel extends ItemViewModel<CouponPushViewModel> {
    public ObservableField<RechargeRecordBean.ListBean> entity = new ObservableField<>();
    public ObservableField<Integer> int_field = new ObservableField<>();
    public Drawable drawableImg;


    public CouponPushItemViewModel(@NonNull CouponPushViewModel viewModel, RechargeRecordBean.ListBean entity) {
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
            KLog.e("点击的位置--》"+getPosition());
            //拿到position
            int position = viewModel.CouponPushList.indexOf(CouponPushItemViewModel.this);
            entity.get().setPosition(position);
            viewModel.LiveEvent.setValue(entity.get());


        }
    });


}
