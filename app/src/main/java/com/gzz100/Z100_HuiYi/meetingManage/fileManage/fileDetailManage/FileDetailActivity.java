package com.gzz100.Z100_HuiYi.meetingManage.fileManage.fileDetailManage;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.FileBean;

import java.util.List;

public class FileDetailActivity extends BaseActivity {
    public static final String BUNDLE = "bundle";
    public static final String AGENDAS_SUM = "agendasSum";
    public static final String AGENDA_INDEX = "agendaIndex";
    public static final String FILE_LIST = "fileList";
    public static final String FILE_INDEX = "fileIndex";
    public static final String AGENDA_TIME = "agendaTime";
    public static final String UP_LEVEL_TITLE = "upLevelTitle";

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
        intent.putExtra(BUNDLE,bundle);
        activity.startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
    }
}
