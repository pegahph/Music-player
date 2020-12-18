package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    ArrayList<Artist> artistsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        artistsList = ListMaker.loadArtists();
        RecyclerView artistRV = (RecyclerView) view.findViewById(R.id.artists_recycler_view);
        ArtistAdapter artistAdapter = new ArtistAdapter(artistsList);
        artistRV.setAdapter(artistAdapter);
        artistRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}