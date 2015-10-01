package com.jw.iii.pocketjw.Activity.Problems;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent commentItemIntent = getIntent();
        Bundle bundle = commentItemIntent.getExtras();
        problemTitle = bundle.get("problemTitle").toString();
        comment = bundle.get("comment").toString();
        commentID = bundle.get("commentID").toString();
        commentApproval = bundle.get("commentApproval").toString();
        commentPublisherUrl = bundle.get("commentPublisherUrl").toString();
        commentPublisherName = bundle.get("commentPublisherName").toString();
        commentImages = (ArrayList<String>)bundle.get("commentImages");

        initView();

        initImages();
    }

    private void initView() {
        commentPublisherImageView = (ImageView)findViewById(R.id.commentPublisherImageView);
        commentPublisherTextView = (TextView)findViewById(R.id.commentPublisherTextView);
        commentTextView = (TextView)findViewById(R.id.commentTextView);
        commentApprovalLinearLayout = (LinearLayout)findViewById(R.id.commentApprovalLinearLayout);
        commentApprovalTextView = (TextView)findViewById(R.id.commentApprovalTextView);
        commentApprovalLinearLayout = (LinearLayout)findViewById(R.id.commentApprovalLinearLayout);
        commentImagesLinearLayout = (LinearLayout)findViewById(R.id.commentImageLinearLayout);

        setTitle(problemTitle);
        ImageLoader.getInstance().displayImage(commentPublisherUrl, commentPublisherImageView);
        commentPublisherTextView.setText(commentPublisherName);
        commentApprovalTextView.setText(commentApproval);
        commentTextView.setText(comment);

        commentApprovalLinearLayout.setOnClickListener(commentApprovalListener);
    }

    private void initImages() {
        images = new ArrayList<>();

        for (final String imageName : commentImages) {
            AVQuery<AVObject> query = new AVQuery<>("_File");
            query.whereEqualTo("name", imageName);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> avObjects, AVException e) {
                    if (e == null && !avObjects.isEmpty()) {
                        final AVObject object = avObjects.get(0);
                        final String url = object.get("url").toString();
                        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                if (bitmap != null) {
                                    saveImage(bitmap, SDCARD_PATH_PROBLEM + "/" + imageName + ".jpeg");
                                    images.add(SDCARD_PATH_PROBLEM + "/" + imageName + ".jpeg");
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setOnClickListener(imageViewListener);
                                    imageView.setContentDescription(String.valueOf(images.size()));
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(240, 240);
                                    layoutParams.setMargins(8, 8, 8, 8);
                                    imageView.setImageBitmap(bitmap);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    commentImagesLinearLayout.addView(imageView);
                                }
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    }
                }
            });
        }
    }

    private void saveImage(Bitmap bitmap, String filePath) {
        BufferedOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            File dirFile = new File(filePath.substring(0, filePath.lastIndexOf('/')));
            dirFile.mkdirs();
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
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener commentApprovalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AVQuery<AVObject> query = new AVQuery<>("Comment");
            query.getInBackground(commentID, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        avObject.put("approval", (int)avObject.get("approval") + 1);
                        avObject.saveInBackground();
                    }
                }
            });
        };
    };

    View.OnClickListener imageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = Integer.valueOf(v.getContentDescription().toString());
            Intent imageDisplayIntent = new Intent(CommentItemActivity.this, ImageDisplayActivity.class);
            imageDisplayIntent.putExtra("index", index);
            imageDisplayIntent.putExtra("images", images);
            startActivity(imageDisplayIntent);
        }
    };

    private String problemTitle, commentID;
    private String comment, commentApproval;
    private String commentPublisherUrl, commentPublisherName;
    private ArrayList<String> commentImages, images;

    private static final String SDCARD_PATH_PROBLEM = "/sdcard/PocketJW/Problem";

    private ImageView commentPublisherImageView;
    private TextView commentPublisherTextView, commentTextView, commentApprovalTextView;
    private LinearLayout commentApprovalLinearLayout, commentImagesLinearLayout;
}
