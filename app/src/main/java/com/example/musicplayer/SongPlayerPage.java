package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.alterac.blurkit.BlurKit;
import io.alterac.blurkit.BlurLayout;

public class SongPlayerPage extends AppCompatActivity {
    BlurLayout blurLayout;
    MusicService theMusicService;
    Song song;
    ImageView backCover , forCover;
    TextView songName , songArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_page);
        getSupportActionBar().hide();
        blurLayout = findViewById(R.id.blurLayout);
        theMusicService = Constant.getMusicService();
        song = theMusicService.getCurrentSong();
        backCover = findViewById(R.id.backCover);
        forCover = findViewById(R.id.forCover);
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        songName.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        songName.setSelected(true);
        Drawable songCover = song.getAlbumArtBitmapDrawable();
        if(songCover!=null){
            backCover.setImageDrawable(songCover);
            forCover.setImageDrawable(songCover);
        }
        else {
            backCover.setImageResource(R.drawable.background6);
            forCover.setImageResource(R.drawable.background6);
        }
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

}