package jz.cbq.work_buy_car.adapter.car;

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
import jz.cbq.work_buy_car.entity.CarInfo;

/**
 * 购物车 Adapter
 */
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.Holder> {
    private List<CarInfo> dataList;
    private CarAdapterOnItemClickListener carAdapterOnItemClickListener;

    public void setDataList(List<CarInfo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setCarAdapterOnItemClickListener(CarAdapterOnItemClickListener carAdapterOnItemClickListener) {
        this.carAdapterOnItemClickListener = carAdapterOnItemClickListener;
    }


    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item, null);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {

        CarInfo carInfo = dataList.get(position);

        holder.product_img.setImageResource(carInfo.getProduct_img());
        holder.product_title.setText(carInfo.getProduct_title());
        holder.product_price.setText(carInfo.getProduct_price() + "");
        holder.product_count.setText(carInfo.getProduct_count() + "  ");

        holder.tx_add.setOnClickListener(v -> {
            if (carAdapterOnItemClickListener != null) {
                carAdapterOnItemClickListener.addOnClick(carInfo, position);
            }
        });

        holder.tx_sub.setOnClickListener(v -> {
            if (carAdapterOnItemClickListener != null) {
                carAdapterOnItemClickListener.subOnClick(carInfo, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (carAdapterOnItemClickListener != null) {
                carAdapterOnItemClickListener.deleteOnClick(carInfo, position);
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
        TextView tx_add;
        TextView tx_sub;


        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.car_list_item_iv_01);
            product_title = itemView.findViewById(R.id.car_list_item_tv_title);
            product_price = itemView.findViewById(R.id.car_list_item_tv_price);
            product_count = itemView.findViewById(R.id.car_list_item_tv_count);
            tx_add = itemView.findViewById(R.id.car_list_item_tv_add);
            tx_sub = itemView.findViewById(R.id.car_list_item_tv_sub);
        }
    }

    public interface CarAdapterOnItemClickListener {
        void addOnClick(CarInfo carInfo, int position);

        void subOnClick(CarInfo carInfo, int position);

        void deleteOnClick(CarInfo carInfo, int position);
    }
}

