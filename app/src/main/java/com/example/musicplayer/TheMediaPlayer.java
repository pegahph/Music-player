package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TheMediaPlayer implements MediaPlayerControl {
    private MusicService musicService;
    private Context context;
    private boolean playbackPaused=false;
    private boolean musicBound = false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;
    private MusicController controllerLayout;




    public TheMediaPlayer(MusicService musicService, Context context) {
        this.musicService = musicService;
        this.musicBound = Constant.isMusicBound();
        this.context = context;
    }

    public void setControllerLayout(FrameLayout controllerLayout, MusicController controller) {
        if (controller == null)
            this.controllerLayout = new MusicController(context);
        else
            this.controllerLayout = controller;
        this.controllerLayout.setParentLayout(controllerLayout);
        this.controllerLayout.setMediaPlayer(this);
        this.controllerLayout.setController();
        setClickListener();
    }

    private void setClickListener() {
        View.OnClickListener next = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        };
        View.OnClickListener prev = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        };
        this.controllerLayout.setPrevNextListeners(next, prev);
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

    public void setController(){

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
