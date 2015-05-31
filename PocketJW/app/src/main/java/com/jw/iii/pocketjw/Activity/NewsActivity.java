package com.jw.iii.pocketjw.Activity;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.jw.iii.pocketjw.IIIApplication;
import com.jw.iii.pocketjw.R;

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

        // 设置TabHost
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();


        // 设置Tab
        FrameLayout frameLayout = (FrameLayout)tabHost.getChildAt(1);

        // 书院新闻
        linearLayoutOffical = new LinearLayout(this);
        linearLayoutOffical.setId(R.id.tabOffical);
        linearLayoutOffical.setOrientation(LinearLayout.VERTICAL);
        linearLayoutOffical.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        frameLayout.addView(linearLayoutOffical);
        tabOffical = tabHost.newTabSpec("tabOffical");
        tabOffical.setIndicator("书院新闻");
        tabOffical.setContent(R.id.tabOffical);
        tabHost.addTab(tabOffical);

        // 书院轶事
        // TODO: 测试完毕后删除下一行注释符号
        /*if (iiiApplication.currentUser != null)*/ {
            linearLayoutUnoffical = new LinearLayout(this);
            linearLayoutUnoffical.setId(R.id.tabUnoffical);
            linearLayoutUnoffical.setOrientation(LinearLayout.VERTICAL);
            linearLayoutUnoffical.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            frameLayout.addView(linearLayoutUnoffical);
            tabUnoffical = tabHost.newTabSpec("tabUnoffical");
            tabUnoffical.setIndicator("书院轶事");
            tabUnoffical.setContent(R.id.tabUnoffical);
            tabHost.addTab(tabUnoffical);
        }

        tabHost.setCurrentTab(0);

        // TODO: 修改Tab文本颜色

        // TODO: 修改tab strip颜色

        // TODO: 实现滑动翻页（优化）
        tabHost.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;   // 起始位置
            private float endX, endY;   // 终止位置
            private float offsetX, offsetY; // 偏移量
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();

                        offsetX = endX - startX;
                        offsetY = endY - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)) {
                            if(offsetX < -80 || offsetX > 80) {
                                tabHost.setCurrentTab(tabHost.getCurrentTab() == 0 ? 1 : 0);
                            }
                        }
                }
                return true;
            }
        });
    }



    private IIIApplication iiiApplication;

    private android.support.v7.widget.Toolbar toolbar;
    private TabHost tabHost;
    private LinearLayout linearLayoutOffical, linearLayoutUnoffical;
    private TabHost.TabSpec tabOffical, tabUnoffical;

}
