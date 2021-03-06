package com.youwu.shouyinxitong.ui.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.KeyboardUtils;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.bean_new.UserBean;
import com.youwu.shouyinxitong.bean_used.ActivityListBean;
import com.youwu.shouyinxitong.bean_used.BackupCashRecordBean;
import com.youwu.shouyinxitong.bean_used.CouponBean;
import com.youwu.shouyinxitong.bean_used.CouponCodeBean;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean_used.OrderNumberBean;
import com.youwu.shouyinxitong.bean_used.RestingInfoBean;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.databinding.ActivityMainBinding;
import com.youwu.shouyinxitong.db.GoodsDao;
import com.youwu.shouyinxitong.db.OrdersDao;
import com.youwu.shouyinxitong.dialog.BackupCashDialog;
import com.youwu.shouyinxitong.dialog.CouponDialog;
import com.youwu.shouyinxitong.dialog.MainFunDialog;
import com.youwu.shouyinxitong.event.SystemMessageEvent;
import com.youwu.shouyinxitong.presenter.PrinterPresenter;
import com.youwu.shouyinxitong.ui.login.LoginActivity;
import com.youwu.shouyinxitong.ui.main.adapter.ViewPagerBindingAdapter;
import com.youwu.shouyinxitong.ui.order.RestingListActivity;
import com.youwu.shouyinxitong.ui.search.SearchVipActivity;
import com.youwu.shouyinxitong.ui.vip.AddVIPActivity;
import com.youwu.shouyinxitong.utils_tool.BeanCloneUtil;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;
import com.youwu.shouyinxitong.utils_tool.MainAboutUtils;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.utils_tool.ScanUtils;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;
import com.youwu.shouyinxitong.view.PopDialog;
import com.youwu.shouyinxitong.view.ScreenManager;
import com.youwu.shouyinxitong.view.ShowShopingDisplay;
import com.youwu.shouyinxitong.viewpager.VerticalVpAdapter;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.PosPrinterDev;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 2021/10/21.
 * ??????
 */

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements ScanUtils.OnResultListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static Context context;
    //    ?????????
    public GoodsDao goodsDao;
    private OrdersDao ordersDao;

    public static boolean ISCONNECT = false;

    //?????????????????????
    private int currentType = 0;
    private int pages = 1;//??????
    private int sizes = 100;//???????????????

    private List<GoodsTypesBean.GoodsTypeBean> goodsTypeBeans = new ArrayList<>();//????????????

    private GoodsTypesBean.GoodsTypeBean goodsTypeBean;//

    private List<YWGoodBean> list = new ArrayList<>();//??????????????????
    private List<MealsItemBean> listMeals = new ArrayList<>();//??????????????????
    private List YWGoodBeans = new ArrayList();

    private List shopCarYWGoodBeans = new ArrayList();//???????????????????????????
    private ShopCarGoodsAdapter shopCarGoodsAdapter;


    private float totalMoney;//????????????
    private float disCountMoney;//??????????????????????????????
    private float couponMoney;//????????????????????????

    private CouponBean couponBean;//??????????????????
    private CouponBean coupon;//?????????????????????
    private VIPBean vipBean;//???????????????vip


    //??????
    private ScreenManager screenManager = ScreenManager.getInstance();
    //??????
    private ShowShopingDisplay showShopingDisplay = null;
    //???????????????  true  ??????  false ??????
    private boolean wetherHorizontalScreen = true;
    public static final String noscanSKU = "0000000000000";//???????????????SKU
    private PopDialog popDialog;
    //    ???????????? ???????????????????????????
    private TextView edtKeymapView;
    //????????????
    private boolean isRestingBack = false;//????????????????????????????????????????????????
    //    ??????????????????????????????
    private OrderNumberBean orderNumberBean;
    private ActivityListBean activityListBean;

    //????????????
    private SunmiPrinterService woyouService = null;//?????????????????? ????????????
    public static PrinterPresenter printerPresenter;


    private BackupCashDialog backupCashDialog;//??????
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        //??????????????????ViewModelFactory?????????ViewModel????????????????????????????????????????????????LoginViewModel(@NonNull Application application)????????????
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }


    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        context = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //????????? goodsBean ?????????
        goodsDao = new GoodsDao(this);
        boolean dataExist = goodsDao.isDataExist();
        if (dataExist) {
            goodsDao.deleteAllData();
        }

        ordersDao = new OrdersDao(this);

        //???????????????
        wetherHorizontalScreen = MainAboutUtils.getWetherHorizontalScreen(this);

        //????????????????????????
        try {
            //????????????????????????
            Intent intent = new Intent(this, PosprinterService.class);
            bindService(intent, mSerconnection, BIND_AUTO_CREATE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connectUSB();
                }
            }).start();
        } catch (Exception e) {
            RxToast.normal("???????????????USB?????????");
        }

//        binding.goodsTypeList.addItemDecoration(new GridDividerItemDecoration(this, 8, 1, getResources().getColor(R.color.white)));
//
//        binding.goodsList.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));




        //??????????????????
        viewModel.getCategoryList();

        //?????????
        initRecyclerShopping();

        //??????????????????
        connectPrintService();


//        binding.vpV.setAdapter(new ViewPagerBindingAdapter());
        binding.vpV.setOrientation(ViewPager2.ORIENTATION_VERTICAL);


    }



    //?????????AddVIPActivity????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UserBeanEventBus(UserBean goodsEntity) { //????????????????????????????????????  EventBus.getDefault().post(111);????????????
        System.out.println("------>"+goodsEntity);

        if (goodsEntity.getUser_type()==1){//????????????
            viewModel.userBean.set(goodsEntity);
        }else if (goodsEntity.getUser_type()==2){//??????????????????
            viewModel.userBean.set(null);
        }

    }


    //??????????????????
    private void connectPrintService() {
        try {

            InnerPrinterManager.getInstance().bindService(this, innerPrinterCallback);

        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            woyouService = service;
            printerPresenter = new PrinterPresenter(MainActivity.this, woyouService);
        }

        @Override
        protected void onDisconnected() {
            woyouService = null;

        }
    };

    private void initRecyclerShopping() {
        binding.shopCarList.setLayoutManager(new LinearLayoutManager(this));
        shopCarGoodsAdapter = new ShopCarGoodsAdapter(this, shopCarYWGoodBeans);
        binding.shopCarList.swapAdapter(shopCarGoodsAdapter, true);
        shopCarGoodsAdapter.bindToRecyclerView(binding.shopCarList);
        shopCarGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position >= 0 && position < shopCarYWGoodBeans.size()) {

                    Intent intent = new Intent(context, GoodsDetailsActivity.class);
                    intent.putExtra("data", (Serializable) shopCarYWGoodBeans.get(position));
                    intent.putExtra("isCar", 1);
                    startActivityForResult(intent, 2002);
                }
            }
        });
        shopCarGoodsAdapter.setOnClickViewLister(new ShopCarGoodsAdapter.OnClickViewLister() {
            @Override
            public void addGoodsListering(YWGoodBean item, int position) {
            }

            @Override
            public void removeGoodsListering(Object item, int position) {
                if (position == -1) {
                    return;
                }
                if (shopCarYWGoodBeans.get(position) instanceof YWGoodBean) {//?????????????????????  ????????????
                    int buyNum = ((YWGoodBean) shopCarYWGoodBeans.get(position)).getBuynum() - 1;
                    ((YWGoodBean) shopCarYWGoodBeans.get(position)).setBuynum(buyNum);
                    if (showShopingDisplay != null) {
                        showShopingDisplay.removeGoods(((YWGoodBean) shopCarYWGoodBeans.get(position)));
                    }
                    if (buyNum == 0) {
                        shopCarGoodsAdapter.remove(position);

                    } else {
                        shopCarGoodsAdapter.notifyDataSetChanged();
                    }
                } else {
                    shopCarYWGoodBeans.remove(item);
                    if (showShopingDisplay != null) {
                        showShopingDisplay.remove(position);
                    }
                    shopCarGoodsAdapter.notifyDataSetChanged();
                }

                //?????? ??????????????? ?????????
                countMoney();
            }

            @Override
            public void removeItemGoodsListering(Object item, int position) {
                if (position < 0 || position >= shopCarYWGoodBeans.size()) {
                    return;
                }
                if (shopCarYWGoodBeans.get(position) instanceof YWGoodBean) {//?????????????????????  ????????????
                    int buyNum = 0;
                    ((YWGoodBean) shopCarYWGoodBeans.get(position)).setBuynum(buyNum);
                    if (showShopingDisplay != null) {
                        showShopingDisplay.removeGoods(((YWGoodBean) shopCarYWGoodBeans.get(position)));
                    }
                    if (buyNum == 0) {
                        shopCarGoodsAdapter.remove(position);
                    } else {
                        shopCarGoodsAdapter.notifyDataSetChanged();
                    }
                } else {
                    shopCarYWGoodBeans.remove(item);
                    if (showShopingDisplay != null) {
                        showShopingDisplay.remove(position);
                    }
                    shopCarGoodsAdapter.notifyDataSetChanged();
                }

                //?????? ??????????????? ?????????
                countMoney();
            }

        });
    }

    //    ??????????????????
    private void initGoodsList(GoodsTypesBean.GoodsTypeBean message) {

        //        ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List list = new ArrayList<>();
        if (message.isMeals) {
            Log.e("????????????id", message.id + "");
            list.addAll(goodsDao.getMealListByCategoryId(AppApplication.subZeroAndDot(message.id)));
        } else {
            Log.e("????????????id", message.id + "");
            list.addAll(goodsDao.getGoodListByCategoryId(message.id));
        }
        Log.e("?????????????????????----->", goodsDao.getAllDate().size() + "");
        Log.e("??????----->", list.size() + "");
        viewModel.goodsList.clear();
        for (Object entity : list) {
            if (entity instanceof YWGoodBean) {
                //??????????????????
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) entity);
                //????????????????????????Item
                viewModel.goodsList.add(itemViewModel);
            } else {
                //??????????????????
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (MealsItemBean) entity, 1);
                //????????????????????????Item
                viewModel.goodsList.add(itemViewModel);

                for (int i = 0; i < ((MealsItemBean) entity).getMeal_store_goods_detail().size(); i++) {
                    MainGoodItemTCViewModel itemViewModels = new MainGoodItemTCViewModel(viewModel, ((MealsItemBean) entity).getMeal_store_goods_detail().get(i));
                    //????????????????????????Item
                    viewModel.goodsList.get(0).goodsList.add(itemViewModels);
                }
            }
        }

        /**
         * ????????????????????????
         */
        YWGoodBean ywGoodBean=new YWGoodBean();
        MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) ywGoodBean);
        viewModel.goodsList.add(itemViewModel);
    }



    @Override
    public void initViewObservable() {

        //????????????
        viewModel.uc.goodsList.observe(this, new Observer<ArrayList<GoodsTypesBean.GoodsTypeBean>>() {
            @Override
            public void onChanged(ArrayList<GoodsTypesBean.GoodsTypeBean> goodsTypeBeanss) {
                goodsTypeBeans.addAll(goodsTypeBeanss);
                goodsTypeBean = goodsTypeBeans.get(0);
                ArrayList<GoodsTypesBean.GoodsTypeBean> listgoods = new ArrayList<>();
                listgoods.addAll(goodsTypeBeans);
//                initRecyclerView(listgoods);
            }
        });
        //??????????????????
        viewModel.uc.goodsListTwo.observe(this, new Observer<List<YWGoodBean>>() {
            @Override
            public void onChanged(List<YWGoodBean> objects) {
                list.addAll(objects);
                goodsDao.initTable(objects);

                if (objects.size() < sizes) {
                    //????????????????????????,??????????????????
                    viewModel.getStoreMealSetGoodsList();
                } else {
                    pages++;
                    viewModel.getGoodsList(pages + "", sizes + "");
                }
            }
        });
        //??????????????????
        viewModel.uc.goodsListMeals.observe(this, new Observer<List<MealsItemBean>>() {
            @Override
            public void onChanged(List<MealsItemBean> mealsItemBeans) {
                listMeals.addAll(mealsItemBeans);
                goodsDao.initMealTable(mealsItemBeans);

                YWGoodBeans.clear();
                viewModel.goodsList.clear();

                YWGoodBeans.addAll(goodsDao.getGoodListByCategoryId(goodsTypeBeans.get(0).id));

                for (Object entity : YWGoodBeans) {
                    MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) entity);
                    //????????????????????????Item
                    viewModel.goodsList.add(itemViewModel);
                }
                /**
                 * ????????????????????????
                 */
                YWGoodBean ywGoodBean=new YWGoodBean();
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) ywGoodBean);
                viewModel.goodsList.add(itemViewModel);

                viewModel.dismissDialog();

                //??????????????????
                viewModel.getActivitList();

            }
        });
        //??????????????????
        viewModel.goodsTypeBeanSingleLiveEvent.observe(this, new Observer<GoodsTypesBean.GoodsTypeBean>() {
            @Override
            public void onChanged(GoodsTypesBean.GoodsTypeBean Bean) {
                goodsTypeBean.setCurrentSelect(0);
                MainItemViewModel itemViewModels = new MainItemViewModel(viewModel, goodsTypeBean);
                viewModel.observableList.set(currentType, itemViewModels);
                currentType = Bean.position;
                Bean.setCurrentSelect(1);
                MainItemViewModel itemViewModel = new MainItemViewModel(viewModel, Bean);
                viewModel.observableList.set(Bean.position, itemViewModel);
//        RxToast.normal("???"+message.position+"???--"+message.name);
                initGoodsList(Bean);

                goodsTypeBean = Bean;
            }
        });
        //??????????????????
        viewModel.ywGoodBeanSingleLiveEvent.observe(this, new Observer<YWGoodBean>() {
            @Override
            public void onChanged(YWGoodBean Bean) {
                Log.e("id---???", Bean.id);

                if (Bean.getType() == 0) {
                    Intent intent = new Intent(context, GoodsDetailsActivity.class);
                    Serializable item = (Serializable) Bean;
                    Serializable ywGoodBean = BeanCloneUtil.cloneTo(item);
                    intent.putExtra("data", ywGoodBean);
                    startActivityForResult(intent, 2001);
                    overridePendingTransition(R.anim.actionsheet_activity_in, R.anim.actionsheet_activity_out);
                } else {
                    addGoodsToCar(Bean, 0);
                }
            }
        });
        viewModel.mealsItemBeanSingleLiveEvent.observe(this, new Observer<MealsItemBean>() {
            @Override
            public void onChanged(MealsItemBean Bean) {
                if (Bean.getType() == 0) {
                    Intent intent = new Intent(context, GoodsDetailsActivity.class);
                    Serializable item = (Serializable) Bean;
                    Serializable ywGoodBean = BeanCloneUtil.cloneTo(item);
                    intent.putExtra("data", ywGoodBean);
                    startActivityForResult(intent, 2001);
                    overridePendingTransition(R.anim.actionsheet_activity_in, R.anim.actionsheet_activity_out);
                } else {
                    addGoodsToCar(Bean, 0);
                }

            }
        });

        //
        viewModel.uc.StoreEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {

                switch (s) {
                    case 1://??????
                        MainFunDialog dialog = new MainFunDialog(context);
                        new XPopup.Builder(context)
                                .asCustom(dialog)
                                .show();
                        break;
                    case 2://????????????
                        currentType = 0;
                        goodsTypeBeans.clear();//????????????
                        YWGoodBeans.clear();//??????????????????
                        goodsDao.deleteAllData();
                        break;
                    case 3://???????????????
                        clearShopCar(1);
                        break;
                    case 4://??????
                        showRestingPop();
                        break;
                    case 5://??????
                        Intent intent = new Intent(MainActivity.this, RestingListActivity.class);
                        intent.putExtra("activityListBean", activityListBean);
                        startActivityForResult(intent, 223);
                        break;

                    case 8://???????????????
                        showCouponList();
                        break;
                    case 9://????????????????????????
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("user", viewModel.userBean.get());
                        bundle.putInt("type", 2);
                        startActivity(AddVIPActivity.class,bundle);
                        break;
                }
            }
        });
        viewModel.uc.activityList.observe(this, new Observer<ActivityListBean>() {
            @Override
            public void onChanged(ActivityListBean activityListBeans) {
                activityListBean = activityListBeans;
            }
        });
        //vip
        viewModel.uc.vipBeanList.observe(this, new Observer<VIPBean>() {
            @Override
            public void onChanged(VIPBean vipBeans) {
                vipBean = vipBeans;

                showVip();
            }
        });
        //?????????
        viewModel.uc.CouponList.observe(this, new Observer<CouponBean>() {
            @Override
            public void onChanged(CouponBean couponBean) {
                if (shopCarYWGoodBeans.size() == 0) { //????????????????????????0??????????????????????????????
                    ToastUtil.showShortToast("???????????????????????????????????????");
                } else {
                    coupon = couponBean;

                    if (coupon.getPayment_code_num() != "") {
                        viewModel.getVIPbyCode(coupon.getPayment_code_num() + "");
                    } else {
                        ToastUtil.showShortToast("?????????????????????????????????????????????????????????????????????????????????????????????!");
                    }

                }
            }
        });
        //??????????????????
        viewModel.searchLiveEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.goodsLists.clear();
                for (int i=0;i<10;i++){
                    MealsItemBean mealsItemBean=new MealsItemBean();
                    mealsItemBean.setMeal_goods_name("??????"+i);
                    mealsItemBean.setMeal_goods_price("1"+i);
                    mealsItemBean.setMeal_goods_sku("1"+i);
                    List<MealGoodsBean> list=new ArrayList<>();
                    for (int j=0;j<2;j++){
                        MealGoodsBean mealGoodsBean=new MealGoodsBean();
                        mealGoodsBean.setGoods_name("??????"+i+"--"+j);
                        list.add(mealGoodsBean);
                    }
                    mealsItemBean.setMeal_store_goods_detail(list);

                    MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (MealsItemBean) mealsItemBean, 1);
                    //????????????????????????Item
                    viewModel.goodsLists.add(itemViewModel);
                }

            }
        });
        //ViewPager????????? ????????????
        viewModel.items.get(1).stringEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RxToast.normal("??????"+s);
            }
        });
    }

    //    ???????????????
    private void clearShopCar(int type) {


        isRestingBack = false;//???????????????????????????????????????????????????????????????????????????????????????????????????

        cancleVip();
        //???????????????
        shopCarYWGoodBeans.clear();
        if (showShopingDisplay != null) {
            showShopingDisplay.cleanShopCar();
        }
        shopCarGoodsAdapter.notifyDataSetChanged();
//        activitBean = null;//??????????????????
        //??????????????????????????????
        if (type == 0) {
            viewModel.getCategoryList();
            initGoodsList(goodsTypeBean);//??????????????????

        }


        showBt("0");
        //?????? ??????????????? ?????????    ??????????????????????????????????????????????????????????????????????????????????????????
        countMoney();
        //???????????????
        if (showShopingDisplay != null) {
            showShopingDisplay.initBannnerView();
        }
    }


    public static IMyBinder myBinder;
    ServiceConnection mSerconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (IMyBinder) service;
            Log.e("myBinder", "connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    /**
     * ??????usb
     */
    private void connectUSB() {
        List<String> usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList != null) {
            String usbAddress = usbList.get(0);
            if (usbAddress.equals(null) || usbAddress.equals("")) {
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
            } else {
                myBinder.ConnectUsbPort(getApplicationContext(), usbAddress, new TaskCallback() {
                    @Override
                    public void OnSucceed() {
                        ISCONNECT = true;
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnFailed() {
                        ISCONNECT = false;
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            ISCONNECT = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 222://?????????????????????vip ??????
                    vipBean = data.getParcelableExtra("vipbean");
                    showVip();
                    break;
                case 223://????????????????????????????????????????????????

                    isRestingBack = true;
                    RestingInfoBean restingInfoBean = (RestingInfoBean) data.getSerializableExtra("RestingInfoBean");
                    orderNumberBean = restingInfoBean.getOrderNumberBean();
                    shopCarYWGoodBeans.clear();
                    shopCarYWGoodBeans.addAll(restingInfoBean.getShopCarYWGoodBeans());

                    if (showShopingDisplay != null) {
                        showShopingDisplay.setList(shopCarYWGoodBeans);
                    }
                    shopCarGoodsAdapter.notifyDataSetChanged();

                    showBt("1");
                    countMoney();

                    if (restingInfoBean.getBean() != null) {
                        vipBean = restingInfoBean.getBean();
                        showVip();
                    }
                    break;
                case 2001://?????????????????????
                    Object good = data.getSerializableExtra("data");
                    addGoodsToCar(good, 1);
                    break;
                case 2002://???????????????
                    Object good2 = data.getSerializableExtra("data");
                    addGoodsToCar(good2, 2);
                    break;
                case 2003://??????????????????
                    this.vipBean = (VIPBean) data.getSerializableExtra("data");
                    showVip();
                    break;
            }
        } else if (resultCode == 222) {
            cancleVip();
        } else if (resultCode == 666) {
            //??????????????????
            clearShopCar(1);
        } else if (resultCode == 223) {
            //????????????????????????vip
            this.vipBean = (VIPBean) data.getSerializableExtra("data");
            showVip();

        } else if (resultCode == 224) {
            //??????????????????????????????vip ??? coupon
            this.vipBean = (VIPBean) data.getSerializableExtra("vipBean");
            this.couponBean = this.coupon = (CouponBean) data.getSerializableExtra("coupon");
            showVip();
            countMoney();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    ????????????vip
    private void cancleVip() {
        vipBean = null;
        binding.vipLayout.setVisibility(View.GONE);
        if (showShopingDisplay != null) {
            showShopingDisplay.cancleCoupon();
        }

        //???????????????????????? ===============start=============


        setYouHuiQuan();
        isHasQuan = false;
        coupon = null;
        couponBean = null;

        countMoney();
        //???????????????????????? ===============end=============

        if (showShopingDisplay != null) {
            showShopingDisplay.cancleCoupon();
        }

        if (!wetherHorizontalScreen) {
            binding.cancelVip.setVisibility(View.GONE);
            binding.vipName.setText("");
            Glide.with(this)
                    .load("")
                    .apply(AppApplication.options)
                    .into(binding.vipHeadImage);
        }
        binding.layoutVipSelect.setVisibility(View.VISIBLE);
    }


    /**
     * @param bean
     * @param from 0 ?????????????????? 1 ???????????????????????? 2 ?????????
     */
    public void addGoodsToCar(Object bean, int from) {
        if (bean instanceof YWGoodBean) {
            YWGoodBean ywGoodBean = BeanCloneUtil.cloneTo((YWGoodBean) bean);
            if (from == 0) {
                ywGoodBean.setBuynum(1);
            }

            if (ywGoodBean.getSales_status().equals("2")) {
                ToastUtil.showLongToast("???????????????");
                return;
            }
            int hasInShopCar = -1;
            for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
                if (shopCarYWGoodBeans.get(i) instanceof YWGoodBean) {
                    if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getSku(), ywGoodBean.getSku())) {
                        //???????????????
                        if (from == 2) {
                            //?????????????????????????????????
                            hasInShopCar = i;
                            break;
                        } else {
                            //????????????????????????????????????
                            if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getDiscount(), ywGoodBean.getDiscount())) {
                                hasInShopCar = i;
                            }
                        }
                    }
                }
            }

            if (hasInShopCar == -1) {
//                ywGoodBean.weightnum=weight_d;
//                ywGoodBean.weight_type=weight_type;
                shopCarYWGoodBeans.add(ywGoodBean);

            } else {
                //?????????????????????????????????????????????
                if (from == 1 || from == 0) {
                    //goods_type==1  1?????????2?????????
                    if (((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).goods_type == 2) {
                        ToastUtil.showShortToast("?????????????????????????????????????????????");
                    } else {
//                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).weightnum=weight_d;
//                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).weight_type=weight_type;
                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar))
                                .setBuynum(((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).getBuynum() + ywGoodBean.getBuynum());
                    }
                } else {
//                    ((YWGoodBean) bean).weightnum=weight_d;
//                    ((YWGoodBean) bean).weight_type=weight_type;
                    //??????
                    shopCarGoodsAdapter.setData(hasInShopCar, bean);
                }
            }
        } else {
            //???????????????
            MealsItemBean mealsItemBean = BeanCloneUtil.cloneTo((MealsItemBean) bean);
            if (from == 0) {
                mealsItemBean.setBuynum(1);
            }

            int hasInShopCar = -1;
            for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
                if (shopCarYWGoodBeans.get(i) instanceof MealsItemBean) {
                    if (TextUtils.equals(((MealsItemBean) shopCarYWGoodBeans.get(i)).getMeal_goods_sku(), mealsItemBean.getMeal_goods_sku())) {
                        //???????????????
                        hasInShopCar = i;
                        break;
                    }
                }
            }
            if (hasInShopCar == -1) {
                shopCarYWGoodBeans.add(mealsItemBean);
            } else {
                //?????????????????????????????????????????????
                if (from == 1 || from == 0) {
                    ((MealsItemBean) shopCarYWGoodBeans.get(hasInShopCar))
                            .setBuynum(((MealsItemBean) shopCarYWGoodBeans.get(hasInShopCar)).getBuynum() + mealsItemBean.getBuynum());
                } else {
                    //??????
                    shopCarGoodsAdapter.setData(hasInShopCar, bean);
                }
            }
        }
        if (showShopingDisplay != null) {
            showShopingDisplay.setList(shopCarYWGoodBeans);
        }
        shopCarGoodsAdapter.notifyDataSetChanged();
        //?????? ??????????????? ?????????
        countMoney();
        showBt("1");
    }

    boolean isHasQuan = false;

    //?????? ??????????????? ?????????
    private void countMoney() {
        Log.d("?????????", "1");
        int buyCount = 0;//??????
        //?????????
        totalMoney = 0f;
        //???????????????????????????????????????
        disCountMoney = 0f;
        //????????????
        couponMoney = 0f;

        double freeMoney = 0f;

        if (shopCarYWGoodBeans.size() > 0) {
//            binding.clear.setText(getString(R.string.empty_goods)+"(" + shopCarYWGoodBeans.size() + ")");
            binding.clear.setText(String.format(
                    getString(R.string.empty_goods),
                    shopCarYWGoodBeans.size()+""));
        } else {
            binding.clear.setText(R.string.empty);
        }

        for (Object shopCarYWGoodBean : shopCarYWGoodBeans) {
            if (shopCarYWGoodBean instanceof YWGoodBean) {//????????????  ?????????????????????
                Log.d("?????????", "2");
//            ????????????  ??????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????????????????????????????saas??????
                YWGoodBean goodBean = (YWGoodBean) shopCarYWGoodBean;
                double itemmoney;
                if (goodBean.goods_type == 2) {
                    itemmoney = Double.parseDouble(goodBean.getPrice()) * goodBean.weightnum * Double.parseDouble(goodBean.getDiscount()) / 100;
                } else {
                    itemmoney = Double.parseDouble(goodBean.getPrice()) * Double.parseDouble(goodBean.getDiscount()) * goodBean.getBuynum() / 100;

                }
                //????????????????????????
                double originItmemoney;
                if (goodBean.goods_type == 2) {
                    originItmemoney = Double.parseDouble(goodBean.getPrice()) * goodBean.weightnum;

                } else {
                    originItmemoney = Double.parseDouble(goodBean.getPrice()) * goodBean.getBuynum();
                }


                totalMoney += itemmoney;                             //??????
                disCountMoney += originItmemoney;                    //?????????????????????
                goodBean.setTotalmoney(YWStringUtils.getStanMoney((float) itemmoney));//????????????
                buyCount += goodBean.getBuynum();
            } else {
                MealsItemBean mealsItemBean = (MealsItemBean) shopCarYWGoodBean;
                double itemmoney = Double.parseDouble(mealsItemBean.getMeal_goods_price()) * mealsItemBean.getBuynum();
                totalMoney += itemmoney;                             //??????
                disCountMoney += itemmoney;                    //?????????????????????
                buyCount += mealsItemBean.getBuynum();
            }
        }
        //
        Double totalDiscount = BigDecimalUtils.subtract(disCountMoney, totalMoney);
        binding.tvDiscountMoney.setText("?????????" + BigDecimalUtils.formatRoundUp(totalDiscount, 2));
        if (showShopingDisplay != null) {
            showShopingDisplay.countMoney(disCountMoney, disCountMoney, buyCount, freeMoney, BigDecimalUtils.formatRoundUp(totalDiscount, 2));
        }
        //???????????????  ????????????????????????????????????????????????
        if (coupon != null) {
            if (showShopingDisplay != null) {
                showShopingDisplay.setCoupon(coupon);
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long end = 0L;
            Long start = 0L;
            try {
                end = sf.parse(coupon.getEndTime()).getTime();// ????????????????????????
                start = sf.parse(coupon.getStartTime()).getTime();// ????????????????????????
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() > end) {
                ToastUtil.showLongToast("????????????????????????");
                couponBean = null;
                coupon = null;
                if (showShopingDisplay != null) {
                    showShopingDisplay.cancleCoupon();
                }
                setYouHuiQuan();
            } else if (System.currentTimeMillis() < start) {
                ToastUtil.showLongToast("??????????????????????????????");
                couponBean = null;
                coupon = null;
                if (showShopingDisplay != null) {
                    showShopingDisplay.cancleCoupon();
                }
                setYouHuiQuan();
            } else {//???????????????????????????????????????????????????
                switch (coupon.getType()) {
                    case "0"://?????????
                        if (Float.parseFloat(coupon.getFull_price()) > totalMoney) {
                            ToastUtil.showLongToast("????????????????????????????????????");
                            couponBean = null;
                            coupon = null;
                            if (showShopingDisplay != null) {
                                showShopingDisplay.cancleCoupon();
                            }
                            setYouHuiQuan();
                        } else {
                            couponBean = coupon;
                            if (showShopingDisplay != null) {
                                showShopingDisplay.setCoupon(couponBean);
                            }
                            binding.couponStr.setText("???????????? " + couponBean.getName());
                            freeMoney = freeMoney + Double.parseDouble(couponBean.getSubtravt());
                        }
                        if (couponBean != null) {
                            couponMoney = disCountMoney - Float.parseFloat(couponBean.getSubtravt());
                        }
                        break;
                    case "1"://?????????
                        couponBean = coupon;
                        if (showShopingDisplay != null) {
                            showShopingDisplay.setCoupon(couponBean);
                        }
                        Double multiply = BigDecimalUtils.multiply(totalMoney + "", couponBean.getDiscount_price());
                        Double zhekou = BigDecimalUtils.divide(multiply, 100);
                        Double jianmian = BigDecimalUtils.formatRoundUp(BigDecimalUtils.subtract(totalMoney, zhekou), 2);

                        freeMoney = jianmian;
                        binding.couponStr.setText("???????????? " + couponBean.getName());
                        couponMoney = totalMoney - Float.parseFloat(jianmian + "");
//                        }
                        break;
                    case "5"://?????????
                        couponBean = coupon;
                        YWGoodBean ywGoodBean = goodsDao.getGoodInfoByGoodSku(couponBean.getGoods_sku());
                        if (ywGoodBean == null) {
                            couponBean = null;
                            coupon = null;
                            ToastUtil.showLongToast("?????????????????????????????????????????????!");
                            if (showShopingDisplay != null) {
                                showShopingDisplay.cancleCoupon();
                            }
                            setYouHuiQuan();
                        }
                        break;
                }
            }
            isHasQuan = true;
        } else {//?????????????????????    ??????????????????????????????????????????
            if (disCountMoney == 0) {
                disCountMoney = totalMoney;
            }
            couponMoney = disCountMoney;
            isHasQuan = false;
            Log.d("?????????", "1");
        }
        Log.d("?????????", "7");
        binding.tvBuyCount.setText("???" + buyCount + "???");
        if (couponMoney < 0) {
            couponMoney = 0;
        }
        //??????????????????????????????   ?????????    ??????????????????????????????
        Log.e("---gu????????????", "totalMoney:" + totalMoney + ",disCountMoney:" + disCountMoney + ",couponMoney:" + couponMoney + ",freeMoney:" + freeMoney);
        setDiscountMoney("?????????", freeMoney);
        if (showShopingDisplay != null) {
            showShopingDisplay.countMoney(disCountMoney, disCountMoney, buyCount, freeMoney, BigDecimalUtils.formatRoundUp(totalDiscount, 2));
        }
        if (shopCarYWGoodBeans.size() == 0) {
            if (showShopingDisplay != null) {
                showShopingDisplay.initBannnerView();
            }
            showBt("0");
        }
    }

    /**
     * ??????????????????
     *
     * @param s
     * @param freeMoney
     */
    private void setDiscountMoney(String s, double freeMoney) {

        binding.tvTotalMoney.setText("??????" + YWStringUtils.getStanMoney((float) disCountMoney));
        if ((float) totalMoney < (float) (freeMoney)) {
            binding.tvCouponPrice.setText("?????????" + totalMoney);
            binding.tvPayMoney.setText("0 ???");
        } else {
            binding.tvCouponPrice.setText("?????????" + BigDecimalUtils.formatRoundUp(freeMoney, 2));
            binding.tvPayMoney.setText("?????????" + YWStringUtils.getStanMoney((float) totalMoney - (float) (freeMoney)));
        }
    }

    //?????????????????????
    @SuppressLint("CheckResult")
    private void showRestingPop() {
        if (popDialog != null && popDialog.isShowing()) {
            popDialog.dismiss();
        }
        View view = View.inflate(this, R.layout.pop_resting, null);

        ImageView closed = (ImageView) view.findViewById(R.id.closed);
        edtKeymapView = (TextView) view.findViewById(R.id.price);

        //                ???????????????
        if (!isRestingBack) {//???????????????????????????????????????
            edtKeymapView.setText(initOrderNumber());
        } else {//??????????????????????????????????????????

            edtKeymapView.setText(orderNumberBean.getOrderNumberStr());

        }

        TextView keymap1 = view.findViewById(R.id.keymap_1);
        TextView keymap2 = view.findViewById(R.id.keymap_2);
        TextView keymap3 = view.findViewById(R.id.keymap_3);
        TextView keymap4 = view.findViewById(R.id.keymap_4);
        TextView keymap5 = view.findViewById(R.id.keymap_5);
        TextView keymap6 = view.findViewById(R.id.keymap_6);
        TextView keymap7 = view.findViewById(R.id.keymap_7);
        TextView keymap8 = view.findViewById(R.id.keymap_8);
        TextView keymap9 = view.findViewById(R.id.keymap_9);
        TextView keymap0 = view.findViewById(R.id.keymap_0);
        TextView keymapDel = view.findViewById(R.id.keymap_del);
        TextView keymapSure = view.findViewById(R.id.keymap_sure);
        Spinner spType = view.findViewById(R.id.sp_type);
        RxView.clicks(keymap0).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("0");
            }
        });
        final String[] spinner = {"A", "B", "C"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, spinner);
        spType.setAdapter(spinnerAdapter);

        switch (AppApplication.orderType) {
            case "A":
                spType.setSelection(0);
                break;
            case "B":
                spType.setSelection(1);
                break;
            case "C":
                spType.setSelection(2);
                break;
        }

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppApplication.orderType = spinner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RxView.clicks(keymap1).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("1");
            }
        });
        RxView.clicks(keymap2).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("2");
            }
        });
        RxView.clicks(keymap3).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("3");
            }
        });
        RxView.clicks(keymap4).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("4");
            }
        });
        RxView.clicks(keymap5).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("5");
            }
        });
        RxView.clicks(keymap6).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("6");
            }
        });
        RxView.clicks(keymap7).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("7");
            }
        });
        RxView.clicks(keymap8).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("8");
            }
        });
        RxView.clicks(keymap9).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                checkIsAllowToEdit("9");
            }
        });

        RxView.clicks(keymapDel).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {

//                    ????????????????????????????????????
                if (isRestingBack) {
                    ToastUtil.showLongToast("????????????????????????????????????");
                    return;
                }
                if (!TextUtils.isEmpty(edtKeymapView.getText().toString().trim())) {
                    edtKeymapView.setText(edtKeymapView.getText().toString().trim().substring(0, edtKeymapView.getText().toString().length() - 1));
                }
            }
        });
        popDialog = new PopDialog.Builder(this).create(view);
        Window dialogWindow = popDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // ????????????????????????
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ?????????????????????????????????
        p.height = (int) (d.getHeight() * 0.65); // ????????????????????????0.6
        p.width = (int) (d.getWidth() * 0.4); // ????????????????????????0.65
        dialogWindow.setAttributes(p);

        RxView.clicks(keymapSure).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {

                String number = edtKeymapView.getText().toString();
                if (!isRestingBack && Integer.parseInt(number) < Integer.parseInt(initOrderNumber())) {
                    ToastUtil.showLongToast("??????????????????????????????????????????????????????");
                    return;
                }
                // ??????????????????????????????  ??????
                RestingInfoBean restingInfoBean = new RestingInfoBean();
                restingInfoBean.setBean(vipBean);
                orderNumberBean.setCreatTime(System.currentTimeMillis());
                orderNumberBean.setOrderNumber(Integer.parseInt(number));

                restingInfoBean.setOrderNumberBean(orderNumberBean);
                restingInfoBean.setShopCarYWGoodBeans(shopCarYWGoodBeans);
                // TODO: 2019/7/17 ????????????????????????????????????

                ordersDao.addOrder(restingInfoBean);
                Log.e("???????????????", restingInfoBean.toString());

                //??????????????????????????????
                if (!isRestingBack) {
                    AppApplication.spUtils.put("OrderNumberBean", AppApplication.gson.toJson(orderNumberBean));
                }
                isRestingBack = false;//??????????????????????????????????????????????????????????????????
                clearShopCar(1);
                popDialog.dismiss();
            }
        });
        RxView.clicks(closed).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                popDialog.dismiss();
            }
        });
        popDialog.setCanceledOnTouchOutside(false);
        popDialog.show();

    }

    //    ??????????????? ???????????????????????????
    private String initOrderNumber() {

        if (!isRestingBack) {
            orderNumberBean = AppApplication.gson.fromJson(AppApplication.spUtils.getString("OrderNumberBean", AppApplication.gson.toJson(new OrderNumberBean())), OrderNumberBean.class);
            if (!DateUtils.isToday(orderNumberBean.getCreatTime())) {
                orderNumberBean = new OrderNumberBean();
            } else {
                orderNumberBean.setOrderNumber(orderNumberBean.getOrderNumber() + 1);
            }
            Log.e("orderNumberBean", orderNumberBean.toString());
        }

        return orderNumberBean.getOrderNumberStr();

    }

    //    ????????????
    @SuppressLint("CheckResult")
    private void showNoBarCodeGoodsPop() {

        for (Object shopCarYWGoodBean : shopCarYWGoodBeans) {
            if (shopCarYWGoodBean instanceof YWGoodBean && TextUtils.equals(noscanSKU, ((YWGoodBean) shopCarYWGoodBean).getSku())) {
                ToastUtil.showLongToast("?????????????????????????????????");
                return;
            }
        }

        if (popDialog != null && popDialog.isShowing()) {
            popDialog.dismiss();
        }

        View view = View.inflate(this, R.layout.pop_no_bar_code_goods, null);

        ImageView closed = (ImageView) view.findViewById(R.id.closed);
        edtKeymapView = (TextView) view.findViewById(R.id.price);

        TextView keymap1 = view.findViewById(R.id.keymap_1);
        TextView keymap2 = view.findViewById(R.id.keymap_2);
        TextView keymap3 = view.findViewById(R.id.keymap_3);
        TextView keymap4 = view.findViewById(R.id.keymap_4);
        TextView keymap5 = view.findViewById(R.id.keymap_5);
        TextView keymap6 = view.findViewById(R.id.keymap_6);
        TextView keymap7 = view.findViewById(R.id.keymap_7);
        TextView keymap8 = view.findViewById(R.id.keymap_8);
        TextView keymap9 = view.findViewById(R.id.keymap_9);
        TextView keymap0 = view.findViewById(R.id.keymap_0);
        TextView keymapDel = view.findViewById(R.id.keymap_del);
        TextView keymapSure = view.findViewById(R.id.keymap_sure);
        TextView keymapPoint = view.findViewById(R.id.keymap_point);

        popDialog = new PopDialog.Builder(this).create(view);
        Window dialogWindow = popDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // ????????????????????????
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ?????????????????????????????????
        p.height = (int) (d.getHeight() * 0.6); // ????????????????????????0.6
        p.width = (int) (d.getWidth() * 0.4); // ????????????????????????0.65
        dialogWindow.setAttributes(p);

//        ????????????
        RxView.clicks(closed).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                popDialog.dismiss();
            }
        });

        RxView.clicks(keymap0).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("0");
            }
        });
        RxView.clicks(keymap1).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("1");
            }
        });
        RxView.clicks(keymap2).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("2");
            }
        });
        RxView.clicks(keymap3).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("3");
            }
        });
        RxView.clicks(keymap4).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("4");
            }
        });
        RxView.clicks(keymap5).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("5");
            }
        });
        RxView.clicks(keymap6).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("6");
            }
        });
        RxView.clicks(keymap7).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("7");
            }
        });
        RxView.clicks(keymap8).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("8");
            }
        });
        RxView.clicks(keymap9).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit("9");
            }
        });
        RxView.clicks(keymapPoint).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                checkIsAllowToEdit(".");
            }
        });

        RxView.clicks(keymapDel).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (!TextUtils.isEmpty(edtKeymapView.getText().toString().trim())) {
                    edtKeymapView.setText(edtKeymapView.getText().toString().trim().substring(0, edtKeymapView.getText().toString().length() - 1));
                }
            }
        });
        RxView.clicks(keymapSure).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                String price = edtKeymapView.getText().toString().trim();
                if (TextUtils.isEmpty(price)) {
                    ToastUtil.showLongToast("????????????????????????");
                    return;
                }


//                ???????????????????????????????????????sku???????????????????????????????????????
                YWGoodBean ywGoodBean = new YWGoodBean();

                ywGoodBean.setBuynum(1);
                ywGoodBean.setPrice(price);
                ywGoodBean.setSku(noscanSKU);
//                noscangoods.setSpecsBean(specsBean);
                ywGoodBean.setProduct_name("????????????");
                shopCarYWGoodBeans.add(0, ywGoodBean);
                if (showShopingDisplay != null) {
                    showShopingDisplay.setList(shopCarYWGoodBeans);
                }
                shopCarGoodsAdapter.notifyDataSetChanged();
                popDialog.dismiss();
                showBt("1");
                countMoney();
            }
        });

        popDialog.setCanceledOnTouchOutside(false);
        popDialog.show();

    }

    //    ??????????????????
    public void checkIsAllowToEdit(String toEditStr) {

        String editStr = edtKeymapView.getText().toString().trim();
        if (toEditStr.equals(".")) {
            if (editStr.contains(".")) {
                ToastUtil.showLongToast("????????????????????????");
                return;
            } else if (TextUtils.isEmpty(editStr)) {
                editStr = "0";
            }
        } else {
            if (editStr.equals("0")) {

                editStr = "";
            }
            if (editStr.contains(".") && editStr.substring(editStr.indexOf(".")).length() > 2) {
                ToastUtil.showLongToast("??????????????????????????????");
                return;
            }
        }
        String appendStr = editStr + toEditStr;
        edtKeymapView.setText(appendStr);

    }

    public void showCouponList() {
        CouponDialog couponDialog = new CouponDialog(context, vipBean, couponBean);
        couponDialog.setListener(new CouponDialog.CounListener() {
            @Override
            public void onSearchVip() {
                Intent intent = new Intent(context, SearchVipActivity.class);
                startActivityForResult(intent, 2003);
            }

            @Override
            public void onChooseCoupon(CouponBean bean) {
                couponBean = coupon = bean;
                showVip();
                countMoney();

            }

            @Override
            public void onCancleCoupon() {
                setYouHuiQuan();
                isHasQuan = false;
                coupon = null;
                couponBean = null;
                if (showShopingDisplay != null) {
                    showShopingDisplay.cancleCoupon();
                }
                showVip();
                countMoney();

            }

            @Override
            public void refreshVip() {
                if (vipBean != null) {
//                    LoadingDialog.show(MainActivity.this);
//                    viewModel.getVipInfo(vipBean.getPhone());
                }

            }
        });

        new XPopup.Builder(context)
                .popupPosition(PopupPosition.Right)
                .asCustom(couponDialog)
                .show();


    }


    /**
     * ???????????? ?????? ????????????
     *
     * @param type 1 ??????
     */
    private void showBt(String type) {
        if (type.equals("1")) {
            binding.hasCarGoodsLayout.setVisibility(View.VISIBLE);
            binding.getresting.setVisibility(View.GONE);
        } else if (type.equals("0")) {
            binding.hasCarGoodsLayout.setVisibility(View.GONE);
            binding.getresting.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ???????????????  ?????? ??????  ??????
     */
    private void setYouHuiQuan() {

        showVip();
        if (wetherHorizontalScreen) {
            binding.couponLayout.setVisibility(View.GONE);
        } else {
            binding.couponStr.setText("");
            binding.couponLayout.setVisibility(View.INVISIBLE);
        }
    }

    //    ?????????????????????VIP??????
    private void showVip() {
        if (vipBean != null) {
            if (vipBean.getCouponList() != null) {
                binding.tvCouponNum.setText(vipBean.getCouponList().size() + "");
            } else {
                binding.tvCouponNum.setText("0");
            }
            if (coupon != null) {
                binding.ivIsSelectCoupon.setImageResource(R.mipmap.icon_coupons_select);
            } else {
                binding.ivIsSelectCoupon.setImageResource(R.mipmap.icon_coupons_normal);
            }

            binding.layoutVipSelect.setVisibility(View.GONE);
            binding.vipLayout.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(vipBean.getHeadPortrait())
                    .apply(AppApplication.options)
                    .into(binding.vipHeadImage);
            binding.vipMoney.setText("?????????" + vipBean.getBalance() + "???");
            binding.vipName.setText(vipBean.getName() + "");
//            if (showShopingDisplay != null) {
//                showShopingDisplay.setvip(vipBean);
//            }
            return;
        } else {
            binding.layoutVipSelect.setVisibility(View.VISIBLE);
            binding.vipLayout.setVisibility(View.GONE);
        }
    }


    //todo
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void SystemMessageEvent(final SystemMessageEvent systemMessageEvent) {
        if (null != systemMessageEvent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int sign = systemMessageEvent.message;
                    if (sign == 1) {
                        //????????????
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    } else if (sign == 2) {
                        //???????????? ????????????
                        goodsDao.deleteAllData();
                        viewModel.getCategoryList();
//                        checkDialog();
                    } else if (sign == 3) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        DisplayManager mDisplayManager;//???????????????

        Display[] displays;//????????????

        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);

        displays = mDisplayManager.getDisplays();

        Log.e("onResume", "??????:" + displays.length);

        if (displays.length > 1 && wetherHorizontalScreen) {
            //???????????????
            initDisplay();
        }
    }

    //    ???????????????
    public void initDisplay() {

        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            return;
        }

        screenManager.init(this);
        // ???????????????????????????   ???????????????
        Display display = screenManager.getPresentationDisplays();
        if (display != null && null == showShopingDisplay) {
            showShopingDisplay = new ShowShopingDisplay(this, display);
            showShopingDisplay.show();
            showShopingDisplay.initBannnerView();
        } else {
            if (!showShopingDisplay.isShow) {
                showShopingDisplay.show();
                showShopingDisplay.initBannnerView();
            } else if (null != showShopingDisplay) {
                showShopingDisplay.initBannnerView();
            }
        }
    }

    public void showBackupCash() {

        backupCashDialog = new BackupCashDialog(this)
                .setTitle("???????????????")
                .setListenr(new BackupCashDialog.OnClickListenr() {
                    @Override
                    public void onCommit(BackupCashRecordBean recordBean) {
                        // ??????????????????
//                        presenter.commitBackupCash(recordBean);
                    }

                    @Override
                    public void onInit() {
                    }

                    @Override
                    public void onConfirm(boolean isPrintCheck) {
                    }

                    @Override
                    public void onCancle(boolean isPrintCheck) {
                    }

                });
        new XPopup.Builder(this)
                .asCustom(backupCashDialog)
                .show();

//        presenter.getBackupCashType();

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

    //????????????long???????????????????????????????????????????????????????????????
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //??????????????????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //????????????????????????????????????
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //??????2000ms??????????????????????????????Toast????????????

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("????????????????????????");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //???????????????????????????????????????????????????????????????????????????
                mExitTime = System.currentTimeMillis();
            } else {
                //??????2000ms??????????????????????????????????????????-??????System.exit()??????????????????
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResult(String resultStr) {
        Log.e("scanToWork2222", "onResult:" + resultStr);

        if (TextUtils.isEmpty(resultStr)) {
            return;
        }

        long last = AppApplication.spUtils.getLong("lastpay");
        if (System.currentTimeMillis() - last < 2000) {
            return;
        }
        AppApplication.spUtils.put("lastpay", System.currentTimeMillis());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scanToWork(resultStr);
            }
        });
    }

    private void scanToWork(String resultStr) {

        Log.e("scanToWork", "resultStr:" + resultStr);

        if (resultStr.length() == 10) {  //??????????????????/ic???
//            LoadingDialog.show((FragmentActivity) getContext(), "??????????????????...");
            Log.e("scanToWork3333", "resultStr:" + resultStr);

            viewModel.getVIPbyICCard(resultStr);
            return;
        }

        if (resultStr.length() == 15) {//?????????????????????????????????
            Log.e("scanToWork", "resultStr222:");
            viewModel.getVIPbyCode(resultStr);
        } else if (resultStr.length() == 14) {//?????????????????????
            CouponCodeBean couponCodeBean = new CouponCodeBean();
            couponCodeBean.setCoupon_code(resultStr);
            viewModel.getCoupon(couponCodeBean);
        } else if (resultStr.length() == 3 || resultStr.length() == 4 || resultStr.length() == 13) {//??????????????????????????????
            YWGoodBean ywGoodBean = goodsDao.getGoodInfoByGoodSku(resultStr);
            if (ywGoodBean == null) {
                RxToast.normal("????????????????????????????????????????????????????????????");
                return;
            }
            addGoodsToCar(ywGoodBean, 0);
        }

    }

}
