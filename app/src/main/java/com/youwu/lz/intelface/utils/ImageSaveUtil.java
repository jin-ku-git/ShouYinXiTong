/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageSaveUtil {

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "facesharp" + File.separator;

    public static boolean deleteCameraBitmap(Context context, String filename) {
        boolean deleted = false;
        if (isMemeryOk(context)) {
            File file = new File(context.getFilesDir(), filename);
            if (file.exists()) {
                file.delete();
            }

        } else {
            String dir = SDCARD_PATH + DeviceUtil.getImei(context);
            File file = new File(dir, filename);
            if (file.exists()) {
                file.delete();
            }

        }
        return deleted;
    }

    public static String saveCameraBitmap(Context context, Bitmap image, String filename) {
        if (image == null) {
            return "";
        }
        String fullPath = "";
        FileOutputStream fileOS = null;
        try {
            if (isMemeryOk(context)) {
                File file = new File(context.getFilesDir(), filename);
                if (file.exists()) {
                    file.delete();
                }
                fileOS = new FileOutputStream(file);
                Uri.fromFile(file);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
                fullPath = file.getAbsolutePath();
            } else {
                fullPath = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOS != null) {
                try {
                    fileOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fullPath;
    }

    public static String loadCameraBitmapPath(Context context, String filename) {
        String filePath = context.getFilesDir() + File.separator + filename;
        File path = new File(filePath);
        if (!path.exists()) {
            filePath = "";
        }
        return filePath;
    }

    public static Bitmap loadCameraBitmap(Context context, String filename) {
        Bitmap image = null;
        String filePath = context.getFilesDir() + File.separator + filename;
        File path = new File(filePath);
        if (!path.exists()) {
            filePath = "";
        }

        File file = new File(filePath);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            image = BitmapFactory.decodeStream(is, null, opts);

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

    public static Bitmap loadBitmapFromPath(Context context, String filePath) {
        Bitmap image = null;
        File path = new File(filePath);
        if (!path.exists()) {
            return null;
        }

        InputStream is = null;
        try {
            is = new FileInputStream(path);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            image = BitmapFactory.decodeStream(is, null, opts);

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

    public static boolean isMemeryOk(Context context) { // 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        long total = mi.availMem;

        return total > 9999 ? true : false;
    }

}