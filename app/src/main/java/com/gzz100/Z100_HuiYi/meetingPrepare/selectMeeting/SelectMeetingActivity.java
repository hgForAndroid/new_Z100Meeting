package com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.meetingPrepare.signIn.SignInActivity;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMeetingActivity extends BaseActivity implements SelectMeetingContract.View, OnMeetingClickListener {

    private String mDeviceIMEI;
    private String mMeetingName;

    public static void toSelectMeetingActivity(Context context) {
        Intent intent = new Intent(context, SelectMeetingActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.id_rcv_select_meeting)
    RecyclerView mRcvSelectMeeting;
    @BindView(R.id.id_tv_no_meeting)
    TextView mTvNoMeeting;
    private MeetingAdapter mAdapter;
    private List<MeetingBean> mMeetings;

    private SelectMeetingContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_meeting);
        ButterKnife.bind(this);
        mPresenter = new SelectMeetingPresenter(this.getApplicationContext(), this);
        saveIMEI();
    }

    /**
     * 保存设备IMEI码
     */
    private void saveIMEI() {
        mDeviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        mPresenter.saveIMEI(this.getApplicationContext(), mDeviceIMEI);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * 重新获取会议列表
     */
    @OnClick(R.id.id_btn_refresh_select_meeting)
    void getMeetingList() {
        mPresenter.fetchMeetingList(true, mDeviceIMEI);
    }

    /**
     * 显示会议列表
     *
     * @param meetings 会议列表集合
     */
    @Override
    public void showMeetingList(List<MeetingBean> meetings) {
        if (mMeetings != null)
            mMeetings.clear();
        mMeetings = meetings;
        mAdapter = new MeetingAdapter(this, meetings);
        mRcvSelectMeeting.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRcvSelectMeeting.setAdapter(mAdapter);
        mAdapter.setOnMeetingClickListener(this);
    }

    /**
     * 从服务器获取会议列表失败后，调用该方法。
     * @param errorMsg  当该值不为空，则代表取数据失败，该值为失败信息。
     */
    @Override
    public void showNoMeetingList(String errorMsg) {
        if (TextUtils.isEmpty(errorMsg)) {
            mTvNoMeeting.setVisibility(View.VISIBLE);
        } else {
            mTvNoMeeting.setText(errorMsg);
            mTvNoMeeting.setVisibility(View.VISIBLE);
        }
        mRcvSelectMeeting.setVisibility(View.GONE);
    }

    /**
     * 主持人选择要开的会议后，调用该方法，跳转到签到界面。
     * @param IMEI    设备id
     * @param meetingID   会议id
     */
    @Override
    public void showSignIn(String IMEI, int meetingID) {
        SignInActivity.toSignInActivity(this, IMEI, meetingID,mMeetingName);
    }

    @Override
    public void setPresenter(SelectMeetingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 点击某个会议
     * @param position   会议序号
     */
    @Override
    public void onMeetingClick(int position) {
        int meetingID = mMeetings.get(position).getMeetingID();
        mMeetingName = mMeetings.get(position).getMeetingName();

        String deviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        mPresenter.selectMeeting(deviceIMEI, meetingID);
    }

    @OnClick(R.id.id_btn_back_select_meeting)
    public void fallback(){
        ActivityStackManager.pop();
    }
    /**
     * 返回按钮，该方式是可能服务器地址输入错误后，向返回重新输入
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityStackManager.pop();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
