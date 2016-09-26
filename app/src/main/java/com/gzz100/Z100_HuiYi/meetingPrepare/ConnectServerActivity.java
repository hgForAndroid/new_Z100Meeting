package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting.SelectMeetingActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

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
    RecyclerView mRcvRecord;

    private ConnectServerContract.Presenter mPresenter;
    private List<String> mIPs;

    private IPAdapter mAdapter;
    private String mIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_server);
        ButterKnife.bind(this);
        mPresenter = new ConnectServerPresenter(this.getApplicationContext(),this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();

    }

    @OnClick(R.id.id_btn_connect_server)
    void onClick() {
        mIp = mEdtIP.getText().toString().trim();
        mPresenter.saveIp(mIp);
    }

    @Override
    public void showHistory(List<String> ips) {
        mTvRecord.setVisibility(View.VISIBLE);
        mRcvRecord.setVisibility(View.VISIBLE);
        mIPs = ips;
        mAdapter = new IPAdapter(this,mIPs);
        mRcvRecord.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRcvRecord.addItemDecoration(new ItemSpaceDecoration(ItemSpaceDecoration.VERTICAL,
                (int) getResources().getDimension(R.dimen.distance_five_dp)));
        mRcvRecord.setAdapter(mAdapter);
        mAdapter.setOnIPClickListener(this);
    }

    @Override
    public void showNoHistory() {
        mTvRecord.setVisibility(View.GONE);
        mRcvRecord.setVisibility(View.GONE);
    }

    @Override
    public void showSelectMeeting() {
        mPresenter.saveCurrentIP(mIp);
        SelectMeetingActivity.toSelectMeetingActivity(this);
    }

    @Override
    public void setIPFromHistory(String ip) {
        mEdtIP.setText(ip);
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

    @Override
    public void onDelete(int position) {
        mPresenter.deleteIP(position);
    }
}
