package com.youwu.shouyinxitong.ui.order;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.ui.calculate.CheckNumViewModel;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 新建订单购物车
 * 2021/11/03
 */

public class OrderNewlyShoppingCartItemViewModel extends ItemViewModel<OrderNewlyBuildViewModel> {
    public ObservableField<CommodityDetailBean> entity = new ObservableField<>();

    public Drawable drawableImg;

    public int textColor=Color.WHITE;
    public int GONE=View.GONE;
    public int VISIBLE=View.VISIBLE;





    public OrderNewlyShoppingCartItemViewModel(@NonNull OrderNewlyBuildViewModel viewModel, CommodityDetailBean entity) {
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

    //删除的点击事件
    public BindingCommand deleteOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            //拿到position
            int position = viewModel.shoppingCartList.indexOf(OrderNewlyShoppingCartItemViewModel.this);

            KLog.d("position-->"+getPosition());
            viewModel.shoppingCartList.remove(position);
//            viewModel.deleteEvent.setValue(position);
        }
    });

    //添加到购物车
    public View.OnClickListener addShoppingCart=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    //条目的点击事件
    public BindingCommand layoutonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });


}
