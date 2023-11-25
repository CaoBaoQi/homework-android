package jz.cbq.work_note_book.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import jz.cbq.work_note_book.R;
import jz.cbq.work_note_book.db.op.NoteBookDBOperator;
import jz.cbq.work_note_book.entity.InAbeyance;
import jz.cbq.work_note_book.utils.AlarmUtil;

/**
 * 待办事项 Adapter
 *
 * @author cbq
 * @date 2023/11/20 22:48
 * @since 1.0.0
 */
public class InAbeyanceRecyclerViewAdapter extends RecyclerView.Adapter<InAbeyanceRecyclerViewAdapter.ViewHolder> {
    /**
     * 待办数据
     */
    List<InAbeyance> inAbeyances;
    /**
     * context
     */
    Context context;
    /**
     *  InAbeyance item 单击事件监听接口
     */
    OnItemClickListener onItemClickListener;

    public InAbeyanceRecyclerViewAdapter(List<InAbeyance> inAbeyances, Context context) {
        this.inAbeyances = inAbeyances;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_abeyance, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        /*
        * 设置待办完成状态 (默认为不勾选)
        * status为 1 则代表已完成
        * */
        holder.accomplish_status
                .setSelected(inAbeyances.get(position).getStatus() == 1);
        
        holder.parent_of_accomplish_status.setOnClickListener(
                view -> {
                    updateInabeyanceStatus(position); // 更新完成状态
                });

        holder.in_abeyance_content.setText(inAbeyances.get(position).getContent());
        if (!"".equals(inAbeyances.get(position).getDate_remind())) {
            holder.hasRemind.setSelected(true);
            holder.date_remind.setText(inAbeyances.get(position).getDate_remind());
        } else {
            holder.hasRemind.setSelected(false);
            holder.date_remind.setText("");
        }

        holder.in_abeyance_main.setOnLongClickListener(view -> {
            deleteInAbeyance(position);
            return true;
        });

        holder.in_abeyance_main.setOnClickListener(view -> {
            InAbeyance inAbeyance = inAbeyances.get(position);
            onItemClickListener.OnItemClick(
                    position, inAbeyance.get_id() + "",
                    inAbeyance.getContent(), inAbeyance.getDate_remind());
        });
    }

    @Override
    public int getItemCount() {
        return inAbeyances.size();
    }

    /**
     * 这是继承自 RecyclerView.ViewHolder 的
     * 自定义的 ViewHolder
     * 也是 InAbeyanceAdapter 的内部类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 待办完成状态选择框的容器
         */
        private final LinearLayout parent_of_accomplish_status;
        /**
         * 待办完成状态
         */
        private final ImageView accomplish_status;
        /**
         * 待办信息主要布局
         */
        private final LinearLayout in_abeyance_main;
        /**
         * 待办内容
         */
        private final TextView in_abeyance_content;
        /**
         * 待办是否有提醒
         */
        private final ImageView hasRemind;
        /**
         * 待办提醒日期
         */
        private final TextView date_remind;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_of_accomplish_status = itemView.findViewById(R.id.in_abeyance_status);
            accomplish_status = itemView.findViewById(R.id.in_abeyance_check_box);
            in_abeyance_main = itemView.findViewById(R.id.in_abeyance_main);
            in_abeyance_content = itemView.findViewById(R.id.in_abeyance_content);
            hasRemind = itemView.findViewById(R.id.in_abeyance_alarm);
            date_remind = itemView.findViewById(R.id.date_remind);
        }
    }

    /**
     * 更新待办的完成状态
     * @param position position
     */
    public void updateInabeyanceStatus(int position) {
        int _id = inAbeyances.get(position).get_id();

        inAbeyances.get(position).
                setStatus(1 - inAbeyances.get(position).getStatus());

        notifyItemChanged(position);
        NoteBookDBOperator.changeInAbeyanceStatus(context, _id);
    }

    /**
     * 删除一条待办
     * @param position position
     */
    public void deleteInAbeyance(int position) {
        int _id = inAbeyances.get(position).get_id();

        String date_remind = inAbeyances.get(position).getDate_remind();
        if (!"".equals(date_remind)) {
            AlarmUtil.cancelAlarm(context.getApplicationContext(), _id);
        }

        NoteBookDBOperator.deleteInAbeyance(context, _id);
        inAbeyances.remove(position);
        notifyItemRemoved(position);

        notifyItemRangeChanged(position, inAbeyances.size() - position);
        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, String _id, String content, String date_remind);
    }
}

