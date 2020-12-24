package com.example.musicplayer;

import android.content.ServiceConnection;

public class Constant {
    public static ServiceConnection musicConnection;
    public static ServiceConnection getMusicConnection() {
        return musicConnection;
    }
    public static void setMusicConnection(ServiceConnection musicConnection) {
        Constant.musicConnection = musicConnection;
    }

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
}
