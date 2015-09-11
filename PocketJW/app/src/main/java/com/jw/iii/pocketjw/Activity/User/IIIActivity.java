package com.jw.iii.pocketjw.Activity.User;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by End on 2015/9/5.
 */
public class IIIActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        userId = null;

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getObjectId();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    public IIIActivity activity;
    private String userId;
}
