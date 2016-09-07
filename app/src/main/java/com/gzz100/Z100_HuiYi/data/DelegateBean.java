package com.gzz100.Z100_HuiYi.data;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateBean {
    private String delegateName;
    private String delegateDepartment;
    private String role;
    private String delegateDetailInfo;
    private ArrayList delegateAgendaList;

    public String getDelegateDetailInfo() {
        return delegateDetailInfo;
    }

    public void setDelegateDetailInfo(String delegateDetailInfo) {
        this.delegateDetailInfo = delegateDetailInfo;
    }

    public ArrayList getDelegateAgendaList() {

        return delegateAgendaList;
    }

    public void setDelegateAgendaList(ArrayList delegateAgendaList) {
        this.delegateAgendaList = delegateAgendaList;
    }

    public String getDelegateDepartment() {

        return delegateDepartment;
    }

    public void setDelegateDepartment(String delegateDepartment) {
        this.delegateDepartment = delegateDepartment;
    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDelegateName() {

        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }
}
