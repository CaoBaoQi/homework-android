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

    /**
     * 存放已被回收的笔记
     */
    List<Note> notes;
    /**
     * context
     */
    Context context;

    public RecycledNoteRecyclerViewAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

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

        holder.itemView.setOnClickListener(view -> restoreNote(position));

        holder.itemView.setOnLongClickListener(view -> {
            deleteNote(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /**
     * 还原一条笔记
     * @param position position
     */
    public void restoreNote(int position) {
        int _id = notes.get(position).get_id();

        NoteBookDBOperator.changeNoteRecycleStatus(context, _id);

        notifyItemRemoved(position);
        notes.remove(position);

        notifyItemRangeChanged(position, notes.size() - position);
        Toast.makeText(context, "恢复成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除一条笔记
     * @param position position
     */
    public void deleteNote(int position) {
        int _id = notes.get(position).get_id();

        NoteBookDBOperator.deleteNote(context, _id);

        notifyItemRemoved(position);
        notes.remove(position);

        notifyItemRangeChanged(position, notes.size() - position);
        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }
}

