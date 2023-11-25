package jz.cbq.work_buy_car.adapter.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.entity.ProductInfo;

/**
 * 商品信息 Adapter
 */
public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.Holder> {
    /**
     * listener
     */
    private onItemClickListener itemClickListener;
    /**
     * 数据
     */
    private List<ProductInfo> dataList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<ProductInfo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_right_list_item, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        ProductInfo productInfo = dataList.get(position);

        holder.img.setImageResource(productInfo.getImg());
        holder.title.setText(productInfo.getTitle());
        holder.description.setText(productInfo.getDescription());
        holder.price.setText(productInfo.getPrice() + " ");

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(productInfo, position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        /**
         * 商品图片
         */
        ImageView img;
        /**
         * 商品标题
         */
        TextView title;
        /**
         * 商品描述
         */
        TextView description;
        /**
         * 商品价格
         */
        TextView price;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.right_list_item_iv_img);
            title = itemView.findViewById(R.id.right_list_item_tx_title);
            description = itemView.findViewById(R.id.right_list_item_tx_description);
            price = itemView.findViewById(R.id.right_list_item_tx_price);
        }
    }

    /**
     * 单击事件
     */
    public interface onItemClickListener {
        void onItemClick(ProductInfo productInfo, int position);
    }
}
