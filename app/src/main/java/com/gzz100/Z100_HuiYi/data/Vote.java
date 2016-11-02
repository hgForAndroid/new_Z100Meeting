package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by Lee on 2016/9/3.
 */
public class Vote {
    private int voteID;
    private String voteTitle;
    private String voteContent;
    private List<String> voteOptionsList;
    private int voteNumberNeeded;

    public int getVoteID() {
        return voteID;
    }

    public String getVoteContent() {
        return voteContent;
    }

    public List<String> getVoteOptionsList() {
        return voteOptionsList;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public int getVoteNumberNeeded() {
        return voteNumberNeeded;
    }

    public void setVoteID(int voteID) {
        this.voteID = voteID;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }

    public void setVoteOptionsList(List<String> voteOptionsList) {
        this.voteOptionsList = voteOptionsList;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public void setVoteNumberNeeded(int voteNumberNeeded) {
        this.voteNumberNeeded = voteNumberNeeded;
    }
}
