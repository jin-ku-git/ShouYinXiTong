package com.youwu.shouyinxitong.ui.commodity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyinxitong.BR;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppViewModelFactory;
import com.youwu.shouyinxitong.bean_new.CommodityDetailBean;
import com.youwu.shouyinxitong.databinding.ActivityEditCommodityDetailBinding;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import com.youwu.shouyinxitong.view.MyCustKeybords;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 编辑商品
 * 2021/10/29
 * 金库
 */

public class EditCommodityDetailActivity extends BaseActivity<ActivityEditCommodityDetailBinding, EditCommodityDetailViewModel> {

    private int type;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    int pageNo=1;//页数
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //获取列表传入的实体
        Intent intent = new Intent();
        if (intent != null) {
            type = intent.getIntExtra("type", 0);
        }
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_edit_commodity_detail;
    }

    @Override
    public EditCommodityDetailViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(EditCommodityDetailViewModel.class);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        //注册文件下载的监听
        viewModel.addVIP.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String submitJson) {
                RxToast.normal("提交的json实体数据：\r\n" + submitJson);
//                MaterialDialogUtils.showBasicDialog(getBaseContext(), "提交的json实体数据：\r\n" + submitJson).show();
            }
        });
        //点击item监听
        viewModel.commodityDetailBeanSingleLiveEvent.observe(this, new Observer<CommodityDetailBean>() {
            @Override
            public void onChanged(CommodityDetailBean commodityDetailBean) {
                Intent intent=new Intent();
                intent.setClass(getBaseContext(),AddCommodityDetailActivity.class);
                intent.putExtra("bean",commodityDetailBean);
                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        super.initData();

        viewModel.CommodityList(pageNo);

        binding.customKeyboard.bindEditText(binding.editSearch);
        //键盘
        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                String s = binding.editSearch.getText().toString();
                binding.customKeyboard.setVisibility(View.GONE);
                Log.e("输入文字", s);
            }
        });

        binding.editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.customKeyboard.setVisibility(View.VISIBLE);
            }
        });

        //下拉刷新
        binding.smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.observableList.clear();
                pageNo=1;
                //获取订单列表
                viewModel.CommodityList(pageNo);
                refreshLayout.finishRefresh(true);
            }
        });
        //上拉加载
        binding.smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                //获取订单列表
                viewModel.CommodityList(pageNo);

                refreshLayout.finishLoadMore(true);
            }
        });

        //输入框监听
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("editable---->", binding.editSearch.getText().toString());

//                Editable s = editSearch.getText();
//                if (s.toString().length()>=2){
//                    List<YWGoodBean> goodListByPinYin = getGoodListByPinYin(s.toString());
//                    searchGoodsAdapter.setNewData(goodListByPinYin);
//                }

            }
        });
        //滑动监听
        binding.searchGoodsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    /*正在拖拽*/
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        binding.customKeyboard.setVisibility(View.GONE);
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

            if (y1 - y2 > 100) {
                Log.e("向上滑", "1");
                binding.customKeyboard.setVisibility(View.GONE);
            } else if (y2 - y1 > 100) {
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
