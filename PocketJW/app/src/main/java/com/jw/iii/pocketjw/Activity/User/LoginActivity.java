package com.jw.iii.pocketjw.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.jw.iii.pocketjw.Activity.MainActivity;
import com.jw.iii.pocketjw.Activity.News.NewsActivity;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.CircularImage;

import java.util.List;


public class LoginActivity extends IIIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CircularImage gravatar = (CircularImage)findViewById(R.id.gravatar);
        gravatar.setImageResource(R.drawable.gravatar);

        gravatar = (CircularImage)findViewById(R.id.gravatar);
        loginButton = (Button)findViewById(R.id.loginButton);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        registerTextView = (TextView)findViewById(R.id.registerTextView);
        forgetPasswordTextView = (TextView)findViewById(R.id.forgetPasswordTextView);
        tourTextView = (TextView)findViewById(R.id.tourTextView);

        // TODO: Finish Register and Tour function
        /*if (getUserId() != null) {
            Intent mainIntent = new Intent(activity, MainActivity.class);
            startActivity(mainIntent);
            activity.finish();
        }*/

        loginButton.setOnClickListener(loginListener);
        registerTextView.setOnClickListener(registerListener);
        forgetPasswordTextView.setOnClickListener(forgetPasswordListener);
        tourTextView.setOnClickListener(tourListener);
    }

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.isEmpty()) {
                makeToast("用户名不可为空");
            } else if (password.isEmpty()) {
                makeToast("密码不可为空");
            } else {
                AVUser.logInInBackground(username, password,
                        new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e != null) {
                                    makeToast(e.getMessage());
                                } else {
                                    if (avUser != null) {
                                        Intent mainIntent = new Intent(activity, MainActivity.class);
                                        startActivity(mainIntent);
                                        activity.finish();
                                    } else {
                                        makeToast("用户名密码错误");
                                    }
                                }
                            }
                        });
            }
        }
    };

    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent registerIntent = new Intent(activity, RegisterActivity.class);
            startActivity(registerIntent);
        }
    };

    View.OnClickListener forgetPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent tourIntent = new Intent(activity, ForgetPasswordActivity.class);
            startActivity(tourIntent);
        }
    };

    View.OnClickListener tourListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent tourIntent = new Intent(activity, NewsActivity.class);
            startActivity(tourIntent);
        }
    };

    private Button loginButton;
    private EditText usernameEditText, passwordEditText;
    private TextView registerTextView, forgetPasswordTextView, tourTextView;
}
