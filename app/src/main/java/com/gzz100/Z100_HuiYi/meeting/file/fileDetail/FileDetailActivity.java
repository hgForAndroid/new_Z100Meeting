package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;

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
    private String mAgendaTime;
    private String mUpLevelText;
    private String mFileName;
    private int mChildCount;


    /**
     * 跳转到文件详情界面
     * @param activity       context
     * @param agendasSum     议程总数
     * @param agendaIndex    当前议程序号
     * @param fileList       文件列表
     * @param fileIndex      选择的文件序号
     * @param agendaTime     议程时间
     * @param upLevelTitle   当前界面的标题
     */
    public static void showFileDetailActivity(Context activity, int agendasSum,
                                              int agendaIndex, List<Document> fileList,
                                              int fileIndex, String agendaTime,
                                              String upLevelTitle){
        Intent intent = new Intent(activity,FileDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AGENDAS_SUM,agendasSum);
        bundle.putInt(AGENDA_INDEX,agendaIndex);
        bundle.putInt(FILE_INDEX,fileIndex);
        bundle.putSerializable(FILE_LIST, (Serializable) fileList);
        bundle.putString(AGENDA_TIME,agendaTime);
        bundle.putString(UP_LEVEL_TITLE,upLevelTitle);
        intent.putExtra(BUNDLE,bundle);
        activity.startActivity(intent);
    }

    @BindView(R.id.id_file_detail_tbv) NavBarView mNavBarView;
    @BindView(R.id.id_slide_layout) View mSlideLayout;
    @BindView(R.id.id_slide_rev)
    RecyclerView mFileNameRcv;
    @BindView(R.id.id_file_pdf_view)
    PDFView mPDFView;



    private Bundle mBundle;

    private FileDetailContract.Presenter mPresenter;

    private FileDetailAdapter mFileDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mSlideLayout = findViewById(R.id.id_slide_layout);
        ButterKnife.bind(this);
        dataFormUpLevel();

        mPresenter = new FileDetailPresenter(null,this);

    }

    private void dataFormUpLevel() {
        mBundle = getIntent().getExtras().getBundle(BUNDLE);
        if (mBundle != null){
            mAgendaSum = mBundle.getInt(AGENDAS_SUM);
            mAgendaIndex = mBundle.getInt(AGENDA_INDEX);
            mFileIndex = mBundle.getInt(FILE_INDEX);
            mFileList = (List<Document>) mBundle.getSerializable(FILE_LIST);
            mAgendaTime = mBundle.getString(AGENDA_TIME);
            mUpLevelText = mBundle.getString(UP_LEVEL_TITLE);

            mFileName = mFileList.get(mFileIndex).getFileName();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        mNavBarView.setState("议程"+mAgendaIndex+"/"+mAgendaSum);
        mNavBarView.setTime(mAgendaTime);
        mNavBarView.setFallBackListener(this);

        mFileDetailAdapter = new FileDetailAdapter(mFileList,this);
        mFileNameRcv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mFileNameRcv.setAdapter(mFileDetailAdapter);
        mFileDetailAdapter.setOnFileClivk(this);
        mFileNameRcv.scrollToPosition(mFileIndex);
        mPresenter.loadFile(mFileList.get(0).getFileName());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            mChildCount = mFileNameRcv.getChildCount();
            setBackgroundColor(mChildCount,mFileIndex);
        }
    }

    private void setBackgroundColor(int childCount, int fileIndex) {
        for (int i = 0; i < childCount; i++) {
            ((LinearLayout)mFileNameRcv.getChildAt(i).findViewById(R.id.id_ll_file_name_all)).
                    setBackgroundColor(getResources().getColor(R.color.color_tab_normal));
            ((TextView)mFileNameRcv.getChildAt(i).findViewById(R.id.id_tv_item_file_name)).setTextColor(
                    getResources().getColor(R.color.color_black));
        }
        ((LinearLayout)mFileNameRcv.getChildAt(fileIndex).findViewById(R.id.id_ll_file_name_all)).
                setBackgroundColor(getResources().getColor(R.color.color_tab_selected));
        ((TextView)mFileNameRcv.getChildAt(fileIndex).findViewById(R.id.id_tv_item_file_name)).setTextColor(
                getResources().getColor(R.color.color_white));
    }

    @OnClick(R.id.id_iv_slide_to_left)
    void onToLeft(){
        mPresenter.slideLeft(null);
    }

    @OnClick(R.id.id_iv_slide_to_right)
    void toRight(){
        mPresenter.slideRight(null);
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
    public void onFileClick(int position) {
        mPresenter.loadFile(mFileList.get(position).getFileName());
        setBackgroundColor(mChildCount,position);
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
}
