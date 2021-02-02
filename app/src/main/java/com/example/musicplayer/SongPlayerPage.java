package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Space;
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
    ImageButton prevBtn, playBtn, nextBtn, shuffleBtn, repeatBtn, favoriteBtn, addToPlaylist;
    SeekBar seekBar;
    TextView currentTime, endTime;
    int targetPosition;
    boolean isTracking = false;
    MusicController theController;
    Runnable timerRunnable;
    boolean killMe = false;
    final Handler updateHandler = new Handler();
    // search stuff
    Space searchBarPlaceholder;
    EditText theSearchBar;
    ImageView searchBtn, shareBtn, deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_page);
        getSupportActionBar().hide();
        blurLayout = findViewById(R.id.blurLayout);
        theMusicService = Constant.getMusicService();

        backCover = findViewById(R.id.backCover);
        forCover = findViewById(R.id.forCover);
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        View grayView = findViewById(R.id.gray_view);
        songName.setSelected(true);

        prevBtn = (ImageButton) findViewById(R.id.full_prev_btn);
        playBtn = (ImageButton) findViewById(R.id.full_play_btn);
        nextBtn = (ImageButton) findViewById(R.id.full_next_btn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.end_time);
        currentTime = (TextView) findViewById(R.id.current_time);
        shuffleBtn = (ImageButton) findViewById(R.id.shuffle_button);
        repeatBtn = (ImageButton) findViewById(R.id.repeat_button);
        favoriteBtn = (ImageButton) findViewById(R.id.favourite);
        addToPlaylist = (ImageButton) findViewById(R.id.add_to_playlist);
        // share stuff
        searchBarPlaceholder = (Space) findViewById(R.id.search_bar_placeholder);
        theSearchBar = (EditText) findViewById(R.id.the_search_bar);
        searchBtn = (ImageView) findViewById(R.id.search_btn);
        shareBtn = (ImageView) findViewById(R.id.share_btn);
        deleteBtn = (ImageView) findViewById(R.id.delete_btn);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);


        mediaPlayer = Constant.getTheMediaPlayer();
        theController = new com.example.musicplayer.MusicController(getApplicationContext());
        theController.getButtons(prevBtn, playBtn, nextBtn);
        theController.setMediaPlayer(mediaPlayer);
        theController.getDetails(forCover, songName, songArtist);
        theController.getEndTimeTextView(backCover, endTime, blurLayout, grayView, shuffleBtn, repeatBtn, seekBar, favoriteBtn, addToPlaylist);
        theController.getSearchStuff(searchBarPlaceholder, theSearchBar, searchBtn, shareBtn, deleteBtn, imm);
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


        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isTracking && !killMe)
                {
                    int currPos = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(stringForTime(currPos));
                }
                if (!killMe)
                    updateHandler.postDelayed(this, 250);
            }
        };

        updateHandler.postDelayed(timerRunnable, 250);

    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
        killMe = false;
        updateHandler.postDelayed(timerRunnable, 250);
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        killMe = true;
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

    public void backToPrevious(View view) {
       finish();
    }
}