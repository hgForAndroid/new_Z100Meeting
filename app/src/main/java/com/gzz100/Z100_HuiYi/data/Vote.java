package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by Lee on 2016/9/3.
 */
public class Vote extends BaseBean{
    private int voteID;
    private String voteTitle;
    private String voteContent;
    private List<Option> voteOptionsList;
    private int voteNumberNeeded;
    private int voteState; //0代表未投，1代表已投

    public int getVoteID() {
        return voteID;
    }

    public String getVoteContent() {
        return voteContent;
    }

    public List<Option> getVoteOptionsList() {
        return voteOptionsList;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public int getVoteNumberNeeded() {
        return voteNumberNeeded;
    }

    public int getVoteState() {
        return voteState;
    }

    public void setVoteID(int voteID) {
        this.voteID = voteID;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }

    public void setVoteOptionsList(List<Option> voteOptionsList) {
        this.voteOptionsList = voteOptionsList;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public void setVoteNumberNeeded(int voteNumberNeeded) {
        this.voteNumberNeeded = voteNumberNeeded;
    }

    public void setVoteState(int voteState) {
        this.voteState = voteState;
    }
}
