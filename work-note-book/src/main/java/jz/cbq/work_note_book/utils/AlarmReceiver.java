package jz.cbq.work_note_book.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

import jz.cbq.work_note_book.MainActivity;
import jz.cbq.work_note_book.R;

/**
 * AlarmReceiver 广播接收器
 *
 * @author cbq
 * @date 2023/11/20 22:35
 * @since 1.0.0
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * onReceive
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

        Notification notification;
        Bundle bundle = intent.getExtras();

        NotificationChannel notificationChannel = new NotificationChannel(
                "TODOAlarm", "待办提醒",
                NotificationManager.IMPORTANCE_HIGH); // 弹出并在通知栏显示

        notificationManager.createNotificationChannel(notificationChannel);

        if (bundle != null) {
            notification = new Notification.Builder(context, "TODOAlarm")
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                    .setTicker("待办提醒")
                    .setContentTitle("待办事项提醒")
                    .setContentText(bundle.getString("content"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(3, notification);
        } else {
            Toast.makeText(context, "内部错误" + " |AlarmReceiver(onReceive)", Toast.LENGTH_SHORT).show();
        }
    }
}
