package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;

public class Theme {
    private static int sTheme;
    public final static int THEME_LIGHT = 0;
    public final static int THEME_DARK = 1;

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.recreate();
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_LIGHT:
                activity.setTheme(R.style.LightTheme);
                break;
            case THEME_DARK:
                activity.setTheme(R.style.DarkTheme);
                break;
        }
    }
}
