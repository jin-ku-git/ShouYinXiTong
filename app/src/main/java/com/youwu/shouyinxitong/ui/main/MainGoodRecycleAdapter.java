package com.youwu.shouyinxitong.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;

import java.util.ArrayList;

/**
 * 首页分类适配器
 */
public class MainGoodRecycleAdapter extends RecyclerView.Adapter<MainGoodRecycleAdapter.myViewHodler> {
    private Context context;
    public ArrayList<GoodsTypesBean.GoodsTypeBean> transverseEntityList;

    private OnItemClickListener mOnItemClickListener;


    //创建构造函数
    public MainGoodRecycleAdapter(Context context, ArrayList<GoodsTypesBean.GoodsTypeBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.transverseEntityList = goodsEntityList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.adapter_goods_types, null);

        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, int position) {
        //根据点击位置绑定数据
        GoodsTypesBean.GoodsTypeBean data = transverseEntityList.get(position);
//        holder.mItemGoodsImg;
        holder.gg_name.setText(data.name);//获取实体类中的name字段并设置

        if (data.getType() == 1) {
            holder.layout_normal.setVisibility(View.VISIBLE);
            holder.img_add.setVisibility(View.GONE);

            if (data.currentSelect == 1) {

                holder.itemView.setBackgroundColor(Color.parseColor("#F4CCA1"));
                holder.gg_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.itemView.setBackgroundColor(0);
                holder.gg_name.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (data.getType() == 2) {
            holder.layout_normal.setVisibility(View.VISIBLE);
            holder.img_add.setVisibility(View.GONE);
//            helper.setGone(R.id.layout_normal, false)
//                    .setGone(R.id.img_add, true);
        } else {
            holder.layout_normal.setVisibility(View.VISIBLE);
            holder.img_add.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                RxToast.normal(data.name);

                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), transverseEntityList,position);


            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos, ArrayList<GoodsTypesBean.GoodsTypeBean> myLiveList,int position);
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return transverseEntityList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

            private TextView gg_name;
            private ImageView img_add;
            private LinearLayout layout_normal;




        public myViewHodler(View itemView) {
            super(itemView);

            gg_name=itemView.findViewById(R.id.type_name);//名称
            img_add=itemView.findViewById(R.id.img_add);//图片
            layout_normal=itemView.findViewById(R.id.layout_normal);



        }
    }

}