package com.davidbelesp.mybookshelf.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.database.DatabaseManager;
import com.davidbelesp.mybookshelf.models.Book;

public class MainActivity extends AppCompatActivity {

    Button btnBookshelf;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadElements();

        SQLiteDatabase database = new DatabaseManager(this).getWritableDatabase();
        if(database==null){
            Toast.makeText(this, "ERROR CREATING DATABASE", Toast.LENGTH_SHORT).show();
        }

        setEvents();

    }

    private void loadElements() {
        this.toolbar = findViewById(R.id.mainToolbar);
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