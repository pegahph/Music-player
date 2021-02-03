package com.example.musicplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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

    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    private AudioManager audioManager;

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
    private boolean isMenu = false;
    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    NotificationBuilder notificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        startPos = 0;
        player = new MediaPlayer();
        rand = new Random();
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        initMusicPlayer();
        //registerBecomingNoisyReceiver();
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

        if (musicService != null && !isMenu)
            handleIntent(intent);
        else if (isMenu){
            isMenu = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // start playback
        mp.start();
        Song currentSong = songs.get(songPos);
        PlaylistMaker.newRecentlyPlayedSong(currentSong);
        isPaused = false;
        isPlayed = true;
        MusicController thisController = Constant.getController();
        thisController.show();
        thisController.setTrackName(songTitle);
        thisController.setArtistName(songArtist);
        BitmapDrawable currentSongAlbumArt = currentSong.getAlbumArtBitmapDrawable();

        thisController.setCoverArt(currentSongAlbumArt);
        thisController.setBackCoverArt(currentSongAlbumArt);

        thisController.setDuration(getDur());

        newNotification();
        if (isPlayed)
            notificationBuilder.builder(ACTION_PAUSE);
        if (isPaused)
            notificationBuilder.builder(ACTION_PLAY);

    }

    private void newNotification() {
        if (songs == null) {
            setList(PlaylistMaker.lastList);
            songPos = songs.indexOf(PlaylistMaker.getLastSong());
        }
        notificationBuilder = new NotificationBuilder(getApplicationContext(), songs.get(songPos));
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
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
            if (!repeatOn) {
                // TODO: what if songs.size() mosavi sefr bashe??
                // I mean when it will?
                if ((songPos + 1) % songs.size() != startPos)
                    playNext();
                else {
                    playNext();
                }
            }
            // repeat on, repeat all the songs in the list
            else if (repeatAll) {
                playNext();
            } else {
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
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        onAudioFocusChange(2);
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
        if (theSongs == null) {
            theSongs = ListMaker.songList;
        }
        this.songs = theSongs;
        PlaylistMaker.lastList = this.songs;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
//  Invoked when the audio focus of the system is updated.
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (player == null) initMusicPlayer();
                else if (!player.isPlaying()) player.start();
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (player.isPlaying()) player.stop();
                player.release();
                player = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (player.isPlaying()) player.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (player.isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }
    }

    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (player != null) {
                            pausePlayer();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (player != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                go();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

//    //Becoming noisy
//    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //pause audio on ACTION_AUDIO_BECOMING_NOISY
//            pausePlayer();
//            Toast.makeText(getApplicationContext(),"on receive",Toast.LENGTH_SHORT).show();
//            //notificationBuilder.buildNotification();
//        }
//    };
//
//    private void registerBecomingNoisyReceiver() {
//        //register after getting audio focus
//        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        registerReceiver(becomingNoisyReceiver, intentFilter);
//    }

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
        onAudioFocusChange(3);
        if (notificationBuilder == null)
        {
            newNotification();
            playSong();
        }
        notificationBuilder.builder(ACTION_PLAY);
    }

    public void seek(int pos) {
        player.seekTo(pos);
    }

    public void go() {
        AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        if (mAudioManager.isMusicActive()) {

            Intent pauseIntent = new Intent("com.android.music.musicservicecommand");

            pauseIntent.putExtra("command", "stop");
            pauseIntent.putExtra("command", "pause");
            MusicService.this.sendBroadcast(pauseIntent);
            if (requestAudioFocus())
            onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
            //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
        }

        player.start();
        //Toast.makeText(getApplicationContext(),"no",Toast.LENGTH_SHORT).show();
        isPlayed = true;
        isPaused = false;
        if (notificationBuilder == null)
        {
            newNotification();
            playSong();
        }
        notificationBuilder.builder(ACTION_PAUSE);
    }

    public void playPrev() {
        songPos--;
        // todo: song is null
        if (songPos < 0) songPos = songs.size() - 1;
        playSong();
    }

    public void playNext() {
        if (shuffle) {
            int newSong = songPos;
            while (newSong == songPos) {
                newSong = rand.nextInt(songs.size());
            }
            songPos = newSong;
        } else {
            songPos++;
            if (songPos >= songs.size()) songPos = 0;
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
        } else if (repeatAll) {
            repeatAll = false;
            Toast.makeText(getApplicationContext(), "repeat one", Toast.LENGTH_SHORT).show();
        } else {
            repeatOn = false;
            Toast.makeText(getApplicationContext(), "repeat off", Toast.LENGTH_SHORT).show();
        }
    }

    public Song getCurrentSong() {
        if (songs == null) {
            setList(PlaylistMaker.lastList);
            songPos = songs.indexOf(PlaylistMaker.getLastSong());
        }
        return songs.get(songPos);
    }

}
