package com.youwu.shouyinxitong.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.youwu.shouyinxitong.ui.main.MainGoodRecycleAdapter
import java.util.*

class RecyclerTouchHelpCallBack(var onCallBack: OnHelperCallBack) : ItemTouchHelper.Callback() {
    //拖拽开关
    var edit = true


    /**
     * 设置拖拽、滑动方向
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (!edit) {
            return 0
        }

        //拖拽方向
        val dragFlags =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        //侧滑删除
        val swipeFlags =  ItemTouchHelper.ACTION_STATE_IDLE

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 拖拽移动
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        //不同类型的item不能移动
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }

        //拖动的position
        var fromPosition = viewHolder.adapterPosition
        //释放的position
        var targetPosition = target.adapterPosition

        onCallBack.onMove(fromPosition, targetPosition)
        return true
    }

    /**
     * 侧滑
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        onCallBack.remove(viewHolder,direction,viewHolder.layoutPosition)
    }

    /**
     * 长按时调用
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.let {
            //长按
            onCallBack.onSelectedChanged(viewHolder, actionState)
        }
    }

    /**
     * 松手时会最后调用
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        onCallBack.clearView(recyclerView, viewHolder)
    }

    /**
     * 是否支持长按，默认true
     */
    override fun isLongPressDragEnabled(): Boolean {
        return super.isLongPressDragEnabled()
    }

    /**
     * 是否支持侧滑，默认true
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return super.isItemViewSwipeEnabled()
    }


    /**
     * 移动item
     *
     * @param fromPosition   长按的item,position
     * @param targetPosition 要到达的position
     */
    fun itemMove(adapter: MainGoodRecycleAdapter, data: MutableList<*>, fromPosition: Int, targetPosition: Int) {
        if (adapter == null || data.isEmpty()) {
            return
        }

        if (fromPosition < targetPosition) {
            for (i in fromPosition until targetPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in targetPosition until fromPosition) {
                Collections.swap(data, i, i + 1)
            }
        }
        adapter.notifyItemMoved(fromPosition, targetPosition)
    }

    interface OnHelperCallBack {
        fun onMove(fromPosition: Int, targetPosition: Int)

        fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder, actionState: Int)

        fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)

    }
}