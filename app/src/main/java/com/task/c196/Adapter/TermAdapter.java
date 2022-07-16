package com.task.c196.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.task.c196.Entity.Term;
import com.task.c196.R;
import com.task.c196.UI.TermDetailActivity;
import java.text.SimpleDateFormat;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.listItemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term current = terms.get(position);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String start = formatter.format(current.getStart());
                    String end = formatter.format(current.getEnd());

                    Intent intent = new Intent(context, TermDetailActivity.class);
                    intent.putExtra("id", String.valueOf(current.getId()));
                    intent.putExtra("name", current.getName());
                    intent.putExtra("start", start);
                    intent.putExtra("end", end);
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Term> terms;
    private final Context context;
    private final LayoutInflater inflater;

    public TermAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        if (terms != null) {
            Term current = terms.get(position);
            holder.termItemView.setText(current.getName());
        } else {
            holder.termItemView.setText("MISSING TERM TITLE.");
        }
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (terms != null) {
            return terms.size();
        } else {
            return 0;
        }
    }
}