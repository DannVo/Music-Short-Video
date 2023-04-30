/*
    SignUpActivity.java
    HomeFragment.java
 */


package com.example.musicapplication.BuilderPattern;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Notification {
    private TextView title, content;
    private Button btnSuccess, btnDanger;

    public Notification(TextView title, TextView content, Button btnSuccess, Button btnDanger) {
        this.title = title;
        this.content = content;
        this.btnSuccess = btnSuccess;
        this.btnDanger = btnDanger;
    }
}

