package com.example.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Formatter;

import io.alterac.blurkit.BlurLayout;

public class MusicController extends FrameLayout {
    private Context context;
    private TheMediaPlayer mediaPlayer;
    private ImageView prevBtn, playBtn, nextBtn;
    private ImageView coverArt, backCover;
    private TextView trackName, artistName, endTimeTextView;
    private boolean isSongPlayerActivity = false;

    public MusicController(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void getButtons(ImageView aPrevBtn, ImageView aPlayBtn, ImageView aNextBtn) {
        prevBtn = aPrevBtn;
        playBtn = aPlayBtn;
        nextBtn = aNextBtn;
        isSongPlayerActivity = false;

        if (playBtn != null) {
            playBtn.requestFocus();
            playBtn.setOnClickListener(playListener);
        }
    }

    public void getDetails(ImageView aCoverArt, TextView aTrackName, TextView aArtistName) {
        coverArt = aCoverArt;
        trackName = aTrackName;
        artistName = aArtistName;
    }
    public void getEndTimeTextView(ImageView aBackCover, TextView aEndTime) {
        isSongPlayerActivity = true;
        this.endTimeTextView = aEndTime;
        this.backCover = aBackCover;
    }

    public void setMediaPlayer(TheMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        updatePausePlay();
    }

    public void show() {
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (mediaPlayer.isPlaying()) {
            playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            playBtn.setContentDescription("Pause");
        } else {
            playBtn.setImageResource(R.drawable.ic_round_play_circle_outline_24);
            playBtn.setContentDescription("Play");
        }
    }

    private final View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    private void doPauseResume() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
        updatePausePlay();
    }

    public void setController() {
        if (Constant.getController() != null) {
            MusicController lastController = Constant.getController();
            this.trackName.setText(lastController.trackName.getText());
            this.artistName.setText(lastController.artistName.getText());
            this.coverArt.setImageDrawable(lastController.coverArt.getDrawable());
            if (isSongPlayerActivity)
            {
                this.backCover.setImageDrawable(lastController.coverArt.getDrawable());
                this.endTimeTextView.setText(stringForTime(mediaPlayer.getDuration()));
            }
        }
        updatePausePlay();
    }

    public void setCoverArt(Drawable aaCoverArt) {
        this.coverArt.setImageDrawable(aaCoverArt);
    }

    public void setBackCoverArt(Drawable aCoverArt) {
        if (isSongPlayerActivity)
        {
            this.backCover.setImageDrawable(aCoverArt);
        }
    }
    public void setDuration(int duration) {
        if (isSongPlayerActivity)
        {
            this.endTimeTextView.setText(stringForTime(duration));
        }
    }

    public void setTrackName(String trackName) {
        this.trackName.setText(trackName);
    }

    public void setArtistName(String artistName) {
        this.artistName.setText(artistName);
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {

        if (nextBtn != null) {
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setOnClickListener(next);
            nextBtn.setEnabled(true);
        }
        if (prevBtn != null) {
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setOnClickListener(prev);
            prevBtn.setEnabled(true);
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        if (hours > 0) {
            return new Formatter().format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return new Formatter().format("%02d:%02d", minutes, seconds).toString();
        }
    }
}

