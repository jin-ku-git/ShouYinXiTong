package com.youwu.shouyinxitong.ui.calculate;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import java.util.ArrayList;
import java.util.List;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 盘算
 * 2021/11/01
 * 金库
 */

public class InventoryViewModel extends BaseViewModel {
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();
    //分类观察者
    public SingleLiveEvent<InventoryTypeBean.GoodsTypeBean> goodsTypeBeanSingleLiveEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<CommodityDetailBean> commodityDetailBeanSingleLiveEvent = new SingleLiveEvent<>();

    public boolean view_false=false;
    public boolean view_true=true;
    List listData=new ArrayList();//分类数据
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    public class UIChangeObservable {

        //分类观察者
        public SingleLiveEvent<ArrayList<InventoryTypeBean.GoodsTypeBean>> goodsList = new SingleLiveEvent<>();

    }


    public InventoryViewModel(@NonNull Application application) {
        super(application);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<InventoryItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<InventoryItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_inventory_type);

    //给RecyclerView添加ObservableList
    public ObservableList<InventoryCommodityItemViewModel> commodityList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<InventoryCommodityItemViewModel> itemBindingShopping = ItemBinding.of(BR.viewModel, R.layout.adapter_inventory_commodity);


    //给RecyclerView添加ObservableList
    public ObservableList<InventoryShoppingCartItemViewModel> shoppingCartList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<InventoryShoppingCartItemViewModel> itemBindingShoppingCart = ItemBinding.of(BR.viewModel, R.layout.item_inventory_car);


    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //清空点击事件
    public BindingCommand emptyonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            shoppingCartList.clear();

        }
    });

    //获取商品分类
    public void getCategoryList() {

        for (int i=0;i<10;i++) {

            InventoryTypeBean.GoodsTypeBean goodsTypeBean =new InventoryTypeBean.GoodsTypeBean();

            goodsTypeBean.setName("测试"+i);
            InventoryItemViewModel itemViewModel = new InventoryItemViewModel(InventoryViewModel.this, goodsTypeBean);
            //双向绑定动态添加Item
            observableList.add(itemViewModel);
            listData.add(goodsTypeBean);

        }
        ArrayList<InventoryTypeBean.GoodsTypeBean> categoryBeans = (ArrayList<InventoryTypeBean.GoodsTypeBean>) listData;
        categoryBeans.get(0).currentSelect=1;

        uc.goodsList.setValue(categoryBeans);
    }
    //获取商品列表
    public void getCommodityList() {

        for (int i=0;i<10;i++) {

            CommodityDetailBean commodityDetailBean =new CommodityDetailBean();

            commodityDetailBean.setCommodity_name("商品测试"+i);
            commodityDetailBean.setCommodity_stock_num(i);
            commodityDetailBean.setCommodity_id(i+"");
            InventoryCommodityItemViewModel itemViewModel = new InventoryCommodityItemViewModel(InventoryViewModel.this, commodityDetailBean);
            //双向绑定动态添加Item
            commodityList.add(itemViewModel);

        }

    }

    //获取购物车列表
    public void getCheckShoppingCartList() {

        for (int i=1;i<10;i++) {

            CommodityDetailBean commodityDetailBean =new CommodityDetailBean();

            commodityDetailBean.setCommodity_name("商品测试"+i);
            commodityDetailBean.setCommodity_ranking(i+".");
            commodityDetailBean.setCommodity_stock_num(i);
            commodityDetailBean.setCommodity_change_num(i+5);
            InventoryShoppingCartItemViewModel itemViewModel = new InventoryShoppingCartItemViewModel(InventoryViewModel.this, commodityDetailBean);
            //双向绑定动态添加Item
            shoppingCartList.add(itemViewModel);

        }

    }

    /**
     * 获取条目下标
     *
     * @param inventoryItemViewModel
     * @return
     */
    public int getItemPosition(InventoryItemViewModel inventoryItemViewModel) {
        return observableList.indexOf(inventoryItemViewModel);
    }
    /**
     * 获取条目下标
     *
     * @param inventoryCommodityItemViewModel
     * @return
     */
    public int getItemPosition(InventoryCommodityItemViewModel inventoryCommodityItemViewModel) {
        return commodityList.indexOf(inventoryCommodityItemViewModel);
    }
    /**
     * 获取条目下标
     *
     * @param inventoryShoppingCartItemViewModel
     * @return
     */
    public int getItemPosition(InventoryShoppingCartItemViewModel inventoryShoppingCartItemViewModel) {
        return shoppingCartList.indexOf(inventoryShoppingCartItemViewModel);
    }
}
