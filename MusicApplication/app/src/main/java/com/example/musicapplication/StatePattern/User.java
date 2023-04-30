package com.example.musicapplication.StatePattern;

import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class User {
    private UserState userState;
    private FloatingActionButton btn;

    public void setState(UserState userState){
        this.userState = userState;
    }

    public void setBtn(FloatingActionButton btn){
        this.btn = btn;
    }

    public void applyState(){
        this.userState.addMusic(btn);
    }
}
