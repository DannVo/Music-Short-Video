package com.example.musicapplication.StatePattern;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PremiumUser implements UserState{
    @Override
    public void addMusic(FloatingActionButton btn) {
        btn.setEnabled(true);
    }
}
