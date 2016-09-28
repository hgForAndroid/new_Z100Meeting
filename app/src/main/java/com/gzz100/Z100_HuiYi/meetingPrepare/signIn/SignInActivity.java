package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.service.DownLoadService;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;
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
    private String mMeetingID;
    private Dialog mDialog;
    private Intent mIntent;
    private String mUrlPrefix;

    public static void toSignInActivity(Context context,String IMEI,String meetingID){
        Intent intent = new Intent(context,SignInActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_IMEI,IMEI);
        bundle.putString(MEETING_ID,meetingID);
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
            mMeetingID = getIntent().getBundleExtra(BUNDLE).getString(MEETING_ID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.fetchCurrentUserBean(false,mDeviceIMEI,mMeetingID);
        EventBus.getDefault().register(this);
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
                mIntent.putExtra("url",mUrlPrefix+mFileIDs.get(position));
                mIntent.putExtra("id",position);
                mIntent.putExtra("name",mFileIDs.get(position));
                startService(mIntent);
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
        ActivityStackManager.pop();
    }

    @Override
    public void startDownLoad(List<String> fileIDs) {
        if (fileIDs != null && fileIDs.size() > 0){
            mFileIDs = fileIDs;
            mIntent = new Intent(SignInActivity.this, DownLoadService.class);
            mUrlPrefix = SharedPreferencesUtil.getInstance(this.getApplicationContext())
                    .getString(Constant.CURRENT_IP, "") + "/DownloadDocument?fileID=";
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
