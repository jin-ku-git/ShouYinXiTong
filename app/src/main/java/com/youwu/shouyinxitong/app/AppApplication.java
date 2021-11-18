package com.youwu.shouyinxitong.app;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.baidu.aip.FaceEnvironment;
import com.baidu.aip.FaceSDKManager;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.request.RequestOptions;
import com.clj.fastble.BleManager;
import com.cretin.www.cretinautoupdatelibrary.model.TypeConfig;
import com.cretin.www.cretinautoupdatelibrary.model.UpdateConfig;
import com.cretin.www.cretinautoupdatelibrary.utils.AppUpdateUtils;
import com.cretin.www.cretinautoupdatelibrary.utils.SSLUtils;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginEventCallbacks;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.youwu.lz.intelface.APIService;
import com.youwu.lz.intelface.exception.FaceError;
import com.youwu.lz.intelface.model.AccessToken;
import com.youwu.lz.intelface.utils.OnResultListener;
import com.youwu.shouyinxitong.BuildConfig;
import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.base.Common;
import com.youwu.shouyinxitong.bean_used.SystemBannerBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.ui.login.LoginActivity;
import com.youwu.shouyinxitong.update_model.OkHttp3Connection;
import com.youwu.shouyinxitong.update_model.UpdateModel;
import com.youwu.shouyinxitong.utils.LocaleUtils;
import com.youwu.shouyinxitong.utils_tool.AidlUtil;
import com.youwu.shouyinxitong.utils_tool.RxToast;

import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.OkHttpClient;

import com.youwu.lz.intelface.Config;
import com.youwu.shouyinxitong.utils_tool.SunConnection;
import com.youwu.shouyinxitong.utils_tool.ToastUtil;

/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {

    private static Stack<Activity> activityStack;
    private static Context context;
    private static AppApplication instance;
    public static String screenOrientation = "";
    public static String orderType = "A";
    public static AppApplication application;
    public static RequestOptions options = new RequestOptions()
            .centerCrop().error(R.mipmap.load_fail)
            .placeholder(R.mipmap.loading);
    public static SPUtils spUtils;
    public static Gson gson;
    private Common common;
    public Common common2;
    public static TextToSpeech textToSpeech = null;//创建自带语音对象
    public static SpeechSynthesizer mSpeechSynthesizer;
    public static String speak = "0";
    public static String speed = "5";
    public static String volume = "5";
    public static int TO_OPTER_TEST = 0;
    public static int TO_REMOVE_WEIGHT = 1;
    public static int TO_TEST_PRINT = 2;

    public static int rechargeTypeSign = 1;

    public static boolean JPushSign = true;

    public static SystemBannerBean systemBannerBean  = new SystemBannerBean();

    private static Toast sToast; // 单例Toast,避免重复创建，显示时间过长
    private static final Handler sHandler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);
//        KLog.init(true);
        //初始化全局异常崩溃
        initCrash();

        //内存泄漏检测
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        LocaleUtils.updateLocale(this, _UserLocale);



        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashReport.initCrashReport(getApplicationContext(), "3c2182a699", false);
        application = this;
        Hawk.init(getApplicationContext()).build();
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);
        mSpeechSynthesizer.setAppId("19644499");
        mSpeechSynthesizer.setApiKey("y8qr26SSQIKnasHBhC6GVmR7", "TK3axgIzvVhmPD2NnAehOKKVxF8p87Io");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, speak);
        mSpeechSynthesizer.initTts(TtsMode.ONLINE);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speed);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, volume);


        RxToast.setContext(this);

        BleManager.getInstance().init(this);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        sToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        gson = new Gson();
        context = getApplicationContext();
        spUtils = new SPUtils("com.biyesheji.qiangchewei");
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//        SDKInitializer.initialize(this);

        CrashReport.initCrashReport(getApplicationContext(), "5f73632816", false);

        //sunmi  打印服务
        AidlUtil.getInstance().connectPrinterService(this);

        initLib();

        APIService.getInstance().init(this);
        APIService.getInstance().setGroupId(Config.groupID);
        // 用ak，sk获取token, 调用在线api，如：注册、识别等。为了ak、sk安全，建议放您的服务器，
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.i("wtf", "AccessToken->" + result.getAccessToken());
            }

            @Override
            public void onError(FaceError error) {
                Log.e("xx", "AccessTokenError:" + error);
                error.printStackTrace();

            }
        }, this, Config.apiKey, Config.secretKey);

        ToastUtil.init(true);
        MultiDex.install(this);

        initTTS(getApplicationContext());


        /**
         * 更新库
         */
        //如果你想使用okhttp作为下载的载体，那么你需要自己依赖okhttp，更新库不强制依赖okhttp！可以使用如下代码创建一个OkHttpClient 并在UpdateConfig中配置setCustomDownloadConnectionCreator start
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30_000, TimeUnit.SECONDS)
                .readTimeout(30_000, TimeUnit.SECONDS)
                .writeTimeout(30_000, TimeUnit.SECONDS)
                //如果你需要信任所有的证书，可解决根证书不被信任导致无法下载的问题 start
                .sslSocketFactory(SSLUtils.createSSLSocketFactory())
                .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                //如果你需要信任所有的证书，可解决根证书不被信任导致无法下载的问题 end
                .retryOnConnectionFailure(true);
        //如果你想使用okhttp作为下载的载体，那么你需要自己依赖okhttp，更新库不强制依赖okhttp！可以使用如下代码创建一个OkHttpClient 并在UpdateConfig中配置setCustomDownloadConnectionCreator end

        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)//是否是Debug模式
                .setBaseUrl("http://www.cretinzp.com/system/versioninfo")//当dataSourceType为DATA_SOURCE_TYPE_URL时，配置此接口用于获取更新信息
                .setMethodType(TypeConfig.METHOD_GET)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的方法
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_URL)//设置获取更新信息的方式
                .setShowNotification(true)//配置更新的过程中是否在通知栏显示进度
                .setNotificationIconRes(R.mipmap.download_icon)//配置通知栏显示的图标
                .setUiThemeType(TypeConfig.UI_THEME_AUTO)//配置UI的样式，一种有12种样式可供选择
                .setRequestHeaders(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求头
                .setRequestParams(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求参数
                .setAutoDownloadBackground(false)//是否需要后台静默下载，如果设置为true，则调用checkUpdate方法之后会直接下载安装，不会弹出更新页面。当你选择UI样式为TypeConfig.UI_THEME_CUSTOM，静默安装失效，您需要在自定义的Activity中自主实现静默下载，使用这种方式的时候建议setShowNotification(false)，这样基本上用户就会对下载无感知了
//                .setCustomActivityClass(CustomActivity.class)//如果你选择的UI样式为TypeConfig.UI_THEME_CUSTOM，那么你需要自定义一个Activity继承自RootActivity，并参照demo实现功能，在此处填写自定义Activity的class
                .setNeedFileMD5Check(false)//是否需要进行文件的MD5检验，如果开启需要提供文件本身正确的MD5校验码，DEMO中提供了获取文件MD5检验码的工具页面，也提供了加密工具类Md5Utils
                .setCustomDownloadConnectionCreator(new OkHttp3Connection.Creator(builder))//如果你想使用okhttp作为下载的载体，可以使用如下代码创建一个OkHttpClient，并使用demo中提供的OkHttp3Connection构建一个ConnectionCreator传入，在这里可以配置信任所有的证书，可解决根证书不被信任导致无法下载apk的问题
                .setModelClass(new UpdateModel());
        //初始化
        AppUpdateUtils.init(this, updateConfig);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().init(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale);
            } else {
                _Configuration.locale =_UserLocale;
            }
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }
    }


    /**
     * 获取全局Context
     */
    public static Context getContext() {
        return context;
    }

    public static void initTTS(Context context) {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US支持否？--》" + a +
                            "\nzh-CN支持否》--》" + b);

                }

            }
        });
    }

    private void setFaceConfig() {
        FaceTracker tracker = FaceSDKManager.getInstance().getFaceTracker(this);
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整

        // 模糊度范围 (0-1) 推荐小于0.7
        tracker.set_blur_thr(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        tracker.set_illum_thr(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        tracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        tracker.set_eulur_angle_thr(FaceEnvironment.VALUE_HEAD_PITCH, FaceEnvironment.VALUE_HEAD_ROLL,
                FaceEnvironment.VALUE_HEAD_YAW);

        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        tracker.set_min_face_size(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        //
        tracker.set_notFace_thr(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        tracker.set_occlu_thr(FaceEnvironment.VALUE_OCCLUSION);
        // 是否进行质量检测
        tracker.set_isCheckQuality(true);
        // 是否进行活体校验
        tracker.set_isVerifyLive(false);
        // 根据设备的cpu核心数设定人脸sdk使用的线程数，如双核设置为2，四核设置为4
        FaceSDK.setNumberOfThreads(2);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        stopLogcatManager();
    }

    //取消交易指令
    public void cancelTransaction() {
        if (null == common) {
            common = new Common();
        }
        common.cancelTransaction();
    }

    public synchronized static AppApplication getInstance() {
        if (null == instance) {
            instance = new AppApplication();
        }
        return instance;
    }
    //todo 滨州奔腾商贸消磁桶串口指令
    public void startWeightSeri2() {
        if (null == common2) {
            common2 = new Common();
        }
//        if (getInner_Ver().startsWith("SWH")) {
//
//            common.initSerial(Common.WEIGHT_SERIAL_PATH, Common.WEIGHT_SERIAL_WAVE);
//        } else {
        common2.initSerial("dev/ttyUSB0", 115200);

//        }
    }

    public static void toast(String txt, int duration) {
        sToast.setText(txt);
        sToast.setDuration(duration);
        sToast.show();
    }


    public static void runUi(Runnable runnable) {
        sHandler.post(runnable);
    }


    //打开钱箱
    public void openDraw() {
        Intent intent = new Intent();
        // 注意在 Android 5.0以后，不能通过隐式 Intent 启动 service，必须制定包名
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        context.bindService(intent, new SunConnection(), Context.BIND_AUTO_CREATE);
//        startService(intent);
    }

    public void removeWeight() {
        if (null == common) {
            common = new Common();
        }
        common.removeWeight();
    }

    public void openWeightSeri() {
        if (null == common) {
            common = new Common();
        }
        common.startWeight();
    }

    public void stopWeightSeri() {
        if (null == common) {
            common = new Common();
        }
        common.stopWeidht();
    }

    public void getWeightSeri() {
        if (null == common) {
            common = new Common();
        }
        common.checkWeight();
    }

    public void getWeightSeri(String type) {
        if (null == common) {
            common = new Common();
        }
        common.checkWeight(type);
    }


    public void releasGoodSeri() {
        if (null == common) {
            common = new Common();
        }
        common.releasegood();
    }

    public void closeWeightSeri() {
        if (null == common) {
            common = new Common();
        }
        common.closeSerialPort();
    }

    public void weightMore() {
        if (null == common) {
            common = new Common();
        }
        common.weightMore();
    }

    public void weightLittle() {
        if (null == common) {
            common = new Common();
        }
        common.weightLittle();
    }

    public void payFailure() {
        if (null == common) {
            common = new Common();
        }
        common.payFailure();
    }

    public void initMQTT() {
        if (null == common) {
            common = new Common();
        }
        common.initMqttClient(application);
    }


    public void closeMqtt() {
        if (null == common) {
            common = new Common();
        }
        common.closeMQtt();
    }


    /**
     * 打开Activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 关闭Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 关闭指定Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 关闭所有的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 当前界面恢复时的操作
     */
    public void resumeActivity(Activity activity) {
        if (activityStack.lastElement() == activity) {
            return;
        }
        activityStack.remove(activity);
        activityStack.push(activity);
    }



    //    商品是否售罄
    public static boolean isSellNull(YWGoodBean item) {

        boolean isnull = false;

//        for (YWGoodBean.SpecsBean spec : item.getSpecs()) {
//            if (Double.parseDouble(spec.getRepertory()) != 0) {
//                isnull = false;
//                break;
//            }
//        }

        return isnull;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // FIXME 允许接收rpRunPlugin等Gradle Task，发布时请务必关掉，以免出现问题
        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
    }

    /**
     * RePlugin允许提供各种“自定义”的行为，让您“无需修改源代码”，即可实现相应的功能
     */
    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig c = new RePluginConfig();

        // 允许“插件使用宿主类”。默认为“关闭”
        c.setUseHostClassIfNotFound(true);

        // FIXME RePlugin默认会对安装的外置插件进行签名校验，这里先关掉，避免调试时出现签名错误
        c.setVerifySign(!BuildConfig.DEBUG);

        // 针对“安装失败”等情况来做进一步的事件处理
        c.setEventCallbacks(new HostEventCallbacks(this));

        // FIXME 若宿主为Release，则此处应加上您认为"合法"的插件的签名，例如，可以写上"宿主"自己的。
        // RePlugin.addCertSignature("AAAAAAAAA");

        // 在Art上，优化第一次loadDex的速度
        // c.setOptimizeArtLoadDex(true);
        return c;
    }

    @Override
    protected RePluginCallbacks createCallbacks() {
        return new HostCallbacks(this);
    }

    /**
     * 宿主针对RePlugin的自定义行为
     */
    private class HostCallbacks extends RePluginCallbacks {

        private static final String TAG = "HostCallbacks";

        private HostCallbacks(Context context) {
            super(context);
        }

        @Override
        public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
            // FIXME 当插件"没有安装"时触发此逻辑，可打开您的"下载对话框"并开始下载。
            // FIXME 其中"intent"需传递到"对话框"内，这样可在下载完成后，打开这个插件的Activity
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onPluginNotExistsForActivity: Start download... p=" + plugin + "; i=" + intent);
            }
            return super.onPluginNotExistsForActivity(context, plugin, intent, process);
        }

        /*
        @Override
        public PluginDexClassLoader createPluginClassLoader(PluginInfo pi, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
            String odexName = pi.makeInstalledFileName() + ".dex";
            if (RePlugin.getConfig().isOptimizeArtLoadDex()) {
                Dex2OatUtils.injectLoadDex(dexPath, optimizedDirectory, odexName);
            }

            long being = System.currentTimeMillis();
            PluginDexClassLoader pluginDexClassLoader = super.createPluginClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent);

            if (BuildConfig.DEBUG) {
                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader use:" + (System.currentTimeMillis() - being));
                String odexAbsolutePath = (optimizedDirectory + File.separator + odexName);
                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader odexSize:" + InterpretDex2OatHelper.getOdexSize(odexAbsolutePath));
            }

            return pluginDexClassLoader;
        }
        */
    }

    private class HostEventCallbacks extends RePluginEventCallbacks {

        private static final String TAG = "HostEventCallbacks";

        public HostEventCallbacks(Context context) {
            super(context);
        }

        @Override
        public void onInstallPluginFailed(String path, InstallResult code) {
            // FIXME 当插件安装失败时触发此逻辑。您可以在此处做“打点统计”，也可以针对安装失败情况做“特殊处理”
            // 大部分可以通过RePlugin.install的返回值来判断是否成功
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onInstallPluginFailed: Failed! path=" + path + "; r=" + code);
            }
            super.onInstallPluginFailed(path, code);
        }

        @Override
        public void onStartActivityCompleted(String plugin, String activity, boolean result) {
            // FIXME 当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
            super.onStartActivityCompleted(plugin, activity, result);
        }
    }

    /**
     * 去除小数点后多余的0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
