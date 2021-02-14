package com.example.musicplayer;

import android.content.ServiceConnection;

public class Constant {
    public static MusicService musicService;
    public static MusicService getMusicService() {
        return musicService;
    }
    public static void setMusicService(MusicService musicService) {
        Constant.musicService = musicService;
    }

    public static MusicController controller;
    public static MusicController getController() {
        return controller;
    }
    public static void setController(MusicController controller) {
        Constant.controller = controller;
    }

    public static boolean musicBound;
    public static boolean isMusicBound() {
        return musicBound;
    }
    public static void setMusicBound(boolean musicBound) {
        Constant.musicBound = musicBound;
    }

    public static TheMediaPlayer theMediaPlayer;
    public static TheMediaPlayer getTheMediaPlayer() {
        return theMediaPlayer;
    }
    public static void setTheMediaPlayer(TheMediaPlayer theMediaPlayer) {
        Constant.theMediaPlayer = theMediaPlayer;
    }


}
