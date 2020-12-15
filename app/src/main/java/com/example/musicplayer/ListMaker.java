package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListMaker {
    static ArrayList<Song> songList = new ArrayList<>();

    public static ArrayList<Song> loadTracks(ContentResolver musicResolver, Resources resources) {
        if (songList.size() == 0)
            getSongs(musicResolver, resources);
        return songList;
    }
    private static void getSongs(ContentResolver musicResolver, Resources resources) {
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor musicCursor = musicResolver.query(musicUri, projection, selection, null, sortOrder);
        // I guess we got musics now.

        if (musicCursor!=null && musicCursor.moveToFirst()) {
            // get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                Long album_id = musicCursor.getLong(albumIdColumn);
                Bitmap albumArt = null;

                // try to get album art
                Uri uri = Uri.parse("content://media/external/audio/albumart");
                Uri coverUri = ContentUris.withAppendedId(uri, album_id);
                try {
                    InputStream inputStream = musicResolver.openInputStream(coverUri);
                    albumArt = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ignored) {
                }

                Song thisSong = new Song(thisId, thisTitle, thisArtist, null);
                if (albumArt != null) {
                    int width = albumArt.getWidth();
                    int height = albumArt.getHeight();
                    albumArt = Bitmap.createScaledBitmap(albumArt, width / 5, height / 5, false);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, albumArt);
                    thisSong.setAlbumArt(bitmapDrawable);
                }
                songList.add(thisSong);
            } while (musicCursor.moveToNext());
        }
    }
}
