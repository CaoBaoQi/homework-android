package jz.cbq.work_note_book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import java.util.Calendar;

import jz.cbq.work_note_book.MainActivity;
import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.db.op.InAbeyanceDBOperator;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.InAbeyance;
import jz.cbq.work_note_book.utils.AlarmUtil;
import jz.cbq.work_note_book.utils.DateUtil;

/**
 * 待办事项 InAbeyanceDialogActivity
 *
 * @author cbq
 * @date 2023/11/20 22:53
 * @since 1.0.0
 */
public class InAbeyanceDialogActivity extends AppCompatActivity {
    /**
     * 待办的 _id
     */
    TextView _id;
    /**
     * 代办的内容
     */
    EditText content;
    /**
     * 设置提醒按钮
     */
    LinearLayout set_remind;
    /**
     * 闹钟设置状态
     */
    ImageView setting_status;
    /**
     * 提醒日期
     */
    TextView date_remind;
    /**
     * 完成按钮
     */
    TextView accomplish;
    /**
     * 传入的数据
     */
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_abeyance_dialog);

        _id = findViewById(R.id.in_abeyance_id);
        content = findViewById(R.id.in_abeyance_edittext);
        set_remind = findViewById(R.id.set_remind);

        set_remind.setOnClickListener(view -> {
            if (!"".equals(date_remind.getText().toString())) {
                setting_status.setSelected(true);
            }
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.SECOND, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2030, 12, 31, 23, 59);

            TimePickerView pickerView = new TimePickerView.Builder(
                    InAbeyanceDialogActivity.this,
                    (date, v) -> {
                        String time = DateUtil.getFormatTime(date);
                        setting_status.setSelected(true);
                        date_remind.setText(time);
                    })
                    .setType(new boolean[]{true, true, true, true, true, false})
                    .setCancelText("取消")
                    .setCancelColor(getResources().getColor(R.color.yellow))
                    .setSubmitText("确定")
                    .setSubmitColor(getResources().getColor(R.color.yellow))
                    .setTitleText("设置提醒日期")
                    .setTitleBgColor(getResources().getColor(R.color.white))
                    .isCyclic(true)
                    .setRangDate(startTime, endTime)
                    .isCenterLabel(false)
                    .setOutSideCancelable(true)
                    .build();
            pickerView.show();
        });
        setting_status = findViewById(R.id.setting_status);
        date_remind = findViewById(R.id.date_remind_show);
        accomplish = findViewById(R.id.input_accomplish);
        accomplish.setOnClickListener(view -> {
            String content_str = content.getText().toString();
            String date_remind_str = date_remind.getText().toString();
            String _id_str = _id.getText().toString();

            InAbeyance inAbeyance;

            if ("".equals(_id_str)) {
                String date_created_str = DateUtil.getCurrentTime();
                inAbeyance = new InAbeyance(content_str, date_remind_str, date_created_str);
                int _id = addInAbeyance(inAbeyance);
                if (!"".equals(date_remind_str)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AlarmUtil.setAlarm(getApplicationContext(),
                                DateUtil.getTimeMillis(date_remind_str),
                                _id, content_str);
                    }
                }
                Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                int _id_int = Integer.parseInt(_id_str);
                inAbeyance = new InAbeyance(_id_int, content_str,
                        date_remind_str);
                updateInAbeyance(inAbeyance);
                if (!"".equals(date_remind_str)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        AlarmUtil.setAlarm(getApplicationContext(),
                                DateUtil.getTimeMillis(date_remind_str),
                                _id_int, content_str);
                    }
                }
                Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
            MainActivity.status = 1;
            onBackPressed();
        });
        accomplish.setClickable(false);
        content.requestFocus();
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditText editText = InAbeyanceDialogActivity.this.content;
                TextView textView = InAbeyanceDialogActivity.this.accomplish;
                String content = editText.getText().toString();
                if ("".equals(content)) {
                    textView.setTextColor(getResources().getColor(R.color.mid_grey));
                    textView.setClickable(false);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.yellow));
                    textView.setClickable(true);
                }
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null) {
            _id.setText(bundle.getString("_id"));
            content.setText(bundle.getString("content"));
            content.setSelection(content.getText().length());
            if (!"".equals(bundle.getString("date_remind"))) {
                setting_status.setSelected(true);
                date_remind.setText(bundle.getString("date_remind"));
            }
        }
    }

    /**
     * 添加一条待办
     *
     * @param inAbeyance InAbeyance
     * @return _id
     */
    public int addInAbeyance(InAbeyance inAbeyance) {
        return InAbeyanceDBOperator.add_in_abeyance(getApplicationContext(), inAbeyance);
    }

    /**
     * 更新一条待办
     *
     * @param inAbeyance InAbeyance
     */
    public void updateInAbeyance(InAbeyance inAbeyance) {
        InAbeyanceDBOperator.updateInAbeyance(getApplicationContext(), inAbeyance);
    }
}
