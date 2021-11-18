package com.youwu.shouyinxitong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzzie
 * Date: 2018-8-10 17:33:43
 */
public class GoodsDao {
    public static final String TAG = "GoodDao";
    // 列定义  id integer primary key,p_name text,bar_code text,p_price text,vip_price text,p_weight integer

    //后台给的原始数据
//    private String image;
//    private String category_id;
//    private String id;
//    private String product_name;
//    private String letters;
//    private List<YWGoodBean.SpecsBean> specs;


    private final String[] GOODS_COLUMNS = new String[]{"id", "category_id", "image",
            "product_name", "letters", "sku", "price", "sales_status", "inventory"
            , "discount", "unit", "cost_price", "ratio_base", "ratio_goods_id", "ratio_unit", "category_name", "store_goods_id", "group_value","goods_type", "meals_sku", "meals_num"};

    private final String[] MEAL_COLUMNS = new String[]{
            "id", "goods_type_id", "meal_id", "meal_goods_name", "meal_goods_sku",
            "meal_goods_price", "img", "sales_status", "goods_id"};

    private Context context;
    private DBHelper goodsDBHelper;

    public GoodsDao(Context context) {
        this.context = context;
        goodsDBHelper = new DBHelper(context);
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

    int key = 0;

    public synchronized void initMealTable(List<MealsItemBean> meals) {
        if (null == meals || meals.isEmpty()) {
            return;
        }

        SQLiteDatabase db = null;


        try {
            db = goodsDBHelper.getWritableDatabase();
            //首先清空一下表
            db.execSQL("delete from " + goodsDBHelper.MEAL_TABLE_NAME);
            db.beginTransaction();

            for (int i = 0; i < meals.size(); i++) {
                key = i;
                MealsItemBean mealsItemBean = meals.get(i);
                if (null != mealsItemBean) {

                    List<MealGoodsBean> goodList = mealsItemBean.getMeal_store_goods_detail();

//                    upGoodsMeals(mealsItemBean.getMeal_goods_sku(), goodList);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", mealsItemBean.getId());
                    contentValues.put("goods_type_id", mealsItemBean.getGoods_type_id());
                    contentValues.put("meal_id", mealsItemBean.getId());
                    contentValues.put("meal_goods_name", mealsItemBean.getMeal_goods_name());
                    contentValues.put("meal_goods_sku", mealsItemBean.getMeal_goods_sku());
                    contentValues.put("meal_goods_price", mealsItemBean.getMeal_goods_price());
                    contentValues.put("img", mealsItemBean.getImg());
                    contentValues.put("sales_status", mealsItemBean.getSales_status());
                    contentValues.put("goods_id", new Gson().toJson(goodList));
                    db.insert(goodsDBHelper.MEAL_TABLE_NAME, null, contentValues);


                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {

            Log.e(TAG, "---", e);
        } finally {


            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    /**
     * 初始化数据
     */
    public synchronized void initTable(List<YWGoodBean> goodsBeans) {
        if (null == goodsBeans || goodsBeans.isEmpty()) {
            return;
        }

        SQLiteDatabase db = null;


        try {
            db = goodsDBHelper.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < goodsBeans.size(); i++) {
                key = i;
                YWGoodBean goodsBean = goodsBeans.get(i);
                if (null != goodsBean) {


                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("id", Integer.parseInt(goodsBean.getId()));
//                    contentValues.put("category_id", goodsBean.getCategory_id());
//                    contentValues.put("image", goodsBean.getImage());
//                    contentValues.put("product_name", goodsBean.getProduct_name());
//                    contentValues.put("letters", goodsBean.getLetters());
//                    contentValues.put("specs", MyApplication.gson.toJson(goodsBean.getSpecs()) + "");
                    contentValues.put("id", Integer.parseInt(AppApplication.subZeroAndDot(goodsBean.getId())));
                    contentValues.put("category_id", goodsBean.getCategory_id());
                    contentValues.put("image", goodsBean.getImage());
                    contentValues.put("letters", goodsBean.getLetters());
                    contentValues.put("product_name", goodsBean.getProduct_name());
                    contentValues.put("sku", goodsBean.getSku());
                    contentValues.put("price", goodsBean.getPrice());
                    contentValues.put("sales_status", goodsBean.getSales_status());
                    contentValues.put("inventory", goodsBean.getInventory());
                    contentValues.put("discount", goodsBean.getDiscount());
                    //以下是进货的新增
                    contentValues.put("unit", goodsBean.getUnit());
                    contentValues.put("cost_price", goodsBean.getCost_price());
                    contentValues.put("ratio_base", goodsBean.getRatio_base());
                    contentValues.put("ratio_goods_id", goodsBean.getRatio_goods_id());
                    contentValues.put("ratio_unit", goodsBean.getRatio_unit());
                    contentValues.put("category_name", goodsBean.getCategory_name());
                    contentValues.put("store_goods_id", goodsBean.getStore_goods_id());
                    contentValues.put("group_value", goodsBean.getGroup_value());
                    contentValues.put("goods_type", goodsBean.getGoods_type());
                    db.insert(goodsDBHelper.GOODS_TABLE_NAME, null, contentValues);

//                    db.execSQL("insert into " +  goodsDBHelper.GOODS_TABLE_NAME + " (Id, GoodsName, Repertory, Price,ImgUrl,Type) values (" + gId + "," + gName +","+ gRepertory + "," + gPrice + "," + gImg + "," + gType + ")");
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {

            Log.e(TAG, "---", e);
        } finally {


            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 获取商品表的大小
     */

    public int getAllGoodsNum() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, null, null, null, null, null);
            return cursor.getCount();
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return 0;
    }

    /**
     * 查询Goods数据库中所有数据
     */
    public List<YWGoodBean> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<YWGoodBean> goodList = new ArrayList<YWGoodBean>(cursor.getCount());
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
     * 根据sku  查询Goods 详情
     */
    public YWGoodBean getGoodInfoByGoodSku(String sku) {
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
                    Log.d("搜索到的商品信息", parseGoodS(cursor).getProduct_name());
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

    public MealsItemBean getMealInfoBymealSku(String sku) {
        if (TextUtils.isEmpty(sku)) {
            return null;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.MEAL_TABLE_NAME, MEAL_COLUMNS, "meal_goods_sku = ?", new String[]{sku}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e(TAG, "cursor  count  " + cursor.getCount());
                if (cursor.moveToNext()) {
                    return paseMealItem(cursor);
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

    //根据套餐分类id 查询套餐表
    public List<MealsItemBean> getMealListByCategoryId(String category_id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<MealsItemBean> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.MEAL_TABLE_NAME, MEAL_COLUMNS, "goods_type_id = ?",
                    new String[]{category_id}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());

                while (cursor != null && cursor.moveToNext()) {
                    MealsItemBean mealsItemBean = paseMealItem(cursor);

                    ywGoodBeanList.add(mealsItemBean);
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
     * 根据category_id 根据商品类别
     *
     * @return
     */
    public List<YWGoodBean> getGoodListByCategoryId(String category_id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "category_id = ?",
                    new String[]{category_id}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (cursor.getCount() > 0) {
                Log.e("daodata", "cursor  count  " + cursor.getCount());
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    if (cursor.moveToNext()) {
//                        Log.e("11111", i + "--moveToNext:" + JsonHelper.toJSON(parseGoodS(cursor)));
//                        ywGoodBeanList.add(parseGoodS(cursor));
//                    }
//                }

                while (cursor != null && cursor.moveToNext()) {
                    ywGoodBeanList.add(parseGoodS(cursor));
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
     * 修改商品对应的套餐sku
     *
     * @param sku
     * @return
     */
    public boolean upGoodsMeals(String sku, List<YWGoodBean> goodList) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "meals_sku = ?", new String[]{sku}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);

            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("meals_sku", "");
                contentValues.put("meals_num", 0);
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "meals_sku = ?",
                        new String[]{sku});
//                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
            }
            for (int i = 0; i < goodList.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("meals_sku", sku);
                contentValues.put("meals_num", goodList.get(i).getGoods_number());
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "id = ?",
                        new String[]{goodList.get(i).goods_id});
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    public boolean upGoodsInventory(YWGoodBean goodsBean) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "id = ?", new String[]{goodsBean.getId()}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("inventory", goodsBean.getInventory());
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "id = ?",
                        new String[]{goodsBean.getId()});
//                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
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

    public boolean updateIfNotExistById(YWGoodBean goodsBean) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "id = ?", new String[]{goodsBean.getId()}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
//                contentValues.put("category_id", goodsBean.getCategory_id());
//                contentValues.put("image", goodsBean.getImage());
//                contentValues.put("product_name", goodsBean.getProduct_name());
//                contentValues.put("letters", goodsBean.getLetters());
//                contentValues.put("specs", MyApplication.gson.toJson(goodsBean.getSpecs()));
                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
                contentValues.put("category_id", goodsBean.getCategory_id());
                contentValues.put("image", goodsBean.getImage());
                contentValues.put("product_name", goodsBean.getProduct_name());
                contentValues.put("letters", goodsBean.getLetters());
                contentValues.put("sku", goodsBean.getSku());
                contentValues.put("price", goodsBean.getPrice());
                contentValues.put("sales_status", goodsBean.getSales_status());
                contentValues.put("inventory", goodsBean.getInventory());
                contentValues.put("discount", goodsBean.getDiscount());
                contentValues.put("unit", goodsBean.getUnit());
                contentValues.put("cost_price", goodsBean.getCost_price());
                contentValues.put("ratio_base", goodsBean.getRatio_base());
                contentValues.put("ratio_goods_id", goodsBean.getRatio_goods_id());
                contentValues.put("ratio_unit", goodsBean.getRatio_unit());
                contentValues.put("category_name", goodsBean.getCategory_name());
                contentValues.put("store_goods_id", goodsBean.getStore_goods_id());
                contentValues.put("group_value", goodsBean.getGroup_value());
                contentValues.put("goods_type", goodsBean.getGoods_type());
                db.update(goodsDBHelper.GOODS_TABLE_NAME, contentValues,
                        "id = ?",
                        new String[]{goodsBean.getId()});
//                db.delete(goodsDBHelper.GOODS_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
                return true;
            } else {
                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
//                contentValues.put("category_id", goodsBean.getCategory_id());
//                contentValues.put("image", goodsBean.getImage());
//                contentValues.put("product_name", goodsBean.getProduct_name());
//                contentValues.put("letters", goodsBean.getLetters());
//                contentValues.put("specs", MyApplication.gson.toJson(goodsBean.getSpecs()));
                contentValues.put("id", Integer.parseInt(goodsBean.getId()));
                contentValues.put("category_id", goodsBean.getCategory_id());
                contentValues.put("image", goodsBean.getImage());
                contentValues.put("product_name", goodsBean.getProduct_name());
                contentValues.put("letters", goodsBean.getLetters());
                contentValues.put("sku", goodsBean.getSku());
                contentValues.put("price", goodsBean.getPrice());
                contentValues.put("sales_status", goodsBean.getSales_status());
                contentValues.put("inventory", goodsBean.getInventory());
                contentValues.put("discount", goodsBean.getDiscount());
                contentValues.put("unit", goodsBean.getUnit());
                contentValues.put("cost_price", goodsBean.getCost_price());
                contentValues.put("ratio_base", goodsBean.getRatio_base());
                contentValues.put("ratio_goods_id", goodsBean.getRatio_goods_id());
                contentValues.put("ratio_unit", goodsBean.getRatio_unit());
                contentValues.put("category_name", goodsBean.getCategory_name());
                contentValues.put("store_goods_id", goodsBean.getStore_goods_id());
                contentValues.put("group_value", goodsBean.getGroup_value());
                contentValues.put("goods_type", goodsBean.getGoods_type());
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

    public List<YWGoodBean> getGoodListByMealSku(String mealsku) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS,
                    "meals_sku = ?",
                    new String[]{mealsku}, null, null, null);
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
    public List<YWGoodBean> getGoodListByPinYin(String goodInfo) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<YWGoodBean> ywGoodBeanList = new ArrayList<>();
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.GOODS_TABLE_NAME, new String[]{GOODS_COLUMNS[0], GOODS_COLUMNS[1],
                            GOODS_COLUMNS[2], GOODS_COLUMNS[3], GOODS_COLUMNS[4], GOODS_COLUMNS[5], GOODS_COLUMNS[6], GOODS_COLUMNS[7],
                            GOODS_COLUMNS[8], GOODS_COLUMNS[9], GOODS_COLUMNS[10]
                            , GOODS_COLUMNS[11], GOODS_COLUMNS[12], GOODS_COLUMNS[13], GOODS_COLUMNS[14], GOODS_COLUMNS[15], GOODS_COLUMNS[16], GOODS_COLUMNS[17],GOODS_COLUMNS[18]},
                    "letters LIKE ?",
                    new String[]{"%" + goodInfo + "%"}, null, null, null);
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
    public YWGoodBean getGoodInfoByGoodCode(String goodCode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.GOODS_TABLE_NAME, GOODS_COLUMNS, "sku = ?", new String[]{goodCode}, null, null, null);
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

    private MealsItemBean paseMealItem(Cursor cursor) {
        MealsItemBean itemBean = new MealsItemBean();
        itemBean.setId(cursor.getInt(cursor.getColumnIndex(MEAL_COLUMNS[0])));
        itemBean.setGoods_type_id(cursor.getInt(cursor.getColumnIndex(MEAL_COLUMNS[1])));
        itemBean.setId(cursor.getInt(cursor.getColumnIndex(MEAL_COLUMNS[2])));
        itemBean.setMeal_goods_name(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[3])));
        itemBean.setMeal_goods_sku(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[4])));
        itemBean.setMeal_goods_price(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[5])));
        itemBean.setImg(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[6])));
        itemBean.setSales_status(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[7])));
        itemBean.setGoodsJson(cursor.getString(cursor.getColumnIndex(MEAL_COLUMNS[8])));
        Type type = new TypeToken<List<MealGoodsBean>>() {
        }.getType();
        itemBean.setMeal_store_goods_detail(new Gson().fromJson(itemBean.getGoodsJson(), type));
        return itemBean;

    }

    /**
     * 将查找到的数据转换成Order类
     * {"id", "category_id", "image", "product_name", "letters", "specs"};
     */
    private YWGoodBean parseGoodS(Cursor cursor) {
        YWGoodBean goods = new YWGoodBean();
        goods.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[0]))));
        goods.setCategory_id(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[1])));
        goods.setImage(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[2])));
        goods.setProduct_name(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[3])));
        goods.setLetters(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[4])));
        goods.setSku(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[5])));
        goods.setPrice(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[6])));
        goods.setSales_status(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[7])));
        goods.setInventory(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[8])));
        goods.setDiscount(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[9])));
        goods.setUnit(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[10])));
        goods.setCost_price(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[11])));
        goods.setRatio_base(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[12])));
        goods.setRatio_goods_id(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[13])));
        goods.setRatio_unit(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[14])));
        goods.setCategory_name(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[15])));
        goods.setStore_goods_id(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[16])));
        goods.setGroup_value(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[17])));
        goods.setGoods_type(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[18])));

//        goods.setSpecs(MyApplication.gson.fromJson(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[5])), new TypeToken<List<YWGoodBean.SpecsBean>>() {
//        }.getType()));
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
