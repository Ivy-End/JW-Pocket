package com.jw.iii.pocketjw.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.jw.iii.pocketjw.Helper.AVService;
import com.jw.iii.pocketjw.R;

public class ForgetPasswordActivity extends IIIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        forgetPasswordButton = (Button)findViewById(R.id.forgetPasswordButton);
        forgetPasswordButton.setOnClickListener(forgetPasswordListener);
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

    View.OnClickListener forgetPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = emailEditText.getText().toString();
            if (email.isEmpty()) {
                makeToast("电子邮件不可为空");
            } else {
                RequestPasswordResetCallback requestPasswordResetCallback = new RequestPasswordResetCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            makeToast(e.getMessage());
                        } else {
                            makeToast("电子邮件已发送");
                            Intent loginIntent = new Intent(activity, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }
                };

                AVService.requestResetPassword(email, requestPasswordResetCallback);
            }
        }
    };

    EditText emailEditText;
    Button forgetPasswordButton;
}
