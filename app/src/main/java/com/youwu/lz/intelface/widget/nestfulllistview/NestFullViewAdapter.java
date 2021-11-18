package com.youwu.lz.intelface.widget.nestfulllistview;

import java.util.List;


public abstract class NestFullViewAdapter<T> {
    private int mItemLayoutId;//看名字
    private List<T> mDatas;//数据源

    public NestFullViewAdapter(int mItemLayoutId, List<T> mDatas) {
        this.mItemLayoutId = mItemLayoutId;
        this.mDatas = mDatas;
    }

    /**
     * 被FullListView调用
     *
     * @param i
     * @param holder
     */
    public void onBind(int i, NestFullViewHolder holder) {
        //回调bind方法，多传一个data过去
        onBind(i, mDatas.get(i), holder);
    }

    /**
     * 数据绑定方法
     *
     * @param pos    位置
     * @param t      数据
     * @param holder ItemView的ViewHolder
     */
    public abstract void onBind(int pos, T t, NestFullViewHolder holder);

    public int getItemLayoutId() {
        return mItemLayoutId;
    }

    public void setItemLayoutId(int mItemLayoutId) {
        this.mItemLayoutId = mItemLayoutId;
    }

    public List<T> getDatas() {

        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }


}
