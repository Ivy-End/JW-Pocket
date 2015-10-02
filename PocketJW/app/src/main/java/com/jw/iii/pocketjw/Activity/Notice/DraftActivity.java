package com.jw.iii.pocketjw.Activity.Notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;

public class DraftActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent selectMemberIntent = getIntent();
        toObjectId = selectMemberIntent.getStringExtra("to");

        noticeEditText = (EditText)findViewById(R.id.noticeEditText);
        sendButton = (Button)findViewById(R.id.sendButton);

        sendButton.setOnClickListener(sendListener);
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

    View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String noticeContent = noticeEditText.getText().toString();
            if (noticeContent.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入通知内容", Toast.LENGTH_SHORT).show();
            } else {
                AVObject object = new AVObject("Notice");
                object.put("from", Utils.getCurrentUser());
                object.put("content", noticeContent);
                object.put("to", toObjectId);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        }
    };

    private String toObjectId;
    private EditText noticeEditText;
    private Button sendButton;
}
