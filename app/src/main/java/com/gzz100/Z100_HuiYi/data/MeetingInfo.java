package com.gzz100.Z100_HuiYi.data;
/**
* 会议概况实体类
* @author XieQXiong
* create at 2016/9/6 15:05
*/

public class MeetingInfo extends BaseBean{
    private String meetingName;       //会议名称
    private String meetingBeginTime;  //会议名称
    private String meetingTime;       //开会时长
    private int allDelegate;          //参会人员总人数
    private int signDelegate;         //已签到参会人员
    private int agendaNum;            //议程个数

    /**
     *
     * @param meetingName       会议名称
     * @param meetingBeginTime  会议名称
     * @param meetingTime       开会时长
     * @param allDelegate       参会人员总人数
     * @param signDelegate      已签到参会人员
     * @param agendaNum``       议程个数
     */
    public MeetingInfo(String meetingName, String meetingBeginTime, String meetingTime, int allDelegate, int signDelegate, int agendaNum) {
        this.meetingName = meetingName;
        this.meetingBeginTime = meetingBeginTime;
        this.meetingTime = meetingTime;
        this.allDelegate = allDelegate;
        this.signDelegate = signDelegate;
        this.agendaNum = agendaNum;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingBeginTime() {
        return meetingBeginTime;
    }

    public void setMeetingBeginTime(String meetingBeginTime) {
        this.meetingBeginTime = meetingBeginTime;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public int getAllDelegate() {
        return allDelegate;
    }

    public void setAllDelegate(int allDelegate) {
        this.allDelegate = allDelegate;
    }

    public int getSignDelegate() {
        return signDelegate;
    }

    public void setSignDelegate(int signDelegate) {
        this.signDelegate = signDelegate;
    }

    public int getAgendaNum() {
        return agendaNum;
    }

    public void setAgendaNum(int agendaNum) {
        this.agendaNum = agendaNum;
    }
}
