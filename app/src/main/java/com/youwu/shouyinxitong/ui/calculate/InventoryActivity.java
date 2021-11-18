package com.youwu.shouyinxitong.ui.calculate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.InventoryTypeBean;
import com.youwu.shouyinxitong.databinding.ActivityInventoryBinding;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 盘算
 * 2021/11/01
 * 金库
 */

public class InventoryActivity extends BaseActivity<ActivityInventoryBinding, InventoryViewModel> {


    private List<InventoryTypeBean.GoodsTypeBean> goodsTypeBeans = new ArrayList<>();//分类数据

    private InventoryTypeBean.GoodsTypeBean goodsTypeBean;//
    int currentType=0;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_inventory;
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
                InventoryItemViewModel itemViewModels = new InventoryItemViewModel(viewModel, goodsTypeBean);
                viewModel.observableList.set(currentType,itemViewModels);
                currentType=Bean.position;
                Bean.setCurrentSelect(1);
                InventoryItemViewModel itemViewModel = new InventoryItemViewModel(viewModel, Bean);
                viewModel.observableList.set(Bean.position,itemViewModel);

                goodsTypeBean=Bean;
            }
        });
        //添加到购物车
        viewModel.commodityDetailBeanSingleLiveEvent.observe(this, new Observer<CommodityDetailBean>() {
            @Override
            public void onChanged(CommodityDetailBean commodityDetailBean) {

                for (int i=0;i<viewModel.shoppingCartList.size();i++){
                    if (commodityDetailBean.getCommodity_id().equals(viewModel.shoppingCartList.get(i).entity.get().getCommodity_id())){

                        return;
                    }
                }
                InventoryShoppingCartItemViewModel itemViewModel = new InventoryShoppingCartItemViewModel(viewModel, commodityDetailBean);
                viewModel.shoppingCartList.add(itemViewModel);
            }
        });
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
