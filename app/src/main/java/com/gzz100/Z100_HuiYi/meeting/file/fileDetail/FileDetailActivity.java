package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.barteksc.pdfviewer.PDFView;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.eventBean.DownLoadComplete;
import com.gzz100.Z100_HuiYi.data.eventBean.PeopleIn;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.meeting.ControllerView;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingEnd;
import com.gzz100.Z100_HuiYi.meeting.vote.OnAllVoteItemClickListener;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteListDialog;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteResultDialog;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.StringUtils;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity implements FileDetailContract.DetailView,
        View.OnClickListener, AdapterView.OnItemClickListener, ControllerView.IOnControllerListener,
        OnAllVoteItemClickListener {
    public static final String BUNDLE = "bundle";
    public static final String AGENDA_INDEX = "agendaIndex";
    public static final String FILE_INDEX = "fileIndex";
    public static final String UP_LEVEL_TITLE = "upLevelTitle";
    public static final String PASSIVE = "passive";
    public static final String IS_HOST_FROM_MAIN = "isHostFromMain";
    public static final String IS_AGENDA_TIME_COUNT_DOWN = "isAgendaTimeCountDown";
    public static final String MIN = "min";
    public static final String SEC = "sec";

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
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;//显示PDF 文件的控件
    @BindView(R.id.id_ll_file_detail_controller)
    LinearLayout mLayoutController;//文件列表下面的上一个，下一个操作的布局，角色为主持人时才显示
    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;//上一个
    @BindView(R.id.id_btn_next)
    Button mBtnNext;//下一个
    private MyHandler mMyHandler;//处理时间倒计时
    private ControllerView mControllerView;//控制条
    private FrameLayout.LayoutParams mFl;//控制条的位置
    /**
     * 议程时间是否倒计时过。
     * true：表示已经开始过会议，因为暂停或者临时去投票，让其中断过。
     * false：表示没有开始过会议。
     */
    private boolean mIsAgendaTimeCountDown;
    private String mCountingMin;
    private String mCountingSec;
    private VoteListDialog mDialog;
    private int mAgendaSum;//议程总数
    private int mAgendaIndex;//当前的议程序号
    private int mFileIndex;//当前显示的文件序号
    private List<DocumentModel> mFileList;//文件列表
    private int mAgendaDuration;//当前议程时间
    private String mUpLevelText;//上一级界面的名称
    /**
     * 是否是被动状态，支持人点击开始或继续而跳转进来的，代表被动，值为true
     */
    private boolean mPassive;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mSlideLayout = findViewById(R.id.id_slide_layout);
        ButterKnife.bind(this);
        //根据用户角色，是否显示控制界面
        showControlBar();
        mPresenter = new FileDetailPresenter(null, this, this);
        //获取传过来的数据
        dataFormUpLevel();

    }

    private void showControlBar() {
        int userRole = MyAPP.getInstance().getUserRole();
        if (userRole == 1) {//主持人
            isHost = true;
            //添加控制条，并且设置控制条每个按钮的监听
            mControllerView = ControllerView.getInstance(this);
            mFl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mFl.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            mRootView.addView(mControllerView, mFl);
            mControllerView.setIOnControllerListener(this);
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
            mFileName = mFileList.get(mFileIndex).getDocumentName();
        }

        if (mBundle != null)
            initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        //屏幕息屏后，需要移除倒计时，否则重新亮屏后倒计时会错乱
        mMyHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //屏幕息屏后亮屏，重新开始倒计时
        Message message = Message.obtain();
        message.what = 0x00;
        mMyHandler.sendMessage(message);

    }

    private void initData() {
        mNavBarView.setFallBackDisplay(true);//显示返回的按钮
        mNavBarView.setFallBackListener(this);//返回上一级的监听

        mNavBarView.setTitle(mFileName.substring(0, mFileName.indexOf(".")));//当前文件名称
        mNavBarView.setUpLevelText(mUpLevelText);//上一级名称
        //当前议程的在全部议程中的进度
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        //设置当前的会议状态，mPassive为true，代表主持人点击了开始或者继续
        if (mPassive) {
            mNavBarView.setCurrentMeetingState("（开会中）");
        } else {
            mNavBarView.setCurrentMeetingState("（未开始）");
        }
        if (mIsHostFromMain) {//主持人点击控制条的开始或者继续，需要重置开会状态
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            mIsHostFromMain = !mIsHostFromMain;//对下文没有影响，可以忽略
        }
        if (mIsAgendaTimeCountDown) {//开始过会议，继续会议需要重新设置回上次停止的时间
            mMin = Integer.valueOf(mCountingMin);
            mSec = Integer.valueOf(mCountingSec);
            mNavBarView.setTimeHour(mCountingMin);
            mNavBarView.setTimeMin(mCountingSec);
        } else {
            //议程时间是int值，代表议程的时长（分钟），所以秒钟都以0开始
            mMin = mAgendaDuration;
            mSec = 0;
            mNavBarView.setTimeHour(StringUtils.resetNum(mMin));
            mNavBarView.setTimeMin(StringUtils.resetNum(mSec));
        }
        //议程时间有剩余，才开始倒计时
//        if (!mNavBarView.getTimeHour().equals("00") && !mNavBarView.getTimeMin().equals("00")){
        mMyHandler = new MyHandler(this);
        countingTime();//开始倒计时
//        }
        //设置文件列表数据
        mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
        mFileNameRcv.setAdapter(mFileDetailAdapter);
        mFileNameRcv.setOnItemClickListener(this);
        mFileNameRcv.setSelection(mFileIndex);
        mFileDetailAdapter.setSelectedItem(mFileIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();
        //加载文件显示
        mPresenter.loadFile(mFileList.get(mFileIndex));
    }

    @Override
    public void startMeeting(View view) {
        if ("开始".equals(((Button) view).getText().toString())) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            //通知所有客户端，客户端先调用本类方法 respondMeetingBegin
            //客户端：之后按顺序调用  respondAgendaTimeIsCounting，respondAgendaIndexChange，respondDocumentIndexChange
            //客户端：因为开始时，会显示跟主持人端相同的界面，所以议程跟议程文件都一定会显示
            //主持人端调用本类方法   meetingBegin
            mPresenter.begin(mControllerInfoBean, mMeetingState, mAgendaIndex, mFileIndex, mUpLevelText);
            EventBus.getDefault().post("开会中");//通知主界面更新会议状态
        } else {//结束
            String deviceIMEI = MPhone.getDeviceIMEI(this);
            int meetingId = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
            mPresenter.startEndMeeting(deviceIMEI, meetingId);//开始访问服务器，请求结束会议，成功后调用endMeetingSuccess
        }
    }

    @Override
    public void endMeetingSuccess() {
        mMeetingState = Constant.MEETING_STATE_ENDING;
        //通知所有客户端，客户端调用本类方法   respondMeetingEnd
        //主持人端调用本类方法  meetingEnding
        mPresenter.ending(mControllerInfoBean, mMeetingState);
        EventBus.getDefault().post("已结束");//通知主界面更新会议状态
    }

    @Override
    public void pauseMeeting(View view) {
        if ("暂停".equals(((Button) view).getText().toString())) {
            mMeetingState = Constant.MEETING_STATE_PAUSE;
            //通知所有客户端，客户端调用本类方法  respondMeetingPause
            //主持人端调用本类方法   meetingPause
            mPresenter.pause(mControllerInfoBean, mMeetingState);
            //需要保存当前停止的时间，分和秒，重新开始后需要设置回这个时间
            saveCountingMinAndSec(mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
            //保存当前的议程序号与议程文件序号，重新开始后需要设置回这个议程以及该议程的文件序号
            savePauseAgendaIndexAndDocumentIndex(mAgendaIndex, mFileIndex);
            EventBus.getDefault().post("暂停中");//通知主界面更新会议状态
        } else {//继续
            mMeetingState = Constant.MEETING_STATE_CONTINUE;
            //为了暂停时，设备不在文件详情界面，而从主界面跳进来，所以得传递一个当前倒计时时间参数值，
            String tempCountingMin = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_MIN, "00");
            String tempCountingSec = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_SEC, "00");
            int pauseAgendaIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
            int pauseDocumentIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
            //通知全部客户端：如果继续时，议程显示跟之前不一样，调用本类方法
            // respondAgendaTimeIsCounting和respondAgendaIndexChange，
            //客户端：如果文件显示跟之前不一样，调用本类方法  respondDocumentIndexChange。
            //客户端：如果继续时，全部显示无变化，调用   respondAgendaNotChange
            //主持人端调用了本类的方法  meetingContinue
            mPresenter.meetingContinue(mControllerInfoBean, mMeetingState, pauseAgendaIndex, pauseDocumentIndex,
                    mUpLevelText, mIsAgendaChange, true, tempCountingMin, tempCountingSec);
            EventBus.getDefault().post("开会中");//通知主界面更新会议状态
        }
    }

    /**
     * 暂停时，需要保存议程序号与议程文件序号
     *
     * @param agendaIndex 议程序号
     * @param fileIndex   议程文件序号
     */
    private void savePauseAgendaIndexAndDocumentIndex(int agendaIndex, int fileIndex) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_AGENDA_INDEX, agendaIndex);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_DOCUMENT_INDEX, fileIndex);
    }

    /**
     * 暂停时，需要保存倒计时的分和秒
     */
    private void saveCountingMinAndSec(String min, String sec) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_MIN, min);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_SEC, sec);
    }

    @Override
    public void startVote(View view) {//点击开始投票，显示投票列表对话框
        mDialog = new VoteListDialog(this);
        mDialog.setOnAllVoteItemClickListener(this);
        mDialog.show();
    }

    @Override
    public void voteResult(View view) {//控制条的投票结果按钮已经去掉,该方法已经没用到
    }

    @Override
    public void onVoteStartStopButtonClick(View view, int position) {
        String deviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        int meetingId = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getInt(Constant.MEETING_ID, -1);
        mVoteId = mDialog.getVoteId();
        SharedPreferencesUtil.getInstance(this).putInt(Constant.BEGIN_VOTE_ID, mVoteId);
        //启动投票，成功则调用本类方法   launchVoteSuccess
        mPresenter.launchVote(deviceIMEI, meetingId, mVoteId, 0);
    }

    @Override
    public void launchVoteSuccess() {
        //如果控制条投票按钮显示投票，则改变成结束投票，因为投票已经正在开始了
        if ("投票".equals(mControllerView.getVoteAndEndVoteText())) {
            mControllerView.setVoteAndEndVoteText("结束投票");
        }
        //需要保存当前停止的时间，分和秒
        saveCountingMinAndSec(mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
        //保存当前的议程序号与议程文件序号
        savePauseAgendaIndexAndDocumentIndex(mAgendaIndex, mFileIndex);
        //开始投票，通知所有客户端进行投票，客户端调用的是本类方法  respondVoteBegin，
        //主持人端调用本类的方法   showVote
        mPresenter.startVote(mControllerInfoBean, mVoteId, mMeetingState);
    }

    @Override
    public void onCheckResultButtonClick(View view, int position) {
        int voteId = mDialog.getVoteId();
        //隐藏投票列表对话框
        mDialog.dismiss();
        //显示投票结果
        VoteResultDialog voteResultDialog = new VoteResultDialog(this, voteId);
        voteResultDialog.show();
    }

    @Override
    public void showVote() {
        mDialog.dismiss();
        EventBus.getDefault().post(MainActivity.PAGE_SIX);
        mRootView.removeView(mControllerView);
        EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
        EventBus.getDefault().post("投票中");
        ActivityStackManager.pop();
    }

    @Override
    public void showVoteResult() {//显示结果现在直接显示对话框，所以该方法没有用到
    }

    private static class MyHandler extends Handler {
        private WeakReference<FileDetailActivity> activityWeakReference;

        public MyHandler(FileDetailActivity activity) {
            activityWeakReference = new WeakReference<FileDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FileDetailActivity fileDetailActivity = activityWeakReference.get();
            switch (msg.what) {
                case 0x00:
                    if (fileDetailActivity.getMin().equals("00") && fileDetailActivity.getSec(false).equals("00")) {
                        ToastUtil.showMessage("议程已结束");
                        fileDetailActivity.mNavBarView.setTimeHour("00");
                        fileDetailActivity.mNavBarView.setTimeMin("00");
                        fileDetailActivity.mMeetingState = Constant.MEETING_STATE_BEGIN;
                    } else {
                        fileDetailActivity.mNavBarView.setTimeHour(fileDetailActivity.getMin());
                        fileDetailActivity.mNavBarView.setTimeMin(fileDetailActivity.getSec(false));
                        fileDetailActivity.mSec--;
                        if (fileDetailActivity.mPassive) {
                            Message message = Message.obtain();
                            message.what = 0x00;
                            sendMessageDelayed(message, 1000);
                        }
                    }
                    break;
            }
        }
    }

    //倒计时
    private void countingTime() {
        if (mPassive) {
            //如果会议继续，但是分秒都已为0 ，不进行倒计时
            if (mIsAgendaTimeCountDown && mSec == 0 && mMin == 0) {
                ToastUtil.showMessage("当前议程已结束");
//                mMeetingState = Constant.MEETING_STATE_BEGIN;
            } else {
                Message message = Message.obtain();
                message.what = 0x00;
                mMyHandler.sendMessageDelayed(message, 1000);
            }
        }
    }


    /**
     * 设置分钟的显示值，如1-9，则在前面加上“0”，否则直接返回该值的字符串
     *
     * @return 重新设置后的新的值
     */
    private String getMin() {
        String newHour = mMin >= 10 ? "" + mMin : "0" + mMin;
        return newHour;
    }

    /**
     * 设置秒钟的显示值，如1-9，则在前面加上“0”，否则不加
     * 然后，判断该值是否等于“00”，是则代表过了一分钟，将分钟数减1，秒钟重新设置为60，继续倒计时
     *
     * @param flag 会议进行中主动切换议程。主持人为true，其他为false
     * @return 重新设置后的新的值
     */
    private String getSec(boolean flag) {
        String newSex = mSec >= 10 ? "" + mSec : "0" + mSec;
        if (newSex.equals("00")) {
            mMin--;
            if (flag) {
                mSec = 59;
            } else {
                mSec = 60;
            }
            return "00";
        }
        return newSex;
    }

    @OnClick(R.id.id_iv_slide_to_left)
        //点击侧边文件列表向左隐藏
    void onToLeft() {
        mPresenter.slideLeft(null);
    }

    @OnClick(R.id.id_iv_slide_to_right)
        //点击侧边文件列表向右显示
    void toRight() {
        mPresenter.slideRight(null);
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

    //接收到主持人发送的数据，如果当前设备参会人员不是主持人，则进行处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getControllerInfo(ControllerInfoBean data) {
        if (!isHost) {
            mPresenter.handleMessageFromHost(data);
        }
    }


    @Override
    public void respondAgendaHasChange(ControllerInfoBean controllerInfoBean) {
        mMeetingState = Constant.MEETING_STATE_BEGIN;
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
            mNavBarView.setTitle(name);
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
            mMyHandler.removeCallbacksAndMessages(null);
            //减低时间延迟
            Message message = Message.obtain();
            message.what = 0x00;
            mMyHandler.sendMessage(message);
        }
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
            mMyHandler.removeCallbacksAndMessages(null);
            //减低时间延迟
            Message message = Message.obtain();
            message.what = 0x00;
            mMyHandler.sendMessage(message);
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
                mNavBarView.setTitle(name);//设置文件标题
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mPresenter.loadFile(mDocuments.get(mFileIndex));//加载文件
                mFileDetailAdapter.notifyDataSetInvalidated();
            }
        }
    }

    /**
     * 议程内容变化
     *
     * @param agendaIndex   议程序号
     * @param documentIndex 文件序号
     */
    private void agendaContentChange(int agendaIndex, int documentIndex) {
        mAgendaIndex = agendaIndex;
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(mAgendaIndex);
        if (mDocuments != null && mDocuments.size() > 0) {
            mFileList = mDocuments;
            mFileIndex = documentIndex;
            String name = mDocuments.get(mFileIndex).getDocumentName().
                    substring(0, mDocuments.get(mFileIndex).getDocumentName().indexOf("."));
            mNavBarView.setTitle(name);
            //加载文件
            mPresenter.loadFile(mDocuments.get(mFileIndex));
            mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void resetAgendaTimeCounting(ControllerInfoBean controllerInfoBean, int agendaIndex) {
        mPassive = true;
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

            if (isHost && mMeetingState != Constant.MEETING_STATE_PAUSE)
                saveCountingMinAndSec(getMin(), getSec(true));

            if (mMeetingState == Constant.MEETING_STATE_BEGIN ||
                    mMeetingState == Constant.MEETING_STATE_CONTINUE) {
                //切换议程时，当状态为开始时，才会开始倒计时
                mMyHandler.removeCallbacksAndMessages(null);
                //减低时间延迟
                Message message = Message.obtain();
                message.what = 0x00;
                mMyHandler.sendMessage(message);
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
            mNavBarView.setMeetingStateOrAgendaState("议程" + agendaIndex + "/" + mAgendaSum);
            mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(agendaIndex);
            if (mDocuments != null && mDocuments.size() > 0) {
                mFileList = mDocuments;
                mFileIndex = 0;
                String name = mDocuments.get(mFileIndex).getDocumentName().
                        substring(0, mDocuments.get(mFileIndex).getDocumentName().indexOf("."));
                mNavBarView.setTitle(name);
                mPresenter.loadFile(mDocuments.get(mFileIndex));
                mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
                mFileNameRcv.setAdapter(mFileDetailAdapter);
                mFileNameRcv.setOnItemClickListener(this);
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mFileDetailAdapter.notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public void respondAgendaTimeIsCounting(boolean isAgendaTimeCountDown) {
        mIsAgendaTimeCountDown = isAgendaTimeCountDown;
    }

    @Override
    public void respondAgendaIndexChange(ControllerInfoBean controllerInfoBean) {
        resetAgendaTimeCounting(controllerInfoBean, controllerInfoBean.getAgendaIndex());
        resetAgendaContent(controllerInfoBean.getAgendaIndex());
    }

    @Override
    public void respondDocumentIndexChange(int documentIndex) {
        mFileIndex = documentIndex;
        mPresenter.loadFile(mFileList.get(documentIndex));
        String name = mFileList.get(documentIndex).getDocumentName().
                substring(0, mFileList.get(documentIndex).getDocumentName().indexOf("."));
        mNavBarView.setTitle(name);
        mFileDetailAdapter.setSelectedItem(documentIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void respondMeetingBegin(boolean isAgendaChange) {
        mIsAgendaChange = isAgendaChange;
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
    }

    //是否已经切换了议程
    private boolean mIsAgendaChange = false;
    //临时的分秒变量
    private int tempMin, tempSec;

    @Override
    public void respondMeetingPause() {//客户端响应暂停指令
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        mMyHandler.removeCallbacksAndMessages(null);

        tempSec = Integer.valueOf(mNavBarView.getTimeMin());
        tempMin = Integer.valueOf(mNavBarView.getTimeHour());
        mIsAgendaChange = false;
    }

    @Override
    public void respondMeetingContinue(boolean isAgendaChange) {//这个方法应该没有存在的必要，先放着，最后再决定
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        mIsAgendaChange = isAgendaChange;
        //继续时还是同一个议程，则继续进行倒计时，
        //如果切换了议程，则不继续这次议程的倒计时
        if (!mIsAgendaChange) {
            mMin = tempMin;
            mSec = tempSec;
            if (mMin != 0 && mSec != 0) {
                //减低时间延迟
                Message message = Message.obtain();
                message.what = 0x00;
                mMyHandler.sendMessage(message);
            }
        }
    }

    @Override
    public void respondMeetingEnd() {//客户端响应结束指令
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;//客户端不再受控
    }

    @Override
    public void respondVoteBegin(int voteId) {
        Vote vote = new Vote();
        vote.setVoteID(voteId);
        //客户端接收到投票开始信号，通知主界面，存储投票id，显示投票界面
        EventBus.getDefault().post(vote);
        ActivityStackManager.pop();//销毁
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

    //点击返回上一级
    @Override
    public void onClick(View v) {
        mPresenter.fallback();
    }

    @Override
    public void loadFile(File file) {
        mPDFView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(1)
                .load();
    }

    @Override
    public void showFileIsDownLoading(String tip) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(tip);
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

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
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(downLoadComplete.getFileName());
            dialog.setMessage("是否重新下载");
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.loadFile(mFileList.get(mFileIndex));
                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    @Override
    public void setMeetingState(int meetingState) {
        mMeetingState = meetingState;
    }

    @Override
    public void meetingBegin() {
        MyAPP.getInstance().setMeetingIsProgress(true);
        mControllerView.setBeginAndEndText("结束");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        countingTime();
    }

    @Override
    public void meetingEnding() {
        MyAPP.getInstance().setMeetingIsProgress(false);
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;
        if (!mPassive)
            mMyHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().post(new MeetingEnd(1));//通知主界面当前会议已结束，让其做出相应的处理
        //将控制条全部设置为不能点击，主持人不再控制会议
        mControllerView.setStartAndEndButtonNotClickable(false);
        mControllerView.setPauseAndContinueButtonNotClickable(false);
        mControllerView.setStartVoteButtonNotClickable(false);
    }

    @Override
    public void meetingPause() {
        MyAPP.getInstance().setMeetingIsProgress(false);
        mControllerView.setPauseAndContinueText("继续");
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        if (!mPassive)
            mMyHandler.removeCallbacksAndMessages(null);
        mIsAgendaChange = false;
        saveCountingMinAndSec(mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
    }

    @Override
    public void meetingContinue() {//主持人会议继续
        MyAPP.getInstance().setMeetingIsProgress(true);
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
                mNavBarView.setTitle(name);
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mPresenter.loadFile(mFileList.get(mFileIndex));
                mFileDetailAdapter.notifyDataSetInvalidated();
            }
        }
        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setCurrentMeetingState("(开会中)");
        if (mNavBarView.getTimeHour().equals("00") &&
                mNavBarView.getTimeMin().equals("00")) {
            ToastUtil.showMessage("议程已结束");
        } else {
            mPassive = true;
            Message message = Message.obtain();
            message.what = 0x00;
            mMyHandler.sendMessage(message);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //由主持人点击开始或继续，mPassive为true，暂停、结束为false
        //mPassive为true，代表是主持人控制会议中，isHost为true代表当前设备的用户是主持人
        if (mPassive && !isHost) {
            //无操作
        } else {
            mFileDetailAdapter.setSelectedItem(position);
            mFileDetailAdapter.notifyDataSetInvalidated();
//        mFileNameRcv.setSelection(position);
            mFileIndex = position;
            mPresenter.loadFile(mFileList.get(position));
            String name = mFileList.get(position).getDocumentName().
                    substring(0, mFileList.get(position).getDocumentName().indexOf("."));
            mNavBarView.setTitle(name);
        }
        if (isHost &&
                (mMeetingState == Constant.MEETING_STATE_BEGIN ||
                        mMeetingState == Constant.MEETING_STATE_CONTINUE)) {//主持人操作后发送消息通知全部客户端
            mPresenter.handleFileClickFromHost(mControllerInfoBean, mMeetingState, mAgendaIndex, position);
        }
    }

    /**
     * 会议中，临时有人进入会议中，主持人发送消息通知刚进入的平板进入受控状态
     * @param peopleIn
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void somePeopleIn(PeopleIn peopleIn){
        if (peopleIn.isPeopleIn()){
            mPresenter.controlTempPeopleIn(mControllerInfoBean, Constant.MEETING_STATE_BEGIN,
                    mAgendaIndex, mFileIndex, mUpLevelText, mIsAgendaChange, true,
                    mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
        }
    }

    //返回
    @Override
    public void fallback() {
        //由主持人点击或继续，mPassive为true，暂停、结束为false
        if (mPassive) {
            //无操作
        } else {
            if (isHost) {
                mRootView.removeView(mControllerView);
                EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
            }
            ActivityStackManager.pop();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //由主持人点击或继续，mPassive为true，暂停、结束为false
            if (mPassive) {
                //无操作
            } else {
                if (mControllerView != null) {
                    mRootView.removeView(mControllerView);//本界面先移除控制View
                    //通知主界面添加控制View,只有这个界面先移除后，主界面才能添加
                    EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
                }
                ActivityStackManager.pop();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyHandler != null) {
            mMyHandler.removeCallbacksAndMessages(null);
            mMyHandler = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
