package jz.cbq.work_note_book.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 *  数据库 DBHelper
 *
 * @author cbq
 * @date 2023/11/20 22:39
 * @since 1.0.0
 */
public class DBHelper extends SQLiteOpenHelper {
    /**
     * 数据库名称
     */
    private static final String DBNAME = "note_book.db";
    /**
     * 实例 instance
     */
    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context, DBNAME, null, 1);
        }
        return instance;
    }

    private DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        noteTable(sqLiteDatabase);
        inAbeyanceTable(sqLiteDatabase);
        accountTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    /**
     * 创建 note 表并插入一条默认数据
     * @param sqLiteDatabase sqLiteDatabase
     */
    private static void noteTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE note(" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "title text," +
                "content text," +
                "date_created text," +
                "date_updated text," +
                "recycle_status integer DEFAULT 0)";

        String insert_default_note = "INSERT INTO note " +
                "(title, content, date_created, date_updated)" +
                "VALUES" +
                "(\"About\", \"1.学校: 晋中学院 \n" +
                "2.专业: 计算机科学与技术 \n" +
                "3.班级: 计算机专升本 2301 班 \n" +
                "4.姓名: 曹宝琪 \n" +
                "5.学号: 2309312102 \n" +
                "6.江天一色 ~ \n" +
                "7.email: 1203952894@qq.com \"," +
                "\"11月20日 上午 11:11\",\"11月20日 上午 11:11\")";

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(insert_default_note);
    }

    /**
     * 创建 in_abeyance 表
     * @param sqLiteDatabase sqLiteDatabase
     */
    private static void inAbeyanceTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE in_abeyance (" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "content text," +
                "date_remind text," +
                "status integer DEFAULT 0," +
                "date_created text)";

        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 创建 账户 表
     * @param sqLiteDatabase sqLiteDatabase
     */
    private static void accountTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE account (" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "username text," +
                "email text," +
                "password text)";

        String insert_default_account = "INSERT INTO account " +
                "(username, email, password)" +
                "VALUES" +
                "('CaoBaoQi', '1203952894@qq.com','123321')";

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(insert_default_account);
    }

}

