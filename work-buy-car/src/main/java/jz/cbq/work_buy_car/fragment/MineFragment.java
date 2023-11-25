package jz.cbq.work_buy_car.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.activity.AboutActivity;
import jz.cbq.work_buy_car.activity.LoginActivity;
import jz.cbq.work_buy_car.activity.UpdatePwdActivity;
import jz.cbq.work_buy_car.entity.UserInfo;

/**
 * 我的
 */
public class MineFragment extends Fragment {
    private View rootView;
    private TextView tv_username;
    private TextView tv_nickname;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            tv_username = rootView.findViewById(R.id.fragment_mine_tv_username);
            tv_nickname = rootView.findViewById(R.id.fragment_mine_tv_nickname);

            rootView.findViewById(R.id.fragment_mine_relative_layout_update_pwd).setOnClickListener(v -> new AlertDialog.Builder(getContext())
                    .setTitle("前往修改密码 ?")
                    .setNegativeButton("取消",(dialog, which) -> Toast.makeText(getActivity(), "已取消", Toast.LENGTH_SHORT).show())
                    .setPositiveButton("确定",(dialog, which) -> {

                        startActivityForResult(new Intent(getActivity(), UpdatePwdActivity.class),1000);

                    }).create().show());

            rootView.findViewById(R.id.mine_fragment_btn_logout).setOnClickListener(v -> new AlertDialog.Builder(getContext())
                    .setTitle("确认退出 ?")
                    .setNegativeButton("取消",(dialog, which) -> Toast.makeText(getActivity(), "取消退出", Toast.LENGTH_SHORT).show())
                    .setPositiveButton("确定",(dialog, which) -> {

                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    }).create().show());

            rootView.findViewById(R.id.fragment_mine_relative_layout_about_app).setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
            rootView.findViewById(R.id.fragment_mine_relative_layout_license).setOnClickListener(v -> Toast.makeText(getActivity(), "Apache License 2.0", Toast.LENGTH_SHORT).show());
            rootView.findViewById(R.id.fragment_mine_relative_layout_call_me).setOnClickListener(v -> Toast.makeText(getActivity(), "曹蓓", Toast.LENGTH_SHORT).show());
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserInfo userInfo = UserInfo.CurrentUserInfo;
        if (userInfo != null) {
            tv_username.setText(userInfo.getUsername());
            tv_nickname.setText(userInfo.getNickname());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}