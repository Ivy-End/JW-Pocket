package com.jw.iii.pocketjw.Activity.Notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jw.iii.pocketjw.Helper.Member.MemberItem;
import com.jw.iii.pocketjw.Helper.Member.MemberItemAdapter;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SelectMemberActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        memberListView = (ListView)findViewById(R.id.memberListView);

        memberItems = new ArrayList<>();
        memberItemsTmp = new ArrayList<>();
        memberItemAdapter = new MemberItemAdapter(memberItems, this, ImageLoader.getInstance());
        memberListView.setAdapter(memberItemAdapter);
        memberListView.setOnItemClickListener(memberItemListener);

        getData();
    }

    private void getData() {
        final AVQuery<AVObject> query = new AVQuery<>("_User").whereNotEqualTo("username", Utils.getCurrentUser().get("username")).orderByAscending("name");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    for (AVObject object : avObjects) {
                        AVFile avFile = object.getAVFile("gravatar");
                        if (avFile != null) {
                            MemberItem memberItem = new MemberItem(object.getObjectId(), object.getAVFile("gravatar").getUrl(),
                                    object.get("name").toString(), object.get("username").toString());
                            memberItemsTmp.add(memberItem);
                        } else {
                            MemberItem memberItem = new MemberItem(object.getObjectId(), "",
                                    object.get("name").toString(), object.get("username").toString());
                            memberItemsTmp.add(memberItem);
                        }
                    }
                    parseData();
                }
            }
        });
    }

    private void parseData() {
        for (MemberItem memberItem : memberItemsTmp) {
            memberItems.add(memberItem);
            memberItemAdapter.notifyDataSetChanged();
        }
        memberItemsTmp.clear();
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

    AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent draftIntent = new Intent(SelectMemberActivity.this, DraftActivity.class);
            draftIntent.putExtra("to", memberItems.get(position).getObjectId());
            startActivity(draftIntent);
        }
    };



    private MemberItemAdapter memberItemAdapter;
    private ArrayList<MemberItem> memberItems, memberItemsTmp;
    private ListView memberListView;
}
