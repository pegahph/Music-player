package com.example.musicplayer;

import android.widget.MediaController.MediaPlayerControl;

public class TheMediaPlayer implements MediaPlayerControl {
    private MusicService musicService;
    private boolean paused=false, playbackPaused=false;
    private boolean musicBound = false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;


    public TheMediaPlayer(MusicService musicService) {
        this.musicService = musicService;
        this.musicBound = Constant.isMusicBound();
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicService != null && musicBound && musicService.isPng())
            lastDuration = musicService.getDur();
        return lastDuration;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && musicBound && musicService.isPng())
            lastCurrentPos = musicService.getPos();
        return lastCurrentPos;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
        lastCurrentPos = pos;
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && musicBound)
            return musicService.isPng();
        else return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
