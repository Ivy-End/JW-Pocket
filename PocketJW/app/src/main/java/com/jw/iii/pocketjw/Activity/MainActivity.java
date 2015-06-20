package com.jw.iii.pocketjw.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.avos.avoscloud.*;    // 导入LeanCloud文件
import com.jw.iii.pocketjw.*;
import com.jw.iii.pocketjw.Fragment.MoreFragment;
import com.jw.iii.pocketjw.Fragment.NewsFragment;
import com.jw.iii.pocketjw.Fragment.NoteFragment;
import com.jw.iii.pocketjw.Fragment.SolveFragment;
import com.jw.iii.pocketjw.Fragment.VolunteerFragment;
import com.jw.iii.pocketjw.R;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

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

        fragmentManager = getSupportFragmentManager();

        selectTab(id_tabNews);
    }

    private void initView() {
        tabGroup = (RadioGroup) findViewById(R.id.tabGroup);
        tabNews = getTabView(id_tabNews, "书院新闻", R.drawable.news_tab_selector);
        tabNote = getTabView(id_tabNote, "通知发文", R.drawable.note_tab_selector);
        tabSolve = getTabView(id_tabSolve, "问题解答", R.drawable.solve_tab_selector);
        tabVolunteer = getTabView(id_tabVolunteer, "书院义工", R.drawable.volunteer_tab_selector);
        tabMore = getTabView(id_tabMore, "更多", R.drawable.more_tab_selector);

        tabGroup.addView(tabNews);
        if (iiiApplication.currentUser != null) {
            tabGroup.addView(tabNote);
            tabGroup.addView(tabSolve);
            tabGroup.addView(tabVolunteer);
        }
        tabGroup.addView(tabMore);
    }

    private RadioButton getTabView(int id, String tabText, int tabDrawable) {
        RadioButton radioButton = new RadioButton(this);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        radioButton.setId(id);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setText(tabText);
        radioButton.setPadding(0, dipToPx(this, 8), 0, dipToPx(this, 4));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        radioButton.setButtonDrawable(android.R.color.transparent);
        Resources resources = getBaseContext().getResources();
        ColorStateList colorStateList = resources.getColorStateList(R.color.text_color_tab_selector);
        radioButton.setTextColor(colorStateList);
        Drawable drawable = this.getResources().getDrawable(tabDrawable);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        radioButton.setCompoundDrawables(null, drawable, null, null);
        radioButton.setOnClickListener(this);
        return radioButton;
    }

    private int dipToPx(Context context, double dip){
        final double scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        selectTab(v.getId());
    }

    private void selectTab(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (index) {
            case id_tabNews:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    if (iiiApplication.currentUser != null) {
                        newsFragment.setLogin(true);
                    } else {
                        newsFragment.setLogin(false);
                    }
                    fragmentTransaction.add(R.id.tabContent, newsFragment);
                } else {
                    fragmentTransaction.show(newsFragment);
                }
                break;
            /*case id_tabNote:
                if (noteFragment == null) {
                    noteFragment = new NoteFragment();
                    fragmentTransaction.add(R.id.tabContent, noteFragment);
                } else {
                    fragmentTransaction.show(noteFragment);
                }
                break;
            case id_tabSolve:
                if (solveFragment == null) {
                    solveFragment = new SolveFragment();
                    fragmentTransaction.add(R.id.tabContent, solveFragment);
                } else {
                    fragmentTransaction.show(solveFragment);
                }
                break;
            case id_tabVolunteer:
                if (volunteerFragment == null) {
                    volunteerFragment = new VolunteerFragment();
                    fragmentTransaction.add(R.id.tabContent, volunteerFragment);
                } else {
                    fragmentTransaction.show(volunteerFragment);
                }
                break;
            case id_tabMore:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    fragmentTransaction.add(R.id.tabContent, moreFragment);
                } else {
                    fragmentTransaction.show(moreFragment);
                }
                break;*/
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (newsFragment != null) {
            fragmentTransaction.hide(newsFragment);
        }
        /*if (noteFragment != null) {
            fragmentTransaction.hide(noteFragment);
        }
        if (solveFragment != null) {
            fragmentTransaction.hide(solveFragment);
        }
        if (volunteerFragment != null) {
            fragmentTransaction.hide(volunteerFragment);
        }
        if (moreFragment != null) {
            fragmentTransaction.hide(moreFragment);
        }*/
    }

    private IIIApplication iiiApplication;

    private static final int id_tabNews = 0x7f081000;
    private static final int id_tabNote = 0x7f081001;
    private static final int id_tabSolve = 0x7f081002;
    private static final int id_tabVolunteer = 0x7f081003;
    private static final int id_tabMore = 0x7f081004;

    private RadioGroup tabGroup;
    private RadioButton tabNews;
    private RadioButton tabNote;
    private RadioButton tabSolve;
    private RadioButton tabVolunteer;
    private RadioButton tabMore;

    private NewsFragment newsFragment;
    private NoteFragment noteFragment;
    private SolveFragment solveFragment;
    private VolunteerFragment volunteerFragment;
    private MoreFragment moreFragment;

    private FragmentManager fragmentManager;
}
