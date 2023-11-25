package jz.cbq.work_buy_car.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.db.UserDbHelper;
import jz.cbq.work_buy_car.entity.UserInfo;


/**
 * 修改密码
 */
public class UpdatePwdActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "公共通知";

    private EditText et_pwd;
    private EditText et_pwd_twice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        et_pwd = findViewById(R.id.update_pwd_activity_et_pwd);
        et_pwd_twice = findViewById(R.id.update_pwd_activity_et_pwd_twice);

        findViewById(R.id.update_pwd_activity_toolbar).setOnClickListener(v -> finish());


        findViewById(R.id.update_pwd_activity_btn_yes).setOnClickListener(v -> {
            String new_pwd = et_pwd.getText().toString();
            String pwd_twice = et_pwd_twice.getText().toString();

            if (TextUtils.isEmpty(new_pwd) || TextUtils.isEmpty(pwd_twice)) {
                Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
            } else if (!new_pwd.equals(pwd_twice)) {
                Toast.makeText(this, "两次输入不一致", Toast.LENGTH_SHORT).show();
            } else {
                UserInfo userInfo = UserInfo.getCurrentUserInfo();
                if (userInfo != null) {
                    int count = UserDbHelper.getInstance(this).updateUserInfoByEmailOrUsername(userInfo.getUsername(), new_pwd);
                    if (count > 0) {
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        setResult(1000);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("您已修改密码")
                                .setPriority(NotificationCompat.PRIORITY_MAX);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "请开启通知权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        notificationManager.notify(2, builder.build());

                        finish();
                        startActivity(new Intent(this, LoginActivity.class));

                    } else {
                        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}