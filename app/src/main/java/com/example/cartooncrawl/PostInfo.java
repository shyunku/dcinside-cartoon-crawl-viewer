package com.example.cartooncrawl;

import java.io.Serializable;

public class PostInfo implements Serializable {
    private int postId = 0;
    private String postTitle = "-";
    private String postTime = "-";
    private int watchedNum = 0;
    private int recommendNum = 0;
    private int replyNum = 0;

    public PostInfo(int postId, String postTitle, String postTime, int watchedNum, int recommendNum, int replyNum) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postTime = postTime;
        this.watchedNum = watchedNum;
        this.recommendNum = recommendNum;
        this.replyNum = replyNum;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public int getWatchedNum() {
        return watchedNum;
    }

    public void setWatchedNum(int watchedNum) {
        this.watchedNum = watchedNum;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(int recommendNum) {
        this.recommendNum = recommendNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }
}
