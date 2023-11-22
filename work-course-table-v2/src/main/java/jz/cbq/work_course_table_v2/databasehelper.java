package jz.cbq.work_course_table_v2;
/**
 * 数据库管理
 * version 1.1.1
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class databasehelper extends SQLiteOpenHelper {
    public static final String createexamdb = "create table Exam(" + "examname varchar,"+"month integer,"+"day integer,"+"examroom varchar)";
    public static final String createcoursedb = "create table Course(" + "coursename varchar,"+"weekstart integer,"+"weekend integer,"+"ofweek integer,"+"timestart integer,"+"timeend integer,"+"courseteacher varchar,"+"courseroom varchar)";
    private Context mContext;

    public databasehelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createexamdb);
        sqLiteDatabase.execSQL(createcoursedb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
