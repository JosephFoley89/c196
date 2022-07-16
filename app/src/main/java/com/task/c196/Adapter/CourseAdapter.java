package com.task.c196.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.task.c196.Entity.Course;
import com.task.c196.R;
import com.task.c196.UI.CourseDetailActivity;
import java.text.SimpleDateFormat;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.listItemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = courses.get(position);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String start = formatter.format(current.getStart());
                    String end = formatter.format(current.getEnd());

                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.putExtra("id", String.valueOf(current.getId()));
                    intent.putExtra("name", current.getName());
                    intent.putExtra("status", current.getStatus());
                    intent.putExtra("start", start);
                    intent.putExtra("end", end);
                    intent.putExtra("note", current.getNotes());
                    intent.putExtra("termId", String.valueOf(current.getTermId()));
                    intent.putExtra("iName", current.getInstructorName());
                    intent.putExtra("iPhone", current.getInstructorPhone());
                    intent.putExtra("iEmail", current.getInstructorEmail());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> courses;
    private final Context context;
    private final LayoutInflater inflater;

    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (courses != null ) {
            Course current = courses.get(position);
            String title = current.getName();
            holder.courseItemView.setText(title);
        } else {
            holder.courseItemView.setText("MISSING");
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null) {
            return courses.size();
        } else {
            return 0;
        }
    }
}
