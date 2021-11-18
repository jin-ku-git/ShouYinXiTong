package com.youwu.shouyinxitong.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.core.DrawerPopupView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.base.BaseAdapter;
import com.youwu.shouyinxitong.bean_used.LanguageBean;
import com.youwu.shouyinxitong.utils_tool.BeanCloneUtil;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import java.util.ArrayList;
import java.util.List;


public class LanguageDialog extends DrawerPopupView implements View.OnClickListener {

    RecyclerView recyclerView;


    TextView tvChooseVip;

    List<LanguageBean> list=new ArrayList<>();

    private LanguageBean languageBean;
    private CounListener listener;

    public void setListener(CounListener listener) {
        this.listener = listener;
    }

    public LanguageDialog(@NonNull Context context, List<LanguageBean> languageBean) {
        super(context);

        this.list = languageBean;


    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_language;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView=findViewById(R.id.recyclerView);
        tvChooseVip=findViewById(R.id.tv_choose_vip);
        tvChooseVip.setOnClickListener(this);

        languageBean=list.get(0);

        BaseAdapter<LanguageBean> adapter = new BaseAdapter<LanguageBean>(R.layout.item_language) {
            @Override
            protected void convert(BaseViewHolder helper, LanguageBean item) {
                helper.setText(R.id.tv_name, item.getName())
                        .setImageResource(R.id.iv_check, item.isSelect() ? R.mipmap.check_select : R.mipmap.check_no)

                ;
            }
        };
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter ada, View view, int position) {
                LanguageBean item = adapter.getItem(position);
                if (item.isSelect()) {
                    RxToast.normal("至少选择一个");
                } else {
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        adapter.getItem(i).setSelect(false);
                    }
                    item.setSelect(true);
                    item.setPosition(position);
                    languageBean = BeanCloneUtil.cloneTo(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setNewData(list);

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_vip://选择语言
                listener.onChoiceLanguage(languageBean);
                break;

        }
        dismiss();
    }


    public interface CounListener {
        void onChoiceLanguage(LanguageBean languageBean);

    }

}
