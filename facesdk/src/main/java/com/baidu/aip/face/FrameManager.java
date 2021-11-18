/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.face;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.baidu.aip.ImageFrame;
import com.baidu.aip.face.stat.Ast;

import java.util.ArrayList;

/**
 * 封装了检测的整体逻辑。
 */
public class FrameManager {
    /**
     * 该回调用于回调
     */
    private Context mContext;
    private boolean isStarted = false;

    public interface OnFrameListener {
        void onFrame(ImageFrame imageFrame);
    }

    public FrameManager(Context context) {
        mContext = context;
        Ast.getInstance().init(context.getApplicationContext(), "3.3.0.0", "facedetect");
    }

    /**
     * 图片源，获取检测图片。
     */
    private ImageSource imageSource;
    /**
     * 帧图片监听器
     */
    private OnFrameListener listener;
    private HandlerThread processThread;
    private Handler processHandler;
    private Handler uiHandler;
    private ImageFrame lastFrame;


    private ArrayList<FaceProcessor> preProcessors = new ArrayList<>();

    public void setListener(OnFrameListener listener) {
        this.listener = listener;
    }

    /**
     * 设置图片帧来源
     *
     * @param imageSource 图片来源
     */
    public void setImageSource(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    /**
     * @return 返回图片来源
     */
    public ImageSource getImageSource() {
        return this.imageSource;
    }

    public void start() {
        if (isStarted) {
            return;
        }
        this.imageSource.addOnFrameAvailableListener(onFrameAvailableListener);
        processThread = new HandlerThread("process");
        processThread.setPriority(10);
        processThread.start();
        processHandler = new Handler(processThread.getLooper());
        uiHandler = new Handler();
        this.imageSource.start();
        isStarted = true;
    }

    private Runnable processRunnable = new Runnable() {
        @Override
        public void run() {
            if (lastFrame == null) {
                return;
            }
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            int[] argb;
            int width;
            int height;
            ArgbPool pool;
            synchronized (lastFrame) {
                argb = lastFrame.getArgb();
                width = lastFrame.getWidth();
                height = lastFrame.getHeight();
                pool = lastFrame.getPool();
                lastFrame = null;
            }
            process(argb, width, height, pool);
        }
    };

    public void stop() {
        if (imageSource != null) {
            this.imageSource.stop();
            this.imageSource.removeOnFrameAvailableListener(onFrameAvailableListener);
        }

        if (processThread != null) {
            processThread.quit();
            processThread = null;
        }
        Ast.getInstance().immediatelyUpload();
        isStarted = false;
    }


    private void process(int[] argb, int width, int height, ArgbPool pool) {

        ImageFrame frame = imageSource.borrowImageFrame();
        frame.setArgb(argb);
        frame.setWidth(width);
        frame.setHeight(height);
        frame.setPool(pool);
        //        frame.retain();

        if (null != listener) {
            listener.onFrame(frame);
        }

        frame.release();

    }

    private OnFrameAvailableListener onFrameAvailableListener = new OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(ImageFrame imageFrame) {
            lastFrame = imageFrame;
            processHandler.removeCallbacks(processRunnable);
            try {
                processHandler.post(processRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            processRunnable.run();
        }
    };
}
