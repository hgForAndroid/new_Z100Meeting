package com.gzz100.Z100_HuiYi.data.meeting.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

/**
* 本地获取会议界面数据
* @author XieQXiong
* create at 2016/9/7 9:20
*/

public class MeetingLocalDataSource implements MeetingDataSource {
    private static MeetingLocalDataSource INSTANCE;
    private static Context mContext;
    private MeetingOperate mMeetingOperate;

    private MeetingLocalDataSource(@NonNull Context context) {
        mMeetingOperate = MeetingOperate.getInstance(context);
    }

    public static MeetingLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MeetingLocalDataSource(context);
            mContext = context;
        }
        return INSTANCE;
    }
    @Override
    public void getDelegateList(LoadDelegateCallback callback) {
        List<DelegateModel> users = mMeetingOperate.queryUserList(Constant.COLUMNS_USER);
        if (users != null && users.size() > 0){
            callback.onDelegateLoaded(users);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMetingInfo(LoadMeetingInfoCallback callback) {
        MeetingInfo meetingInfo = mMeetingOperate.queryMeetingInfo(Constant.COLUMNS_MEETING_INFO);
        if (meetingInfo != null){
            callback.onMeetingInfoLoaded(meetingInfo);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void endMeeting(String IMEI, int meetingId, EndMeetingCallback callback) {

    }
}
