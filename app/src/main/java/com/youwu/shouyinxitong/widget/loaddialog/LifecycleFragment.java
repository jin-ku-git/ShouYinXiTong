package com.youwu.shouyinxitong.widget.loaddialog;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;


public class LifecycleFragment extends Fragment {
    private LifecycleListener mLifecycleListener = null;

    /**
     * activity onDestory监听回调
     */
    public interface LifecycleListener {
        void onDestroy();
    }

    public LifecycleFragment() {
    }

    @SuppressLint("ValidFragment")
    public LifecycleFragment(LifecycleListener mLifecycleListener) {
        this.mLifecycleListener = mLifecycleListener;
    }

    @Override
    public void onDestroyView() {
        if (mLifecycleListener != null) {
            mLifecycleListener.onDestroy();
        }
        super.onDestroyView();
    }
}