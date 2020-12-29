package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.InputStream;

public class Folder {
    public static ContentResolver musicResolver = Song.musicResolver;
    public static Resources resources = Song.resources;
    private long albumId;
    private String folderName;


    public Folder(long albumId, String folderName) {
        this.albumId = albumId;
        this.folderName = folderName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getFolderName() {
        return folderName;
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

}
