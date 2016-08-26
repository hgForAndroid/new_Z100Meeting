package com.gzz100.Z100_HuiYi.meetingManage.fileManage.fileDetailManage;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.FileBean;
import com.gzz100.Z100_HuiYi.widget.NavBarView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileDetailActivity extends BaseActivity {
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
    private List<FileBean> mFileList;
    private String mAgendaTime;
    private String mUpLevelText;
    private String mFileName;

    /**
     * 跳转到文件详情界面
     * @param activity       当前Activity
     * @param agendasSum     议程总数
     * @param agendaIndex    当前议程序号
     * @param fileList       文件列表
     * @param fileIndex      选择的文件序号
     * @param agendaTime     议程时间
     * @param upLevelTitle   当前界面的标题
     */
    public static void showFileDetailActivity(Activity activity, int agendasSum,
                                              int agendaIndex, List<FileBean> fileList,
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

    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        ButterKnife.bind(this);
        dataFormUpLevel();

    }

    private void dataFormUpLevel() {
        mBundle = getIntent().getExtras().getBundle(BUNDLE);
        if (mBundle != null){
            mAgendaSum = mBundle.getInt(AGENDAS_SUM);
            mAgendaIndex = mBundle.getInt(AGENDA_INDEX);
            mFileIndex = mBundle.getInt(FILE_INDEX);
            mFileList = (List<FileBean>) mBundle.getSerializable(FILE_LIST);
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

    private void initData() {
        mNavBarView.setFallBackDisplay(true);
        mNavBarView.setTitle(mFileName);
        mNavBarView.setUpLevelText(mUpLevelText);
        mNavBarView.setState("议程"+mAgendaIndex+"/"+mAgendaSum);
    }
}
