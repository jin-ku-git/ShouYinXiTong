package com.youwu.shouyinxitong.ui.commodity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.bean_used.GoodsDetailGroupBean;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.databinding.ActivityAddCommodityDetailBinding;
import com.youwu.shouyinxitong.utils_tool.GlideEngine;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.view.MySpinner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 添加商品
 * 2021/10/29
 * 金库
 */

public class AddCommodityDetailActivity extends BaseActivity<ActivityAddCommodityDetailBinding, AddCommodityViewModel> implements MySpinner.OnMultipleItemsSelectedListener{

    private List<GoodsTypesBean.GoodsTypeBean> categoryBeans;
    private CommodityDetailBean commodityDetailBean =new CommodityDetailBean();
    private CommodityDetailBean commodityDetailBeans =new CommodityDetailBean();

    private List<GoodsDetailGroupBean.ItemsBean> saleBeans;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        Intent intent=getIntent();
        if (intent != null) {
            commodityDetailBean = (CommodityDetailBean) intent.getSerializableExtra("bean");
            if (commodityDetailBean ==null){
                commodityDetailBeans.setJudge_type(1);
                commodityDetailBean = commodityDetailBeans;
            }else {
                commodityDetailBean.setJudge_type(2);
            }
        }


    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_add_commodity_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.IntegerNum.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){

                    case 1://选择图片
                        PictureSelector.create(AddCommodityDetailActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(PictureConfig.SINGLE)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                }
            }
        });
        viewModel.CommodityDetailLiveData.observe(this, new Observer<CommodityDetailBean>() {
            @Override
            public void onChanged(CommodityDetailBean json) {
                commodityDetailBean =json;

                List<Integer> listCategory = binding.spinnerGoodsCategoryId.getSelectedIndices();
                if (!listCategory.isEmpty()) {
                    int pos = listCategory.get(0);

                    commodityDetailBean.setCommodity_classification(categoryBeans.get(pos).getName());

                }

                Log.e("spinnerGoodsCategoryId---",binding.spinnerGoodsCategoryId.getTitle());
                Integer status = binding.switchBtnStatus.isChecked() ? 1 : 2;//1上架 2不上架
                Integer weigh = binding.switchBtnWeigh.isChecked() ? 2 : 1;
                //goods_type 商品类型 1标品、2称重品
                Log.e("weigh---->",weigh+"");
                commodityDetailBean.setGoods_type(weigh);
                commodityDetailBean.setCommodity_state(status);

                String submitJson = new Gson().toJson(commodityDetailBean);
                RxToast.normal("提交的json实体数据：\r\n"+submitJson);
                KLog.e("提交的json实体数据",submitJson);
            }
        });
        viewModel.Test.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("提交的json实体数据",s);
            }
        });
    }

    @Override
        public void initData() {
        super.initData();
        //分类
        binding.spinnerGoodsCategoryId.setListener(this);
        binding.spinnerGoodsCategoryId.setTitle(getString(R.string.goods_detail_spinner_category));
        binding.spinnerGoodsCategoryId.setMulti(false);
        //单位
        binding.spinnerGoodsUnit.setListener(this);
        binding.spinnerGoodsUnit.setTitle(getString(R.string.goods_detail_spinner_unit));
        binding.spinnerGoodsUnit.setMulti(false);
        //群组
        binding.spinnerGoodsGroup.setListener(this);
        binding.spinnerGoodsGroup.setTitle(getString(R.string.goods_detail_spinner_group));
        binding.spinnerGoodsGroup.setMulti(false);
        //类型
        binding.spinnerSaleGroup.setListener(this);
        binding.spinnerSaleGroup.setTitle(getString(R.string.goods_sale_spinner_group));
        binding.spinnerSaleGroup.setMulti(false);

        //上架 下架
        binding.switchBtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnStatus.isChecked()) {
                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(0xFFE48013));
                } else {
                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(0xFFB4B4B4));
                }
            }
        });
        if (commodityDetailBean !=null){
            //上下架
            if (commodityDetailBean.getCommodity_state()==1){
                binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(0xFFE48013));
            }else {
                binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(0xFFB4B4B4));
            }
            //称重
            if (commodityDetailBean.getGoods_type()==1){
                binding.switchBtnWeigh.setBackColor(ColorStateList.valueOf(0xFFE48013));
            }else {
                binding.switchBtnWeigh.setBackColor(ColorStateList.valueOf(0xFFB4B4B4));
            }
            //当编辑时
            if (commodityDetailBean.getJudge_type()==2){
                //单位不可修改
                List<String> units = new ArrayList<>();
                units.add(commodityDetailBean.getCommodity_company());
                binding.spinnerGoodsUnit.setItems(units);
                binding.spinnerGoodsUnit.setSelection(0);
                binding.spinnerGoodsUnit.setEnabled(false);

                binding.spinnerGoodsGroup.setEnabled(false);
                binding.spinnerSaleGroup.setEnabled(false);
            }


        }


        //称重
        binding.switchBtnWeigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnWeigh.isChecked()) {
                    binding.switchBtnWeigh.setBackColor(ColorStateList.valueOf(0xFFE48013));
                } else {
                    binding.switchBtnWeigh.setBackColor(ColorStateList.valueOf(0xFFB4B4B4));
                }
            }
        });

            viewModel.setCommodityDetailBean(commodityDetailBean);



        // 获取分类列表
        String categories = AppApplication.spUtils.getString("category");
        categoryBeans = JSON.parseArray(categories, GoodsTypesBean.GoodsTypeBean.class);

        List<String> types = new ArrayList<String>();
        for (int i = 0; i < categoryBeans.size(); i++) {
            types.add(i, categoryBeans.get(i).getName());
        }
        binding.spinnerGoodsCategoryId.setItems(types);

        // 获取商品类型列表
        String goodsSale = " {\"items\":[{\"id\":1,\"name\":\"成品\"},{\"id\":2,\"name\":\"辅料\"}],\"token\":\"1\"} ";
        GoodsDetailGroupBean goodsSaleBean = JSON.parseObject(goodsSale, GoodsDetailGroupBean.class);
        saleBeans = goodsSaleBean.getItems();
        List<String> sales = new ArrayList<String>();
        for (int i = 0; i < saleBeans.size(); i++) {
            sales.add(i, saleBeans.get(i).getName());
        }
        if (sales.size() > 0) {
            binding.spinnerSaleGroup.setItems(sales);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // todo:这里上传图片

//                    imgUrl = "";
                    for (LocalMedia media : selectList) {
                        try {
                            FileInputStream fis = new FileInputStream(media.getPath());
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                            binding.goodsImage.setImageBitmap(bitmap);
                            viewModel.entity.setCommodity_image(bitmap.toString());
//                            viewModel.commodityImage.set(bitmapToString(bitmap));

//                            byte[] bytes = QiNiuUtils.Bitmap2Bytes(bitmap);
//                            QiNiuUtils.uploadImg2QiNiu(bytes, this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices, MySpinner spinner) {

    }

    @Override
    public void selectedStrings(List<String> strings, MySpinner spinner) {

    }
}
