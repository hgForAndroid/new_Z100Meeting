package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.multicast.SendMulticastService;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.service.DownLoadService;
import com.gzz100.Z100_HuiYi.tcpController.ClientSendMessageUtil;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
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
        ActivityStackManager.clearExceptOne(this);
        ButterKnife.bind(this);
        initGetIntent();
        mPresenter = new SignInPresenter(this,this);
    }

    /**
     * 先获取Intent携带的设备IMEI码，和会议id
     */
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
        //获取当前用户
        mPresenter.fetchCurrentUserBean(false,mDeviceIMEI,mMeetingID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 按签到按钮
     */
    @OnClick(R.id.id_btn_sign_in)
    void signIn(){
        mPresenter.signIn(mDeviceIMEI,mMeetingID);
    }

    /**
     * 在{@link DownLoadService#downloadCompleted(String)}中调用
     * 该方法是下载会议文件的，先已使用webView显示会议文件，该方法已没有使用
     * 继续下载下一个文件，需下载的文件列表从选择会议获取，在本类中应该保存。
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

    /**
     * 显示该设备对应的参会人员的信息。
     * @param userBean  参会人员实体类
     */
    @Override
    public void showDelegate(UserBean userBean) {
        mTvPosition.setText(userBean.getUserJob());
        mTvName.setText(userBean.getUserName());
        SharedPreferencesUtil.getInstance(this).putString(Constant.USER_NAME,userBean.getUserName());
        //这里发送数据到铭牌成功后才开启tcp服务。
        sendKeyMessageToClients();
    }
    /**
     * 使用组播发送信息给全部客户端，信息包括 会议id，服务器ip，当前主持人平板设备在局域网内的ip
     */
    private void sendKeyMessageToClients() {
        //该角色在进入该Activity时，在onStart方法中调用mPresenter.fetchCurrentUserBean(false,mDeviceIMEI,mMeetingID);
        //里面取完值就已经赋值
        if (MyAPP.getInstance().getUserRole() == 1){//主持人端才发送组播,启动TCP服务器端服务
            //开启组播
            String localIpAddress = MPhone.getWIFILocalIpAdress(this.getApplicationContext());
            SharedPreferencesUtil.getInstance(this).putInt(Constant.MEETING_ID,mMeetingID);
            //打开发送组播信息的服务
            Intent intent = new Intent(this,SendMulticastService.class);
            intent.putExtra("localIpAddress",localIpAddress);
            startService(intent);
            //开启tcp服务器
            mPresenter.startTCPService();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String ip = MPhone.getWIFILocalIpAdress(SignInActivity.this.getApplicationContext());
                    //发送消息给tcp服务器端，让其保存当前客户端的名字+ip
                    ClientSendMessageUtil.getInstance().sendMessage(mTvName.getText().toString()+","+ip);
                }
            }).start();
        }
    }

    /**
     * 获取当前设备对应的参会人员信息失败。
     */
    @Override
    public void showNoDelegate() {
        mDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.string_tip)
                .setMessage(R.string.string_fetch_info_fail)
                .setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.fetchCurrentUserBean(true,mDeviceIMEI,mMeetingID);
                    }
                })
                .create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    /**
     * 点击签到后，调用此方法，跳转到主界面。
     */
    @Override
    public void showMainActivity() {
        MainActivity.toMainActivity(this);
        ActivityStackManager.pop();
    }

    /**
     * 开始下载文件。该方法同 {@link #continueDownLoad} 一样，已经不再使用。
     * @param fileIDs   文件id集合
     */
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.string_tip)
                    .setMessage(R.string.string_exit_system)
                    .setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesUtil.getInstance(SignInActivity.this).killAllRunningService();
                            //删除会议前预下载的所有文件
                            AppUtil.DeleteFolder(AppUtil.getCacheDir(SignInActivity.this));
                            ActivityStackManager.exit();
                        }
                    })
                    .setNegativeButton(R.string.string_no, null)
                    .create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null)
            mDialog = null;
    }

}
