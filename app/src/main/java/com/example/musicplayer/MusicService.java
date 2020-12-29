package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.app.NotificationCompat;

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
    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private String songArtist = "";
    private boolean shuffle = false;
    private Random rand;
    boolean isPaused = false;
    public static boolean isPlayed = false;

    NotificationBuilder notificationBuilder;


//    private MusicController musicController;
//    public void setMusicController() {
//        this.musicController = Constant.getController();
//    }

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
        Constant.getController().show();
//        Constant.getTheMediaPlayer().setPlayBtn(true);
//        musicController.show(0);
        notificationBuilder = new NotificationBuilder(getApplicationContext(), songs.get(songPos));
        if (isPlayed)
            notificationBuilder.builder(ACTION_PAUSE);

//        notificationBuilder.buildNotification(notificationBuilder.generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
//            buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
        if (isPaused)
            notificationBuilder.builder(ACTION_PLAY);

//        notificationBuilder.buildNotification(notificationBuilder.generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
//            buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
    }

//    private Notification.Action generateAction(int icon, String title, String intentAction) {
//        Intent intent = new Intent(getApplicationContext(), MusicService.class);
//        intent.setAction(intentAction);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//        return new Notification.Action.Builder(icon, title, pendingIntent).build();
//    }

    private void handleIntent(Intent intent) {
        if(intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            musicService.go();
            Constant.getController().show();
//            musicController.show(0);
//            Constant.getTheMediaPlayer().setPlayBtn(true);
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            musicService.pausePlayer();
            Constant.getController().show();
//            musicController.show(0);
//            Constant.getTheMediaPlayer().setPlayBtn(false);
        } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
//            musicController.getTransportControls().fastForward();
        } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
//            musicController.getTransportControls().rewind();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            musicService.playPrev();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            musicService.playNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
//            musicController.getTransportControls().stop();
        }
//        musicController.show(0);
    }

//    private void buildNotification(Notification.Action action) {
//        Notification.MediaStyle style = new Notification.MediaStyle();
//
//        Intent intent = new Intent(getApplicationContext(), MusicService.class);
//        intent.setAction(ACTION_STOP);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(songTitle)
//                .setContentText(songArtist)
//                .setLargeIcon(songs.get(songPos).getAlbumArtBitmapDrawable().getBitmap())
//                .setContentIntent(pendingIntent)
//                .setOngoing(isPlayed)
//                .setStyle(style);
//        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
////        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
//        builder.addAction(action);
////        builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
//        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
//    }

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
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
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
//        Toast.makeText(getApplicationContext(), "path.split(): " + playSong.getFolder()[1] + " +++ " + playSong.getFolder()[2], Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "path.: " + playSong.getFolder(), Toast.LENGTH_LONG).show();


        Constant.getController().setTrackName(songTitle);
        Constant.getController().setArtistName(songArtist);
        if (songs.get(songPos).getAlbumArtBitmapDrawable() != null)
            Constant.getController().setCoverArt(songs.get(songPos).getAlbumArtBitmapDrawable().getBitmap());

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
        isPaused = true;
        isPlayed = false;
        notificationBuilder.builder(ACTION_PLAY);

//        notificationBuilder.buildNotification(notificationBuilder.generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));

//        buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
    }
    public void seek(int pos) {
        player.seekTo(pos);
    }
    public void go() {
        player.start();
        isPlayed = true;
        isPaused = false;
        notificationBuilder.builder(ACTION_PAUSE);
//        notificationBuilder.buildNotification(notificationBuilder.generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));

//        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
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