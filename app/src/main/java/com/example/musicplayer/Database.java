package com.example.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    public static HashMap<String, ArrayList<Song>> playlists;
    public static ArrayList<Song> favorite;
    public static ArrayList<Song> recentlyPlayed;

    public Database() {
    }

    public static HashMap<String, ArrayList<Song>> getPlaylists() {
        if (playlists == null || playlists.size() == 0) {
            playlists = new HashMap<>();
            loadPlaylists();
        }
        return playlists;
    }
    public static void loadPlaylists() {
        // we need to get these from database.
        // anyway...
        playlists = new HashMap<>();
        favorite = new ArrayList<>();
        recentlyPlayed = new ArrayList<>();
        // and any other playlist...

        playlists.put("Favorite tracks", favorite);
        playlists.put("Recently played", recentlyPlayed);
        // playlists object is the one that we work with.
    }
    public static void savePlaylistsToDatabase() {
        // save to database
    }
    public static boolean newPlaylist(String playlistName, ArrayList<Song> songs) {
        // if playlist added successfully, return true.
        for (Map.Entry<String, ArrayList<Song>> entry : playlists.entrySet()) {
            String key = entry.getKey();
            if (key.equals(playlistName)) {
                return false;
            }
        }
        playlists.put(playlistName, songs);
        return true;
        // playlist name should be monhaser be fard!!
    }
    public static void deletePlaylist(String name) {
        for (Map.Entry<String, ArrayList<Song>> entry : playlists.entrySet()) {
            if (entry.getKey().equals(name)) {
                playlists.remove(name);
                break;
            }
        }
    }
    public static void newFavoriteSong(Song song) {
        favorite.add(song);
    }
    public static void removeFromFavorites(Song song) {
        favorite.remove(song);
    }
    public static void newRecentlyPlayedSong(Song song) {
        // saved 100 recently played song.
        if (recentlyPlayed.size() >= 100) {
            recentlyPlayed.remove(0);
        }
        recentlyPlayed.add(song);
    }

}
