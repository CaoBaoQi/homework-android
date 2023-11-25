package jz.cbq.work_note_book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jz.cbq.work_note_book.MainActivity;
import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.Note;

/**
 * 编辑 EditActivity
 *
 * @author cbq
 * @date 2023/11/20 22:45
 * @since 1.0.0
 */
public class EditActivity extends AppCompatActivity {
    /**
     * 传来的数据
     */
    Bundle bundle;
    /**
     * 创建日期文本框
     */
    TextView date_created;
    /**
     * 一个隐藏的文本框用于标记 note 的 _id
     */
    TextView note_id;
    /**
     * 标题编辑框
     */
    EditText title;
    /**
     * 内容编辑框
     */
    EditText content;
    /**
     * 返回按钮
     */
    ImageView backButton;
    /**
     * 完成按钮
     */
    ImageView accomplishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_edit);

        note_id = findViewById(R.id.note_id);
        title = findViewById(R.id.note_title_edit);
        content = findViewById(R.id.note_content_edit);
        backButton = findViewById(R.id.back);
        accomplishButton = findViewById(R.id.accomplish);

        backButton.setOnClickListener(view -> {
            MainActivity.status = 0;
            EditActivity.this.onBackPressed();
        });

        accomplishButton.setOnClickListener(view -> {
            String s_id = note_id.getText().toString();
            String title = EditActivity.this.title.getText().toString();
            String content = EditActivity.this.content.getText().toString();

            Note note;
            if ("".equals(s_id) && (!"".equals(title) || !"".equals(content))) {
                note = new Note(title, content);
                addNote(note);
            } else if (!"".equals(s_id) && (!"".equals(title) || !"".equals(content))) {
                int _id = Integer.parseInt(s_id);
                note = new Note(_id, title, content);
                updateNote(note);
            }

            EditActivity.this.onBackPressed();
        });

        bundle = getIntent().getExtras();

        if (bundle != null) {
            date_created = findViewById(R.id.date_created);
            title.setText(bundle.getString("note_title"));
            content.setText(bundle.getString("note_content"));
            note_id.setText(bundle.getString("note_id"));
            String tips = "创建于：" + bundle.getString("date_created");
            date_created.setText(tips);
            bundle.clear();
        }
    }

    /**
     * 添加一条笔记
     *
     * @param note Note
     */
    public void addNote(Note note) {
        NoteBookDBOperator.addNote(this, note);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新一条笔记
     *
     * @param note Note
     */
    public void updateNote(Note note) {
        NoteBookDBOperator.updateNote(this, note);
        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
    }
}
