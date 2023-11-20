package jz.cbq.work_note_book.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;

import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.adapter.RecycledNoteRecyclerViewAdapter;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.Note;

/**
 * RecycleBinActivity
 *
 * @author cbq
 * @date 2023/11/20 22:55
 * @since 1.0.0
 */
public class RecycleBinActivity extends AppCompatActivity {
    /**
     * 存放回收的笔记数据 Note List
     */
    List<Note> notes;
    /**
     * 返回按钮
     */
    ImageView back;
    /**
     * 回收笔记搜索框
     */
    EditText recycle_note_search;
    /**
     * recyclerView
     */
    RecyclerView recyclerView;

    // 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.light_grey));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_recycle_bin);
        back = findViewById(R.id.back_of_recycle_bin);

        back.setOnClickListener(view -> RecycleBinActivity.this.onBackPressed());

        recycle_note_search = findViewById(R.id.search_recycle_note_editText);
        recycle_note_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = RecycleBinActivity.this
                        .recycle_note_search.getText().toString();
                notes = findNotesByKeyWord(keyWord);
                bindData();
            }
        });
        notes = getAllNotes();
        bindData();
    }

    public void bindData() {
        recyclerView = findViewById(R.id.recycle_note_recyclerview);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecycledNoteRecyclerViewAdapter adapter =
                new RecycledNoteRecyclerViewAdapter(notes, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 查询全部已被回收的数据
     * @return List<Note>
     */
    public List<Note> getAllNotes() {
        return NoteBookDBOperator.getNotesData(this, 1);
    }

    /**
     * 按照关键字查询已被回收的笔记
     * @param keyWord 关键字
     * @return List<Note>
     */
    public List<Note> findNotesByKeyWord(String keyWord) {
        return NoteBookDBOperator.queryNotesData(this, keyWord, 1);
    }
}
