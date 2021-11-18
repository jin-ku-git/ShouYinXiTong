package com.youwu.shouyinxitong.dialog;

import android.content.Context;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.view.MyCustKeybords;


public class ApplyOrderDialog extends CenterPopupView implements View.OnClickListener {

    TextView tvTitle;//商品名称

    EditText etNum;//商品数量

    TextView tvUnit;//商品单位

    MyCustKeybords customKeyboard;//键盘

    LinearLayout layoutUnit;//单位整个

    LinearLayout layoutRatio;//配比整个
    TextView tvRatio;//配比
    TextView tvRadioNum;//配比数量

    LinearLayout layoutPrice;//进货价整个
    EditText etPrice;//进货价

    TextView tvOriginNum;//原库存数量

    ImageView iv_close;//关闭按钮

    LinearLayout layoutRatioNum;
    private CommodityDetailBean commodityDetailBean;
    private OnconfirmListener listener;
    private int type = 1;//1是申请订货 2是进货 3是盘点 沽清

    public void setListener(OnconfirmListener listener) {
        this.listener = listener;
    }

    public ApplyOrderDialog(@NonNull Context context, CommodityDetailBean commodityDetailBean, int type) {
        super(context);
        this.commodityDetailBean = commodityDetailBean;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_apply_order;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        tvTitle=findViewById(R.id.tv_title);
        etNum=findViewById(R.id.et_num);
        tvUnit=findViewById(R.id.tv_unit);
        customKeyboard=findViewById(R.id.custom_keyboard);
        layoutUnit=findViewById(R.id.layout_unit);
        tvRatio=findViewById(R.id.tv_ratio);
        layoutPrice=findViewById(R.id.layout_price);
        etPrice=findViewById(R.id.et_price);
        tvOriginNum=findViewById(R.id.tv_origin_num);
        layoutRatio=findViewById(R.id.layout_ratio);
        tvRadioNum=findViewById(R.id.tv_radio_num);
        layoutRatioNum=findViewById(R.id.layout_ratio_num);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);


        if (commodityDetailBean == null) {
            return;
        }
        etNum.setInputType(InputType.TYPE_NULL);
        etPrice.setInputType(InputType.TYPE_NULL);
        if (commodityDetailBean.getCommodity_num() != null) {
            etNum.setText(commodityDetailBean.getCommodity_num()+"");
        } else {
            etNum.setText("0");
        }
        tvTitle.setText(commodityDetailBean.getCommodity_name());
        //如果为0的话改为1
        if (commodityDetailBean.getRatio_base() == 0) {
            commodityDetailBean.setRatio_base(1);
        }
        tvRatio.setText(commodityDetailBean.getRatio_base() + "");
        //1是申请订货 2是进货 3是盘点 沽清
        if (type == 1) {
            tvOriginNum.setVisibility(GONE);
            layoutPrice.setVisibility(GONE);
            layoutRatio.setVisibility(VISIBLE);
            layoutRatioNum.setVisibility(VISIBLE);
        } else if (type == 2) {
            tvOriginNum.setVisibility(GONE);
            commodityDetailBean.setRatio_base(1);
            tvRatio.setVisibility(GONE);
            layoutRatio.setVisibility(GONE);
            layoutRatioNum.setVisibility(GONE);
        } else if (type == 3) {
            tvOriginNum.setVisibility(VISIBLE);
            commodityDetailBean.setRatio_base(1);
            tvRatio.setVisibility(GONE);
            layoutPrice.setVisibility(GONE);
            layoutRatio.setVisibility(GONE);
            layoutRatioNum.setVisibility(GONE);
        }
        tvOriginNum.setText("原库存:" + commodityDetailBean.getCommodity_stock_num() + "");
        etPrice.setText(commodityDetailBean.getCommodity_purchase_price());
        if (TextUtils.isEmpty(commodityDetailBean.getCommodity_unit())) {
            layoutUnit.setVisibility(GONE);
        }
        tvUnit.setText(commodityDetailBean.getCommodity_unit());
        customKeyboard.bindEditText(etNum);
        customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if (listener != null) {
                    try {
                        float i = Float.parseFloat(etNum.getText().toString()) % commodityDetailBean.getRatio_base();
                        if (i != 0) {
//                            1是申请订货 2是进货 3是盘点 沽清
                            if (type == 3) {
                                ToastUtil.showShortToast("数量必须为整数！");
                            } else {
                                ToastUtil.showShortToast("数量必须为配比基数的整数倍！");
                            }
                            return;
                        }
                        if (type == 1) {
                            if (BigDecimalUtils.compare(etNum.getText().toString(), "0") <= 0) {
                                ToastUtil.showShortToast("数量必须大于0！");
                                return;
                            }
                        }
                        commodityDetailBean.setCommodity_num(etNum.getText().toString());
                        commodityDetailBean.setCommodity_purchase_price(etPrice.getText().toString());
                        commodityDetailBean.setCommodity_purchase_price_subtotal((Double.parseDouble(commodityDetailBean.getCommodity_num())*(Double.parseDouble(commodityDetailBean.getCommodity_purchase_price())))+"");


                        listener.onConfirm(commodityDetailBean);
                        dismiss();
                    } catch (Exception e) {
                        ToastUtil.showShortToast("数量必须为配比基数的整数倍！");
                    }

                }

            }
        });
        etNum.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    customKeyboard.bindEditText(etNum);
                }
            }
        });
        etPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    customKeyboard.bindEditText(etPrice);
                }
            }
        });
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                culRatioNum();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        culRatioNum();
    }

    public void culRatioNum() {
        String s = etNum.getText().toString();
        Double multiply = BigDecimalUtils.divide(s, commodityDetailBean.getRatio_base() + "");
        tvRadioNum.setText(BigDecimalUtils.formatZero(multiply, 1));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    public interface OnconfirmListener {
        void onConfirm(CommodityDetailBean goodBean);
    }
}
