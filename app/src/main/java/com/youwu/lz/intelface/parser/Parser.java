/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.parser;

import com.youwu.lz.intelface.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError;
}
