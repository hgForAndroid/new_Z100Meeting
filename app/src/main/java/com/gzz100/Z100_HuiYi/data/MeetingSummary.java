package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class MeetingSummary {
    private String meetingName;
    private String meetingBeginTime;
    private String meetingDuration;
    private int delegateNum;
    private int agendaNum;
    private List<DelegateBean> delegateList;
    private List<Agenda> agendaList;
    private List<Document> documentList;

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

    public int getDelegateNum() {
        return delegateNum;
    }

    public void setDelegateNum(int delegateNum) {
        this.delegateNum = delegateNum;
    }

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public int getAgendaNum() {
        return agendaNum;
    }

    public void setAgendaNum(int agendaNum) {
        this.agendaNum = agendaNum;
    }

    public List<DelegateBean> getDelegateList() {
        return delegateList;
    }

    public void setDelegateList(List<DelegateBean> delegateList) {
        this.delegateList = delegateList;
    }

    public List<Agenda> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<Agenda> agendaList) {
        this.agendaList = agendaList;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }
}
