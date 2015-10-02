package com.jw.iii.pocketjw.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.avos.avoscloud.AVUser;

import java.io.ByteArrayOutputStream;

/**
 * Created by End on 2015/9/23.
 */
public class Utils {

    public static AVUser getCurrentUser() {
        return AVUser.getCurrentUser();
    }

    public static boolean hasSDCard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static int getScaleRadio(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int scaleRadio = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRadio = Math.round((float)height / (float)reqHeight);
            final int widthRadio = Math.round((float)width / (float)reqWidth);
            scaleRadio = heightRadio < widthRadio ? heightRadio : widthRadio;
        }
        return scaleRadio;
    }

    public static Bitmap getScaleBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = getScaleRadio(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

}
