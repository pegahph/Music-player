package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ListMaker {
    static ArrayList<Song> songList = new ArrayList<>();
    static ArrayList<Artist> artistList = new ArrayList<>();
    static ContentResolver musicResolver;
    static Resources resources;

    public static ArrayList<Song> loadTracks() {
        if (songList.size() == 0)
            getSongs();
        return songList;
    }
    private static void getSongs() {
        ArrayList<String> artists = new ArrayList<>();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String selection = MediaStore.Audio.Media.IS_MUSIC +
//                "=1 ) GROUP BY (" + MediaStore.Audio.Media.ALBUM_ID;
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
            Song.musicResolver = musicResolver;
            Song.resources = resources;
            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long album_id = musicCursor.getLong(albumIdColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist, album_id));

                if (!artists.contains(thisArtist)) {
                    artists.add(thisArtist);
                    artistList.add(new Artist(album_id, thisArtist));
                }
            } while (musicCursor.moveToNext());
        }
    }

    public static ArrayList<Artist> loadArtists() {
        if (artistList.size() == 0)
            getSongs();
        return artistList;
    }

    public static ArrayList<Song> getThisArtistsSongs(String thisArtistName){
        ArrayList<Song> thisArtistsSongs = new ArrayList<>();
        for(int i=0; i<songList.size(); i++){
            if(songList.get(i).getArtist().equals(thisArtistName)){
                thisArtistsSongs.add(songList.get(i));
            }
        }
        return thisArtistsSongs;
    }

}
