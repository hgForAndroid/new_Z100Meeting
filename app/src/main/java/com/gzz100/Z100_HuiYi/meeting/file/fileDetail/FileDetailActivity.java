package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.eventBean.Home;
import com.gzz100.Z100_HuiYi.data.eventBean.UpdateCompletedEvent;
import com.gzz100.Z100_HuiYi.data.eventBean.DownLoadComplete;
import com.gzz100.Z100_HuiYi.data.eventBean.PeopleIn;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.meeting.ControllerView;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingEnd;
import com.gzz100.Z100_HuiYi.meeting.vote.IVoteDialogDelete;
import com.gzz100.Z100_HuiYi.meeting.vote.OnAllVoteItemClickListener;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteListDialog;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteResultDialog;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.service.DownLoadService;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.timerService.AgendaOrder;
import com.gzz100.Z100_HuiYi.timerService.AgendaTimerService;
import com.gzz100.Z100_HuiYi.timerService.TimeFlag;
import com.gzz100.Z100_HuiYi.timerService.AgendaTimer;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.StringUtils;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity implements FileDetailContract.DetailView,
        View.OnClickListener, AdapterView.OnItemClickListener, ControllerView.IOnControllerListener,
        OnAllVoteItemClickListener {
    private static final String BUNDLE = "bundle";
    private static final String AGENDA_INDEX = "agendaIndex";
    private static final String FILE_INDEX = "fileIndex";
    private static final String UP_LEVEL_TITLE = "upLevelTitle";
    private static final String PASSIVE = "passive";
    private static final String IS_HOST_FROM_MAIN = "isHostFromMain";
    private static final String IS_AGENDA_TIME_COUNT_DOWN = "isAgendaTimeCountDown";
    private static final String MIN = "min";
    private static final String SEC = "sec";
    private Dialog mTipDialog;

    /**
     * 跳转到文件详情界面
     *
     * @param context               context
     * @param agendaIndex           当前议程序号
     * @param fileIndex             选择的文件序号
     * @param upLevelTitle          当前界面的标题
     * @param passive               是否被动跳转
     * @param isHostFromMain        主持人从主界面跳转过来
     * @param isAgendaTimeCountDown 议程已经开始，并且已经倒计时过了,
     *                              若为false，则min跟sec可以传（空字符串）“”
     * @param min                   倒计时的分
     * @param sec                   倒计时的秒
     */
    public static void start(Context context, int agendaIndex, int fileIndex, String upLevelTitle,
                             boolean passive, boolean isHostFromMain, boolean isAgendaTimeCountDown,
                             String min, String sec) {
        Intent starter = new Intent(context, FileDetailActivity.class);
        Bundle extraBundle = new Bundle();
        extraBundle.putInt(AGENDA_INDEX, agendaIndex);
        extraBundle.putInt(FILE_INDEX, fileIndex);
        extraBundle.putString(UP_LEVEL_TITLE, upLevelTitle);
        extraBundle.putBoolean(PASSIVE, passive);
        extraBundle.putBoolean(IS_HOST_FROM_MAIN, isHostFromMain);
        extraBundle.putBoolean(IS_AGENDA_TIME_COUNT_DOWN, isAgendaTimeCountDown);
        extraBundle.putString(MIN, min);
        extraBundle.putString(SEC, sec);
        starter.putExtra(BUNDLE, extraBundle);
        context.startActivity(starter);
    }

    @BindView(R.id.id_main_fl_root)
    FrameLayout mRootView;//文件界面的根部，用于添加和移除控制条
    View mSlideLayout;//侧边文件列表的布局
    @BindView(R.id.id_file_detail_tbv)
    NavBarView mNavBarView;//导航栏
    @BindView(R.id.id_slide_rev)
    ListView mFileNameRcv;//侧边文件列表的显示控件
    @BindView(R.id.id_web_view_file_detail)
    WebView mWebView;//显示PDF 文件的控件
    @BindView(R.id.id_ll_file_detail_controller)
    LinearLayout mLayoutController;//文件列表下面的上一个，下一个操作的布局，角色为主持人时才显示
    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;//上一个
    @BindView(R.id.id_btn_next)
    Button mBtnNext;//下一个
    @BindView(R.id.id_tv_file_detail_side_layout_agenda)
    TextView mTvFileDetailSideLayoutAgenda;
    @BindView(R.id.id_file_detail_drawer_layout)
    DrawerLayout mDrawerLayout;
    //显示向左的箭头，指明控制条的滑出方向
    @BindView(R.id.id_iv_control_slide_to_right)
    ImageView mControlSideToLeft;
    private ControllerView mControllerView;//控制条
    private FrameLayout.LayoutParams mFl;//控制条的位置
    /**
     * 议程时间是否倒计时过。
     * true：表示已经开始过会议，因为暂停或者临时去投票，让其中断过。
     * false：表示没有开始过会议。
     */
    private boolean mIsAgendaTimeCountDown;
    private String mCountingMin;//倒计时，分
    private String mCountingSec;//倒计时，秒
    private VoteListDialog mVoteListDialog;//投票列表对话框
    private int mAgendaSum;//议程总数
    private int mAgendaIndex;//当前的议程序号
    private int mFileIndex;//当前显示的文件序号
    private List<DocumentModel> mFileList;//文件列表
    private int mAgendaDuration;//当前议程时间
    private String mUpLevelText;//上一级界面的名称
    /**
     * 是否是被动状态，支持人点击开始或继续而跳转进来的，代表被动，值为true
     */
    private boolean mPassive = false;
    /**
     * 主持人设备从主界面跳转过来本界面，不是从文件界面点击文件跳转。
     * 该值为true时，代表主持人点击控制条的开始或者继续。
     * 否则其他情况均为false。
     */
    private boolean mIsHostFromMain;
    private String mFileName;//当前显示的文件的名称
    //消息的实体类，只实例一次，之后发送该实体类，使用原型模式，避免多次创建同一个类
    private ControllerInfoBean mControllerInfoBean = new ControllerInfoBean();
    //开会中有切换议程时，切换后的议程文件列表
    private List<DocumentModel> mDocuments;
    private Bundle mBundle;//跳转到本界面承载传递值的类
    private FileDetailContract.Presenter mPresenter;//P
    private FileDetailAdapter mFileDetailAdapter;//侧边栏显示文件的适配器
    //当前议程的分，秒
    private int mMin, mSec;
    //会议状态的标识
    private int mMeetingState;
    /**
     * 是否为主持人
     */
    private boolean isHost;
    private int mVoteId;//投票id
    private ProgressDialog mProgressDialog;//下载文件时的进度对话框

    private boolean mIsAgendaChange = false;//是否已经切换了议程
    private int tempMin, tempSec;//临时的分秒变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mSlideLayout = findViewById(R.id.id_slide_layout);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initDrawerLayout();
        initWebView();
        //根据用户角色，是否显示控制界面
        showControlBar();
        mPresenter = new FileDetailPresenter(null, this, this);
        //获取传过来的数据
        dataFormUpLevel();
    }

    private void initDrawerLayout() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.openDrawer(Gravity.LEFT);
//        mDrawerLayout.openDrawer(Gravity.RIGHT | Gravity.END);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView instanceof LinearLayout)
                    mControlSideToLeft.setAlpha(1 - slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @OnClick(R.id.id_iv_control_slide_to_right)
    public void showControllerView(){
        if (!mDrawerLayout.isDrawerOpen(Gravity.END)){
            mDrawerLayout.openDrawer(Gravity.END);
        }
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        //支持双指缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //隐藏自带放大缩小控制条
        settings.setDisplayZoomControls(false);
    }

    private void showControlBar() {
        int userRole = MyAPP.getInstance().getUserRole();
        if (userRole == 1) {//主持人
            isHost = true;
            //添加控制条，并且设置控制条每个按钮的监听
            mControllerView = ControllerView.getInstance(this);
            DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.END | Gravity.RIGHT;
            mDrawerLayout.addView(mControllerView, layoutParams);

//            mFl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mFl.gravity = Gravity.RIGHT | Gravity.END;
//            mRootView.addView(mControllerView, mFl);

            mControllerView.setIOnControllerListener(this);
            mControllerView.setOnHideControllerViewListener(new ControllerView.IOnHideControllerViewListener() {
                @Override
                public void hide(View view) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.END)){
                        mDrawerLayout.closeDrawer(Gravity.END);
                    }
                }
            });
        } else {//其他参会人员
            isHost = false;
        }
    }

    private void dataFormUpLevel() {
        mBundle = getIntent().getExtras().getBundle(BUNDLE);
        if (mBundle != null) {//接收传递到当前界面的数据，作于填充界面
            mAgendaIndex = mBundle.getInt(AGENDA_INDEX);
            mFileIndex = mBundle.getInt(FILE_INDEX);
            mUpLevelText = mBundle.getString(UP_LEVEL_TITLE);
            mPassive = mBundle.getBoolean(PASSIVE, false);
            mIsHostFromMain = mBundle.getBoolean(IS_HOST_FROM_MAIN, false);
            mIsAgendaTimeCountDown = mBundle.getBoolean(IS_AGENDA_TIME_COUNT_DOWN, false);
            mCountingMin = mBundle.getString(MIN);
            mCountingSec = mBundle.getString(SEC);
            List<AgendaModel> agendas = FileOperate.getInstance(this).queryAgendaList();
            mAgendaSum = agendas.size();
            mAgendaDuration = agendas.get(mAgendaIndex - 1).getAgendaDuration();//返回的议程序号从1开始
            mFileList = FileOperate.getInstance(this).queryFileList(mAgendaIndex);
            if (mFileList != null && mFileList.size() > 0) {
                mDocuments = mFileList;
                mFileName = mFileList.get(mFileIndex).getDocumentName();
            }
            initData();
        }
    }

    /**
     * 更新倒计时，在{@link AgendaTimerService#mHandler}的handleMessage方法中调用。
     *
     * @param timer
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTimer(AgendaTimer timer) {
        if ("00".equals(timer.getMin()) && "00".equals(timer.getSec())) {
            ToastUtil.showMessage("议程已结束");
            mNavBarView.setTimeHour("00");
            mNavBarView.setTimeMin("00");
            /**
             * 议程时间结束，通知TimerService结束倒计时。
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
             */
            EventBus.getDefault().post(new AgendaOrder(AgendaTimerService.AGENDA_TIME_FINISH));
        } else {
            mNavBarView.setTimeHour(timer.getMin());
            mNavBarView.setTimeMin(timer.getSec());
        }
    }

    /**
     * 设置议程进度
     *
     * @param agendaIndex
     * @param agendaSum
     */
    private void setAgendaProgress(int agendaIndex, int agendaSum) {
        mTvFileDetailSideLayoutAgenda.setText("议程" + agendaIndex + "/" + agendaSum);
    }

    private void initData() {
//        mNavBarView.setMeetingStateOrAgendaStateDisplay(false);
        mNavBarView.setMeetingStateOrAgendaState("议程倒计时:");
        //隐藏标题旁边的会议状态跟时间
        mNavBarView.setMeetingAndTimeDisplay(false);
        //设置  "关于"   隐藏
        mNavBarView.setAboutDisplay(false);
        mNavBarView.setFallBackDisplay(true);//显示返回的按钮
        mNavBarView.setFallBackListener(this);
        if (TextUtils.isEmpty(mFileName)) {
//            mNavBarView.setTitle("当前议程没有文件");
            mNavBarView.setTitleWithSpace("当前议程没有文件");
        } else {
            String name = mFileName.substring(0, mFileName.indexOf("."));
            if ((name.length() > 12)) {
                mNavBarView.setTitleNameSize(20);
            }
//            mNavBarView.setTitle(name);//当前文件名称
            mNavBarView.setTitleWithSpace("查看文件");//当前文件名称
        }
        mNavBarView.setUpLevelText(mUpLevelText);//上一级名称
        //当前议程的在全部议程中的进度
//        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        setAgendaProgress(mAgendaIndex, mAgendaSum);

        //设置当前的会议状态，mPassive为true，代表主持人点击了开始或者继续
        if (mPassive) {
            mNavBarView.setCurrentMeetingState("（开会中）");
        } else {
            int meetingIsProgress = MyAPP.getInstance().isMeetingIsProgress();
            if (meetingIsProgress == 2) {
                mNavBarView.setCurrentMeetingState("（开会中）");
            } else if (meetingIsProgress == 4) {
                mNavBarView.setCurrentMeetingState("（暂停中）");
            } else if (meetingIsProgress == 8) {
                mNavBarView.setCurrentMeetingState("（已结束）");
            } else {
                mNavBarView.setCurrentMeetingState("（未开始）");
            }
        }
        if (mIsHostFromMain) {//主持人点击控制条的开始或者继续，需要重置开会状态
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            mIsHostFromMain = !mIsHostFromMain;//对下文没有影响，可以忽略
        }
        if (mIsAgendaTimeCountDown) {//开始过会议，继续会议需要重新设置回上次停止的时间
            mMin = Integer.valueOf(mCountingMin);
            mSec = Integer.valueOf(mCountingSec);
            if (mMin >= 0 && mSec >= 0) {
                mNavBarView.setTimeHour(mCountingMin);
                mNavBarView.setTimeMin(mCountingSec);
                startTimerService();
                /**
                 * TODO 更新时间
                 * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
                 */
                AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_CONTINUE);
                agendaOrder.setContinueMin(mMin);
                agendaOrder.setContinueSec(mSec);
                EventBus.getDefault().post(agendaOrder);
            } else {
                //在主界面点击开会按钮进入此页面的
                mMeetingState = Constant.MEETING_STATE_BEGIN;
            }
        } else {
            //议程时间是int值，代表议程的时长（分钟），所以秒钟都以0开始
            mMin = mAgendaDuration;
            mSec = 0;
            mNavBarView.setTimeHour(StringUtils.resetNum(mMin));
            mNavBarView.setTimeMin(StringUtils.resetNum(mSec));
            startTimerService();
        }
        if (mFileList != null && mFileList.size() > 0) {
            //设置文件列表数据
            mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileNameRcv.setSelection(mFileIndex);
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
            //加载文件显示
            mPresenter.loadFile(mFileList.get(mFileIndex));
        } else {
            ToastUtil.showMessage(R.string.string_no_file);
            mFileNameRcv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 如果没有启动过服务，就启动服务
     */
    private void startTimerService() {
        if (!AppUtil.isServiceRun(this, "com.gzz100.Z100_HuiYi.timerService.AgendaTimerService")) {
            tempPeopleIn = true;
            //开启议程时间倒计时
            Intent intent = new Intent(this, AgendaTimerService.class);
            intent.putExtra("min", mMin);
            intent.putExtra("sec", mSec);
            startService(intent);
        } else {
            //暂停过，再继续
            continueAfterPause = true;
        }
    }

    private boolean continueAfterPause;
    //设置这个标志，是因为会议开启过程中，有人临时进来，也要让议程的倒计时开始
    private boolean tempPeopleIn = false;

    private boolean flag = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!flag) {
            //倒计时过，临时有人进来，倒计时开始
            if (tempPeopleIn && mIsAgendaTimeCountDown) {
                countingTime();
                tempPeopleIn = false;
            }
            //如果mIsAgendaTimeCountDown为true的话，会在上面就已经发送倒计时的命令，那么就不能继续发命令
            if (!mIsAgendaTimeCountDown) {
                countingTime();
            }
            if (continueAfterPause) {
                countingTime();
                continueAfterPause = false;
            }
            flag = true;
        }
    }

    private void countingTime() {//倒计时
        if (mPassive) {
            //如果会议继续，但是分秒都已为0 ，不进行倒计时
            if (mIsAgendaTimeCountDown && mSec == 0 && mMin == 0) {
                ToastUtil.showMessage(R.string.string_current_agenda_already_end);
            } else {
                /**
                 * //TODO 通知TimerService倒计时
                 * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}调用
                 */
                EventBus.getDefault().post(new AgendaOrder(Constant.MEETING_STATE_BEGIN));
            }
        }
    }

    @Override
    public void startMeeting(View view) {
        if ("开始".equals(((Button) view).getText().toString())) {
            showTipForControl("会议提示", "开始会议？", new OnTipContent() {
                @Override
                public void content() {
                    mMeetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.begin(mControllerInfoBean, mMeetingState, mAgendaIndex, mFileIndex, mUpLevelText);
                    /**
                     * 通知主界面显示状态为“开会中”。
                     * 调用{@link MainActivity#setCurrentMeetingState(String)}
                     */
                    EventBus.getDefault().post("开会中");//通知主界面更新会议状态
                }
            });
        } else {//结束
            showTipForControl("会议提示", "是否结束当前会议？", new OnTipContent() {
                @Override
                public void content() {
                    String deviceIMEI = MPhone.getDeviceIMEI(FileDetailActivity.this);
                    int meetingId = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getInt(Constant.MEETING_ID, -1);
                    //开始访问服务器，请求结束会议，成功后调用endMeetingSuccess
                    mPresenter.startEndMeeting(deviceIMEI, meetingId);
                }
            });
        }
    }

    @Override
    public void endMeetingSuccess() {
        mMeetingState = Constant.MEETING_STATE_ENDING;
        mPresenter.ending(mControllerInfoBean, mMeetingState);
        /**
         * 通知主界面显示状态为“已结束”。
         * 调用{@link MainActivity#setCurrentMeetingState(String)}
         */
        EventBus.getDefault().post("已结束");//通知主界面更新会议状态
    }

    @Override
    public void pauseMeeting(View view) {
        if ("暂停".equals(((Button) view).getText().toString())) {
            showTipForControl("会议提示", "是否暂停会议？", new OnTipContent() {
                @Override
                public void content() {
                    mMeetingState = Constant.MEETING_STATE_PAUSE;
                    //通知主持人端和所有客户端响应暂停事件
                    mPresenter.pause(mControllerInfoBean, mMeetingState);
                    /**
                     * 通知主界面显示状态为“暂停中”。
                     * 调用{@link MainActivity#setCurrentMeetingState(String)}
                     */
                    EventBus.getDefault().post("暂停中");//通知主界面更新会议状态

                }
            });
        } else if ("开会".equals(((Button) view).getText().toString())) {
            showTipForControl("会议提示", "是否返回开会状态？", new OnTipContent() {
                @Override
                public void content() {
                    mNavBarView.setCurrentMeetingState("（开会中）");
                    mControllerView.setPauseAndContinueText("暂停");

                    /**
                     * 见{@link AgendaTimerService#handleTimeFlag(TimeFlag)}
                     */
                    EventBus.getDefault().post(new TimeFlag(true));

                    MyAPP.getInstance().setHostOutOfMeeting(false);

                    int pauseAgendaIndex = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
                    int pauseDocumentIndex = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
                    agendaContentChange(pauseAgendaIndex, pauseDocumentIndex);

                    mMeetingState = Constant.MEETING_STATE_BEGIN;
                    mPassive = true;
                }
            });
        } else {//继续
            showTipForControl("会议提示", "继续会议？", new OnTipContent() {
                @Override
                public void content() {
                    mMeetingState = Constant.MEETING_STATE_CONTINUE;
                    //为了暂停时，设备不在文件详情界面，而从主界面跳进来，所以得传递一个当前倒计时时间参数值，
                    String tempCountingMin = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getString(Constant.COUNTING_MIN, "00");
                    String tempCountingSec = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getString(Constant.COUNTING_SEC, "00");
                    int pauseAgendaIndex = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
                    int pauseDocumentIndex = SharedPreferencesUtil.getInstance(FileDetailActivity.this)
                            .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
                    mPresenter.meetingContinue(mControllerInfoBean, mMeetingState, pauseAgendaIndex, pauseDocumentIndex,
                            mUpLevelText, mIsAgendaChange, true, tempCountingMin, tempCountingSec);
                    /**
                     * 通知主界面显示状态为“开会中”。
                     * 调用{@link MainActivity#setCurrentMeetingState(String)}
                     */
                    EventBus.getDefault().post("开会中");//通知主界面更新会议状态
                }
            });
        }
    }

    @Override
    public void startVote(View view) {//点击开始投票，显示投票列表对话框
        if (MyAPP.getInstance().getHostOutOfMeeting()) {
            ToastUtil.showMessage("请返回会议后再开启投票！");
            return;
        }
        mVoteListDialog = new VoteListDialog(this);
        mVoteListDialog.setOnAllVoteItemClickListener(this);
        mVoteListDialog.setIVoteDialogDelete(new IVoteDialogDelete() {
            @Override
            public void deleteVoteDialog(View view) {
                if (mVoteListDialog != null && mVoteListDialog.isShowing()) {
                    mVoteListDialog.dismiss();
                }
            }
        });
        mVoteListDialog.show();
    }

    /**
     * 弹出的投票的对话框中的 “投票”  按钮。
     *
     * @param view
     * @param position
     */
    @Override
    public void onVoteStartStopButtonClick(View view, int position) {
        if (mMeetingState == Constant.MEETING_STATE_ENDING) {
            ToastUtil.showMessage(R.string.string_meeting_already_ending);
            return;
        }
        //会议是开启状态
        if (mMeetingState == Constant.MEETING_STATE_BEGIN || mMeetingState == Constant.MEETING_STATE_CONTINUE) {
            String deviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
            int meetingId = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getInt(Constant.MEETING_ID, -1);
            mVoteId = mVoteListDialog.getVoteId();
            SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID, mVoteId);
            //启动投票，成功则调用本类方法   launchVoteSuccess
            mPresenter.launchVote(deviceIMEI, meetingId, mVoteId, 0);
        } else {//其他状态，比如暂停
            ToastUtil.showMessage(R.string.string_not_start_meeting);
            return;
        }
    }

    @Override
    public void launchVoteSuccess() {
        //如果控制条投票按钮显示投票，则改变成结束投票，因为投票已经正在开始了
        if ("投票".equals(mControllerView.getVoteAndEndVoteText())) {
            mControllerView.setVoteAndEndVoteText("结束投票");
        }
        /**
         * 显示投票界面，对当前议程状态以及时间的保存。
         * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
         */
        AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_PAUSE);
        agendaOrder.setAgendaIndex(mAgendaIndex);
        agendaOrder.setFileIndex(mFileIndex);
        agendaOrder.setContinueMin(Integer.valueOf(mNavBarView.getTimeHour()));
        agendaOrder.setContinueSec(Integer.valueOf(mNavBarView.getTimeMin()));
        EventBus.getDefault().post(agendaOrder);

        mPresenter.startVote(mControllerInfoBean, mVoteId, mMeetingState);
    }

    /**
     * 弹出的投票的对话框中的 “查看结果”  按钮。
     *
     * @param view
     * @param position
     */
    @Override
    public void onCheckResultButtonClick(View view, int position) {
        int voteId = mVoteListDialog.getVoteId();
        //隐藏投票列表对话框
        mVoteListDialog.dismiss();
        //显示投票结果
        final VoteResultDialog voteResultDialog = new VoteResultDialog(this, voteId);
        voteResultDialog.setIVoteDialogDelete(new IVoteDialogDelete() {
            @Override
            public void deleteVoteDialog(View view) {
                if (voteResultDialog != null && voteResultDialog.isShowing()) {
                    voteResultDialog.dismiss();
                }
            }
        });
        voteResultDialog.show();
    }

    private void removeControllerViewFromParent() {
        mDrawerLayout.removeView(mControllerView);
    }

    @Override
    public void showVote() {
        mVoteListDialog.dismiss();
        /**
         * 通知主界面显示投票的fragment,
         * 调用{@link MainActivity#hostShowVoteFragment(Integer)}
         */
        EventBus.getDefault().post(MainActivity.PAGE_SIX);
//        mRootView.removeView(mControllerView);
//        mDrawerLayout.removeView(mControllerView);
        removeControllerViewFromParent();
        /**
         * 通知主界面重新添加控制条。
         * 调用{@link MainActivity#reAddControllerView(Long)}
         */
        EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
        /**
         * 通知主界面显示状态为“投票中”。
         * 调用{@link MainActivity#setCurrentMeetingState(String)}
         */
        EventBus.getDefault().post("投票中");
        ActivityStackManager.pop();
    }

    @Override//显示结果现在直接显示对话框，所以该方法没有用到
    public void showVoteResult() {
    }


    /**
     * 设置分钟的显示值，如1-9，则在前面加上“0”，否则直接返回该值的字符串。
     *
     * @return 重新设置后的新的值
     */
    private String getMin() {
        String newHour = mMin >= 10 ? "" + mMin : "0" + mMin;
        return newHour;
    }

    /**
     * 设置秒钟的显示值，如1-9，则在前面加上“0”，否则不加
     * 然后，判断该值是否等于“00”，是则代表过了一分钟，将分钟数减1，秒钟重新设置为60，继续倒计时。
     *
     * @param flag 会议进行中主动切换议程。主持人为true，其他为false。
     * @return 重新设置后的新的值。
     */
    private String getSec(boolean flag) {
        String newSex = mSec >= 10 ? "" + mSec : "0" + mSec;
        if (newSex.equals("00")) {
            mMin--;
            if (flag) mSec = 59;
            else mSec = 60;
            return "00";
        }
        return newSex;
    }

    @OnClick(R.id.id_iv_slide_to_left)
        //点击侧边文件列表向左隐藏
    void onToLeft() {
//        mPresenter.slideLeft(null);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.id_iv_slide_to_right)
        //点击侧边文件列表向右显示
    void toRight() {
//        mPresenter.slideRight(null);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    //会议的   开始(结束)，暂停(继续)，投票，显示投票结果，上一个，下一个  点击监听
    @OnClick({R.id.id_btn_previous, R.id.id_btn_next})
    void onControlClick(View v) {
        if (mPassive && !isHost) {//客户端被控制时，不能切换议程
            return;
        }
        switch (v.getId()) {
            case R.id.id_btn_previous://上一个
                if (mAgendaIndex > 1) {
                    mIsAgendaChange = true;
                    if (isHost) {
                        if (mMeetingState == Constant.MEETING_STATE_CONTINUE)
                            mMeetingState = Constant.MEETING_STATE_BEGIN;
                        mPresenter.previousAgendaForHost(mControllerInfoBean, mMeetingState, mAgendaIndex);
                    } else {
                        mPresenter.previousAgenda(mAgendaIndex);
                    }
                }
                break;
            case R.id.id_btn_next://下一个
                if (mAgendaIndex > 0 && mAgendaIndex < mAgendaSum) {
                    mIsAgendaChange = true;
                    if (isHost) {
                        if (mMeetingState == Constant.MEETING_STATE_CONTINUE)
                            mMeetingState = Constant.MEETING_STATE_BEGIN;
                        mPresenter.nextAgendaForHost(mControllerInfoBean, mMeetingState, mAgendaIndex);
                    } else {
                        mPresenter.nextAgenda(mAgendaIndex);
                    }
                }
                break;
        }
    }

    /**
     * 在{@link com.gzz100.Z100_HuiYi.tcpController.Client#mRunnable}中调用。
     * 接收到主持人发送的数据，如果当前设备参会人员不是主持人，则进行处理.
     *
     * @param data
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getControllerInfo(ControllerInfoBean data) {
        if (!isHost) {
            mPresenter.handleMessageFromHost(data);
        }
    }

    @Override
    public void respondAgendaHasChange(ControllerInfoBean controllerInfoBean) {
        if (mAgendaIndex != controllerInfoBean.getAgendaIndex()) {
            //客户端当前的议程跟暂停前的不一致，需要还原
            respondAgendaTimeIsCounting(controllerInfoBean.isAgendaTimeCountDown());
            respondAgendaIndexChange(controllerInfoBean);
        } else {
            //在议程跟暂停前的一致时，客户端当前的文件序号跟暂停前的不一致，需要还原
            if (mFileIndex != controllerInfoBean.getDocumentIndex()) {
                respondDocumentIndexChange(controllerInfoBean.getDocumentIndex());
            }
            //设置文件标题与开会状态
            String name = mFileList.get(mFileIndex).getDocumentName().
                    substring(0, mFileList.get(mFileIndex).getDocumentName().indexOf("."));
            mNavBarView.setTitleWithSpace("查看文件");
//            mNavBarView.setTitle(name);
            mNavBarView.setCurrentMeetingState("(开会中)");
            //时间显示与暂停前的时间显示有不一致的地方，需要重新设置回来，之后再进行时间倒计时
            if (!mNavBarView.getTimeHour().equals(controllerInfoBean.getCountingMin()) ||
                    !mNavBarView.getTimeMin().equals(controllerInfoBean.getCountingSec())) {
                //设置时间
                mMin = Integer.valueOf(controllerInfoBean.getCountingMin());
                mSec = Integer.valueOf(controllerInfoBean.getCountingSec());
                mNavBarView.setTimeHour(getMin());
                mNavBarView.setTimeMin(getSec(true));
            }
            /**
             * TODO
             * 通知TimerService继续倒计时。
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
             */
            AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_CONTINUE);
            agendaOrder.setContinueMin(mMin);
            agendaOrder.setContinueSec(mSec);
            EventBus.getDefault().post(agendaOrder);

        }
        mMeetingState = Constant.MEETING_STATE_BEGIN;
    }

    @Override
    public void respondAgendaNotChange(ControllerInfoBean controllerInfoBean) {
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        mIsAgendaChange = controllerInfoBean.isAgendaChange();
        //设置时间
        mMin = Integer.valueOf(controllerInfoBean.getCountingMin());
        mSec = Integer.valueOf(controllerInfoBean.getCountingSec());
        mNavBarView.setTimeHour(getMin());
        mNavBarView.setTimeMin(getSec(true));
        if (mNavBarView.getTimeHour().equals("00") &&
                mNavBarView.getTimeMin().equals("00")) {
            ToastUtil.showMessage("议程已结束");
        } else {
            /**
             * TODO
             * 通知TimerService继续倒计时。
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
             */
            AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_CONTINUE);
            agendaOrder.setContinueMin(mMin);
            agendaOrder.setContinueSec(mSec);
            EventBus.getDefault().post(agendaOrder);

        }
        if (controllerInfoBean.getAgendaIndex() != mAgendaIndex) {
            //客户端当前界面跟暂停前的界面不一致，需要将界面信息还原到暂停前的状态
            agendaContentChange(controllerInfoBean.getAgendaIndex(), controllerInfoBean.getDocumentIndex());
        } else {
            if (mFileIndex != controllerInfoBean.getDocumentIndex()) {
                //在当前界面，但是，已经切换了议程内的文件
                mFileIndex = controllerInfoBean.getDocumentIndex();
                String name = mDocuments.get(mFileIndex).getDocumentName().
                        substring(0, mDocuments.get(mFileIndex).getDocumentName().indexOf("."));
//                mNavBarView.setTitle(name);//设置文件标题
                mNavBarView.setTitleWithSpace("查看文件");//设置文件标题
                mPresenter.loadFile(mDocuments.get(mFileIndex));//加载文件
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mFileDetailAdapter.notifyDataSetInvalidated();
            }
        }
        mMeetingState = Constant.MEETING_STATE_BEGIN;
    }

    /**
     * 议程内容变化
     *
     * @param agendaIndex   议程序号
     * @param documentIndex 文件序号
     */
    private void agendaContentChange(int agendaIndex, int documentIndex) {
        mAgendaIndex = agendaIndex;
//        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        setAgendaProgress(mAgendaIndex, mAgendaSum);
        mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(mAgendaIndex);
        if (mDocuments != null && mDocuments.size() > 0) {
            mFileList = mDocuments;
            mFileIndex = documentIndex;
            String name = mDocuments.get(mFileIndex).getDocumentName().
                    substring(0, mDocuments.get(mFileIndex).getDocumentName().indexOf("."));
//            mNavBarView.setTitle(name);
            mNavBarView.setTitleWithSpace("查看文件");
            //加载文件
            mPresenter.loadFile(mDocuments.get(mFileIndex));
            mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
            mFileNameRcv.setVisibility(View.VISIBLE);//显示控件
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
        } else {//该议程没有文件
            mFileNameRcv.setVisibility(View.GONE);//隐藏控件
            ToastUtil.showMessage(R.string.string_no_file);
        }
    }

    @Override
    public void resetAgendaTimeCounting(ControllerInfoBean controllerInfoBean, int agendaIndex) {
        if (mMeetingState == Constant.MEETING_STATE_BEGIN ||
                mMeetingState == Constant.MEETING_STATE_CONTINUE) {
            mPassive = true;
            if (agendaIndex != mAgendaIndex) {
                mIsAgendaChange = true;
            }
        }
        if (mIsAgendaChange || mIsAgendaTimeCountDown) {
            if (!mIsAgendaTimeCountDown) {
                //取议程时传入的是mAgendaIndex-1，因为存储议程时，是从0开始的
                int agendaDuration = FileOperate.getInstance(FileDetailActivity.this)
                        .queryAgendaList().get(agendaIndex - 1).getAgendaDuration();
                mMin = agendaDuration;
                mSec = 0;
                mNavBarView.setTimeHour(getMin());
                mNavBarView.setTimeMin(getSec(true));
            } else {
                mMin = Integer.valueOf(controllerInfoBean.getCountingMin());
                mSec = Integer.valueOf(controllerInfoBean.getCountingSec());
                mNavBarView.setTimeHour(controllerInfoBean.getCountingMin());
                mNavBarView.setTimeMin(controllerInfoBean.getCountingSec());
            }
            if (mMeetingState == Constant.MEETING_STATE_BEGIN ||
                    mMeetingState == Constant.MEETING_STATE_CONTINUE) {

//                if (!MyAPP.getInstance().getHostOutOfMeeting()){
                /**
                 * TODO 通知TimerService倒计时。
                 * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
                 */
                AgendaOrder agendaOrder = new AgendaOrder(AgendaTimerService.AGENDA_INDEX_CHANGE);
                agendaOrder.setContinueMin(mMin);
                agendaOrder.setContinueSec(mSec);
                EventBus.getDefault().post(agendaOrder);
//                }

            }
        }
    }

    /**
     * 切换议程后，重设议程的各部分内容
     *
     * @param agendaIndex 议程序号
     */
    @Override
    public void resetAgendaContent(int agendaIndex) {
        if (mIsAgendaChange || mIsAgendaTimeCountDown) {
            mAgendaIndex = agendaIndex;
//            mNavBarView.setMeetingStateOrAgendaState("议程" + agendaIndex + "/" + mAgendaSum);
            setAgendaProgress(agendaIndex, mAgendaSum);
            if (mMeetingState == Constant.MEETING_STATE_BEGIN &&
                    !mNavBarView.getCurrentMeetingStateString().equals("(开会中)")) {
                mNavBarView.setCurrentMeetingState("(开会中)");
            }
            mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(agendaIndex);
            if (mDocuments != null && mDocuments.size() > 0) {
                mFileList = mDocuments;
                mFileIndex = 0;
                String name = mDocuments.get(mFileIndex).getDocumentName().
                        substring(0, mDocuments.get(mFileIndex).getDocumentName().indexOf("."));
//                mNavBarView.setTitle(name);
                mNavBarView.setTitleWithSpace("查看文件");

                mWebView.setVisibility(View.VISIBLE);
                mFileNameRcv.setVisibility(View.VISIBLE);

                mPresenter.loadFile(mDocuments.get(mFileIndex));
                mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
                mFileNameRcv.setAdapter(mFileDetailAdapter);
                mFileNameRcv.setOnItemClickListener(this);
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mFileDetailAdapter.notifyDataSetInvalidated();
            } else {//议程没有文件
                ToastUtil.showMessage(R.string.string_no_file);
                mNavBarView.setTitleWithSpace("当前议程没有文件");
                mWebView.setVisibility(View.GONE);
                mFileNameRcv.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public void respondAgendaTimeIsCounting(boolean isAgendaTimeCountDown) {
        mIsAgendaTimeCountDown = isAgendaTimeCountDown;
    }

    @Override
    public void respondAgendaIndexChange(ControllerInfoBean controllerInfoBean) {
        mMeetingState = Constant.MEETING_STATE_BEGIN;
        resetAgendaTimeCounting(controllerInfoBean, controllerInfoBean.getAgendaIndex());
        resetAgendaContent(controllerInfoBean.getAgendaIndex());
        respondDocumentIndexChange(controllerInfoBean.getDocumentIndex());
    }

    @Override
    public void respondDocumentIndexChange(int documentIndex) {
        mFileIndex = documentIndex;
        mPresenter.loadFile(mFileList.get(documentIndex));
        String name = mFileList.get(documentIndex).getDocumentName().
                substring(0, mFileList.get(documentIndex).getDocumentName().indexOf("."));
//        mNavBarView.setTitle(name);
        mNavBarView.setTitleWithSpace("查看文件");
        mFileDetailAdapter.setSelectedItem(documentIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void respondMeetingBegin(boolean isAgendaChange) {
        mMeetingState = Constant.MEETING_STATE_BEGIN;
        mIsAgendaChange = isAgendaChange;
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_STATE, -1);
    }

    @Override
    public void respondMeetingPause() {//客户端响应暂停指令
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        tempSec = Integer.valueOf(mNavBarView.getTimeMin());
        tempMin = Integer.valueOf(mNavBarView.getTimeHour());
        mIsAgendaChange = false;
        SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_STATE, Constant.MEETING_STATE_PAUSE);

        /**
         * 客户端是否需要？
         * //TODO 通知TimerService保存暂停的时间与议程序号、文件序号
         * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}调用
         */
        AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_PAUSE);
        agendaOrder.setAgendaIndex(mAgendaIndex);
        agendaOrder.setFileIndex(mFileIndex);
        agendaOrder.setContinueMin(tempMin);
        agendaOrder.setContinueSec(tempSec);
        EventBus.getDefault().post(agendaOrder);
    }

    @Override
    public void respondMeetingContinue(boolean isAgendaChange) {
    }

    @Override
    public void respondMeetingEnd() {//客户端响应结束指令
        MyAPP.getInstance().setMeetingIsProgress(8);
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;//客户端不再受控
        /**
         * 停止时间倒计时。
         * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}中调用。
         */
        EventBus.getDefault().post(new AgendaOrder(Constant.MEETING_STATE_ENDING));

        /**
         * 通知主界面当前会议已结束，让其做出相应的处理。
         * 调用{@link MainActivity#handleMeetingEnd(MeetingEnd)}。
         */
//        EventBus.getDefault().post(new MeetingEnd(1));

        ActivityStackManager.pop();
    }

    /**
     * 响应投票.
     *
     * @param voteId
     */
    @Override
    public void respondVoteBegin(int voteId) {
        Vote vote = new Vote();
        vote.setVoteID(voteId);
        /**
         * 客户端接收到投票开始信号，通知主界面，存储投票id，显示投票界面
         * 发送消息给MainActivity，{@link MainActivity#clientShowVoteFragment(Vote)}接收。
         */
        EventBus.getDefault().post(vote);
        ActivityStackManager.pop();//销毁
    }

    /**
     * 同{@link #fileHasUpdate()},{@link #fileHasUpdate()},{@link #fileUpdateError(String)},
     * {@link #showAfterUpdateCompleted(UpdateCompletedEvent)}
     * 这些方法均是更新文件的方法，目前未使用。
     */
    @Override
    public void respondUpdate() {
        mPresenter.updateTheNewDate(mControllerInfoBean, mMeetingState);
        showFileIsDownLoading("正在更新文件中，请稍等...");
        //停止时间倒计时
    }

    @Override
    public void fileHasUpdate() {
        showTipForControl("服务器文件有更新", "马上更新文件,通知客户端更新",
                new OnTipContent() {
                    @Override
                    public void content() {
                        mPresenter.updateTheNewDate(mControllerInfoBean, mMeetingState);
                        showFileIsDownLoading("正在更新文件中，请稍等...");
                        //停止时间倒计时
                    }
                });
    }

    @Override
    public void fileUpdateError(String error) {
        showTipForControl("更新提示", "更新有误，错误：" + error + ",请联系服务器人员。",
                new OnTipContent() {
                    @Override
                    public void content() {
                        mTipDialog.dismiss();
                    }
                });
    }

    /**
     * 会议内容更新完成后，调用该方法。目前更新的功能还未实现，该方法暂时没有调用到。
     * 在{@link com.gzz100.Z100_HuiYi.meetingPrepare.signIn.WriteDatabaseService#splitAndUpdateDateToDatabase(MeetingSummaryBean)}中调用。
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showAfterUpdateCompleted(UpdateCompletedEvent event) {
        if (event.isUpdateCompleted()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            List<AgendaModel> agendas = FileOperate.getInstance(this).queryAgendaList();
            mAgendaSum = agendas.size();
            mAgendaDuration = agendas.get(mAgendaIndex - 1).getAgendaDuration();//返回的议程序号从1开始
            mFileList = FileOperate.getInstance(this).queryFileList(mAgendaIndex);
            mFileName = mFileList.get(mFileIndex).getDocumentName();

//            mNavBarView.setTitle(mFileName.substring(0, mFileName.indexOf(".")));//当前文件名称
            mNavBarView.setTitleWithSpace("查看文件");//当前文件名称
            //当前议程的在全部议程中的进度
//            mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
            setAgendaProgress(mAgendaIndex, mAgendaSum);
            mMin = mAgendaDuration;
            mSec = 0;
            mNavBarView.setTimeHour(StringUtils.resetNum(mMin));
            mNavBarView.setTimeMin(StringUtils.resetNum(mSec));

            mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileNameRcv.setSelection(mFileIndex);
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
            //加载文件显示
            mPresenter.loadFile(mFileList.get(mFileIndex));
        }
    }

    @Override
    public void slideLeft(int distanceX) {
        mSlideLayout.setVisibility(View.GONE);
    }

    @Override
    public void slideRight(int distanceX) {
        mSlideLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(FileDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        mPresenter.fallback();
    }

    @Override
    public void loadFile(String path) {
        mWebView.loadUrl(path);
    }

    /**
     * 文件下载中提示进度条。文件下载已经没有采用，该方法暂未使用到。
     * 该方法同{@link #showFileIsDownLoading(String)}一样，暂未使用到。
     *
     * @param tip
     */
    @Override
    public void showFileIsDownLoading(String tip) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(tip);
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 该方法同{@link #showFileIsDownLoading(String)}一样，暂未使用到。
     * 在{@link DownLoadService#download()}
     * 和{@link DownLoadService#downloadCompleted(String)}中调用到。
     *
     * @param downLoadComplete
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fileDownLoadComplete(DownLoadComplete downLoadComplete) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (downLoadComplete.isDone()) {
            //下载完成，重新加载文件
            DocumentModel documentModel = new DocumentModel();
            documentModel.setDocumentName(downLoadComplete.getFileName());
            mPresenter.loadFile(documentModel);
        } else {
            showTipForControl(downLoadComplete.getFileName(), "是否重新下载", new OnTipContent() {
                @Override
                public void content() {
                    mPresenter.loadFile(mFileList.get(mFileIndex));
                }
            });
        }
    }

    @Override
    public void setMeetingState(int meetingState) {
        mMeetingState = meetingState;
    }

    @Override
    public void meetingBegin() {
        MyAPP.getInstance().setMeetingIsProgress(2);
        mControllerView.setBeginAndEndText("结束");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        startTimerService();
        countingTime();
        SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_STATE, -1);
    }

    @Override
    public void meetingEnding() {
        MyAPP.getInstance().setMeetingIsProgress(8);
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;
        if (!mPassive) {
            /**
             * TODO
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}调用
             */
            EventBus.getDefault().post(new AgendaOrder(Constant.MEETING_STATE_ENDING));
        }
        /**
         * 通知主界面当前会议已结束，让其做出相应的处理。
         * 调用{@link MainActivity#handleMeetingEnd(MeetingEnd)}。
         */
        EventBus.getDefault().post(new MeetingEnd(1));
        //将控制条全部设置为不能点击，主持人不再控制会议
        mControllerView.setStartAndEndButtonNotClickable(false);
        mControllerView.setPauseAndContinueButtonNotClickable(false);
        mControllerView.setStartVoteButtonNotClickable(false);

        //会议结束，关闭所有服务
//        SharedPreferencesUtil.getInstance(this).killAllRunningService();

        if (mControllerView != null) {
            mControllerView.setPauseAndContinueText("开会");
//            mRootView.removeView(mControllerView);
//            mDrawerLayout.removeView(mControllerView);
            removeControllerViewFromParent();
            /**
             * 通知主界面重新添加控制条。
             * 调用{@link MainActivity#reAddControllerView(Long)}
             */
            EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
        }

        ActivityStackManager.pop();
    }

    @Override
    public void meetingPause() {
        MyAPP.getInstance().setMeetingIsProgress(4);
        mControllerView.setPauseAndContinueText("继续");
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        if (!mPassive) {
            /**
             * TODO
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}调用
             */
            AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_PAUSE);
            agendaOrder.setAgendaIndex(mAgendaIndex);
            agendaOrder.setFileIndex(mFileIndex);
            Integer min = Integer.valueOf(mNavBarView.getTimeHour());
            Integer sec = Integer.valueOf(mNavBarView.getTimeMin());
            agendaOrder.setContinueMin(min);
            agendaOrder.setContinueSec(sec);
            EventBus.getDefault().post(agendaOrder);
        }
        mIsAgendaChange = false;
        SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_STATE, Constant.MEETING_STATE_PAUSE);
    }

    @Override
    public void meetingContinue() {//主持人会议继续
        MyAPP.getInstance().setMeetingIsProgress(2);
        String tempCountingMin = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getString(Constant.COUNTING_MIN, "00");//暂停后保存的议程时间倒计时的  分钟
        String tempCountingSec = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getString(Constant.COUNTING_SEC, "00");//暂停后保存的议程时间倒计时的  秒钟
        //如果暂停后，主持人是有切换过议程的，需要还原到上次暂停的界面
        int pauseAgendaIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
        int pauseDocumentIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
        if (mAgendaIndex != pauseAgendaIndex) {
            //当前显示的议程，不是暂停前的议程，则需要重新切换回原来的议程
            agendaContentChange(pauseAgendaIndex, pauseDocumentIndex);
            //将右上角显示的时间切换回暂停前的时间
            mMin = Integer.valueOf(tempCountingMin);
            mSec = Integer.valueOf(tempCountingSec);
            mNavBarView.setTimeHour(tempCountingMin);
            mNavBarView.setTimeMin(tempCountingSec);
        } else {
            //在本界面，如果是已经是暂停后退出过，再次进入
            if (!mNavBarView.getTimeHour().equals(tempCountingMin)
                    && !mNavBarView.getTimeMin().equals(tempCountingSec)) {
                //将右上角显示的时间切换回暂停前的时间
                mMin = Integer.valueOf(tempCountingMin);
                mSec = Integer.valueOf(tempCountingSec);
                mNavBarView.setTimeHour(tempCountingMin);
                mNavBarView.setTimeMin(tempCountingSec);
            }
            //还在本界面，但是当前显示的文件序号跟暂停前的不一样，需要切换到暂停前的议程文件序号
            if (mFileIndex != pauseDocumentIndex) {
                mFileIndex = pauseDocumentIndex;
                String name = mFileList.get(mFileIndex).getDocumentName().
                        substring(0, mFileList.get(mFileIndex).getDocumentName().indexOf("."));
//                mNavBarView.setTitle(name);
                mNavBarView.setTitleWithSpace("查看文件");
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mPresenter.loadFile(mFileList.get(mFileIndex));
                mFileDetailAdapter.notifyDataSetInvalidated();
            }
        }
        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setCurrentMeetingState("(开会中)");
        if (mNavBarView.getTimeHour().equals("00") &&
                mNavBarView.getTimeMin().equals("00")) {
            ToastUtil.showMessage(R.string.string_current_agenda_already_end);
        } else {
            mPassive = true;
            /**
             * //TODO 通知TimerService继续倒计时
             * 在{@link AgendaTimerService#handleOrder(AgendaOrder)}调用
             */
            AgendaOrder agendaOrder = new AgendaOrder(Constant.MEETING_STATE_CONTINUE);
            Integer min = Integer.valueOf(tempCountingMin);
            Integer sec = Integer.valueOf(tempCountingSec);
            agendaOrder.setContinueMin(min);
            agendaOrder.setContinueSec(sec);
            EventBus.getDefault().post(agendaOrder);
        }
        mMeetingState = Constant.MEETING_STATE_BEGIN;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //由主持人点击开始或继续，mPassive为true，暂停、结束为false
        //mPassive为true，代表是主持人控制会议中，isHost为true代表当前设备的用户是主持人
        if (mPassive && !isHost) {//无操作
        } else {
            mFileDetailAdapter.setSelectedItem(position);
            mFileDetailAdapter.notifyDataSetInvalidated();
//        mFileNameRcv.setSelection(position);
            mFileIndex = position;
            mPresenter.loadFile(mFileList.get(position));
            String name = mFileList.get(position).getDocumentName().
                    substring(0, mFileList.get(position).getDocumentName().indexOf("."));
//            mNavBarView.setTitle(name);
            mNavBarView.setTitleWithSpace("查看文件");
        }
        if (isHost &&
                (mMeetingState == Constant.MEETING_STATE_BEGIN ||
                        mMeetingState == Constant.MEETING_STATE_CONTINUE)) {//主持人操作后发送消息通知全部客户端
            mPresenter.handleFileClickFromHost(mControllerInfoBean, mMeetingState, mAgendaIndex, position);
        }
    }

    /**
     * 在{@link com.gzz100.Z100_HuiYi.tcpController.Server#mRunnable}
     * 会议中，临时有人进入会议中，主持人发送消息通知刚进入的平板进入受控状态
     *
     * @param peopleIn isPeopleIn为true，代表有人进入
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void somePeopleIn(PeopleIn peopleIn) {
        if (peopleIn.isPeopleIn()) {
            if (MyAPP.getInstance().getHostOutOfMeeting()) {
                String min = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                        .getString(Constant.COUNTING_MIN, "00");
                String sec = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                        .getString(Constant.COUNTING_SEC, "00");
                int agendaIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                        .getInt(Constant.PAUSE_AGENDA_INDEX, 1);
                int fileIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                        .getInt(Constant.PAUSE_DOCUMENT_INDEX, 1);
                mPresenter.controlTempPeopleIn(peopleIn.getDeviceIp(), mControllerInfoBean, Constant.MEETING_STATE_BEGIN,
                        agendaIndex, fileIndex, mUpLevelText, mIsAgendaChange, true,
                        min, sec);
            } else {
                mPresenter.controlTempPeopleIn(peopleIn.getDeviceIp(), mControllerInfoBean, Constant.MEETING_STATE_BEGIN,
                        mAgendaIndex, mFileIndex, mUpLevelText, mIsAgendaChange, true,
                        mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
            }
        }
    }

    @Override
    public void fallback() {//返回
        //由主持人点击或继续，mPassive为true，暂停、结束为false
        if (mPassive) {//无操作
            hostBackToHomeInProgress(true);
        } else {
            backToHome(true);
        }
    }

    /**
     * 主持人会议进行中退出当前界面
     *
     * @param toHome true，退出的同时，主界面显示会议Tab;false,退出显示进来前的页面
     */
    private void hostBackToHomeInProgress(boolean toHome) {
        if (toHome && isHost) {
            EventBus.getDefault().post(new Home());
        }
        if (isHost) {
            if (mControllerView != null) {
                mControllerView.setPauseAndContinueText("开会");
//                mRootView.removeView(mControllerView);
//                mDrawerLayout.removeView(mControllerView);
                removeControllerViewFromParent();
                /**
                 *
                 * 通知主界面重新添加控制条。
                 * 调用{@link MainActivity#reAddControllerView(Long)}
                 */
                EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
            }
            /**
             * 通知TimerService，不需要将时间发送到文件详情页面显示。
             * 见{@link AgendaTimerService#handleTimeFlag(TimeFlag)}。
             */
            EventBus.getDefault().post(new TimeFlag(false));
            /**
             * 见{@link AgendaTimerService#handleOrder(AgendaOrder)}
             */
            AgendaOrder agendaOrder = new AgendaOrder(AgendaTimerService.HOST_BACK_TO_MAIN);
            agendaOrder.setAgendaIndex(mAgendaIndex);
            agendaOrder.setFileIndex(mFileIndex);
            EventBus.getDefault().post(agendaOrder);

            /**
             * 通知主界面显示状态为“开会中”。
             * 调用{@link MainActivity#setCurrentMeetingState(String)}
             */
            EventBus.getDefault().post("开会中");//通知主界面更新会议状态

            MyAPP.getInstance().setHostOutOfMeeting(true);
            ActivityStackManager.pop();
        }
    }

    /**
     * 退出当前界面，返回主界面
     *
     * @param toHome true，退出的同时，主界面显示会议Tab;false,退出显示进来前的页面
     */
    private void backToHome(boolean toHome) {
        if (toHome) {
            EventBus.getDefault().post(new Home());
        }
        if (isHost) {
            if (mControllerView != null) {
//                mRootView.removeView(mControllerView);
//                mDrawerLayout.removeView(mControllerView);
                removeControllerViewFromParent();
                /**
                 * 通知主界面重新添加控制条。
                 * 调用{@link MainActivity#reAddControllerView(Long)}
                 */
                EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
            }
        }
        ActivityStackManager.pop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //由主持人点击或继续，mPassive为true，暂停、结束为false
            if (mPassive) {
                hostBackToHomeInProgress(false);
            } else {
                backToHome(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVoteListDialog != null) {
            mVoteListDialog.dismiss();
        }
        if (mTipDialog != null) {
            mTipDialog.dismiss();
        }
//        if (mWebView != null) {
//            mWebView.destroy();
//            mWebView.clearCache(true);
//        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 对话框提示
     *
     * @param title        标题
     * @param message      内容
     * @param onTipContent 确定按钮回调
     */
    private void showTipForControl(String title, String message, final OnTipContent onTipContent) {
        mTipDialog = new AlertDialog.Builder(FileDetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.string_ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onTipContent.content();
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
     * 当前类的内部接口，用于所有在当前类弹出的对话框点击确定按钮后的回调。
     */
    interface OnTipContent {
        void content();
    }
}
