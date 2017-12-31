package com.vegasoft.mypasswords.data.files;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    private static final String PREFERENCE_CENTRAL = "MyStorage";
    public static boolean deleteFile(String path) {
        return new File(path).delete();
    }

    public static String saveToInternalStorage(final Context context, Bitmap bitmapImage, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/PreferenceCentral/app_data/imageDir
        File directory = cw.getDir(PREFERENCE_CENTRAL, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getPath();
    }

}
