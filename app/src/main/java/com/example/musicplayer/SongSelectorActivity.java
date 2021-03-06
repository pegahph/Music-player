package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongSelectorActivity extends AppCompatActivity {

    private TextView folderName;
    private TextView songNumbers;
    private RecyclerView songSelectorList;
    private ArrayList<Song> theSongs;
    private String sectionName;
    private long sectionAlbumId;
    private MusicService musicService;
    private MusicController controller;
    private TheMediaPlayer mediaPlayer;
    private Folder songFolder;
    private ImageView albumImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_song_selector);
        getSupportActionBar().hide();
        folderName = (TextView) findViewById(R.id.folderName);
        folderName.setSelected(true);
        songNumbers = findViewById(R.id.songNumbers);
        albumImage = findViewById(R.id.albumImage);
        theSongs = new ArrayList<>();
        Intent in = getIntent();
        String tab = in.getStringExtra("tab");
        assert tab != null;
        switch (tab) {
            case "Artist":
                sectionName = in.getStringExtra("selectedArtistName");
                sectionAlbumId = in.getLongExtra("selectedArtistAlbumId", 0);
                theSongs = ListMaker.getThisArtistsSongs(sectionName);
                break;
            case "Folder":
                sectionName = in.getStringExtra("selectedFolderName");
                sectionAlbumId = in.getLongExtra("selectedFolderAlbumId", 0);
                theSongs = ListMaker.getThisFolderSongs(sectionName);
                break;
            case "Playlist":
                sectionName = in.getStringExtra("selectedPlaylistName");
//            sectionAlbumId = in.getLongExtra("selectedFolderAlbumId", 0);
                theSongs = PlaylistMaker.loadThisPlaylist(sectionName);
                break;
        }

        songFolder = new Folder(sectionAlbumId , sectionName);
        Drawable drawable = songFolder.getAlbumArtBitmapDrawable();
        if(drawable!= null){
            albumImage.setImageDrawable(drawable);
        }
        songSelectorList = (RecyclerView) findViewById(R.id.song_selector_list);

        SongAdapter songAdapter = new SongAdapter(theSongs);
        songSelectorList.setAdapter(songAdapter);
        songSelectorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        musicService = Constant.getMusicService();

        mediaPlayer = Constant.getTheMediaPlayer();
        mediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
        controller = Constant.getController();
        songNumbers.setText(String.format("%s songs", getSongNumbersString()));
        folderName.setText(sectionName);
    }

    private String getSongNumbersString(){
        return Integer.toString(theSongs.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
        controller = Constant.getController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.changeKillMe(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.changeKillMe(true);
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setList(theSongs);
        musicService.playSong();
    }


    public void controllerClicked(View view) {
        Intent intent = new Intent(SongSelectorActivity.this , SongPlayerPage.class);
        startActivity(intent);    }

    public void backToPrevious(View view) {
        finish();
    }
}