package com.gzz100.Z100_HuiYi.multicast;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Closeable;
import java.io.Serializable;

/**
 * Created by XieQXiong on 2016/10/1.
 */
public class MulticaseBean implements Serializable,Cloneable{
    private String serverIP;            //服务器IP
    private String meetingID;            //会议ID
    private int meetingState;            //会议状态 未开始1；已开始 2；已暂停 4；已结束 8；
    private int agendaIndex;               //议程序号
    private int contentType;             //会议当前内容类型  ,文件还投票是
    private int documentIndex;           //文件序号
    private int voteIndex;               //投票表决序号
    private int voteState;                  //投票状态 未开始1；已开始 2；已结束 4；
    private int IsUpdate;                //是否更新 未更新0；待更新 1
    private int IsReturnVoteResult;      //是否返回表决结果 不返回0；要返回 1；

    public MulticaseBean() {
    }

    @Override
    public MulticaseBean clone() throws CloneNotSupportedException {
        MulticaseBean multicaseBean = null;
        try {
            multicaseBean = (MulticaseBean) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

        return multicaseBean;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public int getMeetingState() {
        return meetingState;
    }

    public void setMeetingState(int meetingState) {
        this.meetingState = meetingState;
    }

    public int getAgendaIndex() {
        return agendaIndex;
    }

    public void setAgendaIndex(int agendaIndex) {
        this.agendaIndex = agendaIndex;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getDocumentIndex() {
        return documentIndex;
    }

    public void setDocumentIndex(int documentIndex) {
        this.documentIndex = documentIndex;
    }

    public int getVoteIndex() {
        return voteIndex;
    }

    public void setVoteIndex(int voteIndex) {
        this.voteIndex = voteIndex;
    }

    public int getVoteState() {
        return voteState;
    }

    public void setVoteState(int voteState) {
        this.voteState = voteState;
    }

    public int getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        IsUpdate = isUpdate;
    }

    public int getIsReturnVoteResult() {
        return IsReturnVoteResult;
    }

    public void setIsReturnVoteResult(int isReturnVoteResult) {
        IsReturnVoteResult = isReturnVoteResult;
    }
}
