/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.baidu.aip.ImageFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {

        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Log.d("ImageUtil", "retX is:" + retX + " retY is:" + retY);

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
      /*  if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        } */

        // 下面这句是关键
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
        // false);
    }
    public static void preBit(Bitmap bitmap, File outputFile){
        try {
            FileOutputStream out = new FileOutputStream(outputFile);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isToUpLoad(int frameAfter[], int frameNow[] ){
        boolean isToUpload = false;

        //过滤值
        int difAllow = 100;

        if (null == frameAfter || null == frameNow || frameAfter.length == 0 || frameNow.length == 0){
            return isToUpload;
        }
        double sum = 0;
        for (int i = 0;i < frameAfter.length ; i ++){
            sum += Math.pow((frameAfter[i] - frameNow[i]), 2);
        }

        int difPx = (int) Math.sqrt(sum);
//        Log.e("bitpxarray","difPX  "+difPx);
        if (difPx < difAllow){
            return isToUpload;
        }
        isToUpload = true;
        return isToUpload;
    }

    public static int [] getBitPxArray(ImageFrame frame){

        Bitmap bmp = Bitmap.createBitmap(frame.getArgb(), frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);

        int bitmapWidth = bmp.getWidth();
        int bitmapHeight = bmp.getHeight();
        // 图片大于最大高宽，按大的值缩放
        if (bitmapWidth > 10 || bitmapHeight > 10) {
            float widthScale = 10 * 1.0f / bitmapWidth;
            float heightScale = 10 * 1.0f / bitmapHeight;

            float scale = Math.min(widthScale, heightScale);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
        }

        int frameArray[] = new int[bmp.getWidth() * bmp.getHeight()];

        //创建存储像素的数组

        int pixels[] = new int[bmp.getWidth() * bmp.getHeight()];

        //取得像素数据

        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int a = Color.alpha(pixels[i]);
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);

            frameArray[i] = (a + r + g + b) / 4;
        }
//        Log.e("bitpxarray","array  length  "+frameArray.length);

        return frameArray;
    }
    public static void resize(Bitmap bitmap, File outputFile, int maxWidth, int maxHeight) {
        try {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 图片大于最大高宽，按大的值缩放
            if (bitmapWidth > maxHeight || bitmapHeight > maxWidth) {
                float widthScale = maxWidth * 1.0f / bitmapWidth;
                float heightScale = maxHeight * 1.0f / bitmapHeight;

                float scale = Math.min(widthScale, heightScale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            }
            LogUtil.i("APIService", "upload face size" + bitmap.getWidth() + "*" + bitmap.getHeight());
            // save image
            FileOutputStream out = new FileOutputStream(outputFile);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resize(String inputPath, String outputPath, int dstWidth, int dstHeight) {
        try {
            int inWidth;
            int inHeight;

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(inputPath, options);

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            LogUtil.i("APIService", "origin " + inWidth + " " + inHeight);

            Matrix m = new Matrix();
            ExifInterface exif = new ExifInterface(inputPath);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (rotation != 0) {
                m.preRotate(ExifUtil.exifToDegrees(rotation));
            }

            int maxPreviewImageSize = Math.max(dstWidth, dstHeight);
            int size = Math.min(options.outWidth, options.outHeight);
            size = Math.min(size, maxPreviewImageSize);

            options = new BitmapFactory.Options();
            options.inSampleSize = ImageUtil.calculateInSampleSize(options, size, size);
            options.inScaled = true;
            options.inDensity = options.outWidth;
            options.inTargetDensity = size * options.inSampleSize;

            Bitmap roughBitmap = BitmapFactory.decodeFile(inputPath, options);
            roughBitmap = Bitmap.createBitmap(roughBitmap, 0, 0, roughBitmap.getWidth(),
                    roughBitmap.getHeight(), m, false);
            // save image
            FileOutputStream out = new FileOutputStream(outputPath);
            try {
                roughBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void saveBitmap(String outputPath, Bitmap bitmap) {
        // save image
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
