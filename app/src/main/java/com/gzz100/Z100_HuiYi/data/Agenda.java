package com.gzz100.Z100_HuiYi.data;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class Agenda {
    private String agendaIndex;
    private String agendaTime;
    private String agendaName;
    private String keynoteSpeaker;
    private List<FileBean> filesList;

    public String getAgendaIndex() {
        return agendaIndex;
    }

    public void setAgendaIndex(String agendaIndex) {
        this.agendaIndex = agendaIndex;
    }

    public String getAgendaTime() {
        return agendaTime;
    }

    public void setAgendaTime(String agendaTime) {
        this.agendaTime = agendaTime;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public String getKeynoteSpeaker() {
        return keynoteSpeaker;
    }

    public void setKeynoteSpeaker(String keynoteSpeaker) {
        this.keynoteSpeaker = keynoteSpeaker;
    }

    public List<FileBean> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<FileBean> filesList) {
        this.filesList = filesList;
    }
}
