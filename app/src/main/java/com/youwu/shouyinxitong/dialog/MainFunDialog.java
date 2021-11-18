package com.youwu.shouyinxitong.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.StoreInfo;
import com.youwu.shouyinxitong.base.BaseAdapter;
import com.youwu.shouyinxitong.bean_used.MainFunItemBean;
import com.youwu.shouyinxitong.bean_used.PemissionBean;
import com.youwu.shouyinxitong.ui.applygoods.ApplyOrderActivity;
import com.youwu.shouyinxitong.ui.applygoods.GoodsConfirmActivity;
import com.youwu.shouyinxitong.ui.calculate.CheckNumActivity;
import com.youwu.shouyinxitong.ui.calculate.InventoryActivity;
import com.youwu.shouyinxitong.ui.commodity.AddCommodityDetailActivity;
import com.youwu.shouyinxitong.ui.commodity.EditCommodityDetailActivity;
import com.youwu.shouyinxitong.ui.main.MainActivity;
import com.youwu.shouyinxitong.ui.order.SellOrderListActivity;
import com.youwu.shouyinxitong.ui.print.PrintSetupActivity;
import com.youwu.shouyinxitong.ui.record.RechargeOrdersActivity;
import com.youwu.shouyinxitong.ui.record.SalesCountActivity;
import com.youwu.shouyinxitong.ui.setup.HandoverActivity;
import com.youwu.shouyinxitong.ui.setup.SystemSettingActivity;
import com.youwu.shouyinxitong.ui.vip.AddVIPActivity;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class MainFunDialog extends CenterPopupView {
    RecyclerView recyView;
    private BaseAdapter<MainFunItemBean> adapter;

    public MainFunDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        recyView=findViewById(R.id.recy_view);
        findViewById(R.id.img_fun_close).setOnClickListener(view -> dismiss());
        recyView.addItemDecoration(new GridDividerItemDecoration(getContext(), 5, 2, Color.parseColor("#E4E4E4")));
        adapter = new BaseAdapter<MainFunItemBean>(R.layout.item_main_fun) {
            @Override
            protected void convert(BaseViewHolder helper, MainFunItemBean item) {
                helper.setText(R.id.tv_name, item.getName())
                        .setImageResource(R.id.iv_image, getMipmapId("icon_mainfun" + item.getType()));
            }
        };
        String json = getJson("main_fun.json");
        Type type = new TypeToken<List<MainFunItemBean>>() {
        }.getType();
        List<MainFunItemBean> data = AppApplication.gson.fromJson(json, type);

//        data.remove(5); // 移除 添加商品 模块
        List<PemissionBean> menu_data = StoreInfo.getStore().getMenu_data();
        List<MainFunItemBean> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < menu_data.size(); j++) {
                String name = data.get(i).getName();
                if (name.equals(menu_data.get(j).getLabel())) {
                    list.add(data.get(i));
                    menu_data.remove(j);
                    break;
                }
            }
        }

        adapter.setNewData(list);
        adapter.bindToRecyclerView(recyView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                switch (list.get(position).getType()) {
                    case 1://系统设置
                        getContext().startActivity(new Intent(getContext(), SystemSettingActivity.class));
                        break;
                    case 2://交接班
                        getContext().startActivity(new Intent(getContext(), HandoverActivity.class));
                        break;
                    case 3://打开钱箱
                        AppApplication.getInstance().openDraw();
                        ToastUtil.showTipToast(getContext(), "打开钱箱");
                        break;
                    case 4://新增会员
                        Intent addVip = new Intent(getContext(), AddVIPActivity.class);
                        addVip.putExtra("type",0);
                        getContext().startActivity(addVip);
                        break;
                    case 5://销售单据
                        getContext().startActivity(new Intent(getContext(), SellOrderListActivity.class));
                        break;
                    case 6://新增商品
                        getContext().startActivity(new Intent(getContext(), AddCommodityDetailActivity.class));
                        break;
                    case 7://编辑商品
                        getContext().startActivity(new Intent(getContext(), EditCommodityDetailActivity.class));
                        break;
                    case 8://盘点
                        getContext().startActivity(new Intent(getContext(), InventoryActivity.class));
                        break;
                    case 9://沽清
                        getContext().startActivity(new Intent(getContext(), CheckNumActivity.class));
                        break;
                    case 10://订货申请
                        getContext().startActivity(new Intent(getContext(), ApplyOrderActivity.class));
                        break;
//                    case 11://进货
//                        getContext().startActivity(new Intent(getContext(), PurchaseActivity.class));
//                        break;
                    case 12://配货确认
                        getContext().startActivity(new Intent(getContext(), GoodsConfirmActivity.class));
                        break;
                    case 13://打印设置
                        getContext().startActivity(new Intent(getContext(), PrintSetupActivity.class));
                        break;
                    case 14://备用金收支
                        //ToastUtil.showTipToast(getContext(), "备用金收支");
                        if (getContext() instanceof MainActivity) {
                            ((MainActivity) getContext()).showBackupCash();
                        }
                        break;
                    case 15://充值订单
                        getContext().startActivity(new Intent(getContext(), RechargeOrdersActivity.class));
                        break;
                    case 16://销售统计
                        getContext().startActivity(new Intent(getContext(), SalesCountActivity.class));
                        break;
//                    case 17://账期订单
//                        getContext().startActivity(new Intent(getContext(), AccountPeriodActivity.class));
//                        break;
                }
            }
        });

    }

    public int getMipmapId(String cparam) {
        return getContext().getResources().getIdentifier(cparam, "mipmap", getContext().getPackageName());
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_main_fun;
    }

    public String getJson(String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = getContext().getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toString();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * 0.8);
    }
}
