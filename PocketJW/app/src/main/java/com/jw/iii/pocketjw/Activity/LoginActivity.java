package com.jw.iii.pocketjw.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jw.iii.pocketjw.UI.CircularImage;
import com.jw.iii.pocketjw.R;

import java.util.List;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 以下两行连接LeanCloud，切勿修改
        AVOSCloud.initialize(this, "dohthw768v153y9t2eldqiwvtwf9vu07vvyzxv4kjdqbpdsf", "gs5r4j0xg7wg0xkmvjrgdv4gt1hxiaqxpso3jfzani5w8hhk");
        AVAnalytics.trackAppOpened(getIntent());

        // 已有登陆记录则自动登陆
        SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
        loginUsername = preferences.getString("lastLoginUsername", "");
        loginPassword = preferences.getString("lastLoginPassword", "");
        if (loginUsername != "" && loginPassword != "") {
            if (login()) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(LoginActivity.this, LoginActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }
                });
            }
        }

        // 获取布局资源
        avatar = (CircularImage)findViewById(R.id.default_avatar);

        // 初始化
        avatar.setImageResource(R.drawable.default_avatar);

        if (loginUsername != "") {
            // 自动填充用户名
        }
    }

    private boolean login() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("User");
        query.whereEqualTo("username", loginUsername);
        query.whereEqualTo("password", loginPassword);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e == null) {
                    loginStatus = true;
                } else {
                    // 显示错误信息
                    loginStatus = false;
                }
            }
        });
        return loginStatus;
    }

    private CircularImage avatar;

    private String loginUsername;
    private String loginPassword;

    private boolean loginStatus;

    private Intent intent;

}
