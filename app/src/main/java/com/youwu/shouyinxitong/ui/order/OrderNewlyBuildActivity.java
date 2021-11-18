package com.youwu.shouyinxitong.ui.order;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lxj.xpopup.XPopup;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_new.OrderBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.databinding.ActivityCheckNumBinding;
import com.youwu.shouyinxitong.databinding.ActivityOrderNewlyBuildBinding;
import com.youwu.shouyinxitong.dialog.ApplyOrderDialog;
import com.youwu.shouyinxitong.ui.calculate.CheckNumItemViewModel;
import com.youwu.shouyinxitong.ui.calculate.CheckNumViewModel;
import com.youwu.shouyinxitong.ui.calculate.CheckNummShoppingCartItemViewModel;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 新建订单
 * 2021/11/03
 * 金库
 */

public class OrderNewlyBuildActivity extends BaseActivity<ActivityOrderNewlyBuildBinding, OrderNewlyBuildViewModel> {


    private List<InventoryTypeBean.GoodsTypeBean> goodsTypeBeans = new ArrayList<>();//分类数据

    private InventoryTypeBean.GoodsTypeBean goodsTypeBean;//
    int currentType=0;

    int allNum=0;

    OrderBean orderBean=new OrderBean();
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Intent intent =getIntent();
        orderBean= (OrderBean) intent.getSerializableExtra("entity");


    }

    @Override
    public OrderNewlyBuildViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(OrderNewlyBuildViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_newly_build;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //分类监听
        viewModel.uc.goodsList.observe(this, new Observer<ArrayList<InventoryTypeBean.GoodsTypeBean>>() {
            @Override
            public void onChanged(ArrayList<InventoryTypeBean.GoodsTypeBean> goodsTypeBeanss) {
                goodsTypeBeans.addAll(goodsTypeBeanss);
                goodsTypeBean=goodsTypeBeans.get(0);

            }
        });
        viewModel.goodsTypeBeanSingleLiveEvent.observe(this, new Observer<InventoryTypeBean.GoodsTypeBean>() {
            @Override
            public void onChanged(InventoryTypeBean.GoodsTypeBean Bean) {

                goodsTypeBean.setCurrentSelect(0);
                OrderNewlyBuildItemViewModel itemViewModels = new OrderNewlyBuildItemViewModel(viewModel, goodsTypeBean);
                viewModel.observableList.set(currentType,itemViewModels);
                currentType=Bean.position;
                Bean.setCurrentSelect(1);
                OrderNewlyBuildItemViewModel itemViewModel = new OrderNewlyBuildItemViewModel(viewModel, Bean);
                viewModel.observableList.set(Bean.position,itemViewModel);

                goodsTypeBean=Bean;
            }
        });
        viewModel.commodityDetailBeanSingleLiveEvent.observe(this, new Observer<CommodityDetailBean>() {
            @Override
            public void onChanged(CommodityDetailBean commodityDetailBean) {


                showApplyOrderDialog(commodityDetailBean);

            }
        });
        //删除购物车数据
//        viewModel.deleteEvent.observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                viewModel.
//            }
//        });
    }
    //判断是否添加到购物车
    public void showApplyOrderDialog(CommodityDetailBean commodityDetailBean) {

        ApplyOrderDialog dialog = new ApplyOrderDialog(this, commodityDetailBean, 1);
        dialog.setListener(new ApplyOrderDialog.OnconfirmListener() {
            @Override
            public void onConfirm(CommodityDetailBean commodityDetailBean) {
                for (int i=0;i<viewModel.shoppingCartList.size();i++){//如果购物车里有就替换掉
                    if (commodityDetailBean.getCommodity_id().equals(viewModel.shoppingCartList.get(i).entity.get().getCommodity_id())){
                        OrderNewlyShoppingCartItemViewModel itemViewModel = new OrderNewlyShoppingCartItemViewModel(viewModel, commodityDetailBean);
                        viewModel.shoppingCartList.set(i,itemViewModel);
                        againNum();
                        return;
                    }
                }
                OrderNewlyShoppingCartItemViewModel itemViewModel = new OrderNewlyShoppingCartItemViewModel(viewModel, commodityDetailBean);
                viewModel.shoppingCartList.add(itemViewModel);
                againNum();

            }
        });
        new XPopup.Builder(this)
                .isRequestFocus(false)
                .asCustom(dialog)
                .show();
    }
    //重新计算数量
    private void againNum() {
        allNum=0;
        for (int i=0;i<viewModel.shoppingCartList.size();i++){
            allNum+=Integer.parseInt(viewModel.shoppingCartList.get(i).entity.get().getCommodity_num());
        }
        viewModel.allNum.set(allNum);
    }


    @Override
    public void initData() {
        super.initData();
        binding.goodsTypeList.addItemDecoration(new GridDividerItemDecoration(this, 8, 1, getResources().getColor(R.color.white)));
        binding.commodityList.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        //获取分类
        viewModel.getCategoryList();
        //获取商品列表
        viewModel.getCommodityList();
        //获取购物车列表
//        viewModel.getCheckShoppingCartList();

        //如果是复用订单
        if (orderBean!=null){
            for (int i=0;i<orderBean.getOrder_details().size();i++) {

                CommodityDetailBean commodityDetailBean =new CommodityDetailBean();

                commodityDetailBean.setCommodity_name(orderBean.getOrder_details().get(i).getCommodity_name());//名称
                commodityDetailBean.setCommodity_num(orderBean.getOrder_details().get(i).getCommodity_number());//数量
                commodityDetailBean.setCommodity_purchase_price_subtotal(orderBean.getOrder_details().get(i).getCommodity_total_price());//商品小计
                OrderNewlyShoppingCartItemViewModel itemViewModel = new OrderNewlyShoppingCartItemViewModel(viewModel, commodityDetailBean);
                //双向绑定动态添加Item
                viewModel.shoppingCartList.add(itemViewModel);

            }
        }
        //重新计算数量
        againNum();



    }
}
