package com.youwu.shouyinxitong.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.youwu.shouyinxitong.R;


/**
 * Created by yjm on 16/11/25.
 * 作为显示默认popwindow的容器
 */
public class PopDialog extends Dialog implements View.OnClickListener {
    public PopDialog(Context context) {
        super(context);
    }

    public PopDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PopDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private boolean isFous = true;

        public boolean isFous() {
            return isFous;
        }

        public void setIsFous(boolean isFous) {
            this.isFous = isFous;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public PopDialog create(View layout) {
            PopDialog dialog = new PopDialog(context, R.style.Dialog);
            dialog.addContentView(layout, new LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(isFous());
            return dialog;
        }
    }


    @Override
    public void onClick(View view) {

    }


}
