package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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
    private int mAgendaSum;
    private int mAgendaIndex;
    private int mFileIndex;
    private List<Document> mFileList;
    private String mAgendaDuration;
    private String mUpLevelText;
    private boolean mPassive;
    private String mFileName;
    private MulticastBean mMulticastBean = new MulticastBean();
    private List<Document> mDocuments;

    /**
     * 跳转到文件详情界面
     *
     * @param context      context
     * @param agendaIndex  当前议程序号
     * @param fileIndex    选择的文件序号
     * @param upLevelTitle 当前界面的标题
     * @param passive      是否被动跳转
     */
    public static void start(Context context, int agendaIndex, int fileIndex, String upLevelTitle,boolean passive) {
        Intent starter = new Intent(context, FileDetailActivity.class);
        Bundle extraBundle = new Bundle();
        extraBundle.putInt(AGENDA_INDEX, agendaIndex);
        extraBundle.putInt(FILE_INDEX, fileIndex);
        extraBundle.putString(UP_LEVEL_TITLE, upLevelTitle);
        extraBundle.putBoolean(PASSIVE, passive);
        starter.putExtra(BUNDLE, extraBundle);
        context.startActivity(starter);
    }

    @BindView(R.id.id_file_detail_tbv)
    NavBarView mNavBarView;
    //    @BindView(R.id.id_slide_layout)
    View mSlideLayout;

    @BindView(R.id.id_slide_rev)
    ListView mFileNameRcv;
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;

    @BindView(R.id.id_control_bar)
    View mLayoutControlBar;

    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;
    @BindView(R.id.id_btn_next)
    Button mBtnNext;
    @BindView(R.id.id_btn_pause_meeting)
    Button mBtnPause;
    @BindView(R.id.id_btn_start_meeting)
    Button mBtnStart;

    @BindView(R.id.id_iv_control_bar_slide_to_right)
    ImageView mIvControlSlideToRight;

    private Bundle mBundle;
    private FileDetailContract.Presenter mPresenter;
    private FileDetailAdapter mFileDetailAdapter;

    private int mMin, mSec;

    //会议状态的标识
    private int meetingState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mSlideLayout = findViewById(R.id.id_slide_layout);
        ButterKnife.bind(this);

        //根据用户角色，是否显示控制界面
        showControlBar();

        mPresenter = new FileDetailPresenter(null, this);
        dataFormUpLevel();

    }

    private void showControlBar() {
        int userRole = MyAPP.getInstance().getUserRole();
        if (userRole == 1) {
            mLayoutControlBar.setVisibility(View.VISIBLE);
            mBtnPrevious.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            mIvControlSlideToRight.setVisibility(View.VISIBLE);
        } else {
            mLayoutControlBar.setVisibility(View.GONE);
            mBtnPrevious.setVisibility(View.GONE);
            mBtnNext.setVisibility(View.GONE);
            mIvControlSlideToRight.setVisibility(View.GONE);
        }
    }

    private void dataFormUpLevel() {
        mBundle = getIntent().getExtras().getBundle(BUNDLE);
        if (mBundle != null) {
            mAgendaIndex = mBundle.getInt(AGENDA_INDEX);
            mFileIndex = mBundle.getInt(FILE_INDEX);
            mUpLevelText = mBundle.getString(UP_LEVEL_TITLE);
            mPassive = mBundle.getBoolean(PASSIVE,false);
            List<Agenda> agendas = FileOperate.getInstance(this).queryAgendaList(Constant.COLUMNS_AGENDAS);
            mAgendaSum = agendas.size();
            mAgendaDuration = agendas.get(mAgendaIndex - 1).getAgendaDuration();
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
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initData() {
        mNavBarView.setFallBackDisplay(true);
        mNavBarView.setTitle(mFileName);
        mNavBarView.setUpLevelText(mUpLevelText);
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
//        mNavBarView.setTime(mAgendaDuration);
        if (mPassive){
            mNavBarView.setCurrentMeetingState("（已开始）");
        }else {
            mNavBarView.setCurrentMeetingState("（未开始）");
        }

        mNavBarView.setFallBackListener(this);
        //时间
        String[] split = mAgendaDuration.split(":");
        mMin = Integer.valueOf(split[0]);
        mSec = Integer.valueOf(split[1]);
        mNavBarView.setTimeHour(resetNum(mMin));
        mNavBarView.setTimeMin(resetNum(mSec));
        countingTime();

        mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
        mFileNameRcv.setAdapter(mFileDetailAdapter);
        mFileNameRcv.setOnItemClickListener(this);
        mFileNameRcv.setSelection(mFileIndex);
        mFileDetailAdapter.setSelectedItem(mFileIndex);
        mFileDetailAdapter.notifyDataSetInvalidated();

        mPresenter.loadFile(mFileList.get(mFileIndex).getDocumentName());


    }

    private String resetNum(int num) {
        return num > 9 ? num + "" : "0" + num;
    }

    //倒计时
    private void countingTime() {
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

    private String getMin() {
        String newHour = mMin >= 10 ? "" + mMin : "0" + mMin;
        return newHour;
    }

    private String getSec() {
        String newSex = mSec >= 10 ? "" + mSec : "0" + mSec;
        if (newSex.equals("00")) {
            mMin--;
            mSec = 60;
            return "00";
        }
        return newSex;
    }

    @OnClick(R.id.id_iv_slide_to_left)
    void onToLeft() {
        mPresenter.slideLeft(null);
    }

    @OnClick(R.id.id_iv_slide_to_right)
    void toRight() {
        mPresenter.slideRight(null);
    }

    @OnClick({R.id.id_btn_start_meeting, R.id.id_btn_pause_meeting
            , R.id.id_btn_start_vote, R.id.id_btn_vote_result
            , R.id.id_btn_previous, R.id.id_btn_next})
    void onControlClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_start_meeting://开始
                if ("开始".equals(mBtnStart.getText().toString())) {
                    meetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.begin(mMulticastBean, meetingState, mAgendaIndex, mFileIndex, mUpLevelText);
                    mBtnStart.setText("结束");
                } else {//结束
                    meetingState = Constant.MEETING_STATE_ENDING;
                    mPresenter.ending(mMulticastBean, meetingState);
                }
                break;
            case R.id.id_btn_pause_meeting://暂停
                if ("暂停".equals(mBtnPause.getText().toString())) {
                    meetingState = Constant.MEETING_STATE_PAUSE;
                    mPresenter.pause(mMulticastBean, meetingState);
                    mBtnPause.setText("继续");
                } else {//继续
                    meetingState = Constant.MEETING_STATE_BEGIN;
                    mPresenter.meetingContinue(mMulticastBean, meetingState, mAgendaIndex, mFileIndex, mUpLevelText);
                    mBtnPause.setText("暂停");
                }
                break;
            case R.id.id_btn_start_vote://投票
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MulticastBean multicastBean = mMulticastBean.clone();
//                            multicastBean.setVoteIndex();
                            MulticastController.getDefault().sendMulticaseBean(multicastBean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.id_btn_vote_result://投票结果
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MulticastBean multicastBean = mMulticastBean.clone();
                            multicastBean.setMeetingID("192.168.1.110");
                            MulticastController.getDefault().sendMulticaseBean(multicastBean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.id_btn_previous://上一个
                if (mAgendaIndex > 1) {
                    mPresenter.previousAgenda(mMulticastBean, meetingState, mAgendaIndex);
                }
                break;
            case R.id.id_btn_next://下一个
                if (mAgendaIndex > 0 && mAgendaIndex < mAgendaSum) {
                    mPresenter.nextAgenda(mMulticastBean, meetingState, mAgendaIndex);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMulticase(MulticastBean data) {
        if (MyAPP.getInstance().getUserRole() != 1) {
            //会议开始状态才响应
            if (data.getMeetingState() == Constant.MEETING_STATE_BEGIN) {
                if (data.getAgendaIndex() > 0) {
                    mAgendaIndex = data.getAgendaIndex();
                    resetAgendaTimeCounting(data.getAgendaIndex());
                    resetAgendaContent(data.getAgendaIndex());
                }
                if (data.getDocumentIndex() >= 0) {
                    mFileIndex = data.getDocumentIndex();
                    mPresenter.loadFile(mFileList.get(data.getDocumentIndex()).getDocumentName());
                    mNavBarView.setTitle(mFileList.get(data.getDocumentIndex()).getDocumentName());
//                mFileNameRcv.setSelection(data.getDocumentIndex());
                    mFileDetailAdapter.setSelectedItem(data.getDocumentIndex());
                    mFileDetailAdapter.notifyDataSetInvalidated();
                }
                mNavBarView.setCurrentMeetingState("(开会中)");
            } else if (data.getMeetingState() == Constant.MEETING_STATE_PAUSE) {
                mNavBarView.setCurrentMeetingState("(暂停中)");
            } else if (data.getMeetingState() == Constant.MEETING_STATE_ENDING) {
                mNavBarView.setCurrentMeetingState("(已结束)");
            }
        }
    }

    /**
     * 根据 符号   ：    将时间分割成分秒
     *
     * @param duration 时间字符串
     * @return 时分集合
     */
    private String[] splitDuration(String duration) {
        String[] strings = null;
        if (duration.contains(":")) {
            strings = duration.split(":");
        }
        return strings;
    }

    /**
     * 切换议程后，重设议程的时间，
     *
     * @param mAgendaIndex 议程序号
     */
    @Override
    public void resetAgendaTimeCounting(int mAgendaIndex) {
        //取议程时传入的是mAgendaIndex-1，因为存储议程时，是从0开始的
        String agendaDuration = FileOperate.getInstance(FileDetailActivity.this)
                .queryAgendaList(Constant.COLUMNS_AGENDAS).get(mAgendaIndex - 1).getAgendaDuration();
        mMin = Integer.valueOf(splitDuration(agendaDuration)[0]);
        mSec = Integer.valueOf(splitDuration(agendaDuration)[1]);
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    /**
     * 切换议程后，重设议程的各部分内容
     *
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

    //返回
    @Override
    public void fallback() {
        //不是主持人，并且在开会状态下，不可返回
        if (MyAPP.getInstance().getUserRole() != 1 && meetingState == Constant.MEETING_STATE_BEGIN){
            //不可返回，无操作
        }else {
            ActivityStackManager.pop();
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
    public void meetingBegin() {
        mNavBarView.setCurrentMeetingState("（开会中）");
    }

    @Override
    public void meetingEnding() {
        mNavBarView.setCurrentMeetingState("（已结束）");
    }

    @Override
    public void meetingPause() {
        mNavBarView.setCurrentMeetingState("（暂停中）");
    }

    @Override
    public void meetingContinue() {
        mNavBarView.setCurrentMeetingState("（开会中）");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //非主持人，并处于开会状态
        if (MyAPP.getInstance().getUserRole() != 1 && meetingState == Constant.MEETING_STATE_BEGIN) {
            //无操作
        } else {
            mFileDetailAdapter.setSelectedItem(position);
            mFileDetailAdapter.notifyDataSetInvalidated();
//        mFileNameRcv.setSelection(position);

            mFileIndex = position;
            mPresenter.loadFile(mFileList.get(position).getDocumentName());
            mNavBarView.setTitle(mFileList.get(position).getDocumentName());
        }
        if (MyAPP.getInstance().getUserRole() == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MulticastBean multicastBean = mMulticastBean.clone();
                        //点击开始，发送下面这句
                        multicastBean.setMeetingState(meetingState);
                        //这里设置议程序号为0.切换文件时，忽略切换议程
                        multicastBean.setAgendaIndex(0);
                        multicastBean.setDocumentIndex(position);
                        MulticastController.getDefault().sendMulticaseBean(multicastBean);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
