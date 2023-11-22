package jz.cbq.work_course_table_v2;
/**
 * 统一获取时间
 * @version 1.1.3
 */

import android.content.SharedPreferences;

import java.util.Calendar;

public class timemanager extends MainActivity{

    public static int getWeekOfYear() {//获取周数
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);//获取当前周数
        return weekOfYear;
    }

    public static int getMonth() {//月份
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int getDay() {//日期
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getWeekDay(){//周几
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekday == 0){
            weekday = 7;
        }
        return weekday;
    }
}
