package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.data.vote.VoteOperate;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 将签到返回的数据写入数据库中
* @author XieQXiong
* create at 2016/11/9 9:00
*/


public class WriteDatabaseService extends IntentService {

    private static final String KEY_MEETINGSUMMARYBEAN = "meetingSummaryBean";
    private MeetingSummaryBean mMeetingSummaryBean;

    public WriteDatabaseService() {
        super("WriteDatabaseService");
    }
    // TODO: Customize helper method
    public static void startWriteDatabaseService(Context context,MeetingSummaryBean meetingSummaryBean) {
        Intent intent = new Intent(context, WriteDatabaseService.class);
        intent.putExtra(KEY_MEETINGSUMMARYBEAN,meetingSummaryBean);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mMeetingSummaryBean = (MeetingSummaryBean) intent.getSerializableExtra(KEY_MEETINGSUMMARYBEAN);
        splitAndSaveDataToDatabase(mMeetingSummaryBean);
    }


    /**
     * 拆分数据集合，并将拆分的数据分别存储进数据库中
     * @param meetingSummary    会议的基本数据集合
     */
    private void splitAndSaveDataToDatabase(MeetingSummaryBean meetingSummary) {
        saveMeetingInfo(meetingSummary);
//        saveDelegateList(meetingSummary);
        saveAgendaList(meetingSummary);
        saveDocumentList(meetingSummary);
        saveVoteList(meetingSummary);
    }
    /**
     * 保存投票列表
     * @param meetingSummary   会议的基本数据集合
     */
    private void saveVoteList(MeetingSummaryBean meetingSummary) {
        if (meetingSummary.getVoteList() != null && meetingSummary.getVoteList().size()>0){
            VoteOperate.getInstance(this.getApplicationContext()).
                    insertVoteList(meetingSummary.getVoteList());
        }
    }

    /**
     * 保存文件列表
     * @param meetingSummary   会议的基本数据集合
     */
    private void saveDocumentList(MeetingSummaryBean meetingSummary) {
        if (meetingSummary.getAgendaModelList() != null && meetingSummary.getAgendaModelList().size()>0){
            Map<Integer,List<DocumentModel>> documents = new HashMap<>();
            //根据议程数创建文件集合
            for (int i = 0; i < meetingSummary.getAgendaModelList().size(); i++) {
                List<DocumentModel> documentsList = new ArrayList<>();
                documents.put(meetingSummary.getAgendaModelList().get(i).getAgendaIndex(),documentsList);
            }
            List<DocumentModel> documentList = meetingSummary.getDocumentModelList();
            for (DocumentModel document:documentList){
                //根据文件的所属议程获取到对应的文件集合，将文件加入该集合
                documents.get(document.getDocumentAgendaIndex()).add(document);
            }
            //保存所有的文件集合，这里用议程数跟文件集合数都可以，他们是等量的
            for (int i = 0; i < meetingSummary.getAgendaModelList().size(); i++) {
                int agendaIndex = meetingSummary.getAgendaModelList().get(i).getAgendaIndex();
                FileOperate.getInstance(this.getApplicationContext()).
                        insertFileList(agendaIndex,documents.get(agendaIndex));
            }
        }
    }

    /**
     * 保存议程列表
     * @param meetingSummary  会议的基本数据集合
     */
    private void saveAgendaList(MeetingSummaryBean meetingSummary) {
        List<AgendaModel> agendaList = meetingSummary.getAgendaModelList();
        FileOperate.getInstance(this.getApplicationContext()).
                insertAgendaList(Constant.COLUMNS_AGENDAS,agendaList);
    }
    /**
     * 保存会议概况
     * @param meetingSummary   会议的基本数据集合
     */
    private void saveMeetingInfo(MeetingSummaryBean meetingSummary) {
        MeetingInfo meetingInfo = new MeetingInfo();
        meetingInfo.setMeetingName(meetingSummary.getMeetingModel().getMeetingName());
        meetingInfo.setMeetingBeginTime(meetingSummary.getMeetingModel().getMeetingBeginTime());
        meetingInfo.setMeetingDuration(meetingSummary.getMeetingModel().getMeetingDuration()+"分钟");
        meetingInfo.setDelegateNum(meetingSummary.getDelegateNum());
        meetingInfo.setAgendaNum(meetingSummary.getAgendaNum());
        MeetingOperate.getInstance(this.getApplicationContext()).
                insertMeetingInfo(Constant.COLUMNS_MEETING_INFO,meetingInfo);
    }
}
