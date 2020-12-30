package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FolderFragment extends Fragment {
    ArrayList<Folder> foldersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        foldersList = ListMaker.loadFolders();
        sortByName();
        RecyclerView folderRV = (RecyclerView) view.findViewById(R.id.folders_recycler_view);
        FolderAdapter folderAdapter = new FolderAdapter(foldersList);
        folderRV.setAdapter(folderAdapter);
        folderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void sortByName() {
        Collections.sort(foldersList, new Comparator<Folder>() {
            @Override
            public int compare(Folder f1, Folder f2) {
                return f1.getFolderName().compareTo(f2.getFolderName());
            }
        });
    }
}