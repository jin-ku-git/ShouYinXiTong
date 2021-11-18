package com.youwu.lz.intelface.widget.nestfulllistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.youwu.shouyinxitong.R;

import java.util.ArrayList;
import java.util.List;

public class NestFullGridView extends ViewGroup {
    /**
     * 列数
     */
    private int mSpan = 3;
    /**
     * Item 水平之间的间距
     */
    private int mHorizontalSpace = 0;
    /**
     * Item 垂直之间的间距
     */
    private int mVerticalSpace = 0;
    /**
     * 最大的Item数量
     */
    private int mMaxItem = 9;
    /**
     * 条目的宽高是否一样
     */
    private boolean isSquare;
    protected NestFullViewAdapter mAdapter;

    private LayoutInflater mInflater;
    private List<NestFullViewHolder> mVHCahces;//缓存ViewHolder,按照add的顺序缓存，
    private OnGirdItemClickListener mOnItemClickListener;// 子项点击事件

    
    public NestFullGridView(Context context) {
        this(context, null);
    }

    public NestFullGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestFullGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        mVHCahces = new ArrayList<NestFullViewHolder>();

        mInflater = LayoutInflater.from(context);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NestFullGridView);
        mSpan = array.getInteger(R.styleable.NestFullGridView_gridSpan, mSpan);
        mHorizontalSpace = (int) array.getDimension(R.styleable.NestFullGridView_gridHorizontalSpace, mHorizontalSpace);
        mVerticalSpace = (int) array.getDimension(R.styleable.NestFullGridView_gridHorizontalSpace, mVerticalSpace);
        mMaxItem = array.getInteger(R.styleable.NestFullGridView_gridMaxItem, mMaxItem);
        isSquare = array.getBoolean(R.styleable.NestFullGridView_gridIsSqaure, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 计算单个子View的宽度
        int itemWidth = (width - getPaddingLeft() - getPaddingRight() - mHorizontalSpace * (mSpan - 1)) / mSpan;
        // 测量子View的宽高
        int childCount = getChildCount();
        // 计算一下最大的条目数量
        childCount = Math.min(childCount, mMaxItem);
        if (childCount <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int itemSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            // 判断条目宽高是否一样
            if (isSquare) {
                measureChild(child, itemSpec, itemSpec);
            } else {
                measureChild(child, itemSpec, heightMeasureSpec);
            }
        }
        int height;
        // 判断条目宽高是否一样
        if (isSquare) {
            height = itemWidth * (childCount % mSpan == 0 ? (childCount / mSpan) : (childCount / mSpan + 1))
                    + mVerticalSpace * ((childCount - 1) / mSpan);
        } else {
            height = getChildAt(0).getMeasuredHeight() * (childCount % mSpan == 0 ? (childCount / mSpan) : (childCount / mSpan + 1))
                    + mVerticalSpace * ((childCount - 1) / mSpan);
        }
        height += getPaddingTop() + getPaddingBottom();
        // 指定自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        // 计算一下最大的条目数量
        childCount = Math.min(childCount, mMaxItem);
        if (childCount <= 0) {
            return;
        }
        int cl = getPaddingLeft();
        int ct = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(cl, ct, cl + width, ct + height);
            // 累加宽度
            cl += width + mHorizontalSpace;
            // 如果是换行
            if ((i + 1) % mSpan == 0) {
                // 重置左边的位置
                cl = getPaddingLeft();
                // 叠加高度
                ct += height + mVerticalSpace;
            }
        }
    }


    public void updateUI() {
        if (null != mAdapter) {
            if (null != mAdapter.getDatas() && !mAdapter.getDatas().isEmpty()) {
                /**
                 * 处理view 个数
                 * 使view数小于数据源
                 */
                //数据源有数据
                if (mAdapter.getDatas().size() > getChildCount()) {//数据源大于现有子View不清空

                } else if (mAdapter.getDatas().size() < getChildCount()) {//数据源小于现有子View，删除后面多的
                    removeViews(mAdapter.getDatas().size(), getChildCount() - mAdapter.getDatas().size());
                    //删除View也清缓存
                    while (mVHCahces.size() > mAdapter.getDatas().size()) {
                        mVHCahces.remove(mVHCahces.size() - 1);
                    }
                }
                for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                    NestFullViewHolder holder;
                    if (mVHCahces.size() - 1 >= i) {//说明有缓存，不用inflate，否则inflate
                        holder = mVHCahces.get(i);
                    } else {
                        holder = new NestFullViewHolder(getContext(), mInflater.inflate(mAdapter.getItemLayoutId(), this, false));
                        mVHCahces.add(holder);//inflate 出来后 add进来缓存
                    }
                    mAdapter.onBind(i, holder);
                    //如果View没有父控件 添加
                    if (null == holder.getConvertView().getParent()) {
                        this.addView(holder.getConvertView(), getChildCount());
                    }

                    /* 增加子项点击事件 */
                    final int mPosition = i;
                    holder.getConvertView().setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null && mAdapter != null) {
                                mOnItemClickListener.onItemClick(NestFullGridView.this, v, mPosition);
                            }
                        }
                    });
                    holder.getConvertView().setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (mOnItemClickListener != null && mAdapter != null) {
                                mOnItemClickListener.onLongItemClick(NestFullGridView.this, v, mPosition);
                            }
                            return true;
                        }
                    });
                }
            } else {
                removeViews(0, getChildCount() );//数据源没数据 清空视图
            }
        } else {
            removeViews(0, getChildCount());//适配器为空 清空视图
        }

    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnGirdItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 设置Adapter
     */
    public void setAdapter(NestFullViewAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("FlowBaseAdapter is null");
        }
        mAdapter = adapter;
        updateUI();
    }
    /**
     * 子项点击事件的接口
     */
    public interface OnGirdItemClickListener {

        void onItemClick(NestFullGridView parent, View view, int position);

        void onLongItemClick(NestFullGridView parent, View view, int position);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
