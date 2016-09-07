package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;

/**
 *@author DELL
 *creat at 2016/9/3
 */

public class DelegateDetailActivity extends BaseActivity implements DelegateDetailContract.View {

    public static final String BUNDLE="bundle";
    public static final String DELEGATE_NAME="delegate_name";
    public static final String DELEGATE_DEPARTMENT="delegate_dapartment";
    public static final String DELEGATE_ROLE="delegate_role";
//    public static final String DELEGATE_IMAGE;
    public static final String DELEGATE_AGENDALIST="delegate_agenda_list";
    public static final String DELEGATE_DETAIL_INFO="delegate_detail_info";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_detail);


    }

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
        bundle.putSerializable(DELEGATE_AGENDALIST,delegateBean.getDelegateAgendaList());
        bundle.putString(DELEGATE_DETAIL_INFO,delegateBean.getDelegateDetailInfo());
        intent.putExtra(BUNDLE,bundle);

        activity.startActivity(intent);
    }
}
