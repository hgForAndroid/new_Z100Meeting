package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingRepository;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/5.
 */
public class MeetingPresenter implements MeetingContract.Presenter {
    private MeetingContract.View mView;
    private MeetingRepository mMeetingRepository;
    private Dialog mDialog;

    public MeetingPresenter(@NonNull MeetingRepository meetingRepository, @NonNull MeetingContract.View view) {
        this.mMeetingRepository = checkNotNull(meetingRepository, "meetingRepository cannot be null");
        this.mView = checkNotNull(view, "view cannot be null");
        this.mView.setPresenter(this);
    }

    /**
     * 在{@link MeetingFragment#onStreamerAndOthersClick(View)}中调用，获取会议信息显示。
     * @param context 上下文，用于创建弹窗
     */
    @Override
    public void getMeetingInfo(final Context context) {
        mMeetingRepository.getMetingInfo(new MeetingDataSource.LoadMeetingInfoCallback() {
            @Override
            public void onMeetingInfoLoaded(MeetingInfo meetingInfo) {
                View titleView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_title, null);
                View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_meeting_info, null);
                //                        .setTitle("会议概况")
                mDialog = new AlertDialog.Builder(context)
                        .setCustomTitle(titleView)
//                        .setTitle("会议概况")
                        .setView(contentView)
                        .setNegativeButton(R.string.string_ensure, new DismissDialog())
                        .create();
                mDialog.setCanceledOnTouchOutside(false);
                mView.showMeetingInfo(mDialog, contentView, meetingInfo);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    //关闭弹窗
    class DismissDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mView.dismissDialog(mDialog);
        }
    }

    //这个是全部参会人员
    private List<DelegateModel> mDelegateBeen = null;

    @Override
    public void fetchUserInfo(int userId) {
        DelegateModel delegate = null;
        for (DelegateModel tempDelegate :mDelegateBeen) {
            if (tempDelegate.getDelegateID() == userId){
                delegate = tempDelegate;
                break;
            }
        }
        if (delegate != null){
            mView.showUserInfo(delegate);
        }else {
            mView.showNoUser();
        }
    }

    boolean firstLoad = true;
    @Override
    public void fetchMainUsers() {
        if (firstLoad) {
            firstLoad = false;
            mMeetingRepository.getDelegateList(new MeetingDataSource.LoadDelegateCallback() {
                @Override
                public void onDelegateLoaded(List<DelegateModel> users) {
                    if (users != null && users.size()>0){
                        //过滤返回的人员集合，将有排位的人员提取出来
                        filterUser(users);
                        //TODO 需要时再去掉上面的注释,再将下面的注释掉
//                        mView.showMeetingRoom(users);
                        mDelegateBeen = users;
                        //参会人数大于10个人、
                        int othersNum = 0;
                        if (users.size() > 10){
                            othersNum = users.size() - 10;
                        }
                        mView.setOthersNum(true,othersNum);
                    }

                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    /**
     * 过滤需要排在会议桌的人员
     * @param users  获取到的所有会议参会人员。
     */
    private void filterUser(List<DelegateModel> users) {
        List<DelegateModel> forShowUsers = new ArrayList<>();
        HashMap<Integer,DelegateModel> hashMap = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getSeatIndex() != 0){
                forShowUsers.add(users.get(i));
            }
        }
        if (forShowUsers != null && forShowUsers.size() > 0){
            mView.showMeetingRoom(forShowUsers);
        }else {
            if (users.size() >= 10){
                for (int i = 0; i < 10; i++) {
                    users.get(i).setSeatIndex(i+1);
                    forShowUsers.add(users.get(i));
                }
            }else {
                for (int i = 0; i < users.size(); i++) {
                    users.get(i).setSeatIndex(i+1);
                    forShowUsers.add(users.get(i));
                }
            }

            mView.showMeetingRoom(forShowUsers);
        }

    }

    @Override
    public void resetFirstLoad() {
        firstLoad = true;
    }

    @Override
    public void showDelegate() {
        mView.showDelegate(true);
    }

    @Override
    public void fetchMeetingEndData(String hour, String min, String meetingBeginTime,String currentTime) {
        mView.setEndBegin(meetingBeginTime);
        mView.setEndDuration(Integer.valueOf(hour) +"小时"+min+"分钟");
        mView.setEndEnding(currentTime);
        mView.setEndRecord("会议纪要");
    }

    @Override
    public void start() {
        fetchMainUsers();
    }
}
