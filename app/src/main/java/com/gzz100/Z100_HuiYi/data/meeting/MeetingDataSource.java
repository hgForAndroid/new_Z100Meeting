package com.gzz100.Z100_HuiYi.data.meeting;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/6.
 */
public interface MeetingDataSource {
    interface LoadDelegateCallback{
        void onDelegateLoaded(List<DelegateModel> users);
        void onDataNotAvailable();
    }

    interface LoadMeetingInfoCallback{
        void onMeetingInfoLoaded(MeetingInfo meetingInfo);
        void onDataNotAvailable();
    }

    /**
     * 获取所有参会人员列表
     * @param callback  获取结果回调接口
     */
    void getDelegateList(LoadDelegateCallback callback);

    /**
     * 获取会议概况
     * @param callback  获取结果接口回调
     */
    void getMetingInfo(LoadMeetingInfoCallback callback);
}
