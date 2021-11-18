package com.youwu.shouyinxitong.ui.setup;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.orhanobut.hawk.Hawk;
import com.suke.widget.SwitchButton;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.bean_used.LanguageBean;
import com.youwu.shouyinxitong.databinding.ActivitySystemSettingBinding;
import com.youwu.shouyinxitong.dialog.LanguageDialog;
import com.youwu.shouyinxitong.event.SystemMessageEvent;
import com.youwu.shouyinxitong.ui.main.MainActivity;
import com.youwu.shouyinxitong.utils.LocaleUtils;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 设置
 * 2021/10/26
 * 金库
 */

public class SystemSettingActivity extends BaseActivity<ActivitySystemSettingBinding, SystemSettingViewModel> {

    public static Context context;
    private PackageInfo info;
    TextView switchingTimeTv;
    LinearLayout videoSettingLayout;
    LinearLayout switchingTimeLiner;

    SwitchButton imageSbutton,videoSbutton;

    ListView listView;
    SwitchingTimeListAdapter listAdapter;
    List<SwitchingTime> list;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_system_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.TypeEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewModel.type.set(integer);
            }
        });
        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer){
                    case 1:
                        showCouponList();
                        break;
                }
            }
        });
    }

    public void showCouponList() {
        List<LanguageBean> list= new ArrayList<>();

        LanguageBean languageBean= new LanguageBean();
        languageBean.setName("中文");
        languageBean.setId("1");
        languageBean.setSelect(true);
        languageBean.setPosition(0);

        LanguageBean languageBean1= new LanguageBean();
        languageBean1.setName("英语");
        languageBean1.setId("2");
        languageBean1.setSelect(false);
        languageBean1.setPosition(1);

        list.add(languageBean);
        list.add(languageBean1);

        LanguageDialog languageDialog = new LanguageDialog(context, list);
        languageDialog.setListener(new LanguageDialog.CounListener() {

            @Override
            public void onChoiceLanguage(LanguageBean languageBean) {
                RxToast.normal(languageBean.getName());
                switch (languageBean.getPosition()){

                    case 0:
                        if (LocaleUtils.needUpdateLocale(context, LocaleUtils.LOCALE_CHINESE)) {
                            LocaleUtils.updateLocale(context, LocaleUtils.LOCALE_CHINESE);
                            restartAct();
                        }

                        break;
                    case 1:
                        if (LocaleUtils.needUpdateLocale(context, LocaleUtils.LOCALE_ENGLISH)) {
                            LocaleUtils.updateLocale(context, LocaleUtils.LOCALE_ENGLISH);
                            restartAct();
                        }

                        break;
                }
            }
        });

        new XPopup.Builder(context)
                .popupPosition(PopupPosition.Right)
                .asCustom(languageDialog)
                .show();
    }
    /**
     * 重启当前Activity
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
    }


    @Override
    public void initData() {
        super.initData();
        context=this;
        PackageManager manager = getPackageManager();
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        viewModel.type.set(1);
        binding.accountVersionName.setText("版本：" + info.versionName+"");
        binding.accountName.setText("当前账户：" + Hawk.get("username", ""));
        switchingTimeTv=findViewById(R.id.switching_time_tv);
        switchingTimeTv.setText(AppApplication.spUtils.getString("switchingTime")+"秒");

        videoSettingLayout=findViewById(R.id.video_setting_layout);
        switchingTimeLiner=findViewById(R.id.switching_time_liner);
        imageSbutton=findViewById(R.id.image_sbutton);
        videoSbutton=findViewById(R.id.video_sbutton);

        //选择时间
        switchingTimeLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.levelBack.setVisibility(View.VISIBLE);
                binding.tittleTv.setText("图片切换时间");
                binding.switchingTimeLayout.setVisibility(View.VISIBLE);
                videoSettingLayout.setVisibility(View.GONE);
            }
        });
        imageSbutton.setChecked(true);
        videoSbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    imageSbutton.setChecked(false);
                    AppApplication.spUtils.put("bannerType","videoSbutton");
                }
            }
        });

        imageSbutton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    videoSbutton.setChecked(false);
                    AppApplication.spUtils.put("bannerType","imageSbutton");
                }
            }
        });
        binding.levelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.switchingTimeLayout.setVisibility(View.GONE);
                videoSettingLayout.setVisibility(View.VISIBLE);
                binding.levelBack.setVisibility(View.GONE);
                binding.tittleTv.setText("通用设置");
            }
        });


        listView = (ListView) findViewById(R.id.switching_time_listview);


        SwitchingTime[] switchingTimes = new SwitchingTime[] { new SwitchingTime("3"), new SwitchingTime("5"),
                new SwitchingTime("10"),new SwitchingTime("15"),new SwitchingTime("30"),
                new SwitchingTime("60"),new SwitchingTime("90") };
        list = new ArrayList<SwitchingTime>();
        for(SwitchingTime swt:switchingTimes){
            list.add(swt);
        }
        listAdapter = new SwitchingTimeListAdapter(SystemSettingActivity.this);
        listAdapter.setList(list);
        listView.setAdapter(listAdapter);

        //图片切换时间
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listAdapter.select(position);
                SwitchingTime switchingTime = (SwitchingTime) listAdapter.getItem(position);
                String value = switchingTime.getTimeName() ;

                AppApplication.spUtils.put("switchingTime",value);  //设置banner切换时间

                binding.levelBack.setVisibility(View.GONE);
                binding.switchingTimeLayout.setVisibility(View.GONE);
                videoSettingLayout.setVisibility(View.VISIBLE);
                switchingTimeTv.setText(value+"秒");
                binding.tittleTv.setText("通用设置");
            }
        });

        //账号注销
        binding.accountClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sign = 1;
                EventBus.getDefault().post(new SystemMessageEvent(sign,""));
                finish();
            }
        });
        //账号同步
        binding.accountSynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sign = 2;
                EventBus.getDefault().post(new SystemMessageEvent(sign,""));
                finish();
            }
        });

        //数据同步
        binding.dataSynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sign = 2;
                EventBus.getDefault().post(new SystemMessageEvent(sign,""));
                finish();
            }
        });
    }
}
