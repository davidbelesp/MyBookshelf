package com.davidbelesp.mybookshelf.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;

import java.util.zip.Inflater;

public class DatabaseFragment extends Fragment {

    Button clearDB;
    Button downloadDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_database, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadElements(view);

        setEvents();

    }

    private void setEvents() {

        clearDB.setOnClickListener( e -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete database")
                    .setMessage("Are you sure you want to delete all entries?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> BooksDB.getInstance(getContext()).removeAllBooks())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {})
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        downloadDB.setOnClickListener( e -> {
            Toast.makeText(getContext(), "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
            //TODO DOWNLOAD DATABASE
        });

    }

    private void loadElements(View view) {
        this.clearDB = view.findViewById(R.id.buttonDeleteDB);
        this.downloadDB = view.findViewById(R.id.buttonDownloadDB);
    }
}