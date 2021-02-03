package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.musicplayer.MusicService.MusicBinder;
import com.google.android.material.tabs.TabLayout;


// The Main Activity.  Wow!!!
public class MainActivity extends AppCompatActivity {
    // we are using this ArrayList to store the Songs.
    private ArrayList<Song> songList;
    // and we are gonna show them in a ListView.
//    private RecyclerView songRV;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    private int lastCurrentPos = 0;
    private int lastDuration = 0;
    private TheMediaPlayer theMediaPlayer;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.READ_PHONE_STATE};
    TabLayout tabs;
    ViewPager viewPager;



    // Overriding onCreate function :)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        ListMaker.musicResolver = getContentResolver();
        ListMaker.resources = getResources();
//      permissions
        if(checkAndRequestPermissions()){
            startApp();
        }

    }

    private void startApp(){
//        tabs
        ThePagerAdapter pagerAdapter = new ThePagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
//        songView = (ListView) findViewById(R.id.songList);
//        songRV = (RecyclerView) findViewById(R.id.song_recycler_view);
        songList = new ArrayList<>();
        getSongList();
        // sort the data
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
        PlaylistMaker.createDatabase(getApplicationContext());
        PlaylistMaker.loadPlaylists();
//        SongAdapter songAdapter = new SongAdapter(songList);
//        songRV.setAdapter(songAdapter);
//        songRV.setLayoutManager(new LinearLayoutManager(this));

    }

    private boolean checkAndRequestPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();
        for (String permission: permissions){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                permissionsNeeded.add(permission);
            }
        }
        if(permissionsNeeded.size()!=0) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), 10);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            HashMap<String,Integer> permissionDenieds = new HashMap<>();
            int deniedCount = 0;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionDenieds.put(permissions[i],grantResults[i]);
                    deniedCount++;
                }
            }
            if(deniedCount == 0){
                startApp();
            } else{
                boolean dialogShowed = false;
                for (Map.Entry<String,Integer> entry :permissionDenieds.entrySet()){
                    String permName = entry.getKey();
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,permName) && !dialogShowed) {
                        new AlertDialog.Builder(this).setTitle("Permission Needed!").setMessage("This app needs to have these permissions to be able to work!")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermissions();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                System.exit(0);
                            }
                        }).create().show();
                        dialogShowed = true;
                    }else if (!dialogShowed){
                        new AlertDialog.Builder(this).setTitle("Permission Needed!").setMessage("You have denied some permissions. Allow all permissions at [Setting] > [Permissions]")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.fromParts("package",getPackageName(),null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        System.exit(0);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                System.exit(0);
                            }
                        }).create().show();
                        dialogShowed = true;
                    }
                }
            }
        }

    }


    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;

            musicService = binder.getService();
            Constant.setMusicService(musicService);
            musicService.setMusicService();

//            musicService.setList(songList);
            musicBound = true;
            Constant.setMusicBound(musicBound);

            if (theMediaPlayer == null) {
                Constant.setTheMediaPlayer(new TheMediaPlayer(musicService, getApplicationContext()));
                theMediaPlayer = Constant.getTheMediaPlayer();
            }
            theMediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
            controller = Constant.getController();
//            setController();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
            Constant.setMusicBound(musicBound);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            playIntent.setAction(MusicService.ACTION_PLAY);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            Constant.setMusicConnection(musicConnection);
            startService(playIntent);
        }
        if (controller != null)
            controller.changeKillMe(false);
    }
    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
        controller.changeKillMe(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            if (theMediaPlayer == null) {
                Constant.setTheMediaPlayer(new TheMediaPlayer(musicService, getApplicationContext()));
                theMediaPlayer = Constant.getTheMediaPlayer();
            }
            theMediaPlayer.setControllerLayout((FrameLayout) findViewById(R.id.top_half), controller);
            controller = Constant.getController();
//            theMediaPlayer.setController(MainActivity.this, findViewById(R.id.view_pager), false, controller);
            paused = false;
        }
    }
    @Override
    protected void onStop() {
//        Constant.getController().hide();
        super.onStop();
    }

    // it's just yee.t the menu to top of screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.the_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View menuItemView = findViewById(R.id.Theme);
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicService.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
            case R.id.Theme:
                showPopup(menuItemView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.theme_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.themeMood:
                        changeposition();
                        Theme.changeToTheme(MainActivity.this, ThemeApplication.currentPosition);
                        musicService.setMenu(true);
                        return true;
                    case R.id.removeBackground:
                        viewPager.setBackground(null);
                        viewPager.setBackgroundColor(R.attr.backgroundColor);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void changeposition(){
        if(ThemeApplication.currentPosition == 1){
            ThemeApplication.currentPosition = 0;
        }
        else {
            ThemeApplication.currentPosition =1;
        }
    }


    public void getSongList() {
        songList = ListMaker.loadTracks();
    }

    private Bitmap CropBitmap(Bitmap bm) {
        return Bitmap.createBitmap(bm, bm.getWidth()/4, 0, bm.getWidth()/4, bm.getHeight());
    }

    private Bitmap darkenBitMap(Bitmap bm) {
        Bitmap output = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return Bitmap.createScaledBitmap(output, 720, 1280, true);
        //return Bitmap.createBitmap(output, 0,0,720,1280);
    }

    private Bitmap getArtistImage(String albumid) {
        Bitmap artwork = null;
        try {
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.valueOf(albumid));
            ContentResolver res = this.getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return artwork;
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
//        musicService.setMusicController();
        musicService.setList(songList);
        musicService.playSong();
        if (playbackPaused) {
//            setController();
            playbackPaused = false;
        }
//        controller.show(0);
    }

    public void artistSelected(View view) {
        Intent songSelectorIntent = new Intent(MainActivity.this, SongSelectorActivity.class);

        long albumId = 0;
        String name = "";

        if (view.getTag() instanceof Artist)
        {
            Artist selectedArtist = (Artist) view.getTag();
            name = selectedArtist.getArtistName();
            albumId = selectedArtist.getAlbumId();
            songSelectorIntent.putExtra("tab", "Artist");
            songSelectorIntent.putExtra("selectedArtistName", name);
            songSelectorIntent.putExtra("selectedArtistAlbumId", albumId);

            startActivity(songSelectorIntent);
        }
        else
            Toast.makeText(this, "playlist clicked", Toast.LENGTH_SHORT).show();;
    }

    public void folderSelected(View view) {
        Intent songSelectorIntent = new Intent(MainActivity.this, SongSelectorActivity.class);

        long albumId = 0;
        String name = "";
        Folder selectedFolder = (Folder) view.getTag();
        name = selectedFolder.getFolderName();
        albumId = selectedFolder.getAlbumId();
        songSelectorIntent.putExtra("tab", "Folder");
        songSelectorIntent.putExtra("selectedFolderName", name);
        songSelectorIntent.putExtra("selectedFolderAlbumId", albumId);

        startActivity(songSelectorIntent);

    }

    public void playlistSelected(View view) {
        Intent songSelectorIntent = new Intent(MainActivity.this, SongSelectorActivity.class);

        long albumId = 0;
        String name = "";
        ArrayList<String> keys = PlaylistMaker.keys;
        int position = (int) view.getTag();

        name = keys.get(position);
//        albumId = selectedFolder.getAlbumId();
        songSelectorIntent.putExtra("tab", "Playlist");
        songSelectorIntent.putExtra("selectedPlaylistName", name);
//        songSelectorIntent.putExtra("selectedPlaylistAlbumId", albumId);

        startActivity(songSelectorIntent);

    }

    @Override
    protected void onDestroy() {
        PlaylistMaker.savePlaylists();
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }


    public void controllerClicked(View view) {
        Intent intent = new Intent(MainActivity.this , SongPlayerPage.class);
        startActivity(intent);
    }

}