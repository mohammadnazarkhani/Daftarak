package com.example.daftarak.utility;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class UiExtensions {

    // Set status bar + action bar colors
    public static void setTopToolBarColor(Window window, AppCompatActivity activity, int color) {
        window.setStatusBarColor(color);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    // Set navigation bar color
    public static void setNavigationBarColor(Window window, int color) {
        window.setNavigationBarColor(color);
    }
}
