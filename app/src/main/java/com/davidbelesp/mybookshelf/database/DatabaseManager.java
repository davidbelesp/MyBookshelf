package com.davidbelesp.mybookshelf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.concurrent.ConcurrentLinkedDeque;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "books.db";
    public static final String TABLE_BOOKS = "t_books";


    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_BOOKS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "type TEXT," +
                "description TEXT," +
                "score INT," +
                "chapters INT," +
                "volumes INT," +
                "image TEXT," +
                "status TEXT," +
                "nsfw INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_BOOKS);
        onCreate(sqLiteDatabase);
    }

}
