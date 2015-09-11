package com.jw.iii.pocketjw.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by End on 2015/9/9.
 */
public class URLImageGetter implements Html.ImageGetter {
    private final String BASE_URL = "http://jwsy.suda.edu.cn";
    TextView textView;
    Context context;
    public URLImageGetter(Context contxt, TextView textView) {
        this.context = contxt;
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String paramString) {
        URLDrawable urlDrawable = new URLDrawable(context);

        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(BASE_URL + paramString);
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;

                URLImageGetter.this.textView.requestLayout();
            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        public Drawable fetchDrawable(String url) {
            Drawable drawable = null;
            URL Url;
            try {
                Url = new URL(url);
                drawable = Drawable.createFromStream(Url.openStream(), "");
            } catch (Exception e) {
                return null;
            }
            //按比例缩放图片
            Rect bounds = getDefaultImageBounds(context);
            int newWidth = bounds.width();
            int newHeight = bounds.height();
            drawable.setBounds(0, 0, newWidth, newHeight);
            return drawable;
        }
    }

    //预定图片宽高比例为 4:3
    @SuppressWarnings("deprecation")
    public Rect getDefaultImageBounds(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 9 / 10;
        int height = width * 3 / 4;
        return new Rect(0, 0, width, height);
    }
}