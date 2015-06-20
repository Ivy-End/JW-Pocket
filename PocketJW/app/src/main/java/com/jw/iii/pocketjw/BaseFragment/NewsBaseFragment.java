package com.jw.iii.pocketjw.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.RefreshableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by End on 2015/6/19.
 */
public class NewsBaseFragment extends Fragment {
    private static final String DATA = "data";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_news, container, false);
        refreshableView = (RefreshableView) view.findViewById(R.id.newsView);
        listView = (ListView) view.findViewById(R.id.newsList);
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("tvTitle", items[i]);
            list.add(hashMap);
        }
        adapter = new SimpleAdapter(view.getContext(), list, R.layout.listviewitem_news, new String[] {"tvTitle"}, new int[] {R.id.tvTitle});
        listView.setAdapter(adapter);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Fragment newInstance(int type){
        Fragment fragment = new NewsBaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RefreshableView refreshableView;
    private ListView listView;
    private SimpleAdapter adapter;
    private String[] items = {"A", "B", "C"};


}
