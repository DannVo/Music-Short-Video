package com.example.musicapplication.Model;

import java.io.Serializable;

public class SongItem implements Serializable {
    private String id;
    private String title;
    private String author;
    private String song_uri;
    private String artwork_uri;
    private int size;
    private int duration;
    private int status;
    private String album;
    private int album_code;
    private String note;

    public SongItem(String id, String title, String author, String song_uri, String artwork_uri, int size, int duration, int status, String album, int album_code, String note) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.song_uri = song_uri;
        this.artwork_uri = artwork_uri;
        this.size = size;
        this.duration = duration;
        this.status = status;
        this.album = album;
        this.album_code = album_code;
        this.note = note;
    }

    public SongItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSong_uri() {
        return song_uri;
    }

    public void setSong_uri(String song_uri) {
        this.song_uri = song_uri;
    }

    public String getArtwork_uri() {
        return artwork_uri;
    }

    public void setArtwork_uri(String artwork_uri) {
        this.artwork_uri = artwork_uri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbum_code() {
        return album_code;
    }

    public void setAlbum_code(int album_code) {
        this.album_code = album_code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
