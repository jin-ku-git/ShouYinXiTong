package com.youwu.shouyinxitong.ui.commodity;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.Gson;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.data.DemoRepository;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 编辑商品
 * 2021/10/30
 * 金库
 */

public class EditCommodityDetailViewModel extends BaseViewModel<DemoRepository>  {

    //使用LiveData
    public SingleLiveEvent<String> addVIP = new SingleLiveEvent<>();
    public ObservableField<VIPBean> vipBean = new ObservableField<>();


    public SingleLiveEvent<CommodityDetailBean> commodityDetailBeanSingleLiveEvent = new SingleLiveEvent<>();

    public int GONE= View.GONE;
    public int VISIBLE= View.VISIBLE;

    public EditCommodityDetailViewModel(@NonNull Application application,DemoRepository model) {
        super(application,model);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<EditCommodityItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<EditCommodityItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.adapter_search_commodity);

    //返回点击事件
    public BindingCommand finishonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //提交按钮点击事件
    public BindingCommand onCmtClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            String submitJson = new Gson().toJson(vipBean);
            addVIP.setValue(submitJson);
        }
    });


    public void CommodityList(int pageNo){
        for (int i=0;i<5;i++){
            CommodityDetailBean commodityDetailBean =new CommodityDetailBean();
            commodityDetailBean.setCommodity_name("测试");
            commodityDetailBean.setCommodity_price("55."+i);
            commodityDetailBean.setCommodity_purchase_price("33."+i);
            commodityDetailBean.setCommodity_stock_num(i);
            commodityDetailBean.setCommodity_code("13123124124141");
            commodityDetailBean.setCommodity_code_pinyin("2334555666");
            commodityDetailBean.setCommodity_custom("qwe");
            commodityDetailBean.setGoods_type(1);
            commodityDetailBean.setCommodity_state(1);

            EditCommodityItemViewModel itemViewModel = new EditCommodityItemViewModel(EditCommodityDetailViewModel.this, commodityDetailBean);
            observableList.add(itemViewModel);
        }
    }

    /**
     * 获取条目下标
     *
     * @param editCommodityItemViewModel
     * @return
     */
    public int getItemPosition(EditCommodityItemViewModel editCommodityItemViewModel) {
        return observableList.indexOf(editCommodityItemViewModel);
    }
}
