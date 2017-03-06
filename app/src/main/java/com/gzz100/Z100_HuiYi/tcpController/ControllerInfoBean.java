package com.gzz100.Z100_HuiYi.tcpController;

import java.io.Serializable;

/**
 * 主持人端发送给所有客户端的消息实体类。用于存储发送的信息，客户端接收到后处理该消息。
 * Created by XieQXiong on 2016/10/1.
 */
public class ControllerInfoBean implements Serializable,Cloneable{
    private int meetingState;            //会议状态 未开始1；已开始 2；已暂停 4；已结束 8；
    private int agendaIndex;               //议程序号
    private String upLevelTitle;           //上级界面名称
    private int contentType;             //会议当前内容类型  ,文件还投票是
    private int documentIndex;           //文件序号
    private boolean isVoteBegin;        //投票是否开始
    private int voteId;               //投票表决序号
    private int voteState;                  //投票状态 未开始1；已开始 2；已结束 4；
    private int IsUpdate;                //是否更新 未更新0；待更新 1
    private int IsReturnVoteResult;      //是否返回表决结果 不返回0；要返回 1；
    /**
     * 议程是否改变，在主持人端点击上一个，下一个时需要发送为true，
     * 其他为false或不传值
     */
    private boolean isAgendaChange = false;
    /**
     * 议程已经开始，已经倒计时过
     */
    private boolean isAgendaTimeCountDown;
    private String countingMin;
    private String countingSec;

    private boolean controlTempPeople;
    //下面两个时间，是在在会议临时有人进入，主持人控制端需要发送的，给客户端进行显示，当前会议已经开始了多久
    private String meetingBeginTimeHour;//会议已经进行的时间，时
    private String meetingBeginTimeMin;//会议已经进行的时间，分

    /**
     * 2:开始     4：暂停      8：结束
     */
    private int tempPeopleInCurrentState;
    public ControllerInfoBean() {
    }

    @Override
    public ControllerInfoBean clone() throws CloneNotSupportedException {
        ControllerInfoBean controllerInfoBean = null;
        try {
            controllerInfoBean = (ControllerInfoBean) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

        return controllerInfoBean;
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

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getVoteState() {
        return voteState;
    }

    public void setVoteState(int voteState) {
        this.voteState = voteState;
    }

    /**
     * 未更新0；待更新 1
     */
    public int getIsUpdate() {
        return IsUpdate;
    }
    /**
     * 未更新0；待更新 1
     */
    public void setIsUpdate(int isUpdate) {
        IsUpdate = isUpdate;
    }

    public int getIsReturnVoteResult() {
        return IsReturnVoteResult;
    }

    public void setIsReturnVoteResult(int isReturnVoteResult) {
        IsReturnVoteResult = isReturnVoteResult;
    }
    public String getUpLevelTitle() {
        return upLevelTitle;
    }

    public void setUpLevelTitle(String upLevelTitle) {
        this.upLevelTitle = upLevelTitle;
    }

    public boolean isAgendaChange() {
        return isAgendaChange;
    }

    public void setAgendaChange(boolean agendaChange) {
        isAgendaChange = agendaChange;
    }

    public boolean isAgendaTimeCountDown() {
        return isAgendaTimeCountDown;
    }

    public void setAgendaTimeCountDown(boolean agendaTimeCountDown) {
        isAgendaTimeCountDown = agendaTimeCountDown;
    }

    public String getCountingMin() {
        return countingMin;
    }

    public void setCountingMin(String countingMin) {
        this.countingMin = countingMin;
    }

    public String getCountingSec() {
        return countingSec;
    }

    public void setCountingSec(String countingSec) {
        this.countingSec = countingSec;
    }

    public boolean isVoteBegin() {
        return isVoteBegin;
    }

    public void setVoteBegin(boolean voteBegin) {
        isVoteBegin = voteBegin;
    }

    public boolean isControlTempPeople() {
        return controlTempPeople;
    }

    public void setControlTempPeople(boolean controlTempPeople) {
        this.controlTempPeople = controlTempPeople;
    }

    public String getMeetingBeginTimeHour() {
        return meetingBeginTimeHour;
    }

    public void setMeetingBeginTimeHour(String meetingBeginTimeHour) {
        this.meetingBeginTimeHour = meetingBeginTimeHour;
    }

    public String getMeetingBeginTimeMin() {
        return meetingBeginTimeMin;
    }

    public void setMeetingBeginTimeMin(String meetingBeginTimeMin) {
        this.meetingBeginTimeMin = meetingBeginTimeMin;
    }

    public int getTempPeopleInCurrentState() {
        return tempPeopleInCurrentState;
    }

    public void setTempPeopleInCurrentState(int tempPeopleInCurrentState) {
        this.tempPeopleInCurrentState = tempPeopleInCurrentState;
    }
}
