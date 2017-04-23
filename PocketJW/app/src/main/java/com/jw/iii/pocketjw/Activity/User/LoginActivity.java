package com.jw.iii.pocketjw.Activity.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.jw.iii.pocketjw.Activity.MainActivity;
import com.jw.iii.pocketjw.Activity.News.NewsActivity;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;


public class LoginActivity extends IIIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gravatar = (CircularImage)findViewById(R.id.gravatar);
        gravatar.setImageResource(R.drawable.ic_launcher);

        loginButton = (Button)findViewById(R.id.loginButton);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        registerTextView = (TextView)findViewById(R.id.registerTextView);
        forgetPasswordTextView = (TextView)findViewById(R.id.forgetPasswordTextView);
        tourTextView = (TextView)findViewById(R.id.tourTextView);

        if (Utils.getCurrentUser() != null) {
            Intent mainIntent = new Intent(activity, MainActivity.class);
            startActivity(mainIntent);
            activity.finish();
        }

        loginButton.setOnClickListener(loginListener);
        usernameEditText.setOnFocusChangeListener(usernameListener);
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

    View.OnFocusChangeListener usernameListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String username = usernameEditText.getText().toString();
                if (!username.isEmpty()) {
                    AVQuery<AVUser> query = AVUser.getQuery();
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> avUsers, AVException e) {
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                if (!avUsers.isEmpty()) {
                                    AVUser user = avUsers.get(0);
                                    AVFile file = user.getAVFile("gravatar");
                                    if (file != null) {
                                        ImageLoader.getInstance().loadImage(file.getUrl(), new ImageLoadingListener() {
                                            @Override
                                            public void onLoadingStarted(String s, View view) {

                                            }

                                            @Override
                                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                                gravatar.setImageResource(R.drawable.ic_launcher);
                                            }

                                            @Override
                                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                                gravatar.setImageBitmap(bitmap);
                                            }

                                            @Override
                                            public void onLoadingCancelled(String s, View view) {
                                                gravatar.setImageResource(R.drawable.ic_launcher);
                                            }
                                        });
                                    } else {
                                        gravatar.setImageResource(R.drawable.ic_launcher);
                                    }
                                }
                            }
                        }
                    });
                }
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

    CircularImage gravatar;
    private Button loginButton;
    private EditText usernameEditText, passwordEditText;
    private TextView registerTextView, forgetPasswordTextView, tourTextView;
}
