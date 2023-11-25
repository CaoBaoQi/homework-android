package jz.cbq.work_buy_car;

import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import jz.cbq.work_buy_car.fragment.CarFragment;
import jz.cbq.work_buy_car.fragment.HomeFragment;
import jz.cbq.work_buy_car.fragment.MineFragment;
import jz.cbq.work_buy_car.fragment.OrderFragment;

/**
 * 启动 Main
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 主页 Fragment
     */
    private HomeFragment home;
    /**
     * 购物车 Fragment
     */
    private CarFragment car;
    /**
     * 订单 Fragment
     */
    private OrderFragment order;
    /**
     * 我的 Fragment
     */
    private MineFragment mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 开启毛玻璃感
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // 取消 bottom 的着色效果
        BottomNavigationView bottom = findViewById(R.id.main_activity_bottom);
        bottom.setItemIconTintList(null);

        // 默认加载 HomeFragment
        selectedFragment(0);

        bottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tab_home) {
                selectedFragment(0);
            } else if (item.getItemId() == R.id.tab_car) {
                selectedFragment(1);
            } else if (item.getItemId() == R.id.tab_order) {
                selectedFragment(2);
            } else {
                selectedFragment(3);
            }
            return true;
        });

    }


    /**
     * 选中 Fragment
     *
     * @param position position
     */
    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (home == null) {
                    home = new HomeFragment();
                    transaction.add(R.id.main_activity_container, home);
                } else {
                    transaction.show(home);
                }
                break;
            case 1:
                if (car == null) {
                    car = new CarFragment();
                    transaction.add(R.id.main_activity_container, car);
                } else {
                    transaction.show(car);
                    car.loadData();
                }
                break;
            case 2:
                if (order == null) {
                    order = new OrderFragment();
                    transaction.add(R.id.main_activity_container, order);
                } else {
                    transaction.show(order);
                    order.loadData();
                }
                break;
            default:
                if (mine == null) {
                    mine = new MineFragment();
                    transaction.add(R.id.main_activity_container, mine);
                } else {
                    transaction.show(mine);
                }
                break;
        }

        transaction.commit();
    }

    /**
     * 隐藏 Fragment
     *
     * @param transaction transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (home != null) {
            transaction.hide(home);
        }
        if (car != null) {
            transaction.hide(car);
        }
        if (order != null) {
            transaction.hide(order);
        }
        if (mine != null) {
            transaction.hide(mine);
        }
    }
}