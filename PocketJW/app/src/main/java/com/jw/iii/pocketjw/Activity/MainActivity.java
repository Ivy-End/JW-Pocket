package com.jw.iii.pocketjw.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jw.iii.pocketjw.Fragment.MoreFragment;
import com.jw.iii.pocketjw.Fragment.NewsFragment;
import com.jw.iii.pocketjw.Fragment.ProblemsFragment;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.CircularImage;

import java.util.zip.Inflater;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initView();
        initSlidingMenu();

        setChoiceItem(0);
    }

    private void initView() {
        newsRelativeLayout = (RelativeLayout)findViewById(R.id.newsRelativeLayout);
        newsImageView = (ImageView)findViewById(R.id.newsImageView);
        newsTextView = (TextView)findViewById(R.id.newsTextView);
        problemsRelativeLayout = (RelativeLayout)findViewById(R.id.problemsRelativeLayout);
        problemsImageView = (ImageView)findViewById(R.id.problemsImageView);
        problemsTextView = (TextView)findViewById(R.id.problemsTextView);
        moreRelativeLayout = (RelativeLayout)findViewById(R.id.moreRelativeLayout);
        moreImageView = (ImageView)findViewById(R.id.moreImageView);
        moreTextView = (TextView)findViewById(R.id.moreTextView);
        newsRelativeLayout.setOnClickListener(this);
        problemsRelativeLayout.setOnClickListener(this);
        moreRelativeLayout.setOnClickListener(this);
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setBehindWidth(640);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu_main);


        CircularImage gravatar = (CircularImage)findViewById(R.id.gravatar);
        nameTextView = (TextView)findViewById(R.id.nameTextView);


        gravatar.setImageResource(R.drawable.gravatar);
        nameTextView.setText(AVUser.getCurrentUser().get("name").toString());
    }

    private void setChoiceItem(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        clearChoice();
        hideFragments(transaction);

        switch (index) {
            case 0:
                newsImageView.setImageResource(R.drawable.news_hover);
                newsTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.contentFrameLayout, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }
                break;
            case 1:
                problemsImageView.setImageResource(R.drawable.problem_hover);
                problemsTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                if (problemsFragment == null) {
                    problemsFragment = new ProblemsFragment();
                    transaction.add(R.id.contentFrameLayout, problemsFragment);
                } else {
                    transaction.show(problemsFragment);
                }
                break;
            case 2:
                moreImageView.setImageResource(R.drawable.more_hover);
                moreTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.contentFrameLayout, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void clearChoice() {
        newsImageView.setImageResource(R.drawable.news_default);
        newsTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        problemsImageView.setImageResource(R.drawable.problem_default);
        problemsTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        moreImageView.setImageResource(R.drawable.more_default);
        moreTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (problemsFragment != null) {
            transaction.hide(problemsFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newsRelativeLayout:
                setChoiceItem(0);
                break;
            case R.id.problemsRelativeLayout:
                setChoiceItem(1);
                break;
            case R.id.moreRelativeLayout:
                setChoiceItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private TextView nameTextView;
    private Fragment newsFragment, problemsFragment, moreFragment;
    private FrameLayout contentFrameLayout;
    private RelativeLayout newsRelativeLayout, problemsRelativeLayout, moreRelativeLayout;
    private ImageView newsImageView, problemsImageView, moreImageView;
    private TextView newsTextView, problemsTextView, moreTextView;
    private FragmentManager fragmentManager;

    private SlidingMenu slidingMenu;
}
