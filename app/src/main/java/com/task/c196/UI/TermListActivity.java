package com.task.c196.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.task.c196.Adapter.TermAdapter;
import com.task.c196.Database.Repo;
import com.task.c196.Entity.Term;
import com.task.c196.R;
import com.task.c196.Utility.Messaging;
import java.util.List;

public class TermListActivity extends AppCompatActivity {
    Messaging message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.termRV);
        Repo r = new Repo(getApplication());
        List<Term> termEntityList = r.getAllTerms();
        final TermAdapter adaptor = new TermAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        adaptor.setTerms(termEntityList);
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
                message.DisplayMessage(this, "Alerts may only be set from the detailed view.", "Illegal Action Attempted");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTerm(View view) {
        Intent intent = new Intent(this, TermDetailActivity.class);
        this.startActivity(intent);
    }
}
