package com.jw.iii.pocketjw.Activity.Notice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.jw.iii.pocketjw.R;

public class NoticeItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent noticeItemIntent = getIntent();
        id = noticeItemIntent.getStringExtra("noticeObjectId");
        from = noticeItemIntent.getStringExtra("noticeFrom");
        content = noticeItemIntent.getStringExtra("noticeContent");
        date = noticeItemIntent.getStringExtra("noticeDate");

        initView();
    }

    private void initView() {
        fromTextView = (TextView)findViewById(R.id.fromTextView);
        contentTextView = (TextView)findViewById(R.id.contentTextView);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        delayButton = (Button)findViewById(R.id.delayButton);
        finishButton = (Button)findViewById(R.id.finishButton);

        fromTextView.setText(from);
        contentTextView.setText(content);
        dateTextView.setText(date);

        delayButton.setOnClickListener(delayListener);
        finishButton.setOnClickListener(finishListener);
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

    View.OnClickListener delayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AVQuery<AVObject> query = new AVQuery<>("Notice");
            query.getInBackground(id, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        avObject.put("status", "DELAY");
                        avObject.saveInBackground();
                        finish();
                    }
                }
            });
        }
    };

    View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AVQuery<AVObject> query = new AVQuery<>("Notice");
            query.getInBackground(id, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        avObject.put("status", "FINISH");
                        avObject.saveInBackground();
                        finish();
                    }
                }
            });
        }
    };

    private String id, from, content, date;
    private TextView fromTextView, contentTextView, dateTextView;
    private Button delayButton, finishButton;
}
