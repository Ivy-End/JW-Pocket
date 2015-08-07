package com.jw.iii.pocketjw.DataAdapter;

import android.media.Image;
import android.text.Html;

/**
 * Created by End on 2015/6/20.
 */
public class News {

    public Image getThumb() {
        return thumb;
    }

    public String getTitle() {
        return  title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public void setThumb(Image image) {
        thumb = image;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void setDescription(String s) {
        description = s;
    }

    public void setContent(String s) {
        content = s;
    }

    public Image thumb;
    public String title;
    public String description;
    public String content;
}
