package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import io.alterac.blurkit.BlurKit;
import io.alterac.blurkit.BlurLayout;

public class SongPlayerPage extends AppCompatActivity {
    BlurLayout blurLayout;
    MusicService theMusicService;
    Song song;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_page);
        blurLayout = findViewById(R.id.blurLayout);
        theMusicService = Constant.getMusicService();
        song = theMusicService.getCurrentSong();
        cover = findViewById(R.id.musicCover);
        Drawable songCover = song.getAlbumArtBitmapDrawable();
        if(songCover!=null){
            cover.setImageDrawable(songCover);
        }
        else {
            cover.setImageResource(R.drawable.background6);
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