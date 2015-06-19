package com.jw.iii.pocketjw.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.SlidingTabLayout;

public class NewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);



        Context context = view.getContext();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.newsContent);
        viewPager.setAdapter(new com.jw.iii.pocketjw.PagerAdapter.NewsPagerAdapter(getChildFragmentManager(), context));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.newsSliding);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });


        return view;
    }
}
