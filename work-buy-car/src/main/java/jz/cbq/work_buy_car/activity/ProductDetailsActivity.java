package jz.cbq.work_buy_car.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import jz.cbq.work_buy_car.R;
import jz.cbq.work_buy_car.db.CarDbHelper;
import jz.cbq.work_buy_car.entity.ProductInfo;
import jz.cbq.work_buy_car.entity.UserInfo;

/**
 * 商品细节
 */
public class ProductDetailsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ProductInfo productInfo = (ProductInfo) getIntent().getSerializableExtra("productInfo");

        // 返回按钮事件
        findViewById(R.id.product_details_activity_toolbar).setOnClickListener(v -> finish());

        ImageView iv_img = findViewById(R.id.product_details_activity_iv_img);
        TextView tx_title = findViewById(R.id.product_details_activity_tv_title);
        TextView tx_description = findViewById(R.id.product_details_activity_tv_description);
        TextView tx_price = findViewById(R.id.product_details_activity_tv_price);

        if (productInfo != null) {
            iv_img.setImageResource(productInfo.getImg());
            tx_title.setText(productInfo.getTitle());
            tx_description.setText(productInfo.getDescription());
            tx_price.setText(productInfo.getPrice() + " ");
        }

        findViewById(R.id.product_details_activity_buy_car).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("是否加入到购物车")
                    .setNegativeButton("取消",(dialog, which) -> Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show())
                    .setPositiveButton("确定",(dialog, which) -> {
                        UserInfo userInfo = UserInfo.getCurrentUserInfo();
                        if (userInfo != null) {
                            int count = 0;
                            if (productInfo != null) {
                                count = CarDbHelper.getInstance(ProductDetailsActivity.this)
                                        .addCar(userInfo.getUsername(), productInfo.getId(),
                                                productInfo.getImg(), productInfo.getTitle(),
                                                productInfo.getDescription(), productInfo.getPrice());
                            }
                            if (count > 0) {
                                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create().show();
        });
    }
}
