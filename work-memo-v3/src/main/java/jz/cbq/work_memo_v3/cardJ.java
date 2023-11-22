package jz.cbq.work_memo_v3;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
