package com.youwu.shouyinxitong.ui.search;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.databinding.ActivityDemoBinding;
import com.youwu.shouyinxitong.databinding.ActivitySearchVipBinding;
import com.youwu.shouyinxitong.ui.commodity.EditCommodityDetailViewModel;
import com.youwu.shouyinxitong.ui.main.DemoViewModel;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;
import com.youwu.shouyinxitong.view.MyCustKeybords;
import com.youwu.shouyinxitong.widget.loaddialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * 会员搜索
 * 2021/10/28
 * 金库
 */

public class SearchVipActivity extends BaseActivity<ActivitySearchVipBinding, SearchVipViewModel> {


    private String phone;//所搜索的内容

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_search_vip;
    }

    @Override
    public SearchVipViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SearchVipViewModel.class);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.EditTextEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        binding.tvTitle.setText("返回");
        binding.edit.setInputType(InputType.TYPE_NULL);
        binding.customKeyboard.bindEditText(binding.edit);
        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if (TextUtils.isEmpty(binding.edit.getText().toString())) {
                    ToastUtil.showShortToast("请先输入然后查询");
                    return;
                }
//                LoadingDialog.show(SearchVipActivity.this, "搜索中");
                binding.frameLayout.setVisibility(View.GONE);
                phone = binding.edit.getText().toString();
            }
        });

        viewModel.getUserList("1");


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.frameLayout.setVisibility(View.VISIBLE);
            }
        });


        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    /*正在拖拽*/
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        binding.frameLayout.setVisibility(View.GONE);
                        break;
                    /*滑动停止*/
                    case RecyclerView.SCROLL_STATE_IDLE:
                        break;
                    /*惯性滑动中*/
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //在网上还看到一种解决滑动冲突的方法
                int topPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                Log.e("touch", "onScroll:" + topPosition);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();

            if (y1 - y2 > 50) {
                Log.e("向上滑", "1");
                binding.frameLayout.setVisibility(View.GONE);
            } else if (y2 - y1 > 50) {
                Log.e("向下滑", "2");
            } else if (x1 - x2 > 50) {
                Log.e("向左滑", "3");
            } else if (x2 - x1 > 50) {
                Log.e("向右滑", "4");
            }
        }
        return super.onTouchEvent(event);
    }

}
