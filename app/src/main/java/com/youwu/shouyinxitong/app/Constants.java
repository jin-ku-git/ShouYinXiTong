package com.youwu.shouyinxitong.app;


/**
*@author LzCode@qq.com
*@date on 2018-8-9 10:36:17
*@describe
**/

public class Constants {


    public static final String MQTT_SERVER_URI = "tcp://mqtt.youwuu.com:1883";
    public static final String MQTT_CLIENTID = "ExampleAndroidClient";
    public static final String MQTT_SUBSCRIPTIONTOPIC = "/catering/kds/cashier/A00011"; //订阅主题  --- RFID
//    public static final String MQTT_SUBSCRIPTIONTOPIC = "/youwu/box/vendingMachine/AI/serialNumber/A00011"; //订阅主题    --- 视觉识别
//    public static final String MQTT_SUBSCRIPTIONTOPIC = "common_couplet "; //订阅主题
    public static final String MQTT_PUBLISHTOPIC = "test_push";//发送主题
    public static final String MQTT_USERNAME = "admin";
    public static final String MQTT_PASSWORD = "youwuzhineng_2018";

    public static final String BOX = "http://box.youwuu.com";

//    public class API {
//
//        public static final String INITFACE = "http://yw4.youwuu.com/hardwareapi/scavenging/initFace";            //初始化
//        public static final String FACEOPEN = "http://yw4.youwuu.com/hardwareapi/scavenging/faceOpen";            //在线版开锁
//        public static final String JUIDCARD = "http://yw4.youwuu.com/hardwareapi/scavenging/card_open_door";      //判断IDCARD能否开门
//        public static final String OFFLINEFACE = "http://yw4.youwuu.com/hardwareapi/scavenging/offlineFace";      //离线版开锁
//
////        //========================================RFID==========TEST-START===========================================
////        public static final String ACTIVATION = "http://yueluowusheng.imwork.net/api/machine/vision/activation";    //设备激活
////        public static final String AUTHENTICATION = "http://yueluowusheng.imwork.net/api/machine/vision/authentication";  //设备认证
////        public static final String OPENDOORFORRFID = "http://yueluowusheng.imwork.net/api/machine/vision/openDoor";  //识别用户开门
////        //========================================RFID==========TEST-END===========================================
//
//        //========================================视觉识别==========START===========================================
//        public static final String ISGETUID = BOX + "/api/machine/vision/cheackMember";  //识别用户开门
//        public static final String PAYMENT = BOX + "/api/machine/vision/creatOrder";  //关锁创建订单并支付
//        //========================================视觉识别==========END===========================================
//
//        //========================================RFID==========START===========================================
//        public static final String ACTIVATION = BOX + "/api/machine/vision/activation";    //设备激活
//        public static final String AUTHENTICATION = BOX + "/api/machine/vision/authentication";  //设备认证
//        public static final String OPENDOORFORRFID = BOX + "/api/machine/vision/openDoor";  //识别用户开门
//        //========================================RFID==========END===========================================
//
//        public static final String UPDATA = "https://www.youwuu.com/api/app_version/index";        //检测并自动更新
//
//    }




}
