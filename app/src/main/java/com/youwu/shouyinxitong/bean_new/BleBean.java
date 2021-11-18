package com.youwu.shouyinxitong.bean_new;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

import java.io.Serializable;

public class BleBean implements Serializable {

    private BleDevice bleDevice;
    private BluetoothGatt gatt;

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }
}
