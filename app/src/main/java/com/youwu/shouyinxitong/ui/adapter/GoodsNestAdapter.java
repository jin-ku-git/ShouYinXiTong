package com.youwu.shouyinxitong.ui.adapter;

import com.youwu.lz.intelface.widget.nestfulllistview.NestFullViewAdapter;
import com.youwu.lz.intelface.widget.nestfulllistview.NestFullViewHolder;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;


import java.util.List;

public class GoodsNestAdapter extends NestFullViewAdapter<MealGoodsBean> {
    public GoodsNestAdapter(List<MealGoodsBean> mDatas) {
        super(R.layout.item_goods, mDatas);
    }

    @Override
    public void onBind(int pos, MealGoodsBean goodBean, NestFullViewHolder holder) {
        if (goodBean != null) {
            holder.setText(R.id.name, goodBean.getGoods_name())
                    .setText(R.id.buy_num, goodBean.getGoods_number() + "");
        }
    }
}
