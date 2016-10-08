package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.content.Context;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.MeetingSummary;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.data.signIn.SignInDataSource;
import com.gzz100.Z100_HuiYi.data.signIn.SignInRemoteDataSource;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View mView;
    private Context mContext;

    public SignInPresenter(Context context,SignInContract.View view) {
        this.mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    private boolean isFirst = true;
    @Override
    public void fetchCurrentUserBean(boolean fourUpdate, String IMEI, String meetingID) {
        if (isFirst || fourUpdate){
            SignInRemoteDataSource.getInstance(mContext).fetchUserBean(IMEI, meetingID, new SignInDataSource.LoadUserBeanCallback() {
                @Override
                public void onUserBeanLoaded(UserBean userBean) {
                    mView.showDelegate(userBean);
                    //保存当前用户角色
                    saveUserRole(userBean.getUserRole());
                    //开启下载
                    mView.startDownLoad(userBean.getDocumentURLList());
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showNoDelegate();
                }
            });
        }
        //TODO  有服务器后去掉，这里测试用，1主持人，2其他
        saveUserRole(2);
    }

    /**
     * 保存用户角色，之后根据此值作为控制操作的判断基准
     * @param role    角色类型 ，1代表主持人，2，代表听众
     */
    private void saveUserRole(int role){
        SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).putInt(Constant.USER_ROLE,role);
    }

    @Override
    public void signIn(String IMEI,String meetingID) {
        SignInRemoteDataSource.getInstance(mContext).signIn(IMEI, meetingID, new SignInDataSource.LoadMeetingSummaryCallback() {
            @Override
            public void onMeetingSummaryLoaded(MeetingSummary meetingSummary) {
                //分离对象并存储进数据库
                splitAndSaveDataToDatabase(meetingSummary);
                mView.showMainActivity();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        //TODO 有接口后去掉
        mView.showMainActivity();

    }

    /**
     * 拆分数据集合，并将拆分的数据分别存储进数据库中
     * @param meetingSummary    会议的基本数据集合
     */
    private void splitAndSaveDataToDatabase(MeetingSummary meetingSummary) {
        saveMeetingInfo(meetingSummary);
        saveDelegateList(meetingSummary);
        saveAgendaList(meetingSummary);
        saveDocumentList(meetingSummary);
    }

    /**
     * 保存文件列表
     * @param meetingSummary   会议的基本数据集合
     */
    private void saveDocumentList(MeetingSummary meetingSummary) {
        if (meetingSummary.getAgendaList() != null && meetingSummary.getAgendaList().size()>0){
            Map<Integer,List<Document>> documents = new HashMap<>();
            //根据议程数创建文件集合
            for (int i = 0; i < meetingSummary.getAgendaList().size(); i++) {
                List<Document> documentsList = new ArrayList<>();
                documents.put(meetingSummary.getAgendaList().get(i).getAgendaIndex(),documentsList);
            }
            List<Document> documentList = meetingSummary.getDocumentList();
            for (Document document:documentList){
                //根据文件的所属议程获取到对应的文件集合，将文件加入该集合
                documents.get(document.getDocumentAgendaIndex()).add(document);
            }
            //保存所有的文件集合，这里用议程数跟文件集合数都可以，他们是等量的
            for (int i = 0; i < meetingSummary.getAgendaList().size(); i++) {
                int agendaIndex = meetingSummary.getAgendaList().get(i).getAgendaIndex();
                FileOperate.getInstance(mContext).insertFileList(agendaIndex,documents.get(agendaIndex));
            }
        }
    }

    /**
     * 保存议程列表
     * @param meetingSummary  会议的基本数据集合
     */
    private void saveAgendaList(MeetingSummary meetingSummary) {
        List<Agenda> agendaList = meetingSummary.getAgendaList();
        FileOperate.getInstance(mContext).insertAgendaList(Constant.COLUMNS_AGENDAS,agendaList);
    }

    /**
     * 保存参会人员列表
     * @param meetingSummary  会议的基本数据集合
     */
    private void saveDelegateList(MeetingSummary meetingSummary) {
        List<DelegateBean> delegateList = meetingSummary.getDelegateList();
        MeetingOperate.getInstance(mContext).insertUserList(Constant.COLUMNS_USER,delegateList);
    }

    /**
     * 保存会议概况
     * @param meetingSummary   会议的基本数据集合
     */
    private void saveMeetingInfo(MeetingSummary meetingSummary) {
        MeetingInfo meetingInfo = new MeetingInfo();
        meetingInfo.setMeetingName(meetingSummary.getMeetingName());
        meetingInfo.setMeetingBeginTime(meetingSummary.getMeetingBeginTime());
        meetingInfo.setMeetingDuration(meetingSummary.getMeetingDuration());
        meetingInfo.setDelegateNum(meetingSummary.getDelegateNum());
        meetingInfo.setAgendaNum(meetingSummary.getAgendaNum());
        MeetingOperate.getInstance(mContext).insertMeetingInfo(Constant.COLUMNS_MEETING_INFO,meetingInfo);
    }

    @Override
    public void start() {
        //这里开始取数据需要传递参数，所以这个方法先不采用，直接调用  fetchCurrentUserBean
    }
}
