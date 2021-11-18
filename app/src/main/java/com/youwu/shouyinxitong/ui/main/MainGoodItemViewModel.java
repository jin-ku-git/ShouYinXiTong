package com.youwu.shouyinxitong.ui.main;

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
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.ui.commodity.AddCommodityDetailActivity;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import java.util.Objects;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/10/21.
 */

public class MainGoodItemViewModel extends ItemViewModel<MainViewModel> {
    public ObservableField<YWGoodBean> entity = new ObservableField<>();
    public ObservableField<MealsItemBean> entity_meals = new ObservableField<>();
    private int type=0;
    public Drawable drawableImg;
    public int visibility;
    public int textColor=Color.WHITE;
    public int GONE=View.GONE;
    public int VISIBLE=View.VISIBLE;
    public String RMB="￥";

    public MainGoodItemViewModel(@NonNull MainViewModel viewModel, YWGoodBean entity) {
        super(viewModel);
        visibility= type==0?View.GONE:View.VISIBLE;
        this.entity.set((YWGoodBean) entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.color.colorPrimary);
    }
    public MainGoodItemViewModel(@NonNull MainViewModel viewModel, MealsItemBean MealsItemBean,int type) {
        super(viewModel);
        this.type=type;
        visibility= type==0?View.GONE:View.VISIBLE;
        this.entity_meals.set((MealsItemBean) MealsItemBean);
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

    //图片的点击事件
    public BindingCommand imgSynconsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            //拿到position
            int position = viewModel.observableList.indexOf(MainGoodItemViewModel.this);
            if (type==0){
                Objects.requireNonNull(entity.get()).setType(0);

                viewModel.ywGoodBeanSingleLiveEvent.setValue(entity.get());
            }else {
                Objects.requireNonNull(entity_meals.get()).setType(0);

                viewModel.mealsItemBeanSingleLiveEvent.setValue(entity_meals.get());
            }
        }
    });

    //添加到购物车
    public View.OnClickListener addShoppingCart=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (type==0){
                Objects.requireNonNull(entity.get()).setType(1);
                viewModel.ywGoodBeanSingleLiveEvent.setValue(entity.get());
            }else {
                Objects.requireNonNull(entity_meals.get()).setType(1);
                viewModel.mealsItemBeanSingleLiveEvent.setValue(entity_meals.get());
            }
        }
    };


    //新增商品
    public BindingCommand addonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("新增商品");
            viewModel.startActivity(AddCommodityDetailActivity.class);
        }
    });

    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //以前是使用Messenger发送事件，在MainViewModel中完成删除逻辑
//            Messenger.getDefault().send(NetWorkItemViewModel.this, MainViewModel.TOKEN_MainViewModel_DELTE_ITEM);
            //现在ItemViewModel中存在ViewModel引用，可以直接拿到LiveData去做删除
            ToastUtils.showShort(entity.get().product_name);
        }
    });

}
