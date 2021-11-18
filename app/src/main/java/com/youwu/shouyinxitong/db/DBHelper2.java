package com.youwu.shouyinxitong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
*@author LzCode@qq.com
*@date on 2018-8-11 08:23:30
*@describe
**/

public class DBHelper2 extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "youwuu_db";
    public static final String GOODS_TABLE_NAME = "goods";
    public DBHelper2(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String goods_sql =  "create table if not exists " + GOODS_TABLE_NAME +" (id integer primary key,p_name text,bar_code text,p_price text,vip_price text,p_weight text,p_rep text,good_id text,sku text,stantard_is text,good_img text,reset_price_time text,reset_price text,show text,letters text,pinyin_name text)";
        sqLiteDatabase.execSQL(goods_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String goods_sql = "DROP TABLE IF EXISTS " + GOODS_TABLE_NAME;
        sqLiteDatabase.execSQL(goods_sql);
        onCreate(sqLiteDatabase);
    }
}
