package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/5.
 */
public class MeetingPresenter implements MeetingContract.Presenter {
    private MeetingContract.View mView;
    private MeetingRepository mMeetingRepository;
    public MeetingPresenter(@NonNull MeetingRepository meetingRepository, @NonNull MeetingContract.View view) {
        this.mMeetingRepository = checkNotNull(meetingRepository,"meetingRepository cannot be null");
        this.mView = checkNotNull(view,"view cannot be null");
        this.mView.setPresenter(this);
    }

    @Override
    public void getMeetingInfo() {

        mView.showMeetingInfo();

    }

    @Override
    public void showOthers() {
        mView.showOthers();
    }

    @Override
    public void fetchUserInfo(int userId) {

        mView.showUserInfo();

    }

    boolean firstLoad = true;
    @Override
    public void fetchMainUsers() {
        if (firstLoad){
            firstLoad = false;
            mMeetingRepository.getDelegateList(new MeetingDataSource.LoadDelegateCallback() {
                @Override
                public void onDelegateLoaded(List<UserBean> users) {
                    mView.showMeetingRoom(users);

                }
                @Override
                public void onDataNotAvailable() {

                }
            });
        }

    }

    @Override
    public void resetFirstLoad(){
        firstLoad = true;
    }

    @Override
    public void showDelegate() {
        mView.showDelegate(true);
    }

    @Override
    public void setOtherNum(int otherNum) {

    }

    @Override
    public void start() {
        fetchMainUsers();
    }
}
