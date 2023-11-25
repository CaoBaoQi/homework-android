package jz.cbq.work_buy_car.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import jz.cbq.work_buy_car.MainActivity;
import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.db.UserDbHelper;
import jz.cbq.work_buy_car.entity.UserInfo;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "公共通知";
    /**
     * 用户名、密码 输入框
     */
    private EditText et_username, et_pwd;
    /**
     * sp （用于存储已登录用户的信息）
     */
    private SharedPreferences shared;
    /**
     * 是否登录
     */
    private boolean is_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tx_register = findViewById(R.id.login_activity_tx_register);
        Button btn_login = findViewById(R.id.login_activity_btn_login);
        CheckBox cb_save_pwd = findViewById(R.id.login_activity_cb_save_pwd);

        shared = getSharedPreferences("user", MODE_PRIVATE);
        et_username = findViewById(R.id.login_activity_et_username);
        et_pwd = findViewById(R.id.login_activity_et_pwd);
        is_login = shared.getBoolean("is_login", false);

        if (is_login) {
            String username = shared.getString("username", "CaoBei");
            String pwd = shared.getString("pwd", "2309312103");
            et_username.setText(username);
            et_pwd.setText(pwd);
            cb_save_pwd.setChecked(true);
        }

        tx_register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        btn_login.setOnClickListener(this::validateForm);
        cb_save_pwd.setOnCheckedChangeListener((buttonView, isChecked) -> is_login = isChecked);
    }

    /**
     * 校验登录表单
     *
     * @param v view
     */
    private void validateForm(View v) {
        String username = et_username.getText().toString();
        String pwd = et_pwd.getText().toString();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
        } else {
            UserInfo userInfo = UserDbHelper.getInstance(LoginActivity.this).loadUserInfoByEmailOrUsername(username);
            if (!(userInfo == null)) {
                if ((username.equals(userInfo.getUsername()) || username.equals(userInfo.getEmail())) && pwd.equals(userInfo.getPassword())) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putBoolean("is_login", is_login);
                    editor.putString("username", username);
                    editor.putString("pwd", pwd);
                    editor.apply();

                    UserInfo.setCurrentUserInfo(userInfo);

                    Toast.makeText(this, "登录成功,欢迎您 " + userInfo.getUsername(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.channel_name);
                        String description = getString(R.string.channel_description);
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system; you can't change the importance
                        // or other notification behaviors after this
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("登录成功")
                            .setContentText("欢迎您 " + userInfo.getUsername())
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "请开启通知权限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    notificationManager.notify(1, builder.build());


                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "未注册", Toast.LENGTH_SHORT).show();
            }
        }
    }
}