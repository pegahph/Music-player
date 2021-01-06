package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;

import java.io.InputStream;

public class Song {
    public static ContentResolver musicResolver;
    public static Resources resources;
    public static String INTERNAL_STORAGE_ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    public static String EXTERNAL_STORAGE_ROOT_PATH = Environment.getStorageDirectory().getPath();
    private long id;
    private String title;
    private String artist;
    private String path;
    private String folder;
    private long albumId;
    private boolean favorite;

    public Song(long songId, String songTitle, String songArtist, long albumId, String songPath) {
        this.id = songId;
        this.title = songTitle;
        this.artist = songArtist;
        this.albumId = albumId;
        this.path = songPath;
        this.favorite = false;
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
    public boolean isFavorite() {
        return favorite;
    }
    public void changeFavorite() {
        this.favorite = !favorite;
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
            albumArt = Bitmap.createScaledBitmap(albumArt, width, height, false);
            return new BitmapDrawable(resources, albumArt);
        }
        else {
            return null;
        }
    }
    // Oh my God!!!
    // I did it.
    public String getFolder() {
        if (folder != null)
        {
            return folder;
        }
        boolean internalStorage = path.startsWith(INTERNAL_STORAGE_ROOT_PATH);
        if (internalStorage)
        {
            int index = path.indexOf(INTERNAL_STORAGE_ROOT_PATH) + INTERNAL_STORAGE_ROOT_PATH.length();
            String s = path.substring(index);

            // contains only one slash. means that the file is just in internal storage.
            if (s.split("/").length == 2)  // is two because first one is ""
            {
                folder = "internal storage";     // second on is INTERNAL_STORAGE_ROOT_PATH
            }
            else
            {
                String[] tree = s.split("/");
                folder = tree[tree.length - 2]; // because last one is the song title itself.
            }
        } else {
            int index = path.indexOf(EXTERNAL_STORAGE_ROOT_PATH) + EXTERNAL_STORAGE_ROOT_PATH.length();
            String s = path.substring(index);
            String[] tree = s.split("/");
            folder = tree[tree.length - 2];
        }
        return folder;
    }
}
