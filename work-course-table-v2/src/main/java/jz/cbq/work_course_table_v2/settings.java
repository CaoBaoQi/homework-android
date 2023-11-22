package jz.cbq.work_course_table_v2;
/**
 * 设置
 * #version 1.1.3
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class settings extends MainActivity {
    private List<String> weeklist = new ArrayList<String>();
    private ArrayAdapter<String> weekspinneradapter;
    private List<String> termlist = new ArrayList<String>();
    private ArrayAdapter<String> termspinneradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final SharedPreferences.Editor editsharedPreferences = getSharedPreferences("datasettings",MODE_PRIVATE).edit();
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
        weeklist.add("放假中");
        Spinner weekspinner = findViewById(R.id.weeks_spinner);//以下为设置当前周数
        weekspinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,weeklist);
        weekspinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekspinner.setAdapter(weekspinneradapter);
        weekspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int weekofyear =timemanager.getWeekOfYear();
                int firstweek = weekofyear - position - 1;
                editsharedPreferences.putInt("firstweek",firstweek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(settings.this,"你没选中任何选项",Toast.LENGTH_SHORT).show();
            }
        });

        termlist.add("大一上");
        termlist.add("大一下");
        termlist.add("大二上");
        termlist.add("大二下");
        termlist.add("大三上");
        termlist.add("大三下");
        termlist.add("大四上");
        termlist.add("大四下");
        Spinner termspinner = findViewById(R.id.terms_spinner);//以下为设置当前学期
        termspinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,termlist);
        termspinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termspinner.setAdapter(termspinneradapter);
        termspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editsharedPreferences.putString("term",termspinneradapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(settings.this,"你没选中任何选项",Toast.LENGTH_SHORT).show();
            }
        });
        Button settingsbutton = findViewById(R.id.settings_button);//点击按钮提交
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editsharedPreferences.apply();
                Toast.makeText(settings.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}

