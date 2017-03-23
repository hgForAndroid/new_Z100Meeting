package com.gzz100.Z100_HuiYi.data.meeting;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.meeting.local.MeetingLocalDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.remote.MeetingRemoteDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
* 文件仓库，将获取数据的请求分发给本地或远程
* @author XieQXiong
* create at 2016/9/7 16:21
*/

public class MeetingRepository implements MeetingDataSource {
    private static MeetingRepository INSTANCE = null;

    private final MeetingRemoteDataSource mMeetingRemoteDataSource;

    private final MeetingLocalDataSource mMeetingLocalDataSource;

    private Context mContext;

    private MeetingRepository(@NonNull MeetingRemoteDataSource meetingRemoteDataSource,
                              @NonNull MeetingLocalDataSource meetingLocalDataSource,
                              Context context) {
        mMeetingRemoteDataSource = checkNotNull(meetingRemoteDataSource);
        mMeetingLocalDataSource = checkNotNull(meetingLocalDataSource);
        this.mContext = context;
    }

    public static MeetingRepository getInstance(@NonNull MeetingRemoteDataSource meetingRemoteDataSource,
                                                @NonNull MeetingLocalDataSource meetingLocalDataSource,
                                                Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MeetingRepository(meetingRemoteDataSource, meetingLocalDataSource,context);
        }
        return INSTANCE;
    }

    @Override
    public void getDelegateList(final LoadDelegateCallback callback) {
        mMeetingLocalDataSource.getDelegateList(new LoadDelegateCallback() {
            @Override
            public void onDelegateLoaded(List<DelegateModel> users) {
                //数据库中已经保存，不必再去取，直接返回，避免在MeetingLocalDataSource写重复代码
                callback.onDelegateLoaded(users);
            }

            @Override
            public void onDataNotAvailable() {
                mMeetingRemoteDataSource.getDelegateList(callback);
            }
        });
    }

    @Override
    public void getMetingInfo(final LoadMeetingInfoCallback callback) {
        mMeetingLocalDataSource.getMetingInfo(new LoadMeetingInfoCallback() {
            @Override
            public void onMeetingInfoLoaded(MeetingInfo meetingInfo) {
                //数据库中已经保存，不必再去取，直接返回，避免在MeetingLocalDataSource写重复代码
                callback.onMeetingInfoLoaded(meetingInfo);
            }

            @Override
            public void onDataNotAvailable() {
                mMeetingRemoteDataSource.getMetingInfo(callback);
            }
        });

    }

    @Override
    public void endMeeting(String IMEI, int meetingId, EndMeetingCallback callback) {
        mMeetingRemoteDataSource.endMeeting(IMEI,meetingId,callback);
    }
}
