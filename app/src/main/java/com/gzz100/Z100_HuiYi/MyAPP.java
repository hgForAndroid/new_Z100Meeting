package com.gzz100.Z100_HuiYi;

import android.app.Application;

import com.gzz100.Z100_HuiYi.utils.AppCrashCaughtUtil;

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
    public void onCreate() {//haha
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

    private boolean voting ;

    /**
     * 投票是否进行中
     * @return
     */
    public boolean isVoting() {
        return voting;
    }

    /**
     * 设置投票是否进行中
     * @param voting
     */
    public void setVoting(boolean voting) {
        this.voting = voting;
    }

    private boolean hostOutOfMeeting = false;

    /**
     * 主持人在开会进行中离开文件详情界面，查看其它的内容
     * @param hostOutOfMeeting  true:离开了  false:还在文件详情界面
     */
    public void setHostOutOfMeeting(boolean hostOutOfMeeting){
        this.hostOutOfMeeting = hostOutOfMeeting;
    }

    /**
     * 主持人在开会进行中离开文件详情界面，查看其它的内容
     * @return  true:离开了  false:还在文件详情界面
     */
    public boolean getHostOutOfMeeting(){
        return this.hostOutOfMeeting;
    }
}
