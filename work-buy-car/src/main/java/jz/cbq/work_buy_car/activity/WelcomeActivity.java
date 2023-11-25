package jz.cbq.work_buy_car.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import jz.cbq.work_buy_car.R;


/**
 * 启动页
 */
public class WelcomeActivity extends AppCompatActivity {
    private TextView tv_second;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tv_second = findViewById(R.id.welcome_activity_tv_second);
        tv_second.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        startCountdown();
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_second.setText(secondsRemaining + " s");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}