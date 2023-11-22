package jz.cbq.work_course_table_v2;

/**
 * 考试卡片item
 * @version 1.1.2
 */

public class examcarditem {
    private String examname , examroom;
    private int month , day;
    public examcarditem(String examname, int month, int day, String examroom){//考试界面listitem
        this.examname = examname;
        this.month = month;
        this.day = day;
        this.examroom = examroom;
    }

    public String getExamname(){
        return examname;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public String getExamroom(){
        return examroom;
    }
}
