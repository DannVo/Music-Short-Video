package com.example.musicapplication.StatePattern;

import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NormalUser implements UserState{
    @Override
    public void addMusic(FloatingActionButton btn) {
        btn.setEnabled(false);
    }
}
