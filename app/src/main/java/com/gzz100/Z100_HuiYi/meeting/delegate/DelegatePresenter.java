package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateRepository;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegatePresenter implements DelegateContract.Presenter {
    private final DelegateRepository mDelegateRepository;
    private final DelegateContract.View mDelegateView;

    private boolean IsFirstLoad=true;

    public DelegatePresenter (@NonNull DelegateRepository delegateRepository, DelegateContract.View delegateView) {
        this.mDelegateRepository = checkNotNull(delegateRepository,"delegateRepository cannot be null");
        this.mDelegateView = checkNotNull(delegateView,"delegateView cannot be null");
        mDelegateView.setPresenter(this);
    }
    @Override
    public void start() {
        fetchRoleList();
        fetchDelegateList(Constant.DEFAULT_SPEAKER);
        setDelegateSearchAutoCompleteTextViewHint();
    }

    @Override
    public void fetchRoleList() {

            mDelegateRepository.getRoleList(new DelegateDataSource.LoadRoleListCallback() {
                @Override
                public void onRoleListLoaded(List<String> roleList) {
                    mDelegateView.showRoleList(roleList);

                    if(IsFirstLoad){
                        mDelegateView.showRoleItemDecoration();
                    }

                }

                @Override
                public void onDataNotAvailable() {
                    mDelegateView.showNoRoleList();
                }
            });


    }

    private List<DelegateModel> currRoleDelegateBeanList=new ArrayList<>();
    @Override
    public void fetchDelegateList(int rolePos) {

            mDelegateRepository.getDelegateList(rolePos, new DelegateDataSource.LoadDelegateListCallback() {
                @Override
                public void onDelegateListLoaded(List<DelegateModel> delegateBeans) {
                    mDelegateView.showDelegateList(delegateBeans);
                    if(IsFirstLoad){
                        mDelegateView.showDelegateNameGridItemDecoration();
                    }
                    currRoleDelegateBeanList=delegateBeans;
                }

                @Override
                public void onDataNotAvailable() {
                    mDelegateView.showNoDelegateList();
                }
            });
    }


    @Override
    public void setFirstLoad(boolean reLoad) {
        IsFirstLoad=reLoad;
    }

    private List<DelegateModel> allDelegate=new ArrayList<>();
    @Override
    public void searchByName(String nameInput) {

        if(nameInput==null){
            return;
        }
        List<String> allDelegateName=new ArrayList<>();

        for(DelegateModel delegateBean:allDelegate)
        {
            allDelegateName.add(delegateBean.getDelegateName());
        }
        if(allDelegateName.contains(nameInput))
        {
            mDelegateView.showDelegateDetail(allDelegate.get(allDelegateName.indexOf(nameInput)));
        }
        else{
            //一些提示
            mDelegateView.showNoDelegateDetail();

        }
    }

    @Override
    public void showDelegateDetail( final int delegateNamePos) {

        DelegateModel delegateBean=currRoleDelegateBeanList.get(delegateNamePos);

        if(delegateBean!=null)
        {
            mDelegateView.showDelegateDetail(delegateBean);
        }
        else
            mDelegateView.showNoDelegateDetail();


    }

    @Override
    public void setDelegateSearchAutoCompleteTextViewHint() {
        mDelegateRepository.getDelegateNameHint(new DelegateDataSource.LoadDelegateNameHintCallback() {
            @Override
            public void onDelegateNameHintLoaded(List<DelegateModel> delegateBeanList) {
                mDelegateView.setAutoCompleteTextView(delegateBeanList);
                allDelegate=delegateBeanList;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }
}

