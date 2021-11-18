package com.youwu.shouyinxitong.bean_used;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class OrderNumberBean implements Parcelable, Serializable {


    private long creatTime = System.currentTimeMillis();
    private int orderNumber = 1;

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public int getOrderNumber() {

//        String newString = String.format("%0"+formatLength+"d", sourceDate);

        return orderNumber;
    }

    public String getOrderNumberStr() {

//        String newString = String.format("%0"+formatLength+"d", sourceDate);

        return String.format("%0" + 5 + "d", orderNumber);
    }

    public String getOrderNumberStrV() {

//        String newString = String.format("%0"+formatLength+"d", sourceDate);

        return "B"+String.format("%0" + 4 + "d", orderNumber);
    }

    public String getCreatTimeStr() {


        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        return sf.format(creatTime);
    }

    public OrderNumberBean(long creatTime, int orderNumber) {
        this.creatTime = creatTime;
        this.orderNumber = orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


    @Override
    public String toString() {
        return "OrderNumberBean{" +
                "creatTime=" + creatTime +
                ", orderNumber=" + orderNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.creatTime);
        dest.writeInt(this.orderNumber);
    }

    public OrderNumberBean() {
    }

    protected OrderNumberBean(Parcel in) {
        this.creatTime = in.readLong();
        this.orderNumber = in.readInt();
    }

    public static final Creator<OrderNumberBean> CREATOR = new Creator<OrderNumberBean>() {
        @Override
        public OrderNumberBean createFromParcel(Parcel source) {
            return new OrderNumberBean(source);
        }

        @Override
        public OrderNumberBean[] newArray(int size) {
            return new OrderNumberBean[size];
        }
    };
}
