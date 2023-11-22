package jz.cbq.work_course_table_v2;
/**
 * 添加考试至日历提醒
 * @version 1.1.2
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class examcalendarremind {
    private static String calenderurl = "content://com.android.calendar/calendars";
    private static String calendereventurl = "content://com.android.calendar/events";
    private static String calenderreminderurl = "content://com.android.calendar/reminders";
    private static String calendarsname = "crisgy";
    private static String calendarsaccountname = "crisgy@crisgy.ml";
    private static String calendarsaccounttype = "com.android.boohee";
    private static String calendarsdispalyname = "crisgy考试提醒";

    private static int checkAndAddCalendarAccount(Context context) {//添加日历账户
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    private static int checkCalendarAccount(Context context) {//查询存在账户
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calenderurl), null, null, null, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    private static long addCalendarAccount(Context context) {//添加日历账户
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, calendarsname);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, calendarsaccountname);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, calendarsaccounttype);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarsdispalyname);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, calendarsaccountname);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(calenderurl);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarsaccountname)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, calendarsaccounttype)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    public static boolean addCalendarEvent(Context context, String title, String description, int month, int day) {
        if (context == null) {
            return false;
        }
        int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return false;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        mCalendar.set(year, month - 1 , day);
        long start = mCalendar.getTime().getTime();
        mCalendar.set(year, month - 1, day + 1);//LOG发现start与end相同，实际使用时end提前一天，可能是华为的bug，应该不影响使用
        long end = mCalendar.getTime().getTime();
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        event.put("calendar_id", calId); //插入账户的id
        event.put("allDay", true);
        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");//时区
        Uri newEvent = context.getContentResolver().insert(Uri.parse(calendereventurl), event); //添加事件
        if (newEvent == null) { //添加日历事件失败直接返回
            return false;
        }
        ContentValues values = new ContentValues();//设定事件提醒
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, 1 * 24 * 60);// 提前1天提醒
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(calenderreminderurl), values);
        if(uri == null) { //添加事件提醒失败直接返回
            return false;
        }
        return true;
    }
}
