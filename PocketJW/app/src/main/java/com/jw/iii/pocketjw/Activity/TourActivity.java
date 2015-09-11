package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TourActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        newsListView = (PullToRefreshListView)findViewById(R.id.newsListView);
        newsListView.setMode(PullToRefreshBase.Mode.BOTH);
        newsListView.setOnRefreshListener(newsListener);
        newsListView.setOnItemClickListener(newsItemListener);

        newsItems = new LinkedList<>();
        newsItemsTmp = new ArrayList<>();

        newsAdapter = new SimpleAdapter(this, newsItems,
                R.layout.listviewitem_news, new String[]{"imgImageView", "titleTextView", "dateTextView"},
                new int[]{R.id.imgImageView, R.id.titleTextView, R.id.dateTextView});
        newsListView.setAdapter(newsAdapter);
    }

    public class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            getData();
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            parseData();
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

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("News");
        query.setSkip(page * PAGE_COUNT);
        query.setLimit(PAGE_COUNT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    for (AVObject object : avObjects) {
                        HashMap<String, Object> news = new HashMap<>();
                        AVFile file = object.getAVFile("thumbnail");
                        news.put("imgUrl", file.getThumbnailUrl(true, 72, 54));
                        news.put("imgImageView", R.drawable.ic_launcher);
                        news.put("titleTextView", object.get("title"));
                        news.put("dateTextView", object.get("postDate"));
                        news.put("shareUrl", object.get("shareUrl"));
                        news.put("content", object.get("content"));
                        news.put("objectId", object.getObjectId());
                        newsItemsTmp.add(news);
                    }
                }
            }
        });
    }

    private void parseData() {
        for (HashMap<String, Object> map : newsItemsTmp) {
            newsItems.add(map);
            newsAdapter.notifyDataSetChanged();
        }
        newsItemsTmp.clear();
        updateImageView();
    }

    private void updateImageView() {

        ListView newsListViewTmp = newsListView.getRefreshableView();

        for (int i = 0; i < newsListViewTmp.getCount(); i++) {
            if (!newsItems.get(i).containsKey("isSetImg")) {
                View view = getNewsListViewChild(i, newsListViewTmp);
                ImageView imgImageView = (ImageView)view.findViewById(R.id.imgImageView);
                ImageLoader.getInstance().displayImage(newsItems.get(i).get("imgUrl").toString(), imgImageView);
                newsItems.get(i).put("isSetImg", true);
            }
        }
    }

    private View getNewsListViewChild(int index, ListView listView) {
        final int firstItemPosition = listView.getFirstVisiblePosition();
            final int lastItemPosition = firstItemPosition + listView.getChildCount() - 1;

            if (index < firstItemPosition || index > lastItemPosition) {
                return listView.getAdapter().getView(index, null, listView);
            } else {
                final int childIndex = index - firstItemPosition;
                return listView.getChildAt(childIndex);
        }
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
            Intent newsItemIntent = new Intent(TourActivity.this, NewsItemActivity.class);
            newsItemIntent.putExtra("newsTitle", newsItems.get(position - 1).get("titleTextView").toString());
            newsItemIntent.putExtra("newsUrl", newsItems.get(position - 1).get("shareUrl").toString());
            newsItemIntent.putExtra("newsID", newsItems.get(position - 1).get("objectId").toString());
            newsItemIntent.putExtra("newsContent", newsItems.get(position - 1).get("content").toString());
            startActivity(newsItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0, newsImgCount = 0;
    private int prevLocation = 0;
    private SimpleAdapter newsAdapter;
    private LinkedList<HashMap<String, Object>> newsItems;
    private ArrayList<HashMap<String, Object>> newsItemsTmp;
    private PullToRefreshListView newsListView;
}
