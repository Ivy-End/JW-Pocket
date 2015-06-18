package com.jw.iii.pocketjw.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
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

        tabGroup = (RadioGroup) findViewById(R.id.tabGroup);
        tabNews = getTabView("书院新闻", R.drawable.news_tab_selector);
        tabNote = getTabView("通知发文", R.drawable.note_tab_selector);
        tabSolve = getTabView("问题解答", R.drawable.solve_tab_selector);
        tabVolunteer = getTabView("书院义工", R.drawable.volunteer_tab_selector);
        tabMore = getTabView("更多", R.drawable.more_tab_selector);

        tabGroup.addView(tabNews);
        if (iiiApplication.currentUser != null) {
            tabGroup.addView(tabNote);
            tabGroup.addView(tabSolve);
            tabGroup.addView(tabVolunteer);
        }
        tabGroup.addView(tabMore);
    }

    private RadioButton getTabView(String tabText, int tabDrawable) {
        RadioButton radioButton = new RadioButton(this);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        radioButton.setLayoutParams(layoutParams);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setText(tabText);
        radioButton.setPadding(0, dipToPx(this, 8), 0, dipToPx(this, 4));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        radioButton.setButtonDrawable(null);
        Resources resources = getBaseContext().getResources();
        ColorStateList colorStateList = resources.getColorStateList(R.color.text_color_tab_selector);
        radioButton.setTextColor(colorStateList);
        Drawable drawable = this.getResources().getDrawable(tabDrawable);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        radioButton.setCompoundDrawables(null, drawable, null, null);
        return radioButton;
    }

    private int dipToPx(Context context, double dip){
        final double scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    private IIIApplication iiiApplication;

    private RadioGroup tabGroup;
    private RadioButton tabNews;
    private RadioButton tabNote;
    private RadioButton tabSolve;
    private RadioButton tabVolunteer;
    private RadioButton tabMore;
}
