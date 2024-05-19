package com.davidbelesp.mybookshelf.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.BookStatus;
import com.davidbelesp.mybookshelf.models.BookType;
import com.davidbelesp.mybookshelf.utils.Constants;
import com.davidbelesp.mybookshelf.utils.ImageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditBookActivity extends AppCompatActivity {

    private Book selectedBook;

    //VIEW ELEMENTS
    private Toolbar toolbar;
    private Spinner spinnerStatus;
    private Spinner spinnerType;
    private Spinner spinnerScore;
    private FloatingActionButton confirmBookChanges;
    private FloatingActionButton deleteBook;
    private EditText titleField ;
    private EditText chaptersField;
    private EditText volumesField;
    private EditText descriptionField;
    private Switch nsfwField;
    private ImageView bookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        //LOADING BOOK THAT ARRIVES INTO SELECTED BOOK
        if(getIntent().hasExtra("book")){
            selectedBook = (Book) getIntent().getSerializableExtra("book");
            Log.i("TAGBOOK", selectedBook.toString());
        }

        getElements();

        setButtonEvents();

        setupSpinners();

        setupBookInActivity(selectedBook);

    }

    private void getElements() {
        titleField = findViewById(R.id.textFieldEditTitle);
        chaptersField = findViewById(R.id.numberFieldEditChapters);
        volumesField = findViewById(R.id.numberFieldEditVolumes);
        descriptionField = findViewById(R.id.textFieldEditDescription);
        nsfwField = findViewById(R.id.switchEditNSFW);
        bookImage = findViewById(R.id.editBookImage);

        spinnerStatus = findViewById(R.id.spinnerEditStatus);
        spinnerScore = findViewById(R.id.spinnerEditScore);
        spinnerType = findViewById(R.id.spinnerEditType);

        toolbar = findViewById(R.id.toolbarEditBook);
        confirmBookChanges = findViewById(R.id.btnConfirmBookChanges);
        deleteBook = findViewById(R.id.btnDeleteBook);
    }

    private void setupBookInActivity(Book book) {

        titleField.setText(book.getTitle(), TextView.BufferType.EDITABLE);
        chaptersField.setText(book.getChapters().toString(), TextView.BufferType.EDITABLE);

        volumesField.setText(book.getVolumes().toString(), TextView.BufferType.EDITABLE);
        descriptionField.setText(book.getDescription(), TextView.BufferType.EDITABLE);
        nsfwField.setChecked(book.getNsfw());
        try {bookImage.setImageURI(ImageUtils.loadImage(this, book.getImage()));
        } catch (IOException ex) {throw new RuntimeException(ex);}

        bookImage.setTag(book.getImage());

    }

    private void setupSpinners() {

        List<String> statuses = Arrays.asList(BookStatus.values())
                .stream()
                .map(BookStatus::getLabel)
                .collect(Collectors.toList());

        List<String> types = Arrays.asList(BookType.values())
                .stream()
                .map(BookType::getLabel)
                .collect(Collectors.toList());

        // SCORE ADAPTER
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item_addbook,
                IntStream.rangeClosed(0, 10)
                        .boxed()
                        .map(String::valueOf)
                        .collect(Collectors.toList())
        );
        spinnerScore.setAdapter(scoreAdapter);
        spinnerScore.setSelection(selectedBook.getScore());

        // STATUS ADAPTER
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,R.layout.spinner_item_addbook,
                statuses
        );
        spinnerStatus.setAdapter(statusAdapter);
        spinnerStatus.setSelection(statusAdapter.getPosition(selectedBook.getStatusObject().label));


        // TYPE ADAPTER
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item_addbook,
                types
        );
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setSelection(typeAdapter.getPosition(selectedBook.getTypeObject().label));

    }

    private void setButtonEvents() {

        toolbar.setNavigationOnClickListener(e -> closeActivity());

        confirmBookChanges.setOnClickListener(e -> {
            confirmBookChanges();
            closeActivity();
        });

        deleteBook.setOnClickListener(e -> {
            new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the book?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    BooksDB booksDB = BooksDB.getInstance(this);
                    booksDB.removeBookByID(selectedBook.getID());
                    closeActivity();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        });

        bookImage.setOnClickListener(e -> {
            startActivityForResult(
                new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                    Constants.GALLERY_REQ_CODE);
        });
    }

    private void confirmBookChanges(){

        BooksDB database = BooksDB.getInstance(this);

        Long ID = selectedBook.getID();

        String title = null;
        if(!titleField.getText().toString().equals("")) title = titleField.getText().toString();

        BookStatus status = BookStatus.get(spinnerStatus.getSelectedItem().toString());
        //validation

        String description = descriptionField.getText().toString();
        //validation

        Integer chapters = null;
        if(!chaptersField.getText().toString().equals("")) chapters = Integer.valueOf(chaptersField.getText().toString());

        Integer volumes = null;
        if(!volumesField.getText().toString().equals("")) volumes = Integer.valueOf(volumesField.getText().toString());

        String image= String.valueOf(bookImage.getTag());
        //validation

        BookType type = BookType.get(spinnerType.getSelectedItem().toString());
        //validation

        Integer score = Integer.valueOf(spinnerScore.getSelectedItem().toString());
        //validation

        Boolean nsfw = nsfwField.isChecked();

        //CREATING THE BOOK

        Book newBook = new Book.BookBuilder()
                .title(title)
                .status(status)
                .description(description)
                .chapters(chapters)
                .volumes(volumes)
                .image(image)
                .type(type)
                .score(score)
                .nsfw(nsfw)
                .build();

        database.editBookByID(ID,newBook);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, @Nullable Intent data) {
        super.onActivityResult(reqCode, resCode, data);


        //Detects request codes
        if(reqCode==Constants.GALLERY_REQ_CODE && resCode == Activity.RESULT_OK) {
            //LOAD IMAGE FROM GALLERY
            Uri selectedImage = data.getData();

            //GET THE BITMAP OF THE IMAGE
            Bitmap bitmap = null;
            try {bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);}
            catch (IOException e) {
                Log.e("URI:IMAGE", e.toString());}

            //UPLOAD IMAGE TO THE APP
            try {

                String imageName = ImageUtils.saveImage(this, bitmap);
                bookImage.setTag(imageName);
                Uri imageUri = ImageUtils.loadImage(this, imageName);
                bookImage.setImageURI(imageUri);
                selectedBook.setImage(imageName);

            }
            catch (IOException e) {throw new RuntimeException(e);}

        }

    }

    public void closeActivity(){
        setResult(Constants.REFRESH_LIST_CODE);
        finish();
    }
}