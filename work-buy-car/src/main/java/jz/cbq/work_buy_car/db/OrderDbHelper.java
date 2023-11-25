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
import jz.cbq.work_buy_car.entity.OrderInfo;


/**
 * order DB 数据库
 */
public class OrderDbHelper extends SQLiteOpenHelper {
    private static OrderDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "order.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public OrderDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static OrderDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new OrderDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 order_table 表
        db.execSQL("create table order_table(order_id integer primary key autoincrement, " +
                "username text," +
                "product_img integer," +
                "product_title text," +
                "product_price integer," +
                "product_count integer," +
                "address text," +
                "mobile text" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 批量插入
     *
     * @param list    list
     * @param address address
     * @param mobile  mobile
     */
    public void insertByAll(List<CarInfo> list, String address, String mobile) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                values.put("username", list.get(i).getUsername());
                values.put("product_img", list.get(i).getProduct_img());
                values.put("product_title", list.get(i).getProduct_title());
                values.put("product_price", list.get(i).getProduct_price());
                values.put("product_count", list.get(i).getProduct_count());
                values.put("address", address);
                values.put("mobile", mobile);
                db.insert("order_table", null, values);

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

    }


    /**
     * 获取所有信息
     */
    @SuppressLint("Range")
    public List<OrderInfo> findAll(String username) {
        SQLiteDatabase db = getReadableDatabase();
        List<OrderInfo> list = new ArrayList<>();
        String sql = "select order_id,username,product_img,product_title,product_price,product_count, address, mobile  from order_table where username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int order_id = cursor.getInt(cursor.getColumnIndex("order_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            list.add(new OrderInfo(order_id, username, product_img, product_title, product_price, product_count, address, mobile));
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 根据 order_id 删除
     */
    public int delete(String order_id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete("order_table", " order_id=?", new String[]{order_id});
        db.close();
        return delete;
    }
}

