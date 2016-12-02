package com.gzz100.Z100_HuiYi;

import android.app.Application;

import com.gzz100.Z100_HuiYi.utils.AppCrashCaughtUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

/**
* 应用类
* @author XieQXiong
* create at 2016/8/22 16:32
*/

public class MyAPP extends Application {
    private static MyAPP mInstance;
    public static MyAPP getInstance(){
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化崩溃日志收集器
        AppCrashCaughtUtil.getInstance().setAppCrashCaughtUtilInfo(this);
    }

    //默认角色为普通参会人员
    private int userRole = 2;


    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    private int userId;
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * 获取当前用户角色类型
     * @return   角色类型  ，  1为主持人，2为听众（普通参会人员）
     */
    public int getUserRole(){
        return userRole;
    }

    private int meetingIsProgress;

    public int isMeetingIsProgress() {
        return meetingIsProgress;
    }

    /**
     * 会议进行中，目前会议进行的状态。
     * @param meetingIsProgress   2：开始。  4：暂停   8：结束
     */
    public void setMeetingIsProgress(int meetingIsProgress) {
        this.meetingIsProgress = meetingIsProgress;
    }
}
