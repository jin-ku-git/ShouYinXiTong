/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.shouyinxitong.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.StoreInfo;
import com.youwu.shouyinxitong.bean_used.CouponBean;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.ui.adapter.RestingGoodsAdapter;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;


import java.util.ArrayList;
import java.util.List;


public class ShowShopingDisplay extends BasePresentation {

    Banner banner;
    RelativeLayout videoviewRelativeLayout;

    VideoView videoView;
    LinearLayout showLayout;

    RecyclerView recyclerView;

    ImageView codeUrl;

    CircularImage vipHeadImage;

    TextView vipName;

    TextView vipMoney;

    LinearLayout noVipLayout;
    RelativeLayout vipLayout;
    TextView tvCoupon;
    TextView tvTotalMoney;
    TextView tvDiscountMoney;
    TextView tvZhekouMoney;
    TextView mustPay;
    TextView goodsNumber;

    TextView tvDuihuan;

    private Context outerContext;
    private RestingGoodsAdapter shopCarGoodsAdapter;
    private List shopCarYWGoodBeans;
    private CouponBean coupon;


    public ShowShopingDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        this.outerContext = outerContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shoping);

        banner=findViewById(R.id.banner);
        videoviewRelativeLayout=findViewById(R.id.videoview_relativeLayout);
        videoView=findViewById(R.id.videoview);
        showLayout=findViewById(R.id.show_layout);
        recyclerView=findViewById(R.id.recyclerView);
        codeUrl=findViewById(R.id.code_url);
        vipHeadImage=findViewById(R.id.vip_head_image);
        vipName=findViewById(R.id.vip_name);
        vipMoney=findViewById(R.id.vip_money);
        noVipLayout=findViewById(R.id.no_vip_layout);
        vipLayout=findViewById(R.id.vip_layout);
        tvCoupon=findViewById(R.id.tv_coupon);
        tvTotalMoney=findViewById(R.id.tv_total_money);
        tvDiscountMoney=findViewById(R.id.tv_discount_money);
        tvZhekouMoney=findViewById(R.id.tv_zhekou_money);
        mustPay=findViewById(R.id.must_pay);
        goodsNumber=findViewById(R.id.goods_number);
        tvDuihuan=findViewById(R.id.tv_duihuan);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 通过MediaPlayer设置循环播放
                mp.setLooping(true);
                // OnPreparedListener中的onPrepared方法是在播放源准备完成后回调的，所以可以在这里开启播放
                mp.start();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(outerContext));
        shopCarYWGoodBeans = new ArrayList<>();
        shopCarGoodsAdapter = new RestingGoodsAdapter(outerContext, shopCarYWGoodBeans);
        shopCarGoodsAdapter.bindToRecyclerView(recyclerView);
        recyclerView.swapAdapter(shopCarGoodsAdapter, true);

        //小程序码
        Glide.with(outerContext)
//                 .load(CashierInfo.getCashier().getCode_url())
                .load(StoreInfo.getStore().getCode_url())
//                .load(R.drawable.wechat_img)
                .apply(AppApplication.options)
                .into(codeUrl);
    }

    public void addGoods(Object ywGoodBean, int from) {
//      YWGoodBean ywGoodBean = BeanCloneUtil.cloneTo(ywGood);
        banner.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);

        int hasInShopCar = -1;

        for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
            if (ywGoodBean instanceof YWGoodBean && shopCarYWGoodBeans.get(i) instanceof YWGoodBean && TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getSku(), ((YWGoodBean) ywGoodBean).getSku())) {
                if (from == 2) {
                    //如果是修改直接中断循环
                    hasInShopCar = i;
                    break;
                } else {
                    //不是修改判断折扣是否相等
                    if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getDiscount(), ((YWGoodBean) ywGoodBean).getDiscount())) {
                        hasInShopCar = i;
                    } else {
                        hasInShopCar = -1;
                    }
                }

            }
        }
        if (hasInShopCar == -1) {
//            shopCarYWGoodBeans.get(hasInShopCar).getSpecsBean().setBuynum(ywGoodBean.getSpecsBean().getBuynum());
            shopCarYWGoodBeans.add(0, ywGoodBean);
        } else {
            ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).setBuynum(((YWGoodBean) ywGoodBean).getBuynum());
            ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).setDiscount(((YWGoodBean) ywGoodBean).getDiscount());
        }
        shopCarGoodsAdapter.notifyDataSetChanged();

    }

    public void setList(List list) {
        banner.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);
        shopCarYWGoodBeans.clear();
        shopCarYWGoodBeans.addAll(list);
        shopCarGoodsAdapter.notifyDataSetChanged();
    }

    public void removeGoods(YWGoodBean ywGoodBean) {

        int hasInShopCar = -1;
        for (int i = 0; i < shopCarYWGoodBeans.size(); i++) {
            if (shopCarYWGoodBeans.get(i) instanceof YWGoodBean) {
                if (TextUtils.equals(((YWGoodBean) shopCarYWGoodBeans.get(i)).getSku(), ((YWGoodBean) ywGoodBean).getSku())) {
                    hasInShopCar = i;
                }
            }
        }
        if (hasInShopCar == -1) {
            return;
        }

        int number = ywGoodBean.getBuynum();
        if (number == 0) {
            shopCarYWGoodBeans.remove(hasInShopCar);
        } else {
            ((YWGoodBean) shopCarYWGoodBeans.get(hasInShopCar)).setBuynum(number);
        }


        if (shopCarYWGoodBeans.size() == 0) {
            banner.setVisibility(View.VISIBLE);
            showLayout.setVisibility(View.GONE);
        }

        shopCarGoodsAdapter.notifyDataSetChanged();

    }

    public void remove(int position) {
        shopCarYWGoodBeans.remove(position);
        shopCarGoodsAdapter.notifyDataSetChanged();

    }

    public void cleanShopCar() {
        tvDuihuan.setText("");
        tvDuihuan.setVisibility(View.GONE);
        noVipLayout.setVisibility(View.VISIBLE);
        vipLayout.setVisibility(View.GONE);

        banner.setVisibility(View.VISIBLE);
        showLayout.setVisibility(View.GONE);
        shopCarYWGoodBeans.clear();
        shopCarGoodsAdapter.notifyDataSetChanged();

    }


    public void countMoney(double totalMoney, double disCountMoney, int buyCount, double couponMoney, double zhekoumoney) {
        tvTotalMoney.setText("商品原价：￥" + YWStringUtils.getStanMoney((float) totalMoney));
        goodsNumber.setText("共" + shopCarYWGoodBeans.size() + "种 " + buyCount + "件");
        tvDiscountMoney.setText("优惠金额：￥" + YWStringUtils.getStanMoney((float) couponMoney));
        tvZhekouMoney.setText("折扣金额：￥" + YWStringUtils.getStanMoney((float) zhekoumoney));
        mustPay.setText("应付：￥" + YWStringUtils.getStanMoney((float) (totalMoney - couponMoney - zhekoumoney)));
    }


    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
        tvCoupon.setText("已经使用 " + coupon.getName());
        tvCoupon.setVisibility(View.VISIBLE);
    }

    public void setCouponName(YWGoodBean ywGoodBean) {
        tvDuihuan.setVisibility(View.VISIBLE);
        tvDuihuan.setText("已兑换" + ywGoodBean.getProduct_name() + "1份");
    }

    public void cancleCoupon() {
        this.coupon = null;
        tvCoupon.setVisibility(View.GONE);
        tvDuihuan.setText("");
        tvDuihuan.setVisibility(View.GONE);
    }


    public void setvip(VIPBean vipBean) {
        Glide.with(outerContext)
                .load(vipBean.getHeadPortrait())
                .apply(AppApplication.options)
                .into(vipHeadImage);
        vipMoney.setText("余额：" + vipBean.getBalance() + "元");
        vipName.setText(vipBean.getName());

        noVipLayout.setVisibility(View.GONE);
        vipLayout.setVisibility(View.VISIBLE);

        banner.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);
    }

    public void canclevip() {
        noVipLayout.setVisibility(View.VISIBLE);
        vipLayout.setVisibility(View.GONE);
    }


    public void initBannnerView() {
        //显示图片
        if (AppApplication.systemBannerBean.getItems() == null) {
            List<Integer> bannerImgList = new ArrayList<>();
            int resource = R.mipmap.bg;
            bannerImgList.add(resource);
            int time = Integer.parseInt(AppApplication.spUtils.getString("switchingTime"));
            banner.setDelayTime(time * 1000);     //设置轮播时间
            banner.setImages(bannerImgList)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {
            List<String> bannerImgList = new ArrayList<>();
            for (int i = 0; i < AppApplication.systemBannerBean.getItems().size(); i++) {
                bannerImgList.add(AppApplication.systemBannerBean.getItems().get(i).getImage() + "");
            }
            int time = Integer.parseInt(AppApplication.spUtils.getString("switchingTime"));
            banner.setDelayTime(time * 1000);     //设置轮播时间
            banner.setImages(bannerImgList)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        }

        if (shopCarYWGoodBeans.size() > 0) {
            banner.setVisibility(View.GONE);
            showLayout.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
            showLayout.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
        }

//        if (AppApplication.spUtils.getString("bannerType").equals("imageSbutton")){
//            //显示图片
//           if (AppApplication.systemBannerBean.getItems() == null){
//               List<Integer> bannerImgList = new ArrayList<>();
//               int resource = R.mipmap.bg;
//               bannerImgList.add(resource);
//               int time = Integer.parseInt( AppApplication.spUtils.getString("switchingTime") );
//               banner.setDelayTime(time * 1000);     //设置轮播时间
//               banner.setImages(bannerImgList)
//                       .setImageLoader(new GlideImageLoader())
//                       .start();
//           }else {
//               List<String> bannerImgList = new ArrayList<>();
//               for (int i=0;i< AppApplication.systemBannerBean.getItems().size();i++){
//                   bannerImgList.add(AppApplication.systemBannerBean.getItems().get(i).getImage()+"");
//               }
//               int time = Integer.parseInt( AppApplication.spUtils.getString("switchingTime") );
//               banner.setDelayTime(time * 1000);     //设置轮播时间
//               banner.setImages(bannerImgList)
//                       .setImageLoader(new GlideImageLoader())
//                       .start();
//           }
//            if (shopCarYWGoodBeans.size() > 0) {
//                banner.setVisibility(View.GONE);
//                showLayout.setVisibility(View.VISIBLE);
//                videoView.setVisibility(View.GONE);
//            } else {
//                banner.setVisibility(View.VISIBLE);
//                showLayout.setVisibility(View.GONE);
//                videoView.setVisibility(View.GONE);
//            }
//
//        }else {
//            //显示视频
////            String videoUrl = AppApplication.systemBannerBean.getVideo()+"";
//            String videoUrl = "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4";
//            if (!videoUrl.equals("")){
//                Uri uri = Uri.parse( videoUrl );
//                //设置视频控制器
//                videoView.setMediaController(new MediaController(getContext()));
//                //播放完成回调
//                videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
//                //设置视频路径
//                videoView.setVideoURI(uri);
//                //开始播放视频
//                videoView.start();
//            }
//
//            if (shopCarYWGoodBeans.size() > 0) {
//                banner.setVisibility(View.GONE);
//                showLayout.setVisibility(View.VISIBLE);
//                videoView.pause();
//                videoView.setVisibility(View.GONE);
//                videoviewRelativeLayout.setVisibility(View.GONE);
//            } else {
//                banner.setVisibility(View.GONE);
//                showLayout.setVisibility(View.GONE);
//                videoView.start();
//                videoView.setVisibility(View.VISIBLE);
//                videoviewRelativeLayout.setVisibility(View.VISIBLE);
//            }
//
//        }


    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(getContext(), "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


}
