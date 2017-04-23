package com.jw.iii.pocketjw.Activity.News;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jw.iii.pocketjw.Helper.News.NewsItem;
import com.jw.iii.pocketjw.Helper.News.NewsItemAdapter;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    public void initView() {
        newsListView = (PullToRefreshListView)findViewById(R.id.newsListView);
        newsListView.setMode(PullToRefreshBase.Mode.BOTH);
        newsListView.setOnRefreshListener(newsListener);
        newsListView.setOnItemClickListener(newsItemListener);

        newsItems = new ArrayList<>();
        newsItemsTmp = new ArrayList<>();
        newsItemAdapter = new NewsItemAdapter(newsItems, this, ImageLoader.getInstance());
        newsListView.setAdapter(newsItemAdapter);
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
            newsListView.onRefreshComplete();
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
            newsListView.onRefreshComplete();
            newsListView.getRefreshableView().setSelectionFromTop(prevLocation, TRIM_MEMORY_BACKGROUND);
            super.onPostExecute(strings);
        }
    }

    private void getDataHeader() {
        page = 0;
        getData();
    }

    private void getData() {
        final AVQuery<AVObject> query = new AVQuery<>("News").setSkip(page * PAGE_COUNT).setLimit(PAGE_COUNT).orderByDescending("postDate");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (avObjects.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "没有更多新闻", Toast.LENGTH_SHORT).show();
                    } else {
                        for (AVObject object : avObjects) {
                            AVFile file = object.getAVFile("thumbnail");
                            NewsItem newsItem = new NewsItem(file.getThumbnailUrl(true, 72, 72), object.get("title").toString(),
                                    object.get("postDate").toString(), object.get("content").toString());
                            newsItemsTmp.add(newsItem);
                        }
                        page++;
                    }
                }
            }
        });
    }

    private void parseDataHeader() {
        newsItems.clear();
        newsItemAdapter.notifyDataSetChanged();
        parseData();
    }

    private void parseData() {
        for (NewsItem newsItem : newsItemsTmp) {
            newsItems.add(newsItem);
            newsItemAdapter.notifyDataSetChanged();
        }
        newsItemsTmp.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    PullToRefreshBase.OnRefreshListener<ListView> newsListener = new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView.isHeaderShown()) {
            new GetHeaderDataTask().execute();
        } else {
            prevLocation = newsItems.size();
            new GetFooterDataTask().execute();
        }
        }
    };

    AdapterView.OnItemClickListener newsItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), ((TextView)view.findViewById(R.id.titleTextView)).getText().toString(), Toast.LENGTH_SHORT).show();
            Intent newsItemIntent = new Intent(NewsActivity.this, NewsItemActivity.class);
            newsItemIntent.putExtra("newsTitle", newsItems.get(position - 1).getTitle());
            newsItemIntent.putExtra("newsUrl", newsItems.get(position - 1).getImgUrl());
            newsItemIntent.putExtra("newsContent", newsItems.get(position - 1).getContent());
            startActivity(newsItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0;
    private int prevLocation = 0;

    private ArrayList<NewsItem> newsItems, newsItemsTmp;
    private NewsItemAdapter newsItemAdapter;
    private PullToRefreshListView newsListView;
}
