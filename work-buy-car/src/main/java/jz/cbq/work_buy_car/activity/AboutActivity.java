package jz.cbq.work_buy_car.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import jz.cbq.work_buy_car.R;


/**
 * 关于
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.about_activity_toolbar).setOnClickListener(v -> finish());

    }
}