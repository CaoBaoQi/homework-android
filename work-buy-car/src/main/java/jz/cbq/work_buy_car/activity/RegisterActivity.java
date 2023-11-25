package jz.cbq.work_buy_car.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.db.UserDbHelper;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText et_username, et_email, et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = findViewById(R.id.register_activity_et_username);
        et_email = findViewById(R.id.register_activity_et_email);
        et_pwd = findViewById(R.id.register_activity_et_pwd);

        // 返回按钮事件
        findViewById(R.id.register_activity_toolbar).setOnClickListener(v -> finish());

        Button btn_register = findViewById(R.id.register_activity_btn_register);
        btn_register.setOnClickListener(this::validateForm);
    }

    /**
     * 校验注册表单
     *
     * @param v view
     */
    private void validateForm(View v) {
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String pwd = et_pwd.getText().toString();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
        } else {
            int count = UserDbHelper.getInstance(RegisterActivity.this).register(username, username, email, pwd);
            if (count > 0) {
                Toast.makeText(this, "注册成功请登录", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}