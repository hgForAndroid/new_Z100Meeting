package com.gzz100.Z100_HuiYi.meeting.vote;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XieQXiong on 2016/11/7.
 */

public class UpLoadVote implements Serializable{
    private int voteID;
    private int userID;
    private String IMEI;
    private List<Integer> resultMap;

    public int getVoteID() {
        return voteID;
    }

    public void setVoteID(int voteID) {
        this.voteID = voteID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public List<Integer> getResultMap() {
        return resultMap;
    }

    public void setResultMap(List<Integer> resultMap) {
        this.resultMap = resultMap;
    }
}
