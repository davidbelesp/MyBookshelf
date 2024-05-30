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
import com.davidbelesp.mybookshelf.utils.PdfUtils;

import java.io.File;

public class DatabaseFragment extends Fragment {

    private Button clearDB;
    private Button downloadPDF;

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
                    .setTitle(R.string.delete_database_dialog_title)
                    .setMessage(R.string.delete_database_dialog_message)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> BooksDB.getInstance(getContext()).removeAllBooks())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {})
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        downloadPDF.setOnClickListener( e -> {
            Toast.makeText(requireContext(), R.string.downloading_pdf, Toast.LENGTH_SHORT).show();
            this.createAndOpenPdf();
        });
    }

    private void loadElements(View view) {
        this.clearDB = view.findViewById(R.id.buttonDeleteDB);
        this.downloadPDF = view.findViewById(R.id.buttonDownloadPDF);
    }

    private void createAndOpenPdf() {
        File pdfFile = PdfUtils.createPDF(requireContext());
        PdfUtils.openPDF(requireContext(), pdfFile);
    }


}