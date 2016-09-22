package com.gzz100.Z100_HuiYi.data.selectMeeting;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class SelectMeetingRemoteDataSource implements SelectMeetingDataSource {
    private static SelectMeetingRemoteDataSource mInstance;
    private SelectMeetingRemoteDataSource() {
    }
    public static SelectMeetingRemoteDataSource getInstance(){
        if (mInstance == null){
            synchronized (SelectMeetingRemoteDataSource.class){
                if (mInstance == null) {
                    mInstance = new SelectMeetingRemoteDataSource();
                }
            }
        }
        return mInstance;
    }


    @Override
    public void fetchMeetingList(@NonNull LoadMeetingListCallback callback) {
        checkNotNull(callback);
        List<MeetingBean> meetings = FakeDataProvider.getMeetings();
        if (meetings != null && meetings.size() > 0){
            callback.onMeetingListLoaded(meetings);
        }else {
            callback.onDataNotAvailable();
        }
    }
}
