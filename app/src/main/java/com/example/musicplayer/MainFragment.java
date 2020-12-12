package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    ArrayList<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        songList = ListMaker.loadTracks(getActivity().getContentResolver(), getActivity().getResources());
        RecyclerView songRV = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        SongAdapter songAdapter = new SongAdapter(songList);
        songRV.setAdapter(songAdapter);
        songRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}