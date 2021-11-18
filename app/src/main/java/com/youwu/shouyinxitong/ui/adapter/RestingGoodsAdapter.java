package com.youwu.shouyinxitong.ui.adapter;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;


import java.util.List;

public class RestingGoodsAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public RestingGoodsAdapter(Context mContext, @Nullable List<Object> data) {
        super(R.layout.adapter_resting_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object it) {
        helper.setGone(R.id.lable, false);
        ImageView goodsimage = helper.getView(R.id.goods_image);
        ImageView goodsDiscontIv = helper.getView(R.id.goods_discont_iv);
        goodsDiscontIv.setVisibility(View.INVISIBLE);
        ImageView sellNull = helper.getView(R.id.sell_null);
        int subtotalCount = 0;
        if (it instanceof YWGoodBean) {
            YWGoodBean item = (YWGoodBean) it;
            helper.setText(R.id.goods_name, item.getProduct_name());
            helper.setText(R.id.goods_price, "￥" + item.getPrice());
            helper.setText(R.id.goods_number, item.getBuynum() + "");
//             Double allPrice = BigDecimalUtils.formatRoundUp(
////                    (item.getBuynum() * Double.parseDouble(item.getPrice()) * (Double.parseDouble(item.getDiscount()) / 100)), 1);
            Double preMoney = BigDecimalUtils.multiply(item.getPrice(), item.getBuynum() + "");//打折之前的金额
//            Double realMoney = BigDecimalUtils.subtract(preMoney + "", item.getDiscount_price());//原价-折扣金额，实际支付金额
            if (item.getDiscount().equals("-1")) {
                if (Double.parseDouble(item.getDiscount_price()) > 0) {
                    goodsDiscontIv.setVisibility(View.VISIBLE);
                } else {
                    goodsDiscontIv.setVisibility(View.INVISIBLE);
                }
                Double realMoney = BigDecimalUtils.subtract(preMoney + "", item.getDiscount_price());//原价-折扣金额，实际支付金额
//                helper.setText(R.id.discount_price, "" + item.getDiscount_price() );
                helper.setText(R.id.discount_price, "" + BigDecimalUtils.formatZero(item.getDiscount_price(), 2));
                helper.setText(R.id.goods_all_price, "￥" + BigDecimalUtils.formatZero(realMoney, 2));
            } else {
                if (!item.getDiscount().equals("100")) {
                    goodsDiscontIv.setVisibility(View.VISIBLE);
                } else {
                    goodsDiscontIv.setVisibility(View.INVISIBLE);
                }

                Double realMoney = Double.parseDouble(item.getDiscount()) * Double.parseDouble(item.getPrice()) * item.getBuynum() / 100; //折扣后的商品总额
                helper.setText(R.id.discount_price, "" + BigDecimalUtils.formatZero(BigDecimalUtils.subtract(preMoney + "", realMoney + ""), 2));
                helper.setText(R.id.goods_all_price, "￥" + BigDecimalUtils.formatZero(realMoney, 2));
            }

        } else {
            helper.setGone(R.id.lable, true);
            goodsDiscontIv.setVisibility(View.GONE);
            MealsItemBean item = (MealsItemBean) it;
            helper.setText(R.id.goods_name, item.getMeal_goods_name());
            helper.setText(R.id.goods_price, "￥" + item.getMeal_goods_price());
            helper.setText(R.id.goods_number, item.getBuynum() + "");
            Double preMoney = BigDecimalUtils.multiply(item.getMeal_goods_price(), item.getBuynum() + "");//打折之前的金额
            helper.setText(R.id.goods_all_price, "￥" + BigDecimalUtils.formatZero(preMoney, 2));
            helper.setText(R.id.discount_price, "0");
        }

    }


}
