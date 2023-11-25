package jz.cbq.work_buy_car.adapter.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.entity.OrderInfo;

/**
 * 订单 Adapter
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    private List<OrderInfo> dataList;

    private OrderAdapterOnItemClickListener orderAdapterOnItemClickListener;


    public void setDataList(List<OrderInfo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setOrderAdapterOnItemClickListener(OrderAdapterOnItemClickListener orderAdapterOnItemClickListener) {
        this.orderAdapterOnItemClickListener = orderAdapterOnItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {

        OrderInfo orderInfo = dataList.get(position);

        holder.product_img.setImageResource(orderInfo.getProduct_img());
        holder.product_title.setText(orderInfo.getProduct_title());
        holder.product_price.setText(orderInfo.getProduct_price() + "");
        holder.product_count.setText("x " + orderInfo.getProduct_count());
        holder.address.setText("地址： " + orderInfo.getAddress() + " | 联系方式：" + orderInfo.getMobile());

        holder.itemView.setOnLongClickListener(v -> {
            if (orderAdapterOnItemClickListener != null) {
                orderAdapterOnItemClickListener.deleteOnClick(orderInfo, position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_title;
        TextView product_price;
        TextView product_count;
        TextView address;


        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.order_list_item_iv_01);
            product_title = itemView.findViewById(R.id.order_list_item_tv_title);
            product_price = itemView.findViewById(R.id.order_list_item_tv_price);
            product_count = itemView.findViewById(R.id.order_list_item_tv_count);
            address = itemView.findViewById(R.id.order_list_item_tv_address);
        }
    }

    public interface OrderAdapterOnItemClickListener {
        void deleteOnClick(OrderInfo orderInfo, int position);
    }
}
