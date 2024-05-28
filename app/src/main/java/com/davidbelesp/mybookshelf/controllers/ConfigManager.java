package com.davidbelesp.mybookshelf.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.models.Config;

public class ConfigManager {

    public static Config loadConfig(Context ctx) {

        SharedPreferences preferences = ctx.getSharedPreferences("bookshelf_config", Context.MODE_PRIVATE);

        Config config = new Config.ConfigBuilder()
                .setNSFW(preferences.getBoolean("nsfw", true))
                .setCensor(preferences.getBoolean("censor", true))
                .build();

        return config;
    }

    public static void saveConfig(Context ctx, Config config){

        SharedPreferences preferences = ctx.getSharedPreferences("bookshelf_config", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("nsfw", config.getNSFW());
        editor.putBoolean("censor", config.getCensor());

        editor.commit();
    }

    public static void savePreference(Context ctx, String key, String value){
        SharedPreferences preferences = ctx.getSharedPreferences("bookshelf_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadPreference(Context ctx, String key){
        SharedPreferences preferences = ctx.getSharedPreferences("bookshelf_config", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
