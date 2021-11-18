package com.youwu.shouyinxitong.app;

import com.google.gson.Gson;
import com.youwu.shouyinxitong.bean_used.StoreBean;

import me.goldze.mvvmhabit.utils.SPUtils;


/**
 * 用户数据处理
 */
public class StoreInfo {

    public static Gson gson;
    public static SPUtils spUtils;
    public static String userString = "STORE-BEAN";


    private static void createSth() {
        if (gson == null) {
            gson = new Gson();
        }
        if (spUtils == null) {
            spUtils = new SPUtils("STOREEntity");
        }
    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    public static void setStore(StoreBean user) {
        if (user != null) {
            createSth();
            String userJson = gson.toJson(user);
            spUtils.put(userString, userJson);
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static StoreBean getStore() {
        createSth();
        String userStr = spUtils.getString(userString);
//        LogTool.i("用户数据：" + userStr);
        StoreBean user = gson.fromJson(userStr, StoreBean.class);
        return user;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static int getUserId() {
        StoreBean user = getStore();
        if (user != null) {
            return Integer.valueOf(user.getStore_id());
        }
        return 0;
    }

    /**
     * 删除用户信息
     */
    public static void delectStoreBean() {
        createSth();
        spUtils.clear();
    }


    /**
     * 判断是否记住密码已达到自动登录
     */

    public static int getAutoLogin() {
       /* UserBean user = getUser();
        if (user != null) {
            return Integer.valueOf(user.getIsAutoLogin());
        }*/
        return 0;
    }


}
