package jz.cbq.work_note_book.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;

import jz.cbq.work_note_book.MainActivity;
import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.activity.EditActivity;
import jz.cbq.work_note_book.activity.RecycleBinActivity;
import jz.cbq.work_note_book.adapter.NoteRecyclerViewAdapter;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.Note;

/**
 * 笔记 Fragment
 *
 * @author cbq
 * @date 2023/11/20 22:48
 * @since 1.0.0
 */
public class NoteFragment extends Fragment {
    /**
     * rootView
     */
    private View rootView;
    /**
     * 笔记 List
     */
    private List<Note> notes;
    /**
     * 搜索框
     */
    EditText search_note;
    /**
     * RecyclerView
     */
    RecyclerView recyclerView;
    /**
     * NoteRecyclerViewAdapter 适配器
     */
    NoteRecyclerViewAdapter myRecyclerViewAdapter;
    /**
     * 添加按钮
     */
    public ImageView add_note;
    /**
     * 删除按钮
     */
    public ImageView delete_note;


    /**
     * @return NoteFragment
     */
    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 通过这个方法来初始化NoteFragment的根视图
     * 并且在通过返回键返回该Fragment所在Activity时也会调用
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View rootView 这是这个Fragment的根视图
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_note, container, false);
            initView();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        initView();
    }

    /**
     * 初始化该 Fragment 的控件
     */
    private void initView() {
        search_note = rootView.findViewById(R.id.search_note_editText);

        search_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = search_note.getText().toString();
                notes = queryNotesData(keyWord);
                bindData();
            }
        });

        notes = getNotesData();
        bindData();

        add_note = rootView.findViewById(R.id.add_note);
        add_note.setOnClickListener(view -> startActivity(new Intent(getActivity(), EditActivity.class)));

        delete_note = rootView.findViewById(R.id.delete_node);
        delete_note.setOnClickListener(view -> startActivity(new Intent(getActivity(), RecycleBinActivity.class)));

    }

    /**
     * 将数据绑定到 RecyclerView 中
     */
    public void bindData() {
        recyclerView  = rootView.findViewById(R.id.note_recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myRecyclerViewAdapter = new NoteRecyclerViewAdapter(notes, getActivity());
        myRecyclerViewAdapter.setOnItemClickListener((position, title, content, date_created, note_id) -> {
            Bundle bundle = new Bundle();

            bundle.putString("note_title", title);
            bundle.putString("note_content", content);
            bundle.putString("date_created", date_created);
            bundle.putString("note_id", note_id);

            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        recyclerView.setAdapter(myRecyclerViewAdapter);
    }

    /**
     * 获取全部 Note 数据
     * @return List<Note>
     */
    public List<Note> getNotesData() {
        return NoteBookDBOperator.getNotesData(getActivity(), 0);
    }

    /**
     * 查询符合关键字的 note 数据
     * @param keyWord 关键字
     * @return List<Note>
     */
    public List<Note> queryNotesData(String keyWord) {
        return NoteBookDBOperator.queryNotesData(getActivity(), keyWord, 0);
    }
}
