package com.youwu.shouyinxitong.dialog;

import android.content.Context;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.core.DrawerPopupView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.base.BaseAdapter;
import com.youwu.shouyinxitong.bean_used.CouponBean;
import com.youwu.shouyinxitong.bean.VIPBean;
import com.youwu.shouyinxitong.utils_tool.BeanCloneUtil;


public class CouponDialog extends DrawerPopupView implements View.OnClickListener {

    RecyclerView recyclerView;

    LinearLayout layoutChoose;

    TextView tvChooseVip;
    TextView tvCancel;
    TextView tvConfirm;
    TextView tvRefresh;
    private VIPBean vipBean;
    private CouponBean couponBean;
    private CounListener listener;

    public void setListener(CounListener listener) {
        this.listener = listener;
    }

    public CouponDialog(@NonNull Context context, VIPBean vipBean, CouponBean couponBean) {
        super(context);
        this.vipBean = vipBean;
        this.couponBean = couponBean;


    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_coupon;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView=findViewById(R.id.recyclerView);
        layoutChoose=findViewById(R.id.layout_choose);
        tvChooseVip=findViewById(R.id.tv_choose_vip);
        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);
        tvRefresh=findViewById(R.id.tv_refresh);

        tvChooseVip.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);


        BaseAdapter<CouponBean> adapter = new BaseAdapter<CouponBean>(R.layout.item_coupon) {
            @Override
            protected void convert(BaseViewHolder helper, CouponBean item) {
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_desc, item.getDetails())
                        .setText(R.id.tv_time, item.getStartTime() + "-" + item.getEndTime())
                        .setImageResource(R.id.iv_check, item.isSelect() ? R.mipmap.check_select : R.mipmap.check_no)

                ;
            }
        };
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter ada, View view, int position) {
                CouponBean item = adapter.getItem(position);
                if (item.isSelect()) {
                    item.setSelect(false);
                    couponBean = null;
                } else {
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        adapter.getItem(i).setSelect(false);
                    }
                    item.setSelect(true);
                    couponBean = BeanCloneUtil.cloneTo(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        if (vipBean != null) {
            tvChooseVip.setVisibility(GONE);
            layoutChoose.setVisibility(VISIBLE);
            if (vipBean.getCouponList() != null) {
                String coupon_id = "";
                if (couponBean != null) {
                    coupon_id = couponBean.getId();
                }
                for (int i = 0; i < vipBean.getCouponList().size(); i++) {
                    CouponBean item = vipBean.getCouponList().get(i);
                    if (item == null) {
                        return;
                    }
                    if (item.getId().equals(coupon_id)) {
                        item.setSelect(true);
                    } else {
                        item.setSelect(false);
                    }
                }
            }
            adapter.setNewData(vipBean.getCouponList());

        } else {
            tvChooseVip.setVisibility(VISIBLE);
            layoutChoose.setVisibility(GONE);
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_vip://选择会员
                listener.onSearchVip();
                break;
            case R.id.tv_cancel://取消选择
                listener.onCancleCoupon();
                break;
            case R.id.tv_confirm://确认选择
                listener.onChooseCoupon(couponBean);
                break;
            case R.id.tv_refresh://刷新
                listener.refreshVip();
                dismiss();
                break;

        }
        dismiss();
    }


    public interface CounListener {
        void onSearchVip();

        void onChooseCoupon(CouponBean bean);

        void onCancleCoupon();

        void refreshVip();

    }

}
