package jz.cbq.work_note_book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import jz.cbq.work_note_book.MainActivity;
import jz.cbq.work_note_book.R;

/**
 * 启动页
 *
 * @author cbq
 * @date 2023/11/20 22:45
 * @since 1.0.0
 */
public class SplashActivity extends AppCompatActivity {

    private TextView tv_second;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toast.makeText(this, "点击时间即可跳过", Toast.LENGTH_SHORT).show();

        tv_second = findViewById(R.id.tv_second);
        tv_second.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

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
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
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