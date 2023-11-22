package jz.cbq.work_course_table_v2;
/**
 * 使用模板进行修改
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static int courseflag = 0;
    public static int examflag = 0;
    private static final int noticecourseid = 22;
    private static final int noticeexamid = 26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//菜单栏
        FloatingActionButton fab = findViewById(R.id.fab);//进入添加课程考试界面
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * 添加考试课程选择界面
             * @param view
             */
            @Override
            public void onClick(View view) {//选择考试or课程
                AlertDialog.Builder courseexamdialog = new AlertDialog.Builder(MainActivity.this).setTitle("请选择需要添加的对象").setPositiveButton("课程", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//进入课程添加
                        Intent addcourseintent = new Intent(MainActivity.this, addcourse.class);
                        startActivity(addcourseintent);
                    }
                }).setNegativeButton("考试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//进入考试添加界面
                        Intent addexamintent = new Intent(MainActivity.this,addexam.class);
                        startActivity(addexamintent);
                    }
                });
                courseexamdialog.create().show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_settings){
            Intent settingsintent = new Intent(MainActivity.this,settings.class);//进入设置界面
            startActivity(settingsintent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     *结束前发出提醒
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (courseflag == 1) {
            Intent noticeintent = new Intent(this, noticeactivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noticeintent, PendingIntent.FLAG_IMMUTABLE);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(notificationChannel);
                Notification notification = new Notification.Builder(this, getString(R.string.app_name))
                        .setContentTitle("课程提醒").setContentText("今天有课，记住时间地点了吗？").setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo).setContentIntent(pendingIntent).setAutoCancel(true).build();
                manager.notify(noticecourseid, notification);
            } else {
                Notification notification = new Notification.Builder(this, getString(R.string.app_name))
                        .setContentTitle("课程提醒").setContentText("今天有课，记住时间地点了吗？").setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo).setContentIntent(pendingIntent).setAutoCancel(true).build();
                manager.notify(noticecourseid, notification);
            }
        }
        if (examflag == 1) {
            Intent noticeintent = new Intent(this, noticeactivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noticeintent, PendingIntent.FLAG_IMMUTABLE);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(notificationChannel);
                Notification notification = new Notification.Builder(this, getString(R.string.app_name))
                        .setContentTitle("考试提醒").setContentText("今天有考试，记住地点了吗？").setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo).setContentIntent(pendingIntent).setAutoCancel(true).build();
                manager.notify(noticecourseid, notification);
            } else {
                Notification notification = new Notification.Builder(this, getString(R.string.app_name))
                        .setContentTitle("考试提醒").setContentText("今天有考试，记住地点了吗？").setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo).setContentIntent(pendingIntent).setAutoCancel(true).build();
                manager.notify(noticecourseid, notification);
            }
        }
    }

}
