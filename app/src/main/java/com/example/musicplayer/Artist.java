package com.example.musicplayer;

public class Artist {
    private long albumId;
    private String artistName;


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

}
