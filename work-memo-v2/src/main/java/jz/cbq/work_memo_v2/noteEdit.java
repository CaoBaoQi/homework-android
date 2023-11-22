package jz.cbq.work_memo_v2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class noteEdit extends Activity implements OnClickListener {
    private TextView tv_date;
    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NoteDateBaseHelper DBHelper;
    public int enter_state = 0;//用来区分是新建一个note还是更改原来的note
    public String last_content;//用来获取edittext内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        InitView();
    }

    private void InitView() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        DBHelper = new NoteDateBaseHelper(this);

        //获取此时时刻时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        //接收内容和id
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        enter_state = myBundle.getInt("enter_state");
        et_content.setText(last_content);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ok) {
            SQLiteDatabase db = DBHelper.getReadableDatabase();
            // 获取edittext内容
            String content = et_content.getText().toString();

            // 添加一个新的日志
            if (enter_state == 0) {
                if (!content.equals("")) {
                    //获取此时时刻时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    String dateString = sdf.format(date);

                    //获取星期
                    Date xq = new Date();
                    SimpleDateFormat abc = new SimpleDateFormat("E");
                    String xqString = abc.format(xq);

                    //向数据库添加信息
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    values.put("date", dateString);
                    values.put("xq", xqString);
                    db.insert("note", null, values);
                    startActivity(new Intent(noteEdit.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    //finish();
                } else {
                    Toast.makeText(noteEdit.this, "请输入你的内容！", Toast.LENGTH_SHORT).show();
                }
            }
            // 查看并修改一个已有的日志
            else {
                ContentValues values = new ContentValues();
                values.put("content", content);
                db.update("note", values, "content = ?", new String[]{last_content});
                startActivity(new Intent(noteEdit.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finish();
            }
        } else if (id == R.id.btn_cancel) {
            startActivity(new Intent(noteEdit.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //finish();
        }
    }
}