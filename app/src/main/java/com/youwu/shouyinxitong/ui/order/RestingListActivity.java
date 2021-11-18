package com.youwu.shouyinxitong.ui.order;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.bean_used.ActivityListBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean_used.RestingInfoBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.databinding.ActivityRestingListBinding;
import com.youwu.shouyinxitong.db.GoodsDao;
import com.youwu.shouyinxitong.db.OrdersDao;
import com.youwu.shouyinxitong.ui.adapter.RestingGoodsAdapter;
import com.youwu.shouyinxitong.utils_tool.MainAboutUtils;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 取单页
 * 2021/10/28
 * 金库
 */

public class RestingListActivity extends BaseActivity<ActivityRestingListBinding, RestingListViewModel> {

    private ActivityListBean activityListBean;

    private List<ActivityListBean.ItemsBean> activityitems;
    private ActivityListBean.ItemsBean activitBean;
    //数据库
    private OrdersDao ordersDao;
    private GoodsDao goodsDao;
    private List goodBeans;//所有商品

    private List<RestingInfoBean> restingInfoBeans;
    //左侧
    private RestingOrderAdapter restingOrderAdapter;
    private List<RestingInfoBean> allDate; //数据库中的所有订单

    //    当前选中的订单
    private int currentSelect = -1;

    private RestingGoodsAdapter restingGoodsAdapter;

    private double totalMoney;//需支付金额

    private String activityIds = "";

    private double disCountMoney;//使用优惠券之前的价格
    private double couponMoney;//优惠券以后的价格

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        activityListBean = (ActivityListBean) getIntent().getSerializableExtra("activityListBean");

        activityitems = new ArrayList<>();
        activityitems.addAll(activityListBean.getItems());
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_resting_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        ordersDao = new OrdersDao(this);
        goodsDao = new GoodsDao(this);
        goodBeans = goodsDao.getAllDate();
//            每次进入都查询一下是否有不是今天的 挂单  如果有，就删掉
        for (RestingInfoBean restingInfoBean : ordersDao.getAllDate()) {
            if (!DateUtils.isToday(restingInfoBean.getOrderNumberBean().getCreatTime())) {
                ordersDao.deleteOrder(restingInfoBean.getOrderNumberBean().getCreatTime() + "");
            }
        }

        List shopCarYWGoodBeans = new ArrayList<>();
        binding.shopCarRecyCle.setLayoutManager(new LinearLayoutManager(this));
        restingGoodsAdapter = new RestingGoodsAdapter(this, shopCarYWGoodBeans);
        restingGoodsAdapter.bindToRecyclerView(binding.shopCarRecyCle);
        binding.shopCarRecyCle.swapAdapter(restingGoodsAdapter, true);

        restingInfoBeans = new ArrayList<>();
        allDate = ordersDao.getAllDate();
        restingInfoBeans.addAll(allDate);
        //更新下库存数据,显示挂单的商品是否还有足够的库存来进行挂单
        if (restingInfoBeans.size() > 0) {
            for (RestingInfoBean restingInfoBean : restingInfoBeans) {
                for (Object shopCarYWGoodBean : restingInfoBean.getShopCarYWGoodBeans()) {

                    if (shopCarYWGoodBean instanceof YWGoodBean) {
                        ((YWGoodBean) shopCarYWGoodBean).setCanAddShop(true);
                    } else {
                        MealsItemBean itemsBean = (MealsItemBean) shopCarYWGoodBean;

                    }


                }
            }
        }

        if (restingInfoBeans.size() > 0) {
            currentSelect = 0;
            restingInfoBeans.get(currentSelect).setSelect(1);
            showOrder();
        }

        binding.restingOrderRecyView.setLayoutManager(new LinearLayoutManager(this));
        restingOrderAdapter = new RestingOrderAdapter(this, restingInfoBeans);
        binding.restingOrderRecyView.swapAdapter(restingOrderAdapter, true);
        restingOrderAdapter.bindToRecyclerView(binding.restingOrderRecyView);
        restingOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (currentSelect != -1) {
                    restingInfoBeans.get(currentSelect).setSelect(0);
                }
                currentSelect = position;
                restingInfoBeans.get(currentSelect).setSelect(1);
                restingOrderAdapter.notifyDataSetChanged();
                showOrder();
            }
        });
    }

    @Override
    public void initViewObservable() {

        //监听
        viewModel.loadUrlEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {

            }
        });
    }

    //    显示订单详情
    private void showOrder() {
        if (currentSelect == -1) {
            ToastUtil.showLongToast("无单据可以显示");
            binding.orderNumber.setText("牌号：");
            binding.creatTime.setText("下单" + 0 + "件");
            binding.totalSize.setText("共" + 0 + "种商品");
            binding.allMoney.setText("￥0.00");
            restingGoodsAdapter.setNewData(new ArrayList<>());
            binding.vipLayout.setVisibility(View.GONE);
            return;
        }
        RestingInfoBean restingInfoBean = restingInfoBeans.get(currentSelect);
        binding.orderNumber.setText("牌号：" + restingInfoBean.getOrderNumberBean().getOrderNumberStr());

        int totalNumber = 0;
        totalMoney = 0d;

//          购物车中是否包含套餐
        boolean isHaveMeal = false;

        for (Object shopCarYWGoodBean : restingInfoBean.getShopCarYWGoodBeans()) {
            if (shopCarYWGoodBean instanceof YWGoodBean) {//商品的话  按这个计算价格
//            这个地方  默认显示第一个规格的价格，已经与后台商议好 每个商品最低有一个规格，如果因为集合为空，出现角标越界等问题，联系saas平台
                totalMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * ((YWGoodBean) shopCarYWGoodBean).getBuynum();
                ((YWGoodBean) shopCarYWGoodBean).setTotalmoney(YWStringUtils.getStanMoney((float) Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * ((YWGoodBean) shopCarYWGoodBean).getBuynum()));
                totalNumber += ((YWGoodBean) shopCarYWGoodBean).getBuynum();
            } else {//套餐的话   按这个计算价格
                MealsItemBean itemsBean = (MealsItemBean) shopCarYWGoodBean;
                totalMoney += Double.parseDouble(itemsBean.getMeal_goods_price()) * itemsBean.getBuynum();
                totalNumber += itemsBean.getBuynum();
                isHaveMeal = true;//代码走到这里，就已经包含订单了
            }
        }

        if (true) {//如果套餐也可以享受活动
            // TODO: 2019/7/31 这个是除了优惠券以后的优惠活动的计算价格

//              遍历一下  该使用哪个活动  activityitems 已经按顺序把活动优先级的顺序排列好了  接下来跟商品遍历就可以了
            //此时的购物车中  是没有
            activitBean = null;

            for (ActivityListBean.ItemsBean activityitem : activityitems) {//活动界面已经排序好了 直接按照顺序遍历就好了
                //   ：2打折促销，3第二件打折，4金额梯度优惠, 5满额打折'
                long start = MainAboutUtils.getmilles(activityitem.getStartTime());
                long end = MainAboutUtils.getmilles(activityitem.getEndTime());

                if (System.currentTimeMillis() < start || System.currentTimeMillis() > end) {//先判断活动时间是否符合,不符合跳过
                    return;
                }


                if (activityitem.getType() == 2) { //2 打折促销
                    if (activityitem.getIsgoods().equals("1")) {//所有商品生效
                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                            activitBean = activityitem;
                            activityIds = activitBean.getId();
                        }
                    } else {
                        for (YWGoodBean discountDetail : activityitem.getActivityGoods()) {
                            for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), discountDetail.getSku())) {
                                    activitBean = activityitem;
                                    activityIds = activitBean.getId();
                                }
                            }
                        }
                    }

                } else if (activityitem.getType() == 4) { //3 梯度折扣  修改成5
                    if (activityitem.getIsgoods().equals(1)) {//所有商品生效
                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                            activitBean = activityitem;
                            activityIds = activitBean.getId();

                        }
                    } else {
                        for (YWGoodBean gradientDetail : activityitem.getActivityGoods()) {
//                        if (gradientDetail.getIsActivity() == 2) {
                            for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), gradientDetail.getSku())) {
                                    activitBean = activityitem;
                                    activityIds = activitBean.getId();
                                }
                            }
                        }
                    }


                } else if (activityitem.getType() == 3) { //第二件打折
//                    Log.d("第二件打折",activityitem.getName()+"");
//                    if (activityitem.getIsgoods().equals(1)) {//所有商品生效
//                        for (Object shopCarYWGoodBean : shopCarYWGoodBeans) {//购物车里面没有套餐 都是商品
//                            activitBean = activityitem;
//                            activityIds = activitBean.getId();
//                        }
//                    } else {
//                        for (YWGoodBean secondDiscount : activityitem.getActivityGoods()) {
//                            for (Object shopCarYWGoodBean : shopCarYWGoodBeans) {//购物车里面没有套餐 都是商品
//                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getSku(), secondDiscount.getSku())) {
//                                    activitBean = activityitem;
//                                    activityIds = activitBean.getId();
//                                }
//                            }
//                        }
//                    }
                    for (YWGoodBean secondDiscount : activityitem.getActivityGoods()) {
                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                            if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), secondDiscount.getSku())) {
                                //第二件打折
                                double realMoney = 0D;
                                for (int i = 0; i < ((YWGoodBean) shopCarYWGoodBean).getBuynum(); i++) {
                                    if ((i + 1) % 2 == 0) {
                                        realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * Double.parseDouble(activityitem.getHalfPrice()) / 100;
//                                        Log.e("realMoney", realMoney + "   " + (Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice()) * Double.parseDouble(secondDiscount.getDiscount()) / 100)+"   "+secondDiscount.getDiscount());
                                    } else {
                                        realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice());
//                                        Log.e("realMoney", realMoney + "  " + ((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice());
                                    }
                                }
                                activitBean = activityitem;
                                Log.e("---gu---", "第二件打折 ");

                                ((YWGoodBean) shopCarYWGoodBean).setCountStr("第二件" + (Double.parseDouble(activityitem.getHalfPrice()) / 10) + "折");
                                ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) realMoney));
                            }
                        }
                    }

                    for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                        Log.d("disCountMoney价格233",disCountMoney+"");
                        disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                        Log.d("disCountMoney价格",disCountMoney+"");
                    }
                }
                if (activitBean != null) {// 如果已经匹配好了  跳出循环
                    break;
                }
            }


            if (activitBean != null) {//确定参与优惠活动的话  disCountMoney 的价格需要重新计算
                disCountMoney = 0f;
                Log.e("---gu---", "" + activitBean.getType());
                if (activitBean.getType() == 2) { //2打折促销
                    if (activitBean.getIsgoods().equals(1)) {//所有商品生效


                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品

                            if ( activitBean.getRebate()==null){
                                return;
                            }
                            //打折促销   就直接在   每种商品的总价    上打折
                            for (ActivityListBean.ItemsBean.GradientDetailsBean.Rebate bean : activitBean.getRebate()) {
                                if (((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) > Double.parseDouble(bean.getFull_number())) {
//                                    isHasZheKou = true;
                                    double itemmoney = ((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * Double.parseDouble(bean.getRebate_price()) / 100;
                                    ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) itemmoney));
                                }
                            }

                        }

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            Log.d("disCountMoney价格2",disCountMoney+"");
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                        }
                    } else {
                        for (YWGoodBean discountDetail : activitBean.getActivityGoods()) {

                            for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品

                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), discountDetail.getSku())) {
                                    //打折促销   就直接在   每种商品的总价    上打折
                                    if ( activitBean.getRebate()==null){
                                        return;
                                    }
                                    for (ActivityListBean.ItemsBean.GradientDetailsBean.Rebate bean : activitBean.getRebate()) {
                                        Log.d("折扣", bean.getFull_number() + "====" + bean.getRebate_price());
                                        if (((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) >= Double.parseDouble(bean.getFull_number())) {
//                                            isHasZheKou = true;
                                            Log.d("猪肉折扣", ((YWGoodBean) shopCarYWGoodBean).getBuynum() + "===" + Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) + "===" + Double.parseDouble(bean.getRebate_price()) / 100);
                                            double itemmoney = ((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * (Double.parseDouble(bean.getRebate_price()) / 100);
                                            ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) itemmoney));
                                            ((YWGoodBean) shopCarYWGoodBean).setCountStr(activitBean.getName());
                                        }
                                    }

                                }
                            }
                        }

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            Log.d("disCountMoney价格3",disCountMoney+"");
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                        }
                    }


                } else if (activitBean.getType() == 4) { //3梯度折扣 改为4
                    if (activitBean.getIsgoods().equals(1)) {//所有商品生效

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                            Log.e("---gu---", "梯度折扣:222");

                            Log.e("---gu---", "梯度折扣:333");
                            double itemMoney = ((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice());
                            ActivityListBean.ItemsBean.GradientDetailsBean.PriceValueBean priceValueBean = null;
                            for (int i = 0; i < activitBean.getPriceValue().size(); i++) {
                                if (itemMoney >= Double.parseDouble(activitBean.getPriceValue().get(i).getFull_number())) {
                                    priceValueBean = activitBean.getPriceValue().get(i);
                                } else {
                                    break;
                                }
                            }
                            Log.e("---gu---", "梯度折扣:444");
                            if (priceValueBean != null) {
                                Log.e("---gu---", "梯度折扣 ");
//                                isHasZheKou = true;
                                ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) (itemMoney - Double.parseDouble(priceValueBean.getReduce_price()))));
                                ((YWGoodBean) shopCarYWGoodBean).setCountStr("满" + priceValueBean.getFull_number() + "减" + priceValueBean.getReduce_price());
                            }

                        }


                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            Log.d("disCountMoney价格4",disCountMoney+"");
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                        }
                    } else {
                        Log.e("---gu---", "梯度折扣:111");
                        for (YWGoodBean gradientDetail : activitBean.getActivityGoods()) {
//                        if (gradientDetail.getIsActivity() == 2) {
                            for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                                Log.e("---gu---", "梯度折扣:222");
                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), gradientDetail.getSku())) {
                                    Log.e("---gu---", "梯度折扣:333");
                                    double itemMoney = ((YWGoodBean) shopCarYWGoodBean).getBuynum() * Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice());
                                    ActivityListBean.ItemsBean.GradientDetailsBean.PriceValueBean priceValueBean = null;
                                    for (int i = 0; i < activitBean.getPriceValue().size(); i++) {
                                        if (itemMoney >= Double.parseDouble(activitBean.getPriceValue().get(i).getFull_number())) {
                                            priceValueBean = activitBean.getPriceValue().get(i);
                                        } else {
                                            break;
                                        }
                                    }
                                    Log.e("---gu---", "梯度折扣:444");
                                    if (priceValueBean != null) {
                                        Log.e("---gu---", "梯度折扣 ");
//                                        isHasZheKou = true;
                                        ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) (itemMoney - Double.parseDouble(priceValueBean.getReduce_price()))));
                                        ((YWGoodBean) shopCarYWGoodBean).setCountStr("满" + priceValueBean.getFull_number() + "减" + priceValueBean.getReduce_price());
                                    }
                                }
                            }
                        }

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                            Log.d("disCountMoney价格5",disCountMoney+"");
                        }
                    }

                } else if (activitBean.getType() == 3) { //第二件打折
                    if (activitBean.getIsgoods().equals(1)) {//所有商品生效

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品

                            //第二件打折
                            double realMoney = 0D;
                            for (int i = 0; i < ((YWGoodBean) shopCarYWGoodBean).getBuynum(); i++) {
                                if ((i + 1) % 2 == 0) {
                                    realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * Double.parseDouble(activitBean.getHalfPrice()) / 100;
//                                        Log.e("realMoney", realMoney + "   " + (Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice()) * Double.parseDouble(secondDiscount.getDiscount()) / 100)+"   "+secondDiscount.getDiscount());
                                } else {
                                    realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice());
//                                        Log.e("realMoney", realMoney + "  " + ((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice());
                                }
                            }
                            Log.e("---gu---", "第二件打折 ");
//                            isHasZheKou = true;
                            ((YWGoodBean) shopCarYWGoodBean).setCountStr("第二件" + (Double.parseDouble(activitBean.getHalfPrice()) / 10) + "折");
                            ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) realMoney));
                        }


                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                            Log.d("disCountMoney价格6",disCountMoney+"");
                        }
                    } else {


                        for (YWGoodBean secondDiscount : activitBean.getActivityGoods()) {
                            for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {//购物车里面没有套餐 都是商品
                                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBean).getSku(), secondDiscount.getSku())) {
                                    //第二件打折
                                    double realMoney = 0D;
                                    for (int i = 0; i < ((YWGoodBean) shopCarYWGoodBean).getBuynum(); i++) {
                                        if ((i + 1) % 2 == 0) {
                                            realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice()) * Double.parseDouble(activitBean.getHalfPrice()) / 100;
//                                        Log.e("realMoney", realMoney + "   " + (Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice()) * Double.parseDouble(secondDiscount.getDiscount()) / 100)+"   "+secondDiscount.getDiscount());
                                        } else {
                                            realMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getPrice());
//                                        Log.e("realMoney", realMoney + "  " + ((YWGoodBean) shopCarYWGoodBean).getSpecsBean().getPrice());
                                        }
                                    }
                                    Log.e("---gu---", "第二件打折 ");
//                                    isHasZheKou = true;
                                    ((YWGoodBean) shopCarYWGoodBean).setCountStr("第二件" + (Double.parseDouble(activitBean.getHalfPrice()) / 10) + "折");
                                    ((YWGoodBean) shopCarYWGoodBean).setRealmoney(YWStringUtils.getStanMoney((float) realMoney));
                                }
                            }
                        }

                        for (Object shopCarYWGoodBean :  restingInfoBean.getShopCarYWGoodBeans()) {
                            disCountMoney += Double.parseDouble(((YWGoodBean) shopCarYWGoodBean).getRealmoney());
                        }
                    }

                }
            }

        } else {//如果包含了套餐   则使用优惠券之前的价格为  总价
            disCountMoney = totalMoney;
        }
        couponMoney = totalMoney;
        binding.creatTime.setText(restingInfoBean.getOrderNumberBean().getCreatTimeStr() + "下单" + totalNumber + "件");
        binding.totalSize.setText("共" + restingInfoBean.getShopCarYWGoodBeans().size() + "种商品");
        binding.allMoney.setText("￥" + YWStringUtils.getStanMoney((float) couponMoney));

        restingGoodsAdapter.setNewData(restingInfoBean.getShopCarYWGoodBeans());

//        vip部分
        if (restingInfoBean.getBean() != null) {
            binding.vipLayout.setVisibility(View.VISIBLE);
            binding.vipName.setText(restingInfoBean.getBean().getName());

            Glide.with(this)
                    .load(restingInfoBean.getBean().getHeadPortrait())
                    .apply(AppApplication.options)
                    .into(binding.vipHeadImage);
        } else {
            binding.vipLayout.setVisibility(View.GONE);
        }
    }
}
