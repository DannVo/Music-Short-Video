package com.example.musicapplication.StrategyPattern;

import com.google.android.exoplayer2.ExoPlayer;

public class SkipPreviousSongButton extends SkipButton{
    public SkipPreviousSongButton(){}
    @Override
    public void skipSong(ExoPlayer player) {
        if(player.hasPreviousMediaItem()){
            player.seekToPrevious();
        }
    }
}
