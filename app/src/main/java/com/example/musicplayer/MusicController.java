package com.example.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

public class MusicController extends MediaController {
    public MusicController(Context c) {
        super(c);
    }

    public void hide() {}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            super.hide();
            Context c = getContext();
            ((Activity) c).finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
