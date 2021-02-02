package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.InputStream;

public class Artist {
    private long albumId;
    private String artistName;
    public static ContentResolver musicResolver = Song.musicResolver;
    public static Resources resources = Song.resources;
    int size = 0;


    public Artist(long albumId, String artistName) {
        this.albumId = albumId;
        this.artistName = artistName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getSize() {
        if (size == 0)
        {
            size = ListMaker.getThisArtistsSongs(artistName).size();
        }
        return size;
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
