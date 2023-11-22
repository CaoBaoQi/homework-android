package jz.cbq.work_memo_v3;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


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

//    public void itemClicked(long id){
//        View fr = findViewById(R.id.pager);
//        if(fr != null){
//            AllFragment al = new AllFragment();
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//            ft.replace(R.id.pager, al);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
//        }
//    }
}
