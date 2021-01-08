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
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "playlistsDB";
    private static final String TABLE_NAME = "playlists";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "PlaylistTitle";
    private static final String[] COLUMNS = {KEY_ID, KEY_NAME};

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + KEY_NAME + " TEXT PRIMARY KEY )";
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

    public ArrayList<String> allPlaylistTitles() {
        ArrayList<String> playlistTitles = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                playlistTitles.add(title);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return playlistTitles;
    }

    public void addPlaylistTitle(String playlistTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, playlistTitle);
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updatePlaylistTitle() {

    }
}
