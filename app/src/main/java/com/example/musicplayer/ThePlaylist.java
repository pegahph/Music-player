package com.example.musicplayer;

import java.util.ArrayList;

public class ThePlaylist {
    private String playlistName;
    private ArrayList<Song> playlist;

    public ThePlaylist(String playlistName) {
        this.playlistName = playlistName;
        this.playlist = new ArrayList<>();
    }

    public void addToPlaylist(Song song) {
        playlist.add(song);
    }
    public void removeFromPlaylist(Song song) {
        playlist.remove(song);
    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }
    public String getPlaylistName() {
        return playlistName;
    }

}
