# Day03

- 订单界面 UI + 代码 实现
- 个人中心
  - UpdatePwd 
  - AboutMe + LICENSE
  - CallMe
- 启动页
- 通知

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120203421077.png" alt="image-20231120203421077" style="zoom: 67%;" />

# 1.1 订单界面

## 支付

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
                                              android:layout_width="match_parent"
                                              android:layout_height="match_parent"
                                              xmlns:app="http://schemas.android.com/apk/res-auto">

    <androidx.appcompat.widget.LinearLayoutCompat android:layout_width="match_parent"
                                                  android:layout_height="wrap_content"
                                                  android:orientation="vertical"
                                                  android:layout_margin="26dp">

            <ImageView android:layout_width="125dp" android:layout_height="125dp"
                       android:layout_gravity="center_horizontal"
                       android:src="@drawable/er_code"/>


        
        <androidx.appcompat.widget.LinearLayoutCompat android:layout_width="wrap_content"
                                                      android:layout_gravity="center_horizontal"
                                                      android:layout_height="wrap_content">

            <ImageView android:layout_width="20dp" android:layout_height="20dp"
                       android:layout_gravity="bottom"
                       android:layout_marginBottom="5dp"
                       android:src="@drawable/money"/>
            
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                      android:id="@+id/pay_dialog_tx_money"
                      android:textSize="24sp"
                      android:textColor="@color/primary_color"
                      android:text="@string/money"
                      android:textStyle="bold"/>


        </androidx.appcompat.widget.LinearLayoutCompat>

        <EditText android:layout_width="match_parent" android:layout_height="50dp"
                  android:id="@+id/pay_dialog_et_address"
                  android:textSize="14sp"
                  android:hint="@string/inout_address"/>

        <EditText android:layout_width="match_parent" android:layout_height="50dp"
                  android:id="@+id/pay_dialog_et_mobile"
                  android:textSize="14sp"
                  android:inputType="phone"
                  android:maxLength="11"

                  android:hint="@string/inout_mobile"/>

    </androidx.appcompat.widget.LinearLayoutCompat>

</androidx.appcompat.widget.LinearLayoutCompat>
```

```java
        btn_buy.setOnClickListener(v -> {
            UserInfo userInfo = UserInfo.getCurrentUserInfo();

            if (userInfo != null) {
                List<CarInfo> carInfoList = CarDbHelper.getInstance(getActivity()).findAll(userInfo.getUsername());
                if (carInfoList.isEmpty()) {
                    Toast.makeText(getActivity(), "当前购物车为空", Toast.LENGTH_SHORT).show();
                } else {
                    View pay_view = LayoutInflater.from(getActivity()).inflate(R.layout.pay_dialog_layout, null);
                    EditText et_address = pay_view.findViewById(R.id.pay_dialog_et_address);
                    EditText et_mobile = pay_view.findViewById(R.id.pay_dialog_et_mobile);
                    TextView tx_money = pay_view.findViewById(R.id.pay_dialog_tx_money);

                    tx_money.setText(tx_total.getText().toString());

                    new AlertDialog.Builder(getActivity())
                            .setTitle("支付")
                            .setView(pay_view)
                            .setNegativeButton("取消", (dialog, which) -> Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show())
                            .setPositiveButton("确定", (dialog, which) -> {
                                String address = et_address.getText().toString();
                                String mobile = et_mobile.getText().toString();

                                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(mobile)){
                                    Toast.makeText(getActivity(), "请先完善信息", Toast.LENGTH_SHORT).show();
                                }else {
                                    OrderDbHelper.getInstance(getActivity()).insertByAll(carInfoList, address, mobile);
                                    carInfoList.forEach(carInfo -> CarDbHelper.getInstance(getActivity()).delete(carInfo.getCar_id() + ""));
                                    loadData();
                                    Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                                }

                            }).create().show();

                }
            }
        });
```





![image-20231120204200094](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120204200094.png)

## OrderFragment

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

    <RelativeLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:orientation="vertical"
                android:layout_height="match_parent">

            <androidx.recyclerview.widget.RecyclerView
                    android:id="@+id/order_fragment_recycleView"
                    tools:listitem="@layout/order_list_item"
                    app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"/>
        </androidx.appcompat.widget.LinearLayoutCompat>


    </RelativeLayout>


</androidx.appcompat.widget.LinearLayoutCompat>
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools" xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">


    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_margin="10dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
        <androidx.cardview.widget.CardView android:layout_width="80dp" android:layout_height="80dp"
                                           android:layout_marginLeft="10dp"
                                           app:cardCornerRadius="40dp">
            <ImageView
                    android:id="@+id/order_list_item_iv_01"
                    android:src="@drawable/p_0_1"
                    android:scaleType="centerCrop"
                    android:layout_width="90dp"
                    android:layout_height="90dp" tools:ignore="ContentDescription"/>
        </androidx.cardview.widget.CardView>


        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginLeft="10dp"
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

            <TextView
                    android:id="@+id/order_list_item_tv_title"
                    android:maxLines="2"
                    android:textColor="#333"
                    android:text="@string/title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="wrap_content"
                    android:layout_marginTop="10dp"
                    android:layout_height="wrap_content">

                <ImageView
                        android:src="@drawable/money"
                        android:layout_gravity="bottom"
                        android:layout_marginBottom="4dp"
                        android:layout_width="20dp"
                        android:layout_height="20dp" tools:ignore="ContentDescription"/>
                <TextView
                        android:id="@+id/order_list_item_tv_price"
                        android:text="@string/money"
                        android:textColor="@color/primary_color"
                        android:textSize="20sp"
                        android:textStyle="bold"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"/>

            </androidx.appcompat.widget.LinearLayoutCompat>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_gravity="right"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content">


                <TextView
                        android:id="@+id/order_list_item_tv_count"
                        android:layout_width="wrap_content"
                        android:text="@string/num"
                        android:layout_marginStart="10dp"
                        android:layout_height="wrap_content"/>


            </androidx.appcompat.widget.LinearLayoutCompat>

        </androidx.appcompat.widget.LinearLayoutCompat>

    </androidx.appcompat.widget.LinearLayoutCompat>

    <TextView android:layout_width="350dp" android:layout_height="wrap_content"
              android:id="@+id/order_list_item_tv_address"
              android:text="@string/address"
              android:textSize="12sp"
              android:textStyle="bold"
              android:background="@color/primary_color"
              android:textColor="@color/black"
              android:padding="10dp"/>


</androidx.appcompat.widget.LinearLayoutCompat>
```

```java
/**
 * 订单
 */
public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order, container, false);
        }

        recyclerView = rootView.findViewById(R.id.order_fragment_recycleView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderAdapter = new OrderAdapter();
        recyclerView.setAdapter(orderAdapter);

        orderAdapter.setOrderAdapterOnItemClickListener((orderInfo, position) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("确定删除吗？")
                    .setPositiveButton("确认", (dialog, which) -> {
                        OrderDbHelper.getInstance(getActivity()).delete(orderInfo.getOrder_id() + "");
                        loadData();
                    })
                    .setNegativeButton("取消", (dialog, which) -> Toast.makeText(getActivity(), "取消删除", Toast.LENGTH_SHORT).show()).create().show();
        });

        loadData();


    }

    public void loadData() {
        UserInfo userInfo = UserInfo.getCurrentUserInfo();
        if (userInfo != null) {
            List<OrderInfo> orderInfoList = OrderDbHelper.getInstance(getActivity()).findAll(userInfo.getUsername());
            orderAdapter.setDataList(orderInfoList);
        }

    }
}
```

```java
            case 2:
                if (order == null) {
                    order = new OrderFragment();
                    transaction.add(R.id.main_activity_container, order);
                } else {
                    transaction.show(order);
                    order.loadData();
                }
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120204137289.png" alt="image-20231120204137289" style="zoom:67%;" />

# 1.2 个人中心

## MineFragment

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

    <LinearLayout android:layout_width="match_parent" android:layout_height="100dp"
                  android:gravity="center"
                  android:background="@color/primary_color">

        <androidx.cardview.widget.CardView android:layout_width="80dp" android:layout_height="80dp"
                                           android:layout_marginLeft="10dp"
                                           app:cardCornerRadius="40dp">

            <ImageView android:layout_width="80dp" android:layout_height="80dp"
                       android:src="@drawable/avatar"/>

        </androidx.cardview.widget.CardView>

        <androidx.appcompat.widget.LinearLayoutCompat android:layout_width="wrap_content"
                                                      android:layout_height="wrap_content"
                                                      android:layout_marginLeft="10dp"
                                                      android:orientation="vertical">
            
            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                      android:id="@+id/fragment_mine_tv_username"
                      android:text="@string/cb"
                      android:textColor="@color/white"
                      android:textStyle="bold"
                      android:textSize="16sp"/>

            <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                    android:id="@+id/fragment_mine_tv_nickname"
                    android:text="@string/about_me"
                      android:textColor="@color/white"
                      android:textStyle="bold"
                      android:textSize="16sp"/>
            
        </androidx.appcompat.widget.LinearLayoutCompat>
    </LinearLayout>

<View android:layout_width="match_parent" android:layout_height="20dp"
      android:background="#F5F5F5"/>
                         
    <RelativeLayout
            android:id="@+id/fragment_mine_relative_layout_update_pwd"
            android:layout_width="match_parent" android:layout_height="48dp"
                    android:paddingLeft="10dp"
                    android:paddingRight="10dp">

        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                  android:layout_centerVertical="true"
                  android:text="@string/modify_pwd"/>

        <ImageView android:layout_width="25dp" android:layout_height="25dp"
                   android:layout_centerVertical="true"
                   android:layout_alignParentRight="true"
                   android:src="@drawable/back"/>

        <View android:layout_width="match_parent" android:layout_height="1dp"
              android:layout_alignParentBottom="true"
              android:background="#F5F5F5"/>

    </RelativeLayout>

    <RelativeLayout android:layout_width="match_parent" android:layout_height="48dp"
            android:id="@+id/fragment_mine_relative_layout_about_app"
            android:paddingLeft="10dp"
                    android:paddingRight="10dp">

        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                  android:layout_centerVertical="true"
                  android:text="@string/about_app"/>

        <ImageView android:layout_width="25dp" android:layout_height="25dp"
                   android:layout_centerVertical="true"
                   android:layout_alignParentRight="true"
                   android:src="@drawable/back"/>

        <View android:layout_width="match_parent" android:layout_height="1dp"
              android:layout_alignParentBottom="true"
              android:background="#F5F5F5"/>

    </RelativeLayout>


    <View android:layout_width="match_parent" android:layout_height="20dp"
          android:background="#F5F5F5"/>

    <RelativeLayout android:layout_width="match_parent" android:layout_height="48dp"

            android:id="@+id/fragment_mine_relative_layout_license"

            android:paddingLeft="10dp"
                    android:paddingRight="10dp">

        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                  android:layout_centerVertical="true"
                  android:text="@string/license"/>

        <ImageView android:layout_width="25dp" android:layout_height="25dp"
                   android:layout_centerVertical="true"
                   android:layout_alignParentRight="true"
                   android:src="@drawable/back"/>

        <View android:layout_width="match_parent" android:layout_height="5dp"
              android:layout_alignParentBottom="true"
              android:background="#F5F5F5"/>

    </RelativeLayout>

    <RelativeLayout android:layout_width="match_parent" android:layout_height="48dp"
            android:id="@+id/fragment_mine_relative_layout_call_me"
            android:paddingLeft="10dp"
                    android:paddingRight="10dp">

        <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
                  android:layout_centerVertical="true"
                  android:text="@string/call_me"/>

        <ImageView android:layout_width="25dp" android:layout_height="25dp"
                   android:layout_centerVertical="true"
                   android:layout_alignParentRight="true"
                   android:src="@drawable/back"/>

        <View android:layout_width="match_parent" android:layout_height="5dp"
              android:layout_alignParentBottom="true"
              android:background="#F5F5F5"/>

    </RelativeLayout>

    <View android:layout_width="match_parent" android:layout_height="5dp"
          android:layout_alignParentBottom="true"
          android:background="#F5F5F5"/>
    
<Button android:layout_width="match_parent" android:layout_height="50dp"
        android:id="@+id/mine_fragment_btn_logout"
          android:gravity="center"
          android:textSize="16sp"
          android:textStyle="bold"
          android:background="@drawable/et_bg"
          android:textColor="@color/white"
android:text="@string/logout"/>
</androidx.appcompat.widget.LinearLayoutCompat>
```

```java
/**
 * 我的
 */
public class MineFragment extends Fragment {
    private View rootView;
    private TextView tv_username;
    private TextView tv_nickname;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            tv_username = rootView.findViewById(R.id.fragment_mine_tv_username);
            tv_nickname = rootView.findViewById(R.id.fragment_mine_tv_nickname);

            rootView.findViewById(R.id.fragment_mine_relative_layout_update_pwd).setOnClickListener(v -> new AlertDialog.Builder(getContext())
                    .setTitle("前往修改密码 ?")
                    .setNegativeButton("取消",(dialog, which) -> Toast.makeText(getActivity(), "已取消", Toast.LENGTH_SHORT).show())
                    .setPositiveButton("确定",(dialog, which) -> {

                        startActivityForResult(new Intent(getActivity(), UpdatePwdActivity.class),1000);

                    }).create().show());

            rootView.findViewById(R.id.mine_fragment_btn_logout).setOnClickListener(v -> new AlertDialog.Builder(getContext())
                    .setTitle("确认退出 ?")
                    .setNegativeButton("取消",(dialog, which) -> Toast.makeText(getActivity(), "取消退出", Toast.LENGTH_SHORT).show())
                    .setPositiveButton("确定",(dialog, which) -> {

                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    }).create().show());

            rootView.findViewById(R.id.fragment_mine_relative_layout_about_app).setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
            rootView.findViewById(R.id.fragment_mine_relative_layout_license).setOnClickListener(v -> Toast.makeText(getActivity(), "Apache License 2.0", Toast.LENGTH_SHORT).show());
            rootView.findViewById(R.id.fragment_mine_relative_layout_call_me).setOnClickListener(v -> Toast.makeText(getActivity(), "曹蓓", Toast.LENGTH_SHORT).show());
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserInfo userInfo = UserInfo.CurrentUserInfo;
        if (userInfo != null) {
            tv_username.setText(userInfo.getUsername());
            tv_nickname.setText(userInfo.getNickname());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120204425468.png" alt="image-20231120204425468" style="zoom:67%;" />

## UpdatePwd  修改密码

### UpdatePwdActivity

```java
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
```

###  activity_update_pwd

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".activity.UpdatePwdActivity">


    <androidx.appcompat.widget.Toolbar
            android:id="@+id/update_pwd_activity_toolbar"
            app:navigationIcon="@drawable/baseline_arrow_back_24"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/update_pwd"
            android:background="@color/primary_color" />

    <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_marginLeft="25dp"
            android:layout_marginTop="50dp"
            android:layout_marginRight="25dp">

        <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:background="@drawable/et_bg">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/password" />

            <EditText
                    android:inputType="textPassword"
                    android:id="@+id/update_pwd_activity_et_pwd"
                    android:layout_gravity="center_vertical"
                    android:paddingLeft="10dp"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:background="@null"
                    android:textSize="14sp"
                    android:hint="@string/input_pwd" />

        </LinearLayout>

        <LinearLayout
                android:layout_marginTop="10dp"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:background="@drawable/et_bg">

            <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:src="@drawable/password" />

            <EditText
                    android:inputType="textPassword"
                    android:id="@+id/update_pwd_activity_et_pwd_twice"
                    android:layout_gravity="center_vertical"
                    android:paddingLeft="10dp"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:background="@null"
                    android:textSize="14sp"
                    android:hint="@string/input_pwd_twice" />

        </LinearLayout>

        <Button
                android:id="@+id/update_pwd_activity_btn_yes"
                android:layout_marginTop="15dp"
                android:layout_gravity="center"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="15sp"
                android:textStyle="bold"
                android:text="@string/yes"/>

    </LinearLayout>


</androidx.appcompat.widget.LinearLayoutCompat>
```

### DB

```java
    /**
     * 根据用户名或邮箱修改密码
     */
    public int updateUserInfoByEmailOrUsername(String text, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", password);
        int update = db.update("user_table", values, "username = ? or email = ?", new String[]{text, text});

        db.close();

        return update;
    }
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120205050928.png" alt="image-20231120205050928" style="zoom:67%;" />

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120205216387.png" alt="image-20231120205216387" style="zoom:67%;" />

## AboutMe + LICENSE

### 关于我 AboutMe

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/primary_color"

        tools:context=".activity.AboutActivity">


    <androidx.appcompat.widget.Toolbar
            android:id="@+id/about_activity_toolbar"
            app:navigationIcon="@drawable/baseline_arrow_back_24"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/about"
            android:background="@color/primary_color" />


    <androidx.cardview.widget.CardView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            app:cardCornerRadius="25dp">

        <ImageView
                android:layout_width="275dp"
                android:layout_height="275dp"
                android:src="@drawable/avatar" />

    </androidx.cardview.widget.CardView>


    <LinearLayout
            android:layout_centerHorizontal="true"
            android:layout_width="wrap_content"
            android:gravity="center"
            android:orientation="vertical"
            android:layout_height="wrap_content">

        <TextView
                android:layout_marginTop="160dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textStyle="bold"
                android:text="@string/cb" />

        <TextView
                android:layout_marginTop="10dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textStyle="bold"
                android:text="@string/about_me" />

    </LinearLayout>


</RelativeLayout>
```

```java
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
```

```java
            rootView.findViewById(R.id.fragment_mine_relative_layout_about_app).setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));

```

### LICENSE

```java
            rootView.findViewById(R.id.fragment_mine_relative_layout_license).setOnClickListener(v -> Toast.makeText(getActivity(), "Apache License 2.0", Toast.LENGTH_SHORT).show());

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120204804203.png" alt="image-20231120204804203" style="zoom:67%;" />

## CallMe

```java
            rootView.findViewById(R.id.fragment_mine_relative_layout_call_me).setOnClickListener(v -> Toast.makeText(getActivity(), "曹蓓", Toast.LENGTH_SHORT).show());

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120204831275.png" alt="image-20231120204831275" style="zoom:67%;" />

## 1.3 启动页

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/primary_color"
        tools:context=".activity.WelcomeActivity">

    <androidx.cardview.widget.CardView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:cardElevation="0dp"
            android:backgroundTint="#80000000"
            android:layout_margin="20dp"
            android:layout_alignParentRight="true"
            app:cardCornerRadius="25dp">

        <TextView
                android:id="@+id/welcome_activity_tv_second"
                android:layout_width="80dp"
                android:layout_height="50dp"
                android:textColor="@color/white"
                android:gravity="center"
                android:text="3s"
                android:textSize="16sp" />

    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            app:cardCornerRadius="25dp">

        <ImageView
                android:layout_width="110dp"
                android:layout_height="110dp"
                android:src="@drawable/avatar" />

    </androidx.cardview.widget.CardView>

    <LinearLayout
            android:layout_centerHorizontal="true"
            android:layout_width="wrap_content"
            android:gravity="center"
            android:orientation="vertical"
            android:layout_height="wrap_content">

        <TextView
                android:layout_marginTop="175dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textStyle="bold"
                android:text="@string/cb" />

        <TextView
                android:layout_marginTop="10dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textStyle="bold"
                android:text="@string/about_me" />

    </LinearLayout>




</RelativeLayout>
```

```java
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
```

```java
        <activity
                android:name=".activity.WelcomeActivity"
                android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120203813204.png" alt="image-20231120203813204" style="zoom:67%;" />

## 1.4 通知

```java
    private static final String CHANNEL_ID = "公共通知";
 

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
```

```java
    private static final String CHANNEL_ID = "公共通知";
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
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231120205017737.png" alt="image-20231120205017737" style="zoom:67%;" />
