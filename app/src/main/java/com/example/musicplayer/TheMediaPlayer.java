package com.example.musicplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.TextView;

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

    public void setControllerLayout(ViewGroup controllerLayout, MusicController controller) {
        if (controller == null)
            this.controllerLayout = new MusicController(context);
        else
            this.controllerLayout = controller;
        getButtons(controllerLayout);
        this.controllerLayout.setMediaPlayer(this);
        this.controllerLayout.setController();
        Constant.setController(this.controllerLayout);
        setClickListener(this.controllerLayout);
    }

    public void setClickListener(MusicController aController) {
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
        aController.setPrevNextListeners(next, prev);
    }

    private void getButtons(ViewGroup controllerLayout) {
        ImageButton aPrevBtn = (ImageButton) controllerLayout.findViewById(R.id.prevBtn);
        ImageButton aPlayBtn = (ImageButton) controllerLayout.findViewById(R.id.playBtn);
        ImageButton aNextBtn = (ImageButton) controllerLayout.findViewById(R.id.nextBtn);
        ImageView aCoverArt = (ImageView) controllerLayout.findViewById(R.id.cover_art);
        TextView aTrackName = (TextView) controllerLayout.findViewById(R.id.trackNameTextView);
        TextView aArtistName = (TextView) controllerLayout.findViewById(R.id.artistNameTextView);

        this.controllerLayout.getButtons(aPrevBtn, aPlayBtn, aNextBtn);
        this.controllerLayout.getDetails(aCoverArt, aTrackName, aArtistName);
    }

    public void setShuffle() {
        musicService.setShuffle();
    }
    public boolean getShuffle() {
        return musicService.getShuffle();
    }
    public void changeRepeatStatus() {
        musicService.changeRepeatStatus();
    }
    public Song getCurrentSong() {
        return musicService.getCurrentSong();
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

    private void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            playbackPaused = false;
        }
        Constant.getController().updateFavorite();
    }
    private void playPrev() {
        musicService.playPrev();
        if (playbackPaused) {
            playbackPaused = false;
        }
        Constant.getController().updateFavorite();
    }

}
