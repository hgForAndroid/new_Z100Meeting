package com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting;

import android.content.Context;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingDataSource;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingRemoteDataSource;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class SelectMeetingPresenter implements SelectMeetingContract.Presenter {
    private boolean isFirst = true;
    private SelectMeetingContract.View mView;
    private Context mContext;
    private String mDeviceIMEI;

    public SelectMeetingPresenter(Context context,SelectMeetingContract.View view) {
        this.mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void fetchMeetingList(boolean forceLoad,String IMEI) {
//        if (isFirst || forceLoad){
//            isFirst = false;
//            SelectMeetingRemoteDataSource.getInstance(mContext).fetchMeetingList(new SelectMeetingDataSource.LoadMeetingListCallback() {
//                @Override
//                public void onMeetingListLoaded(List<MeetingBean> meetings) {
//                    mView.showMeetingList(meetings);
//                }
//
//                @Override
//                public void onDataNotAvailable(String errorMsg) {
//                    mView.showNoMeetingList(errorMsg);
//                }
//            },IMEI);
//        }

        mView.showMeetingList(FakeDataProvider.getMeetings());
    }

    @Override
    public void selectMeeting(final String IMEI, final String meetingID) {
//        SelectMeetingRemoteDataSource.getInstance(mContext).startMeeting(new SelectMeetingDataSource.StartMeetingCallback() {
//            @Override
//            public void onStartMeetingSuccess() {
//                mView.showSignIn(IMEI,meetingID);
//            }
//
//            @Override
//            public void onFail(String errorMsg) {
//
//            }
//        },IMEI,meetingID);
        mView.showSignIn(IMEI,meetingID);
    }

    @Override
    public void saveIMEI(Context context, String deviceIMEI) {
        if (Constant.DEBUG)
            Log.e("设备标识码 == ",deviceIMEI);
        mDeviceIMEI = deviceIMEI;
        SharedPreferencesUtil.getInstance(context).putString(Constant.DEVICE_IMEI,deviceIMEI);
    }

    @Override
    public void start() {
        fetchMeetingList(false, mDeviceIMEI);
    }
}
