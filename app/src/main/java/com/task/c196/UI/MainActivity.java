package com.task.c196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.task.c196.R;

public class MainActivity extends AppCompatActivity {
    public static int messageCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void EnterHere(View view) {
        Intent intent = new Intent(MainActivity.this, TermListActivity.class);
        startActivity(intent);
    }
}