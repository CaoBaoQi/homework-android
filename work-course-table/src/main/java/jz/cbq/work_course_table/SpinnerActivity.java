package jz.cbq.work_course_table;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by LENOVO on 2018/12/21.
 */

public class SpinnerActivity extends AppCompatActivity {
    private Spinner mWeek = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mWeek = (Spinner) findViewById(R.id.week_choose);
        String[] arr = {"单周", "双周", "每周"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.week_list, arr);
        mWeek.setAdapter(adapter);
        mWeek.setOnItemClickListener((AdapterView.OnItemClickListener)this);
    }
}

