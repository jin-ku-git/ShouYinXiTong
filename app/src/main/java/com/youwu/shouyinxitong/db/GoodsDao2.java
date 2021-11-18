package com.youwu.shouyinxitong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.youwu.shouyinxitong.bean.YWGoodBean2;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzzie
 * Date: 2018-8-10 17:33:43
 */
public class GoodsDao2 {
    public static final String TAG = "GoodDao";
    // 列定义  id integer primary key,p_name text,bar_code text,p_price text,vip_price text,p_weight integer
//    private final String[] GOODS_COLUMNS = new String[]{"id", "p_name", "bar_code", "p_price", "vip_price", "p_weight", "p_rep", "good_id", "sku", "stantard_is", "good_img", "reset_price_time", "reset_price", "show", "letters", "pinyin_name", "isNotSelect"};
    private final String[] GOODS_COLUMNS = new String[]{"id", "p_name", "bar_code", "p_price", "vip_price", "p_weight", "p_rep", "good_id", "sku", "stantard_is", "good_img", "reset_price_time", "reset_price", "show", "letters", "pinyin_name"};

    private Context context;
    private DBHelper2 goodsDBHelper;

    public GoodsDao2(Context context) {
        this.context = context;
        goodsDBHelper = new DBHelper2(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = goodsDBHelper.getReadableDatabase();
            // select count(Id) from Goods
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, new String[]{"COUNT(id)"}, null, null, null, null, null);

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
     * 初始化数据
     */
    public synchronized void initTable(List<YWGoodBean2> goodsBeans) {
        if (null == goodsBeans || goodsBeans.isEmpty()) {
            return;
        }

        SQLiteDatabase db = null;
        try {
            db = goodsDBHelper.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < goodsBeans.size(); i++) {
                YWGoodBean2 goodsBean = goodsBeans.get(i);
                if (null != goodsBean) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", Integer.parseInt(goodsBean.getProduct_id()));
                //    contentValues.put("isNotSelect", goodsBean.isNotSelect());
                    contentValues.put("p_name", goodsBean.getProduct_name());
                    contentValues.put("bar_code", goodsBean.getBar_code());
                    contentValues.put("p_price", goodsBean.getPrice());
                    contentValues.put("vip_price", goodsBean.getVip_price());
                    contentValues.put("p_weight", goodsBean.getWeight());
                    contentValues.put("p_rep", goodsBean.getRepertory());
                    contentValues.put("good_id", goodsBean.getGood_id());
                    contentValues.put("sku", goodsBean.getSku());
                    contentValues.put("stantard_is", goodsBean.getIs_standard());
                    contentValues.put("good_img", goodsBean.getImage());
                    contentValues.put("reset_price_time", goodsBean.getReset_price_time());
                    contentValues.put("reset_price", goodsBean.getReset_price());
                    contentValues.put("show", goodsBean.getShow());
                    contentValues.put("letters", goodsBean.getLetters());
                    contentValues.put("pinyin_name", goodsBean.getPinyin_name());
                    db.insert(goodsDBHelper.GOODS_TABLE_NAME, null, contentValues);

//                    db.execSQL("insert into " +  goodsDBHelper.GOODS_TABLE_NAME + " (Id, GoodsName, Repertory, Price,ImgUrl,Type) values (" + gId + "," + gName +","+ gRepertory + "," + gPrice + "," + gImg + "," + gType + ")");
                }
            }

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
    public List<YWGoodBean2> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<YWGoodBean2> goodList = new ArrayList<YWGoodBean2>(cursor.getCount());
                while (cursor.moveToNext()) {
                    goodList.add(parseGoodS(cursor));
                }
                return goodList;
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

        return null;
    }

    /**
     * 根据good  模糊查询商品数据 非标品集合
     *
     * @return
     */
    public List<YWGoodBean2> getGoodListForATYPIDGood() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean2> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "stantard_is = ?",
                    new String[]{"" + YWGoodBean2.ATYPID_GOOD}, null, null, null);

            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToNext()) {
                        ywGoodBeanList.add(parseGoodS(cursor));
                    }
                }
            }
            return ywGoodBeanList;
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

        return ywGoodBeanList;
    }

    /**
     * 根据good  模糊查询商品数据 非标品并且设置显示的集合
     *
     * @return
     */
    public List<YWGoodBean2> getGoodListForATYPIDAndMastShowGood() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean2> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "stantard_is = ? and show= ?",
                    new String[]{"" + YWGoodBean2.ATYPID_GOOD, "1"}, null, null, null);

            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToNext()) {
                        ywGoodBeanList.add(parseGoodS(cursor));
                    }
                }
            }
            return ywGoodBeanList;
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

        return ywGoodBeanList;
    }

    /**
     * 根据sku  查询Goods 详情
     */
    public YWGoodBean2 getGoodInfoByGoodSku(String sku) {
        if (TextUtils.isEmpty(sku)) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "sku = ?", new String[]{sku}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e(TAG, "cursor  count  " + cursor.getCount());
                if (cursor.moveToNext()) {
                    return parseGoodS(cursor);
                }
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

        return null;
    }

    /**
     * 根据good  sku 模糊查询商品数据
     *
     * @return
     */
    public List<YWGoodBean2> getGoodListByGoodCode(String goodCode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean2> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5], GOODS_COLUMNS[6],
                            GOODS_COLUMNS[7], GOODS_COLUMNS[8], GOODS_COLUMNS[9], GOODS_COLUMNS[10], GOODS_COLUMNS[11],
                            GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15]}, "bar_code" + " LIKE ? ",
                           // GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15], GOODS_COLUMNS[16]}, "bar_code" + " LIKE ? ",
                    new String[]{"%" + goodCode + "%"}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (cursor.moveToNext()) {
                        ywGoodBeanList.add(parseGoodS(cursor));
                    }
                }
            }
            return ywGoodBeanList;
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

        return ywGoodBeanList;
    }

    public boolean updateIfNotExistBySKU(YWGoodBean2 goodBean) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            if (TextUtils.isEmpty(goodBean.getSku())) {
                return false;
            }
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "sku = ?", new String[]{goodBean.getSku()}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
              //  contentValues.put("isNotSelect", goodBean.isNotSelect());
                contentValues.put("p_name", goodBean.getProduct_name());
                contentValues.put("bar_code", goodBean.getBar_code());
                contentValues.put("p_price", goodBean.getPrice());
                contentValues.put("vip_price", goodBean.getVip_price());
                contentValues.put("p_weight", goodBean.getWeight());
                contentValues.put("p_rep", goodBean.getRepertory());
                contentValues.put("sku", goodBean.getSku());
                contentValues.put("stantard_is", goodBean.getIs_standard());
                contentValues.put("good_img", goodBean.getImage());

                contentValues.put("reset_price_time", goodBean.getReset_price_time());
                contentValues.put("reset_price", goodBean.getReset_price());
                contentValues.put("show", goodBean.getShow());
                contentValues.put("letters", goodBean.getLetters());
                contentValues.put("pinyin_name", goodBean.getPinyin_name());
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "sku = ?",
                        new String[]{goodBean.getSku()});
//                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
                return true;
            } else {
                ContentValues contentValues = new ContentValues();
            //    contentValues.put("isNotSelect", goodBean.isNotSelect());
                contentValues.put("p_name", goodBean.getProduct_name());
                contentValues.put("bar_code", goodBean.getBar_code());
                contentValues.put("p_price", goodBean.getPrice());
                contentValues.put("vip_price", goodBean.getVip_price());
                contentValues.put("p_weight", goodBean.getWeight());
                contentValues.put("p_rep", goodBean.getRepertory());
                contentValues.put("good_id", goodBean.getGood_id());
                contentValues.put("sku", goodBean.getSku());
                contentValues.put("stantard_is", goodBean.getIs_standard());
                contentValues.put("good_img", goodBean.getImage());
                contentValues.put("reset_price_time", goodBean.getReset_price_time());
                contentValues.put("reset_price", goodBean.getReset_price());
                contentValues.put("show", goodBean.getShow());
                contentValues.put("letters", goodBean.getLetters());
                contentValues.put("pinyin_name", goodBean.getPinyin_name());
                db.insert(goodsDBHelper.GOODS_TABLE_NAME, null, contentValues);//执行插入操作
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
     * 根据good  name 模糊查询非标品数据
     *
     * @return
     */
    public List<YWGoodBean2> getGoodListForATYPIDByGoodInfo(String goodInfo) {
        // 汉字转换成拼音
        String pinyin = Pinyin.toPinyin(goodInfo, "");
        pinyin = pinyin.toLowerCase();
        Log.e("pinyin", "search  name   " + pinyin);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean2> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5], GOODS_COLUMNS[6],
                            GOODS_COLUMNS[7], GOODS_COLUMNS[8], GOODS_COLUMNS[9], GOODS_COLUMNS[10], GOODS_COLUMNS[11],
                            GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15]},
//                            GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15], GOODS_COLUMNS[16]},
                    "stantard_is = ? and (bar_code LIKE ? or pinyin_name LIKE ? or letters LIKE ?)",
                    new String[]{"" + YWGoodBean2.ATYPID_GOOD, "%" + pinyin + "%", "%" + pinyin + "%", "%" + pinyin + "%"}, null, null, null);
//            Log.e(TAG,"cursor  "+cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToNext()) {
                        ywGoodBeanList.add(parseGoodS(cursor));
                    }
                }
            }
            return ywGoodBeanList;
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

        return ywGoodBeanList;
    }


    /**
     * 根据good  name 模糊查询商品数据
     *
     * @return
     */
    public List<YWGoodBean2> getGoodListByGoodInfo(String goodInfo) {
        // 汉字转换成拼音
        String pinyin = Pinyin.toPinyin(goodInfo, "");
        pinyin = pinyin.toLowerCase();
        Log.e("pinyin", "search  name   " + pinyin);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean2> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5], GOODS_COLUMNS[6],
                            GOODS_COLUMNS[7], GOODS_COLUMNS[8], GOODS_COLUMNS[9], GOODS_COLUMNS[10], GOODS_COLUMNS[11],
                            GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15]},
                          //  GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15], GOODS_COLUMNS[16]},
                    "bar_code LIKE ? or pinyin_name LIKE ? or letters LIKE ?",
                    new String[]{"%" + pinyin + "%", "%" + pinyin + "%", "%" + pinyin + "%"}, null, null, null);
//            Log.e(TAG,"cursor  "+cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToNext()) {
                        ywGoodBeanList.add(parseGoodS(cursor));
                    }
                }
            }
            return ywGoodBeanList;
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

        return ywGoodBeanList;
    }

    /**
     * 根据条形码  查询Goods 详情
     */
    public YWGoodBean2 getGoodInfoByGoodCode(String goodCode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "bar_code = ?", new String[]{goodCode}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                if (cursor.moveToNext()) {
                    return parseGoodS(cursor);
                }
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

        return null;
    }

    public boolean deleteIfExistByGoodId(String goodId) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "good_id = ?", new String[]{goodId}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                db.delete(goodsDBHelper.GOODS_TABLE_NAME, "good_id = ?", new String[]{goodId});
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

    public boolean updateIfNotExistByGoodId(YWGoodBean2 goodBean) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            if (TextUtils.isEmpty(goodBean.getGood_id())) {
                return false;
            }
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "good_id = ?", new String[]{goodBean.getGood_id()}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
           //     contentValues.put("isNotSelect", goodBean.isNotSelect());
                contentValues.put("p_name", goodBean.getProduct_name());
                contentValues.put("bar_code", goodBean.getBar_code());
                contentValues.put("p_price", goodBean.getPrice());
                contentValues.put("vip_price", goodBean.getVip_price());
                contentValues.put("p_weight", goodBean.getWeight());
                contentValues.put("p_rep", goodBean.getRepertory());
                contentValues.put("sku", goodBean.getSku());
                contentValues.put("stantard_is", goodBean.getIs_standard());
                contentValues.put("good_img", goodBean.getImage());
                contentValues.put("reset_price_time", goodBean.getReset_price_time());
                contentValues.put("reset_price", goodBean.getReset_price());
                contentValues.put("show", goodBean.getShow());
                contentValues.put("letters", goodBean.getLetters());
                contentValues.put("pinyin_name", goodBean.getPinyin_name());
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "good_id = ?",
                        new String[]{goodBean.getGood_id()});
//                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
                return true;
            } else {
                ContentValues contentValues = new ContentValues();
            //    contentValues.put("isNotSelect", goodBean.isNotSelect());
                contentValues.put("p_name", goodBean.getProduct_name());
                contentValues.put("bar_code", goodBean.getBar_code());
                contentValues.put("p_price", goodBean.getPrice());
                contentValues.put("vip_price", goodBean.getVip_price());
                contentValues.put("p_weight", goodBean.getWeight());
                contentValues.put("p_rep", goodBean.getRepertory());
                contentValues.put("good_id", goodBean.getGood_id());
                contentValues.put("sku", goodBean.getSku());
                contentValues.put("stantard_is", goodBean.getIs_standard());
                contentValues.put("good_img", goodBean.getImage());
                contentValues.put("reset_price_time", goodBean.getReset_price_time());
                contentValues.put("reset_price", goodBean.getReset_price());
                contentValues.put("show", goodBean.getShow());
                contentValues.put("letters", goodBean.getLetters());
                contentValues.put("pinyin_name", goodBean.getPinyin_name());
                db.insert(goodsDBHelper.GOODS_TABLE_NAME, null, contentValues);//执行插入操作
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

    public void updateExistGoodNumByBarCode(String goodCode, int optNum) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "bar_code = ?", new String[]{goodCode}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
                if (cursor.moveToNext()) {
                    YWGoodBean2 goodBean = parseGoodS(cursor);

                    String goodRepertory = goodBean.getRepertory();
                    if (TextUtils.isEmpty(goodRepertory)) {
                        return;
                    }

                    double goodRep = Double.parseDouble(goodRepertory) - optNum;

                    ContentValues contentValues = new ContentValues();
               //     contentValues.put("isNotSelect", goodBean.isNotSelect());
                    contentValues.put("p_name", goodBean.getProduct_name());
                    contentValues.put("bar_code", goodBean.getBar_code());
                    contentValues.put("p_price", goodBean.getPrice());
                    contentValues.put("vip_price", goodBean.getVip_price());
                    contentValues.put("p_weight", goodBean.getWeight());
                    contentValues.put("p_rep", "" + goodRep);
                    contentValues.put("sku", goodBean.getSku());
                    contentValues.put("stantard_is", goodBean.getIs_standard());
                    contentValues.put("good_img", goodBean.getImage());
                    contentValues.put("reset_price_time", goodBean.getReset_price_time());
                    contentValues.put("reset_price", goodBean.getReset_price());
                    contentValues.put("show", goodBean.getShow());
                    contentValues.put("letters", goodBean.getLetters());
                    contentValues.put("pinyin_name", goodBean.getPinyin_name());
                    db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                            "good_id = ?",
                            new String[]{goodBean.getGood_id()});
                    Log.e("daodata", "cursor  update  rep  true");
                }
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

        return;
    }


    /**
     * 将查找到的数据转换成Order类
     * {"id", "p_name","bar_code","p_price","vip_price","p_weight","p_rep"};
     */
    private YWGoodBean2 parseGoodS(Cursor cursor) {
        YWGoodBean2 goods = new YWGoodBean2();
        goods.setProduct_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[0]))));
        goods.setProduct_name(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[1])));
        goods.setBar_code(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[2])));
        goods.setPrice(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[3])));
        goods.setVip_price(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[4])));
        goods.setWeight(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[5])));
        goods.setRepertory(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[6])));
        goods.setGood_id(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[7])));
        goods.setSku(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[8])));
        goods.setIs_standard(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[9])));
        goods.setImage(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[10])));
        goods.setReset_price_time(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[11])));
        goods.setReset_price(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[12])));
        goods.setShow(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[13])));

        goods.setLetters(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[14])));
        goods.setPinyin_name(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[15])));
      //  goods.setNotSelect(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[16])));
        return goods;
    }

    public void deleteAllData() {
        SQLiteDatabase db = null;

        db = goodsDBHelper.getReadableDatabase();
        try {
            db.execSQL("delete from " + goodsDBHelper.GOODS_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
