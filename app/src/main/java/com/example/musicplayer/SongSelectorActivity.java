package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SongSelectorActivity extends AppCompatActivity {

    private TextView placeholder;
    private RecyclerView songSelectorList;
    private ArrayList<Song> thisArtistSongs;
    private String artistName;
    private long artistAlbumId;
    private MusicService musicService;
    private ServiceConnection musicConnection;
    private MusicController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selector);

        Intent in = getIntent();
        artistName = in.getStringExtra("selectedArtistName");
        artistAlbumId = in.getLongExtra("selectedArtistAlbumId", 0);

        placeholder = (TextView) findViewById(R.id.placeholder);
        songSelectorList = (RecyclerView) findViewById(R.id.song_selector_list);

        thisArtistSongs = new ArrayList<>();
        thisArtistSongs = ListMaker.getThisArtistsSongs(artistName);

        SongAdapter songAdapter = new SongAdapter(thisArtistSongs);
        songSelectorList.setAdapter(songAdapter);
        songSelectorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        musicService = Constant.getMusicService();
        musicService.setList(thisArtistSongs);
        musicConnection = Constant.getMusicConnection();

        TheMediaPlayer mediaPlayer = Constant.getTheMediaPlayer();
        mediaPlayer.setController(SongSelectorActivity.this, findViewById(R.id.song_selector_layout), true, null);
//        controller = Constant.getController();
//        controller.hide();
//        controller.setAnchorView(findViewById(R.id.song_selector_list));

    }

    public void songPicked(View view) {
        Toast.makeText(this, "artist", Toast.LENGTH_SHORT).show();
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setMusicController();
        musicService.playSong();
//        if (playbackPaused) {
////            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);
    }
}