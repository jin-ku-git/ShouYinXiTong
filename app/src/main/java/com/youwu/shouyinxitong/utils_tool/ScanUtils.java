package com.youwu.shouyinxitong.utils_tool;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

public class ScanUtils {

    public static ScanUtils scanUtils;

    boolean mCaps;
    public int MESSAGE_DELAY = 500;
    StringBuffer mStringBufferResult;

    public static ScanUtils getInstance() {
        if (null == scanUtils) {
            scanUtils = new ScanUtils();
        }
        return scanUtils;
    }

    private boolean hasScanGun(Context context) {
        Configuration cfg = context.getResources().getConfiguration();
        return cfg.keyboard != Configuration.KEYBOARD_NOKEYS;
    }


    /**
     * 检测输入设备是否是扫码器 * * @param context * @return 是的话返回true，否则返回false
     */
    public boolean isInputFromScanner(Context context, KeyEvent event) {
        if (event.getDevice() == null) {
            return false;
        }
        // event.getDevice().getControllerNumber();

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            //实体按键，若按键为返回、音量加减、返回false
            return false;
        }
        if (event.getDevice().getSources() == (InputDevice.SOURCE_KEYBOARD | InputDevice.SOURCE_DPAD | InputDevice.SOURCE_CLASS_BUTTON)) {
            //虚拟按键返回false
            return false;
        }
        Configuration cfg = context.getApplicationContext().getResources().getConfiguration();
        return cfg.keyboard != Configuration.KEYBOARD_UNDEFINED;

    }

    Runnable mScanningFishedRunnable = new Runnable() {
        @Override
        public void run() {
            if (mStringBufferResult==null){
                //防止闪退问题
                return;
            }
            String code = mStringBufferResult.toString();
//            ToastUtil.showLongToast(code);
            Log.e("scanToWork", "  Util  " + code);
            if (null != onResultListener) {
                onResultListener.onResult(code);
            }
            //做相应处理....
            mStringBufferResult.setLength(0);
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 扫码枪事件解析 * * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();

        Log.e("scanToWork", "analysisKeyEvent:" + keyCode);
        //字母大小写判断
        checkLetterStatus(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char aChar = getInputCode(event);
            // char aChar = (char) event.getUnicodeChar();
            if (aChar != 0) {
                if (null == mStringBufferResult) {
                    mStringBufferResult = new StringBuffer();
                }
                mStringBufferResult.append(aChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //若为回车键，直接返回
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else {
                //延迟post，若500ms内，有其他事件
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }
        }
    }

    //检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                mCaps = true;
            } else {
                //松开shift键，表示小写
                mCaps = false;
            }
        }
    }

    //获取扫描内容
    private char getInputCode(KeyEvent event) {
        int keyCode = event.getKeyCode();
        Log.e("scanToWork", "getInputCode:" + keyCode);
        char aChar;

        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }

        return aChar;
    }

    public OnResultListener onResultListener;

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(String resultStr);
    }
}
