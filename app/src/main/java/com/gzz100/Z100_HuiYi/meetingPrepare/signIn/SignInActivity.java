package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;
import com.gzz100.Z100_HuiYi.multicast.MulticastController;
import com.gzz100.Z100_HuiYi.multicast.SendMulticastService;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.service.DownLoadService;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements SignInContract.View{
    private static final String BUNDLE = "bundle";
    private static final String MEETING_ID = "meetingID";
    private static final String DEVICE_IMEI = "deviceIMEI";
    private String mDeviceIMEI;
    private int mMeetingID;
    private Dialog mDialog;
    private Intent mIntent;
    private String mUrlPrefix;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    public static void toSignInActivity(Context context,String IMEI,int meetingID){
        Intent intent = new Intent(context,SignInActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_IMEI,IMEI);
        bundle.putInt(MEETING_ID,meetingID);
        intent.putExtra(BUNDLE,bundle);
        context.startActivity(intent);
    }

    @BindView(R.id.id_tv_position_sign_in)
    TextView mTvPosition;
    @BindView(R.id.id_tv_name_sign_in)
    TextView mTvName;

    private SignInContract.Presenter mPresenter;

    private List<String> mFileIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        initGetIntent();
        mPresenter = new SignInPresenter(this,this);
    }

    private void initGetIntent() {
        if (getIntent().getBundleExtra(BUNDLE) != null){
            mDeviceIMEI = getIntent().getBundleExtra(BUNDLE).getString(DEVICE_IMEI);
            mMeetingID = getIntent().getBundleExtra(BUNDLE).getInt(MEETING_ID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        mPresenter.fetchCurrentUserBean(false,mDeviceIMEI,mMeetingID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 继续下载下一个文件，需下载的文件列表从选择会议获取，在本类中应该保存
     * @param position    下个文件的序号
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void continueDownLoad(Integer position){
        if (mFileIDs != null && mFileIDs.size() > 0){
            if (position < mFileIDs.size()){
                mIntent = new Intent(SignInActivity.this, DownLoadService.class);
                mIntent.putExtra("flag",true);
                mIntent.putExtra("url",mUrlPrefix+mFileIDs.get(position));
                mIntent.putExtra("id",position);
                mIntent.putExtra("name",mFileIDs.get(position));
                startService(mIntent);
            }else {
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_completed)
                        .setContentTitle("文件下载")
                        .setContentText("文件已全部下载完成");
                mNotificationManager.notify(mFileIDs.size(),mNotificationBuilder.build());
            }
        }
    }

    @OnClick(R.id.id_btn_sign_in)
    void signIn(){
        mPresenter.signIn(mDeviceIMEI,mMeetingID);
    }

    @Override
    public void showDelegate(UserBean userBean) {
        mTvPosition.setText(userBean.getUserJob());
        mTvName.setText(userBean.getUserName());

        sendKeyMessageToClients();

    }
    //使用组播发送信息给全部客户端，信息包括 会议id，服务器ip，当前主持人平板设备在局域网内的ip
    private void sendKeyMessageToClients() {
        //该角色在进入该Activity时，在onStart方法中调用mPresenter.fetchCurrentUserBean(false,mDeviceIMEI,mMeetingID);
        //里面取完值就已经赋值
        if (MyAPP.getInstance().getUserRole() == 1){//主持人才发送组播,启动TCP服务器端服务
            //开启组播
            String localIpAddress = MPhone.getWIFILocalIpAdress(this.getApplicationContext());
            SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_ID,mMeetingID);

            Intent intent = new Intent(this,SendMulticastService.class);
            intent.putExtra("localIpAddress",localIpAddress);
            startService(intent);

            mPresenter.startTCPService();
        }
    }

    @Override
    public void showNoDelegate() {
        mDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("获取信息失败，是否重新获取")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.fetchCurrentUserBean(true,mDeviceIMEI,mMeetingID);
                    }
                })
                .create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    @Override
    public void showMainActivity() {
        MainActivity.toMainActivity(this);
        if (MyAPP.getInstance().getUserRole() == 1){
//            if (AppUtil.isServiceRun(this.getApplicationContext(),"com.gzz100.Z100_HuiYi.multicast.ReceivedMultiCastService")){
//                Log.e("服务在运行","=============================================================================");
//            }
//            stopService(new Intent(this, ReceivedMultiCastService.class));
        }
        ActivityStackManager.pop();
    }

    @Override
    public void startDownLoad(List<String> fileIDs) {
        if (fileIDs != null && fileIDs.size() > 0){
            mFileIDs = fileIDs;
            mIntent = new Intent(SignInActivity.this, DownLoadService.class);
            mUrlPrefix = "http://"+SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.CURRENT_IP, "") + "/api/Common/DownloadDocument?documentPath=";
            mIntent.putExtra("url",mUrlPrefix+mFileIDs.get(0));
            mIntent.putExtra("id",0);
            mIntent.putExtra("name",mFileIDs.get(0));
            startService(mIntent);
        }
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null)
            mDialog = null;
    }

}
