package com.example.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {


    private ArrayList<Song> songs;
//    private LayoutInflater songInf;

    public SongAdapter(ArrayList<Song> theSongs) {
        this.songs = theSongs;
//        this.songInf = LayoutInflater.from(c);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumArt;
        private TextView songTitle;
        private TextView songArtist;
        private View thisView;

        public ViewHolder(View itemView) {
            super(itemView);

            albumArt = (ImageView) itemView.findViewById(R.id.album_art);
            songTitle = (TextView) itemView.findViewById(R.id.song_title);
            songArtist = (TextView) itemView.findViewById(R.id.song_artist);
            thisView = itemView;
        }

        public void bindView(int position) {
            Song currentSong = songs.get(position);
            songTitle.setText(currentSong.getTitle());
            songArtist.setText(currentSong.getArtist());
            Drawable albumArtImage = currentSong.getAlbumArtBitmapDrawable();
            if (albumArtImage != null)
                albumArt.setImageDrawable(albumArtImage);
            else
                albumArt.setBackgroundColor(Color.parseColor("#ff5533"));

            thisView.setTag(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View songView = inflater.inflate(R.layout.song, parent, false);

        return new ViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);
//
//        TextView songView = (TextView) songLayout.findViewById(R.id.song_title);
//        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);
//        ImageView albumArt = (ImageView) songLayout.findViewById(R.id.album_art);
//
//        Song currentSong = songs.get(position);
//
//        songView.setText(currentSong.getTitle());
//        artistView.setText(currentSong.getArtist());
//        Drawable albumArtImage = currentSong.getAlbumArt();
//        if (albumArtImage != null)
//            albumArt.setImageDrawable(albumArtImage);
//        else {
//            albumArt.setBackgroundColor(Color.parseColor("#ff3c4a"));
//        }
//
//        songLayout.setTag(position);
//        return songLayout;
//
//    }
}