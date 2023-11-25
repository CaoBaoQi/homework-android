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
 * 笔记 OP
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

        String sql = "SELECT * FROM note " +
                "WHERE recycle_status = " + recycle_status +
                " ORDER BY date_updated DESC";

        List<Note> notes = new ArrayList<>();

        Cursor cursor = database.rawQuery(sql, null);
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

        String sql = "SELECT * FROM note " +
                "WHERE (title LIKE \"%" + keyWord + "%\" " +
                "OR content LIKE \"%" + keyWord + "%\") " +
                "AND recycle_status = " + recycle_status +
                " ORDER BY date_updated DESC";

        List<Note> notes = new ArrayList<>();

        Cursor cursor = database.rawQuery(sql, null);
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
        String sql = "INSERT INTO note" +
                "(title, content, date_created, date_updated)" +
                "VALUES" +
                "('" + note.getTitle() + "','" +
                note.getContent() + "','" +
                date + "','" + date + "')";

        database.execSQL(sql);
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

        String sql = "UPDATE note " +
                "SET title = '" + note.getTitle() + "'," +
                "content = '" + note.getContent() + "'," +
                "date_updated = '" + date + "'" +
                "WHERE _id = " + note.get_id();

        database.execSQL(sql);
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

        String sql = "UPDATE note " +
                "SET recycle_status = 1 - recycle_status " +
                "WHERE _id = " + _id;

        database.execSQL(sql);
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

        String sql = "DELETE FROM note " +
                "WHERE _id = " + _id;

        database.execSQL(sql);
        database.close();
    }

}

