package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.avos.avoscloud.*;    // 导入LeanCloud文件
import com.jw.iii.pocketjw.*;
import com.jw.iii.pocketjw.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jw.iii.pocketjw.R.layout.activity_main);

        // 初始化IIIApplication
        iiiApplication = (IIIApplication)getApplication();

        // 以下两行连接LeanCloud，切勿修改
        AVOSCloud.initialize(this, "dohthw768v153y9t2eldqiwvtwf9vu07vvyzxv4kjdqbpdsf", "gs5r4j0xg7wg0xkmvjrgdv4gt1hxiaqxpso3jfzani5w8hhk");
        AVAnalytics.trackAppOpened(getIntent());

        if(iiiApplication.currentUser == null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            });
        }
    }

    private IIIApplication iiiApplication;

    private Intent intent;
}
