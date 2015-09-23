package com.jw.iii.pocketjw.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.avos.avoscloud.AVUser;

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
}
