package jz.cbq.work_note_book.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * MyFragmentPagerAdapter
 *
 * @author cbq
 * @date 2023/11/20 22:49
 * @since 1.0.0
 */
public class MyFragmentPagerAdapter extends FragmentStateAdapter {
    /**
     * Fragment List
     */
    List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fragmentManager,
                                  @NonNull Lifecycle lifecycle,
                                  List<Fragment> fragmentList) {
        super(fragmentManager, lifecycle);
        this.fragmentList = fragmentList;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}