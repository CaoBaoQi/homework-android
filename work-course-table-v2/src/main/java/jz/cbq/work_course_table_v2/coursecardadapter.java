package jz.cbq.work_course_table_v2;
/**
 *主页课程卡片adapter
 * @version 1.1.1
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class coursecardadapter extends ArrayAdapter<coursecarditem> {

    private int resourceId;

    public coursecardadapter(Context context, int textViewResourceId, List<coursecarditem> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        coursecarditem coursecarditem = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView coursecardname = view.findViewById(R.id.coursecardname_textview);
        TextView coursecardweek = view.findViewById(R.id.coursecardweek_textview);//周数和周几
        TextView coursecardtime = view.findViewById(R.id.coursecardtime_textview);//第几节
        TextView coursecardcontent = view.findViewById(R.id.coursecardcontent_textview);//其他内容包括老师和教室
        String coursename = coursecarditem.getCoursename();
        coursecardname.setText(coursename);
        int weekstart = coursecarditem.getWeekstart();
        int weekend = coursecarditem.getWeekend();
        int ofweek = coursecarditem.getOfweek();
        String weekstarts = "" + weekstart;
        String weekends = "" + weekend;
        String ofweeks;
        switch (ofweek){
            case 1:
                ofweeks = "周一" ;
                break;
            case 2:
                ofweeks = "周二" ;
                break;
            case 3:
                ofweeks = "周三" ;
                break;
            case 4:
                ofweeks = "周四" ;
                break;
            case 5:
                ofweeks = "周五" ;
                break;
            case 6:
                ofweeks = "周六" ;
                break;
            case 7:
                ofweeks = "周日" ;
                break;
            default:
                ofweeks = "ERROR";
        }
        coursecardweek.setText("第"+weekstarts+"周 - 第"+weekends +"周"+ ofweeks);
        int timestart = coursecarditem.getTimestart();
        int timeend = coursecarditem.getTimeend();
        String timestarts = "" + timestart;
        String timeends = "" + timeend;
        coursecardtime.setText("第"+timestarts+"节 - 第"+timeends+"节");
        String courseroom = coursecarditem.getCourseroom();
        String courseteacher = coursecarditem.getCourseteacher();
        coursecardcontent.setText(courseteacher+"  "+courseroom);
        return view;
    }
}
