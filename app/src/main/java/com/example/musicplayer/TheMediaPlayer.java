package com.example.musicplayer;

import android.content.Context;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public class TheMediaPlayer implements MediaPlayerControl {
    private MusicService musicService;
    private boolean playbackPaused=false;
    private boolean musicBound = false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;
    private MusicController controller;


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

    public void setController(Context context, View view, boolean changeContext, MusicController theController){
        if (controller == null || changeContext)
        {
            Constant.setController(new MusicController(context));
        }
        else {
            Constant.setController(theController);
        }
        controller = Constant.getController();
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(view);
        controller.setEnabled(true);
    }

    private void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }
    private void playPrev() {
        musicService.playPrev();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

}