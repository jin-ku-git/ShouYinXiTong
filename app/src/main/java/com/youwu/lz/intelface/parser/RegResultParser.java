/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.parser;

import android.util.Log;

import com.youwu.lz.intelface.exception.FaceError;
import com.youwu.lz.intelface.model.RegResult;

import org.json.JSONException;
import org.json.JSONObject;

public class RegResultParser implements Parser<RegResult> {


    @Override
    public RegResult parse(String json) throws FaceError {
        Log.e("xx", "oarse:" + json);

        try {
            JSONObject jsonObject = new JSONObject(json);
            //正式使用时 此处重新判断 -by 7.7 lz
//            if (jsonObject.has("error_code")) {
//                FaceError error = new FaceError(jsonObject.optInt("error_code"), jsonObject.optString("error_msg"));
//                throw error;
//            }

            RegResult result = new RegResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceError error = new FaceError(FaceError.ErrorCode.JSON_PARSE_ERROR, "Json parse error:" + json, e);
            throw error;
        }
    }
}
