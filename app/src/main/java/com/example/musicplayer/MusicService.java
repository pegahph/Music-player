package com.example.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service
        implements MediaPlayer.OnPreparedListener,
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnCompletionListener,
            AudioManager.OnAudioFocusChangeListener {

    private static final int NOTIFY_ID = 1;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPos;
    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private boolean shuffle = false;
    private Random rand;

    private MusicController musicController;
    public void setMusicController(MusicController musicController) {
        this.musicController = musicController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        player = new MediaPlayer();
        rand = new Random();

        initMusicPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // start playback
        mp.start();
        musicController.show(0);
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        // this method will fire when a track ends.
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
            //
        }
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        this.songs = theSongs;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }


    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        player.reset();
        // get song
        Song playSong = songs.get(songPos);

        songTitle = playSong.getTitle();
        // get id
        long currSong = playSong.getId();

        // set uri
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
            // this method should have try/catch block!!
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source");
        }

        player.prepareAsync();
    }

    public void setSong(int songIndex) {
        songPos = songIndex;
    }

    public int getPos() {
        return player.getCurrentPosition();
    }
    public int getDur() {
        return player.getDuration();
    }
    public boolean isPng() {
        return player.isPlaying();
    }
    public void pausePlayer() {
        player.pause();
    }
    public void seek(int pos) {
        player.seekTo(pos);
    }
    public void go() {
        player.start();
    }

    public void playPrev() {
        songPos--;
        if (songPos<0) songPos = songs.size()-1;
        playSong();
    }
    public void playNext() {
        if (shuffle) {
            int newSong = songPos;
            while (newSong == songPos)
            {
                newSong = rand.nextInt(songs.size());
            }
            songPos = newSong;
        }
        else {
            songPos++;
            if (songPos>=songs.size()) songPos=0;
        }
        playSong();
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }

}
