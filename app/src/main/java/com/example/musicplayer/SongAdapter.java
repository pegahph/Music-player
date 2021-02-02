package com.example.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {


    private ArrayList<Song> songs;

    public SongAdapter(ArrayList<Song> theSongs) {
        this.songs = theSongs;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumArt;
        private TextView songTitle;
        private TextView songArtist;
        private TextView songDuration;
        private View thisView;

        public SongViewHolder(View itemView) {
            super(itemView);

//            albumArt = (ImageView) itemView.findViewById(R.id.album_art);
            songTitle = (TextView) itemView.findViewById(R.id.song_title);
            songArtist = (TextView) itemView.findViewById(R.id.song_artist);
            songDuration = (TextView) itemView.findViewById(R.id.duration);
            thisView = itemView;
        }

        public void bindView(int position) {
            Song currentSong = songs.get(position);
            songTitle.setText(currentSong.getTitle());
            songArtist.setText(currentSong.getArtist());
            songDuration.setText(currentSong.getDurationInTimeFormat());
//            Drawable albumArtImage = currentSong.getAlbumArtBitmapDrawable();
//            if (albumArtImage != null)
//                albumArt.setImageDrawable(albumArtImage);
//            else
//                albumArt.setImageResource(R.drawable.icon2);

            thisView.setTag(position);
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View songView = inflater.inflate(R.layout.song, parent, false);

        return new SongViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}