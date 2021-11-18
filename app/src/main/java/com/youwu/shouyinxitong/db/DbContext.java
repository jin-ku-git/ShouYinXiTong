package com.youwu.shouyinxitong.db;

import android.content.Context;

/**
*@author LzCode@qq.com
*@date on 2018-8-11 08:37:32
*@describe  全局可用的context对象  单例模式:http://www.jianshu.com/p/8b59089a12f6
**/

public class DbContext {
    private static DbContext instance;

    private Context applicationContext;

    public static DbContext getInstance() {
        if (instance == null){
            throw new RuntimeException(DbContext.class.getSimpleName() + "has not been initialized!");
        }

        return instance;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public DbContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 全局信息 只能调用一次
     */
    public static void init(Context applicationContext) {
        if (instance != null) {
            throw new RuntimeException(DbContext.class.getSimpleName() + " can not be initialized multiple times!");
        }
        instance = new DbContext(applicationContext);
    }

    public static boolean isInitialized() {
        return (instance != null);
    }
}
