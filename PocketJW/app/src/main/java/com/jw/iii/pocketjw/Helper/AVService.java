package com.jw.iii.pocketjw.Helper;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by End on 2015/9/5.
 */
public class AVService {

    public static void signUp(String username, String password, String name, String studentID, String email, String phone, SignUpCallback signUpCallback) {
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
