package com.example.musicplayer;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistMaker {
    public static ArrayList<String> keys;
    public static HashMap<String, ArrayList<Song>> playlists;
    public static ArrayList<Song> favorite;
    public static ArrayList<Song> recentlyPlayed;
    public static ArrayList<Song> lastList;
    private static Song lastSong;
    private static DatabaseHelper mDatabaseHelper;

    public PlaylistMaker() {
    }

    public static void createDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }
    public static HashMap<String, ArrayList<Song>> getPlaylists() {
        if (playlists == null) {
            playlists = new HashMap<>();
            loadPlaylists();
        }
        return playlists;
    }

    public static void savePlaylists() {
        Gson gson = new Gson();
        for (String key : keys) {
            String json = gson.toJson(playlists.get(key));
            mDatabaseHelper.addPlaylist(key, json);
        }
        String json = gson.toJson(lastList);
        mDatabaseHelper.addPlaylist("lastList", json);
    }
    public static void loadPlaylists() {
        playlists = new HashMap<>();
        keys = new ArrayList<>();
        Type type = new TypeToken<ArrayList<Song>>() {
        }.getType();
        Gson gson = new Gson();
        for (String key : mDatabaseHelper.getAllPlaylistTitles()) {
            if (key.equals("lastList"))
            {
                String playlistSongsJson = mDatabaseHelper.getThisPlaylistSongs(key);
                ArrayList<Song> playlistSongs = gson.fromJson(playlistSongsJson, type);
                if (playlistSongs != null){
                    lastList = changeToRealSongs(key, playlistSongs);
                } else {
                    lastList = ListMaker.loadTracks();
                }
            }
            else if (!keys.contains(key))
            {
                keys.add(key);
                String playlistSongsJson = mDatabaseHelper.getThisPlaylistSongs(key);
                ArrayList<Song> playlistSongs = gson.fromJson(playlistSongsJson, type);
                playlistSongs = changeToRealSongs(key, playlistSongs);
                playlists.put(key, playlistSongs);
            }
        }
        if (keys.contains("Favorite tracks"))
        {
            favorite = playlists.get("Favorite tracks");
        }
        if (keys.contains("Recently played"))
        {
            recentlyPlayed = playlists.get("Recently played");
            assert recentlyPlayed != null;
            lastSong = recentlyPlayed.get(0);

        }
        // playlists object is the one that we work with.
    }

    private static ArrayList<Song> changeToRealSongs(String key, ArrayList<Song> playlistSongs) {
        ArrayList<Song> tracks = ListMaker.loadTracks();
        ArrayList<Song> result = new ArrayList<>();
        for (Song song : playlistSongs) {
            int index = tracks.indexOf(song);
            if (index != -1 && song != null)
            {
                Song song1 = tracks.get(index);
                result.add(song1);
                if (key.equals("Favorite tracks"))
                {
                    song1.changeFavorite();
                }
            }
        }
        return result;
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
            if (keys.contains("Favorite tracks"))
            {
                favorite = playlists.get("Favorite tracks");
            }
            else {
                favorite = new ArrayList<>();
                playlists.put("Favorite tracks", favorite);
                keys.add("Favorite tracks");
            }
        }
        assert favorite != null;
        if (!favorite.contains(song))
            favorite.add(0, song);
    }
    public static void removeFromFavorites(Song song) {
        favorite.remove(song);
    }
    public static void newRecentlyPlayedSong(Song song) {
        // we saved only 100 song that played recently.
        if (recentlyPlayed == null) {
            if (keys.contains("Recently played"))
            {
                recentlyPlayed = playlists.get("Recently played");
            }
            else {
                recentlyPlayed = new ArrayList<>();
                playlists.put("Recently played", recentlyPlayed);
                keys.add("Recently played");
            }
        }
        assert recentlyPlayed != null;
        recentlyPlayed.remove(song);

        if (recentlyPlayed.size() >= 100) {
            recentlyPlayed.remove(99);
        }
        recentlyPlayed.add(0, song);
        lastSong = song;
    }

    public static Song getLastSong() {
        return lastSong;
    }
}
