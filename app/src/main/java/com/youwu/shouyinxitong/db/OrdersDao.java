package com.youwu.shouyinxitong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.bean_used.OrderNumberBean;
import com.youwu.shouyinxitong.bean_used.RestingInfoBean;
import com.youwu.shouyinxitong.bean_used.TypeResult;
import com.youwu.shouyinxitong.bean.TypeResultDeserializer;
import com.youwu.shouyinxitong.bean.TypeSuper;
import com.youwu.shouyinxitong.bean.VIPBean;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzzie
 * Date: 2018-8-10 17:33:43
 */
public class OrdersDao {
    public static final String TAG = "OrdersDao";
    // 列定义 (id integer primary key,creat_time text,order_number text,ordernumber_bean text,vip_bean text,shop_car_goods text)

    private final String[] ORDER_COLUMNS = new String[]{"id", "creat_time", "ordernumber_bean", "vip_bean", "shop_car_goods", "order_number"};

    private Context context;
    private DBHelper orderDBHelper;

    public OrdersDao(Context context) {
        this.context = context;
        orderDBHelper = new DBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = orderDBHelper.getReadableDatabase();
            // select count(Id) from Goods
            cursor = db.query(orderDBHelper.GOODS_TABLE_NAME, new String[]{"COUNT(id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 添加一个数据到数据库
     * {"id", "creat_time", "vip_bean", "shop_car_goods"}
     */
    public synchronized void addOrder(RestingInfoBean restingInfoBean) {

        SQLiteDatabase db = null;
        try {
            db = orderDBHelper.getWritableDatabase();
            db.beginTransaction();


            ContentValues contentValues = new ContentValues();
            contentValues.put("creat_time", restingInfoBean.getOrderNumberBean().getCreatTime() + "");
            contentValues.put("ordernumber_bean", AppApplication.gson.toJson(restingInfoBean.getOrderNumberBean()));
            contentValues.put("vip_bean", AppApplication.gson.toJson(restingInfoBean.getBean()));
            contentValues.put("shop_car_goods", AppApplication.gson.toJson(restingInfoBean.getShopCarYWGoodBeans()));
            contentValues.put("order_number", restingInfoBean.getOrderNumberBean().getOrderNumberStr() + "");

            db.insert(orderDBHelper.RESTING0RDER_TABLE_NAME, null, contentValues);

//                    db.execSQL("insert into " +  goodsDBHelper.GOODS_TABLE_NAME + " (Id, GoodsName, Repertory, Price,ImgUrl,Type) values (" + gId + "," + gName +","+ gRepertory + "," + gPrice + "," + gImg + "," + gType + ")");


            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 查询Goods数据库中所有数据
     */
    public List<RestingInfoBean> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<RestingInfoBean> restingInfoBeans = new ArrayList();
        try {
            db = orderDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(orderDBHelper.RESTING0RDER_TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    restingInfoBeans.add(parseOrder(cursor));
                }
                return restingInfoBeans;
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllData--exception", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return restingInfoBeans;
    }


    /**
     * 根据sku  查询Goods 详情
     */
//    public YWGoodBean getGoodInfoByGoodSku(String sku) {
//        if (TextUtils.isEmpty(sku)) {
//            return null;
//        }
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "sku = ?", new String[]{sku}, null, null, null);
//            Log.e(TAG, "cursor  " + cursor);
//            if (cursor.getCount() > 0) {
//                Log.e(TAG, "cursor  count  " + cursor.getCount());
//                if (cursor.moveToNext()) {
//                    return parseGoodS(cursor);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 根据category_id 根据商品类别
//     *
//     * @return
//     */
//    public List<YWGoodBean> getGoodListByCategoryId(String category_id) {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
//        try {
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "category_id = ?",
//                    new String[]{category_id}, null, null, null);
//            Log.e(TAG, "cursor  " + cursor);
//            if (cursor.getCount() > 0) {
//                Log.e("daodata", "cursor  count  " + cursor.getCount());
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    if (cursor.moveToNext()) {
//                        ywGoodBeanList.add(parseGoodS(cursor));
//                    }
//                }
//            }
//            return ywGoodBeanList;
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return ywGoodBeanList;
//    }
//
//    public boolean updateIfNotExistById(YWGoodBean goodsBean) {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "id = ?", new String[]{goodsBean.getId()}, null, null, null);
//            Log.e(TAG, "cursor  " + cursor);
//            if (null != cursor && cursor.getCount() > 0) {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
//                contentValues.put("category_id", goodsBean.getCategory_id());
//                contentValues.put("image", goodsBean.getImage());
//                contentValues.put("product_name", goodsBean.getProduct_name());
//                contentValues.put("letters", goodsBean.getLetters());
//                contentValues.put("specs", AppApplication.gson.toJson(goodsBean.getSpecs()));
//                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
//                        "id = ?",
//                        new String[]{goodsBean.getId()});
////                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
//                return true;
//            } else {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
//                contentValues.put("category_id", goodsBean.getCategory_id());
//                contentValues.put("image", goodsBean.getImage());
//                contentValues.put("product_name", goodsBean.getProduct_name());
//                contentValues.put("letters", goodsBean.getLetters());
//                contentValues.put("specs", AppApplication.gson.toJson(goodsBean.getSpecs()));
//                db.insert(goodsDBHelper.GOODS_TABLE_NAME, null, contentValues);//执行插入操作
//                return true;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 根据good  name 模糊查询数据
//     *
//     * @return
//     */
//    public List<YWGoodBean> getGoodListForATYPIDByGoodInfo(String goodInfo) {
//        // 汉字转换成拼音
//        String pinyin = Pinyin.toPinyin(goodInfo, "");
//        pinyin = pinyin.toLowerCase();
//        Log.e("pinyin", "search  name   " + pinyin);
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
//        try {
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
//                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5]},
//                    "bar_code LIKE ? or pinyin_name LIKE ? or letters LIKE ?",
//                    new String[]{"%" + pinyin + "%", "%" + pinyin + "%", "%" + pinyin + "%"}, null, null, null);
////            Log.e(TAG,"cursor  "+cursor);
//            if (cursor.getCount() > 0) {
//                Log.e("daodata", "cursor  count  " + cursor.getCount());
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    if (cursor.moveToNext()) {
//                        ywGoodBeanList.add(parseGoodS(cursor));
//                    }
//                }
//            }
//            return ywGoodBeanList;
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return ywGoodBeanList;
//    }
//
//
//    /**
//     * 根据good  name 模糊查询商品数据
//     *
//     * @return
//     */
//    public List<YWGoodBean> getGoodListByGoodInfo(String goodInfo) {
//        // 汉字转换成拼音
//        String pinyin = Pinyin.toPinyin(goodInfo, "");
//        pinyin = pinyin.toLowerCase();
//        Log.e("pinyin", "search  name   " + pinyin);
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
//        try {
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
//                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5], GOODS_COLUMNS[6],
//                            GOODS_COLUMNS[7], GOODS_COLUMNS[8], GOODS_COLUMNS[9], GOODS_COLUMNS[10], GOODS_COLUMNS[11],
//                            GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15]},
//                    "bar_code LIKE ? or pinyin_name LIKE ? or letters LIKE ?",
//                    new String[]{"%" + pinyin + "%", "%" + pinyin + "%", "%" + pinyin + "%"}, null, null, null);
////            Log.e(TAG,"cursor  "+cursor);
//            if (cursor.getCount() > 0) {
//                Log.e("daodata", "cursor  count  " + cursor.getCount());
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    if (cursor.moveToNext()) {
//                        ywGoodBeanList.add(parseGoodS(cursor));
//                    }
//                }
//            }
//            return ywGoodBeanList;
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return ywGoodBeanList;
//    }
//
//    /**
//     * 根据条形码  查询Goods 详情
//     */
//    public YWGoodBean getGoodInfoByGoodCode(String goodCode) {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = goodsDBHelper.getReadableDatabase();
//            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "bar_code = ?", new String[]{goodCode}, null, null, null);
//            Log.e(TAG, "cursor  " + cursor);
//            if (cursor.getCount() > 0) {
//                Log.e("daodata", "cursor  count  " + cursor.getCount());
//                if (cursor.moveToNext()) {
//                    return parseGoodS(cursor);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return null;
//    }
//
    public boolean deleteOrder(String creatTime) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
            cursor = db.query(orderDBHelper.RESTING0RDER_TABLE_NAME, ORDER_COLUMNS, "creat_time = ?", new String[]{creatTime}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                db.delete(orderDBHelper.RESTING0RDER_TABLE_NAME, "creat_time = ?", new String[]{creatTime});
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * 将查找到的数据转换成Order类
     * {"id", "creat_time", "ordernumber_bean", "vip_bean", "shop_car_goods"}
     */
    private RestingInfoBean parseOrder(Cursor cursor) {
        RestingInfoBean restingInfoBean = new RestingInfoBean();
        restingInfoBean.setOrderNumberBean(AppApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[2])), OrderNumberBean.class));

        restingInfoBean.setBean(AppApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[3])), VIPBean.class));
//List list = new ArrayList();
//        Object o = AppApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[4])), new TypeToken<List<Object>>() {
//        }.getType());

        GsonBuilder gsonb = new GsonBuilder();
        gsonb.registerTypeAdapter(TypeResult.class, new TypeResultDeserializer());
        gsonb.serializeNulls();
        Gson gson = gsonb.create();

        List<TypeSuper> item = gson.fromJson(cursor.getString(cursor.getColumnIndex(ORDER_COLUMNS[4])), TypeResult.class).data;




        restingInfoBean.setShopCarYWGoodBeans(item);

        return restingInfoBean;
    }

    public void deleteAllData() {
        SQLiteDatabase db = null;

        db = orderDBHelper.getReadableDatabase();
        try {
            db.execSQL("delete from " + orderDBHelper.RESTING0RDER_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


}
