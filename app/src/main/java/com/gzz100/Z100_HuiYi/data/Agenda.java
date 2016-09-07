package com.gzz100.Z100_HuiYi.data;

import java.io.Serializable;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class Agenda extends BaseBean{
    private String agendaIndex;
    private String agendaTime;
    private String agendaName;
    private String agendaSpeaker;

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

    public String getAgendaSpeaker() {
        return agendaSpeaker;
    }

    public void setAgendaSpeaker(String agendaSpeaker) {
        this.agendaSpeaker = agendaSpeaker;
    }
}
