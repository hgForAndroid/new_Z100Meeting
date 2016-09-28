package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XieQXiong on 2016/9/2.
 */
public class UserBean extends BaseBean{
    private int userID;
    private String userName;
    private String userDepartment;
    private String userJob;
    private int userRole;
    private List<String> documentURLList;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public List<String> getDocumentURLList() {
        return documentURLList;
    }

    public void setDocumentURLList(List<String> documentURLList) {
        this.documentURLList = documentURLList;
    }
}
