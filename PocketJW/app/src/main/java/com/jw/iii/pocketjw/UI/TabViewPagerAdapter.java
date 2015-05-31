package com.jw.iii.pocketjw.UI;

/**
 * Created by End on 2015/5/31.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    public TabViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public void addTabs(String strTab) {
        pageList.add(strTab);
    }

    @Override
    public Fragment getItem(int position) {
        return BaseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageList.get(position);
    }

    private Context mContext;
    private ArrayList<String> pageList = new ArrayList<String>();
}