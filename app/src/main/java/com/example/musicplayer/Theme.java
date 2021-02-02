package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.widget.PopupMenu;

public class Theme {
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 2;

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.recreate();
//        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.LightTheme);
                break;
            case THEME_LIGHT:
                activity.setTheme(R.style.LightTheme);
                break;
            case THEME_DARK:
                activity.setTheme(R.style.DarkTheme);
                break;
        }
    }
}
