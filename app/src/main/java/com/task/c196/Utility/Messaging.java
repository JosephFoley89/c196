package com.task.c196.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Messaging {
    public void DisplayMessage(Context context, String message, String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.show();
    }

    public void DisplayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
