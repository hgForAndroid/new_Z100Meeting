package com.gzz100.Z100_HuiYi.meeting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.meeting.about.AboutFragment;
import com.gzz100.Z100_HuiYi.meeting.agenda.AgendaFragment;
import com.gzz100.Z100_HuiYi.meeting.agenda.AgendaPresenter;
import com.gzz100.Z100_HuiYi.meeting.delegate.DelegateFragment;
import com.gzz100.Z100_HuiYi.meeting.file.FileFragment;
import com.gzz100.Z100_HuiYi.meeting.file.FilePresenter;
import com.gzz100.Z100_HuiYi.meeting.meetingScenario.MeetingFragment;
import com.gzz100.Z100_HuiYi.meeting.vote.VoteFragment;
import com.gzz100.Z100_HuiYi.data.source.RepositoryUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener,ICommunicate {
    @BindView(R.id.id_main_tbv) NavBarView mNavBarView;
    @BindView(R.id.id_main_ViewPager) ViewPager mViewPager;

    @BindView(R.id.id_main_rdg) RadioGroup mTabGroup;
    @BindView(R.id.id_main_meetingTab) RadioButton mMeetingTab;
    @BindView(R.id.id_main_delegateTab) RadioButton mDelegateTab;
    @BindView(R.id.id_main_agendaTab) RadioButton mAgendaTab;
    @BindView(R.id.id_main_fileTab) RadioButton mFileTab;
    @BindView(R.id.id_main_aboutTab) RadioButton mAboutTab;
    @BindView(R.id.id_main_voteTab) RadioButton mVoteTab;

    private MainFragmentAdapter mMainFragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private MeetingFragment mMeetingFragment;
    private DelegateFragment mDelegateFragment;
    private AgendaFragment mAgendaFragment;
    private FileFragment mFileFragment;
    private VoteFragment mVoteFragment;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;
    public static final int PAGE_SIX = 5;
    private AboutFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mMeetingFragment = MeetingFragment.newInstance();
        mDelegateFragment = DelegateFragment.newInstance();
        mAgendaFragment =  AgendaFragment.newInstance();
        mFileFragment = FileFragment.newInstance();
        mAboutFragment = AboutFragment.newInstance();
        mVoteFragment = VoteFragment.newInstance();
        mFragments.add(mMeetingFragment);
        mFragments.add(mDelegateFragment);
        mFragments.add(mAgendaFragment);
        mFragments.add(mFileFragment);
        mFragments.add(mAboutFragment);

//        mFragments.add(mVoteFragment);

        mMainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mMainFragmentAdapter);
        mViewPager.setCurrentItem(PAGE_ONE);
        mNavBarView.mTvTitle.setText(mMeetingTab.getText());
        mMeetingTab.setChecked(true);
        initEvent();
        initPresenter();
    }

    private void initPresenter() {
        new FilePresenter(RepositoryUtil.getFileRepository(this.getApplicationContext()),
                mFileFragment);
        new AgendaPresenter(RepositoryUtil.getFileRepository(this.getApplicationContext()),
                mAgendaFragment);
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);
        mTabGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.id_main_meetingTab:
                mViewPager.setCurrentItem(PAGE_ONE);
                mNavBarView.mTvTitle.setText(mMeetingTab.getText());
                break;
            case R.id.id_main_delegateTab:
                mViewPager.setCurrentItem(PAGE_TWO);
                mNavBarView.mTvTitle.setText(mDelegateTab.getText());
                break;
            case R.id.id_main_agendaTab:
                mViewPager.setCurrentItem(PAGE_THREE);
                mNavBarView.mTvTitle.setText(mAgendaTab.getText());
                break;
            case R.id.id_main_fileTab:
                mViewPager.setCurrentItem(PAGE_FOUR);
                mNavBarView.mTvTitle.setText(mFileTab.getText());
                break;
            case R.id.id_main_aboutTab:
                mViewPager.setCurrentItem(PAGE_FIVE);
                mNavBarView.mTvTitle.setText(mAboutTab.getText());
                break;
//            case R.id.id_main_voteTab:
//                mViewPager.setCurrentItem(PAGE_SIX);
//                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2){
            switch (mViewPager.getCurrentItem()){
                case PAGE_ONE:
                    mMeetingTab.setChecked(true);
                    break;
                case PAGE_TWO:
                    mDelegateTab.setChecked(true);
                    break;
                case PAGE_THREE:
                    mAgendaTab.setChecked(true);
                    break;
                case PAGE_FOUR:
                    mFileTab.setChecked(true);
                    break;
                case PAGE_FIVE:
                    mAboutTab.setChecked(true);
                    break;
//                case PAGE_SIX:
//                    mMeetingTab.setChecked(true);
//                    break;

            }
        }

    }

    @Override
    public String getCurrentTitle() {
        return mNavBarView.mTvTitle.getText().toString();
    }
}
