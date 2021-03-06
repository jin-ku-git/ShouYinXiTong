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

    //    ???????????????????????????????????????????????????
    private int pages = 1;
    private int sizes = 100;

    List listData=new ArrayList();//????????????
    List<YWGoodBean> listDataTwo=new ArrayList();//??????????????????
    List<MealsItemBean> listMeals =new ArrayList();//??????????????????

    //?????????????????????
    public SingleLiveEvent<GoodsTypesBean.GoodsTypeBean> goodsTypeBeanSingleLiveEvent = new SingleLiveEvent<>();

    //?????????????????????
    public SingleLiveEvent<YWGoodBean> ywGoodBeanSingleLiveEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<MealsItemBean> mealsItemBeanSingleLiveEvent = new SingleLiveEvent<>();
    //??????????????????
    public SingleLiveEvent<String> searchLiveEvent = new SingleLiveEvent<>();

    public MainViewModel(@NonNull Application application, DemoRepository model) {
        super(application, model);
        //??????3???ViewPager??????
        for (int i = 1; i <= 2; i++) {

            ViewPagerItemViewModel itemViewModel = new ViewPagerItemViewModel(this, observableList,goodsList,goodsLists,i);
            items.add(itemViewModel);
        }
    }

    //??????????????????????????????????????????
    public UIChangeObservable uc = new UIChangeObservable();
    public class UIChangeObservable {
        //?????????????????????
        public SingleLiveEvent<Integer> StoreEvent = new SingleLiveEvent<>();

        //???????????????
        public SingleLiveEvent<GoodsListBean> goodsListBeanEvent = new SingleLiveEvent<>();
        //???????????????
        public SingleLiveEvent<ArrayList<GoodsTypesBean.GoodsTypeBean>> goodsList = new SingleLiveEvent<>();
        //?????????????????????
        public SingleLiveEvent<List<YWGoodBean>> goodsListTwo = new SingleLiveEvent<>();
        //?????????????????????
        public SingleLiveEvent<List<MealsItemBean>> goodsListMeals = new SingleLiveEvent<>();
        //????????????
        public SingleLiveEvent<ActivityListBean> activityList = new SingleLiveEvent<>();
        //vip
        public SingleLiveEvent<VIPBean> vipBeanList = new SingleLiveEvent<>();
        //?????????
        public SingleLiveEvent<CouponBean> CouponList = new SingleLiveEvent<>();
    }

    /**
     * ??????
     */
    //???RecyclerView??????ObservableList
    public ObservableList<MainItemViewModel> observableList = new ObservableArrayList<>();
    /**
     * ??????
     */
    //???RecyclerView??????goodsList
    public ObservableList<MainGoodItemViewModel> goodsList = new ObservableArrayList<>();
    /**
     * ???????????????
     */
    //???RecyclerView??????goodsLists
    public ObservableList<MainGoodItemViewModel> goodsLists = new ObservableArrayList<>();

    //???ViewPager??????items
    public ObservableList<ViewPagerItemViewModel> items = new ObservableArrayList<>();
    //???ViewPager??????ItemBinding
    public ItemBinding<ViewPagerItemViewModel> itemBindings = ItemBinding.of(BR.viewModel, R.layout.item_viewpager);


    //??????????????????
    public BindingCommand fun_dialog = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(1);
        }
    });

    //????????????
    public BindingCommand imgSynconsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //??????????????????
            getCategoryList();
            uc.StoreEvent.setValue(2);
        }
    });
    //????????????
    public BindingCommand ChoiceVIPonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startActivity(SearchVipActivity.class);
        }
    });
    //???????????????
    public BindingCommand clearonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(3);
        }
    });
    //??????
    public BindingCommand restingOrderonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(4);
        }
    });
    //??????
    public BindingCommand getrestingonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(5);
        }
    });
    //??????
    public BindingCommand searchOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(6);
        }
    });
    //????????????
    public BindingCommand cancelSearchOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            uc.StoreEvent.setValue(7);
        }
    });

    //?????????????????????
    public BindingCommand CouponsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(8);
        }
    });

    //???????????????
    public BindingCommand useronClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.StoreEvent.setValue(9);
        }
    });

    //????????????
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(CashierActivity.class);
        }
    });


    //??????????????????
    public void getCategoryList() {

            model.CATEGORY_LIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                    .compose(RxUtils.schedulersTransformer()) //????????????
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
                            Log.e("??????????????????????????????-----?????????",toPrettyFormat(submitJson));
                            //????????????

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
                                    //????????????????????????Item
                                    observableList.add(itemViewModel);
                                    listData.add(entity);
                                }
                                ArrayList<GoodsTypesBean.GoodsTypeBean> categoryBeans = (ArrayList<GoodsTypesBean.GoodsTypeBean>) listData;
                                categoryBeans.get(0).currentSelect=1;

                                uc.goodsList.setValue(categoryBeans);
                                if (categoryBeans.size() == 0) {
                                    ToastUtil.showShortToast("????????????");
                                    return;
                                }

                                // todo:????????????????????????????????????sp??????
                                String categories = JSON.toJSONString(categoryBeans);
                                AppApplication.spUtils.put("category", categories);
                                //??????????????????
                                getMealGoodsType();
                            }else {
                                RxToast.normal(response.getMessage());
                            }
                        }
                        @Override
                        public void onError(Throwable throwable) {
                            //???????????????
                            dismissDialog();
                            if (throwable instanceof ResponseThrowable) {
                                ToastUtils.showShort(((ResponseThrowable) throwable).message);
                            }
                        }
                        @Override
                        public void onComplete() {
                            //???????????????
//                            dismissDialog();
                        }
                    });
    }

    /**
     * ??????????????????
     */
    private void getMealGoodsType() {
        model.MEALGOODSTYPE(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //????????????
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
                        Log.e("??????????????????????????????-----?????????",toPrettyFormat(submitJson));

                        List listData=new ArrayList();
                        //????????????
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response);

                            GoodsTypesBean goodsTypesBean= JSON.parseObject(toPrettyFormat(submitJsonData), GoodsTypesBean.class);
                            for (GoodsTypesBean.GoodsTypeBean entity : goodsTypesBean.getData()) {
                                MainItemViewModel itemViewModel = new MainItemViewModel(MainViewModel.this, entity);
                                //????????????????????????Item
                                observableList.add(itemViewModel);

                                listData.add(entity);
                            }
                            //??????????????????
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
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
//                        dismissDialog();
                    }
                });
    }

    /**
     *??????????????????
     * @param page
     * @param size
     */
    public void getGoodsList(String page, String size) {
        GoodsRequestBean goodsRequestBean=new GoodsRequestBean();

        goodsRequestBean.setPageNumber(page);
        goodsRequestBean.setPageSize(size);
        model.PAGE(UriUtils.getRequestPostUrl(goodsRequestBean))
                .compose(RxUtils.schedulersTransformer()) //????????????
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


                        //????????????
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);

                            GoodsListBean goodsListBean= JSON.parseObject(toPrettyFormat(submitJsonData), GoodsListBean.class);

                            if (listDataTwo!=null){
                                listDataTwo.clear();
                            }
                            for (YWGoodBean entity : goodsListBean.getItems()) {
//                                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(MainViewModel.this, entity);
//                                //????????????????????????Item
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
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
//                        dismissDialog();
                    }
                });
    }

    /**
     * ??????????????????
     */
    public void getStoreMealSetGoodsList() {

        model.MEALGOODSLIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //????????????
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

                        //????????????
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);

                            List<MealsItemBean> list=JSON.parseObject(submitJsonData,  new TypeToken<List<MealsItemBean>>(){}.getType());

                            listMeals.addAll(list);
//                            for (MealsItemBean entity : list) {
//                                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(MainViewModel.this, entity);
//                                //????????????????????????Item
//                                goodsList.add(itemViewModel);
//                            }
                            uc.goodsListMeals.setValue(listMeals);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
//                        dismissDialog();
                    }
                });

    }

    //????????????
    public void getActivitList() {

        model.LIST(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //????????????
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
                        Log.e("????????????????????????-----?????????",toPrettyFormat(submitJson));
                        //????????????
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
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
                        dismissDialog();
                    }
                });
    }

    //???????????????/IC???????????????
    public void getVIPbyICCard(String card) {

        Log.e("scanToWork", "card" + card);

        VipCardQequestBean vipCardQequestBean = new VipCardQequestBean();
        vipCardQequestBean.setIc_card(card);

        model.PAYMENT_CARD_TWO(UriUtils.getRequestPostUrl(vipCardQequestBean))
                .compose(RxUtils.schedulersTransformer()) //????????????
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
                        Log.e("???????????????/IC???????????????????????????-----?????????",toPrettyFormat(submitJson));
                        //????????????
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
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
                        dismissDialog();
                    }
                });

    }
    //???????????????????????????
    public void getVIPbyCode(String vipCode) {

        Log.e("scanToWork", "card" + vipCode);

        VipCodeQequestBean vipCodeQequestBean = new VipCodeQequestBean();
        vipCodeQequestBean.setPayment_code(vipCode);

        model.PAYMENT_CODE(UriUtils.getRequestPostUrl(vipCodeQequestBean))
                .compose(RxUtils.schedulersTransformer()) //????????????
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
                        Log.e("????????????????????????-----?????????",toPrettyFormat(submitJson));
                        //????????????
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
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
                        dismissDialog();
                    }
                });

    }
    //???????????????
    public void getCoupon(CouponCodeBean couponCodeBean) {

        model.CONVERSION(UriUtils.getRequestPostUrl(couponCodeBean))
                .compose(RxUtils.schedulersTransformer()) //????????????
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
                        Log.e("???????????????????????????-----?????????",toPrettyFormat(submitJson));
                        //????????????
                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            CouponBean couponBean = AppApplication.gson.fromJson(submitJsonData, CouponBean.class);
                            //????????????id
                            couponBean.setId(couponBean.getCoupon_id());
                            uc.CouponList.setValue(couponBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //???????????????
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //???????????????
                        dismissDialog();
                    }
                });

    }


    /**
     * ??????????????????
     *
     * @param MainItemViewModel
     * @return
     */
    public int getItemPosition(MainItemViewModel MainItemViewModel) {
        return observableList.indexOf(MainItemViewModel);
    }
    /**
     * ??????????????????
     *
     * @param MainItemViewModel
     * @return
     */
    public int getItemPosition(MainGoodItemViewModel MainItemViewModel) {
        return observableList.indexOf(MainItemViewModel);
    }

    /**
     * ??????????????????
     *
     * @param viewPagerItemViewModel
     * @return
     */
    public int getItemPosition(ViewPagerItemViewModel viewPagerItemViewModel) {
        return items.indexOf(viewPagerItemViewModel);
    }
}
