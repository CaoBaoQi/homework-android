package jz.cbq.work_buy_car.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import jz.cbq.work_buy_car.entity.UserInfo;


/**
 * 用户 DB
 */
public class UserDbHelper extends SQLiteOpenHelper {
    /**
     * sHelper
     */
    private static UserDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "user.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static UserDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new UserDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    /**
     * 初始化 DB
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user_table(user_id integer primary key autoincrement, " +
                "username text," +
                "nickname text," +
                "email text," +
                "password text" +
                ")");
        // init-data (sql)
        db.execSQL("insert into user_table values(null,'CaoBei','计算机专升本 2301 班 - 2309312103','1203952894@qq.com','2309312103')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param nickname 昵称
     * @param email    邮箱
     * @param password 密码
     * @return count (0 失败 | 1 成功)
     */
    public int register(String username, String nickname, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("nickname", nickname);
        values.put("email", email);
        values.put("password", password);

        String nullColumnHack = "values(null,?,?,?,?)";

        int insert = (int) db.insert("user_table", nullColumnHack, values);
        db.close();
        return insert;
    }

    /**
     * 根据用户名或邮箱查找用户
     *
     * @param text 用户名或邮箱
     * @return 用户信息
     */
    @SuppressLint("Range")
    public UserInfo loadUserInfoByEmailOrUsername(String text) {
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;
        String sql = "select user_id,username,nickname,email,password from user_table where username=? or email=?";
        String[] selectionArgs = {text, text};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            userInfo = new UserInfo(user_id, username, nickname, email, password);
        }
        cursor.close();
        db.close();
        return userInfo;
    }

    /**
     * 根据用户名或邮箱修改密码
     */
    public int updateUserInfoByEmailOrUsername(String text, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", password);
        int update = db.update("user_table", values, "username = ? or email = ?", new String[]{text, text});

        db.close();

        return update;
    }
}
