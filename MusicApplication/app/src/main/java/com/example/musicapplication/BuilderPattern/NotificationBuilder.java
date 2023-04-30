package com.example.musicapplication.BuilderPattern;

import android.widget.Button;
import android.widget.TextView;

public interface NotificationBuilder {

    NotificationBuilder setTitle(TextView textView);

    NotificationBuilder setContent(TextView textView);

    NotificationBuilder setbtnSuccess(Button btn);

    NotificationBuilder setbtnDanger(Button btn);

    TextView getTitle();

    TextView getContent();

    Button getBtnSuccess();

    Button getBtnDanger();

    Notification create();
}
