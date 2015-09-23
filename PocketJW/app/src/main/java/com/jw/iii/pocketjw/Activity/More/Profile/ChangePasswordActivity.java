package com.jw.iii.pocketjw.Activity.More.Profile;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;

public class ChangePasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        oldPasswordEditText = (EditText)findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText)findViewById(R.id.newPasswordEditText);
        repeatNewPasswordEditText = (EditText)findViewById(R.id.repeatNewPasswordEditText);
        changePasswordButton = (Button)findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(changePasswordListener);
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

    View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String repeatNewPassword = repeatNewPasswordEditText.getText().toString();

            if (oldPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入旧密码", Toast.LENGTH_SHORT).show();
            } else if (newPassword.isEmpty() || repeatNewPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(repeatNewPassword)) {
                Toast.makeText(getApplicationContext(), "两次新密码输入不一致", Toast.LENGTH_SHORT).show();
            } else {
                AVUser user = Utils.getCurrentUser();
                user.updatePasswordInBackground(oldPassword, newPassword, new UpdatePasswordCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
    };

    private EditText oldPasswordEditText, newPasswordEditText, repeatNewPasswordEditText;
    private Button changePasswordButton;
}
