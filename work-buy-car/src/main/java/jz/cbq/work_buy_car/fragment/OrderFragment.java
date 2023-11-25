package jz.cbq.work_buy_car.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.adapter.order.OrderAdapter;
import jz.cbq.work_buy_car.db.OrderDbHelper;
import jz.cbq.work_buy_car.entity.OrderInfo;
import jz.cbq.work_buy_car.entity.UserInfo;

/**
 * 订单
 */
public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private View rootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order, container, false);
        }

        recyclerView = rootView.findViewById(R.id.order_fragment_recycleView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderAdapter = new OrderAdapter();
        recyclerView.setAdapter(orderAdapter);

        orderAdapter.setOrderAdapterOnItemClickListener((orderInfo, position) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("确定删除吗？")
                    .setPositiveButton("确认", (dialog, which) -> {
                        OrderDbHelper.getInstance(getActivity()).delete(orderInfo.getOrder_id() + "");
                        loadData();
                    })
                    .setNegativeButton("取消", (dialog, which) -> Toast.makeText(getActivity(), "取消删除", Toast.LENGTH_SHORT).show()).create().show();
        });

        loadData();


    }

    public void loadData() {
        UserInfo userInfo = UserInfo.getCurrentUserInfo();
        if (userInfo != null) {
            List<OrderInfo> orderInfoList = OrderDbHelper.getInstance(getActivity()).findAll(userInfo.getUsername());
            orderAdapter.setDataList(orderInfoList);
        }

    }
}