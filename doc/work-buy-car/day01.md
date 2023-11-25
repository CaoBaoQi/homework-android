# Day01

- 项目框架搭建
- 登录、注册、记住我功能的实现

# 1.1 项目框架搭建

- 使用 **Activity + Fragment** 实现页面的动态切换
- 通过 **BottomNavigationView** 实现底部 **bottom** 页面效果

## Main 入口

```java
/**
 * 启动 Main
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 主页 Fragment
     */
    private HomeFragment home;
    /**
     * 购物车 Fragment
     */
    private CarFragment car;
    /**
     * 订单 Fragment
     */
    private OrderFragment order;
    /**
     * 我的 Fragment
     */
    private MineFragment mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 开启毛玻璃感
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // 取消 bottom 的着色效果
        BottomNavigationView bottom = findViewById(R.id.main_activity_bottom);
        bottom.setItemIconTintList(null);

        // 默认加载 HomeFragment
        selectedFragment(0);

        bottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tab_home) {
                selectedFragment(0);
            } else if (item.getItemId() == R.id.tab_car) {
                selectedFragment(1);
            } else if (item.getItemId() == R.id.tab_order) {
                selectedFragment(2);
            } else {
                selectedFragment(3);
            }
            return true;
        });

    }


    /**
     * 选中 Fragment
     *
     * @param position position
     */
    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (home == null) {
                    home = new HomeFragment();
                    transaction.add(R.id.main_activity_container, home);
                } else {
                    transaction.show(home);
                }
                break;
            case 1:
                if (car == null) {
                    car = new CarFragment();
                    transaction.add(R.id.main_activity_container, car);
                } else {
                    transaction.show(car);
                }
                break;
            case 2:
                if (order == null) {
                    order = new OrderFragment();
                    transaction.add(R.id.main_activity_container, order);
                } else {
                    transaction.show(order);
                }
                break;
            default:
                if (mine == null) {
                    mine = new MineFragment();
                    transaction.add(R.id.main_activity_container, mine);
                } else {
                    transaction.show(mine);
                }
                break;
        }

        transaction.commit();
    }

    /**
     * 隐藏 Fragment
     *
     * @param transaction transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (home != null) {
            transaction.hide(home);
        }
        if (car != null) {
            transaction.hide(car);
        }
        if (order != null) {
            transaction.hide(order);
        }
        if (mine != null) {
            transaction.hide(mine);
        }
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

    <FrameLayout
            android:background="#f9f4dc"
            android:id="@+id/main_activity_container"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"/>

    <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent">

        <com.google.android.material.bottomnavigation.BottomNavigationView
                android:id="@+id/main_activity_bottom"
                app:menu="@menu/bottom_menu"
                android:background="@color/primary_color"
                android:layout_width="match_parent"
                app:itemTextColor="@color/white"
                android:layout_height="wrap_content" />
    </FrameLayout>

    <TextView
            android:layout_width="match_parent"
            android:layout_height="10dp"
            android:background="@color/primary_color"/>


</androidx.appcompat.widget.LinearLayoutCompat>
```

## Menu

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
            android:id="@+id/tab_home"
            android:icon="@drawable/bar_home"
            android:title="@string/home"/>

    <item
            android:id="@+id/tab_car"
            android:icon="@drawable/bar_car"
            android:title="@string/car"/>

    <item
            android:id="@+id/tab_order"
            android:icon="@drawable/bar_order"
            android:title="@string/order"/>

    <item
            android:id="@+id/tab_mine"
            android:icon="@drawable/bar_mine"
            android:title="@string/mine"/>

</menu>
```

## 主页

```java
/**
 * 主页
 */
public class HomeFragment extends Fragment {

    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        return rootView;
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".fragment.HomeFragment">

    <TextView android:layout_width="match_parent"
              android:layout_height="30dp"
              android:background="@color/primary_color"/>

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/home"
            android:background="@color/primary_color"/>

    <TextView
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/home"/>

</androidx.appcompat.widget.LinearLayoutCompat>
```

## 购物车

```java
/**
 * 购物车
 */
public class CarFragment extends Fragment {

    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_car, container, false);
        }

        return rootView;
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".fragment.CarFragment">

    <TextView android:layout_width="match_parent"
              android:layout_height="30dp"
              android:background="@color/primary_color"/>

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/car"
            android:background="@color/primary_color"/>

    <TextView
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/car"/>

</androidx.appcompat.widget.LinearLayoutCompat>
```


## 订单
```java
/**
 * 订单
 */
public class OrderFragment extends Fragment {

    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order, container, false);
        }

        return rootView;
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".fragment.OrderFragment">

    <TextView android:layout_width="match_parent"
              android:layout_height="30dp"
              android:background="@color/primary_color"/>

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/order"
            android:background="@color/primary_color"/>

    <TextView
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/order"/>

</androidx.appcompat.widget.LinearLayoutCompat>
```

## 我的
```java
/**
 * 我的
 */
public class MineFragment extends Fragment {
    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        }

        return rootView;
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".fragment.MineFragment">

    <TextView android:layout_width="match_parent"
              android:layout_height="30dp"
              android:background="@color/primary_color"/>

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/mine"
            android:background="@color/primary_color"/>

    <TextView
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/mine"/>

</androidx.appcompat.widget.LinearLayoutCompat>
```

## 效果

> 实现点击不同的 bottom 选项展示相对应的 Fragment

![image-20231114131404190](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231114131404190.png)

# 2.2 登录、注册

- 通过 **SharedPreferences** 存储已登录用户信息实现 **记住密码** 功能
- 使用 **SQLite** 管理数据
- **Dialog** 通知框

## UserInfo

```groovy
dependencies {
    //    添加 Lombok
    implementation 'org.projectlombok:lombok:1.18.28'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
}
```

```java
/**
 * 用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    /**
     * id
     */
    private Integer user_id;
    /**
     * username
     */
    private String username;
    /**
     * nickname
     */
    private String nickname;
    /**
     * email
     */
    private String email;
    /**
     * password
     */
    private String password;

    /**
     * 当前登录用户信息
     */
    @Getter
    @Setter
    public static UserInfo CurrentUserInfo;

}
```

## UserDbHelper

### init 初始化 DB

```java
/**
 * 用户 DB
 */
public class UserDbHelper extends SQLiteOpenHelper {
    /**
     * sHelper
     */
    private static UserDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "user.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static UserDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new UserDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    /**
     * 初始化 DB
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user_table(user_id integer primary key autoincrement, " +
                "username text," +
                "nickname text," +
                "email text," +
                "password text" +
                ")");
        // init-data (sql)
        db.execSQL("insert into user_table values(null,'CaoBei','CaoBei','1203952894@qq.com','2309312103')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
```

### login 登录

```java
    /**
     * 根据用户名或邮箱查找用户
     * @param text 用户名或邮箱
     * @return 用户信息
     */
    @SuppressLint("Range")
    public UserInfo loadUserInfoByEmailOrUsername(String text) {
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;
        String sql = "select user_id,username,nickname,email,password from user_table where username=? or email=?";
        String[] selectionArgs = {text, text};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            userInfo = new UserInfo(user_id, username, nickname, email, password);
        }
        cursor.close();
        db.close();
        return userInfo;
    }
```

### register 注册

```java

    /**
     * 注册用户
     * @param username 用户名
     * @param nickname 昵称
     * @param email 邮箱
     * @param password 密码
     * @return count (0 失败 | 1 成功)
     */
    public int register(String username, String nickname, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("nickname", nickname);
        values.put("email", email);
        values.put("password", password);

        String nullColumnHack = "values(null,?,?,?,?)";

        int insert = (int) db.insert("user_table", nullColumnHack, values);
        db.close();
        return insert;
    }
```

## 登录

```java
/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity {
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
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "未注册", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        android:background="#f9f4dc"
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:ignore="ContentDescription"
        tools:context=".activity.LoginActivity">

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:title="@string/app_name"
            android:background="@color/primary_color"
            app:subtitleTextColor="@color/white"/>

    <TextView
            android:layout_marginTop="15dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textSize="20sp"
            android:text="@string/login"
            android:textColor="@color/black"
            android:gravity="center"/>

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_margin="26dp">

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:scaleType="center"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/username"/>

            <EditText
                    android:id="@+id/login_activity_et_username"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_username"
                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textEmailAddress|text"/>


        </androidx.appcompat.widget.LinearLayoutCompat>

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginTop="10dp"
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/password"/>

            <EditText
                    android:id="@+id/login_activity_et_pwd"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_pwd"

                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textPassword"/>


        </androidx.appcompat.widget.LinearLayoutCompat>

        <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

            <CheckBox
                    android:id="@+id/login_activity_cb_save_pwd"
                    android:layout_width="wrap_content"
                    android:text="@string/save_pwd"
                    android:textColor="@color/primary_color"
                    android:layout_centerVertical="true"
                    android:layout_height="wrap_content"/>

            <TextView
                    android:id="@+id/login_activity_tx_register"
                    android:layout_marginTop="10dp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_centerVertical="true"
                    android:layout_alignParentEnd="true"
                    android:layout_gravity="right"
                    android:textColor="@color/primary_color"
                    android:text="@string/does_not_register" tools:ignore="RelativeOverlap,RtlHardcoded"/>
        </RelativeLayout>

        <Button
                android:id="@+id/login_activity_btn_login"
                android:layout_marginTop="20dp"
                android:layout_width="match_parent"
                android:text="@string/login"
                android:layout_height="50dp"/>


    </androidx.appcompat.widget.LinearLayoutCompat>

</androidx.appcompat.widget.LinearLayoutCompat>
```

## 注册

```java
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
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        android:background="#f9f4dc"
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent" xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        tools:ignore="ContentDescription"
        tools:context=".activity.RegisterActivity">

    <androidx.appcompat.widget.Toolbar
            android:id="@+id/register_activity_toolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:title="@string/register"
            app:navigationIcon="@drawable/baseline_arrow_back_24"
            android:background="@color/primary_color"
            app:subtitleTextColor="@color/white"/>

    <TextView
            android:layout_marginTop="15dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textSize="20sp"
            android:text="@string/register"
            android:textColor="@color/black"
            android:gravity="center"/>

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_margin="26dp">

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/username"/>

            <EditText
                    android:id="@+id/register_activity_et_username"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_username"
                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textAutoComplete"/>


        </androidx.appcompat.widget.LinearLayoutCompat>
        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginTop="10dp"
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:orientation="horizontal"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/email"/>

            <EditText
                    android:id="@+id/register_activity_et_email"
                    android:layout_height="match_parent"
                    android:layout_width="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_email"
                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textEmailAddress"
                    tools:ignore="TextFields"/>

        </androidx.appcompat.widget.LinearLayoutCompat>
        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginTop="10dp"
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/password"/>

            <EditText
                    android:id="@+id/register_activity_et_pwd"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_pwd"
                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textPassword"/>


        </androidx.appcompat.widget.LinearLayoutCompat>

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginTop="10dp"
                android:layout_width="match_parent"
                android:background="@drawable/et_bg"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:layout_height="50dp">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/password"/>

            <EditText
                    android:id="@+id/register_activity_et_pwd_twice"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_marginStart="10dp"
                    android:hint="@string/input_pwd_twice"
                    android:textSize="14sp"
                    android:background="@null" android:autofillHints="" android:inputType="textPassword"/>


        </androidx.appcompat.widget.LinearLayoutCompat>


        <Button
                android:id="@+id/register_activity_btn_register"
                android:layout_marginTop="20dp"
                android:layout_width="match_parent"
                android:text="@string/register"
                android:layout_height="50dp"/>
    </androidx.appcompat.widget.LinearLayoutCompat>


</androidx.appcompat.widget.LinearLayoutCompat>
```

## 效果

> 通过 DB 管理数据，实现登录、注册、记住我 (SP) 等功能

- 登录

![image-20231114134433385](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231114134433385.png)

- 注册

![image-20231114134815153](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231114134815153.png)

![image-20231114133117776](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231114133117776.png)

- 记住密码

![image-20231114134610658](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231114134610658.png)
