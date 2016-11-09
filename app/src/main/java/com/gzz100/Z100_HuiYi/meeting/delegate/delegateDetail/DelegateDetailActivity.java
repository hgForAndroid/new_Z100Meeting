package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author DELL
 *         creat at 2016/9/3
 */

public class DelegateDetailActivity extends BaseActivity implements DelegateDetailContract.View, View.OnClickListener {

    /**
     * @param activity     当前Activity
     * @param delegateBean 传输的信息
     */
    public static void showDelegateDetailActivity(Activity activity, DelegateModel delegateBean, String upLevelText) {
        Intent intent = new Intent(activity, DelegateDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(UP_LEVEL_TEXT, upLevelText);
        bundle.putString(DELEGATE_NAME, delegateBean.getDelegateName());
        bundle.putString(DELEGATE_DEPARTMENT, delegateBean.getDelegateDepartment());
        bundle.putInt(DELEGATE_ROLE, delegateBean.getDelegateRole());
        bundle.putString(DELEGATE_JOB, delegateBean.getDelegateJob());
        bundle.putSerializable(DELEGATE_AGENDA_LIST, (Serializable) delegateBean.getDelegateAgendaIndexList());
        bundle.putString(DELEGATE_DETAIL_INFO, delegateBean.getDelegateDetailInfo());
        intent.putExtra(BUNDLE, bundle);

        activity.startActivity(intent);
    }

    public static final String BUNDLE = "bundle";
    public static final String UP_LEVEL_TEXT = "upLevelText";
    public static final String DELEGATE_NAME = "delegate_name";
    public static final String DELEGATE_DEPARTMENT = "delegate_department";
    public static final String DELEGATE_ROLE = "delegate_role";
    public static final String DELEGATE_JOB = "delegate_job";
    public static final String DELEGATE_AGENDA_LIST = "delegate_agenda_list";
    public static final String DELEGATE_DETAIL_INFO = "delegate_detail_info";

    @BindView(R.id.id_delegate_detail_tbv)
    NavBarView mNavBarView;
    @BindView(R.id.id_delegate_detail_name)
    TextView mTvDelegateName;
    @BindView(R.id.id_delelgate_department)
    TextView mTvDelegateDepartment;
    @BindView(R.id.id_delegate_detail_job)
    TextView mTvDelegateJob;
    @BindView(R.id.id_delegate_detail_role)
    TextView mTvDelegateRole;
    @BindView(R.id.id_delegate_detail_info)
    TextView mTvDelegateDetailInfo;
    @BindView(R.id.id_delegate_detail_part2)
    RecyclerView mRecyclerView ;
    @BindView(R.id.id_id_tv_delegate_detail_no_agenda)
    TextView mTvNoAgenda;
    @BindView(R.id.id_delegate_detail_role_image)
    ImageView mIvRole;

    private SpeakerAgendaAdapter mSpeakerAgendaAdapter;

    private Bundle mBundle;
    private ArrayList<Integer> delegateAgendaIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_detail);
        ButterKnife.bind(this);
        mNavBarView.setFallBackDisplay(true);
        mNavBarView.setTimeAndStateDisplay(false);
        mNavBarView.setTitle("个人资料");
        mNavBarView.setFallBackListener(this);
        handleDataFromUpLevel();
        showDelegateAgendaList();

    }

    public void handleDataFromUpLevel() {
        mBundle = getIntent().getExtras().getBundle(BUNDLE);
        if (mBundle != null) {
            //上一级标题
            mNavBarView.setUpLevelText(mBundle.getString(UP_LEVEL_TEXT));

            mTvDelegateName.setText(mBundle.getString(DELEGATE_NAME));
            mTvDelegateDepartment.setText(mBundle.getString(DELEGATE_DEPARTMENT));
            mTvDelegateJob.setText(mBundle.getString(DELEGATE_JOB));
            int roleType = mBundle.getInt(DELEGATE_ROLE);
            setRoleByType(roleType);

            mTvDelegateDetailInfo.setText(mBundle.getString(DELEGATE_DETAIL_INFO));

            delegateAgendaIndexList = (ArrayList<Integer>) mBundle.getSerializable(DELEGATE_AGENDA_LIST);
            if (delegateAgendaIndexList != null && delegateAgendaIndexList.size() > 0){
                //有主讲议程则显示，隐藏无主讲议程的提示
                mTvNoAgenda.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSpeakerAgendaAdapter = new SpeakerAgendaAdapter(DelegateDetailActivity.this,delegateAgendaIndexList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                mRecyclerView.setAdapter(mSpeakerAgendaAdapter);

            }else {//显示无主讲议程的提示
                mTvNoAgenda.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 根据角色类型设置角色名称
     * @param roleType   角色类型
     *                   Constant.KEYNOTE_SPEAKER  主讲人
     *                   Constant.HOST  主持人
     *                   Constant.NORMAL_DELEGATE  其他参会代表
     */
    private void setRoleByType(int roleType){
        if (roleType == Constant.DEFAULT_SPEAKER){//主讲人
            mIvRole.setImageResource(R.drawable.ic_delagate_detail_key_note_speaker);
            mTvDelegateRole.setText("主讲人");
        }else if (roleType == Constant.HOSTS){//主持人
            mIvRole.setImageResource(R.drawable.ic_dalegate_detail_host);
            mTvDelegateRole.setText("主持人");
        }else if (roleType == Constant.OTHER_DELEGATE){//普通参会人员
            mIvRole.setImageResource(R.drawable.ic_delegate_detail_normal);
            mTvDelegateRole.setText("其他参会代表");
        }
    }

    public void showDelegateAgendaList() {

    }

    @Override
    public void onClick(View v) {
        //返回上一级
        ActivityStackManager.pop();
    }
}
