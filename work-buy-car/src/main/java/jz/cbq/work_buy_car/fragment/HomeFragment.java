package jz.cbq.work_buy_car.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.activity.ProductDetailsActivity;
import jz.cbq.work_buy_car.adapter.home.ProductItemAdapter;
import jz.cbq.work_buy_car.adapter.home.ProductTypeAdapter;
import jz.cbq.work_buy_car.entity.DataService;

/**
 * 主页
 */
public class HomeFragment extends Fragment {
    /**
     * rootView
     */
    private View rootView;
    /**
     *商品分类和商品信息 RecyclerView
     */
    private RecyclerView left, right;
    /**
     * 商品分类 Adapter
     */
    private ProductTypeAdapter leftAdapter;
    /**
     * 商品信息 Adapter
     */
    private ProductItemAdapter rightAdapter;
    /**
     * 商品分类数据
     */
    private List<String> leftList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        left = rootView.findViewById(R.id.home_fragment_leftRecyclerView);
        right = rootView.findViewById(R.id.home_fragment_rightRecyclerView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        leftList.add("衣服");
        leftList.add("电子产品");
        leftList.add("生活用品");
        leftList.add("学习");

        leftAdapter = new ProductTypeAdapter(leftList);
        left.setAdapter(leftAdapter);

        rightAdapter = new ProductItemAdapter();
        rightAdapter.setDataList(DataService.getListData(0));
        right.setAdapter(rightAdapter);

        rightAdapter.setItemClickListener((productInfo, position) -> {
            Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
            intent.putExtra("productInfo", productInfo);
            startActivity(intent);
        });

        leftAdapter.setLeftListOnClickItemListener(position -> {
            leftAdapter.setCurrentIndex(position);
            rightAdapter.setDataList(DataService.getListData(position));
        });


    }
}