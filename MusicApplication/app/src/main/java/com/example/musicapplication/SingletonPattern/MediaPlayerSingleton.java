//SongAdapter.java

package com.example.musicapplication.SingletonPattern;

import android.media.MediaPlayer;

public class MediaPlayerSingleton {
    private static MediaPlayerSingleton instance;
    public static MediaPlayer mp;
    private MediaPlayerSingleton(){
        mp = new MediaPlayer();
    }

    public static MediaPlayerSingleton getInstance(){
        if(instance == null){
            instance = new MediaPlayerSingleton();
        }
        return instance;
    }
}
