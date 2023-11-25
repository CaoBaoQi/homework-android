package jz.cbq.work_note_book.db.op;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jz.cbq.work_note_book.db.DBHelper;
import jz.cbq.work_note_book.entity.Account;

/**
 * 账户 OP
 *
 * @author cbq
 * @date 2023/11/25 16:35
 * @since 1.0.0
 */
public class AccountDBOperator {

    /**
     * 注册用户
     * @param context context
     * @return count (0 失败 | 1 成功)
     */
    public static int register(Context context, Account account) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("username", account.getUsername());
        values.put("email", account.getEmail());
        values.put("password", account.getPassword());

        String nullColumnHack = "values(null,?,?,?)";

        int count = (int) database.insert("account", nullColumnHack, values);
        database.close();
        return count;
    }

    /**
     * 根据用户名或邮箱查找
     * @param context context
     * @param text 用户名或邮箱
     * @return Account
     */
    @SuppressLint("Range")
    public static Account findAccount(Context context, String text) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Account account = null;

        String sql = "SELECT * FROM account WHERE username=? or email=?";
        String[] selectionArgs = {text, text};

        Cursor cursor = database.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int user_id = cursor.getInt(cursor.getColumnIndex("_id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            account = new Account(user_id, username, email, password);
        }
        cursor.close();
        database.close();
        return account;
    }
}
