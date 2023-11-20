package jz.cbq.work_note_book.db.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_note_book.db.DBHelper;
import jz.cbq.work_note_book.entity.InAbeyance;
import jz.cbq.work_note_book.entity.Note;
import jz.cbq.work_note_book.utils.DateUtil;

/**
 * 笔记 DB TODO 分离两个 OP
 *
 * @author cbq
 * @date 2023/11/20 22:41
 * @since 1.0.0
 */
public class NoteBookDBOperator {

    /**
     * 获取全部 Note 数据
     * @param context context
     * @param recycle_status 0 未回收 | 1 已回收
     * @return List<Note>
     */
    public static List<Note> getNotesData(Context context, int recycle_status) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String getAllNotes = "select * from note " +
                "where recycle_status = " + recycle_status +
                " order by date_updated desc";

        List<Note> notes = new ArrayList<>();

        Cursor cursor = database.rawQuery(getAllNotes, null);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String date_created = cursor.getString(3);
            String date_updated = cursor.getString(4);
            notes.add(new Note(_id, title, content, date_created, date_updated));
        }
        cursor.close();
        database.close();
        return notes;
    }

    /**
     * 查询符合关键字的 Note 数据
     * @param context context
     * @param keyWord 关键字
     * @param recycle_status 0 未回收 | 1 已回收
     * @return List<Note>
     */
    public static List<Note> queryNotesData(Context context,String keyWord, int recycle_status) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String queryNotesData = "select * from note " +
                "where (title like \"%" + keyWord + "%\" " +
                "or content like \"%" + keyWord + "%\") " +
                "and recycle_status = " + recycle_status +
                " order by date_updated desc";

        List<Note> notes = new ArrayList<>();

        Cursor cursor = database.rawQuery(queryNotesData, null);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String date_created = cursor.getString(3);
            String date_updated = cursor.getString(4);
            notes.add(new Note(_id, title, content, date_created, date_updated));
        }
        cursor.close();
        database.close();
        return notes;
    }

    /**
     * 添加一条笔记
     * @param context context
     * @param note Note
     */
    public static void addNote(Context context, Note note) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String date = DateUtil.getCurrentTime();
        String insert_note = "insert into note" +
                "(title, content, date_created, date_updated)" +
                "values" +
                "('" + note.getTitle() + "','" +
                note.getContent() + "','" +
                date + "','" + date + "')";

        database.execSQL(insert_note);
        database.close();
    }

    /**
     * 更新笔记
     * @param context 更新笔记
     * @param note Note
     */
    public static void updateNote(Context context, Note note) {
        DBHelper dbHelper = DBHelper.getInstance(context);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String date = DateUtil.getCurrentTime();

        String update_note = "update note " +
                "set title = '" + note.getTitle() + "'," +
                "content = '" + note.getContent() + "'," +
                "date_updated = '" + date + "'" +
                "where _id = " + note.get_id();

        database.execSQL(update_note);
        database.close();
    }

    /**
     * 更改笔记状态
     * @param context context
     * @param _id _id
     */
    public static void changeNoteRecycleStatus(Context context, int _id) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String change_recycle_status = "update note " +
                "set recycle_status = 1 - recycle_status " +
                "where _id = " + _id;

        database.execSQL(change_recycle_status);
        database.close();
    }

    /**
     * 根据 id 删除一条笔记
     * @param context context
     * @param _id _id
     */
    public static void deleteNote(Context context, int _id) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String delete_note = "delete from note " +
                "where _id = " + _id;

        database.execSQL(delete_note);
        database.close();
    }

    /**
     * 添加待办
     * @param context context
     * @param inAbeyance InAbeyance
     * @return _id
     */
    public static int add_in_abeyance(Context context, InAbeyance inAbeyance) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String add_in_abeyance = "insert into in_abeyance " +
                "(content, date_remind, date_created)" +
                "values" +
                "('" + inAbeyance.getContent() + "','" +
                inAbeyance.getDate_remind() + "','" +
                inAbeyance.getDate_created() +
                "')";

        database.execSQL(add_in_abeyance);
        String get_id = "select last_insert_rowid() from in_abeyance";
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

        String get_all_in_abeyance = "select * from in_abeyance " +
                "order by _id desc";

        Cursor cursor = database.rawQuery(get_all_in_abeyance, null);
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

        String query_in_abeyance = "select * from in_abeyance " +
                "where content like \"%" + keyWord + "%\"" +
                "order by _id desc";

        Cursor cursor = database.rawQuery(query_in_abeyance, null);// 执行sql语句
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

        String deleteInAbeyance = "delete from in_abeyance where " +
                "_id = " + _id;

        database.execSQL(deleteInAbeyance);
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

        String update_in_abeyance_status = "update in_abeyance set " +
                "status = 1 - status where " +
                "_id = " + _id;

        database.execSQL(update_in_abeyance_status);
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

        String update_in_abeyance = "update in_abeyance set " +
                "content = '" + inAbeyance.getContent() + "'," +
                "date_remind = '" + inAbeyance.getDate_remind() + "'" +
                " where " +
                "_id = " + inAbeyance.get_id();

        database.execSQL(update_in_abeyance);
        database.close();
    }
}

