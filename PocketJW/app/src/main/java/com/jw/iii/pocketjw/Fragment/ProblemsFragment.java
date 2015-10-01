package com.jw.iii.pocketjw.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jw.iii.pocketjw.Activity.Problems.ProblemItemActivity;
import com.jw.iii.pocketjw.Helper.Problem.ProblemItem;
import com.jw.iii.pocketjw.Helper.Problem.ProblemItemAdapter;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by End on 2015/9/9.
 */
public class ProblemsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems, container, false);

        problemListView = (PullToRefreshListView)view.findViewById(R.id.problemsListView);
        problemListView.setMode(PullToRefreshBase.Mode.BOTH);
        problemListView.setOnRefreshListener(problemListener);
        problemListView.setOnItemClickListener(problemItemListener);

        problemItems = new ArrayList<>();
        problemItemsTmp = new ArrayList<>();
        problemItemAdapter = new ProblemItemAdapter(problemItems, getActivity(), ImageLoader.getInstance());
        problemListView.setAdapter(problemItemAdapter);

        return view;
    }

    public class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            getDataHeader();
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            parseDataHeader();
            problemListView.onRefreshComplete();
            super.onPostExecute(strings);
        }
    }

    public class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            getData();
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            parseData();
            problemListView.onRefreshComplete();
            problemListView.getRefreshableView().setSelectionFromTop(prevLocation, FragmentActivity.TRIM_MEMORY_BACKGROUND);
            super.onPostExecute(strings);
        }
    }

    private void getDataHeader() {
        page = 0;
        getData();
    }

    private void getData() {
        final AVQuery<AVObject> query = new AVQuery<>("Problem").setSkip(page++ * PAGE_COUNT).setLimit(PAGE_COUNT).orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (avObjects.isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "没有更多问题", Toast.LENGTH_SHORT).show();
                    } else {
                        for (AVObject object : avObjects) {
                            ProblemItem problemItem = new ProblemItem(object.getObjectId(), object.get("title").toString(),
                                    object.get("desc").toString(), object.get("views").toString(), object.get("comments").toString(),
                                    object.getAVUser("publisher"), object.get("images").toString());
                            problemItemsTmp.add(problemItem);
                        }
                    }
                }
            }
        });
    }

    private void parseDataHeader() {
        problemItems.clear();
        problemItemAdapter.notifyDataSetChanged();
        parseData();
    }

    private void parseData() {
        for (ProblemItem problemItem : problemItemsTmp) {
            problemItems.add(problemItem);
            problemItemAdapter.notifyDataSetChanged();
        }
        problemItemsTmp.clear();
    }

    PullToRefreshBase.OnRefreshListener<ListView> problemListener = new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            if (refreshView.isHeaderShown()) {
                new GetHeaderDataTask().execute();
            } else {
                prevLocation = problemItems.size();
                new GetFooterDataTask().execute();
            }
        }
    };

    AdapterView.OnItemClickListener problemItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(getActivity().getApplicationContext(), ((TextView) view.findViewById(R.id.titleTextView)).getText().toString(), Toast.LENGTH_SHORT).show();
            Intent problemItemIntent = new Intent(getActivity(), ProblemItemActivity.class);
            problemItemIntent.putExtra("problemID", problemItems.get(position - 1).getProblemID());
            problemItemIntent.putExtra("problemTitle", problemItems.get(position - 1).getProblemTitle());
            problemItemIntent.putExtra("problemDesc", problemItems.get(position - 1).getProblemDesc());
            problemItemIntent.putExtra("problemView", problemItems.get(position - 1).getProblemView());
            problemItemIntent.putExtra("problemComment", problemItems.get(position - 1).getProblemComment());
            problemItemIntent.putExtra("problemPublisher", problemItems.get(position - 1).getProblemPublisher());
            problemItemIntent.putExtra("problemImages", problemItems.get(position - 1).getProblemImages());
            startActivity(problemItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0;
    private int prevLocation = 0;

    private ArrayList<ProblemItem> problemItems, problemItemsTmp;
    private ProblemItemAdapter problemItemAdapter;
    private PullToRefreshListView problemListView;
}
