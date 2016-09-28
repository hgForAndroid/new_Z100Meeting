package com.gzz100.Z100_HuiYi.data.selectMeeting;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.MeetingBean;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public interface SelectMeetingDataSource {
    interface LoadMeetingListCallback{
        void onMeetingListLoaded(List<MeetingBean> meetings);
        void onDataNotAvailable();
    }
    interface StartMeetingCallback{
        void onStartMeetingSuccess();
        void onFail();
    }

    /**
     * 获取会议列表，目前需传什么参数未知
     * @param callback    返回结果的回调
     */
    void fetchMeetingList( @NonNull LoadMeetingListCallback callback,String IMEI);

    void startMeeting(@NonNull StartMeetingCallback callback, String IMEI,String meetingID);
}
