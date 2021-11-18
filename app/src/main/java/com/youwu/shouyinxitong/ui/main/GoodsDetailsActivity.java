package com.youwu.shouyinxitong.ui.main;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.util.XPopupUtils;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.databinding.ActivityGoodsDetailsBinding;
import com.youwu.shouyinxitong.dialog.DiscountDialog;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;

import me.goldze.mvvmhabit.base.BaseActivity;


/**
 * 商品详情弹窗
 * 2021/10/25
 *
 */

public class GoodsDetailsActivity extends BaseActivity<ActivityGoodsDetailsBinding, GoodsDetailsViewModel> {


    private Object data;
    private int isCar;

    private DiscountDialog discountDialog;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        data = getIntent().getSerializableExtra("data");
        //0是加入 1是购物车进来
        isCar = getIntent().getIntExtra("isCar", 0);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_goods_details;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        //View层传参到ViewModel层

        if (data instanceof YWGoodBean){
            binding.layoutDetails.setVisibility(View.INVISIBLE);
            YWGoodBean goodBean = (YWGoodBean) data;

            if (isCar <= 0) {
                binding.tvBuyCount.setText("1");
            } else {
                binding.tvBuyCount.setText(goodBean.getBuynum() + "");
            }

            goodBean.setIsCar(isCar);
            viewModel.setYType(0);
            viewModel.setYWGoodBean(goodBean);

            binding.tvTotalMoney.setText("￥" + BigDecimalUtils.multiply(goodBean.price, binding.tvBuyCount.getText().toString()));
        }else if (data instanceof MealsItemBean){
            MealsItemBean item = (MealsItemBean) data;
            viewModel.setYType(1);
            binding.layoutDetails.setVisibility(View.VISIBLE);
            binding.tvDiscountMenu.setVisibility(View.GONE);
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
            GoodsDeatalsListAdapter adapter = new GoodsDeatalsListAdapter(this,item.getMeal_store_goods_detail());
            binding.recyclerView.setAdapter(adapter);

            if (isCar <= 0) {
                binding.tvBuyCount.setText("1");
            } else {
                binding.tvBuyCount.setText(item.getBuynum() + "");

            }

            item.setIsCar(isCar);
            viewModel.setMealsItemBean(item);
            binding.tvTotalMoney.setText("￥" + BigDecimalUtils.multiply(item.getMeal_goods_price(), binding.tvBuyCount.getText().toString()));
        }


        binding.tvDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (binding.price.hasFocus()) {
                    return;
                }
                if (!binding.price.hasFocus() && !binding.tvDiscount.hasFocus()) {
                    return;
                }
                String s = binding.tvDiscount.getText().toString();
                if (s.isEmpty()) {

                } else {
                    Double multiply = BigDecimalUtils.multiply(s, binding.tvPrice.getText().toString());
                    binding.price.setText(BigDecimalUtils.divide(multiply, 100) + "");
                    cul();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //折扣率
        binding.tvDiscount.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(binding.tvDiscount);
                } else {
                    if (discountDialog != null) {
                        discountDialog.dismiss();
                    }
                }
            }
        });
        binding.price.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(binding.price);
                } else {
                    if (discountDialog != null) {
                        discountDialog.dismiss();
                    }
                }
            }
        });
        //价格
        binding.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.tvDiscount.hasFocus()) {
                    return;
                }
                if (!binding.price.hasFocus() && !binding.tvDiscount.hasFocus()) {
                    return;
                }
                String s = binding.price.getText().toString();
                if (TextUtils.isEmpty(s)) {
                } else {
                    Double multiply = BigDecimalUtils.multiply(s, 100 + "");
                    Double divide = BigDecimalUtils.divide(multiply + "", binding.tvPrice.getText().toString());
                    binding.tvDiscount.setText(BigDecimalUtils.formatRoundUp(divide, 2) + "");
                    cul();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //增加数量
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = binding.tvBuyCount.getText().toString();
                int countInt = Integer.parseInt(count);
                binding.tvBuyCount.setText(countInt + 1 + "");
                cul();
            }
        });
        binding.ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = binding.tvBuyCount.getText().toString();
                int countInt = Integer.parseInt(count);
                if (countInt > 1) {
                    binding.tvBuyCount.setText(countInt - 1 + "");
                    cul();
                }
            }
        });

    }
    //计算价格
    private void cul() {
        if (data instanceof YWGoodBean){
            binding.tvTotalMoney.setText("￥" + BigDecimalUtils.multiply(binding.price.getText().toString(), binding.tvBuyCount.getText().toString()));
        }else {
            MealsItemBean item = (MealsItemBean) data;
            binding.tvTotalMoney.setText("￥" + BigDecimalUtils.multiply(item.getMeal_goods_price(), binding.tvBuyCount.getText().toString()));
        }

    }

    public void showDialog(EditText view) {
        discountDialog = new DiscountDialog(this, view);

        new XPopup.Builder(this)
                .atView(view)
                .autoFocusEditText(false)
                .isRequestFocus(false)
                .hasShadowBg(false)
                .popupPosition(PopupPosition.Bottom)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onCreated() { }
                    @Override
                    public void beforeShow() { }
                    @Override
                    public void onShow() { }
                    @Override
                    public void onDismiss() {
                        //如果为空或者为0时，强制改为相应的
                        String money = binding.price.getText().toString();
                        String dicount = binding.tvDiscount.getText().toString();
                        if (data instanceof YWGoodBean) {
                            if (money.isEmpty() || BigDecimalUtils.compare(money, "0") <= 0) {
                                ToastUtil.showShortToast("金额必须大于0");
                                binding.price.setText(((YWGoodBean) data).getPrice());
                                dicount = ((YWGoodBean) data).getDiscount();
                                binding.tvDiscount.setText(((YWGoodBean) data).getDiscount());
                            }
                            if (dicount.isEmpty() || BigDecimalUtils.compare(dicount, "0") <= 0) {
                                ToastUtil.showShortToast("折扣必须大于0");
                                binding.price.setText(((YWGoodBean) data).getPrice());
                                binding.tvDiscount.setText(((YWGoodBean) data).getDiscount());
                            }
                        }
                        binding.price.clearFocus();
                        binding.tvDiscount.clearFocus();
                    }

                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }
                })
                .offsetX(-XPopupUtils.dp2px(this, 150) + view.getWidth() / 2)
                .asCustom(discountDialog)
                .show();

    }

    @Override
    public void initViewObservable() {

        //返回按钮的监听
        viewModel.loadUrlEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer type) {
                String count = binding.tvBuyCount.getText().toString();
                int countInt = Integer.parseInt(count);
                switch (type){
                    case 1://返回
                        finish();
                        break;
                    case 2://取消折扣和打开折扣
                        if (data instanceof YWGoodBean) {
                            if (binding.layoutDiscount.getVisibility() == View.VISIBLE) {
                                binding.layoutDiscount.setVisibility(View.GONE);
                                binding.tvDiscountMenu.setText("单品折扣");
                                binding.price.setText(((YWGoodBean) data).getPrice());
                                binding.tvDiscount.setText("100");
                            } else {
                                //取消折扣，恢复原价
                                binding.layoutDiscount.setVisibility(View.VISIBLE);
                                binding.tvDiscountMenu.setText("取消折扣");
                            }
                        }
                        break;
                    case 3://数量增加
                        binding.tvBuyCount.setText(countInt + 1 + "");
                        cul();
                        break;
                    case 4://减少
                        if (countInt > 1) {
                            binding.tvBuyCount.setText(countInt - 1 + "");
                            cul();
                        }
                        break;
                }

            }
        });
    }

}
