package com.davidbelesp.mybookshelf.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.adapters.BookshelfAdapter;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.BookStatus;
import com.davidbelesp.mybookshelf.models.BookType;
import com.davidbelesp.mybookshelf.utils.Constants;
import com.davidbelesp.mybookshelf.utils.ImageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddBookActivity extends AppCompatActivity {

    private ImageButton goBack;
    private Spinner spinnerStatus;
    private Spinner spinnerType;
    private Spinner spinnerScore;
    private ImageView uploadBookImage;
    private FloatingActionButton confirmBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        loadElements();

        setButtonEvents();

        setSpinners();
    }

    private void loadElements() {
        // Loading elements from the layout
        this.spinnerStatus = findViewById(R.id.spinnerAddStatus);
        this.spinnerScore = findViewById(R.id.spinnerAddScore);
        this.spinnerType = findViewById(R.id.spinnerAddType);
        this.goBack = findViewById(R.id.btnBackAddBook);
        this.confirmBook = findViewById(R.id.btnConfirmAddBook);
        this.uploadBookImage = findViewById(R.id.addBookImage);
    }

    private void setSpinners() {

        //GETTING ALL STATUSES AND TYPES AS STRING LIST
        List<String> statuses = Arrays.asList(BookStatus.values())
                .stream()
                .map(BookStatus::getLabel)
                .collect(Collectors.toList());

        List<String> types = Arrays.asList(BookType.values())
                .stream()
                .map(BookType::getLabel)
                .collect(Collectors.toList());

        //ADAPTERS
        spinnerScore.setAdapter(
                new ArrayAdapter<>(this, R.layout.spinner_item_addbook,
                        IntStream.rangeClosed(0, 10)
                                .boxed()
                                .map(String::valueOf)
                                .collect(Collectors.toList())
                )
        );

        spinnerStatus.setAdapter(
                new ArrayAdapter<>(this,R.layout.spinner_item_addbook,
                        statuses
                )
        );

        spinnerType.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item_addbook,
                    types
                )
        );
    }
    private void setButtonEvents() {

        goBack.setOnClickListener(e -> {
            closeActivity();
        });

        confirmBook.setOnClickListener(e -> {

            //GETTING ALL VIEW ELEMENTS

            EditText titleField = findViewById(R.id.textFieldAddTitle);
            EditText chaptersField = findViewById(R.id.numberFieldAddChapters);
            EditText volumesField = findViewById(R.id.numberFieldAddVolumes);
            EditText descriptionField = findViewById(R.id.textFieldAddDescription);
            Spinner scoreField = findViewById(R.id.spinnerAddScore);
            Spinner statusField = findViewById(R.id.spinnerAddStatus);
            Spinner typeField = findViewById(R.id.spinnerAddType);
            Switch nsfwField = findViewById(R.id.switchAddNSFW);
            ImageView addBookImage = findViewById(R.id.addBookImage);

            // CHECKING IF FIELDS ARE EMPTY

            String title = null;
            if(!titleField.getText().toString().equals("")) title = titleField.getText().toString();

            BookStatus status = BookStatus.get(statusField.getSelectedItem().toString());
            //validation

            String description = descriptionField.getText().toString();
            //validation

            Integer chapters = null;
            if(!chaptersField.getText().toString().equals("")) chapters = Integer.valueOf(chaptersField.getText().toString());

            Integer volumes = null;
            if(!volumesField.getText().toString().equals("")) volumes = Integer.valueOf(volumesField.getText().toString());

            String image= String.valueOf(addBookImage.getTag());
            //validation

            BookType type = BookType.get(typeField.getSelectedItem().toString());
            //validation

            Integer score = Integer.valueOf(scoreField.getSelectedItem().toString());
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

            BooksDB booksDB = BooksDB.getInstance(this);
            booksDB.addBook(newBook);

            closeActivity();
        });

        uploadBookImage.setOnClickListener(e -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), Constants.GALLERY_REQ_CODE);
        });
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
            catch (IOException e) {Log.e("URI:IMAGE", e.toString());}

            //UPLOAD IMAGE TO THE APP
            try {
                String imageName = ImageUtils.saveImage(this, bitmap);
                uploadBookImage.setTag(imageName);
                Uri imageUri = ImageUtils.loadImage(this, imageName);
                uploadBookImage.setImageURI(imageUri);

                Toast.makeText(this, imageName, Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {throw new RuntimeException(e);}
        }
    }

    public void closeActivity(){
        setResult(Constants.REFRESH_LIST_CODE);
        finish();
    }

}