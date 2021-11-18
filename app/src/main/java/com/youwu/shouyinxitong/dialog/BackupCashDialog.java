package com.youwu.shouyinxitong.dialog;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.bean_used.BackupCashRecordBean;
import com.youwu.shouyinxitong.bean_new.BackupCashTypeBean;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;


import java.util.ArrayList;
import java.util.List;


public class BackupCashDialog extends CenterPopupView implements View.OnClickListener {

    TextView tvCancel;

    TextView tvConfirm;

    TextView tvTitle;

    CheckBox cbPrint;

    TabLayout tabLayout;

    ViewPager viewPager;

    private BackupCashPagerAdapter adapter;
    private List<View> views = new ArrayList<>();

    private String confirm = "确定";
    private String cancle = "取消";
    private String title = "提示";
    private boolean showCheckBox=false;
    private OnClickListenr listenr;

    private List<BackupCashTypeBean> backupCashTypeBeans;

    public BackupCashDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        tvCancel=findViewById(R.id.tv_cancel);
        tvConfirm=findViewById(R.id.tv_confirm);
        tvTitle=findViewById(R.id.tv_title);
        cbPrint=findViewById(R.id.cb_print);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager_content);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);


        if (TextUtils.isEmpty(cancle)) {
            tvCancel.setVisibility(GONE);
        }
        popupInfo.isDismissOnTouchOutside = false;

        tabLayout.addTab(tabLayout.newTab().setText("收入"));
        tabLayout.addTab(tabLayout.newTab().setText("支出"));
        tvTitle.setText(title);
        tvCancel.setText(cancle);
        tvConfirm.setText(confirm);
        cbPrint.setVisibility(showCheckBox?VISIBLE:GONE);

        List<String> titleList = new ArrayList<>();

        titleList.add("支出");
        titleList.add("收入");


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View tab01 = inflater.inflate(R.layout.tab_backup_cash, null);
        View tab02 = inflater.inflate(R.layout.tab_backup_cash, null);

        views.add(tab01);
        views.add(tab02);

        adapter =new BackupCashPagerAdapter(views,titleList);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_backup_cash;
    }

    public void setBackupCashTypeBean(List<BackupCashTypeBean> backupCashTypeBeans) {
        this.backupCashTypeBeans = backupCashTypeBeans;
        adapter.setTypeSpinner(this.backupCashTypeBeans);
    }

    public BackupCashDialog setConfirm(String text) {
        this.confirm = text;
        return this;
    }

    public BackupCashDialog setTitle(String text) {
        this.title = text;
        return this;
    }

    public BackupCashDialog setCancle(String cancle) {
        this.cancle = cancle;
        return this;

    }

    public BackupCashDialog setListenr(OnClickListenr listenr) {
        this.listenr = listenr;
        return this;
    }

    public void commitInfo(){
        BackupCashRecordBean recordBean = adapter.getRecord(viewPager.getCurrentItem());
        if (recordBean == null) {
            ToastUtil.showTipToast(getContext(), "为获取到信息");
        }
        listenr.onCommit(recordBean);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

           case  R.id.tv_cancel:
            if (listenr != null) {
                listenr.onCancle(cbPrint.isChecked());
            }
            dismiss();
            break;
            case R.id.tv_confirm:
                // 这里需要先处理提交功能
                if (adapter.isFilled(viewPager.getCurrentItem())){
                    commitInfo();
                } else {
                    ToastUtil.showShortToast("请输入金额和项目！");
                }
                if (listenr != null) {
                    //listenr.onConfirm(cbPrint.isChecked());
                }
                break;
        }
    }

    public interface OnClickListenr {
        void onInit();
        void onCommit(BackupCashRecordBean recordBean);
        void onConfirm(boolean isPrintCheck);
        void onCancle(boolean isPrintCheck);
    }

    class BackupCashPagerAdapter extends PagerAdapter {

        private List<View> viewList;
        private List<String> titleList;

        public BackupCashPagerAdapter(List<View> viewList, List<String> titleList) {
            this.viewList=viewList;
            this.titleList=titleList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
           return viewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public void setTypeSpinner(List<BackupCashTypeBean> typeBeans) {

            if (typeBeans == null) {
                return;
            }

            for (View view : viewList) {
                Spinner spinner = view.findViewById(R.id.spinner_type);
                // 这里对spinner进行填充
                List<String> items = new ArrayList<>();
                for (BackupCashTypeBean type : typeBeans) {
                    items.add(type.getName());
                }
                //ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.adapter_spinner_backup_cash, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }

        public boolean isFilled(int position) {
            if (backupCashTypeBeans == null) {
                return false;
            }

            if (backupCashTypeBeans.size() == 0) {
                return false;
            }
            View view = viewList.get(position);
            EditText editPrice = (EditText)view.findViewById(R.id.edit_price);
            Spinner spinnerType = (Spinner)view.findViewById(R.id.spinner_type);
            BackupCashTypeBean type = backupCashTypeBeans.get(spinnerType.getSelectedItemPosition());

            if (editPrice.getText().toString().isEmpty() || type == null) {
                return false;
            }

            return true;
        }

        public BackupCashRecordBean getRecord(int position) {
            View view = viewList.get(position);
            EditText editPrice = (EditText)view.findViewById(R.id.edit_price);
            EditText editRemark = (EditText)view.findViewById(R.id.edit_remark);
            Spinner spinnerType = (Spinner)view.findViewById(R.id.spinner_type);
            BackupCashTypeBean type = backupCashTypeBeans.get(spinnerType.getSelectedItemPosition());

            BackupCashRecordBean recordBean = new BackupCashRecordBean();
            recordBean.setPrice(Double.parseDouble(editPrice.getText().toString()));
            recordBean.setRemark(editRemark.getText().toString());
            recordBean.setType(position+1); // 这里1表示支出，2表示收入
            recordBean.setFund_id(type.getId());

            return recordBean;
        }
    }


}
