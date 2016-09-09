package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *@author DELL
 *creat at 2016/9/3
 */

public class DelegateDetailActivity extends BaseActivity implements DelegateDetailContract.View {


    /**
     *
     * @param activity 当前Activity
     * @param delegateBean 传输的信息
     */
    public static void showDelegateDetailActivity(Activity activity,DelegateBean delegateBean){
        Intent intent=new Intent(activity,DelegateDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(DELEGATE_NAME,delegateBean.getDelegateName());
        bundle.putString(DELEGATE_DEPARTMENT,delegateBean.getDelegateDepartment());
        bundle.putString(DELEGATE_ROLE,delegateBean.getRole());
        bundle.putString(DELEGATE_JOB,delegateBean.getDelegateJob());
        bundle.putSerializable(DELEGATE_AGENDALIST,delegateBean.getDelegateAgendaList());
        bundle.putString(DELEGATE_DETAIL_INFO,delegateBean.getDelegateDetailInfo());
        intent.putExtra(BUNDLE,bundle);

        activity.startActivity(intent);
    }
    public static final String BUNDLE="bundle";
    public static final String DELEGATE_NAME="delegate_name";
    public static final String DELEGATE_DEPARTMENT="delegate_dapartment";
    public static final String DELEGATE_ROLE="delegate_role";
    public static final String DELEGATE_JOB="delegate_job";
    public static final String DELEGATE_AGENDALIST="delegate_agenda_list";
    public static final String DELEGATE_DETAIL_INFO="delegate_detail_info";

    @BindView(R.id.id_delegat_detail_name)
    TextView mTvDelelgateName;
    @BindView(R.id.id_delelgate_department)
    TextView mTvDelegateDepartment;
    @BindView(R.id.id_delegate_detail_job)
    TextView mTvDelegateJob;
    @BindView(R.id.id_delegate_detail_role)
    TextView mTvDelegateRole;
    @BindView(R.id.id_delegate_detail_info)
    TextView mTvDelegateDetailInfo;

    private Bundle mBundle;
    private ArrayList<Integer> delegateAgendaIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_detail);
        ButterKnife.bind(this);
        handleDataFromUpLevel();
        showDelegateAgendaList();

    }
    public void handleDataFromUpLevel(){
        mBundle=getIntent().getExtras().getBundle(BUNDLE);
        mTvDelelgateName.setText(mBundle.getString(DELEGATE_NAME));
        mTvDelegateDepartment.setText(mBundle.getString(DELEGATE_DEPARTMENT));
        mTvDelegateJob.setText(mBundle.getString(DELEGATE_JOB));
        mTvDelegateRole.setText(mBundle.getString(DELEGATE_ROLE));
        mTvDelegateDetailInfo.setText(mBundle.getString(DELEGATE_DETAIL_INFO));


        delegateAgendaIndexList=(ArrayList<Integer>) mBundle.getSerializable(DELEGATE_AGENDALIST);
    }

    public void showDelegateAgendaList(){

        }
}
