package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import io.alterac.blurkit.BlurLayout;

public class SongPlayerPage extends AppCompatActivity {
    BlurLayout blurLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player_page);
        blurLayout = findViewById(R.id.blurLayout);
        blurLayout.startBlur();
    }
}