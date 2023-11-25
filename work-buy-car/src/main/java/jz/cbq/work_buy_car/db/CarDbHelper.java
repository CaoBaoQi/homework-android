package jz.cbq.work_buy_car.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_buy_car.entity.CarInfo;

/**
 * 购物车 DB
 */
public class CarDbHelper extends SQLiteOpenHelper {
    /**
     * sHelper
     */
    private static CarDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "car.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public CarDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static CarDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new CarDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table car_table(car_id integer primary key autoincrement, " +
                "username text," +
                "product_id integer," +
                "product_img integer," +
                "product_title text," +
                "product_description text," +
                "product_price integer," +
                "product_count integer" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 添加购物车
     *
     * @param username            用户名
     * @param product_id          商品 id
     * @param product_img         商品 img
     * @param product_title       商品 title
     * @param product_description 商品 description
     * @param product_price       商品 price
     * @return count
     */
    public int addCar(String username, Integer product_id, Integer product_img, String product_title, String product_description, Integer product_price) {
        CarInfo carInfo = loadCarInfoByUsernameAndProductId(username, product_id);

        if (carInfo == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("username", username);
            values.put("product_id", product_id);
            values.put("product_img", product_img);
            values.put("product_title", product_title);
            values.put("product_description", product_description);
            values.put("product_price", product_price);
            values.put("product_count", 1);
            String nullColumnHack = "values(null,?,?,?,?,?,?,?)";

            int insert = (int) db.insert("car_table", nullColumnHack, values);
            db.close();
            return insert;
        } else {
            return updateProduct(carInfo.getCar_id(), carInfo);
        }


    }

    /**
     * 根据用户名查找购物信息
     */
    @SuppressLint("Range")
    public CarInfo findCarByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        CarInfo carInfo = null;
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price" +
                "  from car_table where username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_id = cursor.getInt(cursor.getColumnIndex("product_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));

            carInfo = new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count);
        }
        cursor.close();
        db.close();
        return carInfo;
    }

    /**
     * 根据 id 修改购物车商品数量
     */
    public int updateProduct(int car_id, CarInfo carInfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("product_count", carInfo.getProduct_count() + 1);

        int update = db.update("car_table", values, " car_id=?", new String[]{car_id + ""});

        db.close();
        return update;

    }

    /**
     * 根据 id 修改购物车商品数量
     */
    public int updateProductSub(int car_id, CarInfo carInfo) {

        if (carInfo.getProduct_count() >= 2) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("product_count", carInfo.getProduct_count() - 1);

            int update = db.update("car_table", values, " car_id=?", new String[]{car_id + ""});

            db.close();
            return update;
        }
        return 0;
    }

    /**
     * 根据用户名查找购物信息
     */
    @SuppressLint("Range")
    public CarInfo loadCarInfoByUsernameAndProductId(String username, Integer product_id) {
        SQLiteDatabase db = getReadableDatabase();
        CarInfo carInfo = null;
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price,product_count from car_table where username=? and product_id = ?";
        String[] selectionArgs = {username, String.valueOf(product_id)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            carInfo = new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count);
        }
        cursor.close();
        db.close();
        return carInfo;
    }

    /**
     * 获取所有信息
     */
    @SuppressLint("Range")
    public List<CarInfo> findAll(String username) {
        SQLiteDatabase db = getReadableDatabase();
        List<CarInfo> list = new ArrayList<>();
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price,product_count  from car_table where username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_id = cursor.getInt(cursor.getColumnIndex("product_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            list.add(new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count));
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 根据 car_id 删除
     */
    public int delete(String car_id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete("car_table", " car_id=?", new String[]{car_id});
        db.close();
        return delete;
    }


}

