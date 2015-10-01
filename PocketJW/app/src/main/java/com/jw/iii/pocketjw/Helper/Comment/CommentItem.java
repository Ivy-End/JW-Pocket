package com.jw.iii.pocketjw.Helper.Comment;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by End on 2015/9/29.
 */
public class CommentItem {

    public CommentItem(String comment, String commentID, String commentApproval,
                       AVUser commentPublisher, String commentImages) {

        this.comment = comment;
        this.commentID = commentID;
        this.commentApproval = commentApproval;
        this.commentPublisher = commentPublisher;

        loadPublisherInfo();

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

    private void loadPublisherInfo() {
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.getInBackground(commentPublisher.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    commentPublisherUrl = avObject.getAVFile("gravatar").getUrl();
                    commentPublisherName = avObject.get("name").toString();
                }
            }
        });
    }

    public String getComment() {
        return comment;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentApproval() {
        return commentApproval;
    }

    public String getCommentPublisherName() {
        return commentPublisherName;
    }

    public String getCommentPublisherUrl() {
        return commentPublisherUrl;
    }

    public ArrayList<String> getCommentImages() {
        return commentImages;
    }

    private String comment;
    private String commentID;
    private String commentApproval;
    private AVUser commentPublisher;
    private String commentPublisherUrl;
    private String commentPublisherName;
    private ArrayList<String> commentImages;
}
