package com.example.musicapplication.BuilderPattern;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationConcreteBuilder implements NotificationBuilder{
    private TextView title, content;
    private Button btnSuccess, btnDanger;

    public NotificationBuilder setTitle(TextView textView){
        this.title = textView;
        title.setVisibility(View.VISIBLE);
        return this;
    }

    public NotificationBuilder setContent(TextView textView){
        this.content = textView;
        content.setVisibility(View.VISIBLE);
        return this;
    }

    public NotificationBuilder setbtnSuccess(Button btn){
        this.btnSuccess = btn;
        btnSuccess.setVisibility(View.VISIBLE);

        return this;
    }

    public NotificationBuilder setbtnDanger(Button btn){
        this.btnDanger = btn;
        btnDanger.setVisibility(View.VISIBLE);

        return this;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getContent() {
        return content;
    }

    public Button getBtnSuccess() {
        return btnSuccess;
    }

    public Button getBtnDanger() {
        return btnDanger;
    }

    public Notification create(){
        return new Notification(title, content, btnSuccess, btnDanger);
    }
}
