package com.gzz100.Z100_HuiYi.data;
/**
* 会议概况实体类
* @author XieQXiong
* create at 2016/9/6 15:05
*/

public class MeetingInfo extends BaseBean{
    private String meetingName;       //会议名称
    private String meetingBeginTime;  //会议开始时间
    private String meetingDuration;       //开会时长
    private int delegateNum;          //参会人员总人数
    private int signDelegate;         //已签到参会人员
    private int agendaNum;            //议程个数


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

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public int getDelegateNum() {
        return delegateNum;
    }

    public void setDelegateNum(int delegateNum) {
        this.delegateNum = delegateNum;
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
