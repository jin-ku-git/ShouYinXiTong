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
    public static TextToSpeech textToSpeech = null;//????????????????????????
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

    private static Toast sToast; // ??????Toast,???????????????????????????????????????
    private static final Handler sHandler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
        //????????????????????????
        KLog.init(BuildConfig.DEBUG);
//        KLog.init(true);
        //???????????????????????????
        initCrash();

        //??????????????????
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

        JPushInterface.setDebugMode(true);    // ??????????????????,????????????????????????
        JPushInterface.init(this);            // ????????? JPush

        sToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        gson = new Gson();
        context = getApplicationContext();
        spUtils = new SPUtils("com.biyesheji.qiangchewei");
        // ????????? SDK ???????????????????????? context ??????????????? ApplicationContext
//        SDKInitializer.initialize(this);

        CrashReport.initCrashReport(getApplicationContext(), "5f73632816", false);

        //sunmi  ????????????
        AidlUtil.getInstance().connectPrinterService(this);

        initLib();

        APIService.getInstance().init(this);
        APIService.getInstance().setGroupId(Config.groupID);
        // ???ak???sk??????token, ????????????api????????????????????????????????????ak???sk????????????????????????????????????
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
         * ?????????
         */
        //??????????????????okhttp???????????????????????????????????????????????????okhttp???????????????????????????okhttp???????????????????????????????????????OkHttpClient ??????UpdateConfig?????????setCustomDownloadConnectionCreator start
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30_000, TimeUnit.SECONDS)
                .readTimeout(30_000, TimeUnit.SECONDS)
                .writeTimeout(30_000, TimeUnit.SECONDS)
                //???????????????????????????????????????????????????????????????????????????????????????????????? start
                .sslSocketFactory(SSLUtils.createSSLSocketFactory())
                .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                //???????????????????????????????????????????????????????????????????????????????????????????????? end
                .retryOnConnectionFailure(true);
        //??????????????????okhttp???????????????????????????????????????????????????okhttp???????????????????????????okhttp???????????????????????????????????????OkHttpClient ??????UpdateConfig?????????setCustomDownloadConnectionCreator end

        //???????????????
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)//?????????Debug??????
                .setBaseUrl("http://www.cretinzp.com/system/versioninfo")//???dataSourceType???DATA_SOURCE_TYPE_URL?????????????????????????????????????????????
                .setMethodType(TypeConfig.METHOD_GET)//???dataSourceType???DATA_SOURCE_TYPE_URL???????????????????????????
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_URL)//?????????????????????????????????
                .setShowNotification(true)//??????????????????????????????????????????????????????
                .setNotificationIconRes(R.mipmap.download_icon)//??????????????????????????????
                .setUiThemeType(TypeConfig.UI_THEME_AUTO)//??????UI?????????????????????12?????????????????????
                .setRequestHeaders(null)//???dataSourceType???DATA_SOURCE_TYPE_URL??????????????????????????????
                .setRequestParams(null)//???dataSourceType???DATA_SOURCE_TYPE_URL?????????????????????????????????
                .setAutoDownloadBackground(false)//????????????????????????????????????????????????true????????????checkUpdate???????????????????????????????????????????????????????????????????????????UI?????????TypeConfig.UI_THEME_CUSTOM????????????????????????????????????????????????Activity???????????????????????????????????????????????????????????????setShowNotification(false)???????????????????????????????????????????????????
//                .setCustomActivityClass(CustomActivity.class)//??????????????????UI?????????TypeConfig.UI_THEME_CUSTOM?????????????????????????????????Activity?????????RootActivity????????????demo???????????????????????????????????????Activity???class
                .setNeedFileMD5Check(false)//???????????????????????????MD5??????????????????????????????????????????????????????MD5????????????DEMO????????????????????????MD5??????????????????????????????????????????????????????Md5Utils
                .setCustomDownloadConnectionCreator(new OkHttp3Connection.Creator(builder))//??????????????????okhttp????????????????????????????????????????????????????????????OkHttpClient????????????demo????????????OkHttp3Connection????????????ConnectionCreator??????????????????????????????????????????????????????????????????????????????????????????????????????apk?????????
                .setModelClass(new UpdateModel());
        //?????????
        AppUpdateUtils.init(this, updateConfig);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //????????????,???????????????
                .enabled(true) //??????????????????????????????
                .showErrorDetails(true) //??????????????????????????????
                .showRestartButton(true) //????????????????????????
                .trackActivities(true) //????????????Activity
                .minTimeBetweenCrashesMs(2000) //?????????????????????(??????)
                .errorDrawable(R.mipmap.ic_launcher) //????????????
                .restartActivity(LoginActivity.class) //??????????????????activity
//                .errorActivity(YourCustomErrorActivity.class) //??????????????????activity
//                .eventListener(new YourCustomEventListener()) //????????????????????????
                .apply();
    }

    /**
     * ?????????SDK
     */
    private void initLib() {
        // ??????android???ios ???????????????appId=appname_face_android ,??????appname?????????sdk???????????????
        // ???????????????
        // ??????License?????????APPID
        // assets?????????License?????????
        FaceSDKManager.getInstance().init(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        //??????????????????????????????????????????????????????
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
     * ????????????Context
     */
    public static Context getContext() {
        return context;
    }

    public static void initTTS(Context context) {
        //???????????????????????????
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    // Toast.makeText(MainActivity.this,"??????????????????",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//????????????????????????
                    textToSpeech.setSpeechRate(1.0f);//??????????????????

                    //????????????????????????????????????
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US????????????--???" + a +
                            "\nzh-CN????????????--???" + b);

                }

            }
        });
    }

    private void setFaceConfig() {
        FaceTracker tracker = FaceSDKManager.getInstance().getFaceTracker(this);
        // SDK???????????????????????????????????????????????????????????????????????????????????????????????????

        // ??????????????? (0-1) ????????????0.7
        tracker.set_blur_thr(FaceEnvironment.VALUE_BLURNESS);
        // ???????????? (0-1) ????????????40
        tracker.set_illum_thr(FaceEnvironment.VALUE_BRIGHTNESS);
        // ??????????????????
        tracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // ??????yaw,pitch,row ??????????????????-45???45????????????-15-15
        tracker.set_eulur_angle_thr(FaceEnvironment.VALUE_HEAD_PITCH, FaceEnvironment.VALUE_HEAD_ROLL,
                FaceEnvironment.VALUE_HEAD_YAW);

        // ??????????????????????????????????????????????????????????????????80-200??? ???????????????????????????120-200
        tracker.set_min_face_size(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        //
        tracker.set_notFace_thr(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // ?????????????????? ???0-1??? ????????????0.5
        tracker.set_occlu_thr(FaceEnvironment.VALUE_OCCLUSION);
        // ????????????????????????
        tracker.set_isCheckQuality(true);
        // ????????????????????????
        tracker.set_isVerifyLive(false);
        // ???????????????cpu?????????????????????sdk???????????????????????????????????????2??????????????????4
        FaceSDK.setNumberOfThreads(2);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        stopLogcatManager();
    }

    //??????????????????
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
    //todo ???????????????????????????????????????
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


    //????????????
    public void openDraw() {
        Intent intent = new Intent();
        // ????????? Android 5.0??????????????????????????? Intent ?????? service?????????????????????
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
     * ??????Activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * ????????????Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * ??????Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * ????????????Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * ???????????????Activity
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
     * ??????????????????????????????
     */
    public void resumeActivity(Activity activity) {
        if (activityStack.lastElement() == activity) {
            return;
        }
        activityStack.remove(activity);
        activityStack.push(activity);
    }



    //    ??????????????????
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

        // FIXME ????????????rpRunPlugin???Gradle Task????????????????????????????????????????????????
        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
    }

    /**
     * RePlugin????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig c = new RePluginConfig();

        // ?????????????????????????????????????????????????????????
        c.setUseHostClassIfNotFound(true);

        // FIXME RePlugin?????????????????????????????????????????????????????????????????????????????????????????????????????????
        c.setVerifySign(!BuildConfig.DEBUG);

        // ???????????????????????????????????????????????????????????????
        c.setEventCallbacks(new HostEventCallbacks(this));

        // FIXME ????????????Release??????????????????????????????"??????"??????????????????????????????????????????"??????"????????????
        // RePlugin.addCertSignature("AAAAAAAAA");

        // ???Art?????????????????????loadDex?????????
        // c.setOptimizeArtLoadDex(true);
        return c;
    }

    @Override
    protected RePluginCallbacks createCallbacks() {
        return new HostCallbacks(this);
    }

    /**
     * ????????????RePlugin??????????????????
     */
    private class HostCallbacks extends RePluginCallbacks {

        private static final String TAG = "HostCallbacks";

        private HostCallbacks(Context context) {
            super(context);
        }

        @Override
        public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
            // FIXME ?????????"????????????"????????????????????????????????????"???????????????"??????????????????
            // FIXME ??????"intent"????????????"?????????"?????????????????????????????????????????????????????????Activity
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
            // FIXME ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ?????????????????????RePlugin.install?????????????????????????????????
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onInstallPluginFailed: Failed! path=" + path + "; r=" + code);
            }
            super.onInstallPluginFailed(path, code);
        }

        @Override
        public void onStartActivityCompleted(String plugin, String activity, boolean result) {
            // FIXME ?????????Activity????????????????????????????????????????????????APM??????????????????????????????
            super.onStartActivityCompleted(plugin, activity, result);
        }
    }

    /**
     * ???????????????????????????0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//???????????????0
            s = s.replaceAll("[.]$", "");//??????????????????.?????????
        }
        return s;
    }
}
