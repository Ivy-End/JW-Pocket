package com.jw.iii.pocketjw.Activity.More.Contact;

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
import com.jw.iii.pocketjw.Helper.Contact.ContactItem;
import com.jw.iii.pocketjw.Helper.Contact.ContactItemAdapter;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        contactListView = (PullToRefreshListView)findViewById(R.id.contactListView);
        contactListView.setMode(PullToRefreshBase.Mode.BOTH);
        contactListView.setOnRefreshListener(contactListener);
        contactListView.setOnItemClickListener(contactItemListener);

        contactItems = new ArrayList<>();
        contactItemsTmp = new ArrayList<>();
        contactItemAdapter = new ContactItemAdapter(contactItems, this, ImageLoader.getInstance());
        contactListView.setAdapter(contactItemAdapter);
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
        AVQuery<AVObject> query = new AVQuery<>("_User").setSkip(page++ * PAGE_COUNT).setLimit(PAGE_COUNT);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (avObjects.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "没有更多的联系人", Toast.LENGTH_SHORT).show();
                    } else {
                        for(AVObject object : avObjects) {
                            AVFile file = object.getAVFile("gravatar");
                            ContactItem contactItem = new ContactItem(file.getThumbnailUrl(true, 72, 72), object.get("name").toString(),
                                    object.get("mobilePhoneNumber").toString(), object.get("email").toString());
                            contactItemsTmp.add(contactItem);
                        }
                    }
                }
            }
        });
    }

    private void parseData() {
        for (ContactItem contactItem : contactItemsTmp) {
            contactItems.add(contactItem);
            contactItemAdapter.notifyDataSetChanged();
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
            Intent contactItemIntent = new Intent(ContactActivity.this, ContactItemActivity.class);
            contactItemIntent.putExtra("contactImg", contactItems.get(position - 1).getImgUrl());
            contactItemIntent.putExtra("contactName", contactItems.get(position - 1).getName());
            contactItemIntent.putExtra("contactPhone", contactItems.get(position - 1).getPhone());
            contactItemIntent.putExtra("contactEmail", contactItems.get(position - 1).getEmail());
            startActivity(contactItemIntent);
        }
    };

    final private int PAGE_COUNT = 10;
    private int page = 0;
    private int prevLocation = 0;
    private ArrayList<ContactItem> contactItems, contactItemsTmp;
    private ContactItemAdapter contactItemAdapter;
    private PullToRefreshListView contactListView;
}
