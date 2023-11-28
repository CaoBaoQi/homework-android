# 备忘录

> 班级: 信息工程系-计算机专升本 2301
>
> 姓名: 宋文杰-2309312117

## 项目简介

- 项目名称：备忘录
  - 数据存储采用 SQLite 进行存储
  - 使用 Fragment 容器实现页面加载
  - 嵌套菜单栏实现页面选择

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155409253.png" alt="image-20231128155409253" style="zoom:67%;" />

## 一、数据库

## 1.1 SQLite 数据库

```java
public class MyDBOpenHelper extends SQLiteOpenHelper {

    private Calendar createDate,remindDate;
    public MyDBOpenHelper(Context context) {
        //创建一个名为DB_ToDoList的数据库
        super(context, "todoDatabase.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_ToDoItem(_id integer primary key autoincrement, " +
                "remindTitle text not null, " + //待办事项的标题文本
                "createDate text DEFAULT(' '), " +  //待办事项的创建日期和时间
                "modifyDate text DEFAULT(' '), " +  //最后修改日期和时间
                "remindText text DEFAULT(' '), " +    //待办事项的注释说明
                "remindDate text DEFAULT(' '), " +    //待办事项的提醒日期和时间
                "haveDo boolean DEFAULT(0));";      //是否已处理，默认值：false
        db.execSQL(sql);
        sql = "create table tb_Remind(_id integer primary key autoincrement, " + //主键，自动增加
                "remindID integer, notificationID integer);";      //用于保存状态栏提示Notification的ID
        db.execSQL(sql);
        db.execSQL("insert into tb_Remind(notificationID) values(0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table if exists tb_ToDoItem");
        sqLiteDatabase.execSQL("drop table if exists tb_notificationID");
        onCreate(sqLiteDatabase);
    }
}

```

## 二、页面

## 2.1 主页

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ECB88A"/>
</LinearLayout>
```
```java
public class MainActivity extends AppCompatActivity {

    private MyDBOpenHelper dbOpenHelper;
    private Calendar createDate,remindDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbOpenHelper=new MyDBOpenHelper(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new RemindList())
                    .commit();
        }
        this.setTitle("备忘录");
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155509376.png" alt="image-20231128155509376" style="zoom:50%;" />

## 2.2 菜单

```xml
<?xml version="2.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
    <group android:id="@+id/group1">
        <item
                android:id="@+id/menu_add"
                android:title="添加新事项" />
        <item
                android:id="@+id/menu_today"
                android:title="今日提醒" />
        <item
                android:id="@+id/menu_list"
                android:title="列出全部事项">
            <menu>
                <item
                        android:id="@+id/menu_list_todo"
                        android:title="按待办时间排序" />
                <item
                        android:id="@+id/menu_list_create"
                        android:title="按创建时间排序" />
            </menu>
        </item>
        <item
                android:id="@+id/menu_undo"
                android:title="未处理" />
        <item
                android:id="@+id/menu_done"
                android:title="已处理" />
        <item
                android:id="@+id/menu_clear"
                android:title="删除全部事项" />
        <item
                android:id="@+id/menu_first"
                android:title="回到首页" />
        <item
                android:id="@+id/menu_exit"
                android:title="退出" />
        <item
                android:id="@+id/menu_test"
                android:title="生成测试数据" />
    </group>
</menu>
```

```java
//添加menu菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();  //获得menu容器
        inflater.inflate(R.menu.menu, menu);//用menu.xml填充menu容器
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_add) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Add())
                    .commit();
            return true;
        } else if (itemId == R.id.menu_clear) {    //删除全部待办事项
            showClearAll();
            return true;
        } else if (itemId == R.id.menu_exit) { //退出
            showExit();
            return true;
        } else if (itemId == R.id.menu_test) { //生成测试数据
            showTestData();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RemindList())
                    .commit();
            return true;
        } else if (itemId == R.id.menu_list_todo) {     //按待办时间顺序列出全部待办事项
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AllListByToDoTime())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.menu_list_create) {     //按创建时间顺序列出全部待办事项
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AllListByCreateTime())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.menu_today) {  //列出今日提醒
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TodayList())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.menu_first) { //回到首页
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RemindList())
                    .commit();
            return true;
        } else if (itemId == R.id.menu_undo) { //列出未处理事项
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UndoList())
                    .commit();
            return true;
        } else if (itemId == R.id.menu_done) { //列出未处理事项
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DoneList())
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155458977.png" alt="image-20231128155458977" style="zoom:50%;" />

## 2.3 添加新事项

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:background="#B28FCE">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="#211E55"
        android:id="@+id/tvTaskID"
        android:text="✡添加新事项"
        android:textSize="100px"/>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:paddingTop="@dimen/activity_vertical_margin"
        android:background="#B28FCE">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="待办事项"
            android:textSize="60px" />
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/etAddTask" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="@dimen/activity_vertical_margin"
        android:orientation="horizontal"    >
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="提醒日期 "
            android:textSize="60px"/>
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="numberDecimal"
            android:hint="单击此处设置"
            android:focusable="false"
            android:id="@+id/etAddDate" />
    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="@dimen/activity_vertical_margin"
        android:orientation="horizontal"    >
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="提醒时间"
            android:textSize="60px"/>
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="单击此处设置"
            android:focusable="false"
            android:id="@+id/etAddTime" />
    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"    >
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="备  注"
            android:textSize="60px"/>
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingTop="@dimen/activity_vertical_margin"
            android:paddingLeft="@dimen/activity_horizontal_margin"
            android:paddingRight="@dimen/activity_horizontal_margin"
            android:text=""
            android:id="@+id/etAddRemark"/>
    </LinearLayout>

    <LinearLayout
        android:layout_below="@+id/person_classEdit"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        >

        <Button
            android:id="@+id/btnAddCancel"
            android:layout_width="118dp"
            android:layout_height="wrap_content"
            android:background="#B481BB"
            android:text="取消"
            android:textSize="20dp" />

        <Button
            android:id="@+id/btnAkhkjfhsd"
            android:layout_width="20dp"
            android:layout_height="wrap_content"
            android:background="#B28FCE"
            android:textSize="20dp" />

        <Button
            android:id="@+id/btnAdd"
            android:layout_width="230dp"
            android:layout_height="wrap_content"
            android:background="#B481BB"
            android:text="确定添加       "
            android:textSize="20dp" />

    </LinearLayout>

</LinearLayout>

```

```java
public class Add extends Fragment {
    public Add(){}
    private MyDBOpenHelper dbOpenHelper;
    private Button btnAdd, btnCancel;
    private EditText remindTitleEdit, dateEdit, timeEdit, remindTextEdit;
    private Calendar createDate,remindDate;
    private SimpleDateFormat dateFormatter,timeFormatter;
    private int notificationID=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add, container, false);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCancel = (Button) rootView.findViewById(R.id.btnAddCancel);
        remindTitleEdit = (EditText) rootView.findViewById(R.id.etAddTask);
        dateEdit = (EditText) rootView.findViewById(R.id.etAddDate);
        timeEdit = (EditText) rootView.findViewById(R.id.etAddTime);
        remindTextEdit = (EditText) rootView.findViewById(R.id.etAddRemark);

        dbOpenHelper=new MyDBOpenHelper(getActivity().getApplicationContext());

        dateFormatter = new SimpleDateFormat ("yyyy年MM月dd日");
        timeFormatter = new SimpleDateFormat ("HH:mm:ss");
        createDate=Calendar.getInstance();
        createDate.setTimeInMillis(System.currentTimeMillis());
        remindDate=Calendar.getInstance();
        remindDate.setTimeInMillis(remindDate.getTimeInMillis()+1000*60);
        dateEdit.setText(dateFormatter.format(new Date(remindDate.getTimeInMillis())));
        timeEdit.setText(timeFormatter.format(new Date(remindDate.getTimeInMillis())));

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) { remindDate.set(year,month,day);dateEdit.setText(dateFormatter.format(new Date(remindDate.getTimeInMillis()))); }},remindDate.get(Calendar.YEAR),remindDate.get(Calendar.MONTH),remindDate.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) { remindDate.set(remindDate.get(Calendar.YEAR),remindDate.get(Calendar.MONTH),remindDate.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);timeEdit.setText(timeFormatter.format(new Date(remindDate.getTimeInMillis())));Log.e("待办事项-设置提醒时间",remindDate.getTime().toString()); }},remindDate.get(Calendar.HOUR_OF_DAY),remindDate.get(Calendar.MINUTE),true).show(); }});

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RemindList())
                        .commit();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SQLiteDatabase dbWriter=dbOpenHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("remindTitle", remindTitleEdit.getText().toString());
                cv.put("createDate",longDateFormatter.format(new Date(System.currentTimeMillis())));
                cv.put("modifyDate",longDateFormatter.format(new Date(System.currentTimeMillis())));
                cv.put("remindDate",longDateFormatter.format(remindDate.getTimeInMillis()));
                cv.put("remindText",remindTextEdit.getText().toString());
                dbWriter.insert("tb_ToDoItem",null, cv);
                dbWriter.close();
                startTimeService(remindDate.getTimeInMillis()-System.currentTimeMillis(),
                        remindTitleEdit.getText().toString(),remindTextEdit.getText().toString());
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RemindList())
                        .commit();

            }
        });
        return rootView;
    }

    private void startTimeService(Long time,String title,String text){
        int notificationID;
        SQLiteDatabase dbRead=(new MyDBOpenHelper(getActivity().getApplicationContext())).getReadableDatabase();
        Intent intent=new Intent(getActivity().getApplicationContext(),TimeService.class);
        Cursor result=dbRead.query("tb_Remind",new String[]{"notificationID"},null,null,null,null,null,null);
        if (result.moveToFirst()){
            notificationID=result.getInt(0);
            SQLiteDatabase dbWriter=(new MyDBOpenHelper(getActivity().getApplicationContext())).getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("notificationID", notificationID+1);
            dbWriter.update("tb_Remind", cv,null, null);  //为了显示多条Notification，每次通知完，通知ID递增一下，避免消息覆盖掉
            dbWriter.close();
        }else {
            notificationID=0;
        }
        dbRead.close();
        intent.putExtra("time", time);
        intent.putExtra("title",title);
        intent.putExtra("text",text);
        intent.putExtra("notificationID",notificationID);    //传递参数
        getActivity().startService(intent);  //启动Service
    }

}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155626261.png" alt="image-20231128155626261" style="zoom:67%;" />

## 2.4 今日提醒

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:background="#EB7A77">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:padding="10dp"
            android:text="今天："
            android:textColor="#ffffff"
            android:textSize="25sp" />
        <TextView
            android:id="@+id/tvToday"
            android:layout_width="186dp"
            android:layout_height="match_parent"
            android:layout_gravity="right|bottom"
            android:layout_weight="1"
            android:padding="10dp"
            android:textColor="#ffffff"
            android:textSize="25sp"/>
    </LinearLayout>
    <View style="@style/divider_horizontal"    />
    <ListView
        android:id="@+id/listTodayToDo"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>

```

```java
public class TodayList extends Fragment {
    public TodayList() {
    }
    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView ListTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today_list, container, false);
        ListTask = (ListView) rootView.findViewById(R.id.listTodayToDo);
        TextView tvToday= (TextView) rootView.findViewById(R.id.tvToday);
        SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy年MM月dd日");
        tvToday.setText(dateFormatter.format(new Date(System.currentTimeMillis())));
        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();
        readToDoList();
        return rootView;
    }
    protected void readToDoList(){
        SimpleDateFormat dayFormatter = new SimpleDateFormat ("yyyy-MM-dd");
        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{
                        "_id","remindTitle","createDate","modifyDate","remindText","remindDate","haveDo"},
                null,null,null,null,"createDate",null);
        while(result.moveToNext()){
            if (result.getString (5).substring(0,10).compareTo(dayFormatter.format(new Date(System.currentTimeMillis())))==0){
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("_id", String.valueOf(result.getInt(0)));
                temp.put("remindTitle", result.getString(1));
                temp.put("createDate", "创建时间：" + result.getString(2));
                temp.put("modifyDate", "最后修改时间："+result.getString (3));
                temp.put("remindText", "备注：" + result.getString(4));
                temp.put("remindDate", "时间："+result.getString(5));
                temp.put("haveDo", result.getInt(6)==0?"该事项未处理":"该事项已经处理");
                taskList.add(temp);
            }
        }
        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.today_list_item,
                new String[] {"remindDate", "remindTitle","remindText","haveDo"},
                new int[]{R.id.remind_listitem_remindDate,R.id.remind_listitem_taskTitle,R.id.remind_listitem_taskText,R.id.remind_listitem_haveDo} );
        ListTask.setAdapter(listViewAdapter);//将查询到的结果显示到ListView控件中
        ListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击修改列表项
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");
                Log.d("待办小助手--我的提示", taskID);
                Cursor result=dbRead.query("tb_ToDoItem",null,
                        "_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> findByID = new HashMap<String,String>();
                findByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                findByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                findByID.put("modifyDate", "最后修改："+ll+"\n");
                findByID.put("remindText", "备注：" + result.getString(4)+"\n");
                findByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                findByID.put("haveDo", result.getInt(6)==0?"该事项未处理":"该事项已经处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(findByID.get("remindTitle")+findByID.get("createDate")+findByID.get("modifyDate")+findByID.get("remindText")+findByID.get("remindDate")+findByID.get("haveDo"))
                        .setNegativeButton("设为已处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",1);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new TodayList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update update = new Update();
                                update.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, update)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });

        ListTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new TodayList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });

    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155722032.png" alt="image-20231128155722032" style="zoom:67%;" />

## 2.5 列出全部事项

### 按待办时间顺序列出

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <ListView
        android:id="@+id/listAllToDo"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>

```

```java
public class AllListByToDoTime extends Fragment {

    public AllListByToDoTime() {
    }
    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView ListTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_list, container, false);
        ListTask = rootView.findViewById(R.id.listAllToDo);
        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();
        readToDoList();
        return rootView;
    }
    protected void readToDoList(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{"_id","remindTitle","createDate","modifyDate","remindText","remindDate","haveDo"}, null,null,null,null,"remindDate",null);
        if (result.getCount() == 0 ){
            Toast.makeText(getActivity().getApplicationContext(),"数据库中无数据！", Toast.LENGTH_SHORT).show();
            return;
        }else {
            while(result.moveToNext()){
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("_id", String.valueOf(result.getInt(0)));
                temp.put("remindTitle", result.getString (1));
                temp.put("createDate", "创建时间："+result.getString (2));
                temp.put("modifyDate", "最后修改时间："+result.getString (3));
                temp.put("remindText", "备注：" + result.getString(4));
                temp.put("remindDate", "设定的办理时间："+result.getString (5));
                temp.put("haveDo", result.getInt(6)==0?"×未处理":"√已处理");
                taskList.add(temp);
            }
        }

        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.all_list_item,
                new String[] {"remindTitle","createDate", "remindDate","haveDo","remindText"},
                new int[]{R.id.listitem_task,R.id.listitem_createDate,R.id.listitem_remindDate,R.id.listitem_haveDo,R.id.listitem_remark} );
        ListTask.setAdapter(listViewAdapter);//将查询到的结果显示到ListView控件中

        ListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击修改列表项
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");    //获取点击的提醒项ID
                Cursor result=dbRead.query("tb_ToDoItem",null,"_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> itemFindByID = new HashMap<String,String>();
                itemFindByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                itemFindByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                itemFindByID.put("modifyDate", "最后修改："+ll+"\n");
                itemFindByID.put("remindText", "备注：" + result.getString(4)+"\n");
                itemFindByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                itemFindByID.put("haveDo", result.getInt(6)==0?"×未处理":"√已处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(itemFindByID.get("remindTitle")+itemFindByID.get("createDate")+itemFindByID.get("modifyDate")+itemFindByID.get("remindText")+itemFindByID.get("remindDate")+itemFindByID.get("haveDo"))
                        .setNegativeButton("设为已处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",1);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update update = new Update();
                                update.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, update)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });

        ListTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                String remindTitle=temp.get("remindTitle");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?"+"\n\n待办事项标题："+ remindTitle)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });


        ListTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View itemView=view;
                final int itemPosition=position;
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                readToDoList();
                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }
}
```



### 按创建<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155805711.png" alt="image-20231128155805711" style="zoom:67%;" />时间顺序列出

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <ListView
        android:id="@+id/listAllToDo"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>

```

```java
public class AllListByCreateTime extends Fragment {

    public AllListByCreateTime() {
    }

    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView ListTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_list, container, false);
        ListTask = rootView.findViewById(R.id.listAllToDo);
        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();
        readToDoList();
        return rootView;
    }
    protected void readToDoList(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{"_id","remindTitle","createDate","modifyDate","remindText","remindDate","haveDo"}, null,null,null,null,"createDate",null);
        if (result.getCount() == 0 ){
            Toast.makeText(getActivity().getApplicationContext(),"数据库中无数据！", Toast.LENGTH_SHORT).show();
            return;
        }else {
            while(result.moveToNext()){
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("_id", String.valueOf(result.getInt(0)));
                temp.put("remindTitle", result.getString (1));
                temp.put("createDate", "创建时间："+result.getString (2));
                temp.put("modifyDate", "最后修改时间："+result.getString (3));
                temp.put("remindText", "备注：" + result.getString(4));
                temp.put("remindDate", "设定的办理时间："+result.getString (5));
                temp.put("haveDo", result.getInt(6)==0?"×未处理":"√已处理");
                taskList.add(temp);
            }
        }

        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.all_list_item,
                new String[] {"remindTitle","createDate", "remindDate","haveDo","remindText"},
                new int[]{R.id.listitem_task,R.id.listitem_createDate,R.id.listitem_remindDate,R.id.listitem_haveDo,R.id.listitem_remark} );
        ListTask.setAdapter(listViewAdapter);//将查询到的结果显示到ListView控件中
        ListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击修改列表项
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");    //获取点击的提醒项ID
                Cursor result=dbRead.query("tb_ToDoItem",null,"_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> itemFindByID = new HashMap<String,String>();
                itemFindByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                itemFindByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                itemFindByID.put("modifyDate", "最后修改："+ll+"\n");
                itemFindByID.put("remindText", "备注：" + result.getString(4)+"\n");
                itemFindByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                itemFindByID.put("haveDo", result.getInt(6)==0?"×未处理":"√已处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(itemFindByID.get("remindTitle")+itemFindByID.get("createDate")+itemFindByID.get("modifyDate")+itemFindByID.get("remindText")+itemFindByID.get("remindDate")+itemFindByID.get("haveDo"))
                        .setNegativeButton("设为已处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",1);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update update = new Update();
                                update.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, update)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });

        ListTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                String remindTitle=temp.get("remindTitle");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?"+"\n\n待办事项标题："+ remindTitle)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });

        ListTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View itemView=view;
                final int itemPosition=position;
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                readToDoList();
                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155847660.png" alt="image-20231128155847660" style="zoom:67%;" />

## 2.6 未处理事项

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/scrollView"
        android:fadingEdge="none"
        android:scrollbars="vertical">
        <LinearLayout
            android:id="@+id/remindLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical" >
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="今天："
                    android:textColor="#ffffff"
                    android:textSize="25sp" />

                <TextView
                    android:id="@+id/tvToday"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoToday"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="明天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="后天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#Eb7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="未来:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterAll"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterAll"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
        </LinearLayout>
    </ScrollView>
</LinearLayout>

```

```java
public class UndoList extends Fragment {

    public UndoList() {
    }
    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView listToDoToday,listToDoTomorrow,listToDoAfterTomorrow,listToDoAfterAll;
    private TextView tvToday,tvTomorrow,tvAfterTomorrow,tvAfterAll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remind_list, container, false);
        tvToday=(TextView)rootView.findViewById(R.id.tvToday);
        tvTomorrow=(TextView)rootView.findViewById(R.id.tvTomorrow);
        tvAfterTomorrow=(TextView)rootView.findViewById(R.id.tvAfterTomorrow);
        tvAfterAll=(TextView)rootView.findViewById(R.id.tvAfterAll);

        listToDoToday = (ListView) rootView.findViewById(R.id.listToDoToday);
        listToDoTomorrow = (ListView) rootView.findViewById(R.id.listToDoTomorrow);
        listToDoAfterTomorrow = (ListView) rootView.findViewById(R.id.listToDoAfterTomorrow);
        listToDoAfterAll = (ListView) rootView.findViewById(R.id.listToDoAfterAll);

        SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy年MM月dd日");
        tvToday.setText(dateFormatter.format(new Date(System.currentTimeMillis())));//获取今天的日期
        tvTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000)));//获取明天的日期
        tvAfterTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*2)));//获取后天的日期
        tvAfterAll.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*3))+"之后");

        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();  //获得一个只读的SQLiteDatabase对象

        readToDoList(new Date(System.currentTimeMillis()),listToDoToday,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000),listToDoTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000*2),listToDoAfterTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()),listToDoAfterAll,1);
        return rootView;
    }

    protected void readToDoList(Date toDoDay,ListView toDoList,int i){
        SimpleDateFormat dayFormatter = new SimpleDateFormat ("yyyy-MM-dd");
        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{"_id","remindTitle","remindText","remindDate","haveDo"}, "haveDo=?",new String[]{"0"},null,null,"remindDate",null);
        if(i==0){
            while(result.moveToNext()){
                if (result.getString (3).substring(0,10).compareTo(dayFormatter.format(toDoDay))==0){
                    HashMap<String,String> temp = new HashMap<String,String>();
                    temp.put("_id", String.valueOf(result.getInt(0)));
                    temp.put("remindTitle", result.getString (1));
                    temp.put("remindDate", "提醒时间："+result.getString (3).substring(11));
                    temp.put("remindText", "备注：" + result.getString(2));
                    temp.put("taskHaveDo", result.getInt(4)==0?"该事项未处理":"该事项已经处理");
                    taskList.add(temp);
                }
            }
        }else if(i==1){    //显示三天之后的提醒项
            while(result.moveToNext()){
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date remindDay = null,today=null;
                try {
                    remindDay = dateFormatter.parse(result.getString(3));
                    today=dayFormatter.parse(dayFormatter.format(toDoDay));
                    if(((remindDay.getTime() - today.getTime())/(24*3600*1000)) >=3) { //三天之后
                        HashMap<String,String> temp = new HashMap<String,String>();
                        temp.put("_id", String.valueOf(result.getInt(0)));
                        temp.put("remindTitle", result.getString (1));
                        temp.put("remindDate", "提醒时间："+result.getString (3));
                        temp.put("remindText", "备注：" + result.getString(2));
                        temp.put("taskHaveDo", result.getInt(4)==0?"该事项未处理":"该事项已经处理");
                        taskList.add(temp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.remind_list_item,
                new String[] {"remindDate", "remindTitle","remindText","taskHaveDo"},
                new int[]{R.id.remind_listitem_remindDate,R.id.remind_listitem_taskTitle,R.id.remind_listitem_taskText,R.id.remind_listitem_haveDo} );
        toDoList.setAdapter(listViewAdapter);//将查询到的结果显示到ListView控件中
        setListViewHeight(toDoList);   //让多个Listview同时使用一个滚动条,重新设置Listview高度，
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击修改列表项
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");
                Cursor result=dbRead.query("tb_ToDoItem",null,
                        "_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> findByID = new HashMap<String,String>();
                findByID.put("id", "ID："+String.valueOf(result.getInt(0))+"\n");
                findByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                findByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                findByID.put("modifyDate", "最后修改："+ll+"\n");
                findByID.put("remindText", "备注：" + result.getString(4)+"\n");
                findByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                findByID.put("haveDo", result.getInt(6)==0?"该事项未处理":"该事项已经处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(findByID.get("remindTitle")+findByID.get("createDate")+findByID.get("modifyDate")+findByID.get("remindText")+findByID.get("remindDate")+findByID.get("haveDo"))
                        .setNegativeButton("设为已处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",1);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new UndoList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update update = new Update();
                                update.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, update)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View itemView=view;
                final int itemPosition=position;
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new UndoList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
    }
    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter= listview.getAdapter();
        if(null != adapter) {
            for (int i = 0; i <adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);//注意listview子项必须为LinearLayout才能使用
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }
}

```



<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128155929411.png" alt="image-20231128155929411" style="zoom:67%;" />

## 2.7 已处理事项

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/scrollView"
        android:fadingEdge="none"
        android:scrollbars="vertical">
        <LinearLayout
            android:id="@+id/remindLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical" >
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="今天："
                    android:textColor="#ffffff"
                    android:textSize="25sp" />

                <TextView
                    android:id="@+id/tvToday"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoToday"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="明天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="后天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#Eb7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="未来:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterAll"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterAll"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
        </LinearLayout>
    </ScrollView>
</LinearLayout>

```



```java
public class DoneList extends Fragment {

    public DoneList() {
    }
    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView listToDoToday,listToDoTomorrow,listToDoAfterTomorrow,listToDoAfterAll;
    private TextView tvToday,tvTomorrow,tvAfterTomorrow,tvAfterAll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remind_list, container, false);
        tvToday=(TextView)rootView.findViewById(R.id.tvToday);
        tvTomorrow=(TextView)rootView.findViewById(R.id.tvTomorrow);
        tvAfterTomorrow=(TextView)rootView.findViewById(R.id.tvAfterTomorrow);
        tvAfterAll=(TextView)rootView.findViewById(R.id.tvAfterAll);

        listToDoToday = (ListView) rootView.findViewById(R.id.listToDoToday);
        listToDoTomorrow = (ListView) rootView.findViewById(R.id.listToDoTomorrow);
        listToDoAfterTomorrow = (ListView) rootView.findViewById(R.id.listToDoAfterTomorrow);
        listToDoAfterAll = (ListView) rootView.findViewById(R.id.listToDoAfterAll);

        SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy年MM月dd日");
        tvToday.setText(dateFormatter.format(new Date(System.currentTimeMillis())));//获取今天的日期
        tvTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000)));//获取明天的日期
        tvAfterTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*2)));//获取后天的日期
        tvAfterAll.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*3))+"之后");

        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();  //获得一个只读的SQLiteDatabase对象

        readToDoList(new Date(System.currentTimeMillis()),listToDoToday,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000),listToDoTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000*2),listToDoAfterTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()),listToDoAfterAll,1);
        return rootView;
    }

    protected void readToDoList(Date toDoDay,ListView toDoList,int i){
        SimpleDateFormat dayFormatter = new SimpleDateFormat ("yyyy-MM-dd");

        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{
                        "_id","remindTitle","remindText","remindDate","haveDo"},
                "haveDo=?",new String[]{"1"},null,null,"remindDate",null);//查询结果以remindDate排序
        if(i==0){
            while(result.moveToNext()){
                if (result.getString (3).substring(0,10).compareTo(dayFormatter.format(toDoDay))==0){
                    HashMap<String,String> temp = new HashMap<String,String>();
                    temp.put("_id", String.valueOf(result.getInt(0)));
                    temp.put("remindTitle", result.getString (1));
                    temp.put("remindDate", "提醒时间："+result.getString (3).substring(11));
                    temp.put("remindText", "备注：" + result.getString(2));
                    temp.put("taskHaveDo", result.getInt(4)==0?"×未处理":"√已处理");
                    taskList.add(temp);
                }
            }
        }else if(i==1){    //显示三天之后的提醒项
            while(result.moveToNext()){
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date remindDay = null,today=null;
                try {
                    remindDay = dateFormatter.parse(result.getString(3));
                    today=dayFormatter.parse(dayFormatter.format(toDoDay));
                    if(((remindDay.getTime() - today.getTime())/(24*3600*1000)) >=3) { //三天之后
                        HashMap<String,String> temp = new HashMap<String,String>();
                        temp.put("_id", String.valueOf(result.getInt(0)));
                        temp.put("remindTitle", result.getString (1));
                        temp.put("remindDate", "提醒时间："+result.getString (3));
                        temp.put("remindText", "备注：" + result.getString(2));
                        temp.put("taskHaveDo", result.getInt(4)==0?"该事项未处理":"该事项已经处理");
                        taskList.add(temp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.remind_list_item,
                new String[] {"remindDate", "remindTitle","remindText","taskHaveDo"},
                new int[]{R.id.remind_listitem_remindDate,R.id.remind_listitem_taskTitle,R.id.remind_listitem_taskText,R.id.remind_listitem_haveDo} );
        toDoList.setAdapter(listViewAdapter);//将查询到的结果显示到ListView控件中
        setListViewHeight(toDoList);   //让多个Listview同时使用一个滚动条,重新设置Listview高度，

        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击修改列表项
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //获取点击项：
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");
                Log.d("待办小助手--我的提示", taskID);
                Cursor result=dbRead.query("tb_ToDoItem",null, "_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> findByID = new HashMap<String,String>();
                findByID.put("id", "ID："+String.valueOf(result.getInt(0))+"\n");
                findByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                findByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                findByID.put("modifyDate", "最后修改："+ll+"\n");
                findByID.put("remindText", "备注：" + result.getString(4)+"\n");
                findByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                findByID.put("haveDo", result.getInt(6)==0?"该事项未处理":"该事项已经处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(findByID.get("remindTitle")+findByID.get("createDate")+findByID.get("modifyDate")+findByID.get("remindText")+findByID.get("remindDate")+findByID.get("haveDo"))
                        .setNegativeButton("设为未处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",0);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new DoneList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update update = new Update();
                                update.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, update)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });

        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View itemView=view;
                final int itemPosition=position;
                //获取点击项：
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new DoneList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
    }

    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter= listview.getAdapter();
        if(null != adapter) {
            for (int i = 0; i <adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);//注意listview子项必须为LinearLayout才能调用该方法
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128160031742.png" alt="image-20231128160031742" style="zoom:67%;" />



## 2.8 删除全部事项

```java
private void showClearAll(){
        new AlertDialog.Builder(MainActivity.this).setTitle("警告")
                .setMessage("数据删除之后将无法恢复！！\n您确定要删除全部事项吗?")
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase dbWriter=dbOpenHelper.getWritableDatabase();
                        dbWriter.delete("tb_ToDoItem",null,null);
                        Toast.makeText(getApplicationContext(), "数据已经全部删除！", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new RemindList())
                                .commit();
                        dbWriter.close();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
    private void showExit(){
        AlertDialog.Builder exitAlert=new AlertDialog.Builder(MainActivity.this);
        exitAlert.setTitle("警告");
        exitAlert.setMessage("您确定要退出吗?");
        exitAlert.setNeutralButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                MainActivity.this.finish();
            }

        });
        exitAlert.setNegativeButton("取消", null);
        exitAlert.create();
        exitAlert.show();
    }
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128160106656.png" alt="image-20231128160106656" style="zoom:67%;" />

## 2.9 回到首页

```xml
<?xml version="2.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/scrollView"
        android:fadingEdge="none"
        android:scrollbars="vertical">
        <LinearLayout
            android:id="@+id/remindLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical" >
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="今天："
                    android:textColor="#ffffff"
                    android:textSize="25sp" />

                <TextView
                    android:id="@+id/tvToday"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoToday"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="明天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp" />
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#EB7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="后天:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterTomorrow"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterTomorrow"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
            <View
                style="@style/divider_horizontal"
                />
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="#Eb7A77">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:padding="10dp"
                    android:text="未来:"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
                <TextView
                    android:id="@+id/tvAfterAll"
                    android:layout_width="186dp"
                    android:layout_height="match_parent"
                    android:layout_gravity="right|bottom"
                    android:layout_weight="1"
                    android:padding="10dp"
                    android:textColor="#ffffff"
                    android:textSize="25sp"/>
            </LinearLayout>
            <View
                style="@style/divider_horizontal"
                />
            <ListView
                android:id="@+id/listToDoAfterAll"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
        </LinearLayout>
    </ScrollView>
</LinearLayout>

```

```java
public class RemindList extends Fragment {

    public RemindList() {
    }
    private SQLiteDatabase dbRead;
    private MyDBOpenHelper dbOpenHelper;
    private ListView listToDoToday,listToDoTomorrow,listToDoAfterTomorrow,listToDoAfterAll;
    private TextView tvToday,tvTomorrow,tvAfterTomorrow,tvAfterAll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remind_list, container, false);
        tvToday=(TextView)rootView.findViewById(R.id.tvToday);
        tvTomorrow=(TextView)rootView.findViewById(R.id.tvTomorrow);
        tvAfterTomorrow=(TextView)rootView.findViewById(R.id.tvAfterTomorrow);
        tvAfterAll=(TextView)rootView.findViewById(R.id.tvAfterAll);

        listToDoToday = (ListView) rootView.findViewById(R.id.listToDoToday);
        listToDoTomorrow = (ListView) rootView.findViewById(R.id.listToDoTomorrow);
        listToDoAfterTomorrow = (ListView) rootView.findViewById(R.id.listToDoAfterTomorrow);
        listToDoAfterAll = (ListView) rootView.findViewById(R.id.listToDoAfterAll);

        SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy年MM月dd日");
        tvToday.setText(dateFormatter.format(new Date(System.currentTimeMillis())));//获取今天的日期
        tvTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000)));//获取明天的日期
        tvAfterTomorrow.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*2)));//获取后天的日期
        tvAfterAll.setText(dateFormatter.format(new Date(System.currentTimeMillis()+86400000*3))+"之后");
        dbOpenHelper = new MyDBOpenHelper(getActivity().getApplicationContext());
        dbRead= dbOpenHelper.getReadableDatabase();  //获得一个只读的SQLiteDatabase对象
        readToDoList(new Date(System.currentTimeMillis()),listToDoToday,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000),listToDoTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()+86400000*2),listToDoAfterTomorrow,0);
        readToDoList(new Date(System.currentTimeMillis()),listToDoAfterAll,1);
        return rootView;
    }

    protected void readToDoList(Date toDoDay,ListView toDoList,int i){
        SimpleDateFormat dayFormatter = new SimpleDateFormat ("yyyy-MM-dd");
        ArrayList taskList = new ArrayList<HashMap<String,String>>();
        Cursor result=dbRead.query("tb_ToDoItem",new String[]{"_id","remindTitle","remindText","remindDate","haveDo"}, null,null,null,null,"remindDate",null);
        if(i==0){
            while(result.moveToNext()){
                if (result.getString (3).substring(0,10).compareTo(dayFormatter.format(toDoDay))==0){
                    HashMap<String,String> temp = new HashMap<String,String>();
                    temp.put("_id", String.valueOf(result.getInt(0)));
                    temp.put("remindTitle", result.getString (1));
                    temp.put("remindDate", "提醒时间："+result.getString (3).substring(11));
                    temp.put("remindText", "备注：" + result.getString(2));
                    temp.put("taskHaveDo", result.getInt(4)==0?"×未处理":"√已处理");
                    taskList.add(temp);
                }
            }
        }else if(i==1){ //显示三天之后的提醒项
            while(result.moveToNext()){
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date remindDay = null,today=null;
                try {
                    remindDay = dateFormatter.parse(result.getString(3));
                    today=dayFormatter.parse(dayFormatter.format(toDoDay));
                    if(((remindDay.getTime() - today.getTime())/(24*3600*1000)) >=3) { //三天之后
                        HashMap<String,String> temp = new HashMap<String,String>();
                        temp.put("_id", String.valueOf(result.getInt(0)));
                        temp.put("remindTitle", result.getString (1));
                        temp.put("remindDate", "提醒时间："+result.getString (3));
                        temp.put("remindText", "备注：" + result.getString(2));
                        temp.put("taskHaveDo", result.getInt(4)==0?"×未处理":"√已处理");
                        taskList.add(temp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), taskList,R.layout.remind_list_item, new String[] {"remindDate", "remindTitle","remindText","taskHaveDo"},new int[]{R.id.remind_listitem_remindDate,R.id.remind_listitem_taskTitle,R.id.remind_listitem_taskText,R.id.remind_listitem_haveDo} );
        toDoList.setAdapter(listViewAdapter);
        setListViewHeight(toDoList);
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String, String> temp = (HashMap<String, String>) listViewAdapter.getItem(position);
                final String taskID = temp.get("_id");    //获取点击的提醒项ID
                Cursor result=dbRead.query("tb_ToDoItem",null,"_id=? ",new String[]{taskID},null,null,null,null);
                result.moveToFirst();
                HashMap<String,String> itemFindByID = new HashMap<String,String>();
                itemFindByID.put("remindTitle", "标题："+result.getString (1)+"\n");
                itemFindByID.put("createDate", "创建时间："+result.getString (2)+"\n");
                String ll=result.getString (3).equals(" ")?result.getString (2):result.getString (3);
                itemFindByID.put("modifyDate", "最后修改："+ll+"\n");
                itemFindByID.put("remindText", "备注：" + result.getString(4)+"\n");
                itemFindByID.put("remindDate", "提醒时间："+result.getString (5)+"\n");
                itemFindByID.put("haveDo", result.getInt(6)==0?"×未处理":"√已处理");
                new AlertDialog.Builder(getActivity())
                        .setTitle("详细信息")
                        .setMessage(itemFindByID.get("remindTitle")+itemFindByID.get("createDate")+itemFindByID.get("modifyDate")+itemFindByID.get("remindText")+itemFindByID.get("remindDate")+itemFindByID.get("haveDo"))
                        .setNegativeButton("设为已处理", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("haveDo",1);
                                dbWriter.update("tb_ToDoItem", cv,"_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNeutralButton("修改该项内容", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                final Bundle bundle = new Bundle();
                                bundle.putString("taskID", taskID);
                                Update updateFragment = new Update();
                                updateFragment.setArguments(bundle);
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, updateFragment)   //R.id.container是Fragment的容器
                                        .addToBackStack(null) //为了支持回退键
                                        .commit();
                            }

                        })
                        .setPositiveButton("关闭窗口", null)
                        .create()
                        .show();
            }
        });

        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按删除列表项

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> temp = (HashMap<String,String>)listViewAdapter.getItem(position);
                final String taskID=temp.get("_id");
                String remindTitle=temp.get("remindTitle");
                Log.d("待办小助手--我的提示（删除）",taskID+"---"+remindTitle+"---");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("您要删除这条待办事项吗?"+"\n\n待办事项标题："+ remindTitle)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SQLiteDatabase dbWriter = dbOpenHelper.getWritableDatabase();
                                dbWriter.delete("tb_ToDoItem", "_id=?", new String[]{taskID});
                                dbWriter.close();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new RemindList())
                                        .commit();
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
    }

    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter= listview.getAdapter();
        if(null != adapter) {
            for (int i = 0; i <adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);//注意listview子项必须为LinearLayout才能调用该方法
                    totalHeight += listItem.getMeasuredHeight();
                }
            }

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }

}
```



## 2.10 退出

```java
    private void showExit(){
        AlertDialog.Builder exitAlert=new AlertDialog.Builder(MainActivity.this);
        exitAlert.setTitle("警告");
        exitAlert.setMessage("您确定要退出吗?");
        exitAlert.setNeutralButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                MainActivity.this.finish();
            }

        });
        exitAlert.setNegativeButton("取消", null);
        exitAlert.create();
        exitAlert.show();
    }
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128160148924.png" alt="image-20231128160148924" style="zoom:50%;" />

## 2.11 生成测试数据

````java
    private void showTestData() {
        MyDBOpenHelper dbOpenHelper=new MyDBOpenHelper(this);
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        remindDate=Calendar.getInstance();
        String yesterday=dateFormatter.format(new Date(remindDate.getTimeInMillis()-86400000));//昨天这个时候
        String today=dateFormatter.format(new Date(remindDate.getTimeInMillis()+60000));//今天一分钟后
        String today2=dateFormatter.format(new Date(remindDate.getTimeInMillis()+3600000));//今天今天一小时后
        String tomorrow=dateFormatter.format(new Date(remindDate.getTimeInMillis()+86400000));//明天这个时候
        String aftertomorrow1=dateFormatter.format(new Date(remindDate.getTimeInMillis()+86400000*2));//后天这个时候
        String aftertomorrow2=dateFormatter.format(new Date(remindDate.getTimeInMillis()+86400000*3));//大后天这个时候

        String sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('" + yesterday + "','宋文杰-2309312117','信息工程系-计算机专升本 2301 班','" + today + "');";
        db.execSQL(sql_insert);

        sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('"+yesterday+"','开会','评选优秀员工','"+today+"');";
        db.execSQL(sql_insert);
        StartNotification.startTimeService(new Long(60000),"下午2点开会","会议议题,评选优秀员工",this);

        sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('"+yesterday+"','晚上吃饭','火锅','"+today2+"');";
        db.execSQL(sql_insert);
        StartNotification.startTimeService(new Long(1000*60*60),"晚上吃饭","火锅",this);

        sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('"+yesterday+"','实验','操作系统','"+tomorrow+"');";
        db.execSQL(sql_insert);
        StartNotification.startTimeService(new Long(1000*60*60),"实验","操作系统",this);

        sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('"+yesterday+"','画画','水彩','"+aftertomorrow1+"');";
        db.execSQL(sql_insert);
        StartNotification.startTimeService(new Long(1000*60*60),"画画","水彩",this);
        sql_insert="insert into tb_ToDoItem(createDate,remindTitle,remindText,remindDate) " +
                "values ('"+yesterday+"','实验报告','嵌入式计算器','"+aftertomorrow2+"');";
        db.execSQL(sql_insert);
        StartNotification.startTimeService(new Long(1000*60*60),"实验报告","计算器",this);
    }
````

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128160234142.png" alt="image-20231128160234142" style="zoom:50%;" />