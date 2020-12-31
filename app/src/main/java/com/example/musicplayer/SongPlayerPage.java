package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;

import io.alterac.blurkit.BlurKit;
import io.alterac.blurkit.BlurLayout;

public class SongPlayerPage extends AppCompatActivity {
    BlurLayout blurLayout;
    MusicService theMusicService;
//    Song song;
    ImageView backCover , forCover;
    TextView songName , songArtist;
    TheMediaPlayer mediaPlayer;
    ImageView prevBtn, playBtn, nextBtn;
    SeekBar seekBar;
    TextView currentTime, endTime;
    int targetPosition;
    boolean isTracking = false;
    MusicController theController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_page);
        getSupportActionBar().hide();
        blurLayout = findViewById(R.id.blurLayout);
        theMusicService = Constant.getMusicService();

//        song = theMusicService.getCurrentSong();
        backCover = findViewById(R.id.backCover);
        forCover = findViewById(R.id.forCover);
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
//        songName.setText(song.getTitle());
//        songArtist.setText(song.getArtist());
        songName.setSelected(true);
//        Drawable songCover = song.getAlbumArtBitmapDrawable();
//        if(songCover!=null){
//            backCover.setImageDrawable(songCover);
//            forCover.setImageDrawable(songCover);
//        }
//        else {
//            backCover.setImageResource(R.drawable.background6);
//            forCover.setImageResource(R.drawable.background6);
//        }

        prevBtn = (ImageView) findViewById(R.id.full_prev_btn);
        playBtn = (ImageView) findViewById(R.id.full_play_btn);
        nextBtn = (ImageView) findViewById(R.id.full_next_btn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.end_time);
        currentTime = (TextView) findViewById(R.id.current_time);

        mediaPlayer = Constant.getTheMediaPlayer();
        theController = new MusicController(getApplicationContext());
        theController.getButtons(prevBtn, playBtn, nextBtn);
        theController.getDetails(forCover, songName, songArtist);
        theController.getEndTimeTextView(backCover, endTime);
        theController.setMediaPlayer(mediaPlayer);
        theController.setController();
        Constant.setController(theController);
        mediaPlayer.setClickListener(theController);

        Constant.setController(theController);

        int timeMs = mediaPlayer.getDuration();
        seekBar.setMax(timeMs);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                targetPosition = seekBar.getProgress();
                currentTime.setText(stringForTime(targetPosition));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTracking = false;
                mediaPlayer.seekTo(targetPosition);
            }
        });

        final Handler updateHandler = new Handler();

        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                int currPos = mediaPlayer.getCurrentPosition();
                if (!isTracking)
                {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(stringForTime(currPos));
                }
                updateHandler.postDelayed(this, 250);
            }
        };

        updateHandler.postDelayed(timerRunnable, 250);

    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
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