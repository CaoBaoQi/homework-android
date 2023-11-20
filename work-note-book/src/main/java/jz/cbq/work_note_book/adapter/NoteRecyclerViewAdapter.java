package jz.cbq.work_note_book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.Note;

/**
 * NoteRecyclerViewAdapter
 *
 * @author cbq
 * @date 2023/11/20 22:50
 * @since 1.0.0
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.MyRecyclerViewHolder> {
    /**
     *  Note List
     */
    List<Note> notes;
    /**
     * context
     */
    Context context;
    /**
     * note item 单击事件监听器
     */
    OnItemClickListener onItemClickListener;

    public NoteRecyclerViewAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_note, null);
        return new MyRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
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
        holder.note_date.setText(notes.get(position).getDate_updated());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                String title1 = notes.get(position).getTitle();
                String content1 = notes.get(position).getContent();
                String date_created = notes.get(position).getDate_created();
                String note_id = notes.get(position).get_id() + "";
                onItemClickListener.onItemClick(position, title1, content1, date_created, note_id);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            recycleNote(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * 回收一条笔记
     * @param position position
     */
    public void recycleNote(int position) {
        int _id = notes.get(position).get_id();

        NoteBookDBOperator.changeNoteRecycleStatus(context, _id);
        notifyItemRemoved(position);

        notes.remove(position);
        notifyItemRangeChanged(position, notes.size() - position);
        Toast.makeText(context, "移入回收站",Toast.LENGTH_SHORT).show();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 这是继承自 RecyclerView.ViewHolder 的
     * 自定义的 MyRecyclerViewHolder
     * 也是
     */
    public static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        /**
         * 笔记标题
         */
        final TextView note_title;
        /**
         * 笔记内容
         */
        final TextView note_content;
        /**
         * 笔记修改日期
         */
        final TextView note_date;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title = itemView.findViewById(R.id.note_title);
            note_content = itemView.findViewById(R.id.note_content);
            note_date = itemView.findViewById(R.id.note_date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String title, String content, String date_created, String note_id);
    }
}




