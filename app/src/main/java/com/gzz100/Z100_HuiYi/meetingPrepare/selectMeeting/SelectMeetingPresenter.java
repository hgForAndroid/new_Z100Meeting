package com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting;

import android.content.Context;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingDataSource;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingRemoteDataSource;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class SelectMeetingPresenter implements SelectMeetingContract.Presenter {
    private boolean isFirst = true;
    private SelectMeetingContract.View mView;

    public SelectMeetingPresenter(SelectMeetingContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void fetchMeetingList(boolean forceLoad) {
        if (isFirst || forceLoad){
            isFirst = false;
            SelectMeetingRemoteDataSource.getInstance().fetchMeetingList(new SelectMeetingDataSource.LoadMeetingListCallback() {
                @Override
                public void onMeetingListLoaded(List<MeetingBean> meetings) {
                    mView.showMeetingList(meetings);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showNoMeetingList();
                }
            });
        }
    }

    @Override
    public void selectMeeting(String IMEI, String meetingID) {

        mView.showSignIn();
    }

    @Override
    public void saveIMEI(Context context, String deviceIMEI) {
        if (Constant.DEBUG)
            Log.e("设备标识码 == ",deviceIMEI);
        SharedPreferencesUtil.getInstance(context).putString(Constant.DEVICE_IMEI,deviceIMEI);
    }

    @Override
    public void start() {
        fetchMeetingList(false);
    }
}
