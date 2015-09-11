package com.jw.iii.pocketjw.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;

import com.jw.iii.pocketjw.R;

/**
 * Created by End on 2015/9/8.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;
    @SuppressWarnings("deprecation")
    public URLDrawable(Context context) {
        this.setBounds(getDefaultImageBounds(context));
        drawable = context.getResources().getDrawable(R.drawable.img_loading_failure);
        drawable.setBounds(getDefaultImageBounds(context));
    }
    @Override
    public void draw(Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @SuppressWarnings("deprecation")
    public Rect getDefaultImageBounds(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 9 / 10;
        int height = width * 3 / 4;
        return new Rect(0, 0, width, height);
    }
}