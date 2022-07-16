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
import com.task.c196.Entity.Instructor;
import com.task.c196.R;
import com.task.c196.UI.AssessmentDetailActivity;
import com.task.c196.UI.AssessmentListActivity;
import com.task.c196.UI.InstructorDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {
    class InstructorViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private InstructorViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.listItemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Instructor current = instructors.get(position);

                    Intent intent = new Intent(context, InstructorDetailActivity.class);
                    intent.putExtra("id", String.valueOf(current.getId()));
                    intent.putExtra("name", current.getName());
                    intent.putExtra("email", current.getEmail());
                    intent.putExtra("phone", current.getPhone());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Instructor> instructors;
    private final Context context;
    private final LayoutInflater inflater;

    public InstructorAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new InstructorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        if (instructors != null) {
            Instructor current = instructors.get(position);
            String title = current.getName();
            holder.assessmentItemView.setText(title);
        } else {
            holder.assessmentItemView.setText("MISSING INSTRUCTOR NAME");
        }
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (instructors != null) {
            return instructors.size();
        } else {
            return 0;
        }
    }
}
