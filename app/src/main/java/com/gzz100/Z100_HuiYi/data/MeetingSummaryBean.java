package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class MeetingSummaryBean extends BaseBean{
    private MeetingModel meetingModel;
    private int delegateNum;
    private int agendaNum;
    private List<DelegateModel> delegateModelList;
    private List<AgendaModel> agendaModelList;
    private List<DocumentModel> documentModelList;
    private List<Vote> voteList;

    public MeetingModel getMeetingModel() {
        return meetingModel;
    }

    public void setMeetingModel(MeetingModel meetingModel) {
        this.meetingModel = meetingModel;
    }

    public int getDelegateNum() {
        return delegateNum;
    }

    public void setDelegateNum(int delegateNum) {
        this.delegateNum = delegateNum;
    }

    public int getAgendaNum() {
        return agendaNum;
    }

    public void setAgendaNum(int agendaNum) {
        this.agendaNum = agendaNum;
    }

    public List<DelegateModel> getDelegateModelList() {
        return delegateModelList;
    }

    public void setDelegateModelList(List<DelegateModel> delegateModelList) {
        this.delegateModelList = delegateModelList;
    }

    public List<AgendaModel> getAgendaModelList() {
        return agendaModelList;
    }

    public void setAgendaModelList(List<AgendaModel> agendaModelList) {
        this.agendaModelList = agendaModelList;
    }

    public List<DocumentModel> getDocumentModelList() {
        return documentModelList;
    }

    public void setDocumentModelList(List<DocumentModel> documentModelList) {
        this.documentModelList = documentModelList;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<Vote> voteList) {
        this.voteList = voteList;
    }
}
