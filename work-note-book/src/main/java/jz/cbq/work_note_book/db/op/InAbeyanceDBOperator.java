package jz.cbq.work_note_book.db.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_note_book.db.DBHelper;
import jz.cbq.work_note_book.entity.InAbeyance;

/**
 * 待办事项 OP
 *
 * @author cbq
 * @date 2023/11/25 16:04
 * @since 1.0.0
 */
public class InAbeyanceDBOperator {
    /**
     * 添加待办
     * @param context context
     * @param inAbeyance InAbeyance
     * @return _id
     */
    public static int add_in_abeyance(Context context, InAbeyance inAbeyance) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "INSERT INTO in_abeyance " +
                "(content, date_remind, date_created)" +
                "VALUES" +
                "('" + inAbeyance.getContent() + "','" +
                inAbeyance.getDate_remind() + "','" +
                inAbeyance.getDate_created() +
                "')";

        database.execSQL(sql);
        String get_id = "SELECT last_insert_rowid() FROM in_abeyance";
        Cursor cursor = database.rawQuery(get_id, null);
        int _id = -1;
        if (cursor.moveToFirst()) {
            _id = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return _id;
    }

    /**
     * 获取全部待办信息
     * @param context context
     * @return List<InAbeyance>
     */
    public static List<InAbeyance> getInAbeyanceData(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM in_abeyance " +
                "ORDER BY _id DESC";

        Cursor cursor = database.rawQuery(sql, null);
        List<InAbeyance> inAbeyanceList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String content = cursor.getString(1);
            String date_remind = cursor.getString(2);
            int status = cursor.getInt(3);
            InAbeyance inAbeyance = new InAbeyance(_id, content, date_remind, status);
            inAbeyanceList.add(inAbeyance);
        }
        cursor.close();
        database.close();
        return inAbeyanceList;
    }

    /**
     * 根据关键字查询待办
     * @param context context
     * @param keyWord 关键字
     * @return List<InAbeyance>
     */
    public static List<InAbeyance> queryInAbeyanceData(Context context, String keyWord) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM in_abeyance " +
                "WHERE content LIKE \"%" + keyWord + "%\"" +
                "ORDER BY _id DESC";

        Cursor cursor = database.rawQuery(sql, null);// 执行sql语句
        List<InAbeyance> inAbeyanceList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String content = cursor.getString(1);
            String date_remind = cursor.getString(2);
            int status = cursor.getInt(3);
            InAbeyance inAbeyance = new InAbeyance(_id, content, date_remind, status);
            inAbeyanceList.add(inAbeyance);
        }
        cursor.close();
        database.close();
        return inAbeyanceList;
    }

    /**
     * 根据 id 删除待办
     * @param context context
     * @param _id _id
     */
    public static void deleteInAbeyance(Context context, int _id) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "delete FROM in_abeyance WHERE " +
                "_id = " + _id;

        database.execSQL(sql);
        database.close();
    }

    /**
     * 修改待办状态
     * @param context context
     * @param _id _id
     */
    public static void changeInAbeyanceStatus(Context context, int _id) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "UPDATE in_abeyance SET " +
                "status = 1 - status WHERE " +
                "_id = " + _id;

        database.execSQL(sql);
        database.close();
    }

    /**
     * 更新待办
     * @param context context
     * @param inAbeyance InAbeyance
     */
    public static void updateInAbeyance(Context context, InAbeyance inAbeyance) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "UPDATE in_abeyance SET " +
                "content = '" + inAbeyance.getContent() + "'," +
                "date_remind = '" + inAbeyance.getDate_remind() + "'" +
                " WHERE " +
                "_id = " + inAbeyance.get_id();

        database.execSQL(sql);
        database.close();
    }
}
