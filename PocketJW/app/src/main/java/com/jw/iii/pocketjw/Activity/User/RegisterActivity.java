package com.jw.iii.pocketjw.Activity.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.jw.iii.pocketjw.Activity.MainActivity;
import com.jw.iii.pocketjw.Helper.AVService;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends IIIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        repeatPasswordEditText = (EditText)findViewById(R.id.repeatPasswordEditText);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        studentIDEditText = (EditText)findViewById(R.id.studentIDEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(registerListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                activity.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static void saveIamge(Bitmap bitmap, String filePath) {
        BufferedOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            file.createNewFile();
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String repeatPassword = repeatPasswordEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String studentID = studentIDEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            if (username.isEmpty()) {
                makeToast("用户名不可为空");
            } else if (password.isEmpty() || repeatPassword.isEmpty()) {
                makeToast("密码不可为空");
            } else if (!password.equals(repeatPassword)) {
                makeToast("两次密码输入不一致");
            } else if (name.isEmpty()) {
                makeToast("姓名不可为空");
            } else if (studentID.isEmpty()) {
                makeToast("学号不可为空");
            } else if(email.isEmpty()) {
                makeToast("电子邮箱不可为空");
            } else if (phone.isEmpty()) {
                makeToast("手机号不可为空");
            } else {
                SignUpCallback signUpCallback = new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            makeToast(e.getMessage());
                        } else {
                            makeToast("注册成功");
                            Intent mainIntent = new Intent(activity, MainActivity.class);
                            startActivity(mainIntent);
                            activity.finish();
                        }
                    }
                };

                AVService.signUp(username, password, name, studentID, email, phone, signUpCallback, getApplicationContext());
            }
        }
    };

    private static final String SDCARD_PATH_TMP = "/sdcard/PocketJW/Tmp";

    EditText usernameEditText, passwordEditText, repeatPasswordEditText;
    EditText nameEditText, studentIDEditText, emailEditText, phoneEditText;
    Button registerButton;
}
