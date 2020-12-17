package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.InputStream;

public class Song {
    public static  ContentResolver musicResolver;
    public static  Resources resources;
    private long id;
    private String title;
    private String artist;
    private long albumId;

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
    public long getAlbumId() {
        return albumId;
    }
    public BitmapDrawable getAlbumArtBitmapDrawable() {
        Bitmap albumArt = null;

        Uri uri = Uri.parse("content://media/external/audio/albumart");
        Uri coverUri = ContentUris.withAppendedId(uri, albumId);
        try {
            InputStream inputStream = musicResolver.openInputStream(coverUri);
            albumArt = BitmapFactory.decodeStream(inputStream);
        } catch (Exception ignored) {
        }
        if (albumArt != null) {
            int width = albumArt.getWidth();
            int height = albumArt.getHeight();
            albumArt = Bitmap.createScaledBitmap(albumArt, width / 5, height / 5, false);
            return new BitmapDrawable(resources, albumArt);
        }
        else {
            return null;
        }
    }
    // Oh my God!!!
    // I did it.
}
