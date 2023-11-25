package jz.cbq.work_note_book.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jz.cbq.work_note_book.R;

/**
 * 关于我 Fragment
 */
public class AboutMeFragment extends Fragment {

    /**
     * 根视图 rootView
     */
    private View rootView;


    /**
     * @return AboutMeFragment fragment
     */
    public static AboutMeFragment newInstance() {
        return new AboutMeFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        }
        return rootView;
    }
}