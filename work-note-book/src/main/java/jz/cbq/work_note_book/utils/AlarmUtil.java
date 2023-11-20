package jz.cbq.work_note_book.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

/**
 * 闹钟工具类
 *
 * @author cbq
 * @date 2023/11/20 22:38
 * @since 1.0.0
 */
public class AlarmUtil {
    /**
     * 设置闹钟
     * @param context context
     * @param remindTime remindTime
     * @param _id _id
     * @param content content
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static void setAlarm(Context context, long remindTime,int _id, String content) {
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        intent.putExtras(bundle);

        PendingIntent sender = PendingIntent.getBroadcast(
                context, _id, intent, PendingIntent.FLAG_IMMUTABLE);

        /*
         * 注册闹钟
         * RTC_WAKEUP：指定当系统调用 System.currentTimeMillis() 方法返回的值 与 triggerAtTime 相等时
         * 启动 operation 所对应的设备（在指定的时刻，发送广播，并唤醒设备）
         */
        alarmManager.set(AlarmManager.RTC_WAKEUP, remindTime, sender);
    }

    /**
     * 取消闹钟
     * @param context context
     * @param _id _id
     */
    public static void cancelAlarm(Context context, int _id) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, _id, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
