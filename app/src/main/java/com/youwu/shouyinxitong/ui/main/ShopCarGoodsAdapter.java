package com.youwu.shouyinxitong.ui.main;

import android.content.Context;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;


import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ShopCarGoodsAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private OnClickViewLister onClickViewLister;

    public OnClickViewLister getOnClickViewLister() {
        return onClickViewLister;
    }

    public void setOnClickViewLister(OnClickViewLister onClickViewLister) {
        this.onClickViewLister = onClickViewLister;
    }

    public ShopCarGoodsAdapter(Context mContext, @Nullable List<Object> data) {
        super(R.layout.adapter_shop_car_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object it) {
        ImageView add = helper.getView(R.id.add);
        ImageView remove = helper.getView(R.id.remove);
        ImageView imgItemClose = helper.getView(R.id.img_item_close);
        int position = helper.getLayoutPosition() + 1;

        if (it instanceof YWGoodBean) {//商品
            YWGoodBean item = (YWGoodBean) it;

            String sepcs = item.product_name;
            helper.setText(R.id.name, (position < 10 ? "0" + position : position) + "." + item.getProduct_name());
            helper.setText(R.id.buy_num, item.getBuynum() + "");
            Double yuanjia = BigDecimalUtils.multiply(item.getPrice(), item.getBuynum() + "");
            Double multiply1 = BigDecimalUtils.multiply(yuanjia + "", item.getDiscount());
            Double total = BigDecimalUtils.divide(multiply1, 100);//折扣后的价格
            Double dicoutn = BigDecimalUtils.subtract(total, yuanjia);
            helper.setText(R.id.tv_discount_money, "折扣：" + BigDecimalUtils.formatRoundUp(dicoutn, 2))
                    .setText(R.id.tv_total_money, "小计：" + BigDecimalUtils.formatRoundUp(total, 2) + "")
                    .setText(R.id.tv_remarks, item.getRemarks())
                    .setGone(R.id.lable,false)
                    .setGone(R.id.tv_discount_money, Float.parseFloat(item.getDiscount()) != 100 ? true : false)
                    .setGone(R.id.iv_discount, Float.parseFloat(item.getDiscount()) != 100 ? true : false)
                    .setGone(R.id.tv_weightnum, item.getGoods_type() == 2 ? true : false)
                    .setGone(R.id.tv_remarks, (TextUtils.isEmpty(item.getRemarks()) ? false : true))
                    .setGone(R.id.tv_total_money, (Float.parseFloat(item.getDiscount()) != 100 || item.getBuynum() > 1) ? true : false);

            if (item.goods_type==2){
                if (item.weight_type==16){
                    helper.setText(R.id.tv_remarks,item.weightnum+"克");
                }else {
                    helper.setText(R.id.tv_remarks,item.weightnum+"千克");
                }

            }

            if (item.goods_type==2){
                helper.setText(R.id.tv_price, "￥" + YWStringUtils.getStanMoney((float) (Double.valueOf(item.getPrice()).doubleValue()*item.weightnum)) + "");

            }else {
                helper.setText(R.id.tv_price, "￥" + item.getPrice() + "");
            }



            RxView.clicks(add).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {

                    onClickViewLister.addGoodsListering(item, helper.getAdapterPosition());

//                    ToastUtil.showLongToast("getAdapterPosition"+helper.getAdapterPosition());
                }
            });
            RxView.clicks(remove).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }).start();
                    onClickViewLister.removeGoodsListering(it, helper.getAdapterPosition());

//                    ToastUtil.showLongToast("getAdapterPosition"+helper.getAdapterPosition());
                }
            });

            RxView.clicks(imgItemClose).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {

                    onClickViewLister.removeItemGoodsListering(it, helper.getLayoutPosition());

//                    ToastUtil.showLongToast("getAdapterPosition"+helper.getAdapterPosition());
                }
            });


        } else {//套餐
            MealsItemBean item = (MealsItemBean) it;
            helper.setText(R.id.name, (position < 10 ? "0" + position : position) + "." + item.getMeal_goods_name());
            Double total = BigDecimalUtils.multiply(item.getMeal_goods_price(), item.getBuynum() + "");

            helper.setText(R.id.buy_num, item.getBuynum()+"")
                    .setText(R.id.tv_price, "￥" + item.getMeal_goods_price())
                    .setGone(R.id.tv_remarks,false)
                    .setGone(R.id.iv_discount,false)
                    .setGone(R.id.tv_weightnum,false)
                    .setGone(R.id.lable,true)
                    .setGone(R.id.tv_discount_money,false)
                    .setGone(R.id.tv_total_money,item.getBuynum() > 1 ? true : false)
                    .setText(R.id.tv_total_money, "小计：" + BigDecimalUtils.formatRoundUp(total, 2) + "")
            ;
            RxView.clicks(add).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    ToastUtil.showLongToast("同一种套餐一单只能购买一份");

                }
            });
            RxView.clicks(remove).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    onClickViewLister.removeGoodsListering(it, helper.getAdapterPosition());
                }
            });

            RxView.clicks(imgItemClose).throttleFirst(0, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {

                    onClickViewLister.removeItemGoodsListering(it, helper.getLayoutPosition());

//                    ToastUtil.showLongToast("getAdapterPosition"+helper.getAdapterPosition());
                }
            });

        }
    }

    public interface OnClickViewLister {
        public void addGoodsListering(YWGoodBean item, int position);

        public void removeGoodsListering(Object item, int position);

        public void removeItemGoodsListering(Object item, int position);
    }


}
