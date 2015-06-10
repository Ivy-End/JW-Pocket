package com.jw.iii.pocketjw.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.jw.iii.pocketjw.IIIApplication;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.TabViewPagerAdapter;
import com.jw.iii.pocketjw.UI.SlidingTabLayout;

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
        viewPager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(), NewsActivity.this));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout)findViewById(R.id.slidingTabLayout);
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
