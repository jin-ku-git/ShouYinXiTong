package com.youwu.shouyinxitong.app;


import android.util.Log;

import com.youwu.shouyinxitong.bean_used.PostBean;


public class UriUtils {
//    public static String API_SERVER_URL = "http://testapi.youwuu.com/";//根路径
//      public static String API_SERVER_URL = "http://10.57.58.234:8081/";//根路径
//
//    public static String API_SERVER_URL = "http://cyapi.youwuu.com/";//根路径
//    public static String API_SERVER_URL = "http://baiduapi.youwuu.com/";//根路径
//    public static String API_SERVER_URL = "http://47.92.155.15:38080/app/mock/28/";//根路径
//    public static String API_SERVER_URL = "http://baiduapi.youwuu.com";//长沙展会餐饮收银软件baseapi

    //    public static String API_SERVER_URL = "http://restaurantapi.youwuu.com/catering/api/";//JAVA根路径
//    public static String API_SERVER_URL = "https://cy4.youwuu.com/api_cashier_restaurant/";//测试
//    public static String API_SERVER_URL = "https://moni.youwuu.com/api_cashier_restaurant/";//模拟路径
//    public static String API_SERVER_URL = "https://xmsp.youwuu.com/api_cashier_restaurant/";//PHP根路径
    public static String API_SERVER_URL = "https://xmzcdevapi.youwuu.com/api_cashier_restaurant/";//PHP根路径2021/10/08改


    public static final String MQTT_SERVER_URI = "tcp://mqtt.youwuu.com:1883";
    public static final String MQTT_CLIENTID = "ExampleAndroidClient";
    public static final String MQTT_SUBSCRIPTIONTOPIC = "";
    //    public static final String MQTT_SUBSCRIPTIONTOPIC = PreferencesUtils.getString(DemoApplication.application, YWHttpUtils.PHONE_SN, Settings.System.getString(DemoApplication.application.getContentResolver(), Settings.System.ANDROID_ID)); //订阅主题
    //PreferencesUtils.putString(this, YWHttpUtils.PHONE_SN, Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID));
    public static final String MQTT_PUBLISHTOPIC = "common_couplet";
    public static final String MQTT_USERNAME = "convenience";
    public static final String MQTT_PASSWORD = "bianlidian_2018";

    public static final String BOX = "http://box.youwuu.com";

    public static final String IS_ACCOUNT_USERNAME = "is_account_username_to_opt";

    public static String getRequestPostUrl(Object dataString) {
        Log.d("requstdata", "dataString  " + dataString);
        PostBean postBean = new PostBean();
        postBean.setData(dataString);
        postBean.setToken(AppApplication.spUtils.getString("token", ""));
        String requstData = AppApplication.gson.toJson(postBean);


        Log.d("requstdata", "jsonData  " + AppApplication.gson.toJson(postBean));
        Log.d("requstdata", "URL  " + AppApplication.gson.toJson(postBean));
        Log.d("requstdata", "encodedata  " + requstData);

        return requstData;
    }


}
