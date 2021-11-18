package com.youwu.shouyinxitong.bean_new;

public class LoginRequsetBean {
    private String passWord;
    private String userName;
    private String os_uuid;


    public String getOs_uuid() {
        return os_uuid;
    }

    public void setOs_uuid(String os_uuid) {
        this.os_uuid = os_uuid;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
