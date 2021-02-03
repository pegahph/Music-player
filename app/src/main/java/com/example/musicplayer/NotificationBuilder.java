package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class NotificationBuilder {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    Context context;
    Song song;

    public NotificationBuilder(Context context, Song song) {
        this.context = context;
        this.song = song;
    }

    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    private void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();

        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(song.getTitle())
                .setContentText(song.getArtist())
                .setLargeIcon(getLargeIcon())
                .setContentIntent(pendingIntent)
                .setOngoing(MusicService.isPlayed)
                .setStyle(style);
        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
//        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
//        builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void builder(String action) {
        if (action.equals(ACTION_PLAY))
            buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
        else if (action.equals(ACTION_PAUSE))
            buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
    }

    private Bitmap getLargeIcon() {
        BitmapDrawable image = song.getAlbumArtBitmapDrawable();
        Bitmap icon;
        if (image == null) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.background5);
        }
        else {
            icon = image.getBitmap();
        }
        return icon;
    }

}
