package com.jw.iii.pocketjw.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.jw.iii.pocketjw.R;


public class GuideActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 将isFirstIn置为false，下次将不再加载此页面
        // GuideActivity测试完毕后可删去第3-4行注释
        SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
        // Editor editor = preferences.edit();
        // editor.putBoolean("isFirstIn", false);
        // editor.commit();

        // 临时跳转至MainActivity
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                intent = new Intent(GuideActivity.this, LoginActivity.class);
                GuideActivity.this.startActivity(intent);
                GuideActivity.this.finish();
            }
        }, 2000);
    }

    Intent intent;
}
