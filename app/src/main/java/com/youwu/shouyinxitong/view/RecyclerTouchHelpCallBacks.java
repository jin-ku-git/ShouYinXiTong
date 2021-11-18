package com.youwu.shouyinxitong.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyinxitong.bean_used.GoodsTypesBean;
import com.youwu.shouyinxitong.ui.main.MainGoodRecycleAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerTouchHelpCallBacks extends ItemTouchHelper.Callback {
    private OnHelperCallBack onCallBack;

    public  RecyclerTouchHelpCallBacks(OnHelperCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    //拖拽开关
    Boolean edit = true;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (!edit) {
            return 0;
        }

        //拖拽方向
        int dragFlags =
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        //侧滑删除
        int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //不同类型的item不能移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        //拖动的position
        int fromPosition = viewHolder.getAdapterPosition();
        //释放的position
        int targetPosition = target.getAdapterPosition();

        onCallBack.onMove(fromPosition, targetPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * 长按时调用
     */
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder!=null){
            //长按
            onCallBack.onSelectedChanged(viewHolder, actionState);
        }


    }

    /**
     * 松手时会最后调用
     */
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        onCallBack.clearView(recyclerView, viewHolder);

    }

    /**
     * 是否支持长按，默认true
     */
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 是否支持侧滑，默认true
     */
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    public void itemMove(MainGoodRecycleAdapter adapter, ArrayList<GoodsTypesBean.GoodsTypeBean> data, int fromPosition, int targetPosition) {

        if (adapter == null || data.isEmpty()) {
            return;
        }

        if (fromPosition < targetPosition) {
            for (int i = fromPosition; i < targetPosition; i++) {
                Collections.swap(data, i, i + 1);
            }

        } else {
            for (int i = targetPosition; i < fromPosition; i++) {
                Collections.swap(data, i, i + 1);
            }

        }
        adapter.notifyItemMoved(fromPosition, targetPosition);
    }

    public void setEdit(boolean b) {
        edit=b;
    }


    public interface OnHelperCallBack {
        void onMove(int fromPosition, int targetPosition);

        void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);

        void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }


//    interface OnHelperCallBack {
//        fun onMove(fromPosition: Int, targetPosition: Int)
//
//        fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder, actionState: Int)
//
//        fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
//
//    }

}
