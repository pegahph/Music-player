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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private ArrayList<Artist> artists;

    public ArtistAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View artistView = inflater.inflate(R.layout.artist, parent, false);
        return new ArtistViewHolder(artistView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        private TextView artistName;
        private ImageView artistsArt;
        private View thisView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            artistsArt = (ImageView) itemView.findViewById(R.id.albums_art);
            thisView = itemView;
        }

        public void bindView(int position) {
            Artist currentArtist = artists.get(position);
            artistName.setText(currentArtist.getArtistName());
//            Drawable albumArtImage = currentArtist.getAlbumArtBitmapDrawable();
//            if (albumArtImage != null)
//                artistsArt.setImageDrawable(albumArtImage);
//            else
//                artistsArt.setImageResource(R.drawable.icon2);
            thisView.setTag(position);
        }
    }
}
