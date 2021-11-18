package com.youwu.shouyinxitong.ui.login;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.StoreInfo;
import com.youwu.shouyinxitong.app.UriUtils;
import com.youwu.shouyinxitong.bean_new.LoginRequsetBean;
import com.youwu.shouyinxitong.bean_used.NoDataBean;
import com.youwu.shouyinxitong.bean_used.StoreBean;
import com.youwu.shouyinxitong.bean_used.SystemBannerBean;
import com.youwu.shouyinxitong.data.DemoRepository;
import com.youwu.shouyinxitong.ui.main.MainActivity;
import com.youwu.shouyinxitong.utils_tool.RxToast;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;


/**
 * 登录
 * 2121/10/22
 */

public class LoginViewModel extends BaseViewModel<DemoRepository> {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //隐藏显示观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();

        //登录返回观察者
        public SingleLiveEvent<StoreBean> StoreEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.set(model.getUserName());
        password.set(model.getPassword());
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });
    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login(JPushInterface.getRegistrationID(getApplication()));
        }
    });

    //火锅按钮的点击事件
    public BindingCommand HuoGuoOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            RxToast.normal("火锅模式暂未开放");
        }
    });

    //中餐按钮的点击事件
    public BindingCommand ZhongCanOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(true);
        }
    });
    public BindingCommand FanHuiOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(false);
        }
    });

    /**
     * 登陆操作
     **/
    private void login(String os_uuid) {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("收银员账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("收银员密码不能为空！");
            return;
        }

        LoginRequsetBean requsetBean=new LoginRequsetBean();

        requsetBean.setUserName(userName.get());
        requsetBean.setPassWord(password.get());
        requsetBean.setOs_uuid(os_uuid);

        //RaJava模拟登录
        model.CALOGIN(UriUtils.getRequestPostUrl(requsetBean))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<StoreBean>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response.data);
                        Log.e("登录返回结果-----已解析",toPrettyFormat(submitJson));
                        dismissDialog();

                        if (200==response.getCode()){
                            //保存账号密码
                            model.saveUserName(userName.get());
                            model.savePassword(password.get());

                            AppApplication.spUtils.put("cashiername", userName.get());
                            AppApplication.spUtils.put("cashierpassword", password.get());
                            AppApplication.spUtils.put("logintime", getTime());
                            AppApplication.spUtils.put("bannerType","imageSbutton");

                            Log.e("登录返回结果-----已解析",toPrettyFormat(submitJson));
                            StoreBean storeBean = JSON.parseObject(toPrettyFormat(submitJson), StoreBean.class);
                            StoreInfo.setStore(storeBean);
                            AppApplication.spUtils.put("token", storeBean.getToken());
                            AppApplication.spUtils.put("store_id", storeBean.getStore_id());
                            uc.StoreEvent.setValue(storeBean);

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }

    //获取副屏banner
    public void getBanner() {
        //获取副屏banner
        model.GETBANNER(UriUtils.getRequestPostUrl(new NoDataBean()))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean response) {
                        String submitJson = new Gson().toJson(response);
                        Log.e("获取副屏banner返回结果-----已解析",toPrettyFormat(submitJson));
                        dismissDialog();

                        if (200==response.getCode()){
                            String submitJsonData = new Gson().toJson(response.data);
                            SystemBannerBean systemBannerBean = JSON.parseObject(toPrettyFormat(submitJsonData), SystemBannerBean.class);
                            AppApplication.systemBannerBean = systemBannerBean;
                            //跳转到首页
                        startActivity(MainActivity.class);
                            finish();
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            String submitJson = new Gson().toJson(throwable);
                            Log.e("返回结果-----已解析",toPrettyFormat(submitJson));
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });

    }
    private String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
