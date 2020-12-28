package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TheMediaPlayer implements MediaPlayerControl {
    private MusicService musicService;
    private boolean playbackPaused=false;
    private boolean musicBound = false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;
//    private MusicController controller;
    private LinearLayout controllerLayout;
    private ImageButton prevBtn, playBtn, nextBtn;
    private ImageView coverArt;
    private TextView trackName, artistName;


    public void setCoverArt(Bitmap coverArt) {
        this.coverArt.setImageBitmap(coverArt);
    }

    public void setTrackName(String trackName) {
        this.trackName.setText(trackName);
    }

    public void setArtistName(String artistName) {
        this.artistName.setText(artistName);
    }

    public void setPlayBtn(boolean isPlaying) {
        if (isPlaying)
            playBtn.setImageResource(android.R.drawable.ic_media_pause);
        else
            playBtn.setImageResource(android.R.drawable.ic_media_play);
    }

    public TheMediaPlayer(MusicService musicService) {
        this.musicService = musicService;
        this.musicBound = Constant.isMusicBound();
    }

    public void setAnchorLayout(LinearLayout controllerLayout) {
        this.controllerLayout = controllerLayout;
        prevBtn = (ImageButton) this.controllerLayout.findViewById(R.id.prevBtn);
        playBtn = (ImageButton) this.controllerLayout.findViewById(R.id.playBtn);
        nextBtn = (ImageButton) this.controllerLayout.findViewById(R.id.nextBtn);
        coverArt = (ImageView) this.controllerLayout.findViewById(R.id.cover_art);
        trackName = (TextView) this.controllerLayout.findViewById(R.id.trackNameTextView);
        artistName = (TextView) this.controllerLayout.findViewById(R.id.artistNameTextView);

        setClickListener();
    }

    private void setClickListener() {
        // play-pause button
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.isPlayed) {
                    pause();
                    playBtn.setImageResource(android.R.drawable.ic_media_play);
                }
                else {
                    start();
                    playBtn.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        // next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
                playBtn.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        // previous button
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
                playBtn.setImageResource(android.R.drawable.ic_media_pause);
            }
        });
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
