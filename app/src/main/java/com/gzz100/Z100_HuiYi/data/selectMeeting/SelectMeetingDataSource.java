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

        /**
         * 没有数据，如果errorMsg不为空，则代表取数据失败
         * @param errorMsg
         */
        void onDataNotAvailable(String errorMsg);
    }
    interface StartMeetingCallback{
        void onStartMeetingSuccess();
        void onFail(String errorMsg);
    }

    /**
     * 获取会议列表，目前需传什么参数未知
     * @param callback    返回结果的回调
     */
    void fetchMeetingList( @NonNull LoadMeetingListCallback callback,String IMEI);

    void startMeeting(@NonNull StartMeetingCallback callback, String IMEI,int meetingID);
}
