package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jw.iii.pocketjw.Activity.User.LoginActivity;
import com.jw.iii.pocketjw.R;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, WELCOME_DISPLAY_LENGTH);
    }

    private Intent intent;
    private final int WELCOME_DISPLAY_LENGTH = 2000;
}
