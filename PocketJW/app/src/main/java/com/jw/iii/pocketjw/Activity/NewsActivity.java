package com.jw.iii.pocketjw.Activity;

import android.app.TabActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jw.iii.pocketjw.R;

import org.w3c.dom.Text;

public class NewsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        //tabHost.addTab(tabHost.newTabSpec("tabOffical").setIndicator("书院新闻").setContent(R.id.tabOffical));



        tabOffical = tabHost.newTabSpec("tabOffical");
        tabOffical.setIndicator("书院新闻");
        tabOffical.setContent(R.id.tabOffical);
        tabHost.addTab(tabOffical);

        tabUnoffical = tabHost.newTabSpec("tabUnoffical");
        tabUnoffical.setIndicator("书院轶事");
        tabUnoffical.setContent(R.id.tabUnoffical);
        tabHost.addTab(tabUnoffical);

        tabHost.setCurrentTab(0);
    }

    private android.support.v7.widget.Toolbar toolbar;
    private TabHost tabHost;
    private TabHost.TabSpec tabOffical, tabUnoffical;
}
