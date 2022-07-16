package com.task.c196.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.c196.Adapter.CourseAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Course;
import com.task.c196.Entity.Instructor;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;
import com.task.c196.Utility.RemoveDuplicates;

import java.util.ArrayList;
import java.util.List;

public class InstructorDetailActivity  extends AppCompatActivity {
    RemoveDuplicates dupes;
    EditText courseID;
    EditText instructorID;
    EditText email;
    EditText name;
    EditText phone;
    RecyclerView instructorCourseRV;
    Repo repo;
    Messaging message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dupes = new RemoveDuplicates();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_instructor);
        instructorID = findViewById(R.id.instructorID);
        name = findViewById(R.id.instructorName);
        phone = findViewById(R.id.instructorPhone);
        email = findViewById(R.id.instructorEmail);
        instructorCourseRV = findViewById(R.id.instructorCourseRV);
        repo = new Repo(getApplication());
        message = new Messaging();

        if (getIntent().getStringExtra("name") != null) {
            instructorID.setText(String.valueOf(getIntent().getStringExtra("id")));
            name.setText(getIntent().getStringExtra("name"));
            phone.setText(getIntent().getStringExtra("phone"));
            email.setText(getIntent().getStringExtra("email"));
        } else {
            instructorID.setText(repo.getRowCount("term"));
        }

        List<Instructor> instructors = repo.GetInstructorByName(name.getText().toString());
        List<Course> courses = new ArrayList<>();

        for (Instructor instructor : instructors) {
            courses.add(repo.getCourseByID(instructor.getCourseId()));
        }

        final CourseAdapter adapter = new CourseAdapter(this);
        instructorCourseRV.setAdapter(adapter);
        instructorCourseRV.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(courses);
    }

    public void deleteInstructor(View view) {
        Instructor instructor = new Instructor(
            Integer.parseInt(instructorID.getText().toString()),
            name.getText().toString(),
            phone.getText().toString(),
            email.getText().toString(),
            Integer.parseInt(courseID.getText().toString())
        );

        repo.delete(instructor);

        message.DisplayToast(this, "Successfully deleted Instructor.");
        Intent intent = new Intent(this, InstructorListActivity.class);
        this.startActivity(intent);
    }

    public boolean missingFields() {
        return false;
    }

    public void submitInstructor(View view) {

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
                this.finish();
                return true;
            case R.id.Courses:
                intent = new Intent(this, CourseListActivity.class);
                this.startActivity(intent);
                this.finish();
                return true;
            case R.id.Assessments:
                intent = new Intent(this, AssessmentListActivity.class);
                this.startActivity(intent);
                this.finish();
                return true;
            /*case R.id.Instructors:
                intent = new Intent(this, InstructorListActivity.class);
                this.startActivity(intent);
                return true;*/
            case R.id.AlertMe:
                message.DisplayMessage(this, "Nothing to notify.", "Illegal Action Attempted");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}