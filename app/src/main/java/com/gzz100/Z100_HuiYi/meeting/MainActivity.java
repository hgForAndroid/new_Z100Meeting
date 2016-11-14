package com.gzz100.Z100_HuiYi.meeting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.meeting.about.AboutFragment;
import com.gzz100.Z100_HuiYi.meeting.agenda.AgendaFragment;
import com.gzz100.Z100_HuiYi.meeting.agenda.AgendaPresenter;
import com.gzz100.Z100_HuiYi.meeting.agenda.RemoveControlViewEvent;
import com.gzz100.Z100_HuiYi.meeting.delegate.DelegateFragment;
import com.gzz100.Z100_HuiYi.meeting.delegate.DelegatePresenter;
import com.gzz100.Z100_HuiYi.meeting.file.FileFragment;
import com.gzz100.Z100_HuiYi.meeting.file.FilePresenter;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingEnd;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingFragment;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingPresenter;
import com.gzz100.Z100_HuiYi.meeting.vote.OnAllVoteItemClickListener;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteFragment;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteListDialog;
import com.gzz100.Z100_HuiYi.meeting.vote.VotePresenter;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteResultDialog;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.tcpController.Client;
import com.gzz100.Z100_HuiYi.tcpController.Server;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, ICommunicate, ControllerView.IOnControllerListener,
        OnAllVoteItemClickListener, MainContract.View {

    private ControllerView mControllerView;
    private FrameLayout.LayoutParams mFl;
    private int mMeetingState = Constant.MEETING_STATE_NOT_BEGIN;
    private ControllerInfoBean mControllerInfoBean;
    private Gson mGson;
    private VoteListDialog mDialog;
    private int mVoteId;
    private MainContract.Presenter mMainPresenter;
    private VoteResultDialog mVoteResultDialog;

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static final Long TRIGGER_OF_REMOVE_CONTROLLERVIEW = 1L;

    @BindView(R.id.id_main_fl_root)
    FrameLayout mRootView;
    @BindView(R.id.id_main_tbv)
    NavBarView mNavBarView;
    @BindView(R.id.id_main_ViewPager)
    ViewPager mViewPager;

    @BindView(R.id.id_main_rdg)
    RadioGroup mTabGroup;
    @BindView(R.id.id_main_meetingTab)
    RadioButton mMeetingTab;
    @BindView(R.id.id_main_delegateTab)
    RadioButton mDelegateTab;
    @BindView(R.id.id_main_agendaTab)
    RadioButton mAgendaTab;
    @BindView(R.id.id_main_fileTab)
    RadioButton mFileTab;
    @BindView(R.id.id_main_aboutTab)
    RadioButton mAboutTab;
    @BindView(R.id.id_main_voteTab)
    RadioButton mVoteTab;

    private MainFragmentAdapter mMainFragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private MeetingFragment mMeetingFragment;
    private DelegateFragment mDelegateFragment;
    private AgendaFragment mAgendaFragment;
    private FileFragment mFileFragment;
    private VoteFragment mVoteFragment;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;
    public static final int PAGE_SIX = 5;
    private AboutFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStackManager.clearExceptOne(this);
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenter(this,this);
        EventBus.getDefault().register(this);
        init();

//        Configuration config = getResources().getConfiguration();
//        int  smallestScreenWidth = config.smallestScreenWidthDp;
//        int screenHeightDp = config.screenHeightDp;
//        int screenWidthDp = config.screenWidthDp;
//        Log.e("screenHeightDp=","====       "+screenHeightDp+"\n    screenWidthDp=====     "+screenWidthDp);
    }
    private void init() {
        mMeetingFragment = MeetingFragment.newInstance();
        mDelegateFragment = DelegateFragment.newInstance();
        mAgendaFragment = AgendaFragment.newInstance();
        mFileFragment = FileFragment.newInstance();
        mAboutFragment = AboutFragment.newInstance();
        mVoteFragment = VoteFragment.newInstance();
        mFragments.add(mMeetingFragment);
        mFragments.add(mDelegateFragment);
        mFragments.add(mAgendaFragment);
        mFragments.add(mFileFragment);
        mFragments.add(mAboutFragment);
        mFragments.add(mVoteFragment);


        //测试
        if (MyAPP.getInstance().getUserRole() == 1) {//主持人
//            mFragments.add(mVoteFragment);
//            mVoteTab.setVisibility(View.VISIBLE);
            mControllerView = ControllerView.getInstance(this);
            mFl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mFl.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            mRootView.addView(mControllerView, mFl);
            mControllerView.setIOnControllerListener(this);
            //发送消息的实体
            mControllerInfoBean = new ControllerInfoBean();
            mGson = new Gson();

        } else {//不是主持人
//            mVoteTab.setVisibility(View.GONE);
        }
        //默认选择哪个
        defaultSelected();

        initEvent();
        initPresenter();
        //这里没有使用跟文件详情界面一样的方式，因为在这里不用关心Handler被强引用的问题
        //因为时间是要一直进行的
        timeCounting();
    }

    private void defaultSelected() {
        mMainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mMainFragmentAdapter);
        mViewPager.setCurrentItem(PAGE_ONE);
        mNavBarView.mTvTitle.setText(mMeetingTab.getText());
        mNavBarView.setMeetingStateOrAgendaState("未开始");
        mMeetingTab.setChecked(true);
    }

    private void timeCounting() {
        mHandler.post(mRunnable);
    }

    private int hour = 0;
    private int min = 0;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 60000);
            mNavBarView.setTimeMin(getMin());
            min++;
            mNavBarView.setTimeHour(getHour());
        }
    };

    public String getHour() {
        String newHour = hour >= 10 ? "" + hour : "0" + hour;
        return newHour;
    }

    public String getMin() {
        String newMin = min >= 10 ? "" + min : "0" + min;
        if (newMin.equals("60")) {
            hour++;
            min = 0;
            return "00";
        }
        return newMin;
    }

    private void initPresenter() {
        new FilePresenter(RepositoryUtil.getFileRepository(this),
                mFileFragment);
        new AgendaPresenter(RepositoryUtil.getFileRepository(this),
                mAgendaFragment);
        new DelegatePresenter(RepositoryUtil.getDelegateRepository(this.getApplicationContext()),
                mDelegateFragment);
        new VotePresenter(RepositoryUtil.getVoteRepository(this),
                mVoteFragment, this);
        new MeetingPresenter(RepositoryUtil.getMeetingRepository(this),
                mMeetingFragment);
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);
        mTabGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.id_main_meetingTab:
                mViewPager.setCurrentItem(PAGE_ONE);
                mNavBarView.mTvTitle.setText(mMeetingTab.getText());
                break;
            case R.id.id_main_delegateTab:
                mViewPager.setCurrentItem(PAGE_TWO);
                mNavBarView.mTvTitle.setText(mDelegateTab.getText());
                break;
            case R.id.id_main_agendaTab:
                mViewPager.setCurrentItem(PAGE_THREE);
                mNavBarView.mTvTitle.setText(mAgendaTab.getText());
                break;
            case R.id.id_main_fileTab:
                mViewPager.setCurrentItem(PAGE_FOUR);
                mNavBarView.mTvTitle.setText(mFileTab.getText());
                break;
            case R.id.id_main_aboutTab:
                mViewPager.setCurrentItem(PAGE_FIVE);
                mNavBarView.mTvTitle.setText(mAboutTab.getText());
                break;
            case R.id.id_main_voteTab:
                mViewPager.setCurrentItem(PAGE_SIX);
                mNavBarView.mTvTitle.setText(mVoteTab.getText());
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mViewPager.getCurrentItem()) {
                case PAGE_ONE:
                    mMeetingTab.setChecked(true);
                    break;
                case PAGE_TWO:
                    mDelegateTab.setChecked(true);
                    break;
                case PAGE_THREE:
                    mAgendaTab.setChecked(true);
                    break;
                case PAGE_FOUR:
                    mFileTab.setChecked(true);
                    break;
                case PAGE_FIVE:
                    mAboutTab.setChecked(true);
                    break;
                case PAGE_SIX:
                    mVoteTab.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getCurrentTitle() {
        return mNavBarView.mTvTitle.getText().toString();
    }

    @Override
    public void removeControllerView() {
        mRootView.removeView(mControllerView);
    }

    /**
     * 重新添加控制条
     *
     * @param reAdd 值为1l
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reAddControllerView(Long reAdd) {
        if (reAdd == TRIGGER_OF_REMOVE_CONTROLLERVIEW) {//从文件详情界面发送
            mRootView.addView(mControllerView, mFl);
            mControllerView.setIOnControllerListener(this);
        }
    }

    /**
     * 跳转到人员界面的  其他参会人员
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDelegate(Boolean showDelegate) {
        if (showDelegate)
            mDelegateTab.setChecked(showDelegate);
    }

    //接收信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getControllerInfoBean(ControllerInfoBean data) {
        if (MyAPP.getInstance().getUserRole() != 1) {
            mMainPresenter.handleControllerInfoBean(data);
            mMeetingState = data.getMeetingState();
        }
    }

    //主持人在文件详情界面开始会议之后，销毁掉文件详情界面，通知主界面显示投票界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hostShowVoteFragment(Integer votePage) {
        if (PAGE_SIX == votePage) {
            SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_VOTE_BEGIN,true);
            mVoteTab.setChecked(true);
            //设置控制条只有结束投票可点击
            mControllerView.setStartAndEndButtonNotClickable(false);
            mControllerView.setPauseAndContinueButtonNotClickable(false);
        }
    }

    //文件详情界面主持人控制会议后，将当前会议状态信息发送过来，这里处理当前会议状态，在右上角显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCurrentMeetingState(String meetingState) {
        mNavBarView.setMeetingStateOrAgendaState(meetingState);
        if ("暂停中".equals(meetingState)) {
            mMeetingState = Constant.MEETING_STATE_PAUSE;
        } else if ("开会中".equals(meetingState)) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
        } else if ("投票中".equals(meetingState)) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
        }
    }

    //客户端文件详情接收到投票命令，销毁详情页面后发信息到达此页面，进行处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientShowVoteFragment(Vote vote) {
        if (vote != null) {
            SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID,vote.getVoteID());
            mVoteTab.setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeControlView(RemoveControlViewEvent removeControlViewEvent){
        mRootView.removeView(mControllerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mVoteResultDialog != null && mVoteResultDialog.isShowing()){
            mVoteResultDialog.dismiss();
            mVoteResultDialog = null;
        }
    }

    @Override
    public void startMeeting(View view) {
        if ("开始".equals(((Button) view).getText().toString())) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            mMainPresenter.hostStartMeeting(mControllerInfoBean,mMeetingState);
        } else {//结束
            if (mMeetingState == Constant.MEETING_STATE_NOT_BEGIN){
                ToastUtil.showMessage("会议未开始！");
                return;
            }
            if (mMeetingState == Constant.MEETING_STATE_ENDING){
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            mMeetingState = Constant.MEETING_STATE_ENDING;
            mMainPresenter.hostEndMeeting(mControllerInfoBean,mMeetingState);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMeetingEnd(MeetingEnd meetingEnd){
        if (meetingEnd.getFlag() == 1){
            mMeetingState = Constant.MEETING_STATE_ENDING;
            //存储会议结束的时间，时  分
            SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR,mNavBarView.getTimeHour());
            SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN,mNavBarView.getTimeMin());
            //停止时间
            mHandler.removeCallbacksAndMessages(null);
            mNavBarView.setMeetingStateOrAgendaState("已结束");
            SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END,true);
            mMeetingTab.setChecked(true);
            EventBus.getDefault().post(new MeetingEnd(2));
        }
    }

    @Override
    public void pauseMeeting(View view) {
        if ("暂停".equals(((Button) view).getText().toString())) {
            if (mMeetingState == Constant.MEETING_STATE_NOT_BEGIN){
                ToastUtil.showMessage("会议未开始！");
                return;
            }
            if (mMeetingState == Constant.MEETING_STATE_ENDING){
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            mMeetingState = Constant.MEETING_STATE_PAUSE;
            mMainPresenter.hostPauseMeeting(mControllerInfoBean,mMeetingState);
        } else {//继续
            mMeetingState = Constant.MEETING_STATE_CONTINUE;
            mMainPresenter.hostContinueMeeting(mControllerInfoBean,mMeetingState);
        }
    }

    @Override
    public void startVote(View view) {
        String buttonContent = mControllerView.getVoteAndEndVoteText();
        if ("投票".equals(buttonContent)) {
            if (mMeetingState == Constant.MEETING_STATE_ENDING){
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            //点击投票只是显示投票列表，不用发送消息给客户端
            mDialog = new VoteListDialog(this);
            mDialog.setOnAllVoteItemClickListener(this);
            mDialog.show();
        } else {
            String deviceIMEI = MPhone.getDeviceIMEI(this);
            int meetingID = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
            //关闭会议成功，这里不需要传消息实体以及会议状态，在关闭成功后再传，
            // 在下面的方法hostResponseCloseVoteSuccess中处理结束投票的操作
            int voteId = SharedPreferencesUtil.getInstance(this).getInt(Constant.BEGIN_VOTE_ID, -1);
            mMainPresenter.hostLaunchOrCloseVote(deviceIMEI,meetingID,voteId,-1,null,0);
        }
    }

    //投票结果  按钮已经去除，不再调用此方法
    @Override
    public void voteResult(View view) {
    }

    @Override
    public void onVoteStartStopButtonClick(View view, int position) {
        String deviceIMEI = MPhone.getDeviceIMEI(this);
        int meetingID = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
        mVoteId = mDialog.getVoteId();
        SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID,mVoteId);
        mMainPresenter.hostLaunchOrCloseVote(deviceIMEI,meetingID,mVoteId,0,mControllerInfoBean,mMeetingState);
    }

    @Override
    public void onCheckResultButtonClick(View view, int position) {
        int voteId = mDialog.getVoteId();
        //隐藏投票列表对话框
        mDialog.dismiss();
        //显示投票结果
        mVoteResultDialog = new VoteResultDialog(this,voteId);
        mVoteResultDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMeetingState == Constant.MEETING_STATE_BEGIN || mMeetingState == Constant.MEETING_STATE_CONTINUE) {
                ToastUtil.showMessage("会议进行中！！");
                return true;
            } else {
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("退出系统？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppUtil.isServiceRun(MainActivity.this, "com.gzz100.Z100_HuiYi.tcpController.Server")) {
                                    stopService(new Intent(MainActivity.this, Server.class));
                                }
                                if (AppUtil.isServiceRun(MainActivity.this, "com.gzz100.Z100_HuiYi.tcpController.Client")) {
                                    stopService(new Intent(MainActivity.this, Client.class));
                                }
                                if (AppUtil.isServiceRun(MainActivity.this, "com.gzz100.Z100_HuiYi.multicast.SendMulticastService")) {
                                    stopService(new Intent(MainActivity.this, Client.class));
                                }
                                SharedPreferencesUtil.getInstance(MainActivity.this).clearKeyInfo();
                                //删除会议前预下载的所有文件
                                AppUtil.DeleteFolder(AppUtil.getCacheDir(MainActivity.this));
                                MainActivity.this.deleteDatabase(DBHelper.DB_NAME);
                                ActivityStackManager.exit();
                            }
                        })
                        .setNegativeButton("否", null)
                        .create();
                dialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void clientResponseMeetingBegin(int agendaIndex, int documentIndex, String upLevelTitle) {
        mRootView.removeView(mControllerView);
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        FileDetailActivity.start(this, agendaIndex, documentIndex, upLevelTitle, true,
                false, false, "", "");
    }

    @Override
    public void clientResponseMeetingVote(int VoteId) {
        mVoteTab.setChecked(true);
        mNavBarView.setMeetingStateOrAgendaState("投票中");
    }

    @Override
    public void clientResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                              String countingMin, String countingSec) {
        FileDetailActivity.start(this, agendaIndex, documentIndex, upLevelTitle, true, false,
                true, countingMin, countingSec);
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mVoteFragment.onDestroyView();
    }

    @Override
    public void clientResponseMeetingPause() {
        mNavBarView.setMeetingStateOrAgendaState("暂停中");
    }

    @Override
    public void clientResponseMeetingEnd() {
        mNavBarView.setMeetingStateOrAgendaState("已结束");
        SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END,true);
        EventBus.getDefault().post(new MeetingEnd(2));
        //存储会议结束的时间，时  分
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR,mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN,mNavBarView.getTimeMin());
        //停止时间
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void hostResponseMeetingBegin(int agendaIndex, int documentIndex, String upLevelTitle) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentIndex, upLevelTitle, true, true, false, "", "");
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mControllerView.setBeginAndEndText("结束");
    }
    @Override
    public void hostResponseMeetingPause() {
        mControllerView.setPauseAndContinueText("继续");
        mNavBarView.setMeetingStateOrAgendaState("暂停中");
    }

    @Override
    public void hostEndMeetingSuccess() {
        //存储会议结束的时间，时  分
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR,mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN,mNavBarView.getTimeMin());
        //停止时间
        mHandler.removeCallbacksAndMessages(null);
        mNavBarView.setMeetingStateOrAgendaState("已结束");
        SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END,true);
        mMeetingTab.setChecked(true);
        EventBus.getDefault().post(new MeetingEnd(2));
        //将控制条全部设置为不能点击
        mControllerView.setStartAndEndButtonNotClickable(false);
        mControllerView.setPauseAndContinueButtonNotClickable(false);
        mControllerView.setStartVoteButtonNotClickable(false);
    }

    @Override
    public void hostResponseMeetingEnd() {
        String deviceIMEI = MPhone.getDeviceIMEI(this);
        int meetingId = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
        mMainPresenter.hostStartEndMeeting(deviceIMEI,meetingId);
    }

    @Override
    public void hostResponseEndVoteWithoutStart(int agendaIndex, int documentId, String upTitleText) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex, documentId, upTitleText, true, true, false, "", "");
    }

    @Override
    public void hostResponseEndVoteAlreadyStart(int agendaIndex, int documentId, String upTitleText,
                                                String countingMin, String countingSec) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentId, upTitleText, true, true, true, countingMin, countingSec);
    }

    @Override
    public void hostResponseVoteEnd() {
        mControllerView.setVoteAndEndVoteText("投票");
        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        //结束投票后，能对控制条的其他按钮进行操作
        mControllerView.setPauseAndContinueButtonNotClickable(true);
        mControllerView.setStartAndEndButtonNotClickable(true);
        mVoteFragment.onDestroyView();

        //存储会议结束的时间，时  分
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR,mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN,mNavBarView.getTimeMin());
    }

    @Override
    public void hostResponseLaunchVoteSuccess() {
        mMeetingState = Constant.MEETING_STATE_BEGIN;
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mControllerView.setBeginAndEndText("结束");
        if ("继续".equals(mControllerView.getBeginAndEndText())){
            mControllerView.setBeginAndEndText("暂停");
        }
        mControllerView.setVoteAndEndVoteText("结束投票");
        //启动投票后，只能结束投票之后才能对控制条的其他按钮进行操作
        mControllerView.setPauseAndContinueButtonNotClickable(false);
        mControllerView.setStartAndEndButtonNotClickable(false);

        //让投票界面加载内容
        mVoteTab.setChecked(true);
        //隐藏投票列表对话框
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void hostResponseCloseVoteSuccess() {
        mMeetingState = Constant.MEETING_STATE_CONTINUE;
        SharedPreferencesUtil.getInstance(this).
                putBoolean(Constant.IS_VOTE_COMMIT, false);
        mMainPresenter.hostEndVote(mControllerInfoBean,mMeetingState);
    }

    @Override
    public void hostResponseLaunchOrCloseVoteFail(int failFlag) {
        if (failFlag == 0){
            ToastUtil.showMessage("开启投票失败！");
        }else {
            ToastUtil.showMessage("关闭投票失败");
        }
    }

    @Override
    public void hostResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                            String countingMin, String countingSec) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentIndex, "文件", true, true, true, countingMin, countingSec);
        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setMeetingStateOrAgendaState("开会中");
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mMainPresenter = presenter;
    }
}
