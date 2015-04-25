package com.jw.iii.pocketjw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GuideActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 将isFirstIn置为false，下次将不再加载此页面
        // GuideActivity测试完毕后可删去第3-4行注释
        SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
        Editor editor = preferences.edit();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide, menu);
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
}
