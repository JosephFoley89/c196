package com.task.c196.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.c196.Entity.Assessment;
import com.task.c196.R;
import com.task.c196.UI.AssessmentDetailActivity;
import com.task.c196.UI.AssessmentListActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.listItemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment current = assessments.get(position);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String date = formatter.format(current.getDate());

                    Intent intent = new Intent(context, AssessmentDetailActivity.class);
                    intent.putExtra("id", String.valueOf(current.getId()));
                    intent.putExtra("name", current.getName());
                    intent.putExtra("type", current.getType());
                    intent.putExtra("date", date);
                    intent.putExtra("courseID", String.valueOf(current.getCourseID()));
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> assessments;
    private final Context context;
    private final LayoutInflater inflater;

    public AssessmentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (assessments != null) {
            Assessment current = assessments.get(position);
            String title = current.getName();
            holder.assessmentItemView.setText(title);
        } else {
            holder.assessmentItemView.setText("MISSING ASSESSMENT TITLE");
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (assessments != null) {
            return assessments.size();
        } else {
            return 0;
        }
    }
}
