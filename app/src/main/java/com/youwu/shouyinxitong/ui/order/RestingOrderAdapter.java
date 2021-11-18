package com.youwu.shouyinxitong.ui.order;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.RestingInfoBean;


import java.util.List;

public class RestingOrderAdapter extends BaseQuickAdapter<RestingInfoBean, BaseViewHolder> {
    public RestingOrderAdapter(Context mContext, @Nullable List<RestingInfoBean> data) {
        super(R.layout.adapter_resting_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RestingInfoBean item) {
        TextView orderNumber = helper.getView(R.id.order_number);
        TextView creatTime = helper.getView(R.id.creat_time);
        LinearLayout layout = helper.getView(R.id.layout);

        orderNumber.setText("牌号："+item.getOrderNumberBean().getOrderNumberStr());
        creatTime.setText(item.getOrderNumberBean().getCreatTimeStr());



        if (item.getSelect() == 1) {
            layout.setBackgroundResource(R.color.main_hue);
            orderNumber.setTextColor(mContext.getResources().getColor(R.color.white));
            creatTime.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            layout.setBackgroundResource(R.color.white);
            orderNumber.setTextColor(mContext.getResources().getColor(R.color.text_black));
            creatTime.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }





    }
}