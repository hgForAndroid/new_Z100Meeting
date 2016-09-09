package com.gzz100.Z100_HuiYi.data.meeting;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.data.meeting.local.MeetingLocalDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.remote.MeetingRemoteDataSource;
import com.gzz100.Z100_HuiYi.utils.Constant;

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
    public void getDelegateList(LoadDelegateCallback callback) {
        List<UserBean> users = MeetingOperate.getInstance(mContext).queryUserList(Constant.COLUMNS_USER);
        if (users != null && users.size() > 0){
//            mMeetingLocalDataSource.getDelegateList(callback);
            //数据库中已经保存，不必再去取，直接返回，避免在MeetingLocalDataSource写重复代码
            callback.onDelegateLoaded(users);
        }else {
            mMeetingRemoteDataSource.getDelegateList(callback);
        }

    }

    @Override
    public void getMetingInfo(LoadMeetingInfoCallback callback) {
        MeetingInfo meetingInfo = MeetingOperate.getInstance(mContext).queryMeetingInfo(Constant.COLUMNS_MEETING_INFO);
        if (meetingInfo != null){
//            mMeetingLocalDataSource.getMetingInfo(callback);
            //数据库中已经保存，不必再去取，直接返回，避免在MeetingLocalDataSource写重复代码
            callback.onMeetingInfoLoaded(meetingInfo);
        }else {
            mMeetingRemoteDataSource.getMetingInfo(callback);
        }

    }
}
