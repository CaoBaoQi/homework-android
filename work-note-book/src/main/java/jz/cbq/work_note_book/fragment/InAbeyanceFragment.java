package jz.cbq.work_note_book.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.List;

import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.activity.InAbeyanceDialogActivity;
import jz.cbq.work_note_book.adapter.InAbeyanceRecyclerViewAdapter;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.InAbeyance;

/**
 * 待办事项 Fragment
 *
 * @author cbq
 * @date 2023/11/20 22:52
 * @since 1.0.0
 */
public class InAbeyanceFragment extends Fragment {
    /**
     * 根视图 rootView
     */
    private View rootView;
    /**
     * 待办事项 List
     */
    List<InAbeyance> inAbeyances;
    /**
     * 搜索框
     */
    EditText search_inAbeyance;
    /**
     * RecyclerView
     */
    RecyclerView recyclerView;
    /**
     * InAbeyanceRecyclerViewAdapter 适配器
     */
    InAbeyanceRecyclerViewAdapter inAbeyanceRecyclerViewAdapter;
    /**
     * 添加待办按钮
     */
    ImageView add_inAbeyance;



    /**
     * 通过这个方法在外界获取 InAbeyanceFragment 实例
     * @return InAbeyanceFragment fragment
     */
    public static InAbeyanceFragment newInstance() {
        return new InAbeyanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化根视图 rootView
     * 并且在通过返回键返回该 Fragment 所在 Activity 时也会调用
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return rootView
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_in_abeyance, container, false);
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
     * 初始化根视图的控件
     */
    private void initView() {
        search_inAbeyance = rootView.findViewById(R.id.search_in_abeyance_editText);

        search_inAbeyance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = search_inAbeyance.getText().toString();
                inAbeyances = queryInAbeyancesData(keyWord);
                bindData();
            }
        });

        inAbeyances = getInAbeyancesData();
        bindData();

        add_inAbeyance = rootView.findViewById(R.id.add_in_abey_ance);
        add_inAbeyance.setOnClickListener(view -> startActivity(new Intent(getActivity(), InAbeyanceDialogActivity.class)));
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        recyclerView = rootView.findViewById(R.id.in_abeyance_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        inAbeyanceRecyclerViewAdapter = new InAbeyanceRecyclerViewAdapter(inAbeyances, getActivity());

        inAbeyanceRecyclerViewAdapter.setOnItemClickListener((position, _id, content, date_remind) -> {
            Bundle bundle = new Bundle();

            bundle.putString("_id", _id);
            bundle.putString("content", content);
            bundle.putString("date_remind", date_remind);
            Intent intent = new Intent(getActivity(), InAbeyanceDialogActivity.class);

            intent.putExtras(bundle);
            startActivity(intent);
        });

        recyclerView.setAdapter(inAbeyanceRecyclerViewAdapter);
    }

    /**
     * 获取全部 InAbeyance 数据
     * @return List<InAbeyance>
     */
    public List<InAbeyance> getInAbeyancesData() {
        return NoteBookDBOperator.getInAbeyanceData(getActivity());
    }

    /**
     * 查询符合关键字的 inAbeyance 数据
     * @param keyWord 关键字
     * @return List<InAbeyance>
     */
    public List<InAbeyance> queryInAbeyancesData(String keyWord) {
        return NoteBookDBOperator.queryInAbeyanceData(getActivity(), keyWord);
    }
}
