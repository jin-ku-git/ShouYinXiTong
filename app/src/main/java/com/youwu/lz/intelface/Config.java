/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface;


public class Config {

    // 为了apiKey,secretKey为您调用百度人脸在线接口的，如注册，识别等。
    // 为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
//    public static String apiKey = "KYmoGlDBfVoOEXTcOE5gfOR3";
//    public static String secretKey = "ZC6krGSSbgYyCfrTl3RDZXul2NlDGxpa";
//    public static String licenseID = "LzIntelFace-face-android";
//    public static String licenseFileName = "idl-license.face-android";

    // 有物资质
    public static String apiKey = "fw2yh1r5G5MjA65HdSZ7aTyI";
    public static String secretKey = "o47weRys6KE8xZTs1asRIn68Fs3lX9h7";
    public static String licenseID = "youwuu-face-android";
    public static String licenseFileName = "idl-license.face-android";

    /**
     * groupId，标识一组用户（由数字、字母、下划线组成），长度限制128B，可以自行定义，只要注册和识别都是同一个组。
     * 详情见 http://ai.baidu.com/docs#/Face-API/top
     * <p>
     * 人脸识别 接口 https://aip.baidubce.com/rest/2.0/face/v2/identify
     * 人脸注册 接口 https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add
     */

//    public static String groupID = "youwu_1";
    public static String groupID = "youwu_six_saas";

}
