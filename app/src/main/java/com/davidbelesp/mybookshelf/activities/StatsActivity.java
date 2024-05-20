package com.davidbelesp.mybookshelf.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.locale.LocaleHelper;
import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.StatsModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class StatsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //data
    private BooksDB database;
    private ArrayList<Book> books;
    private DecimalFormat formatter;

    //stats items
    private TextView averageScore;
    private TextView entries;
    private TextView chapters;
    private TextView volumes;
    private TextView pages;

    private TextView mangaText;
    private TextView manhwaText;
    private TextView manhuaText;
    private TextView novelText;
    private TextView lightNovelText;

    private TextView completedText;
    private TextView readigText;
    private TextView onholdText;
    private TextView droppedText;
    private TextView plantoreadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        loadElements();

        setEvents();

        loadDatabase();

        setData();
    }

    private void loadDatabase() {
        this.database = BooksDB.getInstance(this);
        this.books = database.getAllBooks();
    }

    private void setEvents() {

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private void loadElements() {
        this.toolbar = findViewById(R.id.statsToolbar);
        this.averageScore = findViewById(R.id.statsAverageScore);
        this.entries = findViewById(R.id.statsEntries);
        this.chapters = findViewById(R.id.statsChapters);
        this.volumes = findViewById(R.id.statsVolumes);
        this.pages = findViewById(R.id.statsPagesEstimation);

        this.mangaText = findViewById(R.id.statsMangaNumber);
        this.manhuaText = findViewById(R.id.statsManhuaNumber);
        this.manhwaText = findViewById(R.id.statsManhwaNumber);
        this.novelText = findViewById(R.id.statsNovelNumber);
        this.lightNovelText = findViewById(R.id.statsLightNovelNumber);

        this.completedText = findViewById(R.id.statsCompletedNumber);
        this.readigText = findViewById(R.id.statsReadingNumber);
        this.onholdText = findViewById(R.id.statsOnholdNumber);
        this.droppedText = findViewById(R.id.statsDropppedNumber);
        this.plantoreadText = findViewById(R.id.statsPlantoreadNumber);

        this.formatter = (DecimalFormat)
                DecimalFormat.getNumberInstance(LocaleHelper.getCurrentLocale(this));
        formatter.setMaximumFractionDigits(2);
    }

    private void setData() {
        if(this.books.size() < 1) return;

        StatsModel stats = new StatsModel();

        //primitive references
        stats.setEntries(books.size());
        stats.setDenominatorAverage((int) books.stream()
                .filter(book -> !book.getScore().equals(0))
                .count());

        //setting calculations
        books.forEach(book -> {

            if(book.getScore()!=0) stats.sumAverageTotal(book.getScore());
            stats.sumChaptersNumber(book.getChapters());
            stats.sumVolumesNumber(book.getVolumes());

            switch (book.getTypeObject()){
                case Manga: stats.sumMangaNumber(1); break;
                case Manhwa: stats.sumManhwaNumber(1); break;
                case Manhua: stats.sumManhuaNumber(1); break;
                case Light_Novel: stats.sumLightNovelNumber(1); break;
                case Novel: stats.sumNovelNumber(1); break;
            }

            switch (book.getStatusObject()){
                case Completed: stats.sumCompletedNumber(1);break;
                case Reading: stats.sumReadingNumber(1);break;
                case On_hold: stats.sumOnholdNumber(1);break;
                case Dropped: stats.sumDroppedNumber(1);break;
                case Plan_to_read: stats.sumPlantoreadNumber(1);break;
            }
        });

        //setting values
        this.averageScore.setText(formatNumber(stats.getAverage()));
        this.entries.setText(stats.getEntries().toString());
        this.chapters.setText(formatNumber(stats.getChaptersNumber()));
        this.volumes.setText(formatNumber(stats.getVolumesNumber()));
        this.pages.setText(formatNumber(stats.getPagesAproximation()));

        this.mangaText.setText(stats.getMangaNumber().toString());
        this.manhwaText.setText(stats.getManhwaNumber().toString());
        this.manhuaText.setText(stats.getManhuaNumber().toString());
        this.novelText.setText(stats.getNovelNumber().toString());
        this.lightNovelText.setText(stats.getLightNovelNumber().toString());

        this.completedText.setText(stats.getCompletedNumber().toString());
        this.readigText.setText(stats.getReadingNumber().toString());
        this.onholdText.setText(stats.getOnholdNumber().toString());
        this.droppedText.setText(stats.getDroppedNumber().toString());
        this.plantoreadText.setText(stats.getPlantoreadNumber().toString());
    }

    private String formatNumber(Integer number){
        String returnNumber = "";
        Double doubleNumber = Double.valueOf(number);
        if (number >= 1_000_000_000
                && LocaleHelper.getCurrentLocale(this).equals(Locale.ENGLISH)) {
            returnNumber = formatter.format(doubleNumber / 1_000_000_000)
                    + "b";
        } else if (number >= 1_000_000) {
            returnNumber = formatter.format(doubleNumber / 1_000_000)
                    + "m";
        } else if (number >= 100_000) {
            returnNumber = formatter.format(doubleNumber / 1_000)
                    + "k";
        } else {
            returnNumber = formatter.format(number);
        }
        return returnNumber;
    }

    private String formatNumber(Double number){
        return formatter.format(number);
    }
}