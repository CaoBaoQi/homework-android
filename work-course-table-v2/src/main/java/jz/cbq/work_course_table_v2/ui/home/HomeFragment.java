package jz.cbq.work_course_table_v2.ui.home;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Calendar;

import jz.cbq.work_course_table_v2.MainActivity;
import jz.cbq.work_course_table_v2.R;
import jz.cbq.work_course_table_v2.coursecarditem;
import jz.cbq.work_course_table_v2.databasehelper;
import jz.cbq.work_course_table_v2.timemanager;
import jz.cbq.work_course_table_v2.zhlgd;

import static android.content.Context.MODE_PRIVATE;
import static jz.cbq.work_course_table_v2.MainActivity.courseflag;
import static jz.cbq.work_course_table_v2.MainActivity.examflag;
@SuppressLint("Range")
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<String> todaycourselist = new ArrayList<>();
    private ArrayList<String> todayexamlist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datasettings", MODE_PRIVATE);//读取昵称周数学期
        String call = sharedPreferences.getString("call", "USER");
        TextView greettextview = root.findViewById(R.id.greet_greet);
        greettextview.setText("Hello, "+call);
        String term = sharedPreferences.getString("term","大一上");
        int firstweek = sharedPreferences.getInt("firstweek",0);
        int weekOfYear = timemanager.getWeekOfYear();
        int week = weekOfYear - firstweek;
        TextView greettime = root.findViewById(R.id.greet_time);//显示当前时间
        String weeks = Integer.toString(week);
        greettime.setText("当前为"+term+"第"+weeks+"周");
        CardView greetcard = root.findViewById(R.id.greetcard);
        greetcard.setOnClickListener(new View.OnClickListener() {//修改昵称
            @Override
            public void onClick(View view) {
                final EditText calleditText = new EditText(getContext());
                AlertDialog.Builder editcalldialog = new AlertDialog.Builder(getActivity()).setTitle("请输入昵称").setView(calleditText).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String call = calleditText.getText().toString();
                        SharedPreferences.Editor editsharedPreferences = getActivity().getSharedPreferences("datasettings",MODE_PRIVATE).edit();
                        editsharedPreferences.putString("call", call);
                        editsharedPreferences.apply();
                        Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    }
                });
                editcalldialog.create().show();
            }
        });

        databasehelper coursedbhelper = new databasehelper(getActivity(),"database.db",null,1);//今日课程显示
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        Cursor coursecursor = coursedatabase.query("Course",null,"weekstart<=?",new String[]{String.valueOf(week)},null,null,"weekend desc");
        if (coursecursor.moveToFirst()){//从数据库中取出课程数据
            do {
                int weekend = coursecursor.getInt(coursecursor.getColumnIndex("weekend"));
                if(weekend >= week){//判断是否结束
                    int ofweek = coursecursor.getInt(coursecursor.getColumnIndex("ofweek"));
                    int weekday = timemanager.getWeekDay();
                    if(ofweek == weekday){//判断是否是今天
                        String coursename = coursecursor.getString(coursecursor.getColumnIndex("coursename"));
                        int timestart = coursecursor.getInt(coursecursor.getColumnIndex("timestart"));
                        int timeend = coursecursor.getInt(coursecursor.getColumnIndex("timeend"));
                        String courseroom = coursecursor.getString(coursecursor.getColumnIndex("courseroom"));
                        final String coursecontent = coursename + "  第" + timestart + "节至第" + timeend + "节  " + courseroom;
                        courseflag = 1;
                        todaycourselist.add(coursecontent);}//添加课程数据到课程显示界面
                    }
                else
                    break;
            }while (coursecursor.moveToNext());
        }
        if(courseflag == 0){
            todaycourselist.add("今天没有课");
        }
        coursecursor.close();
        ArrayAdapter<String> courseadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item_1, todaycourselist);//显示今日课程listview
        ListView todaycourse = root.findViewById(R.id.todaycourse_listview);
        todaycourse.setAdapter(courseadapter);

        int month = timemanager.getMonth();
        Cursor examcursor = coursedatabase.query("Exam",null,"month=?",new String[]{String.valueOf(month)},null,null,"day");
            if (examcursor.moveToFirst()){//从数据库中取出考试数据
                do {
                    int day = examcursor.getInt(examcursor.getColumnIndex("day"));
                    int today = timemanager.getDay();
                    if(day == today){//判断是否结束
                        String examname = examcursor.getString(examcursor.getColumnIndex("examname"));
                        String examroom = examcursor.getString(examcursor.getColumnIndex("examroom"));
                        final String examcontent = examname + "  " + examroom;
                        examflag = 1;
                        todayexamlist.add(examcontent);}//添加考试数据到考试显示界面
                }while (examcursor.moveToNext());
            }
        examcursor.close();
        if(examflag == 0){
            todayexamlist.add("今天没有考试");
        }
        ArrayAdapter<String> examadapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item_1, todayexamlist);//显示今日考试listview
        ListView todayexam = root.findViewById(R.id.todayexam_listview);
        todayexam.setAdapter(examadapter);

        final CardView zhlgdcard = root.findViewById(R.id.zhlgdcard);//智慧理工大CARD
        zhlgdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zhlgdintent = new Intent(getActivity(), zhlgd.class);
                startActivity(zhlgdintent);
            }
        });

        return root;
    }

}
