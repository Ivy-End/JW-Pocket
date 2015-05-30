package com.jw.iii.pocketjw;

import android.app.Application;

import com.avos.avoscloud.AVUser;

/**
 * Created by End on 2015/5/30.
 */
public class IIIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AVUser currentUser;
}
