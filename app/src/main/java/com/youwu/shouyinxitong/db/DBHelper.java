package com.youwu.shouyinxitong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author LzCode@qq.com
 * @date on 2018-8-11 08:23:30
 * @describe
 **/

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 13;
    private static final String DB_NAME = "youwuu_db";
    public static final String GOODS_TABLE_NAME = "goods";
    public static final String ALLGOODS_TABLE_NAME = "all_goods";
    public static final String RESTING0RDER_TABLE_NAME = "RESTINGORDER";
    public static final String MEAL_TABLE_NAME = "MEALTABLE";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // {"id", "category_id", "image", "product_name", "letters", "specs"};
        String goods_sql = "create table if not exists " + GOODS_TABLE_NAME + " (id integer primary key,category_id text,image text,product_name text,letters text,sku text" +
                ",price text,sales_status text," +
                "inventory,discount,unit,cost_price,ratio_base," +
                "ratio_goods_id,ratio_unit,category_name,store_goods_id,group_value,goods_type,meals_sku,meals_num)";
        sqLiteDatabase.execSQL(goods_sql);
        String all_goods_sql = "create table if not exists " + ALLGOODS_TABLE_NAME + " (id integer primary key,category_id text,image text,product_name text,letters text,sku text" +
                ",price text,sales_status text," +
                "inventory,discount,unit,cost_price,ratio_base," +
                "ratio_goods_id,ratio_unit,category_name,store_goods_id,group_value,goods_type)";
        sqLiteDatabase.execSQL(all_goods_sql);
        String order_sql = "create table if not exists " + RESTING0RDER_TABLE_NAME + " (id integer primary key,creat_time text,ordernumber_bean text,vip_bean text,shop_car_goods text,order_number text)";
        sqLiteDatabase.execSQL(order_sql);
        String meal_sql = "create table if not exists " + MEAL_TABLE_NAME + " (id integer primary key,goods_type_id text,meal_id text,meal_goods_name text,meal_goods_sku text," +
                "meal_goods_price text,img text,sales_status text,goods_id text,goods_type text,weightnum text)";
        sqLiteDatabase.execSQL(meal_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String goods_sql = "DROP TABLE IF EXISTS " + GOODS_TABLE_NAME;
        sqLiteDatabase.execSQL(goods_sql);
        onCreate(sqLiteDatabase);

        String all_goods_sql = "DROP TABLE IF EXISTS " + ALLGOODS_TABLE_NAME;
        sqLiteDatabase.execSQL(all_goods_sql);
        onCreate(sqLiteDatabase);

        String order_sql = "DROP TABLE IF EXISTS " + RESTING0RDER_TABLE_NAME;
        sqLiteDatabase.execSQL(order_sql);
        onCreate(sqLiteDatabase);

        String meal_sql = "DROP TABLE IF EXISTS " + MEAL_TABLE_NAME;
        sqLiteDatabase.execSQL(meal_sql);
        onCreate(sqLiteDatabase);
    }
}
