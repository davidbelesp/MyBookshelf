package com.davidbelesp.mybookshelf.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.utils.Constants;
import com.davidbelesp.mybookshelf.utils.PdfUtils;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.zip.Inflater;

public class DatabaseFragment extends Fragment {

    private Button clearDB;
    private Button downloadDB;
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

        downloadPDF.setOnClickListener( e -> {
            this.createAndOpenPdf();
        });
    }

    private void loadElements(View view) {
        this.clearDB = view.findViewById(R.id.buttonDeleteDB);
        this.downloadDB = view.findViewById(R.id.buttonDownloadDB);
        this.downloadPDF = view.findViewById(R.id.buttonDownloadPDF);
    }

    private void createAndOpenPdf() {
        File pdfFile = PdfUtils.createPDF(requireContext());
        PdfUtils.openPDF(requireContext(), pdfFile);
    }


}