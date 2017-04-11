package com.gzz100.Z100_HuiYi.meetingPrepare.signIn.onestep;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingDataSource;
import com.gzz100.Z100_HuiYi.data.selectMeeting.SelectMeetingRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.signIn.SignInDataSource;
import com.gzz100.Z100_HuiYi.data.signIn.SignInRemoteDataSource;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meetingPrepare.signIn.WriteDatabaseService;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.StringUtils;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OneStepSignInActivity extends BaseActivity {

    @BindView(R.id.id_fl_one_step)
    FrameLayout mFrameLayout;
    @BindView(R.id.id_img_one_step)
    ImageView mImageView;
    @BindView(R.id.id_tv_meeting_name_signIn_activity)
    TextView mTvMeetingName;
    @BindView(R.id.id_tv_position_sign_in)
    TextView mPositionName;
    @BindView(R.id.id_tv_name_sign_in)
    TextView mName;
    private String mDeviceIMEI;
    private int mMeetingId;
    private String mMeetingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_step_sign_in);
        ButterKnife.bind(this);
        mDeviceIMEI = MPhone.getDeviceIMEI(this);
        SharedPreferencesUtil.getInstance(this).putString(Constant.CURRENT_IP, "119.29.192.18:89");
        Picasso.with(this).load(R.drawable.title).into(mImageView);
        getMeetingAndSelectMeeting();
    }

    private void getMeetingAndSelectMeeting() {
        SelectMeetingRemoteDataSource.getInstance(this).fetchMeetingList(
                new SelectMeetingDataSource.LoadMeetingListCallback() {
                    @Override
                    public void onMeetingListLoaded(List<MeetingBean> meetings) {
                        startMeeting(meetings);
                    }

                    @Override
                    public void onDataNotAvailable(String errorMsg) {
                        ToastUtil.showMessage("获取不到会议列表");
                    }
                }, mDeviceIMEI);
    }

    private void startMeeting(List<MeetingBean> meetings) {
        boolean hasNoStartMeeting = false;
        for (MeetingBean meetingBean:meetings) {
            if (meetingBean.getMeetingState() == 1){
                hasNoStartMeeting = true;
                mMeetingId = meetingBean.getMeetingID();
                mMeetingName = meetingBean.getMeetingName();
                break;
            }
        }
        if (hasNoStartMeeting) {
            SelectMeetingRemoteDataSource.getInstance(this).startMeeting(
                    new SelectMeetingDataSource.StartMeetingCallback() {
                        @Override
                        public void onStartMeetingSuccess() {
                            SharedPreferencesUtil.getInstance(OneStepSignInActivity.this).putInt(Constant.MEETING_ID, mMeetingId);
                            fetchCurrentUser();
                        }

                        @Override
                        public void onFail(String errorMsg) {
                            ToastUtil.showMessage("开启会议失败，"+errorMsg);
                        }
                    }, mDeviceIMEI, mMeetingId);
        } else {
            ToastUtil.showMessage("没有未开启的会议，请先创建会议。");
        }

    }


    private void fetchCurrentUser() {
        SignInRemoteDataSource.getInstance(this).fetchUserBean(mDeviceIMEI, mMeetingId, new SignInDataSource.LoadUserBeanCallback() {
            @Override
            public void onUserBeanLoaded(UserBean userBean) {
                //保存当前用户角色
//                mTvMeetingName.setText(mMeetingName);
                StringUtils.setTitleWithSpace(mTvMeetingName,mMeetingName," ");

                mPositionName.setText(userBean.getUserJob());
                mName.setText(userBean.getUserName());
                MyAPP.getInstance().setUserRole(userBean.getUserRole());
                MyAPP.getInstance().setUserId(userBean.getUserID());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mFrameLayout.setVisibility(View.GONE);
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onDataNotAvailable() {
                ToastUtil.showMessage("获取不到当前用户。");
            }
        });
    }

    @OnClick(R.id.id_btn_sign_in)
    public void signIn() {

        SignInRemoteDataSource.getInstance(this).signIn(mDeviceIMEI, mMeetingId, new SignInDataSource.LoadMeetingSummaryCallback() {
            @Override
            public void onMeetingSummaryLoaded(MeetingSummaryBean meetingSummary) {
                //分离对象并存储进数据库
                //保存用户列表不放在WriteDatabaseService，
                //是为了避免刚进入主场景界面，用户列表还未写入
                saveDelegateList(meetingSummary);
                WriteDatabaseService.startWriteDatabaseService(OneStepSignInActivity.this,meetingSummary,false);

                toMainActivity();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void toMainActivity() {
        MainActivity.toMainActivity(this);
        ActivityStackManager.pop();
    }

    /**
     * 保存参会人员列表
     * @param meetingSummary  会议的基本数据集合
     */
    private void saveDelegateList(MeetingSummaryBean meetingSummary) {
        List<DelegateModel> delegateList = meetingSummary.getDelegateModelList();
        MeetingOperate.getInstance(this).insertUserList(Constant.COLUMNS_USER,delegateList);
    }
}
