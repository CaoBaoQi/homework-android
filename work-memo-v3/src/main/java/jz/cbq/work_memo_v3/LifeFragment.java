package jz.cbq.work_memo_v3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
