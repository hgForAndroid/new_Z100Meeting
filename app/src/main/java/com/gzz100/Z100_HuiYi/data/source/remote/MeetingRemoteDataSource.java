package com.gzz100.Z100_HuiYi.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.source.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.source.local.FileDBHelper;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.ProgressSubscriber;
import com.gzz100.Z100_HuiYi.network.entity.MeetingInfoPost;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/6.
 */
public class MeetingRemoteDataSource implements MeetingDataSource {

    private static MeetingRemoteDataSource INSTANCE;
//    private final FileDBHelper mDbHelper;
    private static Context mContext;

    private MeetingRemoteDataSource(@NonNull Context context) {
//        mDbHelper = FileDBHelper.getInstance(context);
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

    }

    @Override
    public void getMetingInfo(final LoadMeetingInfoCallback callback) {
        checkNotNull(callback);
        MeetingInfoPost meetingInfoPost = new MeetingInfoPost(new ProgressSubscriber(
                new HttpRxCallbackListener<MeetingInfo>(){

                    @Override
                    public void onNext(MeetingInfo meetingInfo) {
                        callback.onMeetingInfoLoaded(meetingInfo);

                    }
                },mContext
        ));
    }
}
