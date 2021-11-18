/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RegParams implements RequestParams {

    private Map<String, String> params = new HashMap<>();
    private Map<String, File> fileMap = new HashMap<>();
    private String jsonParams = "";


    @Override
    public Map<String, File> getFileParams() {
        return fileMap;
    }

    @Override
    public Map<String, String> getStringParams() {
        return params;
    }


    private String userId;
    private String uid;
    private String groupId;

    private String userInfo;

    public void setUserId(String userId) {
        putParam("user_id", userId);
    }

    public void setUid(String uid) {
        putParam("uid", uid);
    }


    public void setGroupId(String groupId) {

        putParam("group_id", groupId);
    }

    public void setGroupIdList(String groupIdList) {
        putParam("group_id_list", groupIdList);
    }
    public void setImgType(String imgType) {
        putParam("image_type", imgType);
    }
    public void setQualityControl(String qualControl) {
        putParam("quality_control", qualControl);
    }
    public void setLivenessControl(String liveControl) { putParam("liveness_control", liveControl); }


    public void setBase64Img(String base64Img) {
        putParam("image", base64Img);
    }

    public void setUserInfo(String userInfo) {
        putParam("user_info", userInfo);
    }

    public void setToken(String token) {
        putParam("access_token", token);
    }

    public void setImageFile(File imageFile) {
        fileMap.put(imageFile.getName(), imageFile);
    }

    private void putParam(String key, String value) {
        if (value != null) {
            params.put(key, value);
        } else {
            params.remove(key);
        }
    }

    private void putParam(String key, boolean value) {
        if (value) {
            putParam(key, "true");
        } else {
            putParam(key, "false");
        }
    }
}
