# 记事本

| 信息工程系-计算机专升本 2301 班 | 王文照 (2309312121) |
| ------------------------------- | ------------------- |

- 通过 Fragment 实现多页面切换 
- 多种菜单使用 (选项卡、上下文)
- 多个 Adapter 实现笔记分类 (学习、生活、其它)
- Activity 带值传递实现笔记的添加修改删除

## 页面切换

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <androidx.appcompat.widget.Toolbar
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:id="@+id/toolbar"
            app:layout_scrollFlags="scroll|enterAlways"/>

        <com.google.android.material.tabs.TabLayout
            android:id="@+id/tabs"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
    </com.google.android.material.appbar.AppBarLayout>

    <androidx.viewpager.widget.ViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //工具条
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override

    protected void onResume() {

        super.onResume();

        //onCreate(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//这个方法将菜单资源文件中的项增加到应用条
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //得到动作的ID
        if (item.getItemId() == R.id.action_editor_note) {
            Intent intent = new Intent(this, Editor.class);//打开新页面
            startActivity(intent);
            return true;//返回true表示已经处理了所单击的动作源
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        public int getCount(){
            return 4;
        }

        public Fragment getItem(int position){
            if (position == 0) {
                return new AllFragment();
            } else if (position == 1) {
                return new StuFragment();
            } else if (position == 2) {
                return new LifeFragment();
            } else if (position == 3) {
                return new OtherFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return  getResources().getText(R.string.all);
                case 1:
                    return  getResources().getText(R.string.stu);
                case 2:
                    return  getResources().getText(R.string.life);
                case 3:
                    return  getResources().getText(R.string.other);
            }
            return null;
        }
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128182600601.png" alt="image-20231128182600601" style="zoom:67%;" />

## 笔记分类

### 首页

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/pizza_recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical"
/>
```

```java
public class AllFragment extends Fragment {


    public AllFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView notesRecycle = (RecyclerView) inflater.inflate(R.layout.fragment_all, container, false);
        String[] notesName = new String[Note.notes.size()];
        for (int i = 0; i < notesName.length; i++)
            notesName[i] = Note.notes.get(i).getTitle();
        cardJ adapter = new cardJ(notesName);
        notesRecycle.setAdapter(adapter);
        //GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycle.setLayoutManager(layoutManager);
        adapter.setListener(new cardJ.Listener() {
            @Override
            public void onClick(int postion) {
                Intent intent = new Intent(getActivity(), read.class);
                intent.putExtra(read.EXTRA_NOTE_ID, postion);
                intent.putExtra("type", 0);
                getActivity().startActivity(intent);
            }
        });
        return notesRecycle;
    }


}
```

```java
public class cardJ extends RecyclerView.Adapter<cardJ.ViewHolder> {

    private String[] title;
    private Listener listener;

    interface Listener{
        void onClick(int postion);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public cardJ(String[] title) {
        this.title = title;
    }

    public int getItemCount() {
        return title.length;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(title[position]);
        cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183200473.png" alt="image-20231128183200473" style="zoom:67%;" />

### 学习
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/life"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scrollbars="vertical"/>
```

```java
public class StuFragment extends Fragment {


    public StuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView notesRecycle = (RecyclerView) inflater.inflate(R.layout.fragment_stu, container, false);
        String[] notesName = new String[Note.notesStu.size()];
        for (int i = 0; i < notesName.length; i++)
            notesName[i] = Note.notesStu.get(i).getTitle();
        cardS adapter = new cardS(notesName);
        notesRecycle.setAdapter(adapter);
        //GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycle.setLayoutManager(layoutManager);
        adapter.setListener(new cardJ.Listener() {
            @Override
            public void onClick(int postion) {
                Intent intent = new Intent(getActivity(), read.class);
                intent.putExtra(read.EXTRA_NOTE_ID, postion);
                intent.putExtra("type", 1);
                getActivity().startActivity(intent);
            }
        });
        return notesRecycle;
    }
}
```
```java
public class cardS extends RecyclerView.Adapter<cardS.ViewHolder> {

    private String[] title;private cardJ.Listener listener;

    interface Listener{
        void onClick(int postion);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public cardS(String[] title) {
        this.title = title;
    }

    public int getItemCount() {
        return title.length;
    }

    public void setListener(cardJ.Listener listener) {
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stu, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(title[position]);
        cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183225318.png" alt="image-20231128183225318" style="zoom:67%;" />

### 生活
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/life"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scrollbars="vertical"/>
```

```java
public class LifeFragment extends Fragment {


    public LifeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView notesRecycle = (RecyclerView) inflater.inflate(R.layout.fragment_life, container, false);
        String[] notesName = new String[Note.notesLife.size()];
        for (int i = 0; i < notesName.length; i++)
            notesName[i] = Note.notesLife.get(i).getTitle();
        cardL adapter = new cardL(notesName);
        notesRecycle.setAdapter(adapter);
        //GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycle.setLayoutManager(layoutManager);
        adapter.setListener(new cardJ.Listener() {
            @Override
            public void onClick(int postion) {
                Intent intent = new Intent(getActivity(), read.class);
                intent.putExtra(read.EXTRA_NOTE_ID, postion);
                intent.putExtra("type", 2);
                getActivity().startActivity(intent);
            }
        });
        return notesRecycle;
    }

}

```
```java
public class cardL extends RecyclerView.Adapter<cardL.ViewHolder> {

    private String[] title;
    private cardJ.Listener listener;

    interface Listener{
        void onClick(int postion);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public cardL(String[] title) {
        this.title = title;
    }

    public int getItemCount() {
        return title.length;
    }

    public void setListener(cardJ.Listener listener) {
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_life, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(title[position]);
        cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183245875.png" alt="image-20231128183245875" style="zoom:67%;" />

### 其它
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/life"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scrollbars="vertical"/>
```

```java
public class OtherFragment extends Fragment {


    public OtherFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView notesRecycle = (RecyclerView) inflater.inflate(R.layout.fragment_other, container, false);
        String[] notesName = new String[Note.notesOther.size()];
        for (int i = 0; i < notesName.length; i++)
            notesName[i] = Note.notesOther.get(i).getTitle();
        cardO adapter = new cardO(notesName);
        notesRecycle.setAdapter(adapter);
        //GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycle.setLayoutManager(layoutManager);
        adapter.setListener(new cardJ.Listener() {
            @Override
            public void onClick(int postion) {
                Intent intent = new Intent(getActivity(), read.class);
                intent.putExtra(read.EXTRA_NOTE_ID, postion);
                intent.putExtra("type", 3);
                getActivity().startActivity(intent);
            }
        });
        return notesRecycle;
    }

}
```

```java
public class cardO extends RecyclerView.Adapter<cardO.ViewHolder> {

    private String[] title;private cardJ.Listener listener;

    interface Listener{
        void onClick(int postion);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public cardO(String[] title) {
        this.title = title;
    }

    public int getItemCount() {
        return title.length;
    }

    public void setListener(cardJ.Listener listener) {
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_other, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(title[position]);
        cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }
}

```

![](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183301408.png)

## 笔记展示

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".read">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <include
            layout="@layout/toobar_main"
            android:id="@+id/toolbar"/>

        <TextView
            android:layout_marginTop="5dp"
            android:id="@+id/read_title"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:textSize="30sp"
            android:textColor="@color/title"/>

        <TextView
            android:layout_marginTop="5dp"
            android:layout_marginBottom="10dp"
            android:layout_marginRight="8dp"
            android:layout_marginLeft="8dp"
            android:id="@+id/read_text"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:textSize="20sp"
            android:textColor="@color/title" />

    </LinearLayout>

</ScrollView>
```

```java
public class read extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "noteId";
    private ShareActionProvider shareActionProvider;
    public String NoteText;
    public String NoteTitle;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        type= (Integer)getIntent().getExtras().get("type");
        int noteId = (Integer)getIntent().getExtras().get(EXTRA_NOTE_ID);
        switch(type){
            case 0:
                NoteTitle = Note.notes.get(noteId).getTitle();
                NoteText = Note.notes.get(noteId).getText();
                break;
            case 1:
                NoteTitle = Note.notesStu.get(noteId).getTitle();
                NoteText = Note.notesStu.get(noteId).getText();
                break;
            case 2:
                NoteTitle = Note.notesLife.get(noteId).getTitle();
                NoteText = Note.notesLife.get(noteId).getText();
                break;
            case 3:
                NoteTitle = Note.notesOther.get(noteId).getTitle();
                NoteText = Note.notesOther.get(noteId).getText();
                break;
        }

        TextView textView = (TextView)findViewById(R.id.read_title);
        textView.setText(NoteTitle);
        TextView textView2 = (TextView)findViewById(R.id.read_text);
        textView2.setText(NoteText);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        int noteId = (Integer)getIntent().getExtras().get(EXTRA_NOTE_ID);
        Note notes = Note.notes.get(noteId);
        getMenuInflater().inflate(R.menu.menu_read, menu);
        MenuItem menuItem = menu.findItem(R.id.read_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(NoteText);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int noteId = (Integer)getIntent().getExtras().get(EXTRA_NOTE_ID);
        //得到动作的ID
        if (item.getItemId() == R.id.dele) {
            switch (type) {
                case 0:
                    if (Note.notes.get(noteId).getType().equals("学习")) {
                        for (int i = 0; i < Note.notesStu.size(); i++) {
                            if (Note.notesStu.get(i).getTitle().equals(Note.notes.get(noteId).getTitle())
                                    && Note.notesStu.get(i).getText().equals(Note.notes.get(noteId).getText())
                                    && Note.notesStu.get(i).getType().equals(Note.notes.get(noteId).getType())) {
                                Note.notesStu.remove(i);
                                break;
                            }
                        }
                    } else if (Note.notes.get(noteId).getType().equals("生活")) {
                        for (int i = 0; i < Note.notesLife.size(); i++) {
                            if (Note.notesLife.get(i).getTitle().equals(Note.notes.get(noteId).getTitle())
                                    && Note.notesLife.get(i).getText().equals(Note.notes.get(noteId).getText())
                                    && Note.notesLife.get(i).getType().equals(Note.notes.get(noteId).getType())) {
                                Note.notesLife.remove(i);
                                break;
                            }
                        }
                    } else if (Note.notes.get(noteId).getType().equals("其他")) {
                        for (int i = 0; i < Note.notesOther.size(); i++) {
                            if (Note.notesOther.get(i).getTitle().equals(Note.notes.get(noteId).getTitle())
                                    && Note.notesOther.get(i).getText().equals(Note.notes.get(noteId).getText())
                                    && Note.notesOther.get(i).getType().equals(Note.notes.get(noteId).getType())) {
                                Note.notesOther.remove(i);
                                break;
                            }
                        }
                    }
                    Note.notes.remove(noteId);
                    break;
                case 1:
                    for (int i = 0; i < Note.notes.size(); i++) {
                        if (Note.notes.get(i).getTitle().equals(Note.notesStu.get(noteId).getTitle())
                                && Note.notes.get(i).getText().equals(Note.notesStu.get(noteId).getText())
                                && Note.notes.get(i).getType().equals(Note.notesStu.get(noteId).getType())) {
                            Note.notes.remove(i);
                            break;
                        }
                    }
                    Note.notesStu.remove(noteId);
                    break;
                case 2:
                    for (int i = 0; i < Note.notes.size(); i++) {
                        if (Note.notes.get(i).getTitle().equals(Note.notesLife.get(noteId).getTitle())
                                && Note.notes.get(i).getText().equals(Note.notesLife.get(noteId).getText())
                                && Note.notes.get(i).getType().equals(Note.notesLife.get(noteId).getType())) {
                            Note.notes.remove(i);
                            break;
                        }
                    }
                    Note.notesLife.remove(noteId);
                    break;
                case 3:
                    for (int i = 0; i < Note.notes.size(); i++) {
                        if (Note.notes.get(i).getTitle().equals(Note.notesOther.get(noteId).getTitle())
                                && Note.notes.get(i).getText().equals(Note.notesOther.get(noteId).getText())
                                && Note.notes.get(i).getType().equals(Note.notesOther.get(noteId).getType())) {
                            Note.notes.remove(i);
                            break;
                        }
                    }
                    Note.notesOther.remove(noteId);
                    break;
            }

            CharSequence textS = "删除成功！";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, textS, duration);
            toast.show();
            return true;//返回true表示已经处理了所单击的动作源
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareActionIntent(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183658288.png" alt="image-20231128183658288" style="zoom:67%;" />

## 笔记的添加修改删除

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/coordinator"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:contentScrim="?attr/colorPrimary">

            <androidx.appcompat.widget.Toolbar
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:id="@+id/toolbar"
                app:layout_collapseMode="pin"/>

        </com.google.android.material.appbar.CollapsingToolbarLayout>
    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="horizontal">

                <EditText
                    android:layout_marginLeft="40dp"
                    android:id="@+id/edit_title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:ems="10"
                    android:gravity="center"
                    android:hint="@string/edit_title"/>

                <Spinner
                    android:layout_marginTop="5dp"
                    android:layout_marginLeft="10dp"
                    android:id="@+id/spinner"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:entries="@array/type_name"/>
            </LinearLayout>

            <EditText
                android:id="@+id/edit_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:ems="20"
                android:hint="@string/edit_text" />

        </LinearLayout>
    </androidx.core.widget.NestedScrollView>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="end|bottom"
        android:layout_margin="16dp"
        android:src="@drawable/ic_done_white_24dp"
        android:onClick="onClickDone"/>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

```java
public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //工具条
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //向上按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);//打开新页面
        startActivity(intent);
        //super.onBackPressed();
    }

    public void onClickDone(View view){
        CharSequence textSequence = "保存成功!";
        int duration = Toast.LENGTH_SHORT;

        EditText titleView = (EditText) findViewById(R.id.edit_title);
        EditText textView = (EditText) findViewById(R.id.edit_text);
        Spinner typeView = (Spinner) findViewById(R.id.spinner);
        String title = titleView.getText().toString();
        String text = textView.getText().toString();
        String typeName = String.valueOf(typeView.getSelectedItem());
        Note.notes.add(new Note(title, text, typeName));
        if(typeName.equals("学习")){
            Note.notesStu.add(new Note(title, text, typeName));
        }
        else if (typeName.equals("生活")){
            Note.notesLife.add(new Note(title, text, typeName));
        }
        else if (typeName.equals("其他")){
            Note.notesOther.add(new Note(title, text, typeName));
        }
        textView.setText(null);
        titleView.setText(null);


        //Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("name",1);
        //startActivity(intent);

        Toast toast = Toast.makeText(this, textSequence, duration);
        toast.show();
    }
}
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183417346.png" alt="image-20231128183417346" style="zoom:67%;" />

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231128183458147.png" alt="image-20231128183458147" style="zoom:67%;" />
