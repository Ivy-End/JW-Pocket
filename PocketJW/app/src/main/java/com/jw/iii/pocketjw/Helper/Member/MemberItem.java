package com.jw.iii.pocketjw.Helper.Member;

/**
 * Created by End on 2015/10/1.
 */
public class MemberItem {

    public MemberItem(String objectId, String imgUrl, String name, String username) {
        this.objectId = objectId;
        this.imgUrl = imgUrl;
        this.name = name;
        this.username = username;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    private String objectId;
    private String imgUrl;
    private String name;
    private String username;
}