package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.Formatter;

import io.alterac.blurkit.BlurLayout;

public class SongPlayerPage extends AppCompatActivity {
    BlurLayout blurLayout;
    MusicService theMusicService;
//    Song song;
    View grayView;
    ImageView backCover , forCover;
    TextView songName , songArtist;
    TheMediaPlayer mediaPlayer;
    ImageButton prevBtn, playBtn, nextBtn, shuffleBtn, repeatBtn, favoriteBtn, addToPlaylist, volume;
    SeekBar seekBar;
    TextView currentTime, endTime, lyricsTextView;
    int targetPosition;
    boolean isTracking = false, gray = true;
    MusicController theController;
    Runnable timerRunnable;
    boolean killMe = false, isCoverVisible = true;
    final Handler updateHandler = new Handler();
    // search stuff
    Space searchBarPlaceholder;
    EditText theSearchBar;
    ImageView searchBtn, shareBtn, deleteBtn;
    RecyclerView searchRecyclerView;
    CardView cardView;

    private float x1,y1,x2, y2;
    static final int MIN_X_DISTANCE = 200;
    static final int MIN_Y_DISTANCE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_song_player_page);
        getSupportActionBar().hide();
        blurLayout = findViewById(R.id.blurLayout);
        theMusicService = Constant.getMusicService();

        backCover = findViewById(R.id.backCover);
        forCover = findViewById(R.id.forCover);
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        grayView = findViewById(R.id.gray_view);
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
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        cardView = (CardView) findViewById(R.id.card_view);
        volume = (ImageButton) findViewById(R.id.volume);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        lyricsTextView = (TextView) findViewById(R.id.lyrics_text_view);

        mediaPlayer = Constant.getTheMediaPlayer();
        theController = new com.example.musicplayer.MusicController(getApplicationContext());
        theController.getButtons(prevBtn, playBtn, nextBtn);
        theController.setMediaPlayer(mediaPlayer);
        theController.getDetails(forCover, songName, songArtist);
        theController.getEndTimeTextView(backCover, endTime, blurLayout, grayView, shuffleBtn, repeatBtn, seekBar, favoriteBtn, addToPlaylist);
        theController.getSearchStuff(searchBarPlaceholder, theSearchBar, searchBtn, shareBtn, deleteBtn, imm, searchRecyclerView, currentTime, cardView, volume);
        theController.getLyricsStuff(lyricsTextView, this);
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (Math.abs(deltaX) > MIN_X_DISTANCE && Math.abs(deltaY) < MIN_Y_DISTANCE) {
                    if (deltaX > 0) {
                        mediaPlayer.playPreviousSong();
                    }
                    else {
                        mediaPlayer.playNextSong();
                    }
                } else if (Math.abs(deltaX) < MIN_X_DISTANCE && Math.abs(deltaY) > MIN_Y_DISTANCE) {
                    if (deltaY > 0) {
                        swipeDown();
                    }
                    else {
                        swipeUp();
                    }
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
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

    private void swipeDown() {
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void swipeUp() {
        if (forCover.getVisibility() == View.VISIBLE)
            isCoverVisible = true;
        if (isCoverVisible) {
            forCover.setVisibility(View.INVISIBLE);
            lyricsTextView.setVisibility(View.VISIBLE);
            isCoverVisible = false;
        } else {
            forCover.setVisibility(View.VISIBLE);
            lyricsTextView.setVisibility(View.INVISIBLE);
            isCoverVisible = true;
        }
    }
}