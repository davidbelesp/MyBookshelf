package com.davidbelesp.mybookshelf.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static String saveImage(Context ctx, Bitmap image) throws IOException {
        String imageName = "cover_"+System.currentTimeMillis()+".png";


        File f = new File(ctx.getFilesDir(), imageName);
        f.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40 /*ignored for PNGS*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);

        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return imageName;
    }

    public static Uri loadImage(Context ctx, String filename) throws IOException {
        return Uri.fromFile(new File(ctx.getFilesDir() + "/" +filename));
    }

}
