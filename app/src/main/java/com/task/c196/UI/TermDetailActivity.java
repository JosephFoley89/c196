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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.task.c196.Adapter.CourseAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Term;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;
import com.task.c196.Utility.Receiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetailActivity  extends AppCompatActivity {
    EditText termID;
    EditText termName;
    EditText termStart;
    EditText termEnd;
    RecyclerView termCourseRV;
    Repo repo;
    Messaging message;
    final Calendar calendar = Calendar.getInstance();
    final Calendar calendar2 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener listener;
    DatePickerDialog.OnDateSetListener listener2;
    ImageView calIcon;
    ImageView calIcon2;

    Date start;
    Date end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);
        termID = findViewById(R.id.termID);
        termName = findViewById(R.id.termName);
        termStart = findViewById(R.id.termStart);
        termEnd = findViewById(R.id.termEnd);
        termCourseRV = findViewById(R.id.termCourseRV);
        repo = new Repo(getApplication());
        message = new Messaging();
        calIcon = findViewById(R.id.startCalendarIcon);
        calIcon2 = findViewById(R.id.endCalendarIcon);

        SetDatePicker();

        if (getIntent().getStringExtra("name") != null) {
            termID.setText(String.valueOf(getIntent().getStringExtra("id")));
            termName.setText(getIntent().getStringExtra("name"));
            termStart.setText(getIntent().getStringExtra("start"));
            termEnd.setText(getIntent().getStringExtra("end"));
        } else {
            termID.setText(repo.getRowCount("term"));
        }

        final CourseAdapter adapter = new CourseAdapter(this);
        termCourseRV.setAdapter(adapter);
        termCourseRV.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(repo.getAssociatedCourses(termID.getText().toString()));
    }

    public void deleteTerm(View view) {
        if (getIntent().getStringExtra("name") != null) {
            Term term = new Term( Integer.parseInt(termID.getText().toString()), termName.getText().toString(), new Date(termStart.getText().toString()), new Date(termEnd.getText().toString()) );
            if (repo.getAssociatedCourses(String.valueOf(getIntent().getStringExtra("id"))).size() == 0) {
                repo.delete(term);

                message.DisplayToast(this, "Successfully removed Term.");

                Intent intent = new Intent(this, TermListActivity.class);
                this.startActivity(intent);
            } else {
                message.DisplayMessage(this, "You may not delete Terms with Courses assigned to them.", "Illegal Action Attempted");
            }
        } else {
            message.DisplayMessage(this, "You're unable to delete a Term if it hasn't been created yet.", "Illegal Action Attempted");
        }
    }

    public boolean missingFields() {
        if (
            termName.getText().toString().isEmpty() ||
            termStart.getText().toString().isEmpty() ||
            termEnd.getText().toString().isEmpty()
        ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOverlappingTerm() {
        int i = 0;
        List<Term> terms = repo.getAllTerms();
        Date start = new Date(termStart.getText().toString());
        Date end = new Date(termEnd.getText().toString());

        for (Term term : terms) {
            if (start.after(term.getStart()) && start.before(term.getEnd()) || end.after(term.getStart()) && end.before(term.getEnd())) {
                return true;
            }
        }

        return false;
    }

    public void submitTerm(View view) {
        if (missingFields()) {
            message.DisplayMessage( this, "All fields require a value.", "Illegal Action Attempted" );
        } else {
            if (isOverlappingTerm()) {
                message.DisplayMessage( this, "No Terms may overlap.", "Illegal Action Attempted" );
            } else {
                SetDates();
                Term term = new Term(Integer.parseInt(termID.getText().toString()), termName.getText().toString(), new Date(termStart.getText().toString()), new Date(termEnd.getText().toString()));

                if (start.before(end)) {
                    if (repo.isNewRecord("term", term.getId())) {
                        repo.insert(term);
                        message.DisplayToast(this, "Successfully added new Term.");
                    } else {
                        repo.update(term);
                        message.DisplayToast(this, "Successfully updated Term.");
                    }

                    Intent intent = new Intent(this, TermListActivity.class);
                    this.startActivity(intent);
                } else {
                    message.DisplayMessage(this, "Unable to start a term after it's finished", "Illegal Action Attempted");
                }
            }
        }
    }

    private void SetDates() {
        start = new Date(termStart.getText().toString());
        end = new Date(termEnd.getText().toString());
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
                this.finish();
                return true;
             */
            case R.id.AlertMe:
                Notify();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Notify() {
        Long triggerStart = new Date(termStart.getText().toString()).getTime();
        Long triggerEnd = new Date(termEnd.getText().toString()).getTime();
        String name = termName.getText().toString();

        Intent startIntent = new Intent(this, Receiver.class);
        startIntent.putExtra("message", String.format("%s is starting today!", name));
        startIntent.putExtra("title", "Term Starting!");
        PendingIntent sender = PendingIntent.getBroadcast(this, MainActivity.messageCode++, startIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, triggerStart, sender);

        Intent endIntent = new Intent(this, Receiver.class);
        endIntent.putExtra("message", String.format("%s is ending today!", name));
        endIntent.putExtra("title", "Term Ending!");
        PendingIntent sender2 = PendingIntent.getBroadcast(this, MainActivity.messageCode++, endIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager manager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager2.set(AlarmManager.RTC_WAKEUP, triggerEnd, sender2);

        message.DisplayToast(this, String.format("Alert successfully created for the start and end of %s", termName.getText().toString()));
    }

    private void SetDatePicker() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        calIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        TermDetailActivity.this,
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

                termStart.setText(formatter.format(calendar.getTime()));
            }
        };

        calIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        TermDetailActivity.this,
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

                termEnd.setText(formatter.format(calendar2.getTime()));
            }
        };
    }
}
