package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ThePagerAdapter extends FragmentPagerAdapter {
    int tabsCount;
    MainFragment mainFragment;
    ArtistsFragment artistsFragment;
    FolderFragment folderFragment;
    PlaylistFragment playlistFragment;
    SpotifyFragment spotifyFragment;
    public ThePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabsCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        mainFragment = new MainFragment();
        artistsFragment = new ArtistsFragment();
        folderFragment = new FolderFragment();
        playlistFragment = new PlaylistFragment();
        spotifyFragment = new SpotifyFragment();
        switch (position) {
            case 0:
                return folderFragment;
            case 1:
                return artistsFragment;
            case 2:
                return mainFragment;
            case 3:
                return playlistFragment;
            case 5:
                return spotifyFragment;
            default:
                return artistsFragment;
        }

        // Fragment # 0 - This will show FirstFragment
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}

