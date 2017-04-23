package com.jw.iii.pocketjw.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.jw.iii.pocketjw.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by End on 2015/9/5.
 */
public class AVService {

    public static void signUp(String username, String password, String name, String studentID, String email, String phone, SignUpCallback signUpCallback, Context context) {
        AVUser avUser = new AVUser();
        avUser.setUsername(username);
        avUser.setPassword(password);
        avUser.put("name", name);
        avUser.put("studentID", studentID);
        avUser.setEmail(email);
        avUser.setMobilePhoneNumber(phone);
        avUser.signUpInBackground(signUpCallback);
    }



    public static void requestResetPassword(String email, RequestPasswordResetCallback requestPasswordResetCallback) {
        AVUser.requestPasswordResetInBackground(email, requestPasswordResetCallback);
    }


}
