package com.task.c196.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;
import com.task.c196.Adapter.AssessmentAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Course;
import com.task.c196.Entity.Instructor;
import com.task.c196.Entity.Term;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;
import com.task.c196.Utility.Receiver;
import com.task.c196.Utility.RemoveDuplicates;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CourseDetailActivity extends AppCompatActivity {
    EditText courseID;
    EditText courseName;
    EditText courseStart;
    EditText courseEnd;
    EditText notes;
    EditText instructorName;
    EditText instructorEmail;
    EditText instructorPhone;
    Spinner courseStatus;
    Spinner courseTerm;
    Spinner courseInstructor;
    RecyclerView courseAssessmentRV;
    Repo repo;
    Messaging message;
    final Calendar calendar = Calendar.getInstance();
    final Calendar calendar2 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener listener;
    DatePickerDialog.OnDateSetListener listener2;
    ImageView calIcon;
    ImageView calIcon2;
    RemoveDuplicates r;

    Date start;
    Date end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course);
        repo = new Repo(getApplication());
        message = new Messaging();
        courseID = findViewById(R.id.courseID);
        courseName = findViewById(R.id.courseName);
        courseStart = findViewById(R.id.courseStart);
        courseEnd = findViewById(R.id.courseEnd);
        notes = findViewById(R.id.notes);
        courseAssessmentRV = findViewById(R.id.courseAssessmentRV);
        calIcon = findViewById(R.id.datePickerIcon);
        calIcon2 = findViewById(R.id.datePickerIcon2);
        instructorName = findViewById(R.id.instructorName);
        instructorEmail = findViewById(R.id.instructorEmail);
        instructorPhone = findViewById(R.id.instructorPhone);

        r = new RemoveDuplicates();

        if (getIntent().getStringExtra("name") != null) {
            courseID.setText(getIntent().getStringExtra("id"));
            courseName.setText(getIntent().getStringExtra("name"));
            courseStart.setText(getIntent().getStringExtra("start"));
            courseEnd.setText(getIntent().getStringExtra("end"));
            notes.setText(getIntent().getStringExtra("note"));
            instructorPhone.setText(getIntent().getStringExtra("iPhone"));
            instructorEmail.setText(getIntent().getStringExtra("iEmail"));
            instructorName.setText(getIntent().getStringExtra("iName"));
        } else {
            courseID.setText(String.valueOf(repo.getAllCourses().size()+1));
        }

        SetSpinners();
        SetDatePicker();

        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        courseAssessmentRV.setAdapter(adapter);
        courseAssessmentRV.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(repo.getAssociatedAssessments(courseID.getText().toString()));
    }

    public void deleteCourse(View view) {
        if (getIntent().getStringExtra("name") != null) {
            Course course = new Course(
                    Integer.parseInt(courseID.getText().toString()),
                    courseName.getText().toString(),
                    new Date(courseStart.getText().toString()),
                    new Date(courseEnd.getText().toString()),
                    courseStatus.toString(), notes.getText().toString(),
                    courseTerm.getSelectedItemPosition()+1,
                    instructorName.getText().toString(),
                    instructorPhone.getText().toString(),
                    instructorEmail.getText().toString()
            );
            repo.delete(course);
            message.DisplayToast(this, "Successfully removed Course.");

            Intent intent = new Intent(this, CourseListActivity.class);
            this.startActivity(intent);
        } else {
            message.DisplayMessage(this, "You're unable to delete a Course if it hasn't been created yet.", "Illegal Action Attempted");
        }
    }

    private boolean missingField() {
        if (
            courseName.getText().toString().isEmpty() ||
            courseStart.getText().toString().isEmpty() ||
            courseEnd.getText().toString().isEmpty() ||
            courseTerm.getSelectedItem() == null ||
            courseTerm.getSelectedItem().toString().isEmpty() ||
            instructorName.getText().toString().isEmpty() ||
            instructorEmail.getText().toString().isEmpty() ||
            instructorPhone.getText().toString().isEmpty()
        ) {
            return true;
        } else {
            return false;
        }
    }


    public void submitCourse(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        if (missingField()) {
            message.DisplayMessage(this, "All fields other than the Notes field require a value.", "Illegal Action Attempted");
        } else {
            SetDate();
            Course course = new Course(
                    Integer.parseInt(courseID.getText().toString()),
                    courseName.getText().toString(),
                    new Date(courseStart.getText().toString()),
                    new Date(courseEnd.getText().toString()),
                    courseStatus.toString(),
                    notes.getText().toString(),
                    courseTerm.getSelectedItemPosition() + 1,
                    instructorName.getText().toString(),
                    instructorPhone.getText().toString(),
                    instructorEmail.getText().toString()

            );

            Term term = repo.getTermByID(courseTerm.getSelectedItemPosition() + 1);

            if (start.before(end)) {
                if (start.compareTo(term.getStart()) >= 0 && end.compareTo(term.getEnd()) <= 0) {
                    if (repo.isNewRecord("course", Integer.parseInt(courseID.getText().toString()))) {
                        repo.insert(course);
                        message.DisplayToast(this, "Successfully added Course.");
                    } else {
                        repo.update(course);
                        message.DisplayToast(this, "Successfully updated Course.");
                    }

                    Intent intent = new Intent(this, CourseListActivity.class);
                    this.startActivity(intent);
                } else {
                    message.DisplayMessage(this, String.format("The course must start and end during the selected Term, %s - %s.", formatter.format(term.getStart()), formatter.format(term.getEnd())), "Illegal Action Attempted.");
                }
            } else {
                message.DisplayMessage(this, String.format("The course must start and end during the selected Term, %s - %s.", formatter.format(term.getStart()), formatter.format(term.getEnd())), "Illegal Action Attempted.");
            }
        }
    }

    private void SetSpinners() {
        courseStatus = (Spinner)findViewById(R.id.courseStatus);
        courseTerm = (Spinner)findViewById(R.id.courseTerm);

        List<Term> terms = repo.getAllTerms();

        List<CharSequence> statuses = new ArrayList<>();
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Dropped");
        statuses.add("Plan to Take");

        List<CharSequence> termNames = new ArrayList<>();
        for (Term t : terms) {
            termNames.add(t.getName());
        }

        List<Instructor> instructors;

        if (getIntent().getStringExtra("name") != null) {
            instructors = r.removeDupes(repo.getAssociatedInstructors(courseID.getText().toString()));
        } else {
            instructors = r.removeDupes(repo.getAllInstructors());
        }


        List<CharSequence> instructorNames = new ArrayList<>();
        for (Instructor i : instructors) {
            instructorNames.add(i.getName() + " - " + i.getPhone() + " - " + i.getEmail());
        }

        ArrayAdapter<CharSequence> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseStatus.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termNames);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseTerm.setAdapter(termAdapter);

        if (getIntent().getStringExtra("name") != null) {
            courseTerm.setSelection(Integer.parseInt(getIntent().getStringExtra("termId"))-1);
            courseStatus.setSelection(statusAdapter.getPosition(getIntent().getStringExtra("status")));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void SetDate() {
        start = new Date(courseStart.getText().toString());
        end = new Date(courseEnd.getText().toString());
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
                if (courseStart.getText().toString().isEmpty() || courseEnd.getText().toString().isEmpty()) {
                    message.DisplayMessage(this, "Missing a date value.", "Illegal Action Attempted");
                } else {
                    Notify();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Notify() {
        Long triggerStart = new Date(courseStart.getText().toString()).getTime();
        Long triggerEnd = new Date(courseEnd.getText().toString()).getTime();
        String name = courseName.getText().toString();

        Intent startIntent = new Intent(this, Receiver.class);
        startIntent.putExtra("message", String.format("%s is starting today!", name));
        startIntent.putExtra("title", "Course Starting!");
        PendingIntent sender = PendingIntent.getBroadcast(this, MainActivity.messageCode++, startIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, triggerStart, sender);

        Intent endIntent = new Intent(this, Receiver.class);
        endIntent.putExtra("message", String.format("%s is ending today!", name));
        endIntent.putExtra("title", "Course Ending!");
        PendingIntent sender2 = PendingIntent.getBroadcast(this, MainActivity.messageCode++, endIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager manager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager2.set(AlarmManager.RTC_WAKEUP, triggerEnd, sender2);

        message.DisplayToast(this, String.format("Alert successfully created for the start and end of %s", courseName.getText().toString()));
    }

    private void SetDatePicker() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        calIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        CourseDetailActivity.this,
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

                courseStart.setText(formatter.format(calendar.getTime()));
            }
        };

        calIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        CourseDetailActivity.this,
                        listener2,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        listener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, month);
                calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                courseEnd.setText(formatter.format(calendar2.getTime()));
            }
        };
    }

    public void shareNotes(View view) {
        if (notes.getText().length() > 0) {
            Intent shareNoteIntent = new Intent();
            shareNoteIntent.setAction(Intent.ACTION_SEND);
            shareNoteIntent.putExtra(Intent.EXTRA_TEXT, notes.getText().toString());
            shareNoteIntent.putExtra(Intent.EXTRA_TITLE, "Notes from " + courseName.getText().toString());
            shareNoteIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(shareNoteIntent, null);
            startActivity(shareIntent);
        } else {
            message.DisplayMessage(this, "Unable to share notes as the notes are currently empty.", "Illegal Action Attempted");
        }
    }
}