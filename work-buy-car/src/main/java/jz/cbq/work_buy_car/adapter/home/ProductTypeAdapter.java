package jz.cbq.work_buy_car.adapter.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import jz.cbq.work_buy_car.R;

/**
 * 商品分类 Adapter
 */
public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.Holder> {
    /**
     * 数据
     */
    private List<String> dataList;
    /**
     * 当前选中的分类 index
     */
    private int currentIndex = 0;
    /**
     * listener
     */
    private LeftListOnClickItemListener leftListOnClickItemListener;

    public ProductTypeAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setLeftListOnClickItemListener(LeftListOnClickItemListener leftListOnClickItemListener) {
        this.leftListOnClickItemListener = leftListOnClickItemListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_left_list_item, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        holder.tv_name.setText(dataList.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (leftListOnClickItemListener != null) {
                leftListOnClickItemListener.onItemClick(position);
            }
        });

        if (currentIndex == position) {
            holder.itemView.setBackgroundResource(R.drawable.type_selector_bg);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.type_selector_normal_bg);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        /**
         * 分类名称
         */
        TextView tv_name;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.left_list_item_tx_name);
        }
    }

    /**
     * 单击事件
     */
    public interface LeftListOnClickItemListener {
        void onItemClick(int position);
    }
}
