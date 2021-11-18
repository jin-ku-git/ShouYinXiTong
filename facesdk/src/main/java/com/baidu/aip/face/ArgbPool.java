/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.face;


import androidx.core.util.Pools;

public class ArgbPool {

    Pools.SynchronizedPool<int[]> pool = new Pools.SynchronizedPool<>(5);

    public ArgbPool() {

    }

    public int[] acquire(int width, int height) {
        int[] argb = pool.acquire();
        if (argb == null || argb.length != width * height) {

            // TODO: 2019/6/21  java.lang.OutOfMemoryError: Failed to allocate a 31961100 byte allocation with 16382736 free bytes and 15MB until OOM
//            3264 2448
//            at com.baidu.aip.face.ArgbPool.acquire(ArgbPool.java:19)
//            at com.baidu.aip.face.CameraImageSource$1.onPreviewFrame(CameraImageSource.java:69)
//            at com.baidu.aip.face.CameraImageSource$1.onPreviewFrame(CameraImageSource.java:66)
//            at com.baidu.aip.face.camera.Camera1Control$3.onPreviewFrame(Camera1Control.java:239)
//            at android.hardware.Camera$EventHandler.handleMessage(Camera.java:1115)
//            at android.os.Handler.dispatchMessage(Handler.java:102)
//            at android.os.Looper.loop(Looper.java:154)
//            at android.os.HandlerThread.run(HandlerThread.java:61)
            argb = new int[width * height];
        }
        return argb;
    }

    public void release(int[] data) {
        try {
            pool.release(data);
        } catch (IllegalStateException ignored) {
            // ignored
        }
    }
}
