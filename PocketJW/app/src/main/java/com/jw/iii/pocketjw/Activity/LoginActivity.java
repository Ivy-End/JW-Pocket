package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jw.iii.pocketjw.Helper;
import com.jw.iii.pocketjw.UI.CircularImage;
import com.jw.iii.pocketjw.R;

import java.util.List;


public class LoginActivity extends Activity implements View.OnClickListener {

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
            login();
        }

        // 获取布局资源
        avatar = (CircularImage)findViewById(R.id.default_avatar);
        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);
        btLogin = (Button)findViewById(R.id.login);

        // 初始化
        avatar.setImageResource(R.drawable.default_avatar);

        if (loginUsername != "") {
            // 自动填充用户名
            etUsername.setText(loginUsername);
        }

        // 监听事件
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loginUsername = etUsername.getText().toString();
                loginPassword = etPassword.getText().toString();
                // loginPassword = Helper.md5(etPassword.getText().toString());
                // login();

                AVObject gameScore = new AVObject("GameScore");
                gameScore.put("score", 1400);
                gameScore.put("playerName", "Robin");
                gameScore.put("level", 20);
                gameScore.put("gold", 32000);
                gameScore.put("coin", 500);
                gameScore.put("chapter", 15);
                gameScore.put("stage", 8);
                try {
                    gameScore.save();
                } catch (AVException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                /*AVQuery<AVObject> query = new AVQuery<AVObject>("User");
                AVObject gameScore;
                try {
                    gameScore = query.get("553b9f17e4b039a50c0cd311");
                    tvLoginMsg.setText(gameScore.getString("password"));
                } catch (AVException e) {
                    tvLoginMsg.setText(e.getMessage());
                }*/

                break;
            default:
                break;
        }
    }

    private void login() {
        if (loginQuery()) {

            // 保存登陆信息
            SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lastLoginUsername", loginUsername);
            editor.putString("lastLoginUsername", loginPassword);

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

    private boolean loginQuery() {
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
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    loginStatus = false;
                }
            }
        });
        return loginStatus;
    }

    private CircularImage avatar;
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;

    private String loginUsername;
    private String loginPassword;

    private boolean loginStatus;

    private Intent intent;

}
