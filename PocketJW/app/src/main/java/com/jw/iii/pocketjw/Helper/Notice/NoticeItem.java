package com.jw.iii.pocketjw.Helper.Notice;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

/**
 * Created by End on 2015/10/2.
 */
public class NoticeItem {

    public NoticeItem(String objectId, String fromUser, String toUser,
               String content, String postDate, String status, boolean isSender) {
        this.objectId = objectId;

        try {
            AVQuery<AVObject> query = new AVQuery<>("_User");
            AVObject object = query.get(fromUser);
            fromUserName = object.get("name").toString();
            fromUserUrl = object.getAVFile("gravatar").getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.toUser = toUser;
        this.content = content;
        this.postDate = postDate;
        this.status = status;
        this.isSender = isSender;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getFromUserUrl() {
        return fromUserUrl;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public String getToUser() {
        return toUser;
    }

    public String getContent() {
        return content;
    }

    public String getContentSub() {
        if (content.length() < 50) {
            return content;
        } else {
            return content.substring(0, 50) + "...";
        }
    }

    public String getPostDate() {
        return postDate;
    }

    public String getStatus() {
        return status;
    }

    public boolean getIsSender() {
        return isSender;
    }

    private String objectId;
    private String fromUserUrl;
    private String fromUserName;
    private String toUser;
    private String status;
    private String content;
    private String postDate;
    private boolean isSender;
}
