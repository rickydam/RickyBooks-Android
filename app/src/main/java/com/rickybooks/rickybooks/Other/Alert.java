package com.rickybooks.rickybooks.Other;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.rickybooks.rickybooks.MainActivity;

public class Alert {
    private MainActivity activity;

    public Alert(MainActivity activity) {
        this.activity = activity;
    }

    public void create(final String title, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }
}
