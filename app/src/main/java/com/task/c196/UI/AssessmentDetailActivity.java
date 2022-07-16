package com.task.c196.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.task.c196.Adapter.CourseAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Assessment;
import com.task.c196.Entity.Course;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;
import com.task.c196.Utility.Receiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AssessmentDetailActivity extends AppCompatActivity {
    EditText id;
    EditText name;
    EditText date;
    Spinner type;
    Spinner course;
    ImageView calIcon;
    Repo repo;
    Messaging message;
    final Calendar calendar = Calendar.getInstance();;
    DatePickerDialog.OnDateSetListener listener;
    private String datePickerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessment);
        id = findViewById(R.id.assessmentID);
        name = findViewById(R.id.assessmentName);
        date = findViewById(R.id.assessmentDate);
        type = (Spinner) findViewById(R.id.assessmentType);
        course = (Spinner) findViewById(R.id.assessmentCourse);
        calIcon = findViewById(R.id.datePickerIcon);
        repo = new Repo(getApplication());
        message = new Messaging();

        SetSpinners();
        SetDatePicker();

        if (getIntent().getStringExtra("name") != null) {
            id.setText(String.valueOf(getIntent().getStringExtra("id")));
            name.setText(getIntent().getStringExtra("name"));
            date.setText(getIntent().getStringExtra("date"));
        } else {
            id.setText(String.valueOf( repo.getAllAssessments().size() + 1 ));
        }
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
                Notify();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Notify() {
        Long trigger = new Date(date.getText().toString()).getTime();
        String _name = name.getText().toString();

        Intent startIntent = new Intent(this, Receiver.class);
        startIntent.putExtra("message", String.format("%s is starting today!", _name));
        startIntent.putExtra("title", "Assessment Today!");
        PendingIntent sender = PendingIntent.getBroadcast(this, MainActivity.messageCode++, startIntent, PendingIntent.FLAG_IMMUTABLE );
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

        message.DisplayToast(this, String.format("Alert successfully created for %s", name.getText().toString()));
    }

    private boolean missingField() {
        if (
            name.getText().toString().isEmpty() ||
            date.getText().toString().isEmpty() ||
            course.getSelectedItem() == null ||
            course.getSelectedItem().toString().isEmpty()
        ) {
            return true;
        } else {
            return false;
        }
    }

    public void submitTerm(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        if (missingField()) {
            message.DisplayMessage(this, "All fields require a value.", "Illegal Action Attempted");
        } else {
            Assessment assessment = new Assessment(
                Integer.parseInt(id.getText().toString()),
                name.getText().toString(),
                new Date(date.getText().toString()),
                type.getSelectedItem().toString(),
                course.getSelectedItemPosition()+1
            );

            Course assocCourse = repo.getCourseByID(course.getSelectedItemPosition()+1);
            Date assessmentDate = new Date(date.getText().toString());

            if (assessmentDate.compareTo(assocCourse.getStart()) >= 0 && assessmentDate.compareTo(assocCourse.getEnd()) <= 0) {
                if (repo.isNewRecord("assessment", Integer.parseInt(id.getText().toString()))) {
                    if (repo.getAssociatedAssessments(course.getSelectedItemPosition() + 1) >= 5) {
                        message.DisplayMessage(
                                this,
                                "This course already has the maximum number of assessments allotted. Please remove an assessment and try again.",
                                "Illegal Action Attempted");
                    } else {
                        repo.insert(assessment);

                        message.DisplayToast(this, "Successfully added new Assessment.");

                        Intent intent = new Intent(this, AssessmentListActivity.class);
                        this.startActivity(intent);
                    }
                } else {
                    repo.update(assessment);

                    message.DisplayToast(this, "Successfully updated Assessment.");

                    Intent intent = new Intent(this, AssessmentListActivity.class);
                    this.startActivity(intent);
                }
            } else {
                message.DisplayMessage(this, String.format("The assessment date must occur during the course, %s - %s.", formatter.format(assocCourse.getStart()), formatter.format(assocCourse.getEnd()) ), "Illegal Action Attempted");
            }
        }

    }

    public void deleteTerm(View view) {
        if (getIntent().getStringExtra("name") != null) {
            Assessment assessment = new Assessment(
                    Integer.parseInt(id.getText().toString()),
                    name.getText().toString(),
                    new Date(date.getText().toString()),
                    type.getSelectedItem().toString(),
                    course.getSelectedItemPosition()+1
            );
            repo.delete(assessment);

            message.DisplayToast(this, "Successfully removed the Assessment.");

            Intent intent = new Intent(this, AssessmentListActivity.class);
            this.startActivity(intent);
        } else {
            message.DisplayMessage(this, "You're unable to delete an Assessment if it hasn't been created yet.", "Illegal Action Attempted");
        }
    }

    private void SetSpinners() {
        List<CharSequence> typeList = new ArrayList<>();
        typeList.add("Objective");
        typeList.add("Performance");

        List<Course> courses = repo.getAllCourses();
        List<CharSequence> courseName = new ArrayList<>();
        for(Course course : courses) {
            courseName.add(course.getName());
        }

        ArrayAdapter<CharSequence> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        type.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseName);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        course.setAdapter(courseAdapter);

        if (getIntent().getStringExtra("name") != null) {
            type.setSelection(typeAdapter.getPosition(getIntent().getStringExtra("type")));
            course.setSelection(Integer.parseInt(getIntent().getStringExtra("courseID"))-1);
        }
    }

    private void SetDatePicker() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        calIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                    AssessmentDetailActivity.this,
                    listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                datePickerString = formatter.format(calendar.getTime());
                date.setText(formatter.format(calendar.getTime()));
            }
        };
    }
}
