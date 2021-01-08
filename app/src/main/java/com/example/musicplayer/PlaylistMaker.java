package com.example.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistMaker {
    public static HashMap<String, ArrayList<Song>> playlists;
    public static ArrayList<Song> favorite;
    public static ArrayList<Song> recentlyPlayed;

    public PlaylistMaker() {
    }

    public static HashMap<String, ArrayList<Song>> getPlaylists() {
        if (playlists == null || playlists.size() == 0) {
            playlists = new HashMap<>();
            loadPlaylists();
        }
        return playlists;
    }

    public static void savePlaylist() {

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
    public static ArrayList<Song> loadThisPlaylist(String key) {
        return playlists.get(key);
    }
    public static void newFavoriteSong(Song song) {
        favorite.add(song);
    }
    public static void removeFromFavorites(Song song) {
        favorite.remove(song);
    }
    public static void newRecentlyPlayedSong(Song song) {
        // we saved only 100 song that played recently.
        recentlyPlayed.remove(song);

        // TODO: 10 should be 100 and 9 should be 99.
        if (recentlyPlayed.size() >= 10) {
            recentlyPlayed.remove(9);
        }
        recentlyPlayed.add(0, song);
    }

}