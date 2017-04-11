package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting.SelectMeetingActivity;
import com.gzz100.Z100_HuiYi.meetingPrepare.signIn.SignInActivity;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;
import com.gzz100.Z100_HuiYi.tcpController.Client;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.ScreenSize;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectServerActivity extends BaseActivity implements ConnectServerContract.View, OnIPClickListener {

    @BindView(R.id.id_btn_ensure_activity_connect_server)
    Button mBtnEnsure;
    @BindView(R.id.id_exp_activity_connect_server)
    ExpandableEditText mExpandableEditText;

    private ConnectServerContract.Presenter mPresenter;
    private List<String> mIPs;//输入的所有ip

    private IPAdapter mAdapter;//输入的ip的历史记录适配器
    private String mIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_server);
        ButterKnife.bind(this);
        mPresenter = new ConnectServerPresenter(this.getApplicationContext(), this);
        mExpandableEditText.setDropDownEnable(ScreenSize.dp2px(this, 500), 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 调用下面的方法，首先去获取ip的历史记录。
         * 在{@link ConnectServerPresenter#start()}中调用。
         */
        mPresenter.start();
    }

    @OnClick(R.id.id_btn_ensure_activity_connect_server)
    public void onEnsureClick() {
        mIp = mExpandableEditText.getContent();
        if (TextUtils.isEmpty(mIp)) {
            ToastUtil.showMessage(R.string.string_input_server_ip);
            return;
        }
        mPresenter.saveIpThenShowSelectMeeting(mIp);
    }

    @OnClick(R.id.id_ll_activity_connect_server)
    public void clickOutSideArea(View view) {
        //点击外面的区域，如果显示着下拉框，则隐藏掉下拉
        mExpandableEditText.dismiss();
        hideSoftInput(view);
    }

    /**
     * 隐藏软件盘
     *
     * @param view
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示ip历史记录
     *
     * @param ips 记录的所有ip集合
     */
    @Override
    public void showHistory(List<String> ips) {
        mIPs = ips;
        mAdapter = new IPAdapter(this, mIPs);
//        mRcvRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mRcvRecord.addItemDecoration(new ItemSpaceDecoration(ItemSpaceDecoration.VERTICAL,
//                (int) getResources().getDimension(R.dimen.distance_five_dp)));
//        mRcvRecord.setAdapter(mAdapter);

        mExpandableEditText.addItemDecoration(new ItemSpaceDecoration(ItemSpaceDecoration.VERTICAL,
                (int) getResources().getDimension(R.dimen.distance_five_dp)));
        mExpandableEditText.setAdapterForDropDown(mAdapter);


        mAdapter.setOnIPClickListener(this);
    }

    /**
     * 没有历史记录，则隐藏控件
     */
    @Override
    public void showNoHistory() {
//        mTvRecord.setVisibility(View.GONE);
//        mRcvRecord.setVisibility(View.GONE);
        mExpandableEditText.dismiss();
    }

    /**
     * 跳转到会议列表的activity
     */
    @Override
    public void showSelectMeeting() {
        SelectMeetingActivity.toSelectMeetingActivity(this);
    }

    /**
     * 从ip历史记录中选中某个ip后，将其设置到EditText中
     *
     * @param ip 选中的ip
     */
    @Override
    public void setIPFromHistory(String ip) {
//        mEdtIP.setText(ip);
        mExpandableEditText.setContentForEditText(ip);
        mExpandableEditText.dismiss();
    }

    /**
     * 该方法是除主持人端外，其他所有的客户端接收到组播信息后，跳转到签到页面.
     * 在{@link ConnectServerPresenter#mRunnable}中接收到组播信息后，会调用到该方法。
     *
     * @param keyInfoBean 关键信息的实体类，包括服务器ip，会议id，主持人端平板ip
     */
    @Override
    public void showSignInActivity(KeyInfoBean keyInfoBean) {
        //启动请求连接TCP的服务
        startService(new Intent(this, Client.class));
        //获取到后台服务器的ip，以及开启的会议id后，跳转到签到界面
        //还需要当前设备的IMEI
        String deviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        SignInActivity.toSignInActivity(this, deviceIMEI, keyInfoBean.getMeetingId(), keyInfoBean.getMeetingName());
        ActivityStackManager.pop();//这里pop掉是因为客户端不需要重新回来输入ip地址
    }

    @Override
    public void setPresenter(ConnectServerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onIPClick(int position) {
        mIp = mIPs.get(position);
        mPresenter.getIPFromHistory(position);
    }

    /**
     * 删除历史记录中的某个ip
     *
     * @param position
     */
    @Override
    public void onDelete(int position) {
        mPresenter.deleteIP(position);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //等待接收主持人端发送过来的数据
        mPresenter.receivedKeyInfoFromHost();
    }
}
