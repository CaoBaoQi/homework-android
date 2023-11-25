package jz.cbq.work_note_book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jz.cbq.work_note_book.activity.RecycleBinActivity;
import jz.cbq.work_note_book.adapter.MyFragmentPagerAdapter;
import jz.cbq.work_note_book.fragment.InAbeyanceFragment;
import jz.cbq.work_note_book.fragment.NoteFragment;

/**
 * 启动
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * ViewPager2
     */
    ViewPager2 viewPager;
    /**
     * 顶部笔记标签
     */
    TextView note;
    /**
     * 顶部待办标签
     */
    TextView in_abeyance;
    /**
     * 回收站按钮
     */
    ImageView recycle_bin;
    /**
     * 标志当前显示笔记页面或者待办页面
     * 0 为笔记 | 1 为待办
     */
    public static int status = 0;

    // 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.light_grey));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        note = findViewById(R.id.note);
        note.setOnClickListener(this);
        in_abeyance = findViewById(R.id.in_abeyance);
        in_abeyance.setOnClickListener(this);
        recycle_bin = findViewById(R.id.recycle_bin);

        recycle_bin.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RecycleBinActivity.class)));

        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewPager.setCurrentItem(status);
    }

    @Override
    protected void onPause() {
        super.onPause();

        status = viewPager.getCurrentItem();
    }

    /**
     * 初始化 ViewPager2
     */
    private void initViewPager() {
        viewPager = findViewById(R.id.mainViewPager2);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(NoteFragment.newInstance());
        fragmentList.add(InAbeyanceFragment.newInstance());

        MyFragmentPagerAdapter myFragmentPagerAdapter =
                new MyFragmentPagerAdapter(getSupportFragmentManager(),
                        getLifecycle(), fragmentList);

        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeFragment(position);
            }
        });
    }

    /**
     * 切换 Fragment
     *
     * @param position position
     */
    @SuppressLint("NonConstantResourceId")
    private void changeFragment(int position) {
        if (position == R.id.note) {
            viewPager.setCurrentItem(0);

            note.setTextColor(note.getResources().getColor(R.color.black));
            in_abeyance.setTextColor(in_abeyance.getResources().getColor(R.color.dark_grey));
            recycle_bin.setVisibility(View.VISIBLE);
        } else if (position == 0) {
            note.setTextColor(note.getResources().getColor(R.color.black));
            in_abeyance.setTextColor(in_abeyance.getResources().getColor(R.color.dark_grey));
            recycle_bin.setVisibility(View.VISIBLE);
        } else if (position == R.id.in_abeyance) {
            viewPager.setCurrentItem(1);

            note.setTextColor(note.getResources().getColor(R.color.dark_grey));
            in_abeyance.setTextColor(in_abeyance.getResources().getColor(R.color.black));
            recycle_bin.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            note.setTextColor(note.getResources().getColor(R.color.dark_grey));
            in_abeyance.setTextColor(in_abeyance.getResources().getColor(R.color.black));
            recycle_bin.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * onClick
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        changeFragment(view.getId());
    }
}