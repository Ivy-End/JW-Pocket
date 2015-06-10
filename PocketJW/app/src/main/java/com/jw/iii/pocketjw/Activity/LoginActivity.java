package com.jw.iii.pocketjw.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogInCallback;
import com.jw.iii.pocketjw.Helper;
import com.jw.iii.pocketjw.IIIApplication;
import com.jw.iii.pocketjw.UI.CircularImage;
import com.jw.iii.pocketjw.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化IIIApplication
        iiiApplication = (IIIApplication)getApplication();

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
        Button btLogin = (Button) findViewById(R.id.login);
        TextView tvLoginNews = (TextView) findViewById(R.id.loginNews);

        // 初始化
        if (loginUsername != "") {
            // 自动填充用户名
            etUsername.setText(loginUsername);
        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Bitmap bmp = Helper.getLocalBitmap(baseDir + File.pathSeparator + "face.png");
        if (bmp != null) {
            avatar.setImageBitmap(bmp);
        } else {
            avatar.setImageResource(R.drawable.default_avatar);
        }

        // 监听事件
        btLogin.setOnClickListener(this);
        tvLoginNews.setOnClickListener(this);

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 获取face图像
                    loginUsername = etUsername.getText().toString();
                    AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
                    query.whereEqualTo("username", loginUsername);
                    query.findInBackground(new FindCallback<AVObject>() {
                        public void done(List<AVObject> avObjects, AVException e) {
                            if (e == null && avObjects.size() > 0) {
                                AVObject avObject = avObjects.get(0);
                                AVFile avFile = avObject.getAVFile("avatar");
                                avFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, AVException e) {
                                        if(e == null && bytes.length > 0) {
                                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                                            File faceFile = new File(baseDir + File.pathSeparator + "face.png");
                                            FileOutputStream bmpWriter = null;
                                            try {
                                                bmpWriter = new FileOutputStream(faceFile);
                                            } catch (FileNotFoundException ex) {
                                                ex.printStackTrace();
                                            }
                                            bmp.compress(Bitmap.CompressFormat.PNG, 100, bmpWriter);
                                            avatar.setImageBitmap(bmp);
                                        }
                                    }
                                });
                            } else {
                                avatar.setImageResource(R.drawable.default_avatar);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loginUsername = etUsername.getText().toString();
                loginPassword = Helper.md5(etPassword.getText().toString());
                login();
                break;
            case R.id.loginNews:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void login() {
        if (loginQuery()) {

            // 保存登陆信息
            // TODO: 测试完毕后删除下面第4行注释符号
            SharedPreferences preferences = getSharedPreferences("jw_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lastLoginUsername", loginUsername);
            // editor.putString("lastLoginPassword", loginPassword);
            editor.apply();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                }
            });
        }
    }

    private boolean loginQuery() {
        AVUser.logInInBackground(loginUsername, loginPassword, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (avUser != null) {
                    iiiApplication.currentUser = AVUser.getCurrentUser();
                    loginStatus = true;
                } else {
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

    private String loginUsername;
    private String loginPassword;

    private boolean loginStatus;

    private Intent intent;

    private IIIApplication iiiApplication;
}
