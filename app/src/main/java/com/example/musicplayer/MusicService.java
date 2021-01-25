package com.example.musicplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer player;

    private MusicService musicService;
    public void setMusicService() {
        this.musicService = Constant.getMusicService();
    }

    private ArrayList<Song> songs;
    private int songPos;
    private int startPos;

    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private String songArtist = "";
    private boolean shuffle = false;

    //  repeatOn = false  means play all songs once, and then stop.
    //  repeatOn = true and repeatAll = false  means play one song again and again.
    //  repeatOn = true and repeatAll = true  means play all songs and then repeat all of them.
    private boolean repeatOn = false;
    private boolean repeatAll = false;
    private Random rand;
    boolean isPaused = false;
    public static boolean isPlayed = false;

    NotificationBuilder notificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        startPos = 0;
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
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (musicService != null)
            handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // start playback
        mp.start();
        isPaused = false;
        isPlayed = true;
        Song currentSong = songs.get(songPos);
        MusicController thisController = Constant.getController();
        thisController.show();
        thisController.setTrackName(songTitle);
        thisController.setArtistName(songArtist);
        BitmapDrawable currentSongAlbumArt = currentSong.getAlbumArtBitmapDrawable();
        if (currentSongAlbumArt != null)
        {
            thisController.setCoverArt(currentSongAlbumArt);
            thisController.setBackCoverArt(currentSongAlbumArt);
        }
        thisController.setDuration(getDur());

        newNotification();
        if (isPlayed)
            notificationBuilder.builder(ACTION_PAUSE);
        if (isPaused)
            notificationBuilder.builder(ACTION_PLAY);

        PlaylistMaker.newRecentlyPlayedSong(currentSong);
    }

    private void newNotification() {
        if (songs == null) {
            setList(PlaylistMaker.recentlyPlayed);
        }
        notificationBuilder = new NotificationBuilder(getApplicationContext(), songs.get(songPos));
    }

    private void handleIntent(Intent intent) {
        if(intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            musicService.go();
            Constant.getController().show();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            musicService.pausePlayer();
            Constant.getController().show();
        } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
        } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            musicService.playPrev();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            musicService.playNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // this method will fire when a track ends.
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            // repeat off.
            if (!repeatOn)
            {
                // TODO: what if songs.size() mosavi sefr bashe??
                // I mean when it will?
                if ((songPos+1) % songs.size() != startPos)
                    playNext();
                else
                {
                    playNext();
                }
            }
            // repeat on, repeat all the songs in the list
            else if (repeatAll) {
                playNext();
            }
            else {
                playSong();
            }
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
        songArtist = playSong.getArtist();

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
        startPos = songIndex;
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
        isPaused = true;
        isPlayed = false;
        if (notificationBuilder == null)
            newNotification();
        notificationBuilder.builder(ACTION_PLAY);
    }
    public void seek(int pos) {
        player.seekTo(pos);
    }
    public void go() {
        player.start();
        isPlayed = true;
        isPaused = false;
        if (notificationBuilder == null)
            newNotification();
        notificationBuilder.builder(ACTION_PAUSE);
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
    public boolean getShuffle() {
        return shuffle;
    }

    public void changeRepeatStatus() {
        if (!repeatOn) {
            repeatOn = true;
            repeatAll = true;
            Toast.makeText(getApplicationContext(), "repeat all", Toast.LENGTH_SHORT).show();
        }
        else if (repeatAll) {
            repeatAll = false;
            Toast.makeText(getApplicationContext(), "repeat one", Toast.LENGTH_SHORT).show();
        }
        else {
            repeatOn = false;
            Toast.makeText(getApplicationContext(), "repeat off", Toast.LENGTH_SHORT).show();
        }
    }

    public Song getCurrentSong(){
        if (songs == null) {
            setList(PlaylistMaker.recentlyPlayed);
        }
        return songs.get(songPos);
    }

}