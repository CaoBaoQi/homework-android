package jz.cbq.work_course_table_v2;
/**
 * 添加课程
 *@version 1.1.3
 */

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class addcourse extends MainActivity{

    private List<String> weeklist = new ArrayList<String>();//第几周
    private ArrayAdapter<String> courseweekspinneradapter;
    private List<String> ofweeklist = new ArrayList<String>();//周几
    private ArrayAdapter<String> courseofweekspinneradapter;
    private List<String> timelist = new ArrayList<String>();//时间
    private ArrayAdapter<String> coursetimespinneradapter;
    private databasehelper coursedbhelper;
    int ofweek = 1, timestart = 1, timeend = 1, weekstart = 1, weekend = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcourse);
        coursedbhelper = new databasehelper(addcourse.this, "database.db", null, 1);
        final SQLiteDatabase coursedatabase = coursedbhelper.getWritableDatabase();
        ofweeklist.add("周一");
        ofweeklist.add("周二");
        ofweeklist.add("周三");
        ofweeklist.add("周四");
        ofweeklist.add("周五");
        ofweeklist.add("周六");
        ofweeklist.add("周日");
        Spinner courseofweekspinner = findViewById(R.id.coursetimeofweek_spinner);//设置周几spinner
        courseofweekspinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ofweeklist);
        courseofweekspinneradapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        courseofweekspinner.setAdapter(courseofweekspinneradapter);
        courseofweekspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ofweek = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addcourse.this, "你没有选择任何项", Toast.LENGTH_SHORT).show();
            }
        });

        timelist.add("1节");
        timelist.add("2节");
        timelist.add("3节");
        timelist.add("4节");
        timelist.add("5节");
        timelist.add("6节");
        timelist.add("7节");
        timelist.add("8节");
        timelist.add("9节");
        timelist.add("10节");
        timelist.add("11节");
        timelist.add("12节");
        timelist.add("13节");
        Spinner coursetimestartspinner = findViewById(R.id.coursetimestart_spinner);//设置课程开始、结束时间
        Spinner coursetimeendspinner = findViewById(R.id.coursetimeend_spinner);
        coursetimespinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timelist);
        coursetimespinneradapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        coursetimestartspinner.setAdapter(coursetimespinneradapter);//课程开始spinner
        coursetimestartspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                timestart = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addcourse.this, "你没有选择任何项", Toast.LENGTH_SHORT).show();
            }
        });
        coursetimeendspinner.setAdapter(coursetimespinneradapter);///课程结束spinner
        coursetimeendspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position + 1 < timestart){
                    Toast.makeText(addcourse.this,"结束时间早于开始时间，已修改",Toast.LENGTH_SHORT).show();
                    timeend = timestart;
                }else{
                    timeend = position + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addcourse.this, "你没有选择任何项", Toast.LENGTH_SHORT).show();
            }
        });

        weeklist.add("第1周");
        weeklist.add("第2周");
        weeklist.add("第3周");
        weeklist.add("第4周");
        weeklist.add("第5周");
        weeklist.add("第6周");
        weeklist.add("第7周");
        weeklist.add("第8周");
        weeklist.add("第9周");
        weeklist.add("第10周");
        weeklist.add("第11周");
        weeklist.add("第12周");
        weeklist.add("第13周");
        weeklist.add("第14周");
        weeklist.add("第15周");
        weeklist.add("第16周");
        weeklist.add("第17周");
        weeklist.add("第18周");
        weeklist.add("第19周");
        weeklist.add("第20周");
        weeklist.add("第21周");
        weeklist.add("第22周");
        Spinner courseweekstartspinner = findViewById(R.id.courseweekstart_spinner);//设置开始结束周
        Spinner courseweekendspinner = findViewById(R.id.courseweekend_spinner);
        courseweekspinneradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weeklist);
        courseweekspinneradapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        courseweekstartspinner.setAdapter(courseweekspinneradapter);
        courseweekstartspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//设置开始周
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                weekstart = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addcourse.this, "你没有选择任何项", Toast.LENGTH_SHORT).show();
            }
        });
        courseweekendspinner.setAdapter(courseweekspinneradapter);
        courseweekendspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position + 1 < weekstart){
                    Toast.makeText(addcourse.this,"结束时间早于开始时间，已修改",Toast.LENGTH_SHORT).show();
                    weekend = weekstart;
                }else{
                    weekend = position + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addcourse.this, "你没有选择任何项", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText coursenameedittext = findViewById(R.id.coursename_edittext);
        final EditText courseteacheredittext = findViewById(R.id.courseteacher_edittext);
        final EditText courseroomedittext = findViewById(R.id.courseroom_edittext);
        Button add = findViewById(R.id.courseadd_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coursename = coursenameedittext.getText().toString();
                String courseteacher = courseteacheredittext.getText().toString();
                String courseroom = courseroomedittext.getText().toString();
                ContentValues coursevalues = new ContentValues();//添加课程到数据库
                coursevalues.put("coursename", coursename);
                coursevalues.put("weekstart", weekstart);
                coursevalues.put("weekend", weekend);
                coursevalues.put("ofweek", ofweek);
                coursevalues.put("timestart", timestart);
                coursevalues.put("timeend", timeend);
                coursevalues.put("courseteacher", courseteacher);
                coursevalues.put("courseroom", courseroom);
                coursedatabase.insert("Course", null, coursevalues);
                Toast.makeText(addcourse.this, "添加成功",Toast.LENGTH_SHORT).show();
                coursevalues.clear();
                finish();
            }
        });
        Button back = findViewById(R.id.coursefinish_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
