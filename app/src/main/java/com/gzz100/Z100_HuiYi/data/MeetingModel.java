package com.gzz100.Z100_HuiYi.data;

/**
 * Created by XieQXiong on 2016/10/31.
 */

public class MeetingModel {
    private String meetingSummary;
    private String meetingName;
    private String meetingBeginTime;
    private int meetingDuration;

    public String getMeetingSummary() {
        return meetingSummary;
    }

    public void setMeetingSummary(String meetingSummary) {
        this.meetingSummary = meetingSummary;
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

    public int getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(int meetingDuration) {
        this.meetingDuration = meetingDuration;
    }
}
