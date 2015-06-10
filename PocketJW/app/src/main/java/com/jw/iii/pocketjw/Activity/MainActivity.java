package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.avos.avoscloud.*;    // 导入LeanCloud文件
import com.jw.iii.pocketjw.*;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.SlidingTabLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jw.iii.pocketjw.R.layout.activity_main);

        // 初始化IIIApplication
        iiiApplication = (IIIApplication)getApplication();

        // 以下两行连接LeanCloud，切勿修改
        AVOSCloud.initialize(this, "dohthw768v153y9t2eldqiwvtwf9vu07vvyzxv4kjdqbpdsf", "gs5r4j0xg7wg0xkmvjrgdv4gt1hxiaqxpso3jfzani5w8hhk");
        AVAnalytics.trackAppOpened(getIntent());

        initView();
    }

    private void initView() {

        tabNews = (RadioButton) findViewById(R.id.tabNews);
        tabNote = (RadioButton) findViewById(R.id.tabNote);
        tabSolve = (RadioButton) findViewById(R.id.tabSolve);
        tabVolunteer = (RadioButton) findViewById(R.id.tabVolunteer);
        tabMore = (RadioButton)findViewById(R.id.tabMore);
    }
    private IIIApplication iiiApplication;

    private RadioButton tabNews;
    private RadioButton tabNote;
    private RadioButton tabSolve;
    private RadioButton tabVolunteer;
    private RadioButton tabMore;
}
