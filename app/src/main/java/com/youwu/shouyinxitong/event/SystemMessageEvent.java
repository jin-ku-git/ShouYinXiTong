package com.youwu.shouyinxitong.event;

public class SystemMessageEvent {
    public int message;
    public String  phone;

    public SystemMessageEvent(int message,String phone){
        this.message = message;
        this.phone = phone;
    }
}
