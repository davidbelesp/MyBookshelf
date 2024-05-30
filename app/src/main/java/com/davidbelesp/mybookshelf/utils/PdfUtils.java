package com.davidbelesp.mybookshelf.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.database.BooksDB;
import com.davidbelesp.mybookshelf.models.Book;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfUtils {

    public static File createPDF(Context ctx) {
        ArrayList<Book> books = BooksDB.getInstance(ctx).getAllBooks();
        books.sort((b1, b2) -> b2.getScore().compareTo(b1.getScore()));
        books.sort((b1, b2) -> b2.getChapters().compareTo(b1.getChapters()));

        File pdfDir = new File(ctx.getFilesDir(), "pdfs");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }
        File file = new File(pdfDir, "BooksList.pdf");

        try (FileOutputStream fos = new FileOutputStream(file)) {

            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(getBookTable(books, ctx));

            // Close document
            document.close();

            Toast.makeText(ctx, "PDF created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Failed to create PDF", Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    public static void openPDF(Context ctx, File pdf){
        Uri pdfUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", pdf);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ctx, "No application found to open PDF files", Toast.LENGTH_SHORT).show();
        }
    }

    private static Cell getCell(String text, boolean darkMode) {
        Cell cell;
        int[] bgColor = darkMode ?  hexToRgb("#30A3CF") : hexToRgb("#FFD4C5");
        int[] fontColor = darkMode ?  hexToRgb("#000000") : hexToRgb("#C23660");
        cell = new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER));
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBackgroundColor(new DeviceRgb(bgColor[0], bgColor[1], bgColor[2]));
        cell.setFontColor(new DeviceRgb(fontColor[0], fontColor[1], fontColor[2]));
        return cell;
    }

    private static Cell getHeaderCell(String text, boolean darkMode) {
        Cell cell;
        int[] bgColor = darkMode ?  hexToRgb("#214694") : hexToRgb("#C23660");
        int[] fontColor = darkMode ?  hexToRgb("#FFFFFF") : hexToRgb("#FFD4C5");
        cell = new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER));
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBackgroundColor(new DeviceRgb(bgColor[0], bgColor[1], bgColor[2]));
        cell.setFontColor(new DeviceRgb(fontColor[0], fontColor[1], fontColor[2]));
        return cell;
    }

    private static Cell getImageCell(String filepath, boolean isDarkMode, Context ctx){
        Cell cell = new Cell();
        String notAvailable = ctx.getResources().getString(R.string.pdf_not_available);
        // Load image if available
        if (filepath != null) {
            try {
                Uri imageUri = ImageUtils.loadImage(ctx, filepath);
                String path = imageUri.getPath();
                Image img = new Image(ImageDataFactory.create(path));
                img.setWidth(60); // Set the width of the image (in points)
                img.setHeight(75); // Set the height of the image (in points)
                cell.add(img);
            } catch (IOException e) {
                cell = getCell(notAvailable, isDarkMode);
            }
        } else {
            cell = getCell(notAvailable, isDarkMode);
        }
        //Set background color
        int[] bgColor = isDarkMode ?  hexToRgb("#30A3CF") : hexToRgb("#FFD4C5");
        cell.setBackgroundColor(new DeviceRgb(bgColor[0], bgColor[1], bgColor[2]));

        return cell;
    }

    private static Table getBookTable(ArrayList<Book> books, Context ctx) {

        boolean isDarkMode = ThemeUtils.isDarkMode();
        Log.i("PdfUtils", "Dark mode: " + isDarkMode);

        Table table = new Table(new float[]{1,1,2, 4, 6, 2, 2}); // Adjust column widths as needed

        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_number), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_score), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_image), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_title), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_chapters), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_volumes), isDarkMode));
        table.addHeaderCell(getHeaderCell(ctx.getResources().getString(R.string.pdf_column_desc), isDarkMode));

        int i = 1;
        for (Book book : books) {

            //table.addCell(new Image(ImageDataFactory.create(book.getImage())));
            String notAvailable = ctx.getResources().getString(R.string.pdf_not_available);

            table = table.addCell(getCell(String.valueOf(i), isDarkMode));
            table = book.getScore() != null ? table.addCell(getCell(String.valueOf(book.getScore()), isDarkMode))
                    : table.addCell(getCell(notAvailable, isDarkMode));
            // Image cell
            table = table.addCell(getImageCell(book.getImage(), isDarkMode, ctx));

            table = book.getTitle() != null ? table.addCell(getCell(book.getTitle(), isDarkMode))
                    : table.addCell(getCell(notAvailable, isDarkMode));
            table = book.getChapters() != null ? table.addCell(getCell(String.valueOf(book.getChapters()), isDarkMode))
                    : table.addCell(getCell(notAvailable, isDarkMode));
            table = book.getVolumes() != null ? table.addCell(getCell(String.valueOf(book.getVolumes()), isDarkMode))
                    : table.addCell(getCell(notAvailable, isDarkMode));
            table = book.getDescription() != null ? table.addCell(getCell(book.getDescription(), isDarkMode))
                    : table.addCell(getCell(notAvailable, isDarkMode));
            i++;
        }
        return table;
    }

    public static int[] hexToRgb(String hex) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new int[]{r, g, b};
    }
}
