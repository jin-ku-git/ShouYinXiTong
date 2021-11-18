package com.youwu.shouyinxitong.utils_tool;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class SunConnection implements ServiceConnection {

    private IWoyouService iWoyouService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iWoyouService = IWoyouService.Stub.asInterface(service);
        try {
            iWoyouService.openDrawer(new ICallback() {
                @Override
                public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {

                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
}