package com.gzz100.Z100_HuiYi.data.signIn;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.MeetingSummary;
import com.gzz100.Z100_HuiYi.data.UserBean;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public interface SignInDataSource {
    interface LoadUserBeanCallback{
        void onUserBeanLoaded(UserBean userBean);
        void onDataNotAvailable();
    }
    interface LoadMeetingSummaryCallback{
        void onMeetingSummaryLoaded(MeetingSummary meetingSummary);
        void onDataNotAvailable();
    }

    /**
     * 获取当前设备对应的参会人员
     * @param IMEI        设备id
     * @param meetingID   会议id
     * @param callback    获取结果回调函数
     */
    void fetchUserBean(String IMEI, String meetingID, @NonNull LoadUserBeanCallback callback);

    /**
     * 签到
     * @param IMEI        设备id
     * @param meetingID   会议id
     * @param callback    结果回调
     */
    void signIn(String IMEI, String meetingID, @NonNull LoadMeetingSummaryCallback callback);
}
