package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by Lee on 2016/9/3.
 */
public class Vote {
    private String voteID;
    private String voteTitle;
    private String voteQuestion;
    private List<String> voteOptionsList;
    private int voteNumberNeeded;

    public String getVoteID() {
        return voteID;
    }

    public String getVoteQuestion() {
        return voteQuestion;
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

    public void setVoteID(String voteID) {
        this.voteID = voteID;
    }

    public void setVoteQuestion(String voteQuestion) {
        this.voteQuestion = voteQuestion;
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
