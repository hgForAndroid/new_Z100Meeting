package com.gzz100.Z100_HuiYi.data;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class Agenda extends BaseBean{
    private int agendaIndex;
    private String agendaDuration;
    private String agendaName;
    private String agendaSpeaker;

    public int getAgendaIndex() {
        return agendaIndex;
    }

    public void setAgendaIndex(int agendaIndex) {
        this.agendaIndex = agendaIndex;
    }

    public String getAgendaDuration() {
        return agendaDuration;
    }

    public void setAgendaDuration(String agendaDuration) {
        this.agendaDuration = agendaDuration;
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
