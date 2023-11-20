package jz.cbq.work_note_book.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author cbq
 * @date 2023/11/20 22:39
 * @since 1.0.0
 */
public class DateUtil {
    /**
     * 获取当前日期
     * @return 日期字符串
     */
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat
                = new SimpleDateFormat("MM月dd日 aa HH:mm");
        return dateFormat.format(new Date());
    }

    /**
     * 传入一个 Date 类型对象,
     * @param date date
     * @return 转换后的字符串
     */
    public static String getFormatTime(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("MM 月 dd 日 aa HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * 将 util 包下 Date 类型日期时间转换为对应的绝对时间(long 类型的毫秒数)
     * @param dateTime dateTime
     * @return 对应的绝对时间(long 类型的毫秒数)
     */
    public static long getTimeMillis(String dateTime) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat
                = new SimpleDateFormat("MM月dd日 aa HH:mm");
        try {
            Date date = dateFormat.parse(dateTime);
            date.setYear(Calendar.getInstance().get(Calendar.YEAR) - 1900);
            return date.getTime();
        } catch (ParseException e) {
            Log.e("DateUtil->", "getTimeMillis: " + "捕获异常");
        }
        return System.currentTimeMillis() + 5000;
    }
}
