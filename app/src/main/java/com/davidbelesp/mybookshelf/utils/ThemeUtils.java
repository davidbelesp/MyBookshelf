package com.davidbelesp.mybookshelf.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    public static void setTheme(int theme){
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    public static boolean isDarkMode(){
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

}
