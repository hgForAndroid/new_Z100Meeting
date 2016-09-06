package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.utils.Constant;

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

    private List<UserBean> mUsers = new ArrayList<>();

    public static MeetingFragment newInstance(){return new MeetingFragment();}

    private MeetingContract.Presenter mPresenter;

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
        Toast.makeText(getActivity(),"横幅",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.id_tv_meeting_fragment_others)
    void onOthersClick(){
        mPresenter.showDelegate();
    }

    @Override
    public void showMeetingInfo() {

    }

    @Override
    public void showOthers() {

    }

    @Override
    public void showUserInfo() {
//        Intent intent = new Intent(getActivity(),null);
//        startActivity(intent);

    }

    @Override
    public void setPresenter(MeetingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMeetingRoom(List<UserBean> users) {
        mUsers = users;
        mMeetingRoomView.addUsers(users,"李四",this);
    }

    @Override
    public void setOthersNum(boolean isShow, int othersNum) {

    }

    @Override
    public void showDelegate(Boolean isShowDelegate) {
        EventBus.getDefault().post(isShowDelegate);
    }

    @Override
    public void onUserClick(View v, int position) {
        Toast.makeText(getActivity(),mUsers.get(position).getUserName(),Toast.LENGTH_SHORT).show();
        mPresenter.fetchUserInfo(mUsers.get(position).getUserId());
    }
}
