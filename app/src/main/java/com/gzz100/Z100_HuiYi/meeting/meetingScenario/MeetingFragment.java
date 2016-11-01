package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail.DelegateDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
* 会议界面
* @author XieQXiong
* create at 2016/8/23 17:01
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

    public static MeetingFragment newInstance(){return new MeetingFragment();}

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
        Log.e("MeetingFragment -->","onResume");
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

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (Constant.DEBUG)
        Log.e("MeetingFragment -->","onDestroyView");
        super.onDestroyView();
        mPresenter.resetFirstLoad();
        mMeetingRoomView.resetUpAndDownValue();
        mUsers.clear();
    }

    @OnClick(R.id.id_tv_meeting_fragment_streamer)
    void onStreamerClick(){
        mPresenter.getMeetingInfo(getActivity());
//        Toast.makeText(getActivity(),"横幅",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.id_tv_meeting_fragment_others)
    void onOthersClick(){
        mPresenter.showDelegate();
    }

    @Override
    public void showMeetingInfo(Dialog dialog,View contentView,MeetingInfo meetingInfo) {
        ((TextView)contentView.findViewById(R.id.id_tv_dialog_name)).setText(
                meetingInfo.getMeetingName());
        ((TextView)contentView.findViewById(R.id.id_tv_dialog_begin_time)).setText(
                meetingInfo.getMeetingBeginTime());
        ((TextView)contentView.findViewById(R.id.id_tv_dialog_time)).setText(
                meetingInfo.getMeetingDuration() + "分钟");
        ((TextView)contentView.findViewById(R.id.id_tv_dialog_delegate)).setText("计划参会"
                + meetingInfo.getDelegateNum() + "人，实际参会"
                + meetingInfo.getSignDelegate() + "人");
        ((TextView)contentView.findViewById(R.id.id_tv_dialog_agenda)).setText(
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
        DelegateDetailActivity.showDelegateDetailActivity(getActivity(),DelegateModel,mainActivity.getCurrentTitle());
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
        mMeetingRoomView.addUsers(users,userName,this);
    }

    @Override
    public void setOthersNum(boolean isShow, int othersNum) {
        if (isShow)
            mOthers.setText("其他参会人员（"+othersNum+")");
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
