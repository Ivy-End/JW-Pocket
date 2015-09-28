package com.jw.iii.pocketjw.Activity.Problems;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jw.iii.pocketjw.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_image_display);

        Intent imageDisplayIntent = getIntent();
        index = imageDisplayIntent.getIntExtra("index", 0);
        images = imageDisplayIntent.getStringArrayListExtra("images");

        imageViewPager = (ViewPager)findViewById(R.id.imageViewPager);

        refreshView();
    }

    private void refreshView() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(ImageDisplayActivity.this);
                try {
                    File file = new File(images.get(position));
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ImageDisplayActivity.this.getContentResolver(), uri);
                    int width, height;
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        width = 720;
                        height = 1280;
                    } else {
                        width = 1280;
                        height = 720;
                    }
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setImageBitmap(bitmap);
                    imageView.setOnClickListener(imageViewListener);
                    container.addView(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        imageViewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener imageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private int index;
    private ArrayList<String> images;
    private ViewPager imageViewPager;
    private PagerAdapter adapter;
}
