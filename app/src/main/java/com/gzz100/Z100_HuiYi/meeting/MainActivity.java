package com.gzz100.Z100_HuiYi.meeting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.eventBean.PeopleIn;
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
import com.gzz100.Z100_HuiYi.meeting.vote.IVoteDialogDelete;
import com.gzz100.Z100_HuiYi.meeting.vote.OnAllVoteItemClickListener;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteFragment;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteListDialog;
import com.gzz100.Z100_HuiYi.meeting.vote.VotePresenter;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteResultDialog;
import com.gzz100.Z100_HuiYi.tcpController.ClientSendMessageUtil;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.StringUtils;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
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
    private VoteListDialog mDialog;
    private int mVoteId;
    private MainContract.Presenter mMainPresenter;
    private VoteResultDialog mVoteResultDialog;
    private MyHandler mMyHandler;
    //提示是否现在开启会议的对话框
    private Dialog mTipDialog;

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 触发主界面移除控制条的标识
     */
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
    private AboutFragment mAboutFragment;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;
    public static final int PAGE_SIX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStackManager.clearExceptOne(this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mMainPresenter = new MainPresenter(this, this.getApplicationContext());
        init();//初始化各个fragment，默认设置，倒计时等
        if (MyAPP.getInstance().getUserRole() != 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //客户端进入会议后，发送一个消息给主持人控制端，询问是否已经开会，
                    // 如已经开会，主持人客户端接收到该消息，会马上控制客户端，消息随意设置，只要跟Server类中获取的一致
                    ClientSendMessageUtil.getInstance().sendMessage("hello");
                }
            }).start();
        }

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
        if (MyAPP.getInstance().getUserRole() == 1) {//主持人
            mControllerView = ControllerView.getInstance(this);
            mFl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mFl.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            mRootView.addView(mControllerView, mFl);
            mControllerView.setIOnControllerListener(this);
            //发送消息的实体
            mControllerInfoBean = new ControllerInfoBean();
        }
        //默认选择哪个Fragment
        defaultSelected();
        initEvent();//滑动，以及tab选择监听
        initPresenter();//初始化各个Fragment 的 Presenter
        mMyHandler = new MyHandler(this);
        mMyHandler.sendEmptyMessageDelayed(0x02, 60000);
    }

    private void initPresenter() {
        new FilePresenter(RepositoryUtil.getFileRepository(this),
                mFileFragment);
        new AgendaPresenter(RepositoryUtil.getFileRepository(this),
                mAgendaFragment);
        new DelegatePresenter(RepositoryUtil.getDelegateRepository(this),
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

    /**
     * 主界面的默认设置
     */
    private void defaultSelected() {
        mMainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mMainFragmentAdapter);
        mViewPager.setCurrentItem(PAGE_ONE);
        mNavBarView.mTvTitle.setText(mMeetingTab.getText());
        mNavBarView.setMeetingStateOrAgendaState("未开始");
        mMeetingTab.setChecked(true);
    }

    /**
     * 静态内部类Handler，使用弱引用，防止内存泄露。
     */
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> activityWeakReference;

        MyHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == 0x02) {
                    activity.min++;
                    activity.mNavBarView.setTimeMin(activity.getMin());
                    activity.mNavBarView.setTimeHour(activity.getHour());
                    if (MyAPP.getInstance().getUserRole() == 1) {
                        SharedPreferencesUtil.getInstance(activity).putString(Constant.MEETING_BEGIN_TIME_HOUR,
                                activity.mNavBarView.getTimeHour());
                        SharedPreferencesUtil.getInstance(activity).putString(Constant.MEETING_BEGIN_TIME_MIN,
                                activity.mNavBarView.getTimeMin());
                    }
                    //一分钟发送一次
                    this.sendEmptyMessageDelayed(0x02, 60000);
                }
            }
        }
    }

    private int hour = 0;
    private int min = 0;

    /**
     * 获取小时的字符串，0-9，在数字前面加0，其他的不加。
     * @return
     */
    public String getHour() {
        String newHour = hour >= 10 ? "" + hour : "0" + hour;
        return newHour;
    }

    /**
     * 获取分钟的字符串，0-9，在数字前面加0，其他的不变。如果到了60，直接变为00。
     * @return
     */
    public String getMin() {
        String newMin = min >= 10 ? "" + min : "0" + min;
        if (newMin.equals("60")) {
            hour++;
            min = 0;
            return "00";
        }
        return newMin;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.id_main_meetingTab://会议主场景
                mViewPager.setCurrentItem(PAGE_ONE);
                mNavBarView.mTvTitle.setText(mMeetingTab.getText());
                break;
            case R.id.id_main_delegateTab://人员界面
                mViewPager.setCurrentItem(PAGE_TWO);
                mNavBarView.mTvTitle.setText(mDelegateTab.getText());
                break;
            case R.id.id_main_agendaTab://议程界面
                mViewPager.setCurrentItem(PAGE_THREE);
                mNavBarView.mTvTitle.setText(mAgendaTab.getText());
                break;
            case R.id.id_main_fileTab://文件界面
                mViewPager.setCurrentItem(PAGE_FOUR);
                mNavBarView.mTvTitle.setText(mFileTab.getText());
                break;
            case R.id.id_main_aboutTab://关于界面
                mViewPager.setCurrentItem(PAGE_FIVE);
                mNavBarView.mTvTitle.setText(mAboutTab.getText());
                break;
            case R.id.id_main_voteTab://投票界面
                mViewPager.setCurrentItem(PAGE_SIX);
                mNavBarView.mTvTitle.setText(mVoteTab.getText());
                break;
            default:break;
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

    /**
     * 移除控制条
     */
    @Override
    public void removeControllerView() {
        mRootView.removeView(mControllerView);
    }

    /**
     * 在{@link FileDetailActivity#showVote()}调用.
     * 从文件详情界面销毁时发送.
     * 重新添加控制条.
     *
     * @param reAdd 值为 1l
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reAddControllerView(Long reAdd) {
        if (reAdd == TRIGGER_OF_REMOVE_CONTROLLERVIEW) {
            mRootView.addView(mControllerView, mFl);
            mControllerView.setIOnControllerListener(this);
        }
    }

    /**
     * 在{@link MeetingFragment#showDelegate(Boolean)}调用
     * 跳转到人员界面。
     * 在会议主场景界面点击右下角的其它参会人员，会通知MainActivity调用此方法。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDelegate(Boolean showDelegate) {
        if (showDelegate)
            mDelegateTab.setChecked(showDelegate);
    }

    /**
     * 在{@link com.gzz100.Z100_HuiYi.tcpController.Client#mRunnable}
     * 接收信息，主持人发出ControllerInfoBean消息实体，所有客户端在MAinActivity时接收并处理该消息。
     * @param data    ControllerInfoBean消息实体
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getControllerInfoBean(ControllerInfoBean data) {
        if (MyAPP.getInstance().getUserRole() != 1) {
            mMainPresenter.handleControllerInfoBean(data);
            mMeetingState = data.getMeetingState();
        }
    }

    /**
     * 在{@link FileDetailActivity#showVote()}
     * 主持人在文件详情界面开始投票之后，销毁掉文件详情界面，通知主界面显示投票界面。
     * 这是主持人端调用的方法。
     * @param votePage   PAGE_SIX = 5，代表投票的fragment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hostShowVoteFragment(Integer votePage) {
        if (PAGE_SIX == votePage) {
            SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_VOTE_BEGIN, true);
            mVoteTab.setChecked(true);
            //设置控制条只有结束投票可点击
//            mControllerView.setStartAndEndButtonNotClickable(false);
//            mControllerView.setPauseAndContinueButtonNotClickable(false);
        }
    }

    /**
     * 在{@link FileDetailActivity#showVote()}.
     * 文件详情界面主持人控制会议后，将当前会议状态信息发送过来，这里处理当前会议状态，在右上角显示.
     * @param meetingState   会议状态
     */
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

    /**
     * 在{@link FileDetailActivity#respondVoteBegin(int)}
     * 客户端文件详情接收到投票命令，销毁详情页面后发信息到达此页面，进行处理
     * @param vote   投票实体，包含投票的id，利用该id显示对应的投票内容。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientShowVoteFragment(Vote vote) {
        if (vote != null) {
            SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID, vote.getVoteID());
            mVoteTab.setChecked(true);//显示投票界面
        }
    }

    /**
     * 在{@link AgendaFragment#showFileDetail()}
     * 移除控制条
     * @param removeControlViewEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeControlView(RemoveControlViewEvent removeControlViewEvent) {
        mRootView.removeView(mControllerView);//移除控制条
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mVoteResultDialog != null && mVoteResultDialog.isShowing()) {
            mVoteResultDialog.dismiss();
            mVoteResultDialog = null;
        }
        if (mMyHandler != null){
            mMyHandler.removeCallbacksAndMessages(null);
            mMyHandler = null;
        }
    }

    /**
     * 点击控制条的开始、结束按钮
     * @param view
     */
    @Override
    public void startMeeting(View view) {
        if ("开始".equals(((Button) view).getText().toString())) {
            //弹出对话框，询问主持人是否立即开启会议
            checkStartMeetingImmediately();
        } else {//结束
            if (mMeetingState == Constant.MEETING_STATE_NOT_BEGIN) {
                ToastUtil.showMessage("会议未开始！");
                return;
            }
            if (mMeetingState == Constant.MEETING_STATE_ENDING) {
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            if (mNavBarView.getMeetingStateOrAgendaState().equals("投票中")){
                ToastUtil.showMessage("当前投票还未结束，请先结束投票！");
                return;
            }
            checkEndingMeetingImmediately();
        }
    }

    private void checkEndingMeetingImmediately() {
        mTipDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.string_tip)
                .setMessage("是否现在结束会议？")
                .setPositiveButton(R.string.string_ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMeetingState = Constant.MEETING_STATE_ENDING;
                        mMainPresenter.hostEndMeeting(mControllerInfoBean, mMeetingState);
                    }
                })
                .setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        mTipDialog.setCanceledOnTouchOutside(false);//只能点击确定或取消才能让对话框消失
    }

    private void checkStartMeetingImmediately() {
        mTipDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.string_tip)
                .setMessage("是否现在开启会议？")
                .setPositiveButton(R.string.string_ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMeetingState = Constant.MEETING_STATE_BEGIN;
                        mMainPresenter.hostStartMeeting(mControllerInfoBean, mMeetingState);
                    }
                })
                .setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        mTipDialog.setCanceledOnTouchOutside(false);//只能点击确定或取消才能让对话框消失
    }

    /**
     * 这个方法的调用方是在文件详情界面，与{@link MainActivity#hostEndMeetingSuccess()}方法不同。
     * 因为这两个方法有共同的方法，但是调用的源头方法不同。
     * 该方法是在{@link FileDetailActivity#meetingEnding()}中调用。
     * 处理会议结束事件。
     * @param meetingEnd
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMeetingEnd(MeetingEnd meetingEnd) {
        if (meetingEnd.getFlag() == 1) {
            mMeetingState = Constant.MEETING_STATE_ENDING;
            //存储会议结束的时间，时  分
            SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR, mNavBarView.getTimeHour());
            SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN, mNavBarView.getTimeMin());
            //停止时间
            mMyHandler.removeCallbacksAndMessages(null);
            mNavBarView.setMeetingStateOrAgendaState("已结束");
            SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END, true);
            mMeetingTab.setChecked(true);
            /**
             * 通知会议界面显示结束后的信息
             * 调用{@link MeetingFragment#showEnd(MeetingEnd)}。
             */
            EventBus.getDefault().post(new MeetingEnd(2));

            //将控制条全部设置为不能点击
            mControllerView.setStartAndEndButtonNotClickable(false);
            mControllerView.setPauseAndContinueButtonNotClickable(false);
            mControllerView.setStartVoteButtonNotClickable(false);
        }
    }

    /**
     * 点击暂停、继续按钮
     * @param view
     */
    @Override
    public void pauseMeeting(View view) {
        if ("暂停".equals(((Button) view).getText().toString())) {
            if (mMeetingState == Constant.MEETING_STATE_NOT_BEGIN) {
                ToastUtil.showMessage("会议未开始！");
                return;
            }
            if (mMeetingState == Constant.MEETING_STATE_ENDING) {
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            if (mNavBarView.getMeetingStateOrAgendaState().equals("投票中")){
                ToastUtil.showMessage("当前投票还未结束，请先结束投票！");
                return;
            }
            mMeetingState = Constant.MEETING_STATE_PAUSE;
            mMainPresenter.hostPauseMeeting(mControllerInfoBean, mMeetingState);
        } else {//继续
            checkContinueMeetingImmediately();
        }
    }

    private void checkContinueMeetingImmediately() {
        mTipDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.string_tip)
                .setMessage("是否现在继续会议？")
                .setPositiveButton(R.string.string_ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMeetingState = Constant.MEETING_STATE_CONTINUE;
                        mMainPresenter.hostContinueMeeting(mControllerInfoBean, mMeetingState);
                    }
                })
                .setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        mTipDialog.setCanceledOnTouchOutside(false);//只能点击确定或取消才能让对话框消失
    }

    /**
     * 点击控制条的投票按钮。
     * @param view
     */
    @Override
    public void startVote(View view) {
        String buttonContent = mControllerView.getVoteAndEndVoteText();
        if ("投票".equals(buttonContent)) {
            if (mMeetingState == Constant.MEETING_STATE_NOT_BEGIN) {
                ToastUtil.showMessage("会议未开始！");
                return;
            }
            if (mMeetingState == Constant.MEETING_STATE_ENDING) {
                ToastUtil.showMessage("会议已结束！");
                return;
            }
            //点击投票只是显示投票列表，不用发送消息给客户端
            mDialog = new VoteListDialog(this);
            mDialog.setOnAllVoteItemClickListener(this);
            mDialog.setIVoteDialogDelete(new IVoteDialogDelete() {
                @Override
                public void deleteVoteDialog(View view) {
                    if (mDialog != null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                }
            });
            mDialog.show();
        } else {
            boolean voteSubmit = SharedPreferencesUtil.getInstance(this).
                    getBoolean(Constant.IS_VOTE_COMMIT, false);
            if (voteSubmit){
                String deviceIMEI = MPhone.getDeviceIMEI(this);
                int meetingID = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
                int voteId = SharedPreferencesUtil.getInstance(this).getInt(Constant.BEGIN_VOTE_ID, -1);
                mMainPresenter.hostLaunchOrCloseVote(deviceIMEI, meetingID, voteId, -1, null, 0);
            }else {
                ToastUtil.showMessage("还未上传投票结果！");
            }
        }
    }

    /**
     * 启动或结束投票
     * @param view
     * @param position
     */
    @Override
    public void onVoteStartStopButtonClick(View view, int position) {
        if (mMeetingState == Constant.MEETING_STATE_ENDING){
            ToastUtil.showMessage(R.string.string_meeting_already_ending);
            return;
        }
        //会议是开启状态
        if (mMeetingState == Constant.MEETING_STATE_BEGIN || mMeetingState == Constant.MEETING_STATE_CONTINUE){
            String deviceIMEI = MPhone.getDeviceIMEI(this);
            int meetingID = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
            mVoteId = mDialog.getVoteId();
            SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID, mVoteId);
            mMainPresenter.hostLaunchOrCloseVote(deviceIMEI, meetingID, mVoteId, 0, mControllerInfoBean, mMeetingState);
        }else {//其他状态，比如暂停
            ToastUtil.showMessage(R.string.string_not_start_meeting);
            return;
        }

    }

    /**
     * 显示投票结果
     * @param view
     * @param position
     */
    @Override
    public void onCheckResultButtonClick(View view, int position) {
        int voteId = mDialog.getVoteId();
        //隐藏投票列表对话框
        mDialog.dismiss();
        //显示投票结果
        mVoteResultDialog = new VoteResultDialog(this, voteId);
        mVoteResultDialog.setIVoteDialogDelete(new IVoteDialogDelete() {
            @Override
            public void deleteVoteDialog(View view) {
                if (mVoteResultDialog != null && mVoteResultDialog.isShowing()){
                    mVoteResultDialog.dismiss();
                }
            }
        });
        mVoteResultDialog.show();
    }

    /**
     * 客户端响应会议开始指令的操作。
     * @param agendaIndex      议程序号
     * @param documentIndex    文件序号
     * @param upLevelTitle     上一级标题
     */
    @Override
    public void clientResponseMeetingBegin(int agendaIndex, int documentIndex, String upLevelTitle) {
        mRootView.removeView(mControllerView);
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        FileDetailActivity.start(this, agendaIndex, documentIndex, upLevelTitle, true,
                false, false, "", "");
    }

    /**
     * 客户端响应投票指令的操作。
     * @param VoteId   投票id
     */
    @Override
    public void clientResponseMeetingVote(int VoteId) {
        mVoteTab.setChecked(true);
        mNavBarView.setMeetingStateOrAgendaState("投票中");
    }

    /**
     * 客户端响应会议继续的指令的操作。
     * @param agendaIndex              议程序号
     * @param documentIndex             文件序号
     * @param upLevelTitle              上级标题
     * @param countingMin               倒计时时间，分
     * @param countingSec               倒计时时间，秒
     * @param meetingBeginTimeHour      会议已开始时间，时
     * @param meetingBeginTimeMin       会议已开始时间，分
     */
    @Override
    public void clientResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                              String countingMin, String countingSec,
                                              String meetingBeginTimeHour, String meetingBeginTimeMin) {
        FileDetailActivity.start(this, agendaIndex, documentIndex, upLevelTitle, true, false,
                true, countingMin, countingSec);
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mVoteFragment.onDestroyView();
        //会议继续时，将所有客户端的会议进行时间与主持人控制端的会议进行时间进行校正
        mNavBarView.setTimeHour(meetingBeginTimeHour);//重设会议进行时间，时
        mNavBarView.setTimeMin(meetingBeginTimeMin);//重设会议进行时间，分
        hour = Integer.valueOf(meetingBeginTimeHour);//时间值赋予当前时间变量
        min = Integer.valueOf(meetingBeginTimeMin);//时间值赋予当前时间变量
        mMyHandler.removeCallbacksAndMessages(null);//移除所有消息
        mMyHandler.sendEmptyMessageDelayed(0x02, 60000);//重新发送会议进行时间计时消息
    }

    /**
     * 在{@link com.gzz100.Z100_HuiYi.tcpController.Server#mRunnable}
     * 会议，临时有人进入会议中，主持人发送消息通知刚进入的平板进入受控状态
     * @param peopleIn
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void somePeopleIn(PeopleIn peopleIn){
        if (peopleIn.isPeopleIn()){
            if (mMeetingState == Constant.MEETING_STATE_PAUSE){
                mMainPresenter.controlTempPeopleIn(peopleIn.getDeviceIp(),mControllerInfoBean, mMeetingState,
                        mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
            }
        }
    }

    /**
     * 临时有人进入会议后，主持人点击继续会议的指令，该客户端响应指令的操作。
     * @param agendaIndex              议程序号
     * @param documentIndex             文件序号
     * @param upLevelTitle              上级标题
     * @param countingMin               倒计时时间，分
     * @param countingSec               倒计时时间，秒
     * @param meetingBeginTimeHour      会议已开始时间，时
     * @param meetingBeginTimeMin       会议已开始时间，分
     */
    @Override
    public void tempClientResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                                  String countingMin, String countingSec,
                                                  String meetingBeginTimeHour, String meetingBeginTimeMin) {
        Integer newSec = Integer.valueOf(countingSec);
        if (newSec > 3) {//在测试中，临时人员进入会议状态后，议程的倒计时时间的秒总是少3秒左右
            countingSec = StringUtils.resetNum(newSec - 3);
        }
        FileDetailActivity.start(this, agendaIndex, documentIndex, upLevelTitle, true, false,
                true, countingMin, countingSec);
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mVoteFragment.onDestroyView();
        mNavBarView.setTimeHour(meetingBeginTimeHour);//重设会议进行时间，时
        mNavBarView.setTimeMin(meetingBeginTimeMin);//重设会议进行时间，分
        hour = Integer.valueOf(meetingBeginTimeHour);//时间值赋予当前时间变量
        min = Integer.valueOf(meetingBeginTimeMin);//时间值赋予当前时间变量
        mMyHandler.removeCallbacksAndMessages(null);//移除所有消息
        mMyHandler.sendEmptyMessageDelayed(0x02, 60000);//重新发送会议进行时间计时消息
    }

    /**
     * 临时有人进入会议后，主持人点击暂停或结束会议的指令，该客户端响应指令的操作。
     * @param meetingState              会议状态
     * @param meetingBeginTimeHour      会议已开始时间，时
     * @param meetingBeginTimeMin       会议已开始时间，分
     */
    @Override
    public void tempClientResponseMeetingPauseOrEnd(int meetingState, String meetingBeginTimeHour, String meetingBeginTimeMin) {
        mNavBarView.setMeetingStateOrAgendaState("暂停中");
        mVoteFragment.onDestroyView();
        mNavBarView.setTimeHour(meetingBeginTimeHour);//重设会议进行时间，时
        mNavBarView.setTimeMin(meetingBeginTimeMin);//重设会议进行时间，分
        hour = Integer.valueOf(meetingBeginTimeHour);//时间值赋予当前时间变量
        min = Integer.valueOf(meetingBeginTimeMin);//时间值赋予当前时间变量
        mMyHandler.removeCallbacksAndMessages(null);//移除所有消息
        mMyHandler.sendEmptyMessageDelayed(0x02, 60000);//重新发送会议进行时间计时消息
    }

    /**
     * 客户端响应会议暂停指令的操作。
     */
    @Override
    public void clientResponseMeetingPause() {
        mNavBarView.setMeetingStateOrAgendaState("暂停中");
    }

    /**
     * 客户端响应会议结束指令的操作。
     */
    @Override
    public void clientResponseMeetingEnd() {
        mNavBarView.setMeetingStateOrAgendaState("已结束");
        SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END, true);
        /**
         * 通知会议界面显示结束后的信息
         * 调用{@link MeetingFragment#showEnd(MeetingEnd)}。
         */
        EventBus.getDefault().post(new MeetingEnd(2));
        //存储会议结束的时间，时  分
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR, mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN, mNavBarView.getTimeMin());
        //停止时间
        mMyHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 主持人端响应会议开始指令的操作。
     * @param agendaIndex      议程序号
     * @param documentIndex     文件序号
     * @param upLevelTitle      上一级标题
     */
    @Override
    public void hostResponseMeetingBegin(int agendaIndex, int documentIndex, String upLevelTitle) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentIndex, upLevelTitle, true, true, false, "", "");
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mControllerView.setBeginAndEndText("结束");
        MyAPP.getInstance().setMeetingIsProgress(2);
    }

    /**
     * 主持人端响应会议暂停指令的操作。
     */
    @Override
    public void hostResponseMeetingPause() {
        MyAPP.getInstance().setMeetingIsProgress(4);
        mControllerView.setPauseAndContinueText("继续");
        mNavBarView.setMeetingStateOrAgendaState("暂停中");
    }

    /**
     * 这个方法的源头调用是在本类的 {@link MainActivity#startMeeting(View)} }中点击结束按钮，
     * 表示主持人在主界面结束会议。
     * 本方法的上一级调用是在{@link MainPresenter#hostStartEndMeeting(String, int)}。
     * 主持人端响应会议结束成功指令的操作。
     */
    @Override
    public void hostEndMeetingSuccess() {
        mMeetingState = Constant.MEETING_STATE_ENDING;
        //存储会议结束的时间，时  分
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR, mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN, mNavBarView.getTimeMin());
        //停止时间
        mMyHandler.removeCallbacksAndMessages(null);
        mNavBarView.setMeetingStateOrAgendaState("已结束");
        SharedPreferencesUtil.getInstance(this).putBoolean(Constant.IS_MEETING_END, true);
        //设置主场景界面(MeetingFragment)显示会议结束后的信息
        mMeetingTab.setChecked(true);
        /**
         * 通知会议界面显示结束后的信息
         * 调用{@link MeetingFragment#showEnd(MeetingEnd)}。
         */
        EventBus.getDefault().post(new MeetingEnd(2));
        //将控制条全部设置为不能点击
        mControllerView.setStartAndEndButtonNotClickable(false);
        mControllerView.setPauseAndContinueButtonNotClickable(false);
        mControllerView.setStartVoteButtonNotClickable(false);
    }

    /**
     * 在{@link MainPresenter#hostEndMeeting(ControllerInfoBean, int)}中调用
     * 主持人端响应会议结束指令的操作。
     */
    @Override
    public void hostResponseMeetingEnd() {
        MyAPP.getInstance().setMeetingIsProgress(8);
        String deviceIMEI = MPhone.getDeviceIMEI(this);
        int meetingId = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
        mMainPresenter.hostStartEndMeeting(deviceIMEI, meetingId);
    }

    /**
     * 没开始投票前，主持人端响应结束投票指令的操作。
     * @param agendaIndex    议程序号
     * @param documentId      文件序号
     * @param upTitleText
     */
    @Override
    public void hostResponseEndVoteWithoutStart(int agendaIndex, int documentId, String upTitleText) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex, documentId, upTitleText, true, true, false, "", "");
    }

    /**
     * 投票已经开始，主持人端响应结束投票指令的操作。
     * @param agendaIndex    原来的议程序号
     * @param documentId     原来的议程文件序号
     * @param upTitleText    上一级标题
     * @param countingMin    议程倒计时分钟
     * @param countingSec     议程倒计时秒数
     */
    @Override
    public void hostResponseEndVoteAlreadyStart(int agendaIndex, int documentId, String upTitleText,
                                                String countingMin, String countingSec) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentId, upTitleText, true, true, true, countingMin, countingSec);
    }

    /**
     * 主持人端响应投票结束。
     */
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
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_HOUR, mNavBarView.getTimeHour());
        SharedPreferencesUtil.getInstance(this).putString(Constant.ENDING_MIN, mNavBarView.getTimeMin());
    }

    /**
     * 主持人端响应启动投票成功指令的操作。
     */
    @Override
    public void hostResponseLaunchVoteSuccess() {
        mMeetingState = Constant.MEETING_STATE_BEGIN;
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        mControllerView.setBeginAndEndText("结束");
        if ("继续".equals(mControllerView.getBeginAndEndText())) {
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

    /**
     * 主持人端响应关闭投票成功的操作。
     */
    @Override
    public void hostResponseCloseVoteSuccess() {
        mMeetingState = Constant.MEETING_STATE_CONTINUE;
        SharedPreferencesUtil.getInstance(this).
                putBoolean(Constant.IS_VOTE_COMMIT, false);
        mMainPresenter.hostEndVote(mControllerInfoBean, mMeetingState);
    }

    /**
     * 主持人端响应开启（关闭）投票失败的指令的操作。
     * @param failFlag   0：代表开启失败   -1：代表关闭失败
     */
    @Override
    public void hostResponseLaunchOrCloseVoteFail(int failFlag) {
        if (failFlag == 0) {
            ToastUtil.showMessage(R.string.string_launch_vote_fail);
        } else {
            ToastUtil.showMessage(R.string.string_close_vote_fail);
        }
    }

    /**
     * 主持人端响应会议继续的指令的操作。
     * @param agendaIndex     暂停前的议程序号
     * @param documentIndex    暂停前的文件序号
     * @param upLevelTitle     上一级标题
     * @param countingMin      暂停前倒计时分钟
     * @param countingSec      暂停前倒计时秒钟
     */
    @Override
    public void hostResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                            String countingMin, String countingSec) {
        mRootView.removeView(mControllerView);
        FileDetailActivity.start(this, agendaIndex,
                documentIndex, "文件", true, true, true, countingMin, countingSec);
        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setMeetingStateOrAgendaState("开会中");
        MyAPP.getInstance().setMeetingIsProgress(2);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mMainPresenter = presenter;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMeetingState == Constant.MEETING_STATE_BEGIN || mMeetingState == Constant.MEETING_STATE_CONTINUE) {
                ToastUtil.showMessage("会议进行中！！");
                return true;
            } else {
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.string_tip)
                        .setMessage(R.string.string_exit_system)
                        .setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (MyAPP.getInstance().getUserRole() != 1) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ClientSendMessageUtil.getInstance().sendMessage("exit");
                                        }
                                    }).start();
                                }
                                if (mTipDialog != null){
                                    mTipDialog.dismiss();                                }
                                SharedPreferencesUtil.getInstance(MainActivity.this).killAllRunningService();
                                SharedPreferencesUtil.getInstance(MainActivity.this).clearKeyInfo();
                                //删除会议前预下载的所有文件
                                AppUtil.DeleteFolder(AppUtil.getCacheDir(MainActivity.this));
                                MainActivity.this.deleteDatabase(DBHelper.DB_NAME);
                                ActivityStackManager.exit();
                            }
                        })
                        .setNegativeButton(R.string.string_no, null)
                        .create();
                dialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
