package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.eventBean.Home;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;
import com.gzz100.Z100_HuiYi.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author DELL
 *         creat at 2016/9/3
 */

public class DelegateDetailActivity extends BaseActivity implements DelegateDetailContract.View, View.OnClickListener {
    private static final String BUNDLE = "bundle";
    private static final String UP_LEVEL_TEXT = "upLevelText";
    private static final String DELEGATE_NAME = "delegate_name";
    private static final String DELEGATE_DEPARTMENT = "delegate_department";
    private static final String DELEGATE_ROLE = "delegate_role";
    private static final String DELEGATE_JOB = "delegate_job";
    private static final String DELEGATE_AGENDA_LIST = "delegate_agenda_list";
    private static final String DELEGATE_DETAIL_INFO = "delegate_detail_info";

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
    RecyclerView mRecyclerView;
    @BindView(R.id.id_id_tv_delegate_detail_no_agenda)
    TextView mTvNoAgenda;
    @BindView(R.id.id_delegate_detail_role_image)
    ImageView mIvRole;

    private SpeakerAgendaAdapter mSpeakerAgendaAdapter;

    private Bundle mBundle;
    private ArrayList<Integer> delegateAgendaIndexList;
    private List<AgendaModel> mAgendaModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_detail);
        ButterKnife.bind(this);
        mNavBarView.setFallBackDisplay(true);
        mNavBarView.setTimeAndStateDisplay(false);
        mNavBarView.setTitle("个人资料");
        mNavBarView.setFallBackListener(this);
        mNavBarView.setMeetingAndTimeDisplay(false);
        handleDataFromUpLevel();

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

            if (!TextUtils.isEmpty(mBundle.getString(DELEGATE_DETAIL_INFO))){
                mTvDelegateDetailInfo.setText(mBundle.getString(DELEGATE_DETAIL_INFO));
            }


            delegateAgendaIndexList = (ArrayList<Integer>) mBundle.getSerializable(DELEGATE_AGENDA_LIST);
            //从议程序号集合中，循环取得对应的AgendaModel
            List<AgendaModel> agendaModels = FileOperate.getInstance(this).queryAgendaList();
            if (agendaModels != null && agendaModels.size() > 0 && delegateAgendaIndexList != null && delegateAgendaIndexList.size() > 0) {
                mAgendaModelList = new ArrayList<>();
                for (AgendaModel agendaModel : agendaModels) {
                    for (int i = 0; i < delegateAgendaIndexList.size(); i++) {
                        if (delegateAgendaIndexList.get(i) == agendaModel.getAgendaIndex()){
                            mAgendaModelList.add(agendaModel);
                        }
                    }
                }
            }

            if (mAgendaModelList != null && mAgendaModelList.size() > 0) {
                //有主讲议程则显示，隐藏无主讲议程的提示
                mTvNoAgenda.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSpeakerAgendaAdapter = new SpeakerAgendaAdapter(DelegateDetailActivity.this, mAgendaModelList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(mSpeakerAgendaAdapter);

            } else {//显示无主讲议程的提示
                mTvNoAgenda.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 根据角色类型设置角色名称
     *
     * @param roleType 角色类型
     *                 Constant.KEYNOTE_SPEAKER  主讲人
     *                 Constant.HOST  主持人
     *                 Constant.NORMAL_DELEGATE  其他参会代表
     */
    private void setRoleByType(int roleType) {
        if (roleType == Constant.DEFAULT_SPEAKER) {//主讲人
            mIvRole.setImageResource(R.drawable.icon_speaker_selected);
            mTvDelegateRole.setText("主讲人");
        } else if (roleType == Constant.HOSTS) {//主持人
            mIvRole.setImageResource(R.drawable.icon_host_selected);
            mTvDelegateRole.setText("主持人");
        } else if (roleType == Constant.OTHER_DELEGATE) {//普通参会人员
            mIvRole.setImageResource(R.drawable.icon_delegate_selected);
            mTvDelegateRole.setText("其他参会代表");
        }
    }

    @Override
    public void onClick(View v) {
        //返回上一级
//        ActivityStackManager.pop();
        EventBus.getDefault().post(new Home());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityStackManager.pop();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
