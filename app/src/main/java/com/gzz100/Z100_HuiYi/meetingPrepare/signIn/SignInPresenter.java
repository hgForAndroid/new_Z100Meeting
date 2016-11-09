package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.MeetingSummary;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingOperate;
import com.gzz100.Z100_HuiYi.data.signIn.SignInDataSource;
import com.gzz100.Z100_HuiYi.data.signIn.SignInRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteOperate;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;
import com.gzz100.Z100_HuiYi.multicast.MulticastController;
import com.gzz100.Z100_HuiYi.multicast.SendMulticastService;
import com.gzz100.Z100_HuiYi.tcpController.Server;
import com.gzz100.Z100_HuiYi.tcpController.TcpService;
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
    private Gson mGson;

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
                    //保存当前用户角色
                    saveUserRole(userBean.getUserRole());
                    saveUserId(userBean.getUserID());
                    saveUserName(userBean.getUserName());
                    mView.showDelegate(userBean);
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
//        saveUserRole(2);
    }

    /**
     * 保存用户名，用于主场景显示当前参会人员背景不同
     * @param userName
     */
    private void saveUserName(String userName) {
        SharedPreferencesUtil.getInstance(mContext).putString(Constant.USER_NAME,userName);
    }

    /**
     * 保存用户角色，之后根据此值作为控制操作的判断基准
     * @param role    角色类型 ，1代表主持人，2，代表听众
     */
    private void saveUserRole(int role){
        MyAPP.getInstance().setUserRole(role);
    }
    private void saveUserId(int userId){
        MyAPP.getInstance().setUserId(userId);
    }

    @Override
    public void signIn(String IMEI,String meetingID) {
        SignInRemoteDataSource.getInstance(mContext).signIn(IMEI, meetingID, new SignInDataSource.LoadMeetingSummaryCallback() {
            @Override
            public void onMeetingSummaryLoaded(MeetingSummaryBean meetingSummary) {
                //分离对象并存储进数据库
                //保存用户列表不放在WriteDatabaseService，
                //是为了避免刚进入主场景界面，用户列表还未写入
                saveDelegateList(meetingSummary);
                mView.showMainActivity();
                WriteDatabaseService.startWriteDatabaseService(mContext,meetingSummary);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }
    /**
     * 保存参会人员列表
     * @param meetingSummary  会议的基本数据集合
     */
    private void saveDelegateList(MeetingSummaryBean meetingSummary) {
        List<DelegateModel> delegateList = meetingSummary.getDelegateModelList();
        MeetingOperate.getInstance(mContext).insertUserList(Constant.COLUMNS_USER,delegateList);
    }
    @Override
    public void start() {
        //这里开始取数据需要传递参数，所以这个方法先不采用，直接调用  fetchCurrentUserBean
    }

    //已经没用
    @Override
    public void sendMeetingIdAndServerIP(String meetingId,String tcpServerIP) {
        String serverIP = SharedPreferencesUtil.getInstance(mContext).getString(Constant.CURRENT_IP, "");
        Intent intent = new Intent(mContext, SendMulticastService.class);
        intent.putExtra(Constant.MEETING_ID,meetingId);
        intent.putExtra(Constant.CURRENT_IP,serverIP);
        intent.putExtra(Constant.TCP_SERVER_IP,tcpServerIP);
        mContext.startService(intent);

//        KeyInfoBean keyInfoBean = new KeyInfoBean(serverIP,meetingId,tcpServerIP);
//        mGson = new Gson();
//        final String keyInfoJson = mGson.toJson(keyInfoBean);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    //因为是udp组播发送，循环发送这些值到客户端，确保接收到
//                    MulticastController.getDefault().sendMessage(keyInfoJson);
//                }
//            }
//        }).start();
    }

    @Override
    public void startTCPService() {
        Intent tcpServiceIntent = new Intent(mContext, Server.class);
        mContext.startService(tcpServiceIntent);
    }
}
