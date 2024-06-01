package com.davidbelesp.mybookshelf.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.controllers.ConfigManager;
import com.davidbelesp.mybookshelf.database.DatabaseManager;
import com.davidbelesp.mybookshelf.locale.LocaleHelper;
import com.davidbelesp.mybookshelf.utils.ThemeUtils;

public class MainActivity extends AppCompatActivity {

    private Button btnBookshelf;
    private Toolbar toolbar;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(ConfigManager.loadPreference(this,"theme")
                .equals("blue")
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadElements();

        SQLiteDatabase database = new DatabaseManager(this).getWritableDatabase();
        if(database==null){
            Toast.makeText(this, "ERROR CREATING DATABASE", Toast.LENGTH_SHORT).show();
        }

        setEvents();

        LocaleHelper.setLocale(this, LocaleHelper.getCurrentLocale(this));

    }

    private void loadElements() {
        this.toolbar = findViewById(R.id.mainToolbar);
        this.mainLayout = findViewById(R.id.mainConstraint);

        loadBgImage();
    }

    private void loadBgImage(){
        Glide.with(this)
        .asBitmap().load(R.drawable.bg).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                mainLayout.setBackground(new BitmapDrawable(getResources(), resource));
            }
            @Override public void onLoadCleared(@Nullable Drawable placeholder) {}
        });
    }

    private void setEvents() {
        btnBookshelf = findViewById(R.id.libraryButton);
        btnBookshelf.setOnClickListener(e -> changeView());
        toolbar.setOnMenuItemClickListener(item -> openConfig());
    }

    public void changeView(){
        startActivity(new Intent(MainActivity.this, BookshelfActivity.class));
    }

    public boolean openConfig(){
        startActivity(new Intent(MainActivity.this, ConfigActivity.class));
        return true;
    }

}