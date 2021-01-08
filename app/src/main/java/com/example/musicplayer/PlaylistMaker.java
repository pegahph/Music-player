package com.example.musicplayer;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistMaker {
    public static ArrayList<String> keys;
    public static HashMap<String, ArrayList<Song>> playlists;
    public static ArrayList<Song> favorite;
    public static ArrayList<Song> recentlyPlayed;
    private static DatabaseHelper mDatabaseHelper;

    public PlaylistMaker() {
    }

    public static void createDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }
    public static HashMap<String, ArrayList<Song>> getPlaylists() {
        if (playlists == null || playlists.size() == 0) {
            playlists = new HashMap<>();
            loadPlaylists();
        }
        return playlists;
    }

    public static void savePlaylists() {
        for (String key : keys) {
            mDatabaseHelper.addPlaylistTitle(key);
        }
    }
    public static void loadPlaylists() {
        playlists = new HashMap<>();
        keys = new ArrayList<>();
        for (String key : mDatabaseHelper.allPlaylistTitles()) {
            if (!keys.contains(key))
            {
                keys.add(key);
                playlists.put(key, new ArrayList<Song>());
            }
        }
        // playlists object is the one that we work with.
    }
    public static void savePlaylistsToDatabase() {
        // save to database
    }
    public static boolean newPlaylist(String playlistName, ArrayList<Song> songs) {
        // if playlist added successfully, return true.
        for (String key : keys) {
            if (key.equals(playlistName)) {
                return false;
            }
        }
        playlists.put(playlistName, songs);
        keys.add(playlistName);
        return true;
        // playlist name should be monhaser be fard!!
    }
    public static void deletePlaylist(String name) {
        for (String key : keys) {
            if (key.equals(name)) {
                playlists.remove(name);
                break;
            }
        }
    }
    public static ArrayList<Song> loadThisPlaylist(String key) {
        return playlists.get(key);
    }
    public static void newFavoriteSong(Song song) {
        if (favorite == null) {
            favorite = new ArrayList<>();
            playlists.put("Favorite tracks", favorite);
            keys.add("Favorite tracks");
        }
        favorite.add(song);
    }
    public static void removeFromFavorites(Song song) {
        favorite.remove(song);
    }
    public static void newRecentlyPlayedSong(Song song) {
        // we saved only 100 song that played recently.
        if (recentlyPlayed == null) {
            recentlyPlayed = new ArrayList<>();
            playlists.put("Recently played", recentlyPlayed);
            keys.add("Recently played");
        }
        recentlyPlayed.remove(song);

        // TODO: 10 should be 100 and 9 should be 99.
        if (recentlyPlayed.size() >= 10) {
            recentlyPlayed.remove(9);
        }
        recentlyPlayed.add(0, song);
    }

}
