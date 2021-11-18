package com.youwu.shouyinxitong.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;

import java.util.List;

/**
 * 首页商品列表适配器
 */
public class GoodsDeatalsListAdapter extends RecyclerView.Adapter<GoodsDeatalsListAdapter.myViewHodler> {
    private Context context;
    public List<MealGoodsBean> goods_list;

    private OnItemClickListener mOnItemClickListener;





    //创建构造函数
    public GoodsDeatalsListAdapter(Context context, List<MealGoodsBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.goods_list = goodsEntityList;//实体类数据ArrayList
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
        View itemView = View.inflate(context, R.layout.item_goods_details, null);

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
        MealGoodsBean data = goods_list.get(position);

        holder.name.setText(data.getGoods_name());
        holder.buy_num.setText(data.getGoods_number()+"x");

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), data,position);
//            }
//        });

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos, Object myLiveList, int position);
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

            private TextView name,buy_num;//名称，数量

        public myViewHodler(View itemView) {
            super(itemView);


            name=itemView.findViewById(R.id.name);
            buy_num=itemView.findViewById(R.id.buy_num);




        }
    }

}