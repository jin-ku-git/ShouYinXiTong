/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.youwu.lz.intelface.APIService;
import com.youwu.lz.intelface.exception.FaceError;
import com.youwu.lz.intelface.model.AccessToken;
import com.youwu.lz.intelface.model.RequestParams;
import com.youwu.lz.intelface.parser.AccessTokenParser;
import com.youwu.lz.intelface.parser.Parser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用okhttp请求tokeh和调用服务
 */
public class HttpUtil {

    private OkHttpClient client;
    private Handler handler;
    private static volatile HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public void init() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public <T> void post(String path, RequestParams params, final Parser<T> parser, final OnResultListener<T>
            listener) {
        post(path, "images", params, parser, listener);
    }

    public <T> void post(String path, String key, RequestParams params,
                         final Parser<T> parser, final OnResultListener<T> listener) {
        Base64RequestBody body = new Base64RequestBody();
        try {
            LogUtil.i("wtf", "body size->" + body.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        body.setKey(key);
        body.setFileParams(params.getFileParams());
        body.setStringParams(params.getStringParams());
        final Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        // liujinhui 经常client为空指针 ？
        if (client == null) {
            FaceError err = new FaceError(-999, "okhttp inner error");
            listener.onError(err);
            return;
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final FaceError error = new FaceError(FaceError.ErrorCode.NETWORK_REQUEST_ERROR,
                        "network request error", e);
                Log.i("faceError---onFailure","人脸识别失败，faceError："+error);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                LogUtil.i("wtf", "onResponse json->" + responseString);
                final T result;
                try {
                    result = parser.parse(responseString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResult(result);
                        }
                    });
                } catch (final FaceError faceError) {
                    faceError.printStackTrace();
                    Log.i("faceError---onResponse","人脸识别失败，faceError："+faceError);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(faceError);
                        }
                    });
                }

            }
        });
    }
    public <T> void post_baidu3(String path, String params, final Parser<T> parser, final OnResultListener<T>
            listener) {
        post_baidu3(path, "images", params, parser, listener);
    }

    public <T> void post_baidu3(String path, String key, String params,
                                final Parser<T> parser, final OnResultListener<T> listener) {

        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        HashMap<String, Object> map = new HashMap<>();
        map.put("image", params);


        String json = JSON.toJSONString(map);

        RequestBody formbody = RequestBody.create(mediaType, json);
//        String identifyUrl = SPUtils.getInstance().getString("identify_url");
        String identifyUrl = "http://192.168.1.180:8081/api/productenter/v1/cashierrank/exhibition";
        final Request request = new Request.Builder()
                .url(identifyUrl)
                .post(formbody)
                .build();
        // liujinhui 经常client为空指针 ？
        if (client == null) {
            FaceError err = new FaceError(-999, "okhttp inner error");
            listener.onError(err);
            return;
        }




        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final FaceError error = new FaceError(FaceError.ErrorCode.NETWORK_REQUEST_ERROR,
                        "network request error", e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                LogUtil.i("wtf", "onResponse json->" + responseString);
                final T result;
                try {
                    result = parser.parse(responseString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResult(result);
                        }
                    });
                } catch (final FaceError faceError) {
                    faceError.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(faceError);
                        }
                    });
                }

            }
        });
    }


    public void getAccessToken(final OnResultListener<AccessToken> listener, String url, String param) {

        final AccessTokenParser accessTokenParser = new AccessTokenParser();
        RequestBody body = RequestBody.create(MediaType.parse("text/html"), param);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                FaceError error = new FaceError(FaceError.ErrorCode.NETWORK_REQUEST_ERROR, "network request error", e);
                listener.onError(error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || response.body() == null || TextUtils.isEmpty(response.toString())) {
                    throwError(listener, FaceError.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                            "token is parse error, please rerequest token");
                }
                try {
                    AccessToken accessToken = accessTokenParser.parse(response.body().string());
                    if (accessToken != null) {
                        APIService.getInstance().setAccessToken(accessToken.getAccessToken());
                        listener.onResult(accessToken);
                    } else {
                        throwError(listener, FaceError.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                                "token is parse error, please rerequest token");
                    }
                } catch (FaceError error) {
                    error.printStackTrace();
                    listener.onError(error);
                }
            }
        });

    }

    /**
     * throw error
     *
     * @param errorCode
     * @param msg
     * @param listener
     */
    private static void throwError(OnResultListener<AccessToken> listener, int errorCode, String msg) {
        FaceError error = new FaceError(errorCode, msg);
        listener.onError(error);
    }

    /**
     * 释放资源
     */
    public void release() {
        client = null;
        handler = null;
    }
}
