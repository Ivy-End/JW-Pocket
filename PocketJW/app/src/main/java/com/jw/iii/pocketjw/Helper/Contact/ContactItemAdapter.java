package com.jw.iii.pocketjw.Helper.Contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by End on 2015/9/13.
 */
public class ContactItemAdapter extends BaseAdapter {

    public ContactItemAdapter(ArrayList<ContactItem> arrayList, Context context, ImageLoader imageLoader) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listviewitem_contact, null, false);
            holder.imgImageView = (ImageView)convertView.findViewById(R.id.imgImageView);
            holder.nameTextView = (TextView)convertView.findViewById(R.id.nameTextView);
            holder.phoneTextView = (TextView)convertView.findViewById(R.id.phoneTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (this.arrayList != null) {
            ContactItem contactItem = arrayList.get(position);
            if (holder.nameTextView != null) {
                holder.nameTextView.setText(contactItem.getName());
            }
            if (holder.phoneTextView != null) {
                holder.phoneTextView.setText(contactItem.getName());
            }
            if (holder.imgImageView != null) {
                if (contactItem.getImgUrl() != "") {
                    try {
                        imageLoader.displayImage(contactItem.getImgUrl(), holder.imgImageView, displayImageOptions, imageLoadingListenerImp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.imgImageView.setImageResource(R.drawable.ic_launcher);
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
        TextView nameTextView;
        TextView phoneTextView;
    }

    private ArrayList<ContactItem> arrayList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListenerImp imageLoadingListenerImp;
}
