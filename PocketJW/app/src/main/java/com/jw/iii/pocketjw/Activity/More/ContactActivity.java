package com.jw.iii.pocketjw.Activity.More;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ContactActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        contactListView = (PullToRefreshListView)findViewById(R.id.contactListView);
        contactListView.setMode(PullToRefreshBase.Mode.BOTH);
        contactListView.setOnRefreshListener(contactListener);
        contactListView.setOnItemClickListener(contactItemListener);

        contactItems = new LinkedList<>();
        contactItemsTmp = new ArrayList<>();

        contactAdapter = new SimpleAdapter(this, contactItems,
                R.layout.listviewitem_contact, new String[]{"imgImageView", "nameTextView", "phoneTextView"},
                new int[]{R.id.imgImageView, R.id.nameTextView, R.id.phoneTextView});
        contactListView.setAdapter(contactAdapter);

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

    public class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            getData();
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            parseData();
            contactListView.onRefreshComplete();
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
            contactListView.onRefreshComplete();
            contactListView.getRefreshableView().setSelectionFromTop(prevLocation, TRIM_MEMORY_BACKGROUND);
            super.onPostExecute(strings);
        }
    }

    private void getData() {
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.setSkip(page * PAGE_COUNT);
        query.setLimit(PAGE_COUNT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    for(AVObject object : avObjects) {
                        HashMap<String, Object> contact = new HashMap<>();

                        contact.put("imgImageView", R.drawable.gravatar);
                        contact.put("nameTextView", object.get("name"));
                        contact.put("phoneTextView", object.get("mobilePhoneNumber"));
                        contact.put("emailTextView", object.get("email"));
                        contact.put("objectId", object.getObjectId());
                        contactItemsTmp.add(contact);
                    }
                }
            }
        });
    }

    private void parseData() {
        for (HashMap<String, Object> map : contactItemsTmp) {
            contactItems.add(map);
            contactAdapter.notifyDataSetChanged();
        }
        contactItemsTmp.clear();
    }

    PullToRefreshBase.OnRefreshListener<ListView> contactListener = new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            if (refreshView.isHeaderShown()) {
                new GetHeaderDataTask().execute();
            } else {
                prevLocation = contactItems.size();
                new GetFooterDataTask().execute();
            }
        }
    };

    AdapterView.OnItemClickListener contactItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), ((TextView)view.findViewById(R.id.nameTextView)).getText().toString(), Toast.LENGTH_SHORT).show();
            Intent contactItemIntent = new Intent(ContactActivity.this, contactItemActivity.class);
            contactItemIntent.putExtra("contactName", contactItems.get(position - 1).get("nameTextView").toString());
            contactItemIntent.putExtra("contactPhone", contactItems.get(position - 1).get("phoneTextView").toString());
            contactItemIntent.putExtra("contactEmail", contactItems.get(position - 1).get("emailTextView").toString());
            contactItemIntent.putExtra("contactID", contactItems.get(position - 1).get("objectId").toString());
            startActivity(contactItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0;
    private int prevLocation = 0;
    private SimpleAdapter contactAdapter;
    private LinkedList<HashMap<String, Object>> contactItems;
    private ArrayList<HashMap<String, Object>> contactItemsTmp;
    private PullToRefreshListView contactListView;
}
