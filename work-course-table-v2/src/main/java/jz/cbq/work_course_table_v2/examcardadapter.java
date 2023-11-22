package jz.cbq.work_course_table_v2;
/**
 * 考试卡片adapter
 * @version 1.1.3
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class examcardadapter extends ArrayAdapter<examcarditem> {

    private int resourceId;

    public examcardadapter(Context context, int textViewResourceId, List<examcarditem> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        examcarditem examcarditem = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView examcardname = view.findViewById(R.id.examcardname_textview);
        TextView examcardcontent = view.findViewById(R.id.examcardcontent_textview);//包括考试时间加地点
        examcardname.setText(examcarditem.getExamname());
        int month = examcarditem.getMonth();
        String months = "" + month;
        int day = examcarditem.getDay();
        String days = "" + day;
        String room = examcarditem.getExamroom();
        examcardcontent.setText("时间："+months+"月"+days+"日\n地点："+room);
        return view;
    }
}
