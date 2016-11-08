package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

/**
 * Created by XieQXiong on 2016/11/7.
 */

public class MeetingEnd {
    private int flag;

    /**
     * flag 值为1时，代表从文件详情界面发起
     * 为2时，从主界面发起
     * @param flag
     */
    public MeetingEnd(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
