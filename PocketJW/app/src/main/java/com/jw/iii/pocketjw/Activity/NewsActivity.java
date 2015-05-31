package com.jw.iii.pocketjw.Activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jw.iii.pocketjw.Helper;
import com.jw.iii.pocketjw.IIIApplication;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.SlidingTabLayout;
import com.jw.iii.pocketjw.UI.TabViewPagerAdapter;

public class NewsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        iiiApplication = (IIIApplication)getApplication();

        initView();
    }



    private void initView() {

        // 设置ToolBar
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);

        // 设置Tab
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), NewsActivity.this);
        tabViewPagerAdapter.addTabs("书院新闻");
        // TODO: 测试结束后删除下一行注释符号
        // if (iiiApplication.currentUser != null) {
            tabViewPagerAdapter.addTabs("书院轶事");
        // }
        viewPager.setAdapter(tabViewPagerAdapter);


        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });
    }



    private IIIApplication iiiApplication;

    private android.support.v7.widget.Toolbar toolbar;

}
