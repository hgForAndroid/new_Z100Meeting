package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.multicast.MulticaseBean;
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

public class FileDetailActivity extends BaseActivity implements FileDetailContract.DetailView, View.OnClickListener, OnFileClick {
    public static final String BUNDLE = "bundle";
    public static final String AGENDAS_SUM = "agendasSum";
    public static final String AGENDA_INDEX = "agendaIndex";
    public static final String FILE_LIST = "fileList";
    public static final String FILE_INDEX = "fileIndex";
    public static final String AGENDA_TIME = "agendaTime";
    public static final String UP_LEVEL_TITLE = "upLevelTitle";
    private int mAgendaSum;
    private int mAgendaIndex;
    private int mFileIndex;
    private List<Document> mFileList;
    private String mAgendaDuration;
    private String mUpLevelText;
    private String mFileName;
    private int mChildCount;
    private MulticaseBean mMulticaseBean = new MulticaseBean();
    private List<Document> mDocuments;
    //    private Button mBtnPrevious;
//    private Button mBtnNext;

    /**
     * 跳转到文件详情界面
     *
     * @param activity     context
     * @param agendasSum   议程总数
     * @param agendaIndex  当前议程序号
     * @param fileList     文件列表
     * @param fileIndex    选择的文件序号
     * @param agendaTime   议程时间
     * @param upLevelTitle 当前界面的标题
     */
    public static void showFileDetailActivity(Context activity, int agendasSum,
                                              int agendaIndex, List<Document> fileList,
                                              int fileIndex, String agendaTime,
                                              String upLevelTitle) {
        Intent intent = new Intent(activity, FileDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AGENDAS_SUM, agendasSum);
        bundle.putInt(AGENDA_INDEX, agendaIndex);
        bundle.putInt(FILE_INDEX, fileIndex);
        bundle.putSerializable(FILE_LIST, (Serializable) fileList);
        bundle.putString(AGENDA_TIME, agendaTime);
        bundle.putString(UP_LEVEL_TITLE, upLevelTitle);
        intent.putExtra(BUNDLE, bundle);
        activity.startActivity(intent);
    }

    @BindView(R.id.id_file_detail_tbv)
    NavBarView mNavBarView;
    //    @BindView(R.id.id_slide_layout)
    View mSlideLayout;

    @BindView(R.id.id_slide_rev)
    RecyclerView mFileNameRcv;
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;

    @BindView(R.id.id_control_bar)
    View mLayoutControlBar;

    @BindView(R.id.id_btn_previous)
    Button mBtnPrevious;
    @BindView(R.id.id_btn_next)
    Button mBtnNext;

    @BindView(R.id.id_iv_control_bar_slide_to_right)
    ImageView mIvControlSlideToRight;

    private Bundle mBundle;
    private FileDetailContract.Presenter mPresenter;
    private FileDetailAdapter mFileDetailAdapter;

    private int mMin, mSec;

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
            mAgendaSum = mBundle.getInt(AGENDAS_SUM);
            mAgendaIndex = mBundle.getInt(AGENDA_INDEX);
            mFileIndex = mBundle.getInt(FILE_INDEX);
            mFileList = (List<Document>) mBundle.getSerializable(FILE_LIST);
            mAgendaDuration = mBundle.getString(AGENDA_TIME);
            mUpLevelText = mBundle.getString(UP_LEVEL_TITLE);
            mFileName = mFileList.get(mFileIndex).getDocumentName();
            initData();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
//        if (mBundle != null)
//            initData();
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
        mNavBarView.setCurrentMeetingState("（开会中）");
        mNavBarView.setFallBackListener(this);
        //时间
        String[] split = mAgendaDuration.split(":");
        mMin = Integer.valueOf(split[0]);
        mSec = Integer.valueOf(split[1]);
        mNavBarView.setTimeHour(resetNum(mMin));
        mNavBarView.setTimeMin(resetNum(mSec));
        countingTime();

        mFileDetailAdapter = new FileDetailAdapter(mFileList, this);
        mFileNameRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFileNameRcv.setAdapter(mFileDetailAdapter);
        mFileDetailAdapter.setOnFileClick(this);
        mFileNameRcv.scrollToPosition(mFileIndex);
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
//                Dialog dialog = new AlertDialog.Builder(FileDetailActivity.this)
//                        .setTitle("提示")
//                        .setMessage("议程时间已到")
//                        .create();
//                dialog.show();
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            setBackgroundColor(mFileList.size(), mFileIndex);
        }
    }

    private void setBackgroundColor(int childCount, int fileIndex) {
        for (int i = 0; i < childCount; i++) {
            ((LinearLayout) mFileNameRcv.getChildAt(i).findViewById(R.id.id_ll_file_name_backgroud)).
                    setBackgroundColor(getResources().getColor(R.color.color_tab_normal));
            ((TextView) mFileNameRcv.getChildAt(i).findViewById(R.id.id_tv_item_file_name)).setTextColor(
                    getResources().getColor(R.color.color_black));
        }
        ((LinearLayout) mFileNameRcv.getChildAt(fileIndex).findViewById(R.id.id_ll_file_name_backgroud)).
                setBackgroundColor(getResources().getColor(R.color.color_tab_selected));
        ((TextView) mFileNameRcv.getChildAt(fileIndex).findViewById(R.id.id_tv_item_file_name)).setTextColor(
                getResources().getColor(R.color.color_white));
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MulticaseBean multicaseBean = mMulticaseBean.clone();
                            multicaseBean.setMeetingID("192.168.1.110");
                            MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.id_btn_pause_meeting://暂停
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MulticaseBean multicaseBean = mMulticaseBean.clone();
                            multicaseBean.setMeetingID("192.168.1.110");
                            MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.id_btn_start_vote://投票
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MulticaseBean multicaseBean = mMulticaseBean.clone();
                            multicaseBean.setMeetingID("192.168.1.110");
                            MulticastController.getDefault().sendMulticaseBean(multicaseBean);
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
                            MulticaseBean multicaseBean = mMulticaseBean.clone();
                            multicaseBean.setMeetingID("192.168.1.110");
                            MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;
            case R.id.id_btn_previous://上一个
                if (mAgendaIndex > 1) {
                    mAgendaIndex -= 1;
                    resetAgendaTimeCounting(mAgendaIndex);
                    resetAgendaContent(mAgendaIndex);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MulticaseBean multicaseBean = mMulticaseBean.clone();
                                multicaseBean.setAgendaIndex(mAgendaIndex);
                                MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case R.id.id_btn_next://下一个
                if (mAgendaIndex > 0 && mAgendaIndex < mAgendaSum) {
                    mAgendaIndex += 1;
                    resetAgendaTimeCounting(mAgendaIndex);
                    resetAgendaContent(mAgendaIndex);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MulticaseBean multicaseBean = mMulticaseBean.clone();
                                multicaseBean.setAgendaIndex(mAgendaIndex);
                                MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
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
    public void getMulticase(MulticaseBean data) {
        if (MyAPP.getInstance().getUserRole() != 1){
            if (data.getAgendaIndex() > 0) {
                resetAgendaTimeCounting(data.getAgendaIndex());
                resetAgendaContent(data.getAgendaIndex());
            }
            if (data.getDocumentIndex() >= 0) {
                Log.e("getDocumentIndex ==",data.getDocumentIndex()+"");
                mPresenter.loadFile(mDocuments.get(data.getDocumentIndex()).getDocumentName());
                mNavBarView.setTitle(mDocuments.get(data.getDocumentIndex()).getDocumentName());
                setBackgroundColor(mDocuments.size(), data.getDocumentIndex());
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
    private void resetAgendaTimeCounting(int mAgendaIndex) {
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
     * @param mAgendaIndex 议程序号
     */
    private void resetAgendaContent(int mAgendaIndex) {
        mNavBarView.setMeetingStateOrAgendaState("议程" + mAgendaIndex + "/" + mAgendaSum);
        mDocuments = FileOperate.getInstance(FileDetailActivity.this).queryFileList(mAgendaIndex);
        if (mDocuments != null && mDocuments.size() > 0) {
            mNavBarView.setTitle(mDocuments.get(0).getDocumentName());
            mFileDetailAdapter = new FileDetailAdapter(mDocuments, this);
            mFileNameRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mFileNameRcv.setAdapter(mFileDetailAdapter);
            mFileDetailAdapter.setOnFileClick(this);
            mFileDetailAdapter.notifyDataSetChanged();
        }
    }
    //返回
    @Override
    public void fallback() {
        ActivityStackManager.pop();
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

    //点击文件
    @Override
    public void onFileClick(final int position) {
        mPresenter.loadFile(mFileList.get(position).getDocumentName());
        mNavBarView.setTitle(mFileList.get(position).getDocumentName());
        setBackgroundColor(mFileList.size(), position);
        if (MyAPP.getInstance().getUserRole() == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MulticaseBean multicaseBean = mMulticaseBean.clone();
                        multicaseBean.setDocumentIndex(position);
                        MulticastController.getDefault().sendMulticaseBean(multicaseBean);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
    }
}
