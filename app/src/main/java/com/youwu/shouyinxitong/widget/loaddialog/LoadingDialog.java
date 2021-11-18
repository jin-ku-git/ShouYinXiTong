package com.youwu.shouyinxitong.widget.loaddialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;


public class LoadingDialog {
    private static final int CONTEXT_NULL = 0x012;
    static LoadingDialog d = new LoadingDialog();
    private ProgressDialog pDialog;
    static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONTEXT_NULL:
                    get().dismiss();
                    break;
            }
            return false;
        }
    });
    private Context mContext;//用于对比和当前是否是同一个context

    private static LoadingDialog get() {
        return d;
    }

    public static void show(FragmentActivity ctx) {
        show(ctx, "网络请求中...");
    }

    public static void show(FragmentActivity ctx, String msg) {
        if (ctx == null) {
            return;
        }

        try {
            if (get().pDialog == null || get().mContext != ctx) {
                dismiss();
                get().pDialog = ProgressDialog.show(ctx, null, msg, false, false);
                //Activity 生命周期监听
                ctx.getSupportFragmentManager().beginTransaction().add(new LifecycleFragment(new LifecycleFragment.LifecycleListener() {
                    @Override
                    public void onDestroy() {
                        try {
                            Log.d("load", "loadDilaog dissmiss due Activity onDestroy()");
                            dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }), "life").commit();
            } else {
                get().pDialog.setMessage(msg);
            }
            get().mContext = ctx;
        } catch (Exception e) {
            e.printStackTrace();
            //防止未关闭dialog时跳转activity后 内存溢出
            dismiss();
        }
        if (get().pDialog!=null&&!get().pDialog.isShowing()) {
            get().pDialog.show();
        }
    }



    public static void dismiss() {
        try {
            if (get().pDialog != null && get().pDialog.isShowing()) {
                get().pDialog.dismiss();
            }
            get().pDialog = null;
            get().mContext = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
