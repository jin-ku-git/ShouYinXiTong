package com.youwu.shouyinxitong.ui.print;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.databinding.ActivityDemoBinding;
import com.youwu.shouyinxitong.databinding.ActivityPrintSetupBinding;
import com.youwu.shouyinxitong.ui.main.DemoViewModel;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

import static com.youwu.shouyinxitong.ui.main.MainActivity.ISCONNECT;

/**
 * 打印设置
 * 2021/11/03
 * 金库
 */

public class PrintSetupActivity extends BaseActivity<ActivityPrintSetupBinding, PrintSetupViewModel> {


    private int printType;

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_print_setup;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //注册监听相机权限的请求
        viewModel.requestCameraPermissions.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        
        initSpinner();
    }

    private void initSpinner(){
        String[] arrayStrings = new String[]{"商米内置打印机标准样式", "芯烨80mmUSB打印机", "商米内置打印机精简样式"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayStrings);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setSelection(AppApplication.spUtils.getInt("printType", 0), true);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        printType = 0;
                        AppApplication.spUtils.put("printType", 0);
                        break;
                    case 1:
                        if (ISCONNECT) {
                            printType = 1;
                            AppApplication.spUtils.put("printType", 1);
                        } else {
                            Log.d("打印机2", i + "");
//                            Message message = new Message();
//                            message.what = 2;
//                            myHandler.handleMessage(message);
                            binding.spinner.setSelection(0, true);
                            AppApplication.spUtils.put("printType", 0);
                            AppApplication.mSpeechSynthesizer.speak("芯烨打印机连接失败，请检查连接线或重启APP");
                        }
                        break;

                    case 2:
                        printType = 2;
                        AppApplication.spUtils.put("printType", 2);
                        break;

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
