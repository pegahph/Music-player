package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    ArrayList<Artist> artistsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        artistsList = ListMaker.loadArtists();
//        TextView textView = (TextView) view.findViewById(R.id.artists_description);
//        textView.setText(String.format("%d and %d", artistsList.get(1), artistsList.get(2)));
//        RecyclerView artistsRV = (RecyclerView) view.findViewById(R.id.artists_recycler_view);
//        songRV.setHasFixedSize(true);
//        SongAdapter artistAdapter = new SongAdapter(artistsList);
//        songAdapter.setHasStableIds(true);
//        songRV.setAdapter(songAdapter);
//        songRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}