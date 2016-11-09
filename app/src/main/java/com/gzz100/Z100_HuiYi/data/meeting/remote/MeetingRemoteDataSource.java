package com.gzz100.Z100_HuiYi.data.meeting.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.ProgressSubscriber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/6.
 */
public class MeetingRemoteDataSource implements MeetingDataSource {
    private static MeetingRemoteDataSource INSTANCE;
    private static Context mContext;
    private MeetingOperate mMeetingOperate;

    private MeetingRemoteDataSource(@NonNull Context context) {
        mMeetingOperate = MeetingOperate.getInstance(context);
    }

    public static MeetingRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MeetingRemoteDataSource(context);
            mContext = context;
        }
        return INSTANCE;
    }

    @Override
    public void getDelegateList(LoadDelegateCallback callback) {
        checkNotNull(callback);
//        List<DelegateModel> users = FakeDataProvider.getDelegates();
//        if (users != null && users.size() > 0){
//            callback.onDelegateLoaded(users);
//            mMeetingOperate.insertUserList(Constant.COLUMNS_USER,users);
//        }else {
//            callback.onDataNotAvailable();
//        }

        //这里的UsrPost的参数未知

    }

    @Override
    public void getMetingInfo(final LoadMeetingInfoCallback callback) {
        checkNotNull(callback);
//        MeetingInfo meetingInfo = FakeDataProvider.getMeetingInfo();
//        if (meetingInfo != null){
//            callback.onMeetingInfoLoaded(meetingInfo);
//            mMeetingOperate.insertMeetingInfo(Constant.COLUMNS_MEETING_INFO,meetingInfo);
//        }else {
//            callback.onDataNotAvailable();
//        }

    }
}
