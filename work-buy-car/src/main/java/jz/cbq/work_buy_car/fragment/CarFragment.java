package jz.cbq.work_buy_car.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.adapter.car.CarAdapter;
import jz.cbq.work_buy_car.db.CarDbHelper;
import jz.cbq.work_buy_car.db.OrderDbHelper;
import jz.cbq.work_buy_car.entity.CarInfo;
import jz.cbq.work_buy_car.entity.UserInfo;

/**
 * 购物车
 */
public class CarFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private TextView tx_total;
    private Button btn_buy;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_car, container, false);
        }
        recyclerView = rootView.findViewById(R.id.car_fragment_recyclerView);
        tx_total = rootView.findViewById(R.id.car_fragment_tx_total);
        btn_buy = rootView.findViewById(R.id.car_fragment_btn_buy);

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carAdapter = new CarAdapter();
        recyclerView.setAdapter(carAdapter);
        carAdapter.setCarAdapterOnItemClickListener(new CarAdapter.CarAdapterOnItemClickListener() {
            @Override
            public void addOnClick(CarInfo carInfo, int position) {
                CarDbHelper.getInstance(getActivity()).updateProduct(carInfo.getCar_id(), carInfo);
                loadData();
            }

            @Override
            public void subOnClick(CarInfo carInfo, int position) {
                CarDbHelper.getInstance(getActivity()).updateProductSub(carInfo.getCar_id(), carInfo);
                loadData();
            }

            @Override
            public void deleteOnClick(CarInfo carInfo, int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("确定删除吗？")
                        .setPositiveButton("确认", (dialog, which) -> {
                            CarDbHelper.getInstance(getActivity()).delete(carInfo.getCar_id() + "");
                            loadData();
                        })
                        .setNegativeButton("取消", (dialog, which) -> Toast.makeText(getActivity(), "取消删除", Toast.LENGTH_SHORT).show()).create().show();
            }
        });

        btn_buy.setOnClickListener(v -> {
            UserInfo userInfo = UserInfo.getCurrentUserInfo();

            if (userInfo != null) {
                List<CarInfo> carInfoList = CarDbHelper.getInstance(getActivity()).findAll(userInfo.getUsername());
                if (carInfoList.isEmpty()) {
                    Toast.makeText(getActivity(), "当前购物车为空", Toast.LENGTH_SHORT).show();
                } else {
                    View pay_view = LayoutInflater.from(getActivity()).inflate(R.layout.pay_dialog_layout, null);
                    EditText et_address = pay_view.findViewById(R.id.pay_dialog_et_address);
                    EditText et_mobile = pay_view.findViewById(R.id.pay_dialog_et_mobile);
                    TextView tx_money = pay_view.findViewById(R.id.pay_dialog_tx_money);

                    tx_money.setText(tx_total.getText().toString());

                    new AlertDialog.Builder(getActivity())
                            .setTitle("支付")
                            .setView(pay_view)
                            .setNegativeButton("取消", (dialog, which) -> Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show())
                            .setPositiveButton("确定", (dialog, which) -> {
                                String address = et_address.getText().toString();
                                String mobile = et_mobile.getText().toString();

                                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(mobile)){
                                    Toast.makeText(getActivity(), "请先完善信息", Toast.LENGTH_SHORT).show();
                                }else {
                                    OrderDbHelper.getInstance(getActivity()).insertByAll(carInfoList, address, mobile);
                                    carInfoList.forEach(carInfo -> CarDbHelper.getInstance(getActivity()).delete(carInfo.getCar_id() + ""));
                                    loadData();
                                    Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                                }

                            }).create().show();

                }
            }
        });

        loadData();
    }

    public void setTotalData(List<CarInfo> carInfoList) {
        AtomicInteger total = new AtomicInteger();
        carInfoList.forEach(carInfo -> total.addAndGet(carInfo.getProduct_price() * carInfo.getProduct_count()));
        tx_total.setText(total + " ");
    }

    public void loadData() {
        UserInfo userInfo = UserInfo.getCurrentUserInfo();

        if (userInfo != null) {
            List<CarInfo> carInfoList = CarDbHelper.getInstance(getActivity()).findAll(userInfo.getUsername());
            carAdapter.setDataList(carInfoList);
            setTotalData(carInfoList);
        }


    }
}