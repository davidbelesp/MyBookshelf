package com.davidbelesp.mybookshelf.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.adapters.BookshelfAdapter;
import com.davidbelesp.mybookshelf.controllers.ConfigManager;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.BookStatus;
import com.davidbelesp.mybookshelf.models.BookType;
import com.davidbelesp.mybookshelf.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookshelfActivity extends AppCompatActivity {

    private FloatingActionButton btnAddBook;
    private RecyclerView bookshelfList;
    private BooksDB database;
    private List<Book> books;
    private Toolbar toolbar;
    private BookshelfAdapter adapter;
    SearchView searchView;

    //FILTERS
    private BookStatus statusFilter;
    private BookType typeFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_list);
        loadElements();
        loadDatabaseBooks();

        setToolbar();
        setList(books);
        setButtonEvents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        loadTopBar(menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void loadTopBar(Menu menu) {
        //LEAVE IF NAVIGATION CLOSE
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        getMenuInflater().inflate(R.menu.bookshelf_menu_layout,menu);
        MenuItem searchButton = menu.findItem(R.id.btnSearchBookshelf);
        MenuItem filterStatusButton = menu.findItem(R.id.btnFilterByStatus);
        MenuItem filterTypeButton = menu.findItem(R.id.btnFilterByType);
        MenuItem resetFilters = menu.findItem(R.id.btnResetFilters);
        MenuItem statsButton = menu.findItem(R.id.btnStatsPage);

        filterStatusButton.setOnMenuItemClickListener(item -> {
            showStatusFilterDialog();
            return true;
        });
        filterTypeButton.setOnMenuItemClickListener(item -> {
            showTypeFilterDialog();
            return true;
        });
        resetFilters.setOnMenuItemClickListener(item -> {
            resetFilters();
            return true;
        });
        statsButton.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(BookshelfActivity.this, StatsActivity.class));
            return true;
        });

        this.searchView = (SearchView) searchButton.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filterList(text);
                return false;
            }
        });

    }

    private void loadDatabaseBooks() {
        database = BooksDB.getInstance(this);
        this.books = database.getAllBooks();
    }

    private void loadElements() {
        btnAddBook = findViewById(R.id.btnAddBook);
        bookshelfList = findViewById(R.id.bookshelfList);
        toolbar = findViewById(R.id.bookshelfToolbar);
    }

    private void setToolbar(){
        toolbar.setOverflowIcon(getDrawable(R.drawable.baseline_filter_alt_24));
        setSupportActionBar(toolbar);
    }

    private void setButtonEvents() {
        btnAddBook.setOnClickListener(e -> startActivityForResult(
                new Intent(BookshelfActivity.this, AddBookActivity.class), Constants.REFRESH_LIST_CODE)
        );
    }

    private void setList(List<Book> books) {

        //FILTER IF USER DISABLED NSFW
        books = filterNSFW(books);

        //FILTER IF USER SELECTED TYPE
        books = filterType(books, typeFilter);

        //FILTER IF USER SELECTED STATUS
        books = filterStatus(books, statusFilter);

        //ORDER BY DEFAULT (CONFIGURABLE)
        books = filterConfig(books);

        //CONTINUE WITH SETTING LIST

        bookshelfList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookshelfAdapter(this, books);

        adapter.setOnClickListener((position, book) -> {
            Intent intent = new Intent(
                    BookshelfActivity.this,
                    EditBookActivity.class);

            intent.putExtra("book", book);
            startActivityForResult(intent, Constants.REFRESH_LIST_CODE);
        });
        bookshelfList.setAdapter(adapter);
    }

    private List<Book> filterNSFW(List<Book> books) {
        if(!ConfigManager.loadConfig(getApplicationContext()).getNSFW()){
            return books.stream()
                    .filter(book -> !book.getNsfw())
                    .collect(Collectors.toList());
        } else return books;
    }
    private List<Book> filterConfig(List<Book> books) {
        //TODO READ CONFIG FOR DEFAULT FILTER
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getScore).reversed())
                .collect(Collectors.toList());
    }

    private List<Book> filterStatus(List<Book> books, BookStatus status) {
        if(status == null) return books;
        else return
                books.stream()
                        .filter(book -> book.getStatusObject().equals(status))
                        .collect(Collectors.toList());
    }
    private List<Book> filterType(List<Book> books, BookType type) {
        if(type == null) return books;
        else return
                books.stream()
                        .filter(book -> book.getTypeObject().equals(type))
                        .collect(Collectors.toList());
    }
    private void resetFilters(){
        this.statusFilter = null;
        this.typeFilter = null;
        setList(books);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.REFRESH_LIST_CODE) {
            String query = searchView.getQuery().toString();
            filterList(query);

            books = database.getAllBooks();
            adapter.setFilteredList(books);
        }
    }

    private void filterList(String text){
        List<Book> filteredList = books;

        filteredList = filterNSFW(filteredList);
        filteredList = filterStatus(filteredList, this.statusFilter);
        filteredList = filterType(filteredList, this.typeFilter);

        if(text != null && !text.equals("")) {
            filteredList = filteredList.stream()
                .filter(b ->
                        b.getTitle() != null &&
                        b.getTitle().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        }

        if(filteredList.isEmpty()) Toast.makeText(this, getResources()
                    .getString(R.string.no_data_found_text), Toast.LENGTH_SHORT).show();
        adapter.setFilteredList(filteredList);
    }

    private void showTypeFilterDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.type_filter_dialog);
        dialog.setTitle(getResources().getString(R.string.filter_type_title));

        Spinner filterSpinner = dialog.findViewById(R.id.filterTypeSpinner);
        filterSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item_addbook,
                Arrays.asList(BookType.values()).stream()
                        .map(BookType::getLabel)
                        .collect(Collectors.toList())
        ));

        Button btnNo = dialog.findViewById(R.id.btnFilterTypeNo);
        Button btnYes = dialog.findViewById(R.id.btnFilterTypeYes);

        btnYes.setOnClickListener(v -> {
            this.typeFilter = BookType.get(filterSpinner.getSelectedItem().toString());
            setList(database.getAllBooks());
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void showStatusFilterDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.status_filter_dialog);
        dialog.setTitle(getResources().getString(R.string.filter_status_title));

        Spinner filterSpinner = dialog.findViewById(R.id.filterStatusSpinner);

        filterSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item_addbook,
                Arrays.asList(BookStatus.values()).stream()
                        .map(BookStatus::getLabel)
                        .collect(Collectors.toList())
                ));

        Button btnNo = dialog.findViewById(R.id.btnFilterStatusNo);
        Button btnYes = dialog.findViewById(R.id.btnFilterStatusYes);

        btnYes.setOnClickListener(v -> {
            this.statusFilter = BookStatus.get(filterSpinner.getSelectedItem().toString());
            setList(database.getAllBooks());
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDialog(){
        //TODO REFACTOR DIALOG FUNCTIONS
    }
}
