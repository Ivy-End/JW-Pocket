package com.jw.iii.pocketjw.Activity.Problems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avospush.session.SessionControlPacket;
import com.jw.iii.pocketjw.Helper.Utils;
import com.jw.iii.pocketjw.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AddProblemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = (EditText)findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        imageLinearLayout = (LinearLayout)findViewById(R.id.imageLinearLayout);

        images = new ArrayList<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_problem, menu);
        return true;
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
            case R.id.action_menu_add_finish:
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入问题标题", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        AVObject object = new AVObject("Problem");
                        object.put("title", title);
                        object.put("desc", description);
                        object.put("publisher", Utils.getCurrentUser());
                        List<AVFile> fileList = new LinkedList<>();

                        for (String path : images) {
                            AVFile file = AVFile.withAbsoluteLocalPath(Utils.getCurrentUser().getUsername() + "-" + getTitle(path), path);
                            file.saveInBackground();
                            fileList.add(file);
                        }
                        object.addAll("images", fileList);
                        object.saveInBackground();
                        Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            case R.id.action_menu_add_image:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProblemActivity.this);
                builder.setTitle("选择图片");
                final String[] source = { "拍照", "相册" };
                builder.setItems(source, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (Utils.hasSDCard()) {
                                    File path = new File(SDCARD_PATH_TMP);
                                    path.mkdirs();
                                    IMAGE_FILENAME_TMP = "problem_" + getTimeStamp() + "_tmp.jpg";
                                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_PATH_TMP, IMAGE_FILENAME_TMP)));
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                addImage(getRealPathFromURI(data.getData()));
                break;
            case CODE_CAMERA_REQUEST:
                if (Utils.hasSDCard()) {
                    File file = new File(SDCARD_PATH_TMP, IMAGE_FILENAME_TMP);
                    addImage(file.getPath());
                    deleteImage();
                } else {
                    Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void addImage(String oldPath) {
        try {
            IMAGE_FILENAME = "problem_" + getTimeStamp() + ".jpg";
            File path = new File(SDCARD_PATH_PROBLEM);
            path.mkdirs();
            File oldFile = new File(oldPath);
            Uri  oldUri = Uri.fromFile(oldFile);
            //Bitmap bitmap = Utils.getScaleBitmap(oldPath);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), oldUri);
            int width, height;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                width = 640;
                height = 480;
            } else {
                width = 480;
                height = 640;
            }
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            saveIamge(bitmap, SDCARD_PATH_PROBLEM + "/" + IMAGE_FILENAME);
            File file = new File(SDCARD_PATH_PROBLEM + "/" + IMAGE_FILENAME);
            Uri  uri = Uri.fromFile(file);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            displayImage(bitmap);
            images.add(SDCARD_PATH_PROBLEM + "/" + IMAGE_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayImage(Bitmap bitmap) {
        int width, height;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            width = 240;
            height = 180;
        } else {
            width = 180;
            height = 240;
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        ImageView imageView = new ImageView(this);
        imageView.setContentDescription(String.valueOf(images.size()));
        imageView.setOnClickListener(ImageViewListener);
        imageView.setOnLongClickListener(ImageViewLongListener);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240, 240);
        layoutParams.setMargins(8, 8, 8, 8);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageLinearLayout.addView(imageView, layoutParams);
    }

    private void saveIamge(Bitmap bitmap, String filePath) {
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

    private void deleteImage() {
        String path = SDCARD_PATH_TMP + "/" + IMAGE_FILENAME_TMP;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private String getTitle(String path) {
        int pos = path.lastIndexOf("/");
        path = path.substring(pos + 1, path.length());
        path = path.substring(0, path.length() - 4);
        return path;
    }

    private String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return format.format(new Date());
    }

    View.OnClickListener ImageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = Integer.valueOf(v.getContentDescription().toString());
            Intent imageDisplayIntent = new Intent(AddProblemActivity.this, ImageDisplayActivity.class);
            imageDisplayIntent.putExtra("index", index);
            imageDisplayIntent.putExtra("images", images);
            startActivity(imageDisplayIntent);
        }
    };

    View.OnLongClickListener ImageViewLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            images.remove((int) Integer.valueOf(v.getContentDescription().toString()));
            imageLinearLayout.removeView(v);
            return true;
        }
    };

    private EditText titleEditText, descriptionEditText;
    private LinearLayout imageLinearLayout;

    private ArrayList<String> images;

    private static final String SDCARD_PATH_TMP = "/sdcard/PocketJW/Tmp";
    private static final String SDCARD_PATH_PROBLEM = "/sdcard/PocketJW/Problem";
    private String IMAGE_FILENAME_TMP, IMAGE_FILENAME;

    private static final int CODE_GALLERY_REQUEST = 0;
    private static final int CODE_CAMERA_REQUEST = 1;
}
