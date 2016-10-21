package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.multicast.MulticastBean;
import com.gzz100.Z100_HuiYi.multicast.MulticastController;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity implements FileDetailContract.DetailView, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String BUNDLE = "bundle";
    public static final String AGENDA_INDEX = "agendaIndex";
    public static final String FILE_INDEX = "fileIndex";
    public static final String UP_LEVEL_TITLE = "upLevelTitle";
    public static final String PASSIVE = "passive";

    /**
     * 跳转到文件详情界面
     *
     * @param context      context
     * @param agendaIndex  当前议程序号
     * @param fileIndex    选择的文件序号
     * @param upLevelTitle 当前界面的标题
     * @param passive      是否被动跳转
     */
    public static void start(Context context, int agendaIndex, int fileIndex, String upLevelTitle, boolean passive) {
        Intent starter = new Intent(context, FileDetailActivity.class);
        Bundle extraBundle = new Bundle();
        extraBundle.putInt(AGENDA_INDEX, agendaIndex);
        extraBundle.putInt(FILE_INDEX, fileIndex);
        extraBundle.putString(UP_LEVEL_TITLE, upLevelTitle);
        extraBundle.putBoolean(PASSIVE, passive);
        starter.putExtra(BUNDLE, extraBundle);
        context.startActivity(starter);
    }

    View mSlideLayout;//侧边文件列表的布局
    @BindView(R.id.id_file_detail_tbv)
    NavBarView mNavBarView;//导航栏
    @BindView(R.id.id_slide_rev)
    ListView mFileNameRcv;//侧边文件列表的显示控件
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;//显示PDF 文件的控件
    @BindView(R.id.id_control_bar)
    View mLayoutControlBar;//角色为主持人，才会显示该控制条
    @BindView(R.id.id_ll_file_detail_controller)
    LinearLayout mLayoutController;//文件列表下面的上一个，下一个操作的布局，角色为主持人时才显示
    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;//上一个
    @BindView(R.id.id_btn_next)
    Button mBtnNext;//下一个
    @BindView(R.id.id_btn_pause_meeting)
    Button mBtnPause;//暂停，点击后变为继续
    @BindView(R.id.id_btn_start_meeting)
    Button mBtnStart;//开始，点击后变为结束
    @BindView(R.id.id_iv_control_bar_slide_to_left)
    ImageView mIvControlSlideToLeft;//控制条向左显示的按钮，只有角色为主持人才显示

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
    private String mFileName;//当前显示的文件的名称
    //消息的实体类，只实例一次，之后发送该实体类，使用原型模式，避免多次创建同一个类
    private MulticastBean mMulticastBean = new MulticastBean();
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
            mLayoutController.setVisibility(View.VISIBLE);
            mLayoutControlBar.setVisibility(View.VISIBLE);
            mIvControlSlideToLeft.setVisibility(View.VISIBLE);
            isHost = true;
        } else {//其他参会人员
            mLayoutController.setVisibility(View.GONE);
            mLayoutControlBar.setVisibility(View.GONE);
            mIvControlSlideToLeft.setVisibility(View.GONE);
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
            List<Agenda> agendas = FileOperate.getInstance(this).queryAgendaList(Constant.COLUMNS_AGENDAS);
            mAgendaSum = agendas.size();
            if (mPassive) {
                mAgendaDuration = agendas.get(mAgendaIndex).getAgendaDuration();
            } else {
                mAgendaDuration = agendas.get(mAgendaIndex - 1).getAgendaDuration();
            }
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
        //返回上一级的监听
        mNavBarView.setFallBackListener(this);
        //分割议程时间，并设置到当前页面，随后进行倒计时操作
        String[] split = mAgendaDuration.split(":");
        mMin = Integer.valueOf(split[0]);
        mSec = Integer.valueOf(split[1]);
        mNavBarView.setTimeHour(StringUtils.resetNum(mMin));
        mNavBarView.setTimeMin(StringUtils.resetNum(mSec));
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

    //倒计时
    private void countingTime() {
        if (mPassive)
            mHandler.post(mRunnable);
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (getMin().equals("00") && getSec().equals("00")) {
                Toast.makeText(FileDetailActivity.this, "议程时间到！！！！！！", Toast.LENGTH_LONG).show();
            } else {
                mHandler.postDelayed(this, 1000);
                mNavBarView.setTimeHour(getMin());
                mNavBarView.setTimeMin(getSec());
                mSec--;
            }
        }
    };

    /**
     * 设置分钟的显示值，如1-9，则在前面加上“0”，否则直接返回该值的字符串
     * @return   重新设置后的新的值
     */
    private String getMin() {
        String newHour = mMin >= 10 ? "" + mMin : "0" + mMin;
        return newHour;
    }

    /**
     * 设置秒钟的显示值，如1-9，则在前面加上“0”，否则不加
     * 然后，判断该值是否等于“00”，是则代表过了一分钟，将分钟数减1，秒钟重新设置为60，继续倒计时
     * @return  重新设置后的新的值
     */
    private String getSec() {
        String newSex = mSec >= 10 ? "" + mSec : "0" + mSec;
        if (newSex.equals("00")) {
            mMin--;
            mSec = 60;
            return "00";
        }
        return newSex;
    }

    @OnClick(R.id.id_iv_slide_to_left)//点击侧边文件列表向左隐藏
    void onToLeft() {
        mPresenter.slideLeft(null);
    }

    @OnClick(R.id.id_iv_slide_to_right)//点击侧边文件列表向右显示
    void toRight() {
        mPresenter.slideRight(null);
    }

    //会议的   开始(结束)，暂停(继续)，投票，显示投票结果，上一个，下一个  点击监听
    @OnClick({R.id.id_btn_start_meeting, R.id.id_btn_pause_meeting
            , R.id.id_btn_start_vote, R.id.id_btn_vote_result
            , R.id.id_btn_previous, R.id.id_btn_next})
    void onControlClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_start_meeting://开始
                if ("开始".equals(mBtnStart.getText().toString())) {
                    mMeetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.begin(mMulticastBean, mMeetingState, mAgendaIndex, mFileIndex, mUpLevelText);
                } else {//结束
                    mMeetingState = Constant.MEETING_STATE_ENDING;
                    mPresenter.ending(mMulticastBean, mMeetingState);
                }
                break;
            case R.id.id_btn_pause_meeting://暂停
                if ("暂停".equals(mBtnPause.getText().toString())) {
                    mMeetingState = Constant.MEETING_STATE_PAUSE;
                    mPresenter.pause(mMulticastBean, mMeetingState);
                } else {//继续
                    mMeetingState = Constant.MEETING_STATE_CONTINUE;
                    mPresenter.meetingContinue(mMulticastBean, mMeetingState, mAgendaIndex, mFileIndex, mUpLevelText);
                }
                break;
            case R.id.id_btn_start_vote://投票
                //TODO  投票操作
                break;
            case R.id.id_btn_vote_result://投票结果
                //TODO  显示投票结果
                break;
            case R.id.id_btn_previous://上一个
                if (mAgendaIndex > 1) {
                    if (mMeetingState == Constant.MEETING_STATE_CONTINUE)
                        mMeetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.previousAgenda(mMulticastBean, mMeetingState, mAgendaIndex);
                }
                break;
            case R.id.id_btn_next://下一个
                if (mAgendaIndex > 0 && mAgendaIndex < mAgendaSum) {
                    if (mMeetingState == Constant.MEETING_STATE_CONTINUE)
                        mMeetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.nextAgenda(mMulticastBean, mMeetingState, mAgendaIndex);
                }
                break;
        }
    }

    /**
     * 切换议程后，重设议程的时间
     * @param mAgendaIndex 议程序号
     */
    @Override
    public void resetAgendaTimeCounting(int mAgendaIndex) {
        //取议程时传入的是mAgendaIndex-1，因为存储议程时，是从0开始的
        String agendaDuration = FileOperate.getInstance(FileDetailActivity.this)
                .queryAgendaList(Constant.COLUMNS_AGENDAS).get(mAgendaIndex - 1).getAgendaDuration();
        mMin = Integer.valueOf(StringUtils.splitDuration(agendaDuration)[0]);
        mSec = Integer.valueOf(StringUtils.splitDuration(agendaDuration)[1]);
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    /**
     * 切换议程后，重设议程的各部分内容
     * @param agendaIndex 议程序号
     */
    @Override
    public void resetAgendaContent(int agendaIndex) {
        mAgendaIndex = agendaIndex;
        mNavBarView.setMeetingStateOrAgendaState("议程" + agendaIndex + "/" + mAgendaSum);
        mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(agendaIndex);
        if (mDocuments != null && mDocuments.size() > 0) {
            mFileList = mDocuments;
            mNavBarView.setTitle(mDocuments.get(0).getDocumentName());
            mFileDetailAdapter = new FileDetailAdapter(mDocuments, this);
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileNameRcv.setOnItemClickListener(this);
            mFileDetailAdapter.setSelectedItem(0);
            mFileDetailAdapter.notifyDataSetInvalidated();
        }
    }

    //接收到主持人发送的数据，如果当前设备参会人员不是支持人，则进行处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMulticase(MulticastBean data) {
        if (!isHost) {
            mPresenter.handleMessageFromHost(data);
        }
    }

    @Override
    public void respondAgendaIndexChange(int agendaIndex) {
        mAgendaIndex = agendaIndex;
        resetAgendaTimeCounting(agendaIndex);
        resetAgendaContent(agendaIndex);
    }

    @Override
    public void respondDocumentIndexChange(int documentIndex) {
        mFileIndex = documentIndex;
        mPresenter.loadFile(mFileList.get(documentIndex).getDocumentName());
        mNavBarView.setTitle(mFileList.get(documentIndex).getDocumentName());
//                mFileNameRcv.setSelection(documentIndex);
        mFileDetailAdapter.setSelectedItem(documentIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void respondMeetingBegin() {
        mBtnStart.setText("结束");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
    }

    @Override
    public void respondMeetingPause() {
        mBtnPause.setText("继续");
        mNavBarView.setCurrentMeetingState("(暂停中)");
        mPassive = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void respondMeetingContinue() {
        mBtnPause.setText("暂停");
        mNavBarView.setCurrentMeetingState("(开会中)");
        mPassive = true;
        countingTime();
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
        mNavBarView.setCurrentMeetingState("（开会中）");
        mPassive = true;
        countingTime();
    }

    @Override
    public void meetingEnding() {
        mNavBarView.setCurrentMeetingState("（已结束）");
        mPassive = false;
        if (!mPassive)
            mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void meetingPause() {
        mNavBarView.setCurrentMeetingState("（暂停中）");
        mPassive = false;
        if (!mPassive)
            mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void meetingContinue() {
        mNavBarView.setCurrentMeetingState("（开会中）");
        mPassive = true;
        countingTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
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
            mPresenter.handleFileClickFromHost(mMulticastBean,mMeetingState,mAgendaIndex,position);
        }
    }
}
