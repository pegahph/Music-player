package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs) {
        this.songs = theSongs;
        this.songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);

        TextView songView = (TextView) songLayout.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);

        Song currentSong = songs.get(position);

        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());

        songLayout.setTag(position);
        return songLayout;

    }
}
