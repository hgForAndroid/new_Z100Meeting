package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail.DelegateDetailActivity;
import com.gzz100.Z100_HuiYi.network.HttpManager;
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

//    private MeetingRoomView mMeetingRoomView;
    private MeetingRoomView2 mMeetingRoomView;
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

    private MeetingContract.Presenter mPresenter;
    private ICommunicate mainActivity;
    private TextView mStreamer;
    private WebView mWebView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (ICommunicate) context;
    }

    public static MeetingFragment newInstance() {
        return new MeetingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, null);
//        mMeetingRoomView = (MeetingRoomView) view.findViewById(R.id.id_meeting_view);
        mMeetingRoomView = (MeetingRoomView2) view.findViewById(R.id.id_meeting_view);
        mOthers = (TextView) view.findViewById(R.id.id_tv_meeting_fragment_others);

        mWebView = (WebView) view.findViewById(R.id.id_webView_end);
        mWebView.getSettings().setJavaScriptEnabled(true);


        mTopLayout = (RelativeLayout) view.findViewById(R.id.id_rl_meeting_fragment_top);
        mBottomLayout = (FrameLayout) view.findViewById(R.id.id_fl_meeting_fragment_bottom);
        mStreamer = (TextView) view.findViewById(R.id.id_tv_meeting_fragment_streamer);
        setStreamer();
        //会议结束界面部分控件
        mEndBegin = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_begin);
        mEndEnding = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_ending);
        mEndDuration = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_duration);
        mEndRecord = (TextView) view.findViewById(R.id.id_tv_end_meeting_fragment_record);
        mBtnExit = (Button) view.findViewById(R.id.id_btn_exit);

        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 横幅信息设置
     */
    private void setStreamer() {
        MeetingInfo meetingInfo = MeetingOperate.getInstance(getContext()).queryMeetingInfo(Constant.COLUMNS_MEETING_INFO);
        mStreamer.setText(meetingInfo.getMeetingName());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        setMeetingShowResult();
    }

    /**
     * 设置主场景界面的显示
     * 如果已经结束了，显示会议结束等信息
     * 否则显示座位的界面
     */
    private void setMeetingShowResult() {
        if (SharedPreferencesUtil.getInstance(getContext()).
                getBoolean(Constant.IS_MEETING_END, false)) {//会议结束
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

            //WebView显示会议结束后的概述
//            mWebView.setVisibility(View.VISIBLE);
//            String meetingEndingSummary = HttpManager.getInstance(getContext()).getServerIP()
//                    + SharedPreferencesUtil.getInstance(getContext()).getInt(Constant.MEETING_ID, -1);
//            mWebView.loadUrl(meetingEndingSummary);

        }else {//会议未结束
            mPresenter.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 主持人请求结束会议成功，在{@link MainActivity#hostEndMeetingSuccess()}
     * 或{@link MainActivity#handleMeetingEnd(MeetingEnd)}中调用。主持人端有两个调用的原因看后面一个方法的注释。
     * 其他参会人员客户端接收到结束会议的命令，在{@link MainActivity#clientResponseMeetingEnd()}中调用。
     * @param meetingEnd
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEnd(MeetingEnd meetingEnd) {
        if (meetingEnd.getFlag() == 2) {
            setMeetingShowResult();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.resetFirstLoad();
//        mMeetingRoomView.resetUpAndDownValue();
        mUsers.clear();
    }

    /**
     * 这两个方法均是在会议未结束前才有可能调用到，会议结束后这两个按钮不再显示。
     * 横幅和其他参会人员按钮的事件处理。
     * @param view
     */
    @OnClick({R.id.id_tv_meeting_fragment_streamer,R.id.id_tv_meeting_fragment_others })
    void onStreamerAndOthersClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_meeting_fragment_streamer:
                mPresenter.getMeetingInfo(getActivity());
                break;
            case R.id.id_tv_meeting_fragment_others:
                mPresenter.showDelegate();
                break;
        }
    }

    /**
     * 退出按钮，该按钮只在会议结束后，显示会议结果时最下面显示。
     */
    @OnClick(R.id.id_btn_exit)
    void onExitClick() {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.string_tip)
                .setMessage(R.string.string_exit_system)
                .setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtil.getInstance(getContext()).killAllRunningService();
                        SharedPreferencesUtil.getInstance(getContext()).clearKeyInfo();
                        //删除会议前预下载的所有文件
                        AppUtil.DeleteFolder(AppUtil.getCacheDir(getContext()));
                        getActivity().deleteDatabase(DBHelper.DB_NAME);
                        ActivityStackManager.exit();
                    }
                })
                .setNegativeButton(R.string.string_no, null)
                .create();
        dialog.show();
    }

    /**
     * 在{@link MeetingPresenter#getMeetingInfo(Context)}中调用。
     * @param dialog 会议概况弹窗
     * @param contentView
     * @param meetingInfo
     */
    @Override
    public void showMeetingInfo(Dialog dialog, View contentView, MeetingInfo meetingInfo) {
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_name)).setText(
                meetingInfo.getMeetingName());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_begin_time)).setText(
                meetingInfo.getMeetingBeginTime());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_time)).setText(
                meetingInfo.getMeetingDuration());
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_delegate)).setText("计划参会"
                + meetingInfo.getDelegateNum() + "人");
//        ((TextView) contentView.findViewById(R.id.id_tv_dialog_delegate)).setText("计划参会"
//                + meetingInfo.getDelegateNum() + "人，实际参会"
//                + meetingInfo.getSignDelegate() + "人");
        ((TextView) contentView.findViewById(R.id.id_tv_dialog_agenda)).setText(
                meetingInfo.getAgendaNum() + "个");
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }

    /**
     * 点击人员查看详情。
     * @param DelegateModel
     */
    @Override
    public void showUserInfo(DelegateModel DelegateModel) {
        DelegateDetailActivity.showDelegateDetailActivity(getActivity(), DelegateModel, mainActivity.getCurrentTitle());
    }

    /**
     * 假如在本地数据库中找不到这个用户，调用该方法。
     */
    @Override
    public void showNoUser() {
        //查询用户个人详情，没有该用户
        ToastUtil.showMessage(R.string.string_current_user_not_find);
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
        if (isShow && othersNum > 0){
            mOthers.setText("其他参会人员（" + othersNum + ")");
        }else {
            mOthers.setVisibility(View.GONE);
        }
    }

    /**
     * 点击了其他参会人员按钮，通知主界面更换界面。
     * 在{@link MainActivity#showDelegate(Boolean)}中调用
     * @param isShowDelegate  是否显示人员界面
     */
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
        mPresenter.fetchUserInfo(mUsers.get(position).getDelegateID());
    }
}
