package com.youwu.shouyinxitong.ui.calculate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.ui.form.FormFragment;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 沽清
 * 2021/11/03
 */

public class CheckNumViewModel extends BaseViewModel<DemoRepository> {

    public ObservableField<Integer> allNum = new ObservableField<>();
    //使用Observable
    public SingleLiveEvent<Boolean> requestCameraPermissions = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<String> loadUrlEvent = new SingleLiveEvent<>();
    //使用LiveData  删除通知
    public SingleLiveEvent<Integer> deleteEvent = new SingleLiveEvent<>();

    public boolean view_false=false;
    public boolean view_true=true;

    //点击商品观察者
    public SingleLiveEvent<CommodityDetailBean> commodityDetailBeanSingleLiveEvent = new SingleLiveEvent<>();
    List listData=new ArrayList();//分类数据
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    public class UIChangeObservable {

        //分类观察者
        public SingleLiveEvent<ArrayList<InventoryTypeBean.GoodsTypeBean>> goodsList = new SingleLiveEvent<>();

    }
    //分类观察者
    public SingleLiveEvent<InventoryTypeBean.GoodsTypeBean> goodsTypeBeanSingleLiveEvent = new SingleLiveEvent<>();

    public CheckNumViewModel(@NonNull Application application,DemoRepository model) {
        super(application,model);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<CheckNumItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CheckNumItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_check_num_type);

    //给RecyclerView添加ObservableList
    public ObservableList<CheckNummCommodityItemViewModel> commodityList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CheckNummCommodityItemViewModel> itemBindingShopping = ItemBinding.of(BR.viewModel, R.layout.adapter_check_num_commodity);

    //给RecyclerView添加ObservableList
    public ObservableList<CheckNummShoppingCartItemViewModel> shoppingCartList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<CheckNummShoppingCartItemViewModel> itemBindingShoppingCart = ItemBinding.of(BR.viewModel, R.layout.item_check_num_car);

    //表单提交点击事件
    public BindingCommand formSbmClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(FormFragment.class.getCanonicalName());
        }
    });

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
            allNum.set(0);
        }
    });


    //获取商品分类
    public void getCategoryList() {

        for (int i=0;i<10;i++) {

            InventoryTypeBean.GoodsTypeBean goodsTypeBean =new InventoryTypeBean.GoodsTypeBean();

            goodsTypeBean.setName("测试"+i);
            CheckNumItemViewModel itemViewModel = new CheckNumItemViewModel(CheckNumViewModel.this, goodsTypeBean);
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
            CheckNummCommodityItemViewModel itemViewModel = new CheckNummCommodityItemViewModel(CheckNumViewModel.this, commodityDetailBean);
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
            CheckNummShoppingCartItemViewModel itemViewModel = new CheckNummShoppingCartItemViewModel(CheckNumViewModel.this, commodityDetailBean);
            //双向绑定动态添加Item
            shoppingCartList.add(itemViewModel);

        }

    }

    /**
     * 获取条目下标
     *
     * @param checkNumViewModel
     * @return
     */
    public int getItemPosition(CheckNumItemViewModel checkNumViewModel) {
        return observableList.indexOf(checkNumViewModel);
    }
    /**
     * 获取条目下标
     *
     * @param checkNummCommodityItemViewModel
     * @return
     */
    public int getItemPosition(CheckNummCommodityItemViewModel checkNummCommodityItemViewModel) {
        return commodityList.indexOf(checkNummCommodityItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param checkNummShoppingCartItemViewModel
     * @return
     */
    public int getItemPosition(CheckNummShoppingCartItemViewModel checkNummShoppingCartItemViewModel) {
        return shoppingCartList.indexOf(checkNummShoppingCartItemViewModel);
    }

}
