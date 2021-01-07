package com.example.musicplayer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistFragment extends Fragment {

    HashMap<String, ArrayList<Song>> playlists;
    Object[] keys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlists = Database.getPlaylists();
        sortByName();
        keys = playlists.keySet().toArray();
        RecyclerView playlistRV = (RecyclerView) view.findViewById(R.id.playlistRecyclerView);
        RecyclerView.Adapter<PlaylistViewHolder> adapter = new RecyclerView.Adapter<PlaylistViewHolder>() {
            @NonNull
            @Override
            public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);

                View artistView = inflater.inflate(R.layout.playlist, parent, false);
                return new PlaylistViewHolder(artistView);
            }

            @Override
            public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
                holder.bindView(position);
            }

            @Override
            public int getItemCount() {
                return playlists.size();
            }
        };
        playlistRV.setAdapter(adapter);
        playlistRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void sortByName() {
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private TextView playlistName;
        private ImageView playlistArt;
        private View thisView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistName = (TextView) itemView.findViewById(R.id.artist_name);
            playlistArt = (ImageView) itemView.findViewById(R.id.albums_art);
            thisView = itemView;
        }

        public void bindView(int position) {
            playlistName.setText(keys[position].toString());
            thisView.setTag(position);
        }
    }
}