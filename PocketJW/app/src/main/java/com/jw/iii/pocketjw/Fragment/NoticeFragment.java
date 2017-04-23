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
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jw.iii.pocketjw.Activity.Notice.NoticeItemActivity;
import com.jw.iii.pocketjw.Helper.AVService;
import com.jw.iii.pocketjw.Helper.IIIApplication;
import com.jw.iii.pocketjw.Helper.Notice.NoticeItem;
import com.jw.iii.pocketjw.Helper.Notice.NoticeItemAdapter;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by End on 2015/9/9.
 */
public class NoticeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        initView(view);

        return view;
    }



    public void initView(View view) {
        noticesListView = (PullToRefreshListView)view.findViewById(R.id.noticesListView);
        noticesListView.setMode(PullToRefreshBase.Mode.BOTH);
        noticesListView.setOnRefreshListener(noticeListener);
        noticesListView.setOnItemClickListener(noticeItemListener);

        noticeItems = new ArrayList<>();
        noticeItemsTmp = new ArrayList<>();
        noticeItemAdapter = new NoticeItemAdapter(noticeItems, getActivity(), ImageLoader.getInstance());
        noticesListView.setAdapter(noticeItemAdapter);
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
            noticesListView.onRefreshComplete();
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
            noticesListView.onRefreshComplete();
            noticesListView.getRefreshableView().setSelectionFromTop(prevLocation, FragmentActivity.TRIM_MEMORY_BACKGROUND);
            super.onPostExecute(strings);
        }
    }

    private void getDataHeader() {
        page = 0;
        getData();
    }

    private void getData() {
        final AVQuery<AVObject> query_to = new AVQuery<>("Notice").setSkip(page * PAGE_COUNT).setLimit(PAGE_COUNT).orderByDescending("postDate");
        query_to.whereEqualTo("to", Utils.getCurrentUser().getObjectId());
        query_to.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (avObjects.isEmpty()) {
                        Toast.makeText(getActivity(), "没有更多通知", Toast.LENGTH_SHORT).show();
                    } else {
                        for (AVObject object : avObjects) {
                            NoticeItem noticeItem = new NoticeItem(object.getObjectId(), object.getAVUser("from").getObjectId(),
                                    object.get("to").toString(), object.get("content").toString(), object.get("createdAt").toString(),
                                    object.get("status").toString(), false);
                            noticeItemsTmp.add(noticeItem);
                        }
                    }
                }
            }
        });

        final AVQuery<AVObject> query_from = new AVQuery<>("Notice").setSkip(page * PAGE_COUNT).setLimit(PAGE_COUNT).orderByDescending("postDate");
        query_from.whereEqualTo("from", Utils.getCurrentUser().getObjectId());
        query_from.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (avObjects.isEmpty()) {
                        Toast.makeText(getActivity(), "没有更多通知", Toast.LENGTH_SHORT).show();
                    } else {
                        for (AVObject object : avObjects) {
                            NoticeItem noticeItem = new NoticeItem(object.getObjectId(), object.getAVUser("from").getObjectId(),
                                    object.get("to").toString(), object.get("content").toString(), object.get("createdAt").toString(),
                                    object.get("status").toString(), true);
                            noticeItemsTmp.add(noticeItem);
                        }
                    }
                }
            }
        });

        page++;
    }

    private void parseDataHeader() {
        noticeItems.clear();
        noticeItemAdapter.notifyDataSetChanged();
        parseData();
    }

    private void parseData() {
        for (NoticeItem noticeItem : noticeItemsTmp) {
            noticeItems.add(noticeItem);
            noticeItemAdapter.notifyDataSetChanged();
        }
        noticeItemsTmp.clear();
    }

    PullToRefreshBase.OnRefreshListener<ListView> noticeListener = new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            if (refreshView.isHeaderShown()) {
                new GetHeaderDataTask().execute();
            } else {
                prevLocation = noticeItems.size();
                new GetFooterDataTask().execute();
            }
        }
    };

    AdapterView.OnItemClickListener noticeItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(getActivity().getApplicationContext(), ((TextView) view.findViewById(R.id.fromTextView)).getText().toString(), Toast.LENGTH_SHORT).show();
            Intent noticeItemIntent = new Intent(getActivity(), NoticeItemActivity.class);
            noticeItemIntent.putExtra("noticeObjectId", noticeItems.get(position - 1).getObjectId());
            noticeItemIntent.putExtra("noticeFrom", noticeItems.get(position - 1).getFromUserName());
            noticeItemIntent.putExtra("noticeContent", noticeItems.get(position - 1).getContent());
            noticeItemIntent.putExtra("noticeDate", noticeItems.get(position - 1).getPostDate());
            startActivity(noticeItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0;
    private int prevLocation = 0;

    private ArrayList<NoticeItem> noticeItems, noticeItemsTmp;
    private NoticeItemAdapter noticeItemAdapter;
    private PullToRefreshListView noticesListView;
}
