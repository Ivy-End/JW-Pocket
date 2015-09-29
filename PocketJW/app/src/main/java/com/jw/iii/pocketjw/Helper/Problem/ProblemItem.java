package com.jw.iii.pocketjw.Helper.Problem;

import com.avos.avoscloud.AVUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by End on 2015/9/28.
 */
public class ProblemItem {

    public ProblemItem(String problemID, String problemTitle, String problemDesc,
                       String problemView, String problemComment, AVUser problemPublisher,
                       String problemImages) {
        this.problemID = problemID;
        this.problemTitle = problemTitle;
        this.problemDesc = problemDesc;
        this.problemView = problemView;
        this.problemComment = problemComment;
        this.problemPublisher = problemPublisher;

        this.problemImages = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(problemImages);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject)jsonArray.opt(i);
                this.problemImages.add(object.get("objectId").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProblemID() {
        return problemID;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public String getProblemView() {
        return problemView;
    }

    public String getProblemComment() {
        return problemComment;
    }

    public AVUser getProblemPublisher() {
        return problemPublisher;
    }

    public ArrayList<String> getProblemImages() {
        return problemImages;
    }

    private String problemID;
    private String problemTitle;
    private String problemDesc;
    private String problemView;
    private String problemComment;
    private AVUser problemPublisher;
    private ArrayList<String> problemImages;
}
