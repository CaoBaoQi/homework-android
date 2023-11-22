package jz.cbq.work_course_table_v2;
/**
 * 课程卡片item
 * version 1.1.1
 */

import android.util.Log;

public class coursecarditem {
    private String coursename, courseroom, courseteacher;
    private int weekstart, weekend, ofweek, timestart, timeend;

    public coursecarditem(String coursename, int weekstart, int weekend, int ofweek, int timestart, int timeend, String courseroom, String courseteacher) {//课程界面listitem
        this.coursename = coursename;
        this.courseroom = courseroom;
        this.courseteacher = courseteacher;
        this.weekstart = weekstart;
        this.weekend = weekend;
        this.ofweek = ofweek;
        this.timestart = timestart;
        this.timeend = timeend;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getCourseroom() {
        return courseroom;
    }

    public String getCourseteacher() {
        return courseteacher;
    }

    public int getWeekstart() {
        return weekstart;
    }

    public int getWeekend() {
        return weekend;
    }

    public int getOfweek() {
        return ofweek;
    }

    public int getTimestart() {
        return timestart;
    }

    public int getTimeend() {
        return timeend;
    }
}