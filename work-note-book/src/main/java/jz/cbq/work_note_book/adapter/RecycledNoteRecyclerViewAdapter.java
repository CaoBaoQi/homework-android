package jz.cbq.work_note_book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.Note;

/**
 * RecycledNoteRecyclerViewAdapter
 *
 * @author cbq
 * @date 2023/11/20 22:50
 * @since 1.0.0
 */
public class RecycledNoteRecyclerViewAdapter extends
        RecyclerView.Adapter<NoteRecyclerViewAdapter.MyRecyclerViewHolder> {

    List<Note> notes; // 存放已被回收的笔记
    Context context; // 上下文

    // 构造器
    public RecycledNoteRecyclerViewAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public NoteRecyclerViewAdapter.MyRecyclerViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_note, null);
        return new NoteRecyclerViewAdapter.MyRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteRecyclerViewAdapter.MyRecyclerViewHolder holder,
            @SuppressLint("RecyclerView") int position) {
        // 获取标题内容
        String title = notes.get(position).getTitle();
        String content = notes.get(position).getContent();
        if (title.length() == 0) {
            holder.note_title.setVisibility(View.GONE);
        } else {
            holder.note_title.setVisibility(View.VISIBLE);
            holder.note_title.setText(title);
        }
        if (content.length() == 0) {
            holder.note_content.setVisibility(View.GONE);
        } else {
            holder.note_content.setVisibility(View.VISIBLE);
            holder.note_content.setText(content);
        }
        // 设置笔记更新日期
        holder.note_date.setText(notes.get(position).getDate_updated());
        // 设置笔记条目单击事件监听 --> 点击将笔记还原
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreNote(position);
            }
        });
        // 为笔记条目设置长按事件监听器 --> 长按删除
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteNote(position);
                return true;
            }
        });
    }

    // 获取笔记条数
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // 还原一条笔记
    public void restoreNote(int position) {
        // 获取要删除的笔记的_id
        int _id = notes.get(position).get_id();
        // 从数据库中删除
        NoteBookDBOperator.changeNoteRecycleStatus(context, _id);
        notifyItemRemoved(position); // 通知哪一条数据数据被回收
        notes.remove(position); // 在条目中移除被回收的笔记
        // 通知适配器受到影响的position的位置
        notifyItemRangeChanged(position, notes.size() - position);
        Toast.makeText(context, "恢复成功", Toast.LENGTH_SHORT).show();
    }

    // 删除一条笔记
    public void deleteNote(int position) {
        // 获取要删除的笔记的_id
        int _id = notes.get(position).get_id();
        // 从数据库中删除
        NoteBookDBOperator.deleteNote(context, _id);
        notifyItemRemoved(position); // 通知哪一条数据数据被回收
        notes.remove(position); // 在条目中移除被回收的笔记
        // 通知适配器受到影响的position的位置
        notifyItemRangeChanged(position, notes.size() - position);
        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }
}

