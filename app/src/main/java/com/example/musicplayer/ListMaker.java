package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ListMaker {
    private static ArrayList<Song> songList = new ArrayList<>();
    private static ArrayList<Artist> artistList = new ArrayList<>();
    private static ArrayList<Folder> folderList = new ArrayList<>();
    static ContentResolver musicResolver;
    static Resources resources;

    public static ArrayList<Song> loadTracks() {
        if (songList.size() == 0)
            getSongs();
        return songList;
    }
    private static void getSongs() {
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> folders = new ArrayList<>();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String selection = MediaStore.Audio.Media.IS_MUSIC +
//                "=1 ) GROUP BY (" + MediaStore.Audio.Media.ALBUM_ID;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.MediaColumns.DATA,
//                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
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
            int pathColumn = musicCursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            Song.musicResolver = musicResolver;
            Song.resources = resources;
            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long album_id = musicCursor.getLong(albumIdColumn);
                String path = musicCursor.getString(pathColumn);
                int duration = musicCursor.getInt(durationColumn);
                Song thisSong = new Song(thisId, thisTitle, thisArtist, album_id, path, duration);
                songList.add(thisSong);
                String thisFolder = thisSong.getFolder();

                if (!artists.contains(thisArtist)) {
                    artists.add(thisArtist);
                    artistList.add(new Artist(album_id, thisArtist));
                }
                if (!folders.contains(thisFolder)) {
                    folders.add(thisFolder);
                    folderList.add(new Folder(album_id, thisFolder));
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

    public static ArrayList<Folder> loadFolders() {
        if (folderList.size() == 0)
            getSongs();
        return folderList;
    }

    public static ArrayList<Song> getThisFolderSongs(String thisFolderName){
        ArrayList<Song> thisFolderSongs = new ArrayList<>();
        for(int i=0; i<songList.size(); i++){
            if(songList.get(i).getFolder().equals(thisFolderName)){
                thisFolderSongs.add(songList.get(i));
            }
        }
        return thisFolderSongs;
    }

}
