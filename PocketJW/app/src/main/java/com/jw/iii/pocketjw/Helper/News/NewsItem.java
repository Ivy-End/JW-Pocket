package com.jw.iii.pocketjw.Helper.News;

/**
 * Created by End on 2015/9/13.
 */
public class NewsItem {

    public NewsItem(String imgUrl, String title, String date, String content) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    String imgUrl;
    String title;
    String date;
    String content;
}
