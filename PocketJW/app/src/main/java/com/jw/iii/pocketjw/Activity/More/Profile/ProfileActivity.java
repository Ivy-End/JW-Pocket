package com.jw.iii.pocketjw.Activity.More.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        gravatar = (ImageView)findViewById(R.id.gravatar);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        syncImageView = (ImageView)findViewById(R.id.syncImageView);
        changePasswordButton = (Button)findViewById(R.id.changePasswordButton);

        nameEditText.setOnFocusChangeListener(nameListener);
        syncImageView.setOnClickListener(syncListener);
        phoneEditText.setOnFocusChangeListener(phoneListener);
        changePasswordButton.setOnClickListener(changePasswordListener);

        gravatar.setOnClickListener(gravatarListener);

        initData();
    }

    private void initData() {
        ImageLoader.getInstance().loadImage(Utils.getCurrentUser().getAVFile("gravatar").getUrl(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                gravatarBitmap = bitmap;
                gravatar.setImageBitmap(gravatarBitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        AVUser user = Utils.getCurrentUser();
        nameEditText.setText(user.get("name").toString());
        phoneEditText.setText(user.get("mobilePhoneNumber").toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (Utils.hasSDCard()) {
                    File file = new File(SDCARD_PATH, IMAGE_FILENAME_TMP);
                    cropRawPhoto(Uri.fromFile(file));
                } else {
                    Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        gravatarBitmap = extras.getParcelable("data");
                        saveImage(gravatarBitmap);
                        uploadGravatar();
                        gravatar.setImageBitmap(gravatarBitmap);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void cropRawPhoto(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", OUTPUT_WIDTH);
        cropIntent.putExtra("outputY", OUTPUT_HEIGHT);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("scaleUpIfNeed", true);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CODE_RESULT_REQUEST);
    }

    private void saveImage(Bitmap bitmap) {
        if(Utils.hasSDCard()) {
            FileOutputStream fileOutputStream = null;
            File file = new File(SDCARD_PATH);
            file.mkdirs();
            String filename = SDCARD_PATH + IMAGE_FILENAME;
            try {
                fileOutputStream = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadGravatar() {
        try {
            AVUser user = Utils.getCurrentUser();
            AVFile newGravatar = AVFile.withAbsoluteLocalPath("gravatar-" + user.get("studentID").toString(), SDCARD_PATH + IMAGE_FILENAME);
            AVFile oldGravatar = user.getAVFile("gravatar");
            user.put("gravatar", newGravatar);
            user.saveInBackground();
            oldGravatar.deleteInBackground();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "头像更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener gravatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("选择头像");
            final String[] source = { "拍照", "相册" };
            builder.setItems(source, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Utils.hasSDCard()) {
                                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_PATH, IMAGE_FILENAME_TMP)));
                                startActivityForResult(captureIntent, CODE_CAMERA_REQUEST);
                            } else {
                                Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(albumIntent, CODE_GALLERY_REQUEST);
                            break;
                    }
                }
            });
            builder.show();
        }
    };

    View.OnFocusChangeListener nameListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                AVUser user = Utils.getCurrentUser();
                if (!user.get("name").toString().equals(nameEditText.getText().toString())) {
                    user.put("name", nameEditText.getText().toString());
                    user.saveInBackground();
                }
            }
        }
    };

    View.OnFocusChangeListener phoneListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                AVUser user = Utils.getCurrentUser();
                if (!user.get("mobilePhoneNumber").toString().equals(phoneEditText.getText().toString())) {
                    user.put("mobilePhoneNumber", phoneEditText.getText().toString());
                    user.saveInBackground();
                }
            }
        }
    };

    View.OnClickListener syncListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            phoneEditText.setText(telephonyManager.getLine1Number());

            AVUser user = Utils.getCurrentUser();
            if (!user.get("mobilePhoneNumber").toString().equals(phoneEditText.getText())) {
                user.put("mobilePhoneNumber", phoneEditText.getText());
                user.saveInBackground();
            }
        }
    };

    View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent changePasswordIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(changePasswordIntent);
        }
    };

    private ImageView gravatar;
    private EditText nameEditText, phoneEditText;
    private ImageView syncImageView;
    private Button changePasswordButton;

    private Bitmap gravatarBitmap;

    private static final String SDCARD_PATH = "/sdcard/PocketJW/";
    private static final String IMAGE_FILENAME_TMP = "head_image_tmp.jpg";
    private static final String IMAGE_FILENAME = "head_image.jpg";

    private static final int CODE_GALLERY_REQUEST = 0;
    private static final int CODE_CAMERA_REQUEST = 1;
    private static final int CODE_RESULT_REQUEST = 2;

    private static int OUTPUT_WIDTH = 128;
    private static int OUTPUT_HEIGHT = 128;
}
