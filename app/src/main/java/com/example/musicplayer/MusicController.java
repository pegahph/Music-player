package com.example.musicplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Formatter;

import io.alterac.blurkit.BlurLayout;

public class MusicController extends FrameLayout {
    private Context context;
    private TheMediaPlayer mediaPlayer;
    private ImageButton prevBtn, playBtn, nextBtn;
    private ImageButton shuffleBtn, repeatBtn, favoriteBtn;
    private ImageView coverArt, backCover;
    private TextView trackName, artistName, endTimeTextView;
    private boolean isSongPlayerActivity = false;
    private BlurLayout blurLayout;
    private View grayView;
    private SeekBar seekBar;

    public MusicController(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void getButtons(ImageButton aPrevBtn, ImageButton aPlayBtn, ImageButton aNextBtn) {
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
    public void getEndTimeTextView(ImageView aBackCover, TextView aEndTime, BlurLayout blurLayout,
                                   View grayView, ImageButton shuffleBtn, ImageButton repeatBtn, SeekBar seekBar, ImageButton favoriteBtn) {
        isSongPlayerActivity = true;
        this.endTimeTextView = aEndTime;
        this.backCover = aBackCover;
        this.blurLayout = blurLayout;
        this.grayView = grayView;
        this.shuffleBtn = shuffleBtn;
        this.repeatBtn = repeatBtn;
        this.seekBar = seekBar;
        this.favoriteBtn = favoriteBtn;

        if (this.shuffleBtn != null) {
            shuffleBtn.requestFocus();
            shuffleBtn.setOnClickListener(shuffleListener);
            updateShuffle();
        }

        if (this.repeatBtn != null) {
            repeatBtn.requestFocus();
            repeatBtn.setOnClickListener(repeatListener);
//            updateShuffle();
        }

        if (this.favoriteBtn != null) {
            favoriteBtn.requestFocus();
            favoriteBtn.setOnClickListener(favoriteListener);
            updateFavorite();
        }
    }

    public void setMediaPlayer(TheMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        updatePausePlay();
    }

    public void show() {
        updatePausePlay();
//        if (isSongPlayerActivity) {
//            updateShuffle();
//            seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            Toast.makeText(context, stringForTime(mediaPlayer.getCurrentPosition()), Toast.LENGTH_SHORT).show();
//        }
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
    private final View.OnClickListener shuffleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doShuffle();
        }
    };
    private final View.OnClickListener repeatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeRepeat();
        }
    };
    private final View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            favorite();
        }
    };

    private void changeRepeat() {
        mediaPlayer.changeRepeatStatus();
    }

    private void favorite() {
        Song currentSong = mediaPlayer.getCurrentSong();
        if (currentSong.isFavorite()) {
            PlaylistMaker.removeFromFavorites(currentSong);
        }
        else {
            PlaylistMaker.newFavoriteSong(currentSong);
        }
        currentSong.changeFavorite();
        updateFavorite();
    }

    private void doShuffle() {
        mediaPlayer.setShuffle();
        updateShuffle();
    }

    private void updateShuffle() {
        if (mediaPlayer.getShuffle()) {
            shuffleBtn.setImageResource(R.drawable.shuffle_on);
        }
        else {
            shuffleBtn.setImageResource(R.drawable.shuffle_off);
        }
    }

    public void updateFavorite() {
        Song currentSong = mediaPlayer.getCurrentSong();
        if (currentSong.isFavorite()) {
            favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else {
            favoriteBtn.setImageResource(R.drawable.ic_twotone_favorite_24);
        }
    }

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
            this.blurLayout.setVisibility(INVISIBLE);
            this.grayView.setVisibility(INVISIBLE);

            this.backCover.setImageDrawable(aCoverArt);
            this.blurLayout.invalidate();

            this.blurLayout.setVisibility(VISIBLE);
            this.grayView.setVisibility(VISIBLE);

//            this.blurLayout.startBlur();
//            this.blurLayout
            setController();
        }
    }
    public void setDuration(int duration) {
        if (isSongPlayerActivity)
        {
            this.endTimeTextView.setText(stringForTime(duration));
            this.seekBar.setMax(duration);
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