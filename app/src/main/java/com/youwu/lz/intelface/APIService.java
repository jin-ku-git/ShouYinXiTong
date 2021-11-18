/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface;


import android.content.Context;
import android.util.Base64;


import com.youwu.lz.intelface.model.AccessToken;
import com.youwu.lz.intelface.model.RegParams;
import com.youwu.lz.intelface.parser.RegResultParser;
import com.youwu.lz.intelface.utils.DeviceUuidFactory;
import com.youwu.lz.intelface.utils.HttpUtil;
import com.youwu.lz.intelface.utils.OnResultListener;

import java.io.File;

import static com.youwu.lz.intelface.utils.Base64RequestBody.readFile;


public class APIService {

    private static final String BASE_URL = "https://aip.baidubce.com";

    private static final String ACCESS_TOEKN_URL = BASE_URL + "/oauth/2.0/token?";
    //人脸检测 ---》 返回用户info
    private static final String IDENTIFY_URL = BASE_URL + "/rest/2.0/face/v3/search";
    //人脸注册
    private static final String REG_URL = BASE_URL + "/rest/2.0/face/v3/faceset/user/add";

    private String accessToken;

    private String groupId ="youwu_1";

    private APIService() {

    }

    /**
     * 百度小盒子请求
     *
     * @param listener
     * @param file
     * @param eventWeight
     */
    public void getGoodList2(OnResultListener listener, File file, String eventWeight) {
        RegParams params = new RegParams();
        String base64Img = "";
        try {
            byte[] buf = readFile(file);
            base64Img = new String(Base64.encode(buf, Base64.NO_WRAP));

        } catch (Exception e) {
            e.printStackTrace();
        }
        RegResultParser parser = new RegResultParser();
         final String GOOD_LIST_PHOTO = "/api/productenter/v1/cashierrank";
        HttpUtil.getInstance().post_baidu3(GOOD_LIST_PHOTO, base64Img, parser, listener);

    }

    private static volatile APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();


                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        // 采用deviceId分组
        HttpUtil.getInstance().init();
        DeviceUuidFactory.init(context);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 设置accessToken 如何获取 accessToken 详情见:
     *
     * @param accessToken accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 明文aksk获取token
     *
     * @param listener
     * @param context
     * @param ak
     * @param sk
     */
    public void initAccessTokenWithAkSk(final OnResultListener<AccessToken> listener, Context context, String ak,
                                        String sk) {

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(ak);
        sb.append("&client_secret=").append(sk);
        sb.append("&grant_type=client_credentials");
        HttpUtil.getInstance().getAccessToken(listener, ACCESS_TOEKN_URL, sb.toString());

    }

    /**
     * 注册
     *
     * @param listener
     * @param file
     * @param uid
     * @param username
     */
    public void reg(OnResultListener listener, File file, String uid, String username) {
        RegParams params = new RegParams();
        String base64Img = "";
        try {
            byte[] buf = readFile(file);
            base64Img = new String(Base64.encode(buf, Base64.NO_WRAP));

        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setImgType("BASE64");
        params.setBase64Img(base64Img);
        params.setGroupId(groupId);

        params.setUserId(uid);
        params.setUserInfo(username);
        // 参数可以根据实际业务情况灵活调节
        params.setQualityControl("NORMAL");
        params.setLivenessControl("NORMAL");

        RegResultParser parser = new RegResultParser();
        String url = urlAppendCommonParams(REG_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }

    /**
     * 人脸识别 1:N 接口
     *
     * @param listener 回调
     * @param file     人脸图片文件
     */

    public void identify(OnResultListener listener, File file) {
        RegParams params = new RegParams();
        String base64Img = "";
        try {
            byte[] buf = readFile(file);
            base64Img = new String(Base64.encode(buf, Base64.NO_WRAP));

        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setImgType("BASE64");
        params.setBase64Img(base64Img);
        params.setGroupIdList(groupId);
        // 可以根据实际业务情况灵活调节
        params.setQualityControl("NORMAL");
        params.setLivenessControl("NORMAL");

        RegResultParser parser = new RegResultParser();
        String url = urlAppendCommonParams(IDENTIFY_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }

    /**
     * URL append access token，sdkversion，aipdevid
     *
     * @param url
     * @return
     */
    private String urlAppendCommonParams(String url) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?access_token=").append(accessToken);

        return sb.toString();
    }
}
