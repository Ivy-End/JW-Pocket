package com.jw.iii.pocketjw.PagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jw.iii.pocketjw.BaseFragment.NewsBaseFragment;

/**
 * Created by End on 2015/6/19.
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public NewsPagerAdapter(FragmentManager fm, Context context, boolean login) {
        super(fm);
        this.mContext = context;

        if (login) {
            PAGE_COUNT = 2;
            mTabTitle = new String[] {"书院新闻", "书院轶事"};
        } else {
            PAGE_COUNT = 1;
            mTabTitle = new String[] {"书院新闻"};
        }
    }

    @Override
    public Fragment getItem(int position) {

        return NewsBaseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  mTabTitle[position];
    }


    private int PAGE_COUNT;

    private String mTabTitle[];
}
