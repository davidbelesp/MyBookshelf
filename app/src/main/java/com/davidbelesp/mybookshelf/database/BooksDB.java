package com.davidbelesp.mybookshelf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.BookStatus;
import com.davidbelesp.mybookshelf.models.BookType;
import com.davidbelesp.mybookshelf.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class BooksDB extends DatabaseManager {

    private static BooksDB instance;
    private Context context;

    private BooksDB(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public static BooksDB getInstance(Context context){
        if(instance == null){
            instance = new BooksDB(context);
        }
        return instance;
    }

    public long addBook(Book book){
        long id = -1;

        try {
            DatabaseManager databaseManager = getInstance(context);
            SQLiteDatabase database = databaseManager.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("title", book.getTitle());
            values.put("type", book.getType());
            values.put("description", book.getDescription());
            values.put("score", book.getScore());
            values.put("chapters", book.getChapters());
            values.put("volumes", book.getVolumes());
            values.put("image", book.getImage());
            values.put("status", book.getStatus());
            values.put("nsfw", book.getNsfw());

            return database.insert("t_books", null, values);
        }
        catch (Exception ex){
            Log.e("ERROR DB", ex.toString());

        }
        return id;
    }

    public Book getBookByID(Long id){
        //TODO METHOD GETBOOK BY ID (ERROR)

        SQLiteDatabase database = getInstance(context).getWritableDatabase();
        Book currentBook = null;
        return null;
    }

    public ArrayList<Book> getAllBooks(){

        SQLiteDatabase database = getInstance(context).getWritableDatabase();

        ArrayList<Book> books = new ArrayList<>();

        Cursor bookCursor = database.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        if(bookCursor.moveToFirst()) {
            do{
                Book.BookBuilder bookBuilder = new Book.BookBuilder()
                        .id(bookCursor.getLong(0))
                        .title(bookCursor.getString(1))
                        .type(BookType.valueOf(bookCursor.getString(2)))
                        .description(bookCursor.getString(3))
                        .score(bookCursor.getInt(4))
                        .chapters(bookCursor.getInt(5))
                        .volumes(bookCursor.getInt(6));

                String image = bookCursor.getString(7);
                if(image==null)bookBuilder.image(null);
                else if(!image.equals("null"))bookBuilder.image(image);
                else bookBuilder.image(null);

                bookBuilder
                        .status(BookStatus.valueOf(bookCursor.getString(8)));

                int nsfwDB =  bookCursor.getInt(9);
                if(nsfwDB!=0) bookBuilder.nsfw(true);
                else bookBuilder.nsfw(false);

                books.add(bookBuilder.build());
            }while(bookCursor.moveToNext());

            bookCursor.close();
        }

        return books;
    }

    public boolean removeBookByID(Long ID){
        SQLiteDatabase database = getInstance(context).getWritableDatabase();
        return database.delete(
                TABLE_BOOKS,
                "ID =?",
                new String[]{ID.toString()})>0;

    }

    public void editBookByID(Long ID, Book book){
        SQLiteDatabase database = getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", book.getTitle());
        values.put("type", book.getType());
        values.put("description", book.getDescription());
        values.put("score", book.getScore());
        values.put("chapters", book.getChapters());
        values.put("volumes", book.getVolumes());
        values.put("image", book.getImage());
        values.put("status", book.getStatus());
        values.put("nsfw", book.getNsfw());

        database.update(TABLE_BOOKS, values, "id = ?", new String[]{ID.toString()});
    }

    public void removeAllBooks(){
        SQLiteDatabase database = getInstance(context).getWritableDatabase();

        database.execSQL("delete from " + TABLE_BOOKS);
    }
}
