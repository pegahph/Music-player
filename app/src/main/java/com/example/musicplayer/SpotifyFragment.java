package com.example.musicplayer;

import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.spotify.android.appremote.api.ConnectionParams;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;

//import com.spotify.protocol.client.Subscription;
//import com.spotify.protocol.types.PlayerState;
//import com.spotify.protocol.types.Track;
import androidx.fragment.app.Fragment;

public class SpotifyFragment extends Fragment {
//    private static final String CLIENT_ID = "your_client_id";
//    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";
//    private SpotifyAppRemote mSpotifyAppRemote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spotify, container, false);
        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//        SpotifyAppRemote.connect(this.getContext(), connectionParams, new Connector.ConnectionListener() {
//
//            @Override
//            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                mSpotifyAppRemote = spotifyAppRemote;
//                Log.d("MainActivity", "Connected! Yay!");
//
//                // Now you can start interacting with App Remote
//                connected();
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                Log.e("MainActivity", throwable.getMessage(), throwable);
//
//                // Something went wrong when attempting to connect! Handle errors here
//            }
//        });
//    }
//
//    private void connected() {
//        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//    }
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
}
