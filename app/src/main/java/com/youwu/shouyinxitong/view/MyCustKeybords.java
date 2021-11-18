package com.youwu.shouyinxitong.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.util.KeyboardUtils;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.base.BaseAdapter;
import com.youwu.shouyinxitong.utils_tool.ClickUtils;
import com.youwu.shouyinxitong.widget.GridDividerItemDecoration;
import com.youwu.shouyinxitong.widget.XGridLayoutManager;


import java.util.ArrayList;
import java.util.List;


public class MyCustKeybords extends FrameLayout implements View.OnClickListener {

    RecyclerView recyView;

    View layoutDel;

    FrameLayout layoutSystem;

    FrameLayout btnConfirm;
    private BaseAdapter<String> adapter;
    private EditText editText;
    private OnKeyBoradConfirm listener;
    private boolean needSystem;

    public void setListener(OnKeyBoradConfirm listener) {
        this.listener = listener;
    }

    public MyCustKeybords(Context context) {
        this(context, null);
    }

    public MyCustKeybords(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustKeybords(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustKeybords);
        needSystem = typedArray.getBoolean(R.styleable.MyCustKeybords_needSystem, true);
        typedArray.recycle();
        init();
    }

    public void init() {
        View keyBords = LayoutInflater.from(getContext()).inflate(R.layout.view_keybords, this);

        recyView=findViewById(R.id.recy_view);
        layoutDel=findViewById(R.id.layout_del);
        layoutSystem=findViewById(R.id.layout_system);
        btnConfirm=findViewById(R.id.btn_confirm);

        layoutSystem.setOnClickListener(this);
        layoutDel.setOnClickListener(this);

        layoutSystem.setVisibility(needSystem ? VISIBLE : GONE);
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isFastClick()) {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                }
            }
        });

    }

    public void bindEditText(EditText editText) {
        this.editText = editText;
        if (editText != null) {
            editText.setOnEditorActionListener(editorActionListener);
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (listener != null) {
                    listener.onConfirm();
                }
            }
            return false;
        }
    };


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int itemHeight = (getMeasuredHeight() - 2) / 4;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        adapter = new BaseAdapter<String>(R.layout.item_keybords) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                View itemView = helper.itemView;
                itemView.setLayoutParams(params);
                helper.setText(R.id.tv_name, item);
            }

        };
        recyView.setLayoutManager(new XGridLayoutManager(getContext(), 3));
        recyView.addItemDecoration(new GridDividerItemDecoration(getContext(), 3, 1, Color.parseColor("#D8D8D8")));
        adapter.bindToRecyclerView(recyView);
        initData(1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter ad, View view, int position) {
                if (editText != null) {
                    inset(adapter.getItem(position));
                }
            }
        });
    }


    public void inset(String text) {
        int index = editText.getSelectionStart();//获取光标所在位置
        int selectionEnd = editText.getSelectionEnd();
        Editable edit = editText.getEditableText();//获取EditText的文字
        if (selectionEnd > index) {
            edit.replace(index, selectionEnd, "");
            inset(text);
            return;
        }

        if (index < 0 || index >= edit.length()) {
            edit.append(text);
        } else {
            edit.insert(index, text);

        }
    }

    /**
     * 删除光标之前的字符串
     */
    public void delete() {
        int index = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        Editable editable = editText.getText();
        if (selectionEnd > index) {
            editable.delete(index, selectionEnd);
        } else {
            if (index <= 0) {
                return;
            }
            editable.delete(index - 1, index);
        }
    }

    private void initData(int type) {
        List<String> list = new ArrayList<>();
        if (type == 1) {
            for (int i = 1; i < 10; i++) {
                //添加1-9
                list.add(String.valueOf(i));
            }
            list.add("00");
            list.add("0");
            list.add(".");
        }
        adapter.setNewData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_system:
                if (editText != null) {
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    KeyboardUtils.showSoftInput(editText);
                }
                break;
            case R.id.layout_del:
                if (editText != null)
                    delete();
                break;
        }
    }


    public interface OnKeyBoradConfirm {
        void onConfirm();

    }
}
