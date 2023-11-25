# Day02

- 商品分类展示
- 商品详情界面
- 购物车实现
- 订单 DB 实现

<img src="https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231115184108282.png" alt="image-20231115184108282" style="zoom:50%;" />

# 1.1 商品分类展示

## 商品 Info

```java
/**
 * 商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo implements Serializable {
    /**
     * id
     */
    private int id;
    /**
     * img 图片
     */
    private int img;
    /**
     * title 标题
     */
    private String title;
    /**
     * description 描述
     */
    private String description;
    /**
     * price 价格
     */
    private int price;
}
```

## 分类 Adapter

```java
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

```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    <TextView
            android:id="@+id/left_list_item_tx_name"
            android:paddingStart="10dp"
            android:layout_width="100dp"
            android:text="@string/left"
            android:gravity="center_vertical"
            android:layout_height="50dp" tools:ignore="RtlSymmetry"/>
</androidx.appcompat.widget.LinearLayoutCompat>
```

## 信息 Adapter

```java
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
```



```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        tools:ignore="ContentDescription"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_margin="10dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

        <ImageView
                android:background="@drawable/rounded_image"
                android:id="@+id/right_list_item_iv_img"
                android:src="@drawable/avatar"
                android:scaleType="centerCrop"
                android:layout_width="100dp"
                android:layout_height="100dp"/>

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:layout_marginStart="10dp"
                android:orientation="vertical"
                android:layout_height="match_parent">

            <TextView
                    android:id="@+id/right_list_item_tx_title"
                    android:layout_width="wrap_content"
                    android:text="@string/title"
                    android:singleLine="true"
                    android:textColor="#333"
                    android:textStyle="bold"
                    android:layout_height="wrap_content"/>

            <TextView
                    android:id="@+id/right_list_item_tx_description"
                    android:layout_marginTop="3dp"
                    android:layout_width="wrap_content"
                    android:text="@string/description"
                    android:textSize="12sp"
                    android:layout_height="wrap_content"/>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content">

                <ImageView
                        android:src="@drawable/money"
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:layout_gravity="bottom"
                        android:layout_marginBottom="4dp" />

                <TextView
                        android:id="@+id/right_list_item_tx_price"
                        android:layout_width="wrap_content"
                        android:textSize="20sp"
                        android:text="100"
                        android:textStyle="bold"
                        android:textColor="@color/primary_color"
                        android:layout_height="wrap_content" tools:ignore="HardcodedText"/>

            </androidx.appcompat.widget.LinearLayoutCompat>


        </androidx.appcompat.widget.LinearLayoutCompat>



    </androidx.appcompat.widget.LinearLayoutCompat>

</androidx.appcompat.widget.LinearLayoutCompat>
```

## 源数据

```java
/**
 * 源数据提供者
 */
public class DataService {
    /**
     * 根据 position 获取 list
     *
     * @param position position
     * @return list
     */
    public static List<ProductInfo> getListData(int position) {
        List<ProductInfo> list = new ArrayList<>();
        if (position == 0) {
            list.add(new ProductInfo(1, R.drawable.p_0_1, "夏季短袖T恤", "舒适透气的夏季短袖T恤，适合日常休闲穿着。", 39));
            list.add(new ProductInfo(2, R.drawable.p_0_2, "牛仔裤", "经典款牛仔裤，适合各种场合穿着。", 119));
            list.add(new ProductInfo(3, R.drawable.p_0_3, "连衣裙", "时尚简约的连衣裙，适合派对和约会穿着。", 89));
            list.add(new ProductInfo(4, R.drawable.p_0_4, "运动裤", "舒适透气的运动裤，适合运动和健身穿着。", 79));
            list.add(new ProductInfo(5, R.drawable.p_0_5, "风衣", "时尚简约的风衣，适合春秋季节穿着。", 189));
        } else if (position == 1){
            list.add(new ProductInfo(101, R.drawable.p_1_1, "智能手机", "高性能智能手机，配置强大，拍照效果出色。", 1999));
            list.add(new ProductInfo(102, R.drawable.p_1_2, "平板电脑", "轻薄便携的平板电脑，适合娱乐和办公使用。", 1299));
            list.add(new ProductInfo(103, R.drawable.p_1_3, "耳机", "高音质蓝牙耳机，舒适佩戴，适合运动和旅行。", 188));
            list.add(new ProductInfo(104, R.drawable.p_1_4, "智能手表", "功能强大的智能手表，支持健康监测和智能通知。", 599));
        }else if (position == 2){
            list.add(new ProductInfo(1001, R.drawable.p_2_1, "保温杯", "不锈钢保温杯，保温效果好，容量适中。", 59));
            list.add(new ProductInfo(1002, R.drawable.p_2_2, "电风扇", "静音电风扇，风力强劲，适合夏季使用。", 139));
            list.add(new ProductInfo(1003, R.drawable.p_2_3, "洗衣液", "高效去污的洗衣液，衣物清洁彻底。", 29));
        }else if (position == 3){
            list.add(new ProductInfo(10001, R.drawable.p_3_1, "经典硬皮笔记本", "经典款式的硬皮笔记本，尺寸适中，方便携带。", 15));
            list.add(new ProductInfo(10002, R.drawable.p_3_2, "文具套装", "包含铅笔、橡皮、尺子等文具的套装。", 19));
            list.add(new ProductInfo(10003, R.drawable.p_3_3, "书包", "时尚耐用的书包，适合学生使用。", 99));
        }
        return list;
    }
}
```

## 主页 Fragment

```java
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
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".fragment.HomeFragment">

    <TextView android:layout_width="match_parent"
              android:layout_height="30dp"
              android:background="@color/primary_color"/>

    <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="50dp"
            app:title="@string/home"
            android:background="@color/primary_color"/>

    <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            tools:context=".fragment.FragmentHome">
        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:layout_height="match_parent">

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="100dp"
                    android:layout_height="match_parent">

                <androidx.recyclerview.widget.RecyclerView
                        android:id="@+id/home_fragment_leftRecyclerView"
                        tools:listitem="@layout/home_left_list_item"
                        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"/>


            </androidx.appcompat.widget.LinearLayoutCompat>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">

                <androidx.recyclerview.widget.RecyclerView
                        android:id="@+id/home_fragment_rightRecyclerView"
                        tools:listitem="@layout/home_right_list_item"
                        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"/>

            </androidx.appcompat.widget.LinearLayoutCompat>


        </androidx.appcompat.widget.LinearLayoutCompat>


    </FrameLayout>

</androidx.appcompat.widget.LinearLayoutCompat>
```

![image-20231115190355808](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231115190355808.png)

# 1.2 商品详情界面

## 商品详情 Activity

```java
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
                } else {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

```

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        tools:ignore="ContentDescription"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".activity.ProductDetailsActivity">

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:orientation="vertical"
            android:layout_height="wrap_content">

        <androidx.appcompat.widget.Toolbar
                android:id="@+id/product_details_activity_toolbar"
                android:layout_width="match_parent"
                app:title="@string/product_details"
                app:titleTextColor="@color/white"
                android:background="@color/primary_color"
                app:navigationIcon="@drawable/baseline_arrow_back_24"
                android:layout_height="wrap_content"/>

        <ImageView
                android:id="@+id/product_details_activity_iv_img"
                android:src="@drawable/avatar"
                android:scaleType="centerCrop"
                android:layout_width="match_parent"
                android:layout_height="240dp"
                tools:ignore="ContentDescription"/>

        <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="10dp"
                android:orientation="vertical">


            <LinearLayout
                    android:layout_width="wrap_content"
                    android:orientation="horizontal"
                    android:layout_height="wrap_content" tools:ignore="UseCompoundDrawables,UselessParent">

                <ImageView
                        android:src="@drawable/money"
                        android:layout_marginBottom="4dp"
                        android:layout_gravity="bottom"
                        android:layout_width="20dp"
                        android:layout_height="20dp"/>

                <TextView
                        android:id="@+id/product_details_activity_tv_price"
                        android:text="198"
                        android:textColor="@color/primary_color"
                        android:textSize="24sp"
                        android:textStyle="bold"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content" tools:ignore="HardcodedText"/>
            </LinearLayout>

            <TextView
                    android:id="@+id/product_details_activity_tv_title"
                    android:layout_marginTop="10dp"
                    android:textStyle="bold"
                    android:textSize="36sp"
                    android:text="@string/title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>

            <TextView
                    android:id="@+id/product_details_activity_tv_description"
                    android:layout_marginTop="10dp"
                    android:textColor="#999999"
                    android:lineSpacingExtra="6dp"
                    android:text="@string/description"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>

        </LinearLayout>

    </androidx.appcompat.widget.LinearLayoutCompat>

    <Button
            android:id="@+id/product_details_activity_buy_car"
            android:layout_width="wrap_content"
            android:layout_alignParentEnd="true"
            android:layout_alignParentBottom="true"
            android:layout_margin="20dp"
            android:text="@string/buy_car"
            android:layout_height="wrap_content"/>

</RelativeLayout>
```

## CarDbHelper

### init 初始化 DB

```java
/**
 * 购物车 DB
 */
public class CarDbHelper extends SQLiteOpenHelper {
    /**
     * sHelper
     */
    private static CarDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "car.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public CarDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static CarDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new CarDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table car_table(car_id integer primary key autoincrement, " +
                "username text," +
                "product_id integer," +
                "product_img integer," +
                "product_title text," +
                "product_description text," +
                "product_price integer," +
                "product_count integer" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
```

### 添加购物车

```java
	/**
     * 添加购物车
     *
     * @param username            用户名
     * @param product_id          商品 id
     * @param product_img         商品 img
     * @param product_title       商品 title
     * @param product_description 商品 description
     * @param product_price       商品 price
     * @return count
     */
    public int addCar(String username, Integer product_id, Integer product_img, String product_title, String product_description, Integer product_price) {
        CarInfo carInfo = loadCarInfoByUsernameAndProductId(username, product_id);

        if (carInfo == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("username", username);
            values.put("product_id", product_id);
            values.put("product_img", product_img);
            values.put("product_title", product_title);
            values.put("product_description", product_description);
            values.put("product_price", product_price);
            values.put("product_count", 1);
            String nullColumnHack = "values(null,?,?,?,?,?,?,?)";

            int insert = (int) db.insert("car_table", nullColumnHack, values);
            db.close();
            return insert;
        } else {
            return updateProduct(carInfo.getCar_id(), carInfo);
        }


    }
```

### 根据用户名查找购物信息

```java
 /**
     * 根据用户名查找购物信息
     */
    @SuppressLint("Range")
    public CarInfo findCarByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        CarInfo carInfo = null;
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price" +
                "  from car_table where username=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_id = cursor.getInt(cursor.getColumnIndex("product_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));

            carInfo = new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count);
        }
        cursor.close();
        db.close();
        return carInfo;
    }
```

### 根据 id 修改购物车商品数量

```java
 /**
     * 根据 id 修改购物车商品数量
     */
    public int updateProduct(int car_id, CarInfo carInfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("product_count", carInfo.getProduct_count() + 1);

        int update = db.update("car_table", values, " car_id=?", new String[]{car_id + ""});

        db.close();
        return update;

    }
```

### 根据 id 修改购物车商品数量

```java
    /**
     * 根据 id 修改购物车商品数量
     */
    public int updateProductSub(int car_id, CarInfo carInfo) {

        if (carInfo.getProduct_count() >= 2) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("product_count", carInfo.getProduct_count() - 1);

            int update = db.update("car_table", values, " car_id=?", new String[]{car_id + ""});

            db.close();
            return update;
        }
        return 0;
    }
```

### 根据用户名查找购物信息

```java
    /**
     * 根据用户名查找购物信息
     */
    @SuppressLint("Range")
    public CarInfo loadCarInfoByUsernameAndProductId(String username, Integer product_id) {
        SQLiteDatabase db = getReadableDatabase();
        CarInfo carInfo = null;
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price,product_count from car_table where username=? and product_id = ?";
        String[] selectionArgs = {username, String.valueOf(product_id)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            carInfo = new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count);
        }
        cursor.close();
        db.close();
        return carInfo;
    }
```

### 获取所有信息

```java
	/**
     * 获取所有信息
     */
    @SuppressLint("Range")
    public List<CarInfo> findAll(String username) {
        SQLiteDatabase db = getReadableDatabase();
        List<CarInfo> list = new ArrayList<>();
        String sql = "select car_id,username,product_id,product_img,product_title,product_description,product_price,product_count  from car_table where username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int car_id = cursor.getInt(cursor.getColumnIndex("car_id"));
            int product_id = cursor.getInt(cursor.getColumnIndex("product_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            String product_description = cursor.getString(cursor.getColumnIndex("product_description"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            list.add(new CarInfo(car_id, username, product_id, product_img, product_title, product_description, product_price, product_count));
        }
        cursor.close();
        db.close();
        return list;
    }
```

### 根据 car_id 删除

```java
	/**
     * 根据 car_id 删除
     */
    public int delete(String car_id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete("car_table", " car_id=?", new String[]{car_id});
        db.close();
        return delete;
    }
```

![image-20231115190521633](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231115190521633.png)

# 1.3 购物车

### 购物车 Adapter

```java
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
        holder.product_count.setText(carInfo.getProduct_count() + "");

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
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
                                              xmlns:tools="http://schemas.android.com/tools"
                                              android:layout_width="match_parent"
                                              android:layout_height="wrap_content">

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_margin="10dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

        <ImageView
                android:id="@+id/car_list_item_iv_01"
                android:src="@drawable/avatar"
                android:scaleType="centerCrop"
                android:layout_width="90dp"
                android:layout_height="90dp" tools:ignore="ContentDescription"/>

        <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_marginLeft="10dp"
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

            <TextView
                    android:id="@+id/car_list_item_tv_title"
                    android:maxLines="2"
                    android:textColor="#333"
                    android:text="@string/title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="wrap_content"
                    android:layout_marginTop="10dp"
                    android:layout_height="wrap_content">

                <ImageView
                        android:src="@drawable/money"
                        android:layout_gravity="bottom"
                        android:layout_marginBottom="4dp"
                        android:layout_width="20dp"
                        android:layout_height="20dp" tools:ignore="ContentDescription"/>
                <TextView
                        android:id="@+id/car_list_item_tv_price"
                        android:text="@string/money"
                        android:textColor="@color/primary_color"
                        android:textSize="20sp"
                        android:textStyle="bold"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"/>

            </androidx.appcompat.widget.LinearLayoutCompat>

            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_gravity="right"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content">

                <TextView
                        android:id="@+id/car_list_item_tv_sub"
                        android:background="#c9c9c9"
                        android:gravity="center"
                        android:text="@string/sub"
                        android:textColor="@color/white"
                        android:layout_width="20dp"
                        android:layout_height="20dp"/>

                <TextView
                        android:id="@+id/car_list_item_tv_count"
                        android:layout_width="wrap_content"
                        android:text="@string/num"
                        android:layout_marginStart="10dp"
                        android:layout_height="wrap_content"/>

                <TextView
                        android:id="@+id/car_list_item_tv_add"
                        android:background="@color/primary_color"
                        android:gravity="center"
                        android:text="@string/add"
                        android:textColor="@color/white"
                        android:layout_width="20dp"
                        android:layout_height="20dp"/>

            </androidx.appcompat.widget.LinearLayoutCompat>

        </androidx.appcompat.widget.LinearLayoutCompat>

    </androidx.appcompat.widget.LinearLayoutCompat>

</androidx.appcompat.widget.LinearLayoutCompat>
```

### 购物车 Fragment

```java
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
                if (carInfoList.size() == 0) {
                    Toast.makeText(getActivity(), "当前购物车为空", Toast.LENGTH_SHORT).show();
                } else {
                    OrderDbHelper.getInstance(getActivity()).insertByAll(carInfoList, "文华街 199 号", "19510201902");
                    carInfoList.forEach(carInfo -> CarDbHelper.getInstance(getActivity()).delete(carInfo.getCar_id() + ""));
                    loadData();
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
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                android:layout_width="match_parent"
                android:layout_height="match_parent" xmlns:app="http://schemas.android.com/apk/res-auto"
                tools:context=".fragment.CarFragment">

    <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_above="@id/car_fragment_bottom"
            android:orientation="vertical"
            android:layout_height="match_parent">
        <androidx.appcompat.widget.Toolbar
                android:id="@+id/car_fragment_toolbar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:title="@string/car"
                app:titleTextColor="@color/white"
                android:background="@color/primary_color"/>


        <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/car_fragment_recyclerView"
                android:layout_width="match_parent"
                tools:listitem="@layout/car_list_item"
                app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
                android:layout_height="wrap_content"/>


    </androidx.appcompat.widget.LinearLayoutCompat>

    <androidx.appcompat.widget.LinearLayoutCompat
            android:id="@+id/car_fragment_bottom"
            android:gravity="right"
            android:layout_alignParentBottom="true"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

        <androidx.appcompat.widget.LinearLayoutCompat
                android:gravity="center_vertical"
                android:layout_margin="15dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content">
            <TextView
                    android:id="@+id/car_fragment_tx_total"
                    android:text="@string/total"
                    android:textSize="26sp"
                    android:textStyle="bold"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"/>
            <androidx.appcompat.widget.LinearLayoutCompat
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content">

                <androidx.appcompat.widget.LinearLayoutCompat
                        android:layout_width="wrap_content"
                        android:layout_gravity="center_vertical"
                        android:layout_height="wrap_content">
                    <ImageView
                            android:layout_marginBottom="4dp"
                            android:src="@drawable/money"
                            android:layout_gravity="bottom"
                            android:layout_width="20dp"
                            android:layout_height="20dp" tools:ignore="ContentDescription"/>
                    <TextView
                            android:textSize="20sp"
                            android:textStyle="bold"
                            android:textColor="@color/primary_color"
                            android:text="@string/money"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"/>
                </androidx.appcompat.widget.LinearLayoutCompat>

                <Button
                        android:id="@+id/car_fragment_btn_buy"
                        android:layout_marginLeft="10dp"
                        android:text="@string/buy"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"/>

            </androidx.appcompat.widget.LinearLayoutCompat>
        </androidx.appcompat.widget.LinearLayoutCompat>


    </androidx.appcompat.widget.LinearLayoutCompat>


</RelativeLayout>
```

### 优化页面切换时数据的加载

```java
            case 1:
                if (car == null) {
                    car = new CarFragment();
                    transaction.add(R.id.main_activity_container, car);
                } else {
                    transaction.show(car);
                    car.loadData();
                }
                break;
```

![image-20231115190631245](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231115190631245.png)

# 1.4 订单 DB 

## 订单 Info

```java
/**
 * 订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    /**
     * id
     */
    private Integer order_id;
    /**
     * username
     */
    private String username;
    /**
     * 商品 img
     */
    private int product_img;
    /**
     * 商品 title
     */
    private String product_title;

    /**
     * 商品 price
     */
    private int product_price;

    /**
     * 商品 count
     */
    private int product_count;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String mobile;
}
```

## OrderDbHelper

### init 初始化 DB

```java
/**
 * order DB 数据库
 */
public class OrderDbHelper extends SQLiteOpenHelper {
    private static OrderDbHelper sHelper;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "order.db";
    /**
     * 版本号
     */
    private static final int VERSION = 1;

    public OrderDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建单例，供使用调用该类里面的的增删改查的方法
     *
     * @param context context
     * @return UserDbHelper
     */
    public synchronized static OrderDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new OrderDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 order_table 表
        db.execSQL("create table order_table(order_id integer primary key autoincrement, " +
                "username text," +
                "product_img integer," +
                "product_title text," +
                "product_price integer," +
                "product_count integer," +
                "address text," +
                "mobile text" +
                ")");
    }
        @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
```

### 批量插入

```java
	/**
     * 批量插入
     *
     * @param list    list
     * @param address address
     * @param mobile  mobile
     */
    public void insertByAll(List<CarInfo> list, String address, String mobile) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                values.put("username", list.get(i).getUsername());
                values.put("product_img", list.get(i).getProduct_img());
                values.put("product_title", list.get(i).getProduct_title());
                values.put("product_price", list.get(i).getProduct_price());
                values.put("product_count", list.get(i).getProduct_count());
                values.put("address", address);
                values.put("mobile", mobile);
                db.insert("order_table", null, values);

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

    }
```

### 获取所有信息

```java
 	/**
     * 获取所有信息
     */
    @SuppressLint("Range")
    public List<OrderInfo> findAll(String username) {
        SQLiteDatabase db = getReadableDatabase();
        List<OrderInfo> list = new ArrayList<>();
        String sql = "select order_id,username,product_img,product_title,product_price,product_count, address, mobile  from order_table where username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            int order_id = cursor.getInt(cursor.getColumnIndex("order_id"));
            int product_img = cursor.getInt(cursor.getColumnIndex("product_img"));
            String product_title = cursor.getString(cursor.getColumnIndex("product_title"));
            int product_price = cursor.getInt(cursor.getColumnIndex("product_price"));
            int product_count = cursor.getInt(cursor.getColumnIndex("product_count"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            list.add(new OrderInfo(order_id, username, product_img, product_title, product_price, product_count, address, mobile));
        }
        cursor.close();
        db.close();
        return list;
    }
```

### 根据 order_id 删除

```java
    /**
     * 根据 order_id 删除
     */
    public int delete(String order_id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete("order_table", " order_id=?", new String[]{order_id});
        db.close();
        return delete;
    }
```

![image-20231115190905802](https://cb-cbq-jz.oss-cn-beijing.aliyuncs.com/images/image-20231115190905802.png)

