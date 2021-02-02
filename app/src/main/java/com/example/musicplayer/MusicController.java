package com.example.musicplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Formatter;

import io.alterac.blurkit.BlurLayout;

public class MusicController extends FrameLayout {
    private Context context;
    private TheMediaPlayer mediaPlayer;
    private ImageButton prevBtn, playBtn, nextBtn;
    private ImageButton shuffleBtn, repeatBtn, favoriteBtn, addToPlayList;
    private ImageView coverArt, backCover;
    private TextView trackName, artistName, endTimeTextView;
    private boolean isSongPlayerActivity = false;
    private BlurLayout blurLayout;
    private View grayView;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    boolean killMe = false;
    final Handler updateHandler = new Handler();
    private Song lastSong;
    // search stuff
    private Space searchBarPlaceholder;
    private EditText theSearchBar;
    private ImageView searchBtn, shareBtn, deleteBtn;
    private InputMethodManager imm;
    private static ArrayList<Song> songTitleCompared = new ArrayList<>();
    private static ArrayList<Song> songArtistCompared = new ArrayList<>();

    public MusicController(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void  getProgressbar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public void getButtons(ImageButton aPrevBtn, ImageButton aPlayBtn, ImageButton aNextBtn) {
        prevBtn = aPrevBtn;
        playBtn = aPlayBtn;
        nextBtn = aNextBtn;
        isSongPlayerActivity = false;

        if (playBtn != null) {
            playBtn.requestFocus();
            playBtn.setOnClickListener(playListener);
        }
    }

    public void getDetails(ImageView aCoverArt, TextView aTrackName, TextView aArtistName) {
        coverArt = aCoverArt;
        trackName = aTrackName;
        artistName = aArtistName;
    }
    public void getEndTimeTextView(ImageView aBackCover, TextView aEndTime, BlurLayout blurLayout,
                                   View grayView, ImageButton shuffleBtn, ImageButton repeatBtn, SeekBar seekBar, ImageButton favoriteBtn, ImageButton addToPlaylist) {
        isSongPlayerActivity = true;
        this.endTimeTextView = aEndTime;
        this.backCover = aBackCover;
        this.blurLayout = blurLayout;
        this.grayView = grayView;
        this.shuffleBtn = shuffleBtn;
        this.repeatBtn = repeatBtn;
        this.seekBar = seekBar;
        this.favoriteBtn = favoriteBtn;
        this.addToPlayList = addToPlaylist;

        if (this.shuffleBtn != null) {
            shuffleBtn.requestFocus();
            shuffleBtn.setOnClickListener(shuffleListener);
            updateShuffle();
        }

        if (this.repeatBtn != null) {
            repeatBtn.requestFocus();
            repeatBtn.setOnClickListener(repeatListener);
//            updateShuffle();
        }

        if (this.favoriteBtn != null) {
            favoriteBtn.requestFocus();
            favoriteBtn.setOnClickListener(favoriteListener);
            updateFavorite();
        }

        if (this.addToPlayList != null) {
            addToPlaylist.requestFocus();
            addToPlaylist.setOnClickListener(addToPlaylistListener);
        }
    }

    public void getSearchStuff(Space searchBarPlaceholder, EditText theSearchBar, ImageView searchBtn, ImageView shareBtn, ImageView deleteBtn, InputMethodManager imm) {
        this.searchBarPlaceholder = searchBarPlaceholder;
        this.theSearchBar = theSearchBar;
        this.searchBtn = searchBtn;
        this.shareBtn = shareBtn;
        this.deleteBtn = deleteBtn;
        this.imm = imm;


        this.searchBtn.setOnClickListener(doSearchBtnListener);
    }
    public void setMediaPlayer(TheMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        updatePausePlay();
    }

    public void show() {
        updatePausePlay();
//        if (isSongPlayerActivity) {
//            updateShuffle();
//            seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            Toast.makeText(context, stringForTime(mediaPlayer.getCurrentPosition()), Toast.LENGTH_SHORT).show();
//        }
    }

    private void updatePausePlay() {
        if (mediaPlayer.isPlaying()) {
            playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            playBtn.setContentDescription("Pause");
        } else {
            playBtn.setImageResource(R.drawable.ic_round_play_circle_outline_24);
            playBtn.setContentDescription("Play");
        }
    }

    private final View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };
    private final View.OnClickListener shuffleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doShuffle();
        }
    };
    private final View.OnClickListener repeatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeRepeat();
        }
    };
    private final View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            favorite();
        }
    };
    private final View.OnClickListener addToPlaylistListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "add to playlist", Toast.LENGTH_SHORT).show();
        }
    };
    private final View.OnClickListener doSearchBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            doSearchStuff();
        }
    };
    private final View.OnClickListener undoSearchBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            undoSearchStuff();
        }
    };

    private void doSearchStuff() {
        changeVisibilityOfTheseGuys(GONE);
        this.searchBarPlaceholder.setVisibility(GONE);
        this.theSearchBar.setVisibility(VISIBLE);
        this.theSearchBar.setText("");
        this.imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        this.theSearchBar.addTextChangedListener(textWatcher);
        this.shareBtn.setVisibility(GONE);
        this.deleteBtn.setVisibility(GONE);
        changeVisibilityOfTheseGuys(VISIBLE);
        searchBtn.setOnClickListener(undoSearchBtnListener);
    }

    private void undoSearchStuff() {
        changeVisibilityOfTheseGuys(GONE);
        this.searchBarPlaceholder.setVisibility(VISIBLE);
        this.theSearchBar.setVisibility(GONE);
        this.theSearchBar.removeTextChangedListener(textWatcher);
            // TODO: keyboard nemire paeen!!! bayad bere paeen.
//        Keyboard.hide();
        this.imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        this.shareBtn.setVisibility(VISIBLE);
        this.deleteBtn.setVisibility(VISIBLE);
        changeVisibilityOfTheseGuys(VISIBLE);
        searchBtn.setOnClickListener(doSearchBtnListener);

    }

    private void changeRepeat() {
        mediaPlayer.changeRepeatStatus();
    }

    private void favorite() {
        Song currentSong = mediaPlayer.getCurrentSong();
        if (currentSong.isFavorite()) {
            PlaylistMaker.removeFromFavorites(currentSong);
        }
        else {
            PlaylistMaker.newFavoriteSong(currentSong);
        }
        currentSong.changeFavorite();
        updateFavorite();
    }

    private void doShuffle() {
        mediaPlayer.setShuffle();
        updateShuffle();
    }

    private void updateShuffle() {
        if (mediaPlayer.getShuffle()) {
            shuffleBtn.setImageResource(R.drawable.shuffle_on);
        }
        else {
            shuffleBtn.setImageResource(R.drawable.shuffle_off);
        }
    }

    public void updateFavorite() {
        if (isSongPlayerActivity) {
            Song currentSong = mediaPlayer.getCurrentSong();
            if (currentSong.isFavorite()) {
                favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
            else {
                favoriteBtn.setImageResource(R.drawable.ic_twotone_favorite_24);
            }
        }
    }

    private void doPauseResume() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
        updatePausePlay();
    }

    public void setController() {
        lastSong = PlaylistMaker.getLastSong();
        if (lastSong != null) {
            this.trackName.setText(lastSong.getTitle());
            this.artistName.setText(lastSong.getArtist());
            changeCover(this.coverArt, lastSong.getAlbumArtBitmapDrawable());
            if (isSongPlayerActivity)
            {
//                changeCover(this.backCover, lastSong.getAlbumArtBitmapDrawable());
                changeBackCover(lastSong.getAlbumArtBitmapDrawable());
                this.endTimeTextView.setText(stringForTime(mediaPlayer.getDuration()));
            }
        }
        else if (Constant.getController() != null) {
            MusicController lastController = Constant.getController();
            this.trackName.setText(lastController.trackName.getText());
            this.artistName.setText(lastController.artistName.getText());
            changeCover(this.coverArt, lastController.coverArt.getDrawable());
            if (isSongPlayerActivity)
            {
//                changeCover(this.backCover, lastController.coverArt.getDrawable());
                changeBackCover(lastController.coverArt.getDrawable());
                this.endTimeTextView.setText(stringForTime(mediaPlayer.getDuration()));
            }
        }
        updatePausePlay();
    }

    public void setCoverArt(Drawable aaCoverArt) {
        changeCover(this.coverArt, aaCoverArt);
    }

    public void setBackCoverArt(Drawable aCoverArt) {
        if (isSongPlayerActivity)
        {
            changeBackCover(aCoverArt);

//            this.blurLayout.startBlur();
//            this.blurLayout
//            setController();
        }
    }
    public void setDuration(int duration) {
        if (isSongPlayerActivity)
        {
            this.endTimeTextView.setText(stringForTime(duration));
            this.seekBar.setMax(duration);
        }
        else {
            this.progressBar.setMax(duration);
            updateProgress();
        }
    }

    public void updateProgress(){
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!killMe && !isSongPlayerActivity)
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateHandler.postDelayed(this, 250);
            }
        };

        updateHandler.postDelayed(timerRunnable, 250);
    }

    public void setTrackName(String trackName) {
        this.trackName.setText(trackName);
    }

    public void setArtistName(String artistName) {
        this.artistName.setText(artistName);
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {

        if (nextBtn != null) {
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setOnClickListener(next);
            nextBtn.setEnabled(true);
        }
        if (prevBtn != null) {
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setOnClickListener(prev);
            prevBtn.setEnabled(true);
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        if (hours > 0) {
            return new Formatter().format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return new Formatter().format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void changeCover(ImageView coverArtImageView, Drawable aCoverArt) {
        if (aCoverArt != null)
            coverArtImageView.setImageDrawable(aCoverArt);
        else if (isSongPlayerActivity)
            coverArtImageView.setImageResource(R.drawable.background6);
        else
            coverArtImageView.setImageResource(R.drawable.background5);

    }

    private void changeVisibilityOfTheseGuys(int visibility) {
        this.blurLayout.setVisibility(visibility);
        this.grayView.setVisibility(visibility);
    }

    private void changeBackCover(Drawable aCoverArt) {
        changeVisibilityOfTheseGuys(INVISIBLE);

        changeCover(this.backCover, aCoverArt);

        this.blurLayout.invalidate();

        changeVisibilityOfTheseGuys(VISIBLE);
    }

    // a thing that gets a text and searches in all songs
    private void theSearchThing(String text) {
        if (text.equals(""))
            return;
//        Toast.makeText(context, "songTitleCompared size = " + songTitleCompared.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "songArtistCompared size = " + songArtistCompared.size(), Toast.LENGTH_SHORT).show();        songTitleCompared.clear();
        songArtistCompared.clear();
        songTitleCompared.clear();
//        Toast.makeText(context, "songTitleCompared size = " + songTitleCompared.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "songArtistCompared size = " + songArtistCompared.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        for (Song song : ListMaker.loadTracks()) {
            String songTitle = song.getTitle();
            String songArtist = song.getArtist();
//            songTitle = "az panjere bebin birono";
//            text = "ebi";
//            songArtist = "ebiram";
            if (songTitle.toLowerCase().contains(text.toLowerCase())) {
                songTitleCompared.add(song);
            }
            if (songArtist.toLowerCase().contains(text.toLowerCase())) {
                songArtistCompared.add(song);
            }
        }
        Toast.makeText(context, "songTitleCompared size = " + songTitleCompared.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "songArtistCompared size = " + songArtistCompared.size(), Toast.LENGTH_SHORT).show();
    }

    TextWatcher textWatcher = new TextWatcher() {
        String text;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            text = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(text))
                theSearchThing(s.toString());
        }
    };
}