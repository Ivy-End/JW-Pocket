package com.jw.iii.pocketjw.Helper.News;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by End on 2015/9/13.
 */
public class NewsItemAdapter extends BaseAdapter {

    public NewsItemAdapter(ArrayList<NewsItem> arrayList, Context context, ImageLoader imageLoader) {
        this.arrayList = arrayList;
        this.context = context;
        this.imageLoader = imageLoader;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoadingListenerImp = new ImageLoadingListenerImp();
    }

    @Override
    public int getCount() {
        if (arrayList == null) {
            return 0;
        } else {
            return arrayList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (arrayList == null) {
            return null;
        } else {
            return arrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listviewitem_news, null, false);
            holder.imgImageView = (ImageView)convertView.findViewById(R.id.imgImageView);
            holder.titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
            holder.dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (this.arrayList != null) {
            NewsItem newsItem = arrayList.get(position);
            if (holder.titleTextView != null) {
                holder.titleTextView.setText(newsItem.getTitle());
            }
            if (holder.dateTextView != null) {
                holder.dateTextView.setText(newsItem.getDate());
            }
            if (holder.imgImageView != null) {
                try {
                    imageLoader.displayImage(newsItem.getImgUrl(), holder.imgImageView, displayImageOptions, imageLoadingListenerImp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return convertView;
    }

    public static class ImageLoadingListenerImp extends SimpleImageLoadingListener {

        public static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView)view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private class ViewHolder {
        ImageView imgImageView;
        TextView titleTextView;
        TextView dateTextView;
    }

    private ArrayList<NewsItem> arrayList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListenerImp imageLoadingListenerImp;
}
