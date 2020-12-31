package com.example.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Formatter;

public class MusicController extends FrameLayout {
    private Context context;
    private TheMediaPlayer mediaPlayer;
    private ImageView prevBtn, playBtn, nextBtn;
    private ImageView coverArt;
    private TextView trackName, artistName;
    private boolean hasTextView = false;


    public MusicController(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void getButtons(ImageView aPrevBtn, ImageView aPlayBtn, ImageView aNextBtn) {
        prevBtn = aPrevBtn;
        playBtn = aPlayBtn;
        nextBtn = aNextBtn;

        if (playBtn != null) {
            playBtn.requestFocus();
            playBtn.setOnClickListener(playListener);
        }
    }

    public void getDetails(ImageView aCoverArt, TextView aTrackName, TextView aArtistName) {
        coverArt = aCoverArt;
        trackName = aTrackName;
        artistName = aArtistName;
        hasTextView = true;
    }

    public void getDetailsForAssurance(ImageView aCoverArt, TextView aTrackName, TextView aArtistName) {
        if (!hasTextView)
        {
            coverArt = aCoverArt;
            trackName = aTrackName;
            artistName = aArtistName;
        }
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
            if (hasTextView)
            {
                this.trackName.setText(lastController.trackName.getText());
                this.artistName.setText(lastController.artistName.getText());
                this.coverArt.setImageDrawable(lastController.coverArt.getDrawable());
            }
        }
        updatePausePlay();
    }

    public void setCoverArt(Bitmap coverArt) {
        if (hasTextView)
            this.coverArt.setImageBitmap(coverArt);
    }

    public void setTrackName(String trackName) {
        if (hasTextView)
            this.trackName.setText(trackName);
    }

    public void setArtistName(String artistName) {
        if (hasTextView)
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
}

