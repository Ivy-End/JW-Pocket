package com.jw.iii.pocketjw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class WelcomeActivity extends ActionBarActivity {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Intent intent;
    boolean isFirstIn = false;
    boolean isLogIn = false;
    final int WELCOME_DISPLAY_LENGTH = 2000;
}
