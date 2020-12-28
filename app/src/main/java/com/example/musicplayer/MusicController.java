package com.example.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MusicController extends FrameLayout {
    private Context context;
    private FrameLayout root;
    private TheMediaPlayer mediaPlayer;
    private ImageButton prevBtn, playBtn, nextBtn;
    private ImageView coverArt;
    private TextView trackName, artistName;

    public MusicController(@NonNull Context context) {
        super(context);
        this.context = context;
        Toast.makeText(context, "this is a controller", Toast.LENGTH_SHORT).show();
    }

    public void setParentLayout(FrameLayout root) {
        this.root = root;
        getButtons();
    }

    private void getButtons() {
        prevBtn = (ImageButton) root.findViewById(R.id.prevBtn);
        playBtn = (ImageButton) root.findViewById(R.id.playBtn);
        nextBtn = (ImageButton) root.findViewById(R.id.nextBtn);
        coverArt = (ImageView) root.findViewById(R.id.cover_art);
        trackName = (TextView) root.findViewById(R.id.trackNameTextView);
        artistName = (TextView) root.findViewById(R.id.artistNameTextView);

        if (playBtn != null) {
            playBtn.requestFocus();
            playBtn.setOnClickListener(playListener);
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
            playBtn.setImageResource(android.R.drawable.ic_media_pause);
            playBtn.setContentDescription("Pause");
        } else {
            playBtn.setImageResource(android.R.drawable.ic_media_play);
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
        }
        Constant.setController(this);
        updatePausePlay();
    }

    public void setCoverArt(Bitmap coverArt) {
        this.coverArt.setImageBitmap(coverArt);
    }

    public void setTrackName(String trackName) {
        this.trackName.setText(trackName);
    }

    public void setArtistName(String artistName) {
        this.artistName.setText(artistName);
    }
}
