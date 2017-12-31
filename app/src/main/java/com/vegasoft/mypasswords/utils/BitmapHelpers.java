package com.vegasoft.mypasswords.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class BitmapHelpers {

    private static final String TAG = "BitmapHelpers";

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor == null)
                return null;
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception exception) {
            return null;
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Nullable
    public static Bitmap prepareAndGetBitmap(String path, Integer crop_px) {
        int orientation = BitmapHelpers.getOrientationFromExif(path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        if (bitmap == null)
            return null;

        float aspect = (float) bitmap.getWidth() / bitmap.getHeight();
        int width = 780;
        if (crop_px != null)
            width = crop_px;
        int height = (int) (width / aspect);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmap = BitmapHelpers.rotateBitmap(bitmap, orientation);
        return bitmap;
    }

    private static int getOrientationFromExif(String path) {

        int orientation = -1;
        try {
            ExifInterface exif = null;
            if (path != null)
                exif = new ExifInterface(path);
            int exifOrientation = ExifInterface.ORIENTATION_ROTATE_270;
            if (exif != null)
                exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to get image exif orientation", e);
        }

        return orientation;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }
}
