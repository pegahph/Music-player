package com.example.musicplayer;

import android.graphics.drawable.Drawable;

public class Song {
    private long id;
    private String title;
    private String artist;
    private Drawable albumArt;
    private long albumId;


    public Song(long songId, String songTitle, String songArtist, Drawable albumArt) {
        this.id = songId;
        this.title = songTitle;
        this.artist = songArtist;
        this.albumArt = albumArt;
    }

    public Song(long songId, String songTitle, String songArtist, long albumId) {
        this.id = songId;
        this.title = songTitle;
        this.artist = songArtist;
        this.albumId = albumId;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public Drawable getAlbumArt() {
        return albumArt;
    }
    public long getAlbumId() {
        return albumId;
    }
    public void setAlbumArt(Drawable albumArt) {
        this.albumArt = albumArt;
    }
}
