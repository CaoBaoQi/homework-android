package jz.cbq.work_course_table_v2;
/**
 * 添加考试
 * 考试日期选择为简单列表下拉选择，未判定是否合法
 * @version 1.1.2
 */

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class addexam extends MainActivity{

    private List<String> monthlist = new ArrayList<String>();
    private ArrayAdapter<String> exammonthspinneradapter;
    private List<String> daylist = new ArrayList<String>();
    private ArrayAdapter<String> examdayspinneradapter;
    private databasehelper examtimedbhelper;
    int month =0, day = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexam);
        examtimedbhelper = new databasehelper(addexam.this,"database.db",null,1);
        final SQLiteDatabase examdatabase = examtimedbhelper.getWritableDatabase();
        monthlist.add("  1  ");
        monthlist.add("  2  ");
        monthlist.add("  3  ");
        monthlist.add("  4  ");
        monthlist.add("  5  ");
        monthlist.add("  6  ");
        monthlist.add("  7  ");
        monthlist.add("  8  ");
        monthlist.add("  9  ");
        monthlist.add("  10  ");
        monthlist.add("  11  ");
        monthlist.add("  12  ");
        Spinner exammonthspinner = findViewById(R.id.examtimemonth_spinner);//以下为选择考试月份
        exammonthspinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,monthlist);
        exammonthspinneradapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        exammonthspinner.setAdapter(exammonthspinneradapter);
        exammonthspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addexam.this,"你没选中任何选项",Toast.LENGTH_SHORT).show();
            }
        });

        daylist.add("  1  ");
        daylist.add("  2  ");
        daylist.add("  3  ");
        daylist.add("  4  ");
        daylist.add("  5  ");
        daylist.add("  6  ");
        daylist.add("  7  ");
        daylist.add("  8  ");
        daylist.add("  9  ");
        daylist.add("  10  ");
        daylist.add("  11  ");
        daylist.add("  12  ");
        daylist.add("  13  ");
        daylist.add("  14  ");
        daylist.add("  15  ");
        daylist.add("  16  ");
        daylist.add("  17  ");
        daylist.add("  18  ");
        daylist.add("  19  ");
        daylist.add("  20  ");
        daylist.add("  21  ");
        daylist.add("  22  ");
        daylist.add("  23  ");
        daylist.add("  24  ");
        daylist.add("  25  ");
        daylist.add("  26  ");
        daylist.add("  27  ");
        daylist.add("  28  ");
        daylist.add("  29  ");
        daylist.add("  30  ");
        daylist.add("  31  ");
        Spinner examdayspinner = findViewById(R.id.examtimeday_spinner);//以下为选择考试日期
        examdayspinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,daylist);
        examdayspinneradapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        examdayspinner.setAdapter(examdayspinneradapter);
        examdayspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(addexam.this,"你没选中任何选项",Toast.LENGTH_SHORT).show();
            }
        });
        final EditText examnameedittext = findViewById(R.id.examname_edittext);//读取考试名称
        final EditText examroomedittext = findViewById(R.id.examroom_edittext);//读取考试地点
        Button add = findViewById(R.id.examadd_button);//点击提交
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String examname = examnameedittext.getText().toString();//只能写在里面才能赋值，不懂为啥
                String examroom = examroomedittext.getText().toString();
                ContentValues examvalues = new ContentValues();//添加考试到数据库
                examvalues.put("examname",examname);
                examvalues.put("month",month);
                examvalues.put("day",day);
                examvalues.put("examroom",examroom);
                examdatabase.insert("Exam",null,examvalues);
                Toast.makeText(addexam.this,"添加成功",Toast.LENGTH_SHORT).show();
                examvalues.clear();
                finish();
            }
        });
        Button back = findViewById(R.id.examfinish_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
