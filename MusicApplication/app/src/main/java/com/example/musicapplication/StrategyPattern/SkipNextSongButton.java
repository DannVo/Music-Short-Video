package com.example.musicapplication.StrategyPattern;

import com.google.android.exoplayer2.ExoPlayer;

public class SkipNextSongButton extends SkipButton{
    public SkipNextSongButton(){}

    @Override
    public void skipSong(ExoPlayer player) {
        if(player.hasNextMediaItem()){
            player.seekToNext();
        }
    }
}
