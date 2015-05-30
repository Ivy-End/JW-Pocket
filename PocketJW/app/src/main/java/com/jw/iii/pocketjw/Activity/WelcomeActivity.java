package com.jw.iii.pocketjw.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jw.iii.pocketjw.R;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 读取是否为第一次打开应用
        // 若第一次打开应用则跳转到GuideActivity
        // 否则跳转到MainActivity
        SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        isLogIn = preferences.getBoolean("isLogIn", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstIn) {
                    intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                    } else {
                        if(isLogIn) {
                        intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    }
                }
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, WELCOME_DISPLAY_LENGTH);

    }

    Intent intent;
    boolean isFirstIn = false;
    boolean isLogIn = false;
    final int WELCOME_DISPLAY_LENGTH = 2000;
}
