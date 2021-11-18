package com.youwu.shouyinxitong.ui.calculate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.databinding.ActivityCheckNumBinding;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

import static me.goldze.mvvmhabit.base.BaseViewModel.toPrettyFormat;

/**
 * 沽清
 * 2021/11/03
 * 金库
 */

public class CheckNumActivity extends BaseActivity<ActivityCheckNumBinding, CheckNumViewModel> {


    private List<InventoryTypeBean.GoodsTypeBean> goodsTypeBeans = new ArrayList<>();//分类数据

    private InventoryTypeBean.GoodsTypeBean goodsTypeBean;//
    int currentType=0;

    int allNum=0;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public CheckNumViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(CheckNumViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_check_num;
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
                CheckNumItemViewModel itemViewModels = new CheckNumItemViewModel(viewModel, goodsTypeBean);
                viewModel.observableList.set(currentType,itemViewModels);
                currentType=Bean.position;
                Bean.setCurrentSelect(1);
                CheckNumItemViewModel itemViewModel = new CheckNumItemViewModel(viewModel, Bean);
                viewModel.observableList.set(Bean.position,itemViewModel);

                goodsTypeBean=Bean;
            }
        });
        viewModel.commodityDetailBeanSingleLiveEvent.observe(this, new Observer<CommodityDetailBean>() {
            @Override
            public void onChanged(CommodityDetailBean commodityDetailBean) {


                for (int i=0;i<viewModel.shoppingCartList.size();i++){
                    if (commodityDetailBean.getCommodity_id().equals(viewModel.shoppingCartList.get(i).entity.get().getCommodity_id())){

                        return;
                    }
                }
                    CheckNummShoppingCartItemViewModel itemViewModel = new CheckNummShoppingCartItemViewModel(viewModel, commodityDetailBean);
                    viewModel.shoppingCartList.add(itemViewModel);
                    allNum=0;
                    for (int i=0;i<viewModel.shoppingCartList.size();i++){
                        allNum+=viewModel.shoppingCartList.get(i).entity.get().getCommodity_stock_num();
                    }
                    viewModel.allNum.set(allNum);


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
        viewModel.getCheckShoppingCartList();
    }
}
