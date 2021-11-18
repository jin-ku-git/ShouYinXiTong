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
 * 金库
 */

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements ScanUtils.OnResultListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static Context context;
    //    数据库
    public GoodsDao goodsDao;
    private OrdersDao ordersDao;

    public static boolean ISCONNECT = false;

    //默认选中第一个
    private int currentType = 0;
    private int pages = 1;//页数
    private int sizes = 100;//每页的数量

    private List<GoodsTypesBean.GoodsTypeBean> goodsTypeBeans = new ArrayList<>();//分类数据

    private GoodsTypesBean.GoodsTypeBean goodsTypeBean;//

    private List<YWGoodBean> list = new ArrayList<>();//商品列表数据
    private List<MealsItemBean> listMeals = new ArrayList<>();//套餐列表数据
    private List YWGoodBeans = new ArrayList();

    private List shopCarYWGoodBeans = new ArrayList();//购物车所展示的数据
    private ShopCarGoodsAdapter shopCarGoodsAdapter;


    private float totalMoney;//合计价格
    private float disCountMoney;//使用优惠券之前的价格
    private float couponMoney;//优惠券以后的价格

    private CouponBean couponBean;//用到的优惠券
    private CouponBean coupon;//获取到的优惠券
    private VIPBean vipBean;//确认以后的vip


    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;
    //是否为横屏  true  横屏  false 竖屏
    private boolean wetherHorizontalScreen = true;
    public static final String noscanSKU = "0000000000000";//无码商品的SKU
    private PopDialog popDialog;
    //    无码商品 弹窗种的价格输入框
    private TextView edtKeymapView;
    //挂单弹窗
    private boolean isRestingBack = false;//是否是挂单重新编辑回到这个界面的
    //    正常情况下的顺序单号
    private OrderNumberBean orderNumberBean;
    private ActivityListBean activityListBean;

    //打印服务
    private SunmiPrinterService woyouService = null;//商米标准打印 打印服务
    public static PrinterPresenter printerPresenter;


    private BackupCashDialog backupCashDialog;//弹窗
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
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
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
        //初始化 goodsBean 数据库
        goodsDao = new GoodsDao(this);
        boolean dataExist = goodsDao.isDataExist();
        if (dataExist) {
            goodsDao.deleteAllData();
        }

        ordersDao = new OrdersDao(this);

        //是否为横屏
        wetherHorizontalScreen = MainAboutUtils.getWetherHorizontalScreen(this);

        //芯烨打印机初始化
        try {
            //芯烨打印机初始化
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
            RxToast.normal("未连接芯烨USB打印机");
        }

//        binding.goodsTypeList.addItemDecoration(new GridDividerItemDecoration(this, 8, 1, getResources().getColor(R.color.white)));
//
//        binding.goodsList.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));




        //获取商品分类
        viewModel.getCategoryList();

        //购物车
        initRecyclerShopping();

        //连接打印服务
        connectPrintService();


//        binding.vpV.setAdapter(new ViewPagerBindingAdapter());
        binding.vpV.setOrientation(ViewPager2.ORIENTATION_VERTICAL);


    }



    //获取从AddVIPActivity传递的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UserBeanEventBus(UserBean goodsEntity) { //方法名任意，这里的参数和  EventBus.getDefault().post(111);对应即可
        System.out.println("------>"+goodsEntity);

        if (goodsEntity.getUser_type()==1){//选择用户
            viewModel.userBean.set(goodsEntity);
        }else if (goodsEntity.getUser_type()==2){//取消选择用户
            viewModel.userBean.set(null);
        }

    }


    //连接打印服务
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
                if (shopCarYWGoodBeans.get(position) instanceof YWGoodBean) {//判断是否是商品  还是套餐
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

                //计算 件数和金额 并设置
                countMoney();
            }

            @Override
            public void removeItemGoodsListering(Object item, int position) {
                if (position < 0 || position >= shopCarYWGoodBeans.size()) {
                    return;
                }
                if (shopCarYWGoodBeans.get(position) instanceof YWGoodBean) {//判断是否是商品  还是套餐
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

                //计算 件数和金额 并设置
                countMoney();
            }

        });
    }

    //    刷新商品列表
    private void initGoodsList(GoodsTypesBean.GoodsTypeBean message) {

        //        刷新一下实际库存，因为如果客户没买，内存中的库存可能会有改变。但是并没有真正成交
        List list = new ArrayList<>();
        if (message.isMeals) {
            Log.e("查询套餐id", message.id + "");
            list.addAll(goodsDao.getMealListByCategoryId(AppApplication.subZeroAndDot(message.id)));
        } else {
            Log.e("查询商品id", message.id + "");
            list.addAll(goodsDao.getGoodListByCategoryId(message.id));
        }
        Log.e("数据库数据长度----->", goodsDao.getAllDate().size() + "");
        Log.e("长度----->", list.size() + "");
        viewModel.goodsList.clear();
        for (Object entity : list) {
            if (entity instanceof YWGoodBean) {
                //添加商品列表
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) entity);
                //双向绑定动态添加Item
                viewModel.goodsList.add(itemViewModel);
            } else {
                //添加套餐列表
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (MealsItemBean) entity, 1);
                //双向绑定动态添加Item
                viewModel.goodsList.add(itemViewModel);

                for (int i = 0; i < ((MealsItemBean) entity).getMeal_store_goods_detail().size(); i++) {
                    MainGoodItemTCViewModel itemViewModels = new MainGoodItemTCViewModel(viewModel, ((MealsItemBean) entity).getMeal_store_goods_detail().get(i));
                    //双向绑定动态添加Item
                    viewModel.goodsList.get(0).goodsList.add(itemViewModels);
                }
            }
        }

        /**
         * 添加一个添加商品
         */
        YWGoodBean ywGoodBean=new YWGoodBean();
        MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) ywGoodBean);
        viewModel.goodsList.add(itemViewModel);
    }



    @Override
    public void initViewObservable() {

        //分类监听
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
        //商品列表监听
        viewModel.uc.goodsListTwo.observe(this, new Observer<List<YWGoodBean>>() {
            @Override
            public void onChanged(List<YWGoodBean> objects) {
                list.addAll(objects);
                goodsDao.initTable(objects);

                if (objects.size() < sizes) {
                    //获取所有商品结束,获取套餐列表
                    viewModel.getStoreMealSetGoodsList();
                } else {
                    pages++;
                    viewModel.getGoodsList(pages + "", sizes + "");
                }
            }
        });
        //套餐列表监听
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
                    //双向绑定动态添加Item
                    viewModel.goodsList.add(itemViewModel);
                }
                /**
                 * 添加一个添加商品
                 */
                YWGoodBean ywGoodBean=new YWGoodBean();
                MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (YWGoodBean) ywGoodBean);
                viewModel.goodsList.add(itemViewModel);

                viewModel.dismissDialog();

                //获取优惠活动
                viewModel.getActivitList();

            }
        });
        //点击分类监听
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
//        RxToast.normal("第"+message.position+"个--"+message.name);
                initGoodsList(Bean);

                goodsTypeBean = Bean;
            }
        });
        //点击商品监听
        viewModel.ywGoodBeanSingleLiveEvent.observe(this, new Observer<YWGoodBean>() {
            @Override
            public void onChanged(YWGoodBean Bean) {
                Log.e("id---》", Bean.id);

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
                    case 1://弹窗
                        MainFunDialog dialog = new MainFunDialog(context);
                        new XPopup.Builder(context)
                                .asCustom(dialog)
                                .show();
                        break;
                    case 2://更新数据
                        currentType = 0;
                        goodsTypeBeans.clear();//清空分类
                        YWGoodBeans.clear();//清空商品列表
                        goodsDao.deleteAllData();
                        break;
                    case 3://清空购物车
                        clearShopCar(1);
                        break;
                    case 4://挂单
                        showRestingPop();
                        break;
                    case 5://取单
                        Intent intent = new Intent(MainActivity.this, RestingListActivity.class);
                        intent.putExtra("activityListBean", activityListBean);
                        startActivityForResult(intent, 223);
                        break;

                    case 8://优惠券弹窗
                        showCouponList();
                        break;
                    case 9://跳转选择用户页面
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
        //优惠券
        viewModel.uc.CouponList.observe(this, new Observer<CouponBean>() {
            @Override
            public void onChanged(CouponBean couponBean) {
                if (shopCarYWGoodBeans.size() == 0) { //购物车商品数量为0时，不允许使用优惠券
                    ToastUtil.showShortToast("请先选择商品后再使用优惠券");
                } else {
                    coupon = couponBean;

                    if (coupon.getPayment_code_num() != "") {
                        viewModel.getVIPbyCode(coupon.getPayment_code_num() + "");
                    } else {
                        ToastUtil.showShortToast("该优惠券未查询到对应会员账户信息，暂无法使用请联系工作人员处理!");
                    }

                }
            }
        });
        //点击搜索监听
        viewModel.searchLiveEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.goodsLists.clear();
                for (int i=0;i<10;i++){
                    MealsItemBean mealsItemBean=new MealsItemBean();
                    mealsItemBean.setMeal_goods_name("测试"+i);
                    mealsItemBean.setMeal_goods_price("1"+i);
                    mealsItemBean.setMeal_goods_sku("1"+i);
                    List<MealGoodsBean> list=new ArrayList<>();
                    for (int j=0;j<2;j++){
                        MealGoodsBean mealGoodsBean=new MealGoodsBean();
                        mealGoodsBean.setGoods_name("测试"+i+"--"+j);
                        list.add(mealGoodsBean);
                    }
                    mealsItemBean.setMeal_store_goods_detail(list);

                    MainGoodItemViewModel itemViewModel = new MainGoodItemViewModel(viewModel, (MealsItemBean) mealsItemBean, 1);
                    //双向绑定动态添加Item
                    viewModel.goodsLists.add(itemViewModel);
                }

            }
        });
        //ViewPager第二页 搜索监听
        viewModel.items.get(1).stringEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                RxToast.normal("测试"+s);
            }
        });
    }

    //    清空购物车
    private void clearShopCar(int type) {


        isRestingBack = false;//不管是不是挂单返回的界面，都清除掉，如果是挂单的，该单也已经清除了

        cancleVip();
        //清空购物车
        shopCarYWGoodBeans.clear();
        if (showShopingDisplay != null) {
            showShopingDisplay.cleanShopCar();
        }
        shopCarGoodsAdapter.notifyDataSetChanged();
//        activitBean = null;//清空优惠活动
        //重新从数据库同步数据
        if (type == 0) {
            viewModel.getCategoryList();
            initGoodsList(goodsTypeBean);//刷新商品列表

        }


        showBt("0");
        //计算 件数和金额 并设置    当购物车的商品为零的时候，则优惠券由于满减等原因直接就置空了
        countMoney();
        //初始化副屏
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
     * 连接usb
     */
    private void connectUSB() {
        List<String> usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList != null) {
            String usbAddress = usbList.get(0);
            if (usbAddress.equals(null) || usbAddress.equals("")) {
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            } else {
                myBinder.ConnectUsbPort(getApplicationContext(), usbAddress, new TaskCallback() {
                    @Override
                    public void OnSucceed() {
                        ISCONNECT = true;
                        Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnFailed() {
                        ISCONNECT = false;
                        Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
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
                case 222://人脸识别返回的vip 信息
                    vipBean = data.getParcelableExtra("vipbean");
                    showVip();
                    break;
                case 223://从上一个界面返回的之前挂单的对象

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
                case 2001://点击的商品列表
                    Object good = data.getSerializableExtra("data");
                    addGoodsToCar(good, 1);
                    break;
                case 2002://点击购物车
                    Object good2 = data.getSerializableExtra("data");
                    addGoodsToCar(good2, 2);
                    break;
                case 2003://选择会员回来
                    this.vipBean = (VIPBean) data.getSerializableExtra("data");
                    showVip();
                    break;
            }
        } else if (resultCode == 222) {
            cancleVip();
        } else if (resultCode == 666) {
            //支付成功返回
            clearShopCar(1);
        } else if (resultCode == 223) {
            //去收款页面是选择vip
            this.vipBean = (VIPBean) data.getSerializableExtra("data");
            showVip();

        } else if (resultCode == 224) {
            //选中优惠券，重新设置vip 和 coupon
            this.vipBean = (VIPBean) data.getSerializableExtra("vipBean");
            this.couponBean = this.coupon = (CouponBean) data.getSerializableExtra("coupon");
            showVip();
            countMoney();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    取消使用vip
    private void cancleVip() {
        vipBean = null;
        binding.vipLayout.setVisibility(View.GONE);
        if (showShopingDisplay != null) {
            showShopingDisplay.cancleCoupon();
        }

        //取消优惠券的使用 ===============start=============


        setYouHuiQuan();
        isHasQuan = false;
        coupon = null;
        couponBean = null;

        countMoney();
        //取消优惠券的使用 ===============end=============

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
     * @param from 0 直接点解列表 1 从列表到商品详情 2 修改，
     */
    public void addGoodsToCar(Object bean, int from) {
        if (bean instanceof YWGoodBean) {
            YWGoodBean ywGoodBean = BeanCloneUtil.cloneTo((YWGoodBean) bean);
            if (from == 0) {
                ywGoodBean.setBuynum(1);
            }

            if (ywGoodBean.getSales_status().equals("2")) {
                ToastUtil.showLongToast("商品已售罄");
                return;
            }
            int hasInShopCar = -1;
            for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
                if (shopCarYWGoodBeans.get(i) instanceof YWGoodBean) {
                    if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getSku(), ywGoodBean.getSku())) {
                        //找到该商品
                        if (from == 2) {
                            //如果是修改直接中断循环
                            hasInShopCar = i;
                            break;
                        } else {
                            //不是修改判断折扣是否相等
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
                //说明购物车里存在相同价格的商品
                if (from == 1 || from == 0) {
                    //goods_type==1  1标品、2非标品
                    if (((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).goods_type == 2) {
                        ToastUtil.showShortToast("该商品为标品商品，不可重复添加");
                    } else {
//                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).weightnum=weight_d;
//                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).weight_type=weight_type;
                        ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar))
                                .setBuynum(((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).getBuynum() + ywGoodBean.getBuynum());
                    }
                } else {
//                    ((YWGoodBean) bean).weightnum=weight_d;
//                    ((YWGoodBean) bean).weight_type=weight_type;
                    //修改
                    shopCarGoodsAdapter.setData(hasInShopCar, bean);
                }
            }
        } else {
            //说明是套餐
            MealsItemBean mealsItemBean = BeanCloneUtil.cloneTo((MealsItemBean) bean);
            if (from == 0) {
                mealsItemBean.setBuynum(1);
            }

            int hasInShopCar = -1;
            for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
                if (shopCarYWGoodBeans.get(i) instanceof MealsItemBean) {
                    if (TextUtils.equals(((MealsItemBean) shopCarYWGoodBeans.get(i)).getMeal_goods_sku(), mealsItemBean.getMeal_goods_sku())) {
                        //找到该商品
                        hasInShopCar = i;
                        break;
                    }
                }
            }
            if (hasInShopCar == -1) {
                shopCarYWGoodBeans.add(mealsItemBean);
            } else {
                //说明购物车里存在相同价格的商品
                if (from == 1 || from == 0) {
                    ((MealsItemBean) shopCarYWGoodBeans.get(hasInShopCar))
                            .setBuynum(((MealsItemBean) shopCarYWGoodBeans.get(hasInShopCar)).getBuynum() + mealsItemBean.getBuynum());
                } else {
                    //修改
                    shopCarGoodsAdapter.setData(hasInShopCar, bean);
                }
            }
        }
        if (showShopingDisplay != null) {
            showShopingDisplay.setList(shopCarYWGoodBeans);
        }
        shopCarGoodsAdapter.notifyDataSetChanged();
        //计算 件数和金额 并设置
        countMoney();
        showBt("1");
    }

    boolean isHasQuan = false;

    //计算 件数和金额 并设置
    private void countMoney() {
        Log.d("优惠券", "1");
        int buyCount = 0;//件数
        //总应付
        totalMoney = 0f;
        //使用优惠券之前的价格，总额
        disCountMoney = 0f;
        //优惠券价
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
            if (shopCarYWGoodBean instanceof YWGoodBean) {//商品的话  按这个计算价格
                Log.d("优惠券", "2");
//            这个地方  默认显示第一个规格的价格，已经与后台商议好 每个商品最低有一个规格，如果因为集合为空，出现角标越界等问题，联系saas平台
                YWGoodBean goodBean = (YWGoodBean) shopCarYWGoodBean;
                double itemmoney;
                if (goodBean.goods_type == 2) {
                    itemmoney = Double.parseDouble(goodBean.getPrice()) * goodBean.weightnum * Double.parseDouble(goodBean.getDiscount()) / 100;
                } else {
                    itemmoney = Double.parseDouble(goodBean.getPrice()) * Double.parseDouble(goodBean.getDiscount()) * goodBean.getBuynum() / 100;

                }
                //单品折扣前的价格
                double originItmemoney;
                if (goodBean.goods_type == 2) {
                    originItmemoney = Double.parseDouble(goodBean.getPrice()) * goodBean.weightnum;

                } else {
                    originItmemoney = Double.parseDouble(goodBean.getPrice()) * goodBean.getBuynum();
                }


                totalMoney += itemmoney;                             //应付
                disCountMoney += originItmemoney;                    //总额（折扣前）
                goodBean.setTotalmoney(YWStringUtils.getStanMoney((float) itemmoney));//条目总价
                buyCount += goodBean.getBuynum();
            } else {
                MealsItemBean mealsItemBean = (MealsItemBean) shopCarYWGoodBean;
                double itemmoney = Double.parseDouble(mealsItemBean.getMeal_goods_price()) * mealsItemBean.getBuynum();
                totalMoney += itemmoney;                             //应付
                disCountMoney += itemmoney;                    //总额（折扣前）
                buyCount += mealsItemBean.getBuynum();
            }
        }
        //
        Double totalDiscount = BigDecimalUtils.subtract(disCountMoney, totalMoney);
        binding.tvDiscountMoney.setText("折扣：" + BigDecimalUtils.formatRoundUp(totalDiscount, 2));
        if (showShopingDisplay != null) {
            showShopingDisplay.countMoney(disCountMoney, disCountMoney, buyCount, freeMoney, BigDecimalUtils.formatRoundUp(totalDiscount, 2));
        }
        //判断优惠券  优惠券不论是否有套餐，都可以享受
        if (coupon != null) {
            if (showShopingDisplay != null) {
                showShopingDisplay.setCoupon(coupon);
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long end = 0L;
            Long start = 0L;
            try {
                end = sf.parse(coupon.getEndTime()).getTime();// 日期转换为时间戳
                start = sf.parse(coupon.getStartTime()).getTime();// 日期转换为时间戳
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() > end) {
                ToastUtil.showLongToast("优惠券已经过期！");
                couponBean = null;
                coupon = null;
                if (showShopingDisplay != null) {
                    showShopingDisplay.cancleCoupon();
                }
                setYouHuiQuan();
            } else if (System.currentTimeMillis() < start) {
                ToastUtil.showLongToast("优惠券活动还未开始！");
                couponBean = null;
                coupon = null;
                if (showShopingDisplay != null) {
                    showShopingDisplay.cancleCoupon();
                }
                setYouHuiQuan();
            } else {//如果以上满足，那么可以使用优惠券了
                switch (coupon.getType()) {
                    case "0"://满减券
                        if (Float.parseFloat(coupon.getFull_price()) > totalMoney) {
                            ToastUtil.showLongToast("商品总金额小于满减金额！");
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
                            binding.couponStr.setText("已经使用 " + couponBean.getName());
                            freeMoney = freeMoney + Double.parseDouble(couponBean.getSubtravt());
                        }
                        if (couponBean != null) {
                            couponMoney = disCountMoney - Float.parseFloat(couponBean.getSubtravt());
                        }
                        break;
                    case "1"://折扣券
                        couponBean = coupon;
                        if (showShopingDisplay != null) {
                            showShopingDisplay.setCoupon(couponBean);
                        }
                        Double multiply = BigDecimalUtils.multiply(totalMoney + "", couponBean.getDiscount_price());
                        Double zhekou = BigDecimalUtils.divide(multiply, 100);
                        Double jianmian = BigDecimalUtils.formatRoundUp(BigDecimalUtils.subtract(totalMoney, zhekou), 2);

                        freeMoney = jianmian;
                        binding.couponStr.setText("已经使用 " + couponBean.getName());
                        couponMoney = totalMoney - Float.parseFloat(jianmian + "");
//                        }
                        break;
                    case "5"://兑换券
                        couponBean = coupon;
                        YWGoodBean ywGoodBean = goodsDao.getGoodInfoByGoodSku(couponBean.getGoods_sku());
                        if (ywGoodBean == null) {
                            couponBean = null;
                            coupon = null;
                            ToastUtil.showLongToast("店铺暂无该商品，无法使用优惠券!");
                            if (showShopingDisplay != null) {
                                showShopingDisplay.cancleCoupon();
                            }
                            setYouHuiQuan();
                        }
                        break;
                }
            }
            isHasQuan = true;
        } else {//没有优惠券的话    使用优惠券前后的价格就一样了
            if (disCountMoney == 0) {
                disCountMoney = totalMoney;
            }
            couponMoney = disCountMoney;
            isHasQuan = false;
            Log.d("优惠券", "1");
        }
        Log.d("优惠券", "7");
        binding.tvBuyCount.setText("共" + buyCount + "件");
        if (couponMoney < 0) {
            couponMoney = 0;
        }
        //使用优惠券之前的价格   总价格    使用优惠券以后的价格
        Log.e("---gu收款金额", "totalMoney:" + totalMoney + ",disCountMoney:" + disCountMoney + ",couponMoney:" + couponMoney + ",freeMoney:" + freeMoney);
        setDiscountMoney("折扣：", freeMoney);
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
     * 设置优惠金额
     *
     * @param s
     * @param freeMoney
     */
    private void setDiscountMoney(String s, double freeMoney) {

        binding.tvTotalMoney.setText("总额" + YWStringUtils.getStanMoney((float) disCountMoney));
        if ((float) totalMoney < (float) (freeMoney)) {
            binding.tvCouponPrice.setText("优惠：" + totalMoney);
            binding.tvPayMoney.setText("0 元");
        } else {
            binding.tvCouponPrice.setText("优惠：" + BigDecimalUtils.formatRoundUp(freeMoney, 2));
            binding.tvPayMoney.setText("应付：" + YWStringUtils.getStanMoney((float) totalMoney - (float) (freeMoney)));
        }
    }

    //显示挂单的弹窗
    @SuppressLint("CheckResult")
    private void showRestingPop() {
        if (popDialog != null && popDialog.isShowing()) {
            popDialog.dismiss();
        }
        View view = View.inflate(this, R.layout.pop_resting, null);

        ImageView closed = (ImageView) view.findViewById(R.id.closed);
        edtKeymapView = (TextView) view.findViewById(R.id.price);

        //                单号的处理
        if (!isRestingBack) {//不是已经挂单的，按照顺序来
            edtKeymapView.setText(initOrderNumber());
        } else {//已经挂单的，按照原来的数据来

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
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
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
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("1");
            }
        });
        RxView.clicks(keymap2).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("2");
            }
        });
        RxView.clicks(keymap3).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("3");
            }
        });
        RxView.clicks(keymap4).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("4");
            }
        });
        RxView.clicks(keymap5).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("5");
            }
        });
        RxView.clicks(keymap6).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("6");
            }
        });
        RxView.clicks(keymap7).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("7");
            }
        });
        RxView.clicks(keymap8).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("8");
            }
        });
        RxView.clicks(keymap9).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许修改");
                    return;
                }
                checkIsAllowToEdit("9");
            }
        });

        RxView.clicks(keymapDel).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {

//                    已经挂单的单号不允许修改
                if (isRestingBack) {
                    ToastUtil.showLongToast("已经挂单的单号不允许改变");
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
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.65); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.4); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        RxView.clicks(keymapSure).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {

                String number = edtKeymapView.getText().toString();
                if (!isRestingBack && Integer.parseInt(number) < Integer.parseInt(initOrderNumber())) {
                    ToastUtil.showLongToast("自定义的单号不能小于当前的顺序单号！");
                    return;
                }
                // 单号，购物车商品列表  会员
                RestingInfoBean restingInfoBean = new RestingInfoBean();
                restingInfoBean.setBean(vipBean);
                orderNumberBean.setCreatTime(System.currentTimeMillis());
                orderNumberBean.setOrderNumber(Integer.parseInt(number));

                restingInfoBean.setOrderNumberBean(orderNumberBean);
                restingInfoBean.setShopCarYWGoodBeans(shopCarYWGoodBeans);
                // TODO: 2019/7/17 把挂单的数据存到数据库中

                ordersDao.addOrder(restingInfoBean);
                Log.e("挂单的数据", restingInfoBean.toString());

                //记录下已经挂单的数据
                if (!isRestingBack) {
                    AppApplication.spUtils.put("OrderNumberBean", AppApplication.gson.toJson(orderNumberBean));
                }
                isRestingBack = false;//再次挂单也是可以的，完成以后状态变成不是挂单
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

    //    初始化单号 获取的是下一个单号
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

    //    无码商品
    @SuppressLint("CheckResult")
    private void showNoBarCodeGoodsPop() {

        for (Object shopCarYWGoodBean : shopCarYWGoodBeans) {
            if (shopCarYWGoodBean instanceof YWGoodBean && TextUtils.equals(noscanSKU, ((YWGoodBean) shopCarYWGoodBean).getSku())) {
                ToastUtil.showLongToast("无码商品只能加入一种！");
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
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.4); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

//        关闭弹窗
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
                    ToastUtil.showLongToast("输入价格不能为空");
                    return;
                }


//                与后台协商，编写一个固定的sku，让后台知道这个是无码商品
                YWGoodBean ywGoodBean = new YWGoodBean();

                ywGoodBean.setBuynum(1);
                ywGoodBean.setPrice(price);
                ywGoodBean.setSku(noscanSKU);
//                noscangoods.setSpecsBean(specsBean);
                ywGoodBean.setProduct_name("无码商品");
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

    //    键盘输入逻辑
    public void checkIsAllowToEdit(String toEditStr) {

        String editStr = edtKeymapView.getText().toString().trim();
        if (toEditStr.equals(".")) {
            if (editStr.contains(".")) {
                ToastUtil.showLongToast("已经是小数格式了");
                return;
            } else if (TextUtils.isEmpty(editStr)) {
                editStr = "0";
            }
        } else {
            if (editStr.equals("0")) {

                editStr = "";
            }
            if (editStr.contains(".") && editStr.substring(editStr.indexOf(".")).length() > 2) {
                ToastUtil.showLongToast("最多输入小数点后两位");
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
     * 清空挂单 按钮 显示隐藏
     *
     * @param type 1 显示
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
     * 优惠券布局  横屏 隐藏  竖屏
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

    //    展示主界面上的VIP信息
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
            binding.vipMoney.setText("余额：" + vipBean.getBalance() + "元");
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
                        //账号注销
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    } else if (sign == 2) {
                        //账号同步 数据同步
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


        DisplayManager mDisplayManager;//屏幕管理类

        Display[] displays;//屏幕数组

        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);

        displays = mDisplayManager.getDisplays();

        Log.e("onResume", "屏幕:" + displays.length);

        if (displays.length > 1 && wetherHorizontalScreen) {
            //初始化副屏
            initDisplay();
        }
    }

    //    初始化副屏
    public void initDisplay() {

        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            return;
        }

        screenManager.init(this);
        // 获取真实存在的副屏   初始化副屏
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
                .setTitle("备用金收支")
                .setListenr(new BackupCashDialog.OnClickListenr() {
                    @Override
                    public void onCommit(BackupCashRecordBean recordBean) {
                        // 这里处理提交
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

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("再按一次退出程序");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
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

        if (resultStr.length() == 10) {  //识别到会员卡/ic卡
//            LoadingDialog.show((FragmentActivity) getContext(), "会员卡识别中...");
            Log.e("scanToWork3333", "resultStr:" + resultStr);

            viewModel.getVIPbyICCard(resultStr);
            return;
        }

        if (resultStr.length() == 15) {//通过会员码获取会员信息
            Log.e("scanToWork", "resultStr222:");
            viewModel.getVIPbyCode(resultStr);
        } else if (resultStr.length() == 14) {//优惠券条码扫描
            CouponCodeBean couponCodeBean = new CouponCodeBean();
            couponCodeBean.setCoupon_code(resultStr);
            viewModel.getCoupon(couponCodeBean);
        } else if (resultStr.length() == 3 || resultStr.length() == 4 || resultStr.length() == 13) {//商品条码扫描识别商品
            YWGoodBean ywGoodBean = goodsDao.getGoodInfoByGoodSku(resultStr);
            if (ywGoodBean == null) {
                RxToast.normal("该十三位商品条码无效，请联系管理员检查！");
                return;
            }
            addGoodsToCar(ywGoodBean, 0);
        }

    }

}
