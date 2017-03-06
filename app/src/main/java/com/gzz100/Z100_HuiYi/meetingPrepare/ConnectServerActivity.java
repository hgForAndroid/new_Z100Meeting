package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectServerActivity extends BaseActivity implements ConnectServerContract.View, OnIPClickListener {
    @BindView(R.id.id_edt_connect_server)
    EditText mEdtIP;
    @BindView(R.id.text_record)
    TextView mTvRecord;
    @BindView(R.id.id_rcv_connect_server)
    RecyclerView mRcvRecord;//显示ip历史记录的控件

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @OnClick(R.id.id_btn_connect_server)
    void onClick() {
        mIp = mEdtIP.getText().toString().trim();
        if (TextUtils.isEmpty(mIp)) {
            ToastUtil.showMessage(R.string.string_input_server_ip);
            return;
        }
        mPresenter.saveIpThenShowSelectMeeting(mIp);
    }

    /**
     * 显示ip历史记录
     * @param ips   记录的所有ip集合
     */
    @Override
    public void showHistory(List<String> ips) {
        mTvRecord.setVisibility(View.VISIBLE);
        mRcvRecord.setVisibility(View.VISIBLE);
        mIPs = ips;
        mAdapter = new IPAdapter(this, mIPs);
        mRcvRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRcvRecord.addItemDecoration(new ItemSpaceDecoration(ItemSpaceDecoration.VERTICAL,
                (int) getResources().getDimension(R.dimen.distance_five_dp)));
        mRcvRecord.setAdapter(mAdapter);
        mAdapter.setOnIPClickListener(this);
    }

    /**
     * 没有历史记录，则隐藏控件
     */
    @Override
    public void showNoHistory() {
        mTvRecord.setVisibility(View.GONE);
        mRcvRecord.setVisibility(View.GONE);
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
     * @param ip   选中的ip
     */
    @Override
    public void setIPFromHistory(String ip) {
        mEdtIP.setText(ip);
    }

    /**
     * 该方法是除主持人端外，其他所有的客户端接收到组播信息后，跳转到签到页面
     * @param keyInfoBean  关键信息的实体类，包括服务器ip，会议id，主持人端平板ip
     */
    @Override
    public void showSignInActivity(KeyInfoBean keyInfoBean) {
        //启动请求连接TCP的服务
        startService(new Intent(this, Client.class));
        //获取到后台服务器的ip，以及开启的会议id后，跳转到签到界面
        //还需要当前设备的IMEI
        String deviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        SignInActivity.toSignInActivity(this, deviceIMEI, keyInfoBean.getMeetingId());
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
