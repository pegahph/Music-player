package com.example.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playlistsDB";
    private static final String TABLE_NAME = "playlists";
    private static final String KEY_NAME = "PlaylistTitle";
    private static final String KEY_SONGS = "PlaylistSongs";
    private static final String[] COLUMNS = {KEY_NAME, KEY_SONGS};
    ArrayList<String> playlistTitles;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + KEY_NAME + " TEXT PRIMARY KEY, " + KEY_SONGS + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(String playlistTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + " = ?", new String[] {playlistTitle});
        db.close();
    }

    public String getThisPlaylistSongs(String playlistName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " " + KEY_NAME +" = ?", // c. selections
                new String[] { playlistName }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        String playlistSongs = "";
        if (cursor != null) {
            cursor.moveToFirst();
            playlistSongs = cursor.getString(cursor.getColumnIndex(KEY_SONGS));
        }
        return playlistSongs;
    }


    public ArrayList<String> getAllPlaylistTitles() {
        if (playlistTitles != null)
            return playlistTitles;
        playlistTitles = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String playlistTitle = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                if (!playlistTitle.equals("theme")) {
                    playlistTitles.add(playlistTitle);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return playlistTitles;
    }

    public void addPlaylist(String playlistTitle, String playlistSongs) {
        if (playlistTitles.contains(playlistTitle))
        {
            updatePlaylist(playlistTitle, playlistSongs);
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, playlistTitle);
        values.put(KEY_SONGS, playlistSongs);
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updatePlaylist(String playlistTitle, String playlistSongs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, playlistTitle);
        values.put(KEY_SONGS, playlistSongs);

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                KEY_NAME +" = ?", // selections
                new String[] { playlistTitle });

        db.close();

        return i;
    }

    public void saveTheme(int position) {
        String thePosition = Integer.toString(position);
        String theme = "theme";

        addPlaylist(theme, thePosition);
    }

    public int getTheme() {
        String theme = "theme";
        try {
            String state = getThisPlaylistSongs(theme);
            return Integer.parseInt(state);
        } catch (Exception ignored) {
            return 0;
        }
    }
}
