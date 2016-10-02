package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting.SelectMeetingActivity;
import com.gzz100.Z100_HuiYi.multicast.MulticastService;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import org.greenrobot.eventbus.EventBus;

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
        Intent intent = new Intent(this,MulticastService.class);
        this.startService(intent);
//        startService(new Intent(this,MulticastService.class));
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
//        Intent intent = new Intent();
//        intent.setAction("action.GET_MULYICAST");
//        stopService(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("退出系统？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopService(new Intent(ConnectServerActivity.this,MulticastService.class));
                            ActivityStackManager.exit();
                        }
                    })
                    .setNegativeButton("否",null)
                    .create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
