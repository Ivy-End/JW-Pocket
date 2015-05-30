package com.jw.iii.pocketjw.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.jw.iii.pocketjw.R;

public class NewsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
    }

    private android.support.v7.widget.Toolbar toolbar;
}
