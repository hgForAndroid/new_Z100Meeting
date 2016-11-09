package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail.DelegateDetailActivity;
import com.gzz100.Z100_HuiYi.tcpController.Client;
import com.gzz100.Z100_HuiYi.tcpController.Server;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议界面
 *
 * @author XieQXiong
 *         create at 2016/8/23 17:01
 */

public class MeetingFragment extends Fragment implements MeetingContract.View, OnUserClickListener {

    private MeetingRoomView mMeetingRoomView;

    private List<DelegateModel> mUsers = new ArrayList<>();
    //会议结束后显示的布局
    private RelativeLayout mTopLayout;
    //会议未结束显示的布局
    private FrameLayout mBottomLayout;
    //会议结束后的  会议开始于   会议结束于  会议时长   会议纪要
    private TextView mEndBegin;
    private TextView mEndEnding;
    private TextView mEndDuration;
    private TextView mEndRecord;
    //用于设置 其他参会人员人数
    private TextView mOthers;
    private Button mBtnExit;

    public static MeetingFragment newInstance() {
        return new MeetingFragment();
    }

    private MeetingContract.Presenter mPresenter;

    private ICommunicate mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (ICommunicate) context;
    }

    @Override
    public void onResume() {
        if (Constant.DEBUG)
            Log.e("MeetingFragment -->", "onResume");
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, null);
        mMeetingRoomView = (MeetingRoomView) view.findViewById(R.id.id_meeting_view);
        mOthers = (TextView) view.findViewById(R.id.id_tv_meeting_fragment_others);

        mTopLayout = (RelativeLayout) view.findViewById(R.id.id_rl_meeting_fragment_top);
        mBottomLayout = (FrameLayout) view.findViewById(R.id.id_fl_meeting_fragment_bottom);
        //会议结束界面部分控件
        mEndBegin = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_begin);
        mEndEnding = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_ending);
        mEndDuration = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_duration);
        mEndRecord = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_record);
        mBtnExit = (Button) view.findViewById(R.id.id_btn_exit);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        setMeetingEndShoeResult();
    }

    /**
     * 会议结束后显示会议的开始，结束等信息
     */
    private void setMeetingEndShoeResult() {
        if (SharedPreferencesUtil.getInstance(getContext()).getBoolean(Constant.IS_MEETING_END, false)) {
            mTopLayout.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.GONE);
            String meetingDuration = MeetingOperate.getInstance(getContext()).
                    queryMeetingInfo(Constant.COLUMNS_MEETING_INFO).getMeetingDuration();
            //存储会议结束的时间，时  分
            String hour = SharedPreferencesUtil.getInstance(getContext()).getString(Constant.ENDING_HOUR, "");
            String min = SharedPreferencesUtil.getInstance(getContext()).getString(Constant.ENDING_MIN, "");
            String currentTime = SharedPreferencesUtil.getInstance(getContext()).getString(Constant.ENDING_CURRENT_TIME, "");
            if (TextUtils.isEmpty(currentTime)){
                SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
                String date= sdf.format(new java.util.Date());
                SharedPreferencesUtil.getInstance(getContext()).putString(Constant.ENDING_CURRENT_TIME,date);
                mPresenter.fetchMeetingEndData(hour,min,meetingDuration,date);
            }else {
                mPresenter.fetchMeetingEndData(hour,min,meetingDuration,currentTime);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEnd(MeetingEnd meetingEnd) {
        if (meetingEnd.getFlag() == 2) {
            setMeetingEndShoeResult();
        }
    }

    @Override
    public void onDestroyView() {
        if (Constant.DEBUG)
            Log.e("MeetingFragment -->", "onDestroyView");
        super.onDestroyView();
        mPresenter.resetFirstLoad();
        mMeetingRoomView.resetUpAndDownValue();
        mUsers.clear();
    }

    @OnClick({R.id.id_tv_meeting_fragment_streamer, R.id.id_btn_exit})
    void onStreamerClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_meeting_fragment_streamer:
                mPresenter.getMeetingInfo(getActivity());
                break;
            case R.id.id_btn_exit:
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("退出系统？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppUtil.isServiceRun(getActivity(), "com.gzz100.Z100_HuiYi.tcpController.Server")) {
                                    getActivity().stopService(new Intent(getActivity(), Server.class));
                                }
                                if (AppUtil.isServiceRun(getActivity(), "com.gzz100.Z100_HuiYi.tcpController.Client")) {
                                    getActivity().stopService(new Intent(getActivity(), Client.class));
                                }
                                clearCache();
                                getActivity().deleteDatabase(DBHelper.DB_NAME);
                                ActivityStackManager.exit();
                            }
                        })
                        .setNegativeButton("否", null)
                        .create();
                dialog.show();
                break;
        }
    }

    /**
     * 清除sharedPreference中，对显示有影响的缓存
     */
    private void clearCache() {
        SharedPreferencesUtil.getInstance(getContext()).remove(Constant.IS_MEETING_END);
        SharedPreferencesUtil.getInstance(getContext()).remove(Constant.COUNTING_MIN);
        SharedPreferencesUtil.getInstance(getContext()).remove(Constant.COUNTING_SEC);
        SharedPreferencesUtil.getInstance(getContext()).remove(Constant.PAUSE_AGENDA_INDEX);
        SharedPreferencesUtil.getInstance(getContext()).remove(Constant.PAUSE_DOCUMENT_INDEX);
    }

    @OnClick(R.id.id_tv_meeting_fragment_others)
    void onOthersClick() {
        mPresenter.showDelegate();
    }

    @Override
    public void showMeetingInfo(Dialog dialog, View contentView, MeetingInfo meetingInfo) {
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_name)).setText(
                meetingInfo.getMeetingName());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_begin_time)).setText(
                meetingInfo.getMeetingBeginTime());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_time)).setText(
                meetingInfo.getMeetingDuration());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_delegate)).setText("计划参会"
                + meetingInfo.getDelegateNum() + "人，实际参会"
                + meetingInfo.getSignDelegate() + "人");
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_agenda)).setText(
                meetingInfo.getAgendaNum() + "个");
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void showUserInfo(DelegateModel DelegateModel) {
        DelegateDetailActivity.showDelegateDetailActivity(getActivity(), DelegateModel, mainActivity.getCurrentTitle());
//        Intent intent = new Intent(getActivity(), DelegateDetailActivity.class);
//        startActivity(intent);

    }

    @Override
    public void showNoUser() {
        //查询用户个人详情，没有该用户
        Toast.makeText(getActivity(), "无该用户", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MeetingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMeetingRoom(List<DelegateModel> users) {
        mUsers = users;
        String userName = SharedPreferencesUtil.getInstance(this.getContext()).getString(Constant.USER_NAME, "");
        mMeetingRoomView.addUsers(users, userName, this);
    }

    @Override
    public void setOthersNum(boolean isShow, int othersNum) {
        if (isShow)
            mOthers.setText("其他参会人员（" + othersNum + ")");
    }

    @Override
    public void showDelegate(Boolean isShowDelegate) {
        EventBus.getDefault().post(isShowDelegate);
    }

    @Override
    public void setEndBegin(String beginTime) {
        mEndBegin.setText(beginTime);
    }

    @Override
    public void setEndEnding(String endingTime) {
        mEndEnding.setText(endingTime);
    }

    @Override
    public void setEndDuration(String duration) {
        mEndDuration.setText(duration);
    }

    @Override
    public void setEndRecord(String record) {
        mEndRecord.setText(record);
    }

    @Override
    public void setEndingLayoutShow() {
        mTopLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void onUserClick(View v, int position) {
//        Toast.makeText(getActivity(),mUsers.get(position).getUserName(),Toast.LENGTH_SHORT).show();
        mPresenter.fetchUserInfo(mUsers.get(position).getDelegateID());
    }
}
