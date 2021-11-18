package com.youwu.shouyinxitong.ui.main;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.library.baseAdapters.BR;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.orhanobut.logger.Logger;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.UriUtils;
import com.youwu.shouyinxitong.bean_new.RechargeRecordBean;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.bean_used.ActivityListBean;
import com.youwu.shouyinxitong.bean_used.CouponBean;
import com.youwu.shouyinxitong.bean_used.CouponCodeBean;
import com.youwu.shouyinxitong.bean_used.GoodsListBean;
import com.youwu.shouyinxitong.bean_used.GoodsRequestBean;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean_used.NoDataBean;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean.VipCardQequestBean;
import com.youwu.shouyinxitong.bean.VipCodeQequestBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.data.DemoRepository;

import com.youwu.shouyinxitong.databinding.ItemViewpagerBinding;
import com.youwu.shouyinxitong.ui.cashier.CashierActivity;
import com.youwu.shouyinxitong.ui.search.SearchVipActivity;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.viewpager.ViewPagerItemViewModel;


import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 2021/10/21
 */

public class MainViewModel extends BaseViewModel<DemoRepository> {
    public SingleLiveEvent<String> itemClickEvent = new SingleLiveEvent<>();

    public ObservableField<UserBean> userBean = new ObservableField<>();

    //    获取所有商品接口的页数和每页的个数
    private int pages = 1;
    private int sizes = 100;

    List listData=new ArrayList();//分类数据
    List<YWGoodBean> listDataTwo=new ArrayList();//商品列表数据
    List<MealsItemBean> listMeals =new ArrayList();//套餐列表数据

    //点击分类观察者
    public SingleLiveEvent<GoodsTypesBean.GoodsTypeBean> goodsTypeBeanSingleLiveEvent = new SingleLiveEvent<>();

    //点击商品观察者
    public SingleLiveEvent<YWGoodBean> ywGoodBeanSingleLiveEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<MealsItemBean> mealsItemBeanSingleLiveEvent = new SingleLiveEvent<>();
    //点击搜索按钮
    public SingleLiveEvent<String> searchLiveEvent = new SingleLiveEvent<>();

    public MainViewModel(@NonNull Application application, DemoRepository model) {
        super(application, model);
        //模拟3个ViewPager页面
        for (int i = 1; i <= 2; i++) {

            ViewPagerItemViewModel itemViewModel = new ViewPagerItemViewModel(this, observableList,goodsList,goodsLists,i);
            items.add(itemViewModel);
        }
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    public class UIChangeObservable {
        //登录返回观察者
        public SingleLiveEvent<Integer> StoreEvent = new SingleLiveEvent<>();

        //返回观察者
        public SingleLiveEvent<GoodsListBean> goodsListBeanEvent = new SingleLiveEvent<>();
        //分类观察者
        public SingleLiveEvent<ArrayList<GoodsTypesBean.GoodsTypeBean>> goodsList = new SingleLiveEvent<>();
        //商品列表观察者
        public SingleLiveEvent<List<YWGoodBean>> goodsListTwo = new SingleLiveEvent<>();
        //套餐列表观察者
        public SingleLiveEvent<List<MealsItemBean>> goodsListMeals = new SingleLiveEvent<>();
        //优惠活动
        public SingleLiveEvent<ActivityListBean> activityList = new SingleLiveEvent<>();
        //vip
        public SingleLiveEvent<VIPBean> vipBeanList = new SingleLiveEvent<>();
        //优惠券
        public SingleLiveEvent<CouponBean> CouponList = new SingleLiveEvent<>();
    }

    /**
     * 分类
     */
    //给RecyclerView添加ObservableList
    public ObservableList<MainItemViewModel> observableList = new ObservableArrayList<>();
    /**
     * 商品
     */
    //给RecyclerView添加goodsList
    public ObservableList<MainGoodItemViewModel> goodsList = new ObservableArrayList<>();
    /**
     * 搜索的商品
     */
    //给RecyclerView添加goodsLists
    public ObservableList<MainGoodItemViewModel> goodsLists = new ObservableArrayList<>();

    //给ViewPager添加items
    public ObservableList<ViewPagerItemViewModel> items = new ObservableArrayList<>();
    //给ViewPager添加ItemBinding
    public ItemBinding<ViewPagerItemViewModel> itemBindings = ItemBinding.of(BR.viewModel, R.layout.item_viewpager);


    //弹窗点击事件
    public BindingCommand fun_dialog = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(1);
        }
    });

    //更新数据
    public BindingCommand imgSynconsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //获取商品分类
            getCategoryList();
            uc.StoreEvent.setValue(2);
        }
    });
    //选择会员
    public BindingCommand ChoiceVIPonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startActivity(SearchVipActivity.class);
        }
    });
    //清空购物车
    public BindingCommand clearonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(3);
        }
    });
    //挂单
    public BindingCommand restingOrderonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(4);
        }
    });
    //取单
    public BindingCommand getrestingonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(5);
        }
    });
    //搜索
    public BindingCommand searchOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(6);
        }
    });
    //取消搜索
    public BindingCommand cancelSearchOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            uc.StoreEvent.setValue(7);
        }
    });

    //点击优惠券弹窗
    public BindingCommand CouponsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(8);
        }
    });

    //清空购物车
    public BindingCommand useronClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(9);
        }
    });

    //点击收银
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(CashierActivity.class);
        }
    });


    //获取商品分类
    public void getCategoryList() {

            model.CATEGORY_LIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            showDialog();
                        }
                    })
                    .subscribe(new DisposableObserver<BaseBean<Object>>() {
                        @Override
                        public void onNext(BaseBean response) {
                            String submitJson = new Gson().toJson(response);
                            Log.e("获取商品分类返回结果-----已解析",toPrettyFormat(submitJson));
                            //清除列表

                            Logger.d(response);
                            observableList.clear();
                            if (200==response.getCode()){
                                String submitJsonData = new Gson().toJson(response.data);
                                GoodsTypesBean goodsTypesBean= JSON.parseObject(toPrettyFormat(submitJsonData), GoodsTypesBean.class);

                                if (listData!=null){
                                    listData.clear();
                                }
                                for (GoodsTypesBean.GoodsTypeBean entity : goodsTypesBean.getGoodsTypeBeans()) {
                                    MainItemViewModel itemViewModel = new MainItemViewModel(MainViewModel.this, entity);
                                    //双向绑定动态添加Item
                                    observableList.add(itemViewModel);
                                    listData.add(entity);
                                }
                                ArrayList<GoodsTypesBean.GoodsTypeBean> categoryBeans = (ArrayList<GoodsTypesBean.GoodsTypeBean>) listData;
                                categoryBeans.get(0).currentSelect=1;

                                uc.goodsList.setValue(categoryBeans);
                                if (categoryBeans.size() == 0) {
                                    ToastUtil.showShortToast("没有商品");
                                    return;
                                }

                                // todo:这里需要把分类存放在内存sp中去
                                String categories = JSON.toJSONString(categoryBeans);
                                AppApplication.spUtils.put("category", categories);
                                //获取套餐分类
                                getMealGoodsType();
                            }else {
                                RxToast.normal(response.getMessage());
                            }
                        }
                        @Override
                        public void onError(Throwable throwable) {
                            //关闭对话框
                            dismissDialog();
                            if (throwable instanceof ResponseThrowable) {
                                ToastUtils.showShort(((ResponseThrowable) throwable).message);
                            }
                        }
                        @Override
                        public void onComplete() {
                            //关闭对话框
//                            dismissDialog();
                        }
                    });
    }

    /**
     * 获取套餐分类
     */
    private void getMealGoodsType() {
        model.MEALGOODSTYPE(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("获取套餐分类返回结果-----已解析",toPrettyFormat(submitJson));

                        List listData=new ArrayList();
                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response);

                            GoodsTypesBean goodsTypesBean= JSON.parseObject(toPrettyFormat(submitJsonData), GoodsTypesBean.class);
                            for (GoodsTypesBean.GoodsTypeBean entity : goodsTypesBean.getData()) {
                                MainItemViewModel itemViewModel = new MainItemViewModel(MainViewModel.this, entity);
                                //双向绑定动态添加Item
                                observableList.add(itemViewModel);

                                listData.add(entity);
                            }
                            //添加一个新的
                            GoodsTypesBean.GoodsTypeBean entitys=new GoodsTypesBean.GoodsTypeBean();
                            MainItemViewModel itemViewModel = new MainItemViewModel(MainViewModel.this, entitys);
                            observableList.add(itemViewModel);

                            ArrayList<GoodsTypesBean.GoodsTypeBean> categoryBeans = (ArrayList<GoodsTypesBean.GoodsTypeBean>) listData;
                            for (int j = 0; j < categoryBeans.size(); j++) {
                                categoryBeans.get(j).isMeals=true;
                            }
                            uc.goodsList.setValue(categoryBeans);
                            pages = 1;
                            sizes = 100;
                            getGoodsList(pages + "", sizes + "");
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
//                        dismissDialog();
                    }
                });
    }

    /**
     *获取商品列表
     * @param page
     * @param size
     */
    public void getGoodsList(String page, String size) {
        GoodsRequestBean goodsRequestBean=new GoodsRequestBean();

        goodsRequestBean.setPageNumber(page);
        goodsRequestBean.setPageSize(size);
        model.PAGE(UriUtils.getRequestPostUrl(goodsRequestBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);


                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);

                            GoodsListBean goodsListBean= JSON.parseObject(toPrettyFormat(submitJsonData), GoodsListBean.class);

                            if (listDataTwo!=null){
                                listDataTwo.clear();
                            }
                            for (YWGoodBean entity : goodsListBean.getItems()) {
//                                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(MainViewModel.this, entity);
//                                //双向绑定动态添加Item
//                                goodsList.add(itemViewModel);

                                listDataTwo.add(entity);
                            }

                            uc.goodsListTwo.setValue(listDataTwo);

//                            uc.goodsListBeanEvent.setValue(goodsListBean);


                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
//                        dismissDialog();
                    }
                });
    }

    /**
     * 获取套餐列表
     */
    public void getStoreMealSetGoodsList() {

        model.MEALGOODSLIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);

//                        dismissDialog();

                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);

                            List<MealsItemBean> list=JSON.parseObject(submitJsonData,  new TypeToken<List<MealsItemBean>>(){}.getType());

                            listMeals.addAll(list);
//                            for (MealsItemBean entity : list) {
//                                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(MainViewModel.this, entity);
//                                //双向绑定动态添加Item
//                                goodsList.add(itemViewModel);
//                            }
                            uc.goodsListMeals.setValue(listMeals);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
//                        dismissDialog();
                    }
                });

    }

    //优惠活动
    public void getActivitList() {

        model.LIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("优惠活动返回结果-----已解析",toPrettyFormat(submitJson));
                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            ActivityListBean activityListBean = JSON.parseObject(submitJsonData, ActivityListBean.class);
                            uc.activityList.setValue(activityListBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }

    //根据会员卡/IC卡获取会员
    public void getVIPbyICCard(String card) {

        Log.e("scanToWork", "card" + card);

        VipCardQequestBean vipCardQequestBean = new VipCardQequestBean();
        vipCardQequestBean.setIc_card(card);

        model.PAYMENT_CARD_TWO(UriUtils.getRequestPostUrl(vipCardQequestBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("根据会员卡/IC卡获取会员返回结果-----已解析",toPrettyFormat(submitJson));
                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            VIPBean vipBean = AppApplication.gson.fromJson(submitJsonData, VIPBean.class);
                            uc.vipBeanList.setValue(vipBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });

    }
    //根据会员码获取会员
    public void getVIPbyCode(String vipCode) {

        Log.e("scanToWork", "card" + vipCode);

        VipCodeQequestBean vipCodeQequestBean = new VipCodeQequestBean();
        vipCodeQequestBean.setPayment_code(vipCode);

        model.PAYMENT_CODE(UriUtils.getRequestPostUrl(vipCodeQequestBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("优惠活动返回结果-----已解析",toPrettyFormat(submitJson));
                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            VIPBean vipBean = AppApplication.gson.fromJson(submitJsonData, VIPBean.class);
                            uc.vipBeanList.setValue(vipBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });

    }
    //获取优惠券
    public void getCoupon(CouponCodeBean couponCodeBean) {

        model.CONVERSION(UriUtils.getRequestPostUrl(couponCodeBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("获取优惠券返回结果-----已解析",toPrettyFormat(submitJson));
                        //清除列表
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            CouponBean couponBean = AppApplication.gson.fromJson(submitJsonData, CouponBean.class);
                            //接口没给id
                            couponBean.setId(couponBean.getCoupon_id());
                            uc.CouponList.setValue(couponBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });

    }


    /**
     * 获取条目下标
     *
     * @param MainItemViewModel
     * @return
     */
    public int getItemPosition(MainItemViewModel MainItemViewModel) {
        return observableList.indexOf(MainItemViewModel);
    }
    /**
     * 获取条目下标
     *
     * @param MainItemViewModel
     * @return
     */
    public int getItemPosition(MainGoodItemViewModel MainItemViewModel) {
        return observableList.indexOf(MainItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param viewPagerItemViewModel
     * @return
     */
    public int getItemPosition(ViewPagerItemViewModel viewPagerItemViewModel) {
        return items.indexOf(viewPagerItemViewModel);
    }
}
