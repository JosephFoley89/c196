package com.task.c196.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.c196.Adapter.AssessmentAdapter;
import com.task.c196.Adapter.CourseAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Assessment;
import com.task.c196.Entity.Course;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;

import java.util.List;

public class AssessmentListActivity extends AppCompatActivity {
    Messaging message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.assessmentRV);
        Repo r = new Repo(getApplication());
        List<Assessment> assessments = r.getAllAssessments();
        final AssessmentAdapter adaptor = new AssessmentAdapter(AssessmentListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(AssessmentListActivity.this));
        recyclerView.setAdapter(adaptor);
        adaptor.setAssessments(assessments);
        message = new Messaging();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.Terms:
                intent = new Intent(this, TermListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.Courses:
                intent = new Intent(this, CourseListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.Assessments:
                intent = new Intent(this, AssessmentListActivity.class);
                this.startActivity(intent);
                return true;
            /*case R.id.Instructors:
                intent = new Intent(this, InstructorListActivity.class);
                this.startActivity(intent);
                return true;*/
            case R.id.AlertMe:
                message.DisplayMessage(this, "Alerts may only be set from the detailed view.", "Illegal Action Attempted");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAssessment(View view) {
        Intent intent = new Intent(this, AssessmentDetailActivity.class);
        this.startActivity(intent);
    }
}
