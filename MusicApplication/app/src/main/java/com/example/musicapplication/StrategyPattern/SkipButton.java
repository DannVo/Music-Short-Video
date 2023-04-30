//SongDetail.java

package com.example.musicapplication.StrategyPattern;

import com.google.android.exoplayer2.ExoPlayer;

public abstract class SkipButton {
    public static ExoPlayer player;
    public SkipButton (){}

    public abstract void skipSong(ExoPlayer player);
}
