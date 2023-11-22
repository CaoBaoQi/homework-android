package jz.cbq.work_course_table_v2.ui.slideshow;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Calendar;

import jz.cbq.work_course_table_v2.MainActivity;
import jz.cbq.work_course_table_v2.R;
import jz.cbq.work_course_table_v2.addexam;
import jz.cbq.work_course_table_v2.databasehelper;
import jz.cbq.work_course_table_v2.examcardadapter;
import jz.cbq.work_course_table_v2.examcarditem;
import jz.cbq.work_course_table_v2.examcalendarremind;
@SuppressLint("Range")
public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private databasehelper examtimedbhelper;
    ArrayList<examcarditem> examlist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        examtimedbhelper = new databasehelper(getActivity(),"database.db",null,1);
        final SQLiteDatabase examdatabase = examtimedbhelper.getWritableDatabase();
        initexamitemcard();//初始化考试LISTVIEW
        examcardadapter examcardadapter = new examcardadapter(getActivity(), R.layout.examitem, examlist);
        final ListView examlistview = (ListView) root.findViewById(R.id.exam_listview);
        examlistview.setAdapter(examcardadapter);//显示listview
        examlistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                examcarditem examcarditem = examlist.get(position);//显示考试详情
                final String examname = examcarditem.getExamname();
                final int month = examcarditem.getMonth();
                String months = "" + month;
                final int day = examcarditem.getDay();
                String days = "" + day;
                final String examroom = examcarditem.getExamroom();
                final String examcontent = "名称" + examname + "\n时间：" + months + "月" + days + "日\n地点" + examroom;
                AlertDialog.Builder courseexamdialog = new AlertDialog.Builder(getActivity()).setTitle("考试信息").setMessage(examcontent).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//进入课程添加
                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//查看考试详情或添加到日历或删除
                        examdatabase.delete("Exam","examname=?", new String[]{String.valueOf(examname)});
                        Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                    }
                }).setNeutralButton("添加到日历", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//添加到日历
                        PackageManager packageManager = getActivity().getPackageManager();
                        boolean calendarpermission = (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission("android.permission.WRITE_CALENDAR","jz.cbq.work_course_table_v2"));
                        if (calendarpermission) {//Log检查发现该过程只运行一次，华为日历里有今明两天事件，可能是huawei日历的bug
                            boolean result = examcalendarremind.addCalendarEvent(getContext(), examname + "考试", "考试地点：" + examroom, month, day);
                            if (result != false) {
                                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(),"没有日历权限哦",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                courseexamdialog.create().show();
            }
        });
        Button examclear = root.findViewById(R.id.examclear_button);//删除所有考试
        examclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder examcleardialog = new AlertDialog.Builder(getActivity()).setTitle("确认清空考试数据吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        examdatabase.delete("Exam", null, null);
                        Toast.makeText(getContext(),"数据已清空",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("我再想想", null );
                examcleardialog.create().show();
            }
        });
        return root;
    }

    private void initexamitemcard(){
        final SQLiteDatabase examdatabase = examtimedbhelper.getWritableDatabase();
        Cursor examcursor = examdatabase.query("Exam",null,null,null,null,null,null);
        if (examcursor.moveToFirst()){//从数据库中取出考试数据
            do {
                final String examname = examcursor.getString(examcursor.getColumnIndex("examname"));
                final int month = examcursor.getInt(examcursor.getColumnIndex("month"));
                final int day = examcursor.getInt(examcursor.getColumnIndex("day"));
                String examroom = examcursor.getString(examcursor.getColumnIndex("examroom"));
                examcarditem examcard = new examcarditem(examname, month, day, examroom);
                examlist.add(examcard);//添加考试数据到考试显示界面
            }while (examcursor.moveToNext());
        }
        examcursor.close();
    }
}
