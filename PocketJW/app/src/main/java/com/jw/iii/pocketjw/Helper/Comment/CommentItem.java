package com.jw.iii.pocketjw.Helper.Comment;

import com.avos.avoscloud.AVUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by End on 2015/9/29.
 */
public class CommentItem {

    public CommentItem(String comment, String commentApproval,
                       AVUser commentPublisher, String commentImages) {

        this.comment = comment;
        this.commentApproval = commentApproval;
        this.commentPublisher = commentPublisher;

        this.commentImages = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(commentImages);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject)jsonArray.opt(i);
                this.commentImages.add(object.get("objectId").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getComment() {
        return comment;
    }

    public String getCommentApproval() {
        return commentApproval;
    }

    public AVUser getCommentPublisher() {
        return commentPublisher;
    }

    public ArrayList<String> getCommentImages() {
        return commentImages;
    }

    private String comment;
    private String commentApproval;
    private AVUser commentPublisher;
    private ArrayList<String> commentImages;
}
