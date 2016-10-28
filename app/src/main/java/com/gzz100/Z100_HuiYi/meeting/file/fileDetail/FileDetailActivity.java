package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.meeting.ControllerView;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.Constant;
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
        View.OnClickListener, AdapterView.OnItemClickListener, ControllerView.IOnControllerListener {
    public static final String BUNDLE = "bundle";
    public static final String AGENDA_INDEX = "agendaIndex";
    public static final String FILE_INDEX = "fileIndex";
    public static final String UP_LEVEL_TITLE = "upLevelTitle";
    public static final String PASSIVE = "passive";
    public static final String IS_HOST_FROM_MAIN = "isHostFromMain";
    public static final String IS_AGENDA_TIME_COUNT_DOWN = "isAgendaTimeCountDown";
    public static final String MIN = "min";
    public static final String SEC = "sec";
    private MyHandler mMyHandler;
    private ControllerView mControllerView;
    private FrameLayout.LayoutParams mFl;
    private boolean mIsAgendaTimeCountDown;
    private String mCountingMin;
    private String mCountingSec;
    private int isAgendaTimeCountDown;

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
    FrameLayout mRootView;

    View mSlideLayout;//侧边文件列表的布局
    @BindView(R.id.id_file_detail_tbv)
    NavBarView mNavBarView;//导航栏
    @BindView(R.id.id_slide_rev)
    ListView mFileNameRcv;//侧边文件列表的显示控件
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;//显示PDF 文件的控件
    //    @BindView(R.id.id_control_bar)
//    View mLayoutControlBar;//角色为主持人，才会显示该控制条
    @BindView(R.id.id_ll_file_detail_controller)
    LinearLayout mLayoutController;//文件列表下面的上一个，下一个操作的布局，角色为主持人时才显示
    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;//上一个
    @BindView(R.id.id_btn_next)
    Button mBtnNext;//下一个

    private int mAgendaSum;//议程总数
    private int mAgendaIndex;//当前的议程序号
    private int mFileIndex;//当前显示的文件序号
    private List<Document> mFileList;//文件列表
    private String mAgendaDuration;//当前议程时间
    private String mUpLevelText;//上一级界面的名称
    /**
     * 是否是被动状态，支持人点击开始或继续而跳转进来的，代表被动，值为true
     */
    private boolean mPassive;
    /**
     * 主持人从主界面跳转过来
     */
    private boolean mIsHostFromMain;
    private String mFileName;//当前显示的文件的名称
    //消息的实体类，只实例一次，之后发送该实体类，使用原型模式，避免多次创建同一个类
    private ControllerInfoBean mControllerInfoBean = new ControllerInfoBean();
    //开会中有切换议程时，切换后的议程文件列表
    private List<Document> mDocuments;
    private Bundle mBundle;
    private FileDetailContract.Presenter mPresenter;
    private FileDetailAdapter mFileDetailAdapter;
    //当前议程的分，秒
    private int mMin, mSec;
    //会议状态的标识
    private int mMeetingState;
    /**
     * 是否为主持人
     */
    private boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mSlideLayout = findViewById(R.id.id_slide_layout);
        ButterKnife.bind(this);
        //根据用户角色，是否显示控制界面
        showControlBar();

        mPresenter = new FileDetailPresenter(null, this);
        //获取传过来的数据
        dataFormUpLevel();

    }

    private void showControlBar() {
        int userRole = MyAPP.getInstance().getUserRole();
        if (userRole == 1) {//主持人
            isHost = true;

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
            List<Agenda> agendas = FileOperate.getInstance(this).queryAgendaList(Constant.COLUMNS_AGENDAS);
            mAgendaSum = agendas.size();
            mAgendaDuration = agendas.get(mAgendaIndex-1).getAgendaDuration();
            mFileList = FileOperate.getInstance(this).queryFileList(mAgendaIndex);
            mFileName = mFileList.get(mFileIndex).getDocumentName();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (mBundle != null)
            initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initData() {
        mNavBarView.setFallBackDisplay(true);//显示返回的按钮
        mNavBarView.setTitle(mFileName);//当前文件名称
        mNavBarView.setUpLevelText(mUpLevelText);//上一级名称
        //当前议程的在全部议程中的进度
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        //设置当前的会议状态，mPassive为true，代表主持人点击了开始或者继续
        if (mPassive) {
            mNavBarView.setCurrentMeetingState("（开会中）");
        } else {
            mNavBarView.setCurrentMeetingState("（未开始）");
        }
        if (mIsHostFromMain) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            mIsHostFromMain = !mIsHostFromMain;
        }

        //返回上一级的监听
        mNavBarView.setFallBackListener(this);

        if (mIsAgendaTimeCountDown) {
            mMin = Integer.valueOf(mCountingMin);
            mSec = Integer.valueOf(mCountingSec);
            mNavBarView.setTimeHour(mCountingMin);
            mNavBarView.setTimeMin(mCountingSec);
        } else {
            //分割议程时间，并设置到当前页面，随后进行倒计时操作
            String[] split = mAgendaDuration.split(":");
            mMin = Integer.valueOf(split[0]);
            mSec = Integer.valueOf(split[1]);
            mNavBarView.setTimeHour(StringUtils.resetNum(mMin));
            mNavBarView.setTimeMin(StringUtils.resetNum(mSec));
        }

        mMyHandler = new MyHandler(this);
        countingTime();
        //设置文件列表数据
        mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
        mFileNameRcv.setAdapter(mFileDetailAdapter);
        mFileNameRcv.setOnItemClickListener(this);
        mFileNameRcv.setSelection(mFileIndex);
        mFileDetailAdapter.setSelectedItem(mFileIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();

        mPresenter.loadFile(mFileList.get(mFileIndex).getDocumentName());
    }


    @Override
    public void startMeeting(View view) {
        if ("开始".equals(((Button) view).getText().toString())) {
            mMeetingState = Constant.MEETING_STATE_BEGIN;
            mPresenter.begin(mControllerInfoBean, mMeetingState, mAgendaIndex, mFileIndex, mUpLevelText);
            EventBus.getDefault().post("开会中");
        } else {//结束
            mMeetingState = Constant.MEETING_STATE_ENDING;
            mPresenter.ending(mControllerInfoBean, mMeetingState);
            EventBus.getDefault().post("已结束");
        }
    }

    @Override
    public void pauseMeeting(View view) {
        if ("暂停".equals(((Button) view).getText().toString())) {
            mMeetingState = Constant.MEETING_STATE_PAUSE;
            mPresenter.pause(mControllerInfoBean, mMeetingState);
            //需要保存当前停止的时间，分和秒
            saveCountingMinAndSec(mNavBarView.getTimeHour(), mNavBarView.getTimeMin());
            //保存当前的议程序号与议程文件序号
            savePauseAgendaIndexAndDocumentIndex(mAgendaIndex,mFileIndex);

            EventBus.getDefault().post("暂停中");
        } else {//继续
            mMeetingState = Constant.MEETING_STATE_CONTINUE;
            //为了暂停时，设备不在文件详情界面，而从主界面跳进来，所以得传递一个当前倒计时时间参数值，
            String tempCountingMin = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_MIN, "");
            String tempCountingSec = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_SEC, "");
            int pauseAgendaIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
            int pauseDocumentIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
            mPresenter.meetingContinue(mControllerInfoBean, mMeetingState, pauseAgendaIndex, pauseDocumentIndex,
                    mUpLevelText, mIsAgendaChange, true, tempCountingMin, tempCountingSec);

            EventBus.getDefault().post("开会中");
        }
    }

    /**
     * 保存议程序号与议程文件序号
     * @param agendaIndex      议程序号
     * @param fileIndex        议程文件序号
     */
    private void savePauseAgendaIndexAndDocumentIndex(int agendaIndex, int fileIndex) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_AGENDA_INDEX, agendaIndex);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_DOCUMENT_INDEX, fileIndex);
    }

    /**
     * 保存倒计时的分和秒
     */
    private void saveCountingMinAndSec(String min, String sec) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_MIN, min);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_SEC, sec);
    }

    @Override
    public void startVote(View view) {
        EventBus.getDefault().post(MainActivity.PAGE_SIX);
        mRootView.removeView(mControllerView);
        EventBus.getDefault().post(MainActivity.TRIGGER_OF_REMOVE_CONTROLLERVIEW);
        EventBus.getDefault().post("投票中");
        ActivityStackManager.pop();
    }

    @Override
    public void voteResult(View view) {

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
            Message message = Message.obtain();
            message.what = 0x00;
            mMyHandler.sendMessageDelayed(message, 1000);
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
        if (mPassive && !isHost){//客户端被控制时，不能切换议程
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

    //接收到主持人发送的数据，如果当前设备参会人员不是支持人，则进行处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMulticast(ControllerInfoBean data) {
        if (!isHost) {
            mPresenter.handleMessageFromHost(data);
        }
    }

    @Override
    public void respondAgendaNotChange(ControllerInfoBean controllerInfoBean) {
        if (controllerInfoBean.getAgendaIndex() != mAgendaIndex){
            //客户端当前界面不在暂停前的界面，需要将界面信息还原到暂停前的状态
            resetContentWhenAgendaNotChange(controllerInfoBean);
        }else {
            //在当前界面，但是，主持人已经切换了议程内的文件
            mFileIndex = controllerInfoBean.getDocumentIndex();
            mNavBarView.setTitle(mDocuments.get(mFileIndex).getDocumentName());
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
        }
    }

    /**
     * 还原界面信息到暂停前的状态
     * @param controllerInfoBean    主持人发送的消息实体
     */
    private void resetContentWhenAgendaNotChange(ControllerInfoBean controllerInfoBean){
        //设置时间
        mMin = Integer.valueOf(controllerInfoBean.getCountdingMin());
        mSec = Integer.valueOf(controllerInfoBean.getCountdingSec());
        mNavBarView.setTimeHour(getMin());
        mNavBarView.setTimeMin(getSec(true));
        mMyHandler.removeCallbacksAndMessages(null);

        //减小倒计时时间差
        Message message = Message.obtain();
        message.what = 0x00;
        mMyHandler.sendMessage(message);

        //设置内容
//        agendaContentChange(controllerInfoBean);
        agendaContentChange(controllerInfoBean.getAgendaIndex(),controllerInfoBean.getDocumentIndex());
    }

    /**
     * 议程内容变化
     * @param agendaIndex     议程序号
     * @param documentIndex   文件序号
     */
    private void agendaContentChange(int agendaIndex, int documentIndex) {
        mAgendaIndex = agendaIndex;
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(mAgendaIndex);
        if (mDocuments != null && mDocuments.size() > 0) {
            mFileList = mDocuments;
            mFileIndex = documentIndex;
            mNavBarView.setTitle(mDocuments.get(mFileIndex).getDocumentName());
            mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileDetailAdapter.setSelectedItem(mFileIndex);
            mFileDetailAdapter.notifyDataSetInvalidated();
        }
    }
    @Override
    public void resetAgendaTimeCounting(ControllerInfoBean controllerInfoBean,int agendaIndex) {
        if (mIsAgendaChange || mIsAgendaTimeCountDown) {
            if (!mIsAgendaTimeCountDown){
                //取议程时传入的是mAgendaIndex-1，因为存储议程时，是从0开始的
                String agendaDuration = FileOperate.getInstance(FileDetailActivity.this)
                        .queryAgendaList(Constant.COLUMNS_AGENDAS).get(mAgendaIndex - 1).getAgendaDuration();
                mMin = Integer.valueOf(StringUtils.splitDuration(agendaDuration)[0]);
                mSec = Integer.valueOf(StringUtils.splitDuration(agendaDuration)[1]);
                mNavBarView.setTimeHour(getMin());
                mNavBarView.setTimeMin(getSec(true));
            }else {
                mMin = Integer.valueOf(controllerInfoBean.getCountdingMin());
                mSec = Integer.valueOf(controllerInfoBean.getCountdingSec());
                mNavBarView.setTimeHour(controllerInfoBean.getCountdingMin());
                mNavBarView.setTimeMin(controllerInfoBean.getCountdingSec());
            }

            if (isHost && mMeetingState != Constant.MEETING_STATE_PAUSE)
                saveCountingMinAndSec(getMin(), getSec(true));

            mMyHandler.removeCallbacksAndMessages(null);
            countingTime();
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
                mNavBarView.setTitle(mDocuments.get(mFileIndex).getDocumentName());
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
        resetAgendaTimeCounting(controllerInfoBean,controllerInfoBean.getAgendaIndex());
        resetAgendaContent(controllerInfoBean.getAgendaIndex());
    }

    @Override
    public void respondDocumentIndexChange(int documentIndex) {
        mFileIndex = documentIndex;
        mPresenter.loadFile(mFileList.get(documentIndex).getDocumentName());
        mNavBarView.setTitle(mFileList.get(documentIndex).getDocumentName());
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
    public void respondMeetingPause() {
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        if (!mPassive)
            mMyHandler.removeCallbacksAndMessages(null);

        tempSec = Integer.valueOf(mNavBarView.getTimeMin());
        tempMin = Integer.valueOf(mNavBarView.getTimeHour());
        mIsAgendaChange = false;
    }

    @Override
    public void respondMeetingContinue(boolean isAgendaChange) {
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        mIsAgendaChange = isAgendaChange;
        //继续时还是同一个议程，则继续进行倒计时，
        //如果切换了议程，则不继续这次议程的倒计时
        if (!mIsAgendaChange) {
            mMin = tempMin;
            mSec = tempSec;
            //减低时间延迟
            Message message = Message.obtain();
            message.what = 0x00;
            mMyHandler.sendMessage(message);
        }
    }

    @Override
    public void respondMeetingEnd() {
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;
    }

    //返回
    @Override
    public void fallback() {
        //由主持人点击或继续，mPassive为true，暂停、结束为false
        if (mPassive) {
            //无操作
        } else {
            if (isHost){
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
//        mPDFView.fromFile(file)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .defaultPage(1)
//                .load();
    }

    @Override
    public void setMeetingState(int meetingState) {
        mMeetingState = meetingState;
    }

    @Override
    public void meetingBegin() {
        mControllerView.setBeginAndEndText("结束");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        countingTime();
    }

    @Override
    public void meetingEnding() {
        mNavBarView.setCurrentMeetingState("(已结束)");
        mPassive = false;
        if (!mPassive)
            mMyHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void meetingPause() {
        mControllerView.setPauseAndContinueText("继续");
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        if (!mPassive)
            mMyHandler.removeCallbacksAndMessages(null);
        mIsAgendaChange = false;
    }

    @Override
    public void meetingContinue() {
        //如果暂停后，主持人是有切换过议程的，需要还原到上次暂停的界面
        int pauseAgendaIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
        int pauseDocumentIndex = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
        if (mAgendaIndex != pauseAgendaIndex){
            agendaContentChange(pauseAgendaIndex,pauseDocumentIndex);

            String tempCountingMin = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_MIN, "");
            String tempCountingSec = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.COUNTING_SEC, "");

            mMin = Integer.valueOf(tempCountingMin);
            mSec = Integer.valueOf(tempCountingSec);
            mNavBarView.setTimeHour(tempCountingMin);
            mNavBarView.setTimeMin(tempCountingSec);
        }else {
            //还在本界面，但是当前显示的文件序号跟暂停前的不一样，需要切换到暂停前的议程文件序号
            if (mFileIndex != pauseDocumentIndex){
                mFileIndex = pauseDocumentIndex;
                mNavBarView.setTitle(mFileList.get(mFileIndex).getDocumentName());
                mFileDetailAdapter.setSelectedItem(mFileIndex);
                mFileDetailAdapter.notifyDataSetInvalidated();

            }
        }

        mControllerView.setPauseAndContinueText("暂停");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        countingTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyHandler != null) {
            mMyHandler.removeCallbacksAndMessages(null);
            mMyHandler = null;
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
            mPresenter.loadFile(mFileList.get(position).getDocumentName());
            mNavBarView.setTitle(mFileList.get(position).getDocumentName());
        }
        if (isHost) {//主持人操作后发送消息通知全部客户端
            mPresenter.handleFileClickFromHost(mControllerInfoBean, mMeetingState, mAgendaIndex, position);
        }
    }
}
