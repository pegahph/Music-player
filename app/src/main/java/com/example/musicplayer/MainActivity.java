package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.example.musicplayer.MusicService.MusicBinder;


// The Main Activity.  Wow!!!
public class MainActivity extends AppCompatActivity implements MediaPlayerControl {
    // we are using this ArrayList to store the Songs.
    private ArrayList<Song> songList;
    // and we are gonna show them in a ListView.
    private ListView songView;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;


    // Overriding onCreate function :)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songView = (ListView) findViewById(R.id.songList);
        songList = new ArrayList<>();

        getSongList();

        // sort the data
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });

        SongAdapter songAdapter = new SongAdapter(this, songList);
        songView.setAdapter(songAdapter);

        setController();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;

            musicService = binder.getService();

            musicService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }
    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    // it's just yee.t the menu to top of screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.the_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicService.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        // I guess we got musics now.


        if (musicCursor!=null && musicCursor.moveToFirst()) {
            // get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                songList.add(new Song(thisId, thisTitle, thisArtist));

            }while (musicCursor.moveToNext());

        }


//        assert musicCursor != null;
//        musicCursor.close();
    }

    public void songPicked(View view) {
//        Toast.makeText(musicService, "Hello", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, view.getTag().toString(), Toast.LENGTH_SHORT).show();
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setMusicController(controller);
        musicService.playSong();
        if (playbackPaused) {
//            setController();
            playbackPaused = false;
        }
//        controller.show(0);
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicService != null && musicBound && musicService.isPng())
            lastDuration = musicService.getDur();
        return lastDuration;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && musicBound && musicService.isPng())
            lastCurrentPos = musicService.getPos();
        return lastCurrentPos;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
        lastCurrentPos = pos;
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && musicBound)
            return musicService.isPng();
        else return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.songList));
        controller.setEnabled(true);
    }

    private void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
//        controller.show(0);
    }
    private void playPrev() {
        musicService.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
//        controller.show(0);
    }

}