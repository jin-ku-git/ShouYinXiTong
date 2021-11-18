package com.youwu.shouyinxitong.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lid.lib.LabelTextView;
import com.youwu.lz.intelface.widget.nestfulllistview.NestFullListView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.ui.adapter.GoodsNestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页商品列表适配器
 */
public class MainGoodListRecycleAdapter extends RecyclerView.Adapter<MainGoodListRecycleAdapter.myViewHodler> {
    private Context context;
    public List<Object> goods_list;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    private int priceType = 1;

    RequestOptions options = new RequestOptions()
            .error(R.mipmap.icon_goods_default)
            .placeholder(R.mipmap.icon_goods_default);


    //创建构造函数
    public MainGoodListRecycleAdapter(Context context, List<Object> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.goods_list = goodsEntityList;//实体类数据ArrayList
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
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
        View itemView = View.inflate(context, R.layout.adapter_goods_list, null);

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
        Object data = goods_list.get(position);


        holder.layout_goods.setVisibility(View.VISIBLE);
        holder.lable.setVisibility(View.GONE);
        holder.nestFull.setVisibility(View.GONE);
        holder.lookMore.setVisibility(View.GONE);
        holder.iv_add.setVisibility(View.GONE);


        /******************************************/

        if (data instanceof YWGoodBean) {
            YWGoodBean item = (YWGoodBean) data;
            if (item.getSpecs().size() > 0) {

                holder.goods_name.setText(item.getProduct_name());

                if (priceType == 1) {
                    holder.tv_price.setText("￥" + item.getPrice());
                } else if (priceType == 2) {
                    holder.tv_price.setText("￥" + item.getCost_price());
                } else if (priceType == 3) {
                    holder.tv_price.setText(item.getInventory() + "");
                }


                Glide.with(context)
                        .load(item.getImage())
                        .apply(options)
                        .into(holder.goods_image);

                if (item.getSales_status().equals("2")) {
                    holder.goods_image.setVisibility(View.GONE);
                } else {
                    holder.goods_image.setVisibility(View.VISIBLE);
                }
            }

        } else if (data instanceof MealsItemBean) {
            MealsItemBean item = (MealsItemBean) data;

            holder.lable.setVisibility(View.VISIBLE);
            holder.nestFull.setVisibility(View.VISIBLE);
            holder.goods_name.setText(item.getMeal_goods_name());
            holder.tv_price.setText("￥" + item.getMeal_goods_price());


            Glide.with(context)
                    .load(item.getImg())
                    .apply(options)
                    .into(holder.goods_image);
            if (item.getSales_status().equals("2")) {
                holder.sell_null.setVisibility(View.VISIBLE);
            } else {
                holder.sell_null.setVisibility(View.GONE);
            }

            List<MealGoodsBean> goods_detail = item.getMeal_store_goods_detail();
            List<MealGoodsBean> list = new ArrayList<>();
            if (goods_detail.size() > 5) {
                holder.lookMore.setVisibility(View.VISIBLE);
                for (int i = 0; i < 4; i++) {
                    list.add(goods_detail.get(i));
                }
            } else {
                holder.lookMore.setVisibility(View.GONE);
                list.addAll(goods_detail);
            }
            GoodsNestAdapter adapter = new GoodsNestAdapter(list);
            holder.nestFull.setAdapter(adapter);


        } else {
            holder.layout_goods.setVisibility(View.GONE);
            holder.iv_add.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), data,position);
            }
        });
        //点击图片
        holder.goods_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), data,position);
            }
        });
        //点击商品
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemLongClickListener.onItemLongClickListener(holder.getAdapterPosition(), data,position);
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos, Object myLiveList, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(int pos, Object myLiveList, int position);
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return goods_list.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {


            private FrameLayout layout_goods;
            private ImageView goods_image,sell_null;
            private TextView goods_name,tv_price;//名称，价格
            private LabelTextView lable;//套餐标志
            private NestFullListView nestFull;
            private TextView lookMore;//查看更多
            private TextView iv_add;//新增商品
        private LinearLayout layout;



        public myViewHodler(View itemView) {
            super(itemView);


            layout_goods=itemView.findViewById(R.id.layout_goods);
            goods_image=itemView.findViewById(R.id.goods_image);
            sell_null=itemView.findViewById(R.id.sell_null);
            goods_name=itemView.findViewById(R.id.goods_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            lable=itemView.findViewById(R.id.lable);
            nestFull=itemView.findViewById(R.id.nestFull);
            lookMore=itemView.findViewById(R.id.lookMore);
            iv_add=itemView.findViewById(R.id.iv_add);
            layout=itemView.findViewById(R.id.layout);



        }
    }

}