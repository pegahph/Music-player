package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongSelectorActivity extends AppCompatActivity {

    private TextView placeholder;
    private RecyclerView songSelectorList;
    private ArrayList<Song> theSongs;
    private String sectionName;
    private long sectionAlbumId;
    private MusicService musicService;
    private ServiceConnection musicConnection;
    private MusicController controller;
    private TheMediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selector);



        theSongs = new ArrayList<>();

        Intent in = getIntent();
        String tab = in.getStringExtra("tab");
        assert tab != null;
        if (tab.equals("Artist"))
        {
            sectionName = in.getStringExtra("selectedArtistName");
            sectionAlbumId = in.getLongExtra("selectedArtistAlbumId", 0);
            theSongs = ListMaker.getThisArtistsSongs(sectionName);
        }
        else if (tab.equals("Folder"))
        {
            sectionName = in.getStringExtra("selectedFolderName");
            sectionAlbumId = in.getLongExtra("selectedFolderAlbumId", 0);
            theSongs = ListMaker.getThisFolderSongs(sectionName);
        }

        else if (tab.equals("Playlist"))
        {
            sectionName = in.getStringExtra("selectedPlaylistName");
//            sectionAlbumId = in.getLongExtra("selectedFolderAlbumId", 0);
            theSongs = PlaylistMaker.loadThisPlaylist(sectionName);
            placeholder.setText(sectionName);
        }

//        placeholder = (TextView) findViewById(R.id.placeholder);
        songSelectorList = (RecyclerView) findViewById(R.id.song_selector_list);

        SongAdapter songAdapter = new SongAdapter(theSongs);
        songSelectorList.setAdapter(songAdapter);
        songSelectorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        musicService = Constant.getMusicService();
        musicConnection = Constant.getMusicConnection();

        mediaPlayer = Constant.getTheMediaPlayer();
        mediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
        controller = Constant.getController();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
        controller = Constant.getController();
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setList(theSongs);
        musicService.playSong();
    }


    public void controllerClicked(View view) {
        Intent intent = new Intent(SongSelectorActivity.this , SongPlayerPage.class);
        startActivity(intent);    }
}