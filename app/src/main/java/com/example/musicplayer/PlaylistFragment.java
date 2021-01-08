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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistFragment extends Fragment {

    HashMap<String, ArrayList<Song>> playlists;
    ArrayList<String> keys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
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

        FloatingActionButton addPlaylist = view.findViewById(R.id.add_playlist);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistMaker.newPlaylist("playlist #" + (keys.size() + 1), new ArrayList<Song>());
                playlists = PlaylistMaker.getPlaylists();
                keys = PlaylistMaker.keys;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        playlists = PlaylistMaker.getPlaylists();
        keys = PlaylistMaker.keys;
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
            playlistName.setText(keys.get(position));
            thisView.setTag(position);
        }
    }
}